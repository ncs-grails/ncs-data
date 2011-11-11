package edu.uchicago.norc

class NorcBirthIncentiveBatch {

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
	String source
	String status
	String mode
	String email
	String phonenumber
	String segmentid
	String incentiveamount
	String incentivemailed
	String sampleUnitKey
	Date datein
	String apptime

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
		source(nullable:true,maxSize:75)
		status(nullable:true,maxSize:75)
		mode(nullable:true,maxSize:75)
		email(nullable:true,maxSize:75)
		phonenumber(nullable:true,maxSize:75)
		segmentid(nullable:true,maxSize:75)
		incentiveamount(nullable:true,maxSize:75)
		incentivemailed(nullable:true,maxSize:75)
		sampleUnitKey(nullable:true,maxSize:75)
		datein(nullable:true)
		apptime(nullable:true,maxSize:75)
    }
}
