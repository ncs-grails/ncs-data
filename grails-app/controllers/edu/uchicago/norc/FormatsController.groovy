package edu.uchicago.norc

import edu.umn.ncs.data.AccessService

import grails.converters.*

class FormatsController {

	def dataParserService
	
	static allowedMethods = [ save:'POST', update:'PUT', show:'GET', delete:'DELETE' ]

	def save = {
		
		// dependency injection wasn't working here,
		// so I'll explicitly instantiate the service
		AccessService accessService = new AccessService()
		
		if ( ! accessService.hasRoleAccess(params.key, request.remoteAddr, 'ROLE_WRITE_RESULT') ) {
			response.sendError(403)
			render "ACCESS DENIED ROLE_WRITE_RESULT"
		} else {

			def table

			try {
				table = request.XML
			} catch (Exception ex) {
				response << " ! Invalid XML:\n"
				response << "	${ex.cause}\n"
				response << "	${ex.message}\n"
			}

		dataParserService.parseFormats(table, response)
			
		render "saved data\n"
		}
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
