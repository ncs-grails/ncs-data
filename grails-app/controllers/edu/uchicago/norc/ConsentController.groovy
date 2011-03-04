package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import grails.converters.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.*

class ConsentController {

	static allowedMethods = [ save:'POST', update:'PUT', show:'GET', delete:'DELETE' ]

	def regurgitate = {
		response << "regugitating response:\n${request.reader.text}"
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
			table?.CONSENT_BATCH1?.each{ c ->
				
				def mailingId = c.MailingID.toString()
				
				def norcConsent = NorcConsent.findByMailingId(mailingId)
				
				if (! norcConsent) {
					norcConsent = new NorcConsent()
					response <<  " + Creating new NorcConsent(${mailingId})\n"
				} else {
					response <<  " ~ Updating existing NorcConsent(${mailingId})\n"
				}					
				
				
				norcConsent.firstName = c.fName.toString()
				norcConsent.lastName = c.lName.toString()
				norcConsent.address1 = c.Address1.toString()
				norcConsent.address2 = c.Address2.toString()
				norcConsent.addressUnit = c.Unit.toString()
				norcConsent.city = c.City.toString()
				norcConsent.state = c.State.toString()
				norcConsent.zipCode = c.Zip.toString()
				norcConsent.zipFour = c.Zip4.toString()
				norcConsent.norcSuId = c.SU_ID.toString()
				norcConsent.mailingId = c.MailingID.toString()
				norcConsent.email = c.email.toString()
				norcConsent.phoneNumber = c.phoneNumber.toString()

				// Read, parse, and lookup the docType
				def docTypeString = c.DocType.toString()
				try {
					if ( ! docTypeString ) {
						norcConsent.docType = null
					} else {
						def docType = NorcDocType.findByValue(Integer.parseInt(docTypeString))
						if (docType) {
							norcConsent.docType = docType
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
						norcConsent.source = null
					} else {
						def source = NorcSource.findByValue(Integer.parseInt(sourceString))
						if (source) {
							norcConsent.source = source
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
						norcConsent.status = null
					} else {
						def status = NorcStatus.findByValue(Integer.parseInt(statusString))
						if (status) {
							norcConsent.status = status
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
						norcConsent.mode = null
					} else {
						def mode = NorcDocMode.findByValue(Integer.parseInt(modeString))
						if (mode) {
							norcConsent.mode = mode
						} else {
							response <<  "! Unknown Mode: ${modeString}.  Please update FMTS.MODE first.\n"
						}
					}
				} catch (Exception e) {
					response << "! Invalid Mode: ${modeString}\n"
					println "! parse Mode Exception: ${e.toString()}"
				}

				// PS Date
				try {
					def pregnancyScreenerDateString = c.DatePS.toString()
					if ( ! pregnancyScreenerDateString ) {
						norcConsent.pregnancyScreenerDate = null
					} else {
						DateTime dt = fmt.parseDateTime(pregnancyScreenerDateString)
						norcConsent.pregnancyScreenerDate = dt.toCalendar().getTime()
					}
				} catch (Exception e) {
					response << "! Invalid DatePS: ${c.DatePS.toString()}\n"
					println "! parse DatePS Input: ${c.DatePS.toString()}"
					println "! parse DatePS Exception: ${e.toString()}"
				}
				
				// App Time & Date
				try {
					def appTimeString = c.AppTime.toString()
					if ( ! appTimeString ) {
						norcConsent.appTime = null
					} else {
						DateTime dt = fmt.parseDateTime(appTimeString)
						norcConsent.appTime = dt.toCalendar().getTime()
					}
				} catch (Exception e) {
					response << "! Invalid AppTime: ${c.AppTime.toString()}\n"
					println "! parse AppTime Input: ${c.AppTime.toString()}"
					println "! parse AppTime Exception: ${e.toString()}"
				}
				
				try {
					norcConsent.segmentId = Integer.parseInt(c.SegmentID.toString())
				} catch (Exception e) {
					response << "! Invalid SegmentID: ${c.SegmentID.toString()}\n"
				}
				
				if (norcConsent.hasErrors()) {
					response << "! consent has errors.\n"
				} else if (norcConsent.save(flush:true)) {
					response << "= saved data\n"
				} else {
					response << "! unable to save data.\n"
					
					norcConsent.errors.each{ e ->
						// println "NorcConsent:error::${e}"
						e.fieldErrors.each{ fe ->
							response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
						}
						
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
