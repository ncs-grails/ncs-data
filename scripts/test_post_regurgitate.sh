#!/bin/bash

. norc_key.sh
. rest_server.sh

curl --insecure \
	--request POST \
	--header "Content-Type:text/xml" \
	--sslv3 \
	--data @norc_test.xml \
	${base_url}formats/regurgitate

