from django.core.management.base import BaseCommand
from djangoProject3.models import Player_Status
from djangoProject3.models import Team_Status

import csv
import os


class Command(BaseCommand):

    def handle(self, *args, **options):

        # Get the path to the data folder
        data_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data")

        with open(os.path.join(data_path, 'squads.csv'), 'r') as f:
            teams = Team_Status.objects.bulk_create(map(lambda row: Team_Status(**row), csv.DictReader(f)))

        print(f"Team_Status table populated: {len(teams)} rows")

        def parsePlayerRow(row: dict) -> dict:
            for san in ["goalsConceded", "goals", "assists", "cleanSheets"]:
                if row[san] == "":
                    row[san] = 0
            return Player_Status(**row)

        with open(os.path.join(data_path, 'players.csv'), 'r') as f:
            players = Player_Status.objects.bulk_create(map(parsePlayerRow, csv.DictReader(f)))

        print(f"Player_Status table populated: {len(players)} rows")

