package edu.umn.ncs.data

class DataExchangePartnerRole {
	String name
	String role

	String toString() { name }

    static constraints = {
		name()
		role()
    }
}
