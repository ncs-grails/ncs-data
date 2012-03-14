package edu.umn.ncs.data

import org.joda.time.*
import org.joda.time.contrib.hibernate.*


class EmailNotifyService {

	static transactional = true

	def mailService

	def notifyOfNorcUpload(clientAddress) {
		def norcPartner = DataExchangePartner.findByName('NORC')
		notifyOfUpload(clientAddress, norcPartner, [:])
	}

	def notifyOfUpload(clientAddress, dataExchangePartner, dataDetails) {
		def recipients = [ 'ajz@umn.edu'
		, 'ast@umn.edu'
		, 'jaf@umn.edu'
		, 'msg@umn.edu' ]

		if (dataExchangePartner.contactEmail) {
			recipients.add dataExchangePartner.contactEmail
		}

		def referenceDate = new Date()

		mailService.sendMail {
			to recipients.toArray()
			from "help@ncs.umn.edu"
			subject "Notification of ${dataExchangePartner.name} Data Upload ${referenceDate}"
			body( view:"/combined/notify",
					model:[ referenceDate: referenceDate, 
						clientAddress: clientAddress,
						dataExchangePartner: dataExchangePartner,
						dataDetails: dataDetails ] )
		}
	}
}
