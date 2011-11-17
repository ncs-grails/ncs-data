package edu.uchicago.norc

class NorcConsent {

	String firstName
	String lastName
	String address1
	String address2
	String addressUnit
	String city
	String state
	String zipCode
	String zipFour
	String norcSuId
	String mailingId
	NorcDocType docType
	NorcSource source
	NorcStatus status
	Date pregnancyScreenerDate
	NorcDocMode mode
	Date appTime
	String email
	String phoneNumber
	Integer segmentId

	// New Columns
	String ppgCurrent
	Integer ppgStatus
	String homePhone
	String sampleUnitKey
	String area
	String pscrDisp
	String flagPpgfollow
	Date instrumentDispDate
	String in3
	String localApptime
	String hhId
	Integer flagCaticombo
	String datePS
	
    static constraints = {
		firstName(nullable:true)
		lastName(nullable:true)
		address1(nullable:true)
		address2(nullable:true)
		addressUnit(nullable:true)
		city(nullable:true, maxSize:30)
		state(nullable:true, maxSize:2)
		zipCode(nullable:true, maxSize:5)
		zipFour(nullable:true, maxSize:4)
		norcSuId(nullable:true)
		mailingId(nullable:true)
		docType(unique:'mailingId', nullable:true)
		source(nullable:true)
		status(nullable:true)
		pregnancyScreenerDate(nullable:true)
		mode(nullable:true)
		appTime(nullable:true)
		email(nullable:true, email:true)
		phoneNumber(nullable:true, maxSize:32)
		segmentId(nullable:true)

		ppgCurrent(nullable:true)
		ppgStatus(nullable:true)
		homePhone(nullable:true)
		sampleUnitKey(nullable:true)
		area(nullable:true)
		pscrDisp(nullable:true)
		flagPpgfollow(nullable:true)
		instrumentDispDate(nullable:true)
		in3(nullable:true)
		localApptime(nullable:true)
		hhId(nullable:true)
		flagCaticombo(nullable:true)
		datePS(nullable:true)

    }
}
