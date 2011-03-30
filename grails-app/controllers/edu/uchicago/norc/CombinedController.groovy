package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import grails.converters.*

// Incentive Controller
class CombinedController {
	def dataParserService
	
	static allowedMethods = [ index:'POST' ]

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
			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}
			
			dataParserService.parseEverything(table, response)
			
		}
		render "save action finished.\n"
	}
}
