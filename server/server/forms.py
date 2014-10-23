# -*- coding: utf-8 -*-

from __future__ import unicode_literals

from django import forms
from .models import Usuario, Rastreador, Localizacao,

class UsuarioForm(forms.ModelForm):
    class Meta:
        model = Usuario
        fields = ['nome', 'senha', 'ativo']

class RastreadorForm(forms.ModelForm):
    class Meta:
        model = Rastreador
        fields = ['nome', 'usuario']

class LocalizacaoForm(forms.ModelForm):
    class Meta:
        model = Localizacao
