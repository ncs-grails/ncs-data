package edu.uchicago.norc

class NorcRamseyPpgFollowup {

	String fname
	String lname
	String address1
	String address2
	String unit
	String city
	String state
	String zip
	String zip4
	String suId
	String mailingid
	String doctype
	String status
	String mailpulldate

    static constraints = {
		
		fname(nullable:true,maxSize:75)
		lname(nullable:true,maxSize:75)
		address1(nullable:true,maxSize:75)
		address2(nullable:true,maxSize:75)
		unit(nullable:true,maxSize:75)
		city(nullable:true,maxSize:75)
		state(nullable:true,maxSize:75)
		zip(nullable:true,maxSize:75)
		zip4(nullable:true,maxSize:75)
		suId(nullable:true,maxSize:75)
		mailingid(nullable:true,maxSize:75)
		doctype(nullable:true,maxSize:75)
		status(nullable:true,maxSize:75)
		mailpulldate(nullable:true,maxSize:75)
    }
}
