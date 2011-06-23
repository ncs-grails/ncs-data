package edu.umn.ncs.data

class AccessService {

    static transactional = true

	private def hasRoleAccess = { privateKey, ipAddress, roleName ->
		def now = new Date()
		def grantAccess = false
		def accessRoles = getAccessRoles(privateKey, ipAddress)

		if (accessRoles) {
			def readMailingRole = accessRoles.find{it.role == roleName}

			if (readMailingRole) {
				grantAccess = true
			} else {
				println "${now}\tncs-data\tInsufficent access to role ${roleName} for client host."
			}

		} else {
			println "${now}\tncs-data\tClient host ${ipAddress}."
		}
	}

	private def getAccessRoles = { privateKey, ipAddress ->
		def now = new Date()
		def roles = []

		if (privateKey) {
			def dataExchangePartnerInstance = DataExchangePartner.findByPrivateKey(privateKey)

			if (dataExchangePartnerInstance) {

				def clientHostInstance = dataExchangePartnerInstance.allowedClients.find { it.ipvFour == ipAddress || it.ipvSix == ipAddress }

				if (clientHostInstance) {
					println "${now}\tncs-data\tAccess granted from client host ${ipAddress} using ${dataExchangePartnerInstance} key."
					// load the roles
					roles = dataExchangePartnerInstance.roles
				} else {
					println "${now}\tncs-data\tHost Not Found:: Denied access from ${ipAddress} as ${dataExchangePartnerInstance} client host."
				}
				
			} else {
				println "${now}\tncs-data\tKey Not Found:: Denied access to ${ipAddress} using key: ${privateKey}"
			}

		}

		return roles
	}
}
