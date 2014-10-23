from django.conf.urls import patterns, url

from . import views

urlpatterns = patterns('',
    url(r'^rastreador/$', views.rastreador),
    url(r'^rastreador/(?P<pk>\d+)/$', views.rastreador),
    url(r'^localizacao/$', views.localizacao),
    url(r'^localizacao/(?P<pk>\d+)/$', views.localizacao),
    url(r'^usuario/$', views.usuario),
    url(r'^usuario/(?P<pk>\d+)/$', views.usuario),
    url(r'^movimentacao/$', views.movimentacao),
    url(r'^movimentacao/(?P<pk>\d+)/$', views.movimentacao),
)
