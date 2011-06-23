package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import groovy.xml.StreamingMarkupBuilder
import grails.converters.*

// Incentive Controller
class CombinedController {
	def dataParserService
	def emailNotifyService
	def sessionFactory
	
	static allowedMethods = [ index:'POST', textXmlParser:'POST' ]

	def regurgitate = {
		render "regurgitating response:\n${request.reader.text}"
	}

	def index = {
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
				// println "table class: ${table.class}"
				// save the XML data somewhere?

				println " ~ Saving Local Copy..."
				response << " ~ Saving Local Copy... \n"

				def fileName = 'textXmlParser-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-hh-mm') + '.xml'
				def xmlFileWriter = new File(fileName).newWriter()
				
				def smb = new StreamingMarkupBuilder().bind {xml -> xml.mkp.yield table}
				
				xmlFileWriter.write(smb.toString())
				xmlFileWriter.flush()
				
				println " ~ Done Saving Local Copy."
				response << " ~ Saving Local Copy. \n"

			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}
			
			println " ~ Beginning Data Parse."
			response << " ~ Beginning Data Parse. \n"
			dataParserService.parseEverything(table, response)
			
		}
		render "save action finished.\n"

		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
		
		def session = sessionFactory.getCurrentSession();
		session.clear();
	}

	def textXmlParser = {
		// dependency injection wasn't working here,
		// so I'll explicitly instantiate the service
		AccessService accessService = new AccessService()
		def now = new Date()

		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_INSTRUMENT') ) {
			println "Yo!"
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_INSTRUMENT\n"
		} else {

			def table

			try {
				
				println " ~ Parsing XML"
				response << " ~ Parsing XML \n"
				
				table = request.XML
				// println "table class: ${table.class}"
				
				// save the XML data somewhere?

				println " ~ Saving Local Copy..."
				response << " ~ Saving Local Copy... \n"

				def fileName = 'textXmlParser-upload_' + g.formatDate(date:now, format: 'yyyy-MM-dd-hh-mm') + '.xml'
				def xmlFileWriter = new File(fileName).newWriter()
				
				def smb = new StreamingMarkupBuilder().bind {xml -> xml.mkp.yield table}
				
				xmlFileWriter.write(smb.toString())
				xmlFileWriter.flush()
				
				println " ~ Done Saving Local Copy."
				response << " ~ Saving Local Copy. \n"

			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}
			
			println " ~ Beginning Data Parse."
			response << " ~ Beginning Data Parse. \n"
			//dataParserService.parseNorcData(table, response)
			dataParserService.parseEverything(table, response)
			
		}
		render "save action finished.\n"
		
		// send notification email
		emailNotifyService.notifyOfNorcUpload(request.remoteAddr)
		
		def session = sessionFactory.getCurrentSession();
		session.clear();
	}
}
