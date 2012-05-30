#!/bin/bash

# host=secure.ncs.umn.edu
host=localhost:8443

. norc_key.sh
. rest_server.sh

${curl_cmd} --request POST \
	--header "Content-Type:text/xml" \
	--data @norc_test.xml \
	--sslv3 \
	--insecure \
	${base_url}formats/save?key=${norc_key}

