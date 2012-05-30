#!/bin/bash

. norc_key.sh
. rest_server.sh

${curl_cmd} --insecure \
	--request POST \
	--header "Content-Type:text/xml" \
	--sslv3 \
	--data @test_vdr.xml.gz \
	${base_url}vdr/?key=${norc_key}

