from django.conf.urls import patterns, url

from . import views

urlpatterns = patterns(
    '',
    url(r'^tracker/(?P<user>\w+)/$', views.tracker_list),
    url(r'^tracker/(?P<pk>\d+)/$', views.tracker),
    url(r'^localization/$', views.localization_list),
    url(r'^localization/(?P<pk>\d+)/$', views.localization),
    url(r'^localization/(?P<serial>\w+)/$', views.localization_list),
    url(r'^user/$', views.user_list),
    url(r'^user/(?P<pk>\d+)/$', views.user),
    url(r'^login/$', views.login),
    url(r'^movement/(?P<user>\w+)/(?P<serial>\w+)/$', views.movement),
)
