package edu.umn.ncs.data

class ClientHost {

	String hostname
	String ipvFour
	String ipvSix

	String userCreated
	Date dateCreated = new Date()

	String toString() { hostname }

    static constraints = {
		hostname()
		ipvFour(nullable:true)
		ipvSix(nullable:true)
		userCreated()
		dateCreated()
    }
}
