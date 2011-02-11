package edu.uchicago.norc

class NorcDocType {
	Integer value
	String name
	String label
	
    static constraints = {
		value(unique:true)
		name()
		label()
    }
}
