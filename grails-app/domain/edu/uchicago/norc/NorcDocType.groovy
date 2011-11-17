package edu.uchicago.norc

class NorcDocType {
	Integer value
	String name
	String label

	String toString() { label }
	
    static constraints = {
		value(unique:true)
		name()
		label()
    }
}
