from scrapy import cmdline

cmdline.execute("scrapy crawl transfermarkt -o transfermarkt.json".split())