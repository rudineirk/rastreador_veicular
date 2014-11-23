from django.conf.urls import patterns, url

from . import views

urlpatterns = patterns(
    '',
    url(r'^tracker/$', views.tracker_list),
    url(r'^tracker/(?P<pk>\d+)/$', views.tracker),
    url(r'^localization/$', views.localization_list),
    url(r'^localization/(?P<pk>\d+)/$', views.localization),
    url(r'^localization/(?P<user>\w+)/(?P<tracker>\w+)/$', views.localization),
    url(r'^user/$', views.user_list),
    url(r'^user/(?P<pk>\d+)/$', views.user),
    url(r'^movement/(?P<user>\w+)/(?P<tracker>\w+)/$', views.movement),
)
