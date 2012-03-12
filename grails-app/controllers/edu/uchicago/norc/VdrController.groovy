package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import edu.umn.ncs.data.VdrService
import edu.umn.ncs.data.ClientHost
import edu.umn.ncs.data.DataExchangePartner

class VdrController {

	static allowedMethods = [ index: 'POST' ]

	// dependency injection wasn't working here,
	// so I'll explicitly instantiate the service
	VdrService vdrService = new VdrService()
	AccessService accessService = new AccessService()
	def emailNotifyService

	def index = {

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			log.info " ~ Saving Data File..."
			response << " ~ Saving Data File ... \n"

			// build the file name
			def fileName = vdrService.getNewSaveLocation()
			def httpsInputStream = request.getInputStream()
			def result = vdrService.saveStreamToFile(httpsInputStream, fileName)

			log.info " ~ Saved Data File '${fileName}' (${result.bytesRead} bytes)."
			response << " ~ Saved Data File (${result.bytesRead} bytes). \n"

			log.info " ~ Calculated MD5 sum:${result.md5Sum}."
			response << " ~ Calculated MD5 sum:${result.md5Sum}. \n"
			
		}
		render "save action finished.\n"

		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
	}

	def key = {

		def clientHostEligible = false

		def clientHost = ClientHost.findByIpvFour(request.remoteAddr)
		if (clientHost) {
			def dataExchangePartner = DataExchangePartner.createCriteria().list{
				allowedClients {
					idEq(clientHost.id)
				}
			}
			if (dataExchangePartner) {
				clientHostEligible = true
			}
		}

		[ clientHostEligible: clientHostEligible ]
	}


	def uploadFile = {
		def remoteAddress = request.remoteAddr
		def key = params.key

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			flash.message = "Access denied: ROLE_WRITE_INSTRUMENT, from host: ${remoteAddress}"
			redirect(action:'key')
		} else {
			[ key: key ]
		}
	}

	def upload = {
		def remoteAddress = request.remoteAddr
		def key = params.key

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			flash.message = "Access denied: ROLE_WRITE_INSTRUMENT, from host: ${remoteAddress}"
			redirect(action:'key')
		} else {
			def f = request.getFile('vdrFile')
			if (! f.empty ) {
				def fileName = vdrService.getNewSaveLocation()
				f.transferTo(new File(fileName))
				log.info "Wrote file ${fileName}."
				flash.message = "The file was successfully uploaded."
				// send notification email
				emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
				redirect(action:'key')
			} else {
				flash.message = "Sorry, the file you uploaded was empty."
				redirect(action:'uploadFile', params:[key: key])
			}
		}
	}
}
