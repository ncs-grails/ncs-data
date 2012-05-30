#!/bin/bash

. norc_key.sh
. rest_server.sh

${curl_cmd} --insecure \
	--request POST \
	--header "Content-Type:text/xml" \
	--sslv3 \
	--data @norc_test.xml \
	${base_url}combined/textXmlParser?key=${norc_key}

