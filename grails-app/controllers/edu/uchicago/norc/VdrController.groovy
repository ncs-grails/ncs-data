package edu.uchicago.norc

class VdrController {

	def index = {
		// dependency injection wasn't working here,
		// so I'll explicitly instantiate the service
		AccessService accessService = new AccessService()

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			def table

				def saveLocal = false

				if (saveLocal) {
					println " ~ Saving Data File..."
					response << " ~ Saving Data File... \n"

					def now = new Date()
					// build the file name
					def fileName = 'ncs-vdr-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-hh-mm') + '.xml'
					// get a file to write to
					def xmlFileWriter = new File(fileName).newWriter()
					// get a reader for the request data stream
					def dataReader = request.getReader()
					// write the data from the reader to the XML file
					xmlFileWriter.write(dataReader)
					// flush the stream
					xmlFileWriter.flush()

					println " ~ Done Saving Data File."
					response << " ~ Saving Data File. \n"
				}
			} catch (Exception ex) {
				response << " ! Error processing upload:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}
			
		}
		render "save action finished.\n"

		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
		
		def session = sessionFactory.getCurrentSession();
		session.clear();
	}
}
