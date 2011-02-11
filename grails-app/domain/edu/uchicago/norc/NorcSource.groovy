package edu.uchicago.norc

class NorcSource {
	Integer value
	String name
	String label
	
    static constraints = {
		value(unique:true)
		name()
		label()
    }
}
