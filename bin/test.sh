#!/usr/bin/env bash

0 8-18/1 * * 1-5  flock -xn /home/test/esCron.lock -c /home/test/logstashShell/logstashjob.sh >> /home/test/logstashShell/run.log