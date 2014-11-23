# -*- coding: utf8 -*-

from datetime import datetime
from math import radians, cos, sin, asin, sqrt
from pynmea import nmea
import time
import serial


class PosicaoGPS(object):
    def __init__(self, gpsdata):
        self.longitude = float(gpsdata.longitude)
        self.latitude = float(gpsdata.latitude)
        self.altitude = int(gpsdata.antenna_altitude)
        self.velocidade = 0.0
        self.timestamp = datetime.now()

    def calc_velocidade(self, pos_gps):
        if pos_gps is None:
            return

        # formula de haversine
        rad_lat1 = radians(pos_gps.latitude)
        rad_lat2 = radians(self.latitude)
        rad_long1 = radians(pos_gps.longitude)
        rad_long2 = radians(self.longitude)

        diff_long = rad_long2 - rad_long1
        diff_lat = rad_lat2 - rad_lat1
        a = sin(diff_lat/2)**2 + cos(rad_lat1) * \
            cos(rad_lat2) * sin(diff_long/2)**2
        c = 2 * asin(sqrt(a))
        km = 6367 * c

        timediff = self.timestamp - pos_gps.timestamp
        timediff = timediff.total_seconds() / (60**2)

        self.velocidade = km / timediff


class GPS(object):
    def __init__(self, porta="/dev/ttyAMA0", baudrate=9600, timeout=2):
        self.porta = porta
        self.baudrate = baudrate
        self.timeout = timeout
        #self.porta = self.__init_porta__()
        self.porta = None
        self.gpsdata = nmea.GPGGA()
        self.old_pos = None

    def __init_porta__(self):
        porta = serial.Serial(
            self.porta,
            baudrate=self.baudrate,
            timeout=self.timeout
        )
        if porta.isOpen() is False:
            porta.open()

        porta.flushInput()
        porta.flushOutput()
        return porta

    def verificaGpsPronto(self):
        return True

    def lerPosicao(self):
        self.gpsdata.parse('$GPGGA,064746.000,4925.4895,N,00103.9255,E,1,05,2.1,-68.0,M,47.1,M,,0000*4F')
        gps_pos = PosicaoGPS(self.gpsdata)
        gps_pos.calc_velocidade(self.old_pos)
        self.old_pos = gps_pos
        return gps_pos


class MonitoraPosicao(object):
    def __init__(self, queue):
        self.queue = queue
        self.gps = GPS()

    def verifica_gps(self):
        while True:
            if not self.gps.verificaGpsPronto():
                time.sleep(1)
                continue

            self.queue.put(self.gps.lerPosicao())
            time.sleep(2)
