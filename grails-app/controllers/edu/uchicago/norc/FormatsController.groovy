package edu.uchicago.norc

import edu.umn.ncs.data.AccessService

import grails.converters.*

class FormatsController {

	static allowedMethods = [ save:'POST', update:'PUT', show:'GET', delete:'DELETE' ]

	def regurgitate = {
		render "regurgitating response:\n${request.reader.text}"
	}

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
			table?.FMTS?.each{ f ->
				
				def fName = f.FMTNAME.toString()
				def fLabel = f.LABEL.toString()
				def fValue = f.START.toString()
				
				switch (fName) {
					case "DOCTYPE":
						def norcDocType = NorcDocType.findByValue(fValue)
						if ( ! norcDocType ) {
							norcDocType = new NorcDocType(name:fName, label:fLabel, value:fValue)
							norcDocType.save(flush:true)
							response << "Saved new NorcDocType: ${norcDocType.label}\n"
						} else {
							if (norcDocType.label != fLabel) {
								norcDocType.label = fLabel
								norcDocType.save(flush:true)
		
								response << "Updated NorcDocType(${norcDocType.value}) = ${norcDocType.name}\n"
							} else {
								response << "NorcDocType(${norcDocType.value}) already in the system.\n"
							}
						}
						break
					case "MODE":
						def norcDocMode = NorcDocMode.findByValue(fValue)
						if ( ! norcDocMode ) {
							norcDocMode = new NorcDocMode(name:fName, label:fLabel, value:fValue)
							norcDocMode.save(flush:true)
							response << "Saved new NorcDocType: ${norcDocMode.label}\n"
						} else {
							if (norcDocMode.label != fLabel) {
								norcDocMode.label = fLabel
								norcDocMode.save(flush:true)
		
								response << "Updated NorcDocMode(${norcDocMode.value}) = ${norcDocMode.name}\n"
							} else {
								response << "NorcDocMode(${norcDocMode.value}) already in the system.\n"
							}
						}
						break
					case "SOURCE":
						def norcSource = NorcSource.findByValue(fValue)
						if ( ! norcSource ) {
							norcSource = new NorcSource(name:fName, label:fLabel, value:fValue)
							norcSource.save(flush:true)
							response << "Saved new NorcSource: ${norcSource.label}\n"
						} else {
							if (norcSource.label != fLabel) {
								norcSource.label = fLabel
								norcSource.save(flush:true)
		
								response << "Updated NorcSource(${norcSource.value}) = ${norcSource.name}\n"
							} else {
								response << "NorcSource(${norcSource.value}) already in the system.\n"
							}
						}
						break
					case "STATUS":
						def norcStatus = NorcStatus.findByValue(fValue)
						if ( ! norcStatus ) {
							norcStatus = new NorcStatus(name:fName, label:fLabel, value:fValue)
							norcStatus.save(flush:true)
							response << "Saved new NorcStatus: ${norcStatus.label}\n"
						} else {
							if (norcStatus.label != fLabel) {
								norcStatus.label = fLabel
								norcStatus.save(flush:true)
		
								response << "Updated NorcStatus(${norcStatus.value}) = ${norcStatus.name}\n"
							} else {
								response << "NorcStatus(${norcStatus.value}) already in the system.\n"
							}
						}
						break
					default:
						response << "Unknown FMTS: ${fName}\n"
				}
			}
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
