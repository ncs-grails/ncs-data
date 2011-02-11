package edu.uchicago.norc

class NorcIncentive {

	/* Missing data is delimited as:
	<field missing="." />
	*/
	
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
	Date incentiveDate
	NorcDocMode mode
	Date appTime
	String email
	String phoneNumber
	Integer segmentId
	BigDecimal incentiveAmount
	// this should be a date
	Boolean incentiveMailed
	Date pregnancyScreenerDate
	
	static constraints = {
		firstName(nullable:true)
		lastName(nullable:true)
		address1(nullable:true)
		address2(nullable:true)
		addressUnit(nullable:true)
		city(nullable:true, maxSize:30)
		state(nullable:true, maxSize:2)
		zipCode(nullable:true, maxSize:4)
		zipFour(nullable:true, maxSize:5)
		norcSuId(nullable:true)
		mailingId(nullable:true)
		docType(nullable:true)
		source(nullable:true)
		status(nullable:true)
		pregnancyScreenerDate(nullable:true)
		mode(nullable:true)
		appTime(nullable:true)
		email(nullable:true, email:true)
		phoneNumber(nullable:true, maxSize:32)
		segmentId(nullable:true)
	}
}
