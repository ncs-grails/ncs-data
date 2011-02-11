package edu.uchicago.norc

class NorcStatus {
	Integer value
	String name
	String label
	
    static constraints = {
		value(unique:true)
		name()
		label()
    }
}
