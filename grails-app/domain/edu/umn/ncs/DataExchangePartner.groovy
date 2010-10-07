package edu.umn.ncs

class DataExchangePartner {

	// The name of the partner, i.e. NORC
	String name
	// Contact Name
	String contactName
	// Contact Email
	String contactEmail
	// This is a 2048 bit string
	String privateKey

	// Provenance fields
	String userCreated
	Date dateCreated = new Date()

	static hasMany = [
		roles : DataExchangePartnerRole,
		allowedClients : ClientHost
	]


	String toSring() { name }

    static constraints = {
		name(maxSize:254)
		contactName(maxSize:254)
		contactEmail(email:true)
		privateKey(maxSize:4096)
		userCreated()
		dateCreated()
    }
}
