#!/bin/bash

# the private key used to access the RESTful service
. norc_key.sh
. rest_server.sh

# If the "batch-repo" folder doesn't exist, create it.
if [ ! -d batch-repo ]; then
	mkdir batch-repo
fi

# Change our working directory to the batch-repo folder
pushd batch-repo

# Remove any existing batch_list.csv file
if [ -e batch_list.csv ]; then
	rm batch_list.csv
fi

# Download the latest batch_list.csv file
if [ ! -e batch_list.csv ]; then
	# Get Batch List
	${curl_cmd} --output batch_list.csv \
		${base_url}mailing/listCsv?key=${rest_key}

	# Get a list of all the batch IDs
	grep -v "batch_id" batch_list.csv | awk -F \, '{print $1}' > batch_ids.txt
fi

# Debug print statement
echo -e "batch_id\t:\tpeices" > counts.txt

# Loop through all the batches and...
for batch_id in `cat batch_ids.txt`; do
	
	# rm batch_show_${batch_id}.csv
	
	# If we don't have a copy of the batch contents, download one
	if [ ! -e batch_show_${batch_id}.csv ]; then
		echo "Fetching batch ${batch_id}..."
		# Get Batch Detail
		${curl_cmd} --output batch_show_${batch_id}.csv \
			${base_url}mailing/showCsv/${batch_id}?key=${rest_key}

	else
		echo "Skipping batch ${batch_id}..."
	fi

	# Count how many peeces are in the batch
	peices=`cat batch_show_${batch_id}.csv |wc -l`
	peices=$(( $peices - 1))
	
	# Write out the batch ID and peice count to counts.txt
	echo -e "${batch_id}\t:\t${peices}" >> counts.txt

done

# Return to the last directory we were in
popd


