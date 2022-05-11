from django.db import models
import boto3
import os
import logging
import bcrypt

from boto3.dynamodb.conditions import Key

USER_TABLE = os.environ['USER_TABLE']
AWS_REGION = os.environ['AWS_REGION']
AWS_ACCESS_KEY_ID = os.environ['AWS_ACCESS_KEY_ID']
AWS_SECRET_ACCESS_KEY = os.environ['AWS_SECRET_ACCESS_KEY']

logger = logging.getLogger(__name__)


def register_model(email, password):
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
        print(password_hashed)
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
            logger.error('Existing item updated to database.')
            return 409
        logger.error('New item added to database.')
    else:
        logger.error('Unknown error inserting item to database.')

    return status


# Create your models here.
def login_model(email, password):
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

