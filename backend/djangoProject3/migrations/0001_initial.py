# Generated by Django 4.0 on 2022-05-26 15:09

from django.db import migrations, models
import django.db.models.deletion
import uuid


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='GoalKeeper',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=256)),
                ('age', models.IntegerField()),
                ('role', models.CharField(max_length=100)),
                ('value', models.BigIntegerField()),
                ('squad', models.CharField(max_length=100)),
                ('year', models.PositiveIntegerField()),
                ('games', models.PositiveIntegerField()),
                ('goals_conceded', models.PositiveIntegerField()),
                ('clean_sheets', models.PositiveIntegerField()),
                ('minutes', models.PositiveIntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='League',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=30)),
                ('country', models.CharField(max_length=30)),
            ],
        ),
        migrations.CreateModel(
            name='Parameters',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
            ],
        ),
        migrations.CreateModel(
            name='Player',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=256)),
                ('age', models.IntegerField()),
                ('role', models.CharField(max_length=100)),
                ('value', models.BigIntegerField()),
                ('squad', models.CharField(max_length=100)),
                ('year', models.PositiveIntegerField()),
                ('games', models.PositiveIntegerField()),
                ('goals', models.PositiveIntegerField()),
                ('assists', models.PositiveIntegerField()),
                ('minutes', models.PositiveIntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='Team',
            fields=[
                ('name', models.CharField(max_length=30, primary_key=True, serialize=False)),
                ('avg_age', models.PositiveIntegerField()),
                ('squad_value', models.PositiveBigIntegerField()),
                ('year', models.PositiveIntegerField()),
                ('number_players', models.PositiveIntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('email', models.EmailField(max_length=254)),
                ('password', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='Request',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('datetime', models.DateTimeField()),
                ('user_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='djangoProject3.user')),
            ],
        ),
        migrations.AddConstraint(
            model_name='player',
            constraint=models.UniqueConstraint(fields=('name', 'squad'), name='players_constraint'),
        ),
    ]
