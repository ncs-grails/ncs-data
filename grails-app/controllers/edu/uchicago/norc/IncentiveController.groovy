package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import grails.converters.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.*

class IncentiveController {

	static allowedMethods = [ save:'POST', update:'PUT', show:'GET', delete:'DELETE' ]

	def regurgitate = {
		render "regurgitating response:\n${request.reader.text}"
	}

	def save = {

		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

		// dependency injection wasn't working here,
		// so I'll explicitly instantiate the service
		AccessService accessService = new AccessService()

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			def table

			try {
				table = request.XML
			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}

			table?.INCENTIVE_BATCH1?.each{ c ->

				def mailingId = c.MailingID.toString()

				def norcIncentive = NorcIncentive.findByMailingId(mailingId)

				if (! norcIncentive) {
					norcIncentive = new NorcIncentive()
					response <<  " + Creating new NorcIncentive(${mailingId})\n"
				} else {
					response <<  " ~ Updating existing NorcIncentive(${mailingId})\n"
				}


				norcIncentive.firstName = c.fName.toString()
				norcIncentive.lastName = c.lName.toString()
				norcIncentive.address1 = c.Address1.toString()
				norcIncentive.address2 = c.Address2.toString()
				norcIncentive.addressUnit = c.Unit.toString()
				norcIncentive.city = c.City.toString()
				norcIncentive.state = c.State.toString()
				norcIncentive.zipCode = c.Zip.toString()
				norcIncentive.zipFour = c.Zip4.toString()
				norcIncentive.norcSuId = c.SU_ID.toString()
				norcIncentive.mailingId = c.MailingID.toString()

				def incentiveAmountString = c.incentiveAmount.toString()
				try {
					if (incentiveAmountString.isBigDecimal()) {
						norcIncentive.incentiveAmount = incentiveAmountString.toBigDecimal()
					} else {
						response <<  "! Invalid incentiveAmount: ${c.incentiveAmount.toString()}.\n"
					}
				} catch (Exception e) {
					println "! parse incentiveAmount Exception: ${e.toString()}"
					response <<  "! Invalid incentiveAmount: ${c.incentiveAmount.toString()}.\n"
				}

				def incentiveMailed = c.incentiveMailed.toString()
				if (incentiveMailed == "1") {
					norcIncentive.incentiveMailed = true
				} else if (incentiveMailed == "0") {
					norcIncentive.incentiveMailed = false
				} else if ( ! incentiveMailed ) {
					norcIncentive.incentiveMailed = null
				} else {
					response <<  "! Unknown incentiveMailed: ${incentiveMailed}.  1 or 0 expect.\n"
				}



				// Read, parse, and lookup the docType
				def docTypeString = c.DocType.toString()
				try {
					if ( ! docTypeString ) {
						norcIncentive.docType = null
					} else {
						def docType = NorcDocType.findByValue(Integer.parseInt(docTypeString))
						if (docType) {
							norcIncentive.docType = docType
						} else {
							response <<  "! Unknown DocType: ${docTypeString}.  Please update FMTS.DOCTYPE first.\n"
						}
					}
				} catch (Exception e) {
					response << "! Invalid DocType: ${docTypeString}"
				}

				// Read, parse, and lookup the Source
				def sourceString = c.Source.toString()
				try {
					if ( ! sourceString ) {
						norcIncentive.source = null
					} else {
						def source = NorcSource.findByValue(Integer.parseInt(sourceString))
						if (source) {
							norcIncentive.source = source
						} else {
							response <<  "! Unknown Source: ${sourceString}.  Please update FMTS.SOURCE first.\n"
						}
					}
				} catch (Exception e) {
					response << "! Invalid Source: ${sourceString}"
				}

				// Read, parse, and lookup the Status
				def statusString = c.Status.toString()
				try {
					if ( ! statusString ) {
						norcIncentive.status = null
					} else {
						def status = NorcStatus.findByValue(Integer.parseInt(statusString))
						if (status) {
							norcIncentive.status = status
						} else {
							response <<  "! Unknown Status: ${statusString}.  Please update FMTS.STATUS first.\n"
						}
					}
				} catch (Exception e) {
					response << "! Invalid Status: ${statusString}\n"
					println "! parse Status Exception: ${e.toString()}"
				}

				// Read, parse, and lookup the Mode
				def modeString = c.Mode.toString()
				try {
					if ( ! modeString ) {
						norcIncentive.mode = null
					} else {
						def mode = NorcDocMode.findByValue(Integer.parseInt(modeString))
						if (mode) {
							norcIncentive.mode = mode
						} else {
							response <<  "! Unknown Mode: ${modeString}.  Please update FMTS.MODE first.\n"
						}
					}
				} catch (Exception e) {
					response << "! Invalid Mode: ${modeString}\n"
					println "! parse Mode Exception: ${e.toString()}"
				}

				// Incentive Date
				try {
					def incentiveDateString = c.DateIn.toString()
					if ( ! incentiveDateString ) {
						norcIncentive.incentiveDate = null
					} else {
						DateTime dt = fmt.parseDateTime(incentiveDateString)
						norcIncentive.incentiveDate = dt.toCalendar().getTime()
					}
				} catch (Exception e) {
					response << "! Invalid DateIn: ${c.DateIn.toString()}\n"
					println "! parse DateIn Input: ${c.DateIn.toString()}"
					println "! parse DateIn Exception: ${e.toString()}"
				}

				// App Time & Date
				try {
					def appTimeString = c.AppTime.toString()
					if ( ! appTimeString ) {
						norcIncentive.appTime = null
					} else {
						DateTime dt = fmt.parseDateTime(appTimeString)
						norcIncentive.appTime = dt.toCalendar().getTime()
					}
				} catch (Exception e) {
					response << "! Invalid AppTime: ${c.AppTime.toString()}\n"
					println "! parse AppTime Input: ${c.AppTime.toString()}"
					println "! parse AppTime Exception: ${e.toString()}"
				}

				// PS Date
				try {
					def pregnancyScreenerDateString = c.DatePS.toString()
					if ( ! pregnancyScreenerDateString ) {
						norcIncentive.pregnancyScreenerDate = null
					} else {
						DateTime dt = fmt.parseDateTime(pregnancyScreenerDateString)
						norcIncentive.pregnancyScreenerDate = dt.toCalendar().getTime()
					}
				} catch (Exception e) {
					response << "! Invalid DatePS: ${c.DatePS.toString()}\n"
					println "! parse DatePS Input: ${c.DatePS.toString()}"
					println "! parse DatePS Exception: ${e.toString()}"
				}


				norcIncentive.email = c.email.toString()
				norcIncentive.phoneNumber = c.phoneNumber.toString()
				try {
					norcIncentive.segmentId = Integer.parseInt(c.SegmentID.toString())
				} catch (Exception e) {
					response << "! Invalid SegmentID: ${c.SegmentID.toString()}\n"
				}

				// DONE SETTING VALUES, NOW WE SAVE.

				if (norcIncentive.hasErrors()) {
					response << "! consent has errors.\n"
				} else if (norcIncentive.save(flush:true)) {
					response << "= saved data\n"
				} else {
					response << "! unable to save data.\n"

					norcIncentive.errors.each{ e ->
						// println "norcIncentive:error::${e}"
						e.fieldErrors.each{ fe -> response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n" }
					}
				}
			}
		}
		render "save action finished.\n"
	}

	def update = {
		response.sendError(401)
		render "UPDATE not allowed, use POST\n"
	}

	def show = {
		response.sendError(401)
		render "SHOW not allowed\n"
	}

	def delete = {
		response.sendError(401)
		render "DELETE not allowed\n"
	}
}
