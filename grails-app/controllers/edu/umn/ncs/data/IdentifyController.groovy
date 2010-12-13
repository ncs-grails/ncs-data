package edu.umn.ncs.data

class IdentifyController {

    def index = {
		def dataExchangePartnerInstanceList = []
		def ipAddress = request.remoteAddr

		def clientHost = ClientHost.findByIpvFour(ipAddress)
		if ( ! clientHost ) {
			// try IPv6
			clientHost = ClientHost.findByIpvSix(ipAddress)
		}

		if (clientHost) {
			def c = DataExchangePartner.createCriteria()
			dataExchangePartnerInstanceList = c.list{
				allowedClients {
					eq("id", clientHost.id)
				}
			}
		}

		[ ipAddress: ipAddress, clientHost: clientHost,
			dataExchangePartnerInstanceList:dataExchangePartnerInstanceList ]
	}
}
