#!/bin/bash

curl_cmd="curl --insecure"

. norc_key.sh
. rest_server.sh

# Get Batch List
${curl_cmd} --output batch_list_orig.xml \
	${base_url}mailing/listNorc?key=${rest_key}

tidy -xml -i batch_list_orig.xml > batch_list.xml

max_batch_id=`grep "<batch id='" batch_list.xml | sed -e "s/\s*<batch id='//" |sed -e "s/'>//" | sort -n | tail -n 1`

# Get Batch Detail
${curl_cmd} --output batch_show_orig.xml \
	${base_url}mailing/showNorc/${max_batch_id}?key=${rest_key}

tidy -xml -i batch_show_orig.xml > batch_show.xml
