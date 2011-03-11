package edu.uchicago.norc

import grails.converters.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.*

class DataParserService {

	static transactional = true

	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

	def parseEverything(table, response) {
		parseFormats table, response
		parseConsent table, response
		parseIncentive table, response
	}
	
	def parseIncentive(table, response) {
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

	def parseConsent(table, response) {
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

	def parseFormats(table, response) {

		table?.FMTS?.each{ f ->
			
			def fName = f.FMTNAME.toString()
			def fLabel = f.LABEL.toString()
			def fValue = f.START.toString()
			
			switch (fName) {
				case "DOCTYPE":
					def norcDocType = NorcDocType.findByValue(fValue)
					if ( ! norcDocType ) {
						norcDocType = new NorcDocType(name:fName, label:fLabel, value:fValue)
						norcDocType.save(flush:true)
						response << "Saved new NorcDocType: ${norcDocType.label}\n"
					} else {
						if (norcDocType.label != fLabel) {
							norcDocType.label = fLabel
							norcDocType.save(flush:true)
	
							response << "Updated NorcDocType(${norcDocType.value}) = ${norcDocType.name}\n"
						} else {
							response << "NorcDocType(${norcDocType.value}) already in the system.\n"
						}
					}
					break
				case "MODE":
					def norcDocMode = NorcDocMode.findByValue(fValue)
					if ( ! norcDocMode ) {
						norcDocMode = new NorcDocMode(name:fName, label:fLabel, value:fValue)
						norcDocMode.save(flush:true)
						response << "Saved new NorcDocType: ${norcDocMode.label}\n"
					} else {
						if (norcDocMode.label != fLabel) {
							norcDocMode.label = fLabel
							norcDocMode.save(flush:true)
	
							response << "Updated NorcDocMode(${norcDocMode.value}) = ${norcDocMode.name}\n"
						} else {
							response << "NorcDocMode(${norcDocMode.value}) already in the system.\n"
						}
					}
					break
				case "SOURCE":
					def norcSource = NorcSource.findByValue(fValue)
					if ( ! norcSource ) {
						norcSource = new NorcSource(name:fName, label:fLabel, value:fValue)
						norcSource.save(flush:true)
						response << "Saved new NorcSource: ${norcSource.label}\n"
					} else {
						if (norcSource.label != fLabel) {
							norcSource.label = fLabel
							norcSource.save(flush:true)
	
							response << "Updated NorcSource(${norcSource.value}) = ${norcSource.name}\n"
						} else {
							response << "NorcSource(${norcSource.value}) already in the system.\n"
						}
					}
					break
				case "STATUS":
					def norcStatus = NorcStatus.findByValue(fValue)
					if ( ! norcStatus ) {
						norcStatus = new NorcStatus(name:fName, label:fLabel, value:fValue)
						norcStatus.save(flush:true)
						response << "Saved new NorcStatus: ${norcStatus.label}\n"
					} else {
						if (norcStatus.label != fLabel) {
							norcStatus.label = fLabel
							norcStatus.save(flush:true)
	
							response << "Updated NorcStatus(${norcStatus.value}) = ${norcStatus.name}\n"
						} else {
							response << "NorcStatus(${norcStatus.value}) already in the system.\n"
						}
					}
					break
				default:
					response << "Unknown FMTS: ${fName}\n"
			}
		}
	}
}
