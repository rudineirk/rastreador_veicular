# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from django.db import models
import json


class User(models.Model):
    class Meta:
        db_table = 'user'

    name = models.CharField(max_length=50)
    password = models.CharField(max_length=30)
    active = models.BooleanField()

    def __str__(self):
        return self.__unicode__()

    def __unicode__(self):
        return unicode(json.dumps({
            'name': self.name,
            'active': self.active,
            'id': self.id
        }, ensure_ascii=False))

    def login(self, name, password):
        try:
            user = User.objects.filter(name=name).get()
            if user.password == password:
                return True
            return False
        except User.DoesNotExist:
            return False


class LogLogin(models.Model):
    class Meta:
        db_table = 'log_login'
        ordering = ['timestamp']
    user = models.ForeignKey(User)
    timestamp = models.DateTimeField(auto_now=True)
    status = models.IntegerField()
    origin = models.CharField(max_length=100)

    def __str__(self):
        return self.__unicode__()

    def __unicode__(self):
        return unicode(json.dumps({
            'user': self.user,
            'timestamp': self.timestamp,
            'status': self.status,
            'origin': self.origin,
        }, ensure_ascii=False))


class Tracker(models.Model):
    class Meta:
        db_table = 'tracker'

    name = models.CharField(max_length=50)
    user = models.ForeignKey(User)

    def __str__(self):
        return self.__unicode__()

    def __unicode__(self):
        return unicode(json.dumps({
            'name': self.name,
            'user': self.user,
        }, ensure_ascii=False))


class Localization(models.Model):
    class Meta:
        db_table = 'localization'
        ordering = ['timestamp']

    pos_lat = models.FloatField()
    pos_long = models.FloatField()
    pos_alt = models.FloatField()
    velocity = models.FloatField()
    timestamp = models.DateTimeField(auto_now=True)
    tracker = models.ForeignKey(Tracker)

    def __str__(self):
        return self.__unicode__()

    def __unicode__(self):
        return unicode(json.dumps({
            'pos_lat': self.pos_lat,
            'pos_long': self.pos_long,
            'pos_alt': self.pos_alt,
            'timestamp': self.timestamp,
            'tracker': self.tracker.id,
        }, ensure_ascii=False))
