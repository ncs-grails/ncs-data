#!/bin/bash

. norc_key.sh
. rest_server.sh
payload=$1

if [ ! -f "${payload}" ]; then
	echo "Usage:"
	echo "	$0 <zip file to upload>"
else
	
	echo "Pre-HTTP-POST MD5 Sum:"
	md5sum ${payload}

	echo "Posting data..."
	curl --insecure \
		--request POST \
		--header "Content-Type:application/zip" \
		--sslv3 \
		--data-binary @${payload} \
		${base_url}vdr/?key=${umn_key}
fi
