# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from rest_framework import serializers
from .models import User, Tracker, Localization


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'name', 'active')


class TrackerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tracker
        fields = (
            'id',
            'name',
            'user',
            'serial',
        )


class LocalizationSerializer(serializers.ModelSerializer):
    dt = serializers.Field(source='to_datestring')
    class Meta:
        model = Localization
        fields = (
            'id',
            'pos_lat',
            'pos_long',
            'pos_alt',
            'velocity',
            'timestamp',
            'tracker',
        )
