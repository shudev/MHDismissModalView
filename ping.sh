#!/bin/bash

PING=/bin/ping
AWK=/usr/bin/awk
COUNT=4
DEADLINE=10

ping_host() {
    local output=$(ping -c $COUNT  $1 2>&1)
    echo $output
    # notice $output is quoted to preserve newlines
    local temp=$(echo "$output"| AWK '
        BEGIN { pl=100; rtt=0.5 }
        /packets transmitted/ {
            match($0, /(\S)% packet loss/)
            pl=substr($0, RSTART, RLENGTH)
            rtt=0.3
        }
        /unknown host/  {
            # no output at all means network is probably down
            pl=90
            rtt=0.1
        }
        END { print pl ":" rtt } ')
    RETURN_VALUE=$temp
}

# ping a host on the local lan
ping_host 191.168.11.7

#/usr/bin/rrdtool update \
#    /www/htdocs/rrd/logs/network_stats/ping_lan.rrd \
##    --template \
 #   pl:rtt \
 #   N:$RETURN_VALUE

# ping a host on the internal network
#ping_host www.utoronto.ca
echo $RETURN_VALUE

#/usr/bin/rrdtool update \
#    /www/htdocs/rrd/logs/network_stats/ping_internal.rrd \
#    --template \
#    pl:rtt \
#    N:$RETURN_VALUE

# ping a host on the external network
ping_host www.google.com
echo $RETURN_VALUE

#/usr/bin/rrdtool update \
#    /www/htdocs/rrd/logs/network_stats/ping_external.rrd \
#    --template \
#    pl:rtt \
#    N:$RETURN_VALUE

