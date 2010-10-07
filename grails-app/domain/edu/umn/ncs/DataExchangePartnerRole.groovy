package edu.umn.ncs

class DataExchangePartnerRole {
	String name
	String role

	String toString() { name }

    static constraints = {
		name()
		role()
    }
}
