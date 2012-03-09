package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import edu.umn.ncs.data.VdrService

class VdrController {

	static allowedMethods = [ index: 'POST' ]

	// dependency injection wasn't working here,
	// so I'll explicitly instantiate the service
	VdrService vdrService = new VdrService()
	AccessService accessService = new AccessService()
	def emailNotifyService

	def index = {

		def config = grailsApplication.config.ncs

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			def saveLocation = config.uploads

			log.info " ~ Saving Data File..."
			response << " ~ Saving Data File ... \n"

			def now = new Date()
			// build the file name
			def fileName = saveLocation + '/ncs-vdr-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-HH-mm') + '.xml.zip'
			// get a reader for the request data stream
			def httpsInputStream = request.getInputStream()

			def result = vdrService.saveStreamToFile(httpsInputStream, fileName)
			def totalBytesRead = result.bytesRead
			def md5Sum = result.md5Sum

			log.info " ~ Done Saving Data File (${totalBytesRead} bytes)."
			response << " ~ Saved Data File (${totalBytesRead} bytes). \n"

			log.info " ~ Calculated MD5 sum:${md5Sum}."
			response << " ~ Calculated MD5 sum:${md5Sum}. \n"
			
		}
		render "save action finished.\n"

		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
	}

	def key = { }
}
