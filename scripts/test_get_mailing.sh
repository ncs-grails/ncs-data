#!/bin/bash

. norc_key.sh
. rest_server.sh

# Get Batch List
${curl_cmd} --output batch_list.xml \
	${base_url}mailing/listNorc?key=${rest_key}

# Get Batch Detail
${curl_cmd} --output batch_show.xml \
	${base_url}mailing/showNorc/35?key=${rest_key}
