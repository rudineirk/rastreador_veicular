# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from django.http import HttpResponse
from rest_framework.decorators import api_view
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser

from .models import User, Tracker, Localization
from .serializers import UserSerializer, \
    TrackerSerializer, \
    LocalizationSerializer


class JSONResponse(HttpResponse):
    """
    An HttpResponse that renders its content into JSON.
    """
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)


@api_view(['GET', 'POST'])
def user_list(request):
    if request.method == "GET":
        users = User.objects.all()
        serializer = UserSerializer(users, many=True)
        return JSONResponse(serializer.data)
    elif request.method == "POST":
        data = JSONParser().parse(request)
        serializer = UserSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)


@api_view(['GET', 'PUT', 'DELETE'])
def user(request, pk=None):
    try:
        user = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = UserSerializer(user)
    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = UserSerializer(user, data=data)
        if not serializer.is_valid():
            return JSONResponse(serializer.errors, status=400)
        serializer.save()
    elif request.method == 'DELETE':
        user.delete()
        return HttpResponse(status=204)

    return JSONResponse(serializer.data)


@api_view(['POST'])
def login(request):
    data = JSONParser().parse(request)
    user = data.get('user', None)
    password = data.get('password', None)
    if user is None or password is None:
        return HttpResponse(status=400)

    try:
        user = User.objects.get(
            name=user,
            password=password,
            active=True,
        )
    except User.DoesNotExist:
        return HttpResponse(status=404)


    return JSONResponse(UserSerializer(user).data, status=200)


@api_view(['GET', 'POST'])
def tracker_list(request, user=None):
    if request.method == "GET":
        try:
            user = User.objects.get(name=user)
        except User.DoesNotExist:
            return HttpResponse(status=404)
        trackers = Tracker.objects.filter(user=user)
        serializer = TrackerSerializer(trackers, many=True)
        return JSONResponse(serializer.data)
    if request.method == "POST":
        data = JSONParser().parse(request)
        serializer = TrackerSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)


@api_view(['GET', 'PUT', 'DELETE'])
def tracker(request, pk=None):
    try:
        tracker = Tracker.objects.get(pk=pk)
    except Tracker.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = TrackerSerializer(tracker)
    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = TrackerSerializer(tracker, data=data)
        if not serializer.is_valid():
            return JSONResponse(serializer.errors, status=400)
        serializer.save()
    elif request.method == 'DELETE':
        tracker.delete()
        return HttpResponse(status=204)

    return JSONResponse(serializer.data)


@api_view(['GET', 'POST'])
def localization_list(request, serial=None):
    try:
        tracker = Tracker.objects.get(serial=serial)
    except Tracker.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == "GET":
        localizations = Localization.objects.filter(tracker=tracker)
        localizations = localizations.order_by('-timestamp')[:10]
        serializer = LocalizationSerializer(localizations, many=True)
        return JSONResponse(serializer.data)
    if request.method == "POST":
        data = JSONParser().parse(request)
        data['tracker'] = tracker.pk
        serializer = LocalizationSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)


@api_view(['GET', 'PUT', 'DELETE'])
def localization(request, pk=None):
    try:
        localization = Localization.objects.get(pk=pk)
    except Localization.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = LocalizationSerializer(localization)
    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = LocalizationSerializer(localization, data=data)
        if not serializer.is_valid():
            return JSONResponse(serializer.errors, status=400)
        serializer.save()
    elif request.method == 'DELETE':
        localization.delete()
        return HttpResponse(status=204)

    return JSONResponse(serializer.data)


@api_view(['GET'])
def movement(request, user=None, serial=None):
    try:
        user = User.objects.get(name=user)
        tracker = Tracker.objects.get(serial=serial, user=user)
        localization = Localization.objects.filter(tracker=tracker)
        localization = localization.order_by('-timestamp')[0]
    except (User.DoesNotExist,
            Tracker.DoesNotExist,
            Localization.DoesNotExist):
        return HttpResponse(status=404)

    resp = {
        "movement": False,
        "velocity": localization.velocity,
        "pos_lat": localization.pos_lat,
        "pos_long": localization.pos_long,
        "pos_alt": localization.pos_alt,
        "dt": localization.to_datestring(),
    }

    if localization.velocity > 20:
        resp['movement'] = True

    return JSONResponse(resp)
