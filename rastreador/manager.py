#!/usr/bin/python
# -*- coding: utf8 -*-

import Queue
from threading import Thread
import time
from communication import EnvioPosicao
from gpsmonitor import MonitoraPosicao


SERIAL = 'A123456'
SERVER_ADDR = 'http://192.168.1.50:8000'

class RastreadorVeicular(object):
    def __init__(self):
        self.queue = Queue.Queue()
        self.monitora = MonitoraPosicao(self.queue)
        self.envio_pos = EnvioPosicao(
            self.queue,
            serial=SERIAL,
            server_addr=SERVER_ADDR,
        )

    def run(self):
        monitora_thread = Thread(target=self.monitora.verifica_gps)
        envio_gps_thread = Thread(target=self.envio_pos.envia_pos)
        monitora_thread.daemon = True
        envio_gps_thread.daemon = True
        monitora_thread.start()
        envio_gps_thread.start()

        while True:
            if not monitora_thread.is_alive():
                monitora_thread.run()
            if not envio_gps_thread.is_alive():
                envio_gps_thread.run()
            time.sleep(0.5)


def _main():
    rastreador_veicular = RastreadorVeicular()
    rastreador_veicular.run()


if __name__ == '__main__':
    _main()
