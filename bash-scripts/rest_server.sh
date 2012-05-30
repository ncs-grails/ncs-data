# Set this to the server you wish to send data to

base_url=https://localhost:8443/ncs-data/
#base_url=https://staging.ncs.umn.edu/ncs-data/
#base_url=https://secure.ncs.umn.edu/ncs-data/

# define the curl command
#curl_cmd="curl --insecure"
curl_cmd="curl --sslv3 --insecure"
