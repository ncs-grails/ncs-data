package edu.uchicago.norc

class IncentiveController {
	
	static allowedMethods = [ save:'POST' ]

	def save = {
		forward(controller:"combined", action:"index", params:[key:params.key])
	}
}
