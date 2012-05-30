#!/bin/bash

. norc_key.sh
. rest_server.sh

if [ ! -d batch-repo ]; then
	mkdir batch-repo
fi

pushd batch-repo

if [ ! -e batch_list.xml ]; then
	# Get Batch List
	${curl_cmd} --output batch_list_orig.xml \
		${base_url}mailing/listNorc?key=${rest_key}

	# Re-Format the XML
	tidy -xml -i batch_list_orig.xml > batch_list.xml
	rm batch_list_orig.xml
fi

if [ ! -e batch_ids.txt ]; then
	# Get a list of all the batch IDs
	grep "<batch id='" batch_list.xml | awk -F \' '{print $2}' > batch_ids.txt
fi

echo -e "batch_id\t:\tpeices" > counts.txt
for batch_id in `cat  batch_ids.txt`; do
	echo "Fetching batch ${batch_id}..."
	
	if [ ! -e batch_show_${batch_id}.xml ]; then
		# Get Batch Detail
		${curl_cmd} --output batch_show_orig.xml \
			${base_url}mailing/showNorc/${batch_id}?key=${rest_key}

		tidy -xml -i batch_show_orig.xml > batch_show_${batch_id}.xml
		rm batch_show_orig.xml
	fi

	peices=`grep -c "<trackedItem " batch_show_${batch_id}.xml`
	
	echo -e "${batch_id}\t:\t${peices}" >> counts.txt

done


popd
