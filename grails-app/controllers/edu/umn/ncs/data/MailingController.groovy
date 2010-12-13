package edu.umn.ncs.data
import groovy.xml.StreamingMarkupBuilder
import edu.umn.ncs.Batch
import edu.umn.ncs.TrackedItem
import edu.umn.ncs.InstrumentLink
import edu.umn.ncs.DwellingUnitLink
import edu.umn.ncs.StudyLink
import edu.umn.ncs.DwellingUnit

import grails.converters.XML

// This is the RESTful web service for
// the mailings.

class MailingController {

	// GET, POST, PUT, DELETE
    static allowedMethods = [list: "GET", show: "GET"]

	def now = new Date()

    def index = {
        redirect(action: "list", params: params)
    }

	// the batch list adapted for NORC
    def listNorc = {
		// List all the batches
		if ( ! hasRoleAccess(params.key, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ${readMailingRole}"
		} else {

			def c = Batch.createCriteria()

			// finding all mailed batches
			def batchInstanceList = c.list{
				or {
					isNotNull("mailDate")
					isNotNull("addressAndMailingDate")
					lt("instrumentDate", now)
				}
			}

			// get the xml
			def batchInstanceListXml = batchInstanceList.encodeAsXML()

			// parse it out to an XPath
			def xmlOutput = new XmlSlurper().parseText(batchInstanceListXml)

			// let's update it with the batch info
			xmlOutput.batch.each{ b ->
				def batchId = Integer.parseInt(b.@id.toString())
				def batchInstance = Batch.get(batchId)
				// println "Found BatchID: ${batchId}"

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
		}
    }

	// the default batch list
	def list = {
		// List all the batches
		if ( ! hasRoleAccess(params.key, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ${readMailingRole}"
		} else {

			def c = Batch.createCriteria()

			// finding all mailed batches
			def batchInstanceList = c.list{
				or {
					isNotNull("mailDate")
					isNotNull("addressAndMailingDate")
					lt("instrumentDate", now)
				}
			}

			// rendering the results
			render batchInstanceList as XML
		}
    }

	// the default show action for a mailing/batch
    def show = {

		if ( ! hasRoleAccess(params.key, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ${readMailingRole}"

		} else {
			def batchInstance = Batch.get(params.id)

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

		if ( ! hasRoleAccess(params.key, 'ROLE_READ_MAILING') ) {
			response.sendError(403)
			render "ACCESS DENIED ${readMailingRole}"

		} else {
			def batchInstance = Batch.get(params.id)

			if (! batchInstance) {
				redirect(action:"list", params:params)
			}
			else {
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
				xmlOutput.items.trackedItem.each{ i ->

					def duid = Integer.parseInt(i.dwellingUnit.@id.toString())

					def dwellingUnitInstance = DwellingUnit.get(duid)
					// println "Found Dwelling Unit Item ID: ${duid}"

					i.dwellingUnit.appendNode {
						norcSu(id:dwellingUnitInstance.norcSuId)
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

	private def hasRoleAccess = { privateKey, roleName ->

		def grantAccess = false

		def accessRoles = getAccessRoles(privateKey)

		if (accessRoles) {
			def readMailingRole = accessRoles.find{it.role == roleName}

			if (readMailingRole) {
				grantAccess = true
			} else {
				println "Insuffiecent access to role ${roleName} for client host ${ipAddress} using ${dataExchangePartnerInstance} key!"
			}

		} else {
			println "Client host ${ipAddress} using ${dataExchangePartnerInstance} key has no roles!"
		}
	}

	private def getAccessRoles = { privateKey ->

		def roles = []

		def ipAddress = request.remoteAddr

		if (privateKey) {
			def dataExchangePartnerInstance = DataExchangePartner.findByPrivateKey(privateKey)

			if (dataExchangePartnerInstance) {

				def clientHostInstance = dataExchangePartnerInstance.allowedClients.find { it.ipvFour == ipAddress || it.ipvSix == ipAddress }

				if (clientHostInstance) {
					println "Access grandted from client host ${ipAddress} using ${dataExchangePartnerInstance} key."
					// load the roles
					roles = dataExchangePartnerInstance.roles
				} else {
					println "Host Found:: Denied access from ${ipAddress} as ${dataExchangePartnerInstance} client host."
				}
				
			} else {
				println "Key Not Found:: Denied access to ${ipAddress} using key: ${privateKey}"
			}

		}

		return roles
	}
}

