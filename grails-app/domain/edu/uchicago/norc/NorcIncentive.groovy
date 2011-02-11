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
	String email
	String phoneNumber
	Integer segmentId
	String norcSuId
	String mailingId
	NorcDocType docType
	NorcSource source
	NorcStatus status
	NorcDocMode mode
	Date incentiveDate
	Date appTime
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
		zipCode(nullable:true, maxSize:5)
		zipFour(nullable:true, maxSize:4)
		email(nullable:true, email:true)
		phoneNumber(nullable:true, maxSize:32)
		segmentId(nullable:true)
		norcSuId(nullable:true)
		mailingId(nullable:true)
		docType(nullable:true)
		source(nullable:true)
		status(nullable:true)
		mode(nullable:true)
		pregnancyScreenerDate(nullable:true)
		incentiveDate(nullable:true)
		appTime(nullable:true)
		incentiveAmount(nullable:true)
		incentiveMailed(nullable:true)
	}
}
