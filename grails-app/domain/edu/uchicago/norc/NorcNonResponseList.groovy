package edu.uchicago.norc

class NorcNonResponseList {
	String suId
	String promptDisp
	String promptDispDate
	String catiDisp
	String catiDispDate
	String promptDispLabel
	String catiDispLabel
	String mail
	String mailUndeliverable
	
    static constraints = {
		suId(unique:true,maxSize:32)
		promptDisp(nullable:true,maxSize:75)
		promptDispDate(nullable:true,maxSize:75)
		catiDisp(nullable:true,maxSize:75)
		catiDispDate(nullable:true,maxSize:75)
		promptDispLabel(nullable:true,maxSize:75)
		catiDispLabel(nullable:true,maxSize:75)
		mail(nullable:true,maxSize:75)
		mailUndeliverable(nullable:true,maxSize:75)
    }
}
