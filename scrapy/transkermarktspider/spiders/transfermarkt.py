import scrapy
import unidecode
import re

cleanString = lambda x: '' if x is None else unidecode.unidecode(re.sub(r'\s+', ' ', x))


def change_money(money):
    if money:
        money = money.split("R")[1]
        if money[-1] == "m":
            money = float(money.split("m")[0])
            money = int(money * 1000000)
        elif money[-1] == ".":
            money = float(money.split("T")[0])
            money = int(money * 1000)
        elif money[-1] == "n":
            money = float(money.split("b")[0])
            money = int(money * 1000000000)
        return money
    return 0


class TransfermarktSpider(scrapy.Spider):
    name = 'transfermarkt'
    allowed_domains = ['www.transfermarkt.com']
    start_urls = ['https://www.transfermarkt.com/']

    def parse(self, response):
        championships = ["serie-a", "premier-league", "primera-division", "ligue-1", "bundesliga"]
        for championship in championships:
            for i in range(2010, 2022):
                link = ""
                if championship == "serie-a":
                    link = "https://www.transfermarkt.com/" + championship + "/startseite/wettbewerb/IT1/saison_id/" + str(i)
                elif championship == "premier-league":
                    link = "https://www.transfermarkt.com/" + championship + "/startseite/wettbewerb/GB1/saison_id/" + str(i)
                elif championship == "primera-division":
                    link = "https://www.transfermarkt.com/" + championship + "/startseite/wettbewerb/ES1/saison_id/" + str(i)
                elif championship == "ligue-1":
                    link = "https://www.transfermarkt.com/" + championship + "/startseite/wettbewerb/FR1/saison_id/" + str(i)
                elif championship == "bundesliga":
                    link = "https://www.transfermarkt.com/" + championship + "/startseite/wettbewerb/L1/saison_id/" + str(i)
                if link != "":
                    yield response.follow(link, callback=self.parse_squad_in_a_championship, dont_filter = True, meta={'year': i, 'championship': championship})
                pass

    def parse_squad_in_a_championship(self, response):
        a_table = response.css("table[class='items'] tbody")
        squads = a_table.css("tr[class='odd']") + a_table.css("tr[class='even']")
        for squad in squads:
            name_squad = cleanString(squad.css("td[class='hauptlink no-border-links'] a::text").extract_first())
            if(name_squad!=""):
                avg_age_set = squad.css("td[class='zentriert'] ::text").extract()
                number_players = avg_age_set[0]
                avg_age = avg_age_set[1]
                squad_value = cleanString(squad.css("td[class='rechts'] a::text").extract_first())
                money = change_money(squad_value)
                link_squad = squad.css("td[class='hauptlink no-border-links'] a::attr(href)").extract_first()
                if name_squad:
                    yield {
                        'squad_name': name_squad,
                        'avg_age': float(avg_age),
                        'squad_value': money,
                        'year': response.meta['year'],
                        'number_players': int(number_players),
                        'championship': response.meta['championship']
                    }
                    link = "https://www.transfermarkt.com" + link_squad
                    yield response.follow(link,
                                          callback=self.parse_player_in_a_team, dont_filter = True,
                                          meta={'squad_name': name_squad, 'year': response.meta['year'], 'championship': response.meta['championship']})
                pass

    def parse_player_in_a_team(self, response):
        a_table = response.css("tbody")
        players = a_table.css("tr[class='odd']") + a_table.css("tr[class='even']")
        for player in players:
            name_player = cleanString(player.css("span[class='hide-for-small'] a::attr(title)").extract_first())
            role_extract = player.css("tr td::text").extract()
            role = role_extract[0]
            try:
                birthday = int(player.css("td[class ='zentriert'] ::text").extract_first().split("(")[1].split(")")[0])
            except:
                birthday = -1
            value_player = cleanString(player.css("td[class='rechts hauptlink'] a::text").extract_first())
            money = change_money(value_player)
            if(money==0):
                money=-1
            link_player = player.css("span[class='hide-for-small'] a::attr(href)").extract_first()
            name_link = link_player.split("/")[1]
            id_link = link_player.split("/")[4]
            create_link = "https://www.transfermarkt.com/" + name_link + "/leistungsdatendetails/spieler/" + id_link + "/plus/0?saison=" + \
                          str(response.meta['year']) + "&verein=&liga=&wettbewerb=&pos=&trainer_id="
            if name_player:
                yield response.follow(create_link,
                                      callback=self.parse_bio_player, dont_filter = True,
                                      meta={'player_name': name_player, 'age': birthday, 'role': role,
                                            'value_player': money, 'squad_name': response.meta['squad_name'],
                                            'year': response.meta['year'], 'championship': response.meta['championship']})
            pass

    def parse_bio_player(self, response):
        a_table = response.css("table[class='items'] tfoot")
        if a_table:
            games_played = 0
            minute_played = 0
            goals = 0
            assists = 0
            minute = 0
            stats = a_table.css("td[class='zentriert'] ::text").extract()
            minute_played_t = a_table.css("td[class='rechts'] ::text").extract()

            try:
                minute = int(re.sub("[^0-9]", "", minute_played_t[1]))
            except:
                minute= -1
            games_played_t = 0
            goals_t = 0
            assists_t = 0
            goals_conceded = 0
            clean_sheets = 0
            if stats[0] != '-' and stats[0] != '-/-/-':
                games_played_t = int(stats[0])
            if response.meta['role'] != "Goalkeeper":
                if stats[1] != '-' and stats[1] != '-/-/-':
                    goals_t = int(stats[1])
                if stats[2] != '-' and stats[2] != '-/-/-':
                    assists_t = int(stats[2])
            else:
                if stats[3] != '-' and stats[3] != '-/-/-':
                    goals_conceded = int(stats[3])
                if stats[4] != '-' and stats[4] != '-/-/-':
                    clean_sheets = int(stats[4])

            if games_played_t > 0:
                games_played = games_played + games_played_t
            if goals_t > 0:
                goals = goals + goals_t
            if assists_t > 0:
                assists = assists + assists_t
            if minute > 0:
                minute_played = minute_played + minute
            if response.meta['role'] == "Goalkeeper":
                yield {
                    'name_player': response.meta['player_name'],
                    'age': response.meta['age'],
                    'role': response.meta['role'],
                    'value_player': response.meta['value_player'],
                    'squad_name': response.meta['squad_name'],
                    'year': response.meta['year'],
                    'games_played': games_played,
                    'goals_conceded': goals_conceded,
                    'clean_sheets': clean_sheets,
                    'minute_played': minute_played,
                    'championship': response.meta['championship'],
                }
            else:
                yield {
                    'name_player': response.meta['player_name'],
                    'age': response.meta['age'],
                    'role': response.meta['role'],
                    'value_player': response.meta['value_player'],
                    'squad_name': response.meta['squad_name'],
                    'year': response.meta['year'],
                    'games_played': games_played,
                    'goals': goals,
                    'assists': assists,
                    'minute_played': minute_played,
                    'championship': response.meta['championship'],
                }
            pass
