# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Localization',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('pos_lat', models.FloatField()),
                ('pos_long', models.FloatField()),
                ('pos_alt', models.FloatField()),
                ('velocity', models.FloatField()),
                ('timestamp', models.DateTimeField()),
            ],
            options={
                'ordering': ['timestamp'],
                'db_table': 'localization',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Tracker',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=100)),
                ('serial', models.CharField(max_length=50)),
            ],
            options={
                'db_table': 'tracker',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50)),
                ('password', models.CharField(max_length=30)),
                ('active', models.BooleanField()),
            ],
            options={
                'db_table': 'user',
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='tracker',
            name='user',
            field=models.ForeignKey(to='server.User'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='localization',
            name='tracker',
            field=models.ForeignKey(to='server.Tracker'),
            preserve_default=True,
        ),
    ]
