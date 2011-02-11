package edu.uchicago.norc

class NorcDocMode {
	Integer value
	String name
	String label
	
    static constraints = {
		value(unique:true)
		name()
		label()
    }
}
