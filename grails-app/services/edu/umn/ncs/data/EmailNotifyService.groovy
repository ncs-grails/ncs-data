package edu.umn.ncs.data

import org.joda.time.*
import org.joda.time.contrib.hibernate.*


class EmailNotifyService {

	static transactional = true

	def mailService


	def notifyOfNorcUpload(clientAddress) {

		if (clientAddress != '0:0:0:0:0:0:0:1' || clientAddress != '127.0.0.1') {
			def recipients = [ 'ajz@umn.edu'
			, 'ast@umn.edu'
			, 'jaf@umn.edu'
			, 'msg@umn.edu'
			, 'ngp@umn.edu' ]

			def referenceDate = new Date()

			mailService.sendMail {
				to recipients.toArray()
				from "help@ncs.umn.edu"
				subject "Notification of NORC Data Upload ${referenceDate}"
				body( view:"/combined/notify",
						model:[ referenceDate: referenceDate, clientAddress: clientAddress ] )
			}
		}
	}
}
