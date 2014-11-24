# -*- coding: utf8 -*-

import json
import requests
import time
from requests.exceptions import ConnectionError, Timeout

class EnvioPosicao(object):
    def __init__(self, queue, serial='', server_addr=''):
        self.queue = queue
        self.server_addr = server_addr
        self.serial = serial
        self.url = self.get_url()

    def envia_pos(self):
        while True:
            data = self.queue.get()
            data = {
                'pos_alt': data.altitude,
                'pos_long': data.longitude,
                'pos_lat': data.latitude,
                'velocity': data.velocidade,
                'timestamp': data.timestamp.strftime('%Y-%m-%d %H:%M:%S'),
            }
            try:
                response = requests.post(self.url, data=json.dumps(data))
                if not response.ok:
                    print('Failed ({0}): {1}\n'.format(
                        response.status_code,
                        response.text,
                    ))
            except (ConnectionError, Timeout):
                print('Failed: Connection Error')
            time.sleep(0.01)

    def get_url(self):
        url = '/'.join([
            self.server_addr,
            'api/localization',
            self.serial,
        ])
        return url + '/'
