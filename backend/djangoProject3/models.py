from django.db import models
import boto3
import os
import logging
import bcrypt
import uuid

from boto3.dynamodb.conditions import Key

USER_TABLE = os.environ['USER_TABLE']
AWS_REGION = os.environ['AWS_REGION']
AWS_ACCESS_KEY_ID = os.environ['AWS_ACCESS_KEY_ID']
AWS_SECRET_ACCESS_KEY = os.environ['AWS_SECRET_ACCESS_KEY']

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
    name = models.CharField(max_length=30)
    country = models.CharField(max_length=30)

class Team(models.Model):
    name = models.CharField(primary_key=True, max_length=30)
    avg_age = models.PositiveIntegerField()
    squad_value = models.PositiveBigIntegerField()
    year = models.PositiveIntegerField()

class Players(models.Model):
    name = models.CharField(max_length=30)
    age = models.IntegerField()
    role = models.CharField(max_length=30)
    value = models.PositiveBigIntegerField()
    squad = models.CharField(max_length=30)
    year = models.PositiveIntegerField()
    games = models.PositiveIntegerField()
    goals = models.PositiveIntegerField()
    assists = models.PositiveIntegerField()
    minutes = models.PositiveIntegerField()

    # Make two unique fields in the table
    class Meta:
        constraints = [
            models.UniqueConstraint(fields=['name', 'squad'], name='players_constraint')
        ]

