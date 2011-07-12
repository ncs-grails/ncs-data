package edu.uchicago.norc

class NorcNonResponseList {
	String suId
	String promptDisp
	String promptDispDate
	String promptDispLabel
	String mail
	String mailUndeliverable
	
    static constraints = {
		suId(unique:true,maxSize:32)
		promptDisp(nullable:true,maxSize:75)
		promptDispDate(nullable:true,maxSize:75)
		promptDispLabel(nullable:true,maxSize:75)
		mail(nullable:true,maxSize:75)
		mailUndeliverable(nullable:true,maxSize:75)
    }
}
