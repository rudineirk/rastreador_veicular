# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Localizacao',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, primary_key=True, verbose_name='ID')),
                ('posicao_lat', models.FloatField()),
                ('posicao_long', models.FloatField()),
                ('posicao_alt', models.FloatField()),
                ('timestamp', models.DateTimeField(auto_now=True)),
            ],
            options={
                'ordering': ['timestamp'],
                'db_table': 'localizacao',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='LogLogin',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, primary_key=True, verbose_name='ID')),
                ('timestamp', models.DateTimeField(auto_now=True)),
                ('status', models.IntegerField()),
                ('origem', models.CharField(max_length=100)),
            ],
            options={
                'ordering': ['timestamp'],
                'db_table': 'log_login',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Rastreador',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, primary_key=True, verbose_name='ID')),
                ('nome', models.CharField(max_length=50)),
            ],
            options={
                'db_table': 'rastreador',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Usuario',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, primary_key=True, verbose_name='ID')),
                ('nome', models.CharField(max_length=50)),
                ('senha', models.CharField(max_length=30)),
                ('ativo', models.BooleanField()),
            ],
            options={
                'db_table': 'usuario',
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='rastreador',
            name='usuario',
            field=models.ForeignKey(to='server.Usuario'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='loglogin',
            name='usuario',
            field=models.ForeignKey(to='server.Usuario'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='localizacao',
            name='rastreador',
            field=models.ForeignKey(to='server.Rastreador'),
            preserve_default=True,
        ),
    ]
