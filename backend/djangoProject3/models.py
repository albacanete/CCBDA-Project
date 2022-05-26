from django.db import models
import boto3
import os
import logging
import bcrypt
import uuid

from boto3.dynamodb.conditions import Key
"""
USER_TABLE = os.environ['USER_TABLE']
AWS_REGION = os.environ['AWS_REGION']
AWS_ACCESS_KEY_ID = os.environ['AWS_ACCESS_KEY_ID']
AWS_SECRET_ACCESS_KEY = os.environ['AWS_SECRET_ACCESS_KEY']
"""
logger = logging.getLogger(__name__)

class User(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    email = models.EmailField()
    password = models.CharField(max_length=100)

    def register(self, email, password):
        errors = []
        try:
            dynamodb = boto3.resource('dynamodb', region_name=AWS_REGION, aws_access_key_id=AWS_ACCESS_KEY_ID,
                                      aws_secret_access_key=AWS_SECRET_ACCESS_KEY)
            table = dynamodb.Table(USER_TABLE)
        except Exception as e:
            logger.error(
                'Error connecting to database table: ' + (e.fmt if hasattr(e, 'fmt') else '') + ','.join(e.args))
            return 403

        try:

            password_hashed = password.encode('utf-8')
            # Generate salt
            mySalt = bcrypt.gensalt()

            # Hash password
            hash = bcrypt.hashpw(password_hashed, mySalt)
            response = table.put_item(
                Item={
                    'email': email,
                    'password': hash.decode('utf-8'),
                },
                ReturnValues='ALL_OLD',
            )
        except Exception as e:
            logger.error(
                'Error adding item to database: ' + (e.fmt if hasattr(e, 'fmt') else '') + ','.join(e.args))
            return 403

        status = response['ResponseMetadata']['HTTPStatusCode']
        if status == 200:
            if 'Attributes' in response:
                errors.append('User already registered!')

            logger.error('New item added to database.')
        else:
            logger.error('Unknown error inserting item to database.')

        return errors

    # Create your models here.
    def login(self, email, password):
        try:
            dynamodb = boto3.resource('dynamodb', region_name=AWS_REGION, aws_access_key_id=AWS_ACCESS_KEY_ID,
                                      aws_secret_access_key=AWS_SECRET_ACCESS_KEY)
            table = dynamodb.Table(USER_TABLE)
        except Exception as e:
            logger.error(
                'Error connecting to database table: ' + (e.fmt if hasattr(e, 'fmt') else '') + ','.join(e.args))
            return 403

        try:
            # Get user
            response = table.get_item(
                Key={
                    'email': email
                }
            )

            password_hashed = password.encode('utf-8')
            return bcrypt.checkpw(password_hashed, response['Item']['password'].encode('utf-8'))

        except Exception as e:
            print(e)

class Parameters(models.Model):
    #id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    pass

class Request(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    datetime = models.DateTimeField()
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    #parameter_id = models.ForeignKey()

class League(models.Model):
    id_league = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=50)

class Team(models.Model):
    id_team = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=50)

class Player(models.Model):
    id_player = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=50)

class Team_Status(models.Model):
    id_team = models.ForeignKey(Team, on_delete=models.CASCADE())
    id_league = models.ForeignKey(League, on_delete=models.CASCADE())
    year = models.PositiveIntegerField()
    value_team = models.PositiveBigIntegerField()
    number_players = models.PositiveIntegerField()

    class Meta:
        constraints = [
            models.UniqueConstraint(
                fields=['idTeam', 'year'], name='unique_idTeam_year_combination'
            )
        ]


class Player_Status(models.Model):
    id_stats_player = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    id_player = models.ForeignKey(Player, on_delete=models.CASCADE())
    id_team = models.ForeignKey(Team, on_delete=models.CASCADE())
    value_player = models.PositiveBigIntegerField()
    year = models.PositiveIntegerField()
    role = models.CharField(max_length=100)

    class Meta:
        constraints = [
            models.UniqueConstraint(
                fields=['id_player', 'year', 'id_team'], name='unique_idPlayer_year_idTeam_combination'
            )
        ]


class Stats_Player(models.Model):
    id_stats_player= models.ForeignKey(Player_Status, on_delete=models.CASCADE())
    year = models.PositiveIntegerField()
    games = models.PositiveIntegerField()
    goals = models.PositiveIntegerField()
    assists = models.PositiveIntegerField()
    minutes = models.PositiveIntegerField()
    class Meta:
        constraints = [
            models.UniqueConstraint(
                fields=['id_stats_player', 'year'], name='unique_idStatsPlayer_year_idTeam_combination'
            )
        ]
