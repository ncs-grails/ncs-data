#!/bin/bash

. norc_key.sh
. rest_server.sh

#curl https://secure.ncs.umn.edu/ncs-data/combined/textXmlParser?key=${norc_key} \
#curl https://secure.ncs.umn.edu/ncs-data/combined/?key=${norc_key} \
#	--cacert "secure_ncs_umn_edu_interim.cer" \
#	--request POST --header "Content-Type:text/xml" \
#	--data @"data_transmission_30JUN11.xml" \
#	--output "results.html"

#curl --insecure \
#	--request POST \
#	--header "Content-Type:text/xml" \
#	--data @transmissionX_1_14APR11.xml \
#	${base_url}combined/?key=${norc_key}
	
#curl --insecure \
#	--request POST \
#	--header "Content-Type:text/xml" \
#	--data @ramsey_nonresponse_list_17JUN11.xml \
#	${base_url}combined/?key=${norc_key}

#curl --insecure \
#        --request POST \
#        --header "Content-Type:text/xml" \
#        --data @data_transmission_22JUN11.xml \
#        ${base_url}combined/?key=${norc_key}

#curl --insecure \
#        --request POST \
#        --header "Content-Type:text/xml" \
#        --data @data_transmission_30JUN11.xml \
#        ${base_url}combined/?key=${norc_key}

#curl --insecure \
#        --request POST \
#        --header "Content-Type:text/xml" \
#        --data @data_transmission_07sep11_temp.xml \
#        ${base_url}combined/?key=${norc_key}

#curl --insecure \
#        --request POST \
#        --header "Content-Type:text/xml" \
#        --data @data_transmission_16SEP11_1.xml \
#        ${base_url}combined/?key=${norc_key}

${curl_cmd} --insecure \
        --request POST \
        --header "Content-Type:text/xml" \
		--sslv3 \
        --data @data_transmission_10OCT11.xml \
        ${base_url}combined/?key=${norc_key}

#curl --insecure \
#        --request POST \
#        --header "Content-Type:text/xml" \
#        --data @ramsey_nonresponse_list_16SEP11.xml \
#        ${base_url}combined/?key=${norc_key}

