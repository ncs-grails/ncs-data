package edu.uchicago.norc

import edu.umn.ncs.data.AccessService
import grails.converters.*

// Incentive Controller
class IncentiveController {
	def dataParserService
	
	static allowedMethods = [ save:'POST', update:'PUT', show:'GET', delete:'DELETE' ]

	def save = {

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
				println "table class: ${table.class}"
			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}
			
			dataParserService.parseIncentive(table, response)
			
		}
		render "save action finished.\n"
	}

	def update = {
		response.sendError(401)
		render "UPDATE not allowed, use POST\n"
	}

	def show = {
		response.sendError(401)
		render "SHOW not allowed\n"
	}

	def delete = {
		response.sendError(401)
		render "DELETE not allowed\n"
	}
}
