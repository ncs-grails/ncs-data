package edu.uchicago.norc

import edu.umn.ncs.data.AccessService

class VdrController {

	static allowedMethods = [ index: 'POST' ]

	def index = {

		// dependency injection wasn't working here,
		// so I'll explicitly instantiate the service
		AccessService accessService = new AccessService()

		def config = grailsApplication.config.ncs

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			def saveLocation = config.uploads

			println " ~ Saving Data File..."
			response << " ~ Saving Data File... \n"

			def now = new Date()
			// build the file name
			def fileName = saveLocation + '/ncs-vdr-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-hh-mm') + '.xml.zip'
			// get a file to write to
			def fileWriter = new File(fileName).newWriter()
			// get a reader for the request data stream
			def dataReader = request.getInputStream()
			// write the data from the reader to the XML file

			int c
			// TODO: Find out why this loop is not copying line feeds!
			while ((c = dataReader.read()) != -1 ) {
				fileWriter.write(c)
			}
			// flush the stream
			fileWriter.flush()

			println " ~ Done Saving Data File."
			response << " ~ Saving Data File. \n"
			
		}
		render "save action finished.\n"

		// send notification email
		// emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
	}
}
