# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from django.shortcuts import render
from django.http import HttpResponse
import json

from .models import Usuario, Rastreador, Localizacao

RETORNOS = {
    'ok': [{'status': 'ok'}, 200],
    'nao_encontrado': [{'status': 'erro',
                        'erro': 'Não encontrado'}, 404],
    'parametros_invalidos': [{'status': 'erro',
                              'erro': 'Parâmetros inválidos'}, 400],
    'nao_modificado': [{'status': 'aviso',
                        'aviso': 'Dados não modificados'}, 304],
    'nao_implementado': [{'status': 'erro',
                          'erro': 'Não implementado'}, 501],
    'metodo_invalido': [{'status': 'erro',
                         'erro': 'Método inválido'}]
}


def get_json_data(req_body):
    try:
        req_json = json.loads(request.body)
    except ValueError:
        return None, RETORNOS['parametros_invalidos']

    if len(req_json.keys()) <= 0:
        return None, RETORNOS['parametros_invalidos']

    return req_json, None


def rastreador_get(request, pk=None):
    if pk is None:
        rastreadores = Rastreador.objects.all()
        if not rastreadores:
            return RETORNOS['nao_encontrado']

        rastreadores_data = []
        for rastreador in rastreadores:
            rastreadores_data.append({
                'id': rastreador.id,
                'nome': rastreador.nome,
                'id_usuario': rastreador.usuario,
            })
        return [{'rastreadores': rastreadores_data}, 200]

    try:
        rastreador = Rastreador.objects.get(id=pk)
    except Rastreador.DoesNotExist:
        return RETORNOS['nao_encontrado']

    return [{'id': rastreador.id,
             'nome': rastreador.nome,
             'id_usuario': rastreador.usuario}, 200]


def usuario_get(request, pk=None):
    if pk is None:
        usuarios = Usuario.objects.all()
        if not usuarios:
            return RETORNOS['nao_encontrado']
        usuarios_data = []
        for usuario in usuarios:
            usuarios_data.append({
                'id': usuario.id,
                'nome': usuario.nome,
                'ativo': usuario.ativo,
            })
        return [{'usuarios': usuarios_data}, 200]

    try:
        usuario = Usuario.objects.get(id=pk)
    except Usuario.DoesNotExist:
        return RETORNOS['nao_encontrado']

    return [{'id': usuario.id,
        'nome': usuario.nome,
        'ativo': usuario.ativo}, 200]


def usuario_post(request, pk=None):
    if pk is None:
        return RETORNOS['parametros_invalidos']

    req_json, status = get_json_data(request.body)
    if status is not None:
        return status

    try:
        usuario = Usuario.objects.get(id=pk)
    except Usuario.DoesNotExist:
        return RETORNOS['nao_encontrado']



    if usuario.nome == request.get('nome'):


    return [{'id': usuario.id,
        'nome': usuario.nome,
        'ativo': usuario.ativo}, 200]


def usuario(request, pk=None):
    if request.method == 'GET':
        data = usuario_get(request, pk)
    elif request.method == 'POST':
        data = RETORNOS['nao_implementado']
    elif request.method == 'DELETE':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'PUT':
        data = RETORNOS['metodo_invalido']
    else:
        data = RETORNOS['metodo_invalido']

    return HttpResponse(json.dumps(data[0], ensure_ascii=False), status=data[1])


def rastreador(request, pk=None):
    if request.method == 'GET':
        data = rastreador_get(request, pk)
    elif request.method == 'POST':
        data = RETORNOS['nao_implementado']
    elif request.method == 'DELETE':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'PUT':
        data = RETORNOS['nao_implementado']
    else:
        data = RETORNOS['metodo_invalido']

    return HttpResponse(json.dumps(data[0], ensure_ascii=False), status=data[1])


def localizacao(request, pk=None):
    if request.method == 'GET':
        data = RETORNOS['nao_implementado']
    elif request.method == 'POST':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'DELETE':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'PUT':
        data = RETORNOS['nao_implementado']
    else:
        data = RETORNOS['metodo_invalido']

    return HttpResponse(json.dumps(data[0], ensure_ascii=False), status=data[1])


def movimentacao(request, pk=None):
    if request.method == 'GET':
        data = RETORNOS['nao_implementado']
    elif request.method == 'POST':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'DELETE':
        data = RETORNOS['metodo_invalido']
    elif request.method == 'PUT':
        data = RETORNOS['metodo_invalido']
    else:
        data = RETORNOS['metodo_invalido']

    return HttpResponse(json.dumps(data[0], ensure_ascii=False), status=data[1])
