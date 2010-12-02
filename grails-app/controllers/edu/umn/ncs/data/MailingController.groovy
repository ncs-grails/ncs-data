package edu.umn.ncs.data
import edu.umn.ncs.Batch
import edu.umn.ncs.TrackedItem

import grails.converters.XML

// This is the RESTful web service for
// the mailings.

class MailingController {

	def dataRetrievalService

	// GET, POST, PUT, DELETE
    static allowedMethods = [list: "GET", show: "GET"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		// List all the batches

		if (params.key != "DEMO") {
			response.sendError(403)
			render "ACCESS DENIED"
		} else {
			def batchInstanceList = Batch.list()
			render batchInstanceList as XML
		}
    }

    def show = {

		if (params.key != "DEMO") {
			response.sendError(403)
			render "ACCESS DENIED"
		} else {
			def batchInstance = Batch.get(params.id)

			if (! batchInstance) {
				redirect(action:"list", params:params)
			}
			else {
				// don't display creation config info
				batchInstance.creationConfig = null

				XML.use("deep") {
					render batchInstance as XML
				}
			}
		}
    }
}

