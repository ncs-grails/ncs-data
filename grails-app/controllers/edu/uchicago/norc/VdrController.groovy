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


			log.info " ~ Saving Data File..."
			response << " ~ Saving Data File ... \n"

			def now = new Date()
			// build the file name
			def fileName = saveLocation + '/ncs-vdr-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-HH-mm') + '.xml.zip'
			// get a file to write to
			def fileOutputStream = new File(fileName).newDataOutputStream()
			// get a reader for the request data stream
			def httpsInputStream = request.getInputStream()
			// create a 1k buffer for data transfer
			byte[] transferBuffer = new byte[1024]
			// this will store the bytes read as we go along
			int bytesRead
			long totalBytesRead = 0

			while ( (bytesRead = httpsInputStream.read(transferBuffer)) > 0 ) {
				fileOutputStream.write(transferBuffer,0, bytesRead)
				totalBytesRead += bytesRead
			}
			// flush the stream
			fileOutputStream.close()
			httpsInputStream.close()

			log.info " ~ Done Saving Data File (${totalBytesRead} bytes)."
			response << " ~ Saved Data File (${totalBytesRead} bytes). \n"
			response << " ~ Calculating MD5 sum... \n"

			def md5Sum = "too big to calculate"
			try {
				def md5Command = "md5sum ${fileName}"
				log.info "Executing Shell command: ${md5Command}"
				def proc = md5Command.execute()
				proc.waitFor()
				def resultText = proc.in.text
				md5Sum = resultText[0..31]
			} catch (java.io.IOException ex) {
				md5Sum = "too big of a file to calculate MD5 quickly"
			}
			log.info " ~ Calculated MD5 sum:${md5Sum}."
			response << " ~ Calculated MD5 sum:${md5Sum}. \n"
			
		}
		render "save action finished.\n"

		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
	}
}
