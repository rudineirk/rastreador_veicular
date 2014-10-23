# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from django.db import models


class Usuario(models.Model):
    class Meta:
        db_table = 'usuario'

    nome = models.CharField(max_length=50)
    senha = models.CharField(max_length=30)
    ativo = models.BooleanField()

    def __str__(self):
        return self.nome

    def __unicode__(self):
        return unicode(self.nome)


class LogLogin(models.Model):
    class Meta:
        db_table = 'log_login'
        ordering = ['timestamp']
    usuario = models.ForeignKey(Usuario)
    timestamp = models.DateTimeField(auto_now=True)
    status = models.IntegerField()
    origem = models.CharField(max_length=100)


class Rastreador(models.Model):
    class Meta:
        db_table = 'rastreador'

    nome = models.CharField(max_length=50)
    usuario = models.ForeignKey(Usuario)


class Localizacao(models.Model):
    class Meta:
        db_table = 'localizacao'
        ordering = ['timestamp']

    posicao_lat = models.FloatField()
    posicao_long = models.FloatField()
    posicao_alt = models.FloatField()
    timestamp = models.DateTimeField(auto_now=True)
    rastreador = models.ForeignKey(Rastreador)
