package edu.umn.ncs.data
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;

import groovy.xml.StreamingMarkupBuilder
import edu.umn.ncs.Batch
import edu.umn.ncs.TrackedItem
import edu.umn.ncs.InstrumentLink
import edu.umn.ncs.DwellingUnitLink
import edu.umn.ncs.StudyLink
import edu.umn.ncs.DwellingUnit
import edu.umn.ncs.BatchDirection
import edu.umn.ncs.InstrumentFormat

import grails.converters.XML
import groovy.xml.MarkupBuilder

// This is the RESTful web service for
// the mailings.

class MailingController {

	// GET, POST, PUT, DELETE
    static allowedMethods = [list: "GET", show: "GET"]
	
	def accessService
	
	static def debug = false

    def index = {
        redirect(action: "list", params: params)
    }

	// the batch list adapted for NORC
    def listNorc = {
		def now = new Date()
		
		// List all the batches
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_READ_MAILING"
		} else {

			def c = Batch.createCriteria()
			def batchDirectionInstance = BatchDirection.findByName('outgoing')
			def instrumentFormatInstance = InstrumentFormat.findByName('first class mail')

			// finding all mailed batches
			def batchInstanceList = c.list{
				and {
					direction {
						idEq(batchDirectionInstance.id)
					}
					format {
						idEq(instrumentFormatInstance.id)
					}
					or {
						isNotNull("mailDate")
						isNotNull("addressAndMailingDate")
						lt("instrumentDate", now)
					}
				}
			}

			if (batchInstanceList) {

				// get the xml
				def batchInstanceListXml = batchInstanceList.encodeAsXML()
	
				// parse it out to an XPath
				def xmlOutput = new XmlSlurper().parseText(batchInstanceListXml)
	
				// let's update it with the batch info
				xmlOutput.batch.each{ b ->
					def batchId = Integer.parseInt(b.@id.toString())
					def batchInstance = Batch.read(batchId)
					if (debug) { println "Found BatchID: ${batchId}" }
	
					b.appendNode {
						norcDoc(id:batchInstance.norcDocId)
					}
					b.appendNode {
						norcProject(id:batchInstance.norcProjectId)
					}
				}
	
				// check the whole document using XmlUnit
				def outputBuilder = new StreamingMarkupBuilder()
				String xmlResult = outputBuilder.bind{ mkp.yield xmlOutput }
				
				// rendering the results
				//render batchInstanceListXml
				render(contentType:"text/xml", text:xmlResult)
				
			} else {
				render batchInstanceList as XML
			}
		}
    }

	// the default batch list
	def list = {
		def now = new Date()
		
		// List all the batches
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_READ_MAILING"
		} else {

			def c = Batch.createCriteria()
			def batchDirectionInstance = BatchDirection.findByName('outgoing')
			def instrumentFormatInstance = InstrumentFormat.findByName('first class mail')

			// finding all mailed batches
			def batchInstanceList = c.list{
				and {
					direction {
						idEq(batchDirectionInstance.id)
					}
					format {
						idEq(instrumentFormatInstance.id)
					}
					or {
						isNotNull("mailDate")
						isNotNull("addressAndMailingDate")
						lt("instrumentDate", now)
					}
				}
			}

			// rendering the results
			render batchInstanceList as XML
		}
    }

	// This is a CSV version of the list action
	def listCsv = {
		def now = new Date()
		
		// List all the batches
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_READ_MAILING"
		} else {

			def c = Batch.createCriteria()
			def batchDirectionInstance = BatchDirection.findByName('outgoing')
			def instrumentFormatInstance = InstrumentFormat.findByName('first class mail')
			
			// first get all NORC instruments
			def norcInstrumentIds = InstrumentLink.list().collect{it.instrument.id}
			if (debug) {
				println "Instruments linked to NORC: "
				norcInstrumentIds.each{ println "\t${it}" }
			}

			// finding all mailed batches
			def batchInstanceList = c.list{
				and {
					instruments {
						instrument {
							'in'("id", norcInstrumentIds)
						}
					}
					direction {
						idEq(batchDirectionInstance.id)
					}
					format {
						idEq(instrumentFormatInstance.id)
					}
					or {
						isNotNull("mailDate")
						isNotNull("addressAndMailingDate")
						lt("instrumentDate", now)
					}
				}
			}

			def fileName = "ncs-ramsey-mailing-batches.csv" 
			
			// Set response header to CSV
			response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
			response.contentType = "text/csv"
	
			
			// The column headers
			def columns = [
					'batch_id',
					'primary_instrument_id',
					'primary_instrument_name',
					'norc_doc_id',
					'date_created',
					'mail_date',
					'date_on_instrument'
				]
			
			// print them to the response stream
			response << columns.collect{ '"' + it + '"' }.join(',') + "\n"
			
			batchInstanceList.each{ batchInstance ->
				
				response << batchInstance.id + ','
				response << batchInstance.primaryInstrument.id + ','
				response << '"' + batchInstance.primaryInstrument.name + '",'
				response << (batchInstance.norcDocId ?: '') + ','
				response << '"' + (batchInstance.dateCreated?.toString() ?: '') + '",'
				response << '"' + (batchInstance.mailDate?.toString() ?: '') + '",'
				response << '"' + (batchInstance.instrumentDate?.toString() ?: '') + '"\n'
			}
		}
		render ""
	}
	
	
	// the default show action for a mailing/batch
    def show = {
		def now = new Date()
		
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ${readMailingRole}"

		} else {
			def batchInstance = Batch.read(params.id)

			if (! batchInstance) {
				redirect(action:"list", params:params)
			}
			else {
				// don't display creation config info
				batchInstance.creationConfig = null

				// adding NORC data


				XML.use("deep") {
					render batchInstance as XML
				}
			}
		}
    }

	def showNorc = {
		def now = new Date()
		
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_READ_MAILING"

		} else {
			def batchInstance = Batch.read(params.id)

			if (! batchInstance) {
				response.sendError(404)
				render "Mailing # ${params.id} Not Found"
			} else if ( ! batchInstance.norcDocId ) {
				def writer = new StringWriter()
				def builder = new MarkupBuilder(writer)
				builder.message{
					error "Access Denied to Non-NORC batch."
					explanation "This batch does not have a matching NORC Doc ID."
				}
				render(contentType:"text/xml", text: writer.toString() )
				return
			} else {
				// don't display creation config info
				batchInstance.creationConfig = null

				def batchInstanceXml
				// adding NORC data


				XML.use("deep") {
					batchInstanceXml = batchInstance as XML
					//render batchInstance as XML
				}

				// parse it out to an XPath
				def xmlOutput = new XmlSlurper().parseText(batchInstanceXml.toString())


				// add the doc id and project id
				xmlOutput.appendNode {
					norcDoc(id:batchInstance.norcDocId)
				}
				xmlOutput.appendNode {
					norcProject(id:batchInstance.norcProjectId)
				}

				// let's update it with the batch info
				xmlOutput?.items?.trackedItem?.each{ i ->
					
					try {
						def duid = Integer.parseInt(i.dwellingUnit.@id.toString())
		
						def dwellingUnitInstance = DwellingUnit.read(duid)
						if (debug) { println "Found Dwelling Unit Item ID: ${duid}" }
		
						i.dwellingUnit.appendNode {
							norcSu(id:dwellingUnitInstance.norcSuId)
						}
					} catch (Exception) {
						// no dwelling unit here
						if (debug) { println "DEBUG: not a dwelling unit" }
					}
					
					try {
						def pid = Integer.parseInt(i.person.@id.toString())
		
						def personInstance = Person.read(pid)
						if (debug) { println "Found Person from Item ID: ${pid}" }
		
						i.person.appendNode {
							norcSu(id:personInstance.norcSuId)
						}
					} catch (Exception) {
						// no dwelling unit here
						if (debug) { println "DEBUG: not a dwelling unit" }
					}
					
				}

				// check the whole document using XmlUnit
				def outputBuilder = new StreamingMarkupBuilder()
				String xmlResult = outputBuilder.bind{ mkp.yield xmlOutput }

				// rendering the results
				//render batchInstanceListXml
				render(contentType:"text/xml", text:xmlResult)

			}
		}
    }
	
	def showCsv = {
		def now = new Date()
		
		// List all the batches
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_READ_MAILING"
		} else {

			def batchInstance = Batch.read(params.id)
		
			if (! batchInstance) {
				response.sendError(404)
				render "Mailing # ${params.id} Not Found"
			} else if ( ! batchInstance.norcDocId ) {
				def writer = new StringWriter()
				def builder = new MarkupBuilder(writer)
				builder.message{
					error "Access Denied to Non-NORC batch."
					explanation "This batch does not have a matching NORC Doc ID."
				}
				render(contentType:"text/xml", text: writer.toString() )
				return
			} else {
	
				def fileName = "ncs-ramsey-mailing-batch-${batchInstance.id}.csv"
				
				// Set response header to CSV
				response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"");
				response.contentType = "text/csv"
		
				
				// The column headers
				def columns = [
						'batch_id',
						'primary_instrument_id',
						'primary_instrument_name',
						'norc_doc_id',
						'date_created',
						'mail_date',
						'date_on_instrument',
						'tracked_item_id',
						'norc_su_id',
						'dwelling_unit_id',
						'person_id',
						'address_id',
						'street_address'
					]
				
				// print them to the response stream
				response << columns.collect{ '"' + it + '"' }.join(',') + "\n"
				
				
				def rowPrefix = ""

				rowPrefix += batchInstance.id + ','
				rowPrefix += batchInstance.primaryInstrument.id + ','
				rowPrefix += '"' + batchInstance.primaryInstrument.name + '",'
				rowPrefix += (batchInstance.norcDocId ?: '') + ','
				rowPrefix += '"' + (batchInstance.dateCreated?.toString() ?: '') + '",'
				rowPrefix += '"' + (batchInstance.mailDate?.toString() ?: '') + '",'
				rowPrefix += '"' + (batchInstance.instrumentDate?.toString() ?: '') + '",'

				if (debug) {
					println "Row Prefix:\n${rowPrefix}"
					
				}
				
				batchInstance.items.each{ trackedItemInstance ->
					
					response << rowPrefix
					
					response << trackedItemInstance.id + ','
					if (trackedItemInstance.person) {
						response << '"' + trackedItemInstance.person.norcSuId + '",'
						response << ','
						response << trackedItemInstance.person.id + ','
						response << trackedItemInstance.person.primaryAddress?.id + ','
						response << '"' + trackedItemInstance.person.primaryAddress?.address + '"'
						
					} else if (trackedItemInstance.dwellingUnit) {
						response << '"' + trackedItemInstance.dwellingUnit.norcSuId + '",'
						response << trackedItemInstance.dwellingUnit.id + ','
						response << ','
						response << trackedItemInstance.dwellingUnit.address.id + ','
						response << '"' + trackedItemInstance.dwellingUnit.address.address + '"'
					} else {
						response << ',,,,'
					}
					
					response << '\n'
					
				}
			}
			render ""
		}
	}
}

