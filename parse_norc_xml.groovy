import org.joda.time.DateTime

public class ParsedFile {
	def fileName = ""
	def parsedFile = null
	def propertyMapList = []

	private def setParsedFile(String fileName) {
		println "Parsing file ${fileName}, please wait..."
		parsedFile = new XmlParser().parse(fileName)
		println "Done parsing file"
	}

	private def setPropertyMapList(String parentNode) {
		propertyMapList = []		// Clear out the map list for each new parent node
		println "    Getting elements for root node ${parsedFile.name()}"
		def status = true
		String nodeName = ""
		String propertyName = ""
		String propertyType = ""
		def propertyMap = [:]			// Map with property names and types	
		parsedFile.children().each { node ->
			if (status){	
				// Process the specified node. Only do this once on the first match
				if (node.name() == parentNode) {
					node.children().each {
						if (it.name()){
							// Set the node name
							nodeName = it.name()							
							// println "        Processing node ${nodeName}"
							// Determine property data type
							propertyType = setPropertyType(it)
							// Format property name with camelcase
							if (nodeName.contains('_')) {
								propertyName = getCamelCase(nodeName)
							}
							else {
								propertyName = nodeName.toLowerCase()						
							}
							if (propertyName == "private") {
								propertyName = "privateVar"
							}
							propertyMap = [:]		// Clear the map
							propertyMap["nodeName"] = nodeName
							propertyMap["name"] = propertyName
							propertyMap["type"] = propertyType
							
							// println "        Adding map: ${propertyMap} to list"
							propertyMapList << propertyMap
						}
					}
					status = false
				}
			}
		}	
	}
	
	private def getCamelCase = { p ->
		// println "            Prettifying property ${p}"
		// Lower case entire string
		def prettyProp = p.toLowerCase()
		// Replace double underscores with single
		prettyProp = prettyProp.replaceAll('__','_')				
		// Upper Case all words separated by underscore
		prettyProp = prettyProp.split('_').collect{ it[0].toUpperCase() + it.substring(1) }.join('')	
		// Return camel case word
		prettyProp = prettyProp[0].toLowerCase() + prettyProp.substring(1)
	}

	private def writeListToFile = { fileName, extension, constraint ->
		def line = ""
		def f = new File("$fileName$extension")
		// If the file already exists, append the constraint info for the properties
		if (f.exists()) {
			f.withWriterAppend { out ->
				propertyMapList.each { m ->
					if (constraint) {
						if (m["type"] == "String") {
							line = "${m['name']}(nullable:true,maxSize:75)"											
						}
						else {
							line = "${m['name']}(nullable:true)"																	
						}
					}
					else {						
						line = "${m['type']} ${m['name']}"
					}					
					out.println line
				}
			}		
		}
		else {
			// If file does not exist, create and add properties
			new File("$fileName$extension").withWriter { out ->
				propertyMapList.each { m ->
					if (constraint) {
						if (m["type"] == "String") {
							line = "${m['name']}(nullable:true,maxSize:75)"											
						}
						else {
							line = "${m['name']}(nullable:true)"																	
						}
					}
					else {						
						line = "${m['type']} ${m['name']}"
					}					
					out.println line
				}
			}
		
		}
	}

	private def writeCodeToFile = { fileName, extension, table ->
		def line = ""
		def f = new File("$fileName$extension")
		def nodeName = ""
		new File("$fileName$extension").withWriter { out ->
			propertyMapList.each { m ->
				// Dates require special processing
				if (m["type"] == "Date") {
					nodeName = m["nodeName"]
				
					line = "try {"
					out.println line
					line = "\tdateTimeString = c.${nodeName}.toString()"
					out.println line
					line = "\tif ( ! dateTimeString ) {"
					out.println line
					line = "\t\t${table}.${m['name']} = null"
					out.println line
					line =	"\t} else {"
					out.println line
					line = "\t\tDateTime dt = fmt.parseDateTime(dateTimeString)"
					out.println line
					line = "\t\t${table}.${m['name']} = dt.toCalendar().getTime()"											
					out.println line
					line =	"\t}"
					out.println line
					line = "} catch (Exception e) {"
					out.println line
					def errString = "! Invalid " + nodeName + ": \${c." + nodeName + ".toString()}"
					line = "\tresponse << '${errString}'"
					out.println line
					errString = "! parse " + nodeName + " Input: \${c." + nodeName + ".toString()}"
					line = "\tprintln '${errString}'"
					out.println line
					errString = "! parse " + nodeName + " Exception: \${e.toString()}"
					line = "\tprintln '${errString}'"
					out.println line
					line = "}"					
					// line = "def ${m['name']} = new DateTime(c.${m['nodeName']}.toString())"											
				}
				else {						
					line = "${table}.${m['name']} = c.${m['nodeName']}.toString()"
				}					
				out.println line
			}			
		}		
	}
	
	private def setPropertyType = { node ->
		// Search for a date|time string in the format yyyy-mm-ddThh:mm:ss	example: 2011-02-25T08:44:45
		def type = ""
		if (node.text().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}") ) {
			try {
				//println "Converting ${node.text()} to joda DateTime"
				def theDate = new DateTime(node.text())
				type = "Date"
			}
			catch (Exception ex) {
				println "Could not convert ${node.text()} to a DateTime with error: ${ex}"
				type = "String"
			}
		}
		else {
			type = "String"		
		}
		return type
	}
	
	private def getLongestNodeTextLength = {
		def longestNodeTextLength = 0
		def nodeText = ""
		// Process each node getting the length of any text within the node
		parsedFile.children().each { node ->
			if (node.name()) {
				nodeText = node.text()				
				if (nodeText && longestNodeTextLength < nodeText.length()) {
					longestNodeTextLength = nodeText.length()
				}			
			}
		}	
	
	}
}


// Parse the XML File
def parsedFile = new ParsedFile()
//parsedFile.setParsedFile("transmissionX_1_14APR11.xml")
//parsedFile.setParsedFile("ramsey_nonresponse_list_17JUN11.xml")
//parsedFile.setParsedFile("data_transmission_07sep11_temp.xml")
//parsedFile.setParsedFile("data_transmission_10OCT11.xml")
//parsedFile.setParsedFile("data_transmission_01NOV11.xml")
//parsedFile.setParsedFile("data_transmission_28OCT11.xml")
//parsedFile.setParsedFile("transmission_1_28OCT11.xml")
//parsedFile.setParsedFile("ramsey_nonresponse_list_04NOV11.xml")
//parsedFile.setParsedFile("data_transmission_10NOV11.xml")
//parsedFile.setParsedFile("data_transmission_13NOV11.xml")
parsedFile.setParsedFile("RamseyPPGFollowUp_07NOV11.xml")

def node = ""
def f = null
def propertyListFile = ""
def dataImportCodeFile = ""
def tableName = ""


// Get list of domain class properties from node RAMSEY_PPGFOLLOWUP from RamseyPPGFollowUp_07NOV11.xml - added 2011-11-16 
node = "RAMSEY_PPGFOLLOWUP"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcRamseyPpgFollowup"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"

/*
// Get list of domain class properties from node LOCNLQ_BATCH1 from data_transmission_13NOV11.xml - added 2011-11-16 
node = "LOCNLQ_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcLoCnLqBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"


// Get list of domain class properties from node LBREMINDER_BATCH1 from transmission_1_28OCT11.xml- added 2011-11-16 
node = "LBREMINDER_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcLbReminderBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"


// Get list of domain class properties from node BIRTHINCENTIVE_BATCH1 from transmission_1_28OCT11.xml- added 2011-11-11 
node = "BIRTHINCENTIVE_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcBirthIncentiveBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"


// Get list of domain class properties from node HICON_BATCH1 - added 2011-11-11 
node = "HICON_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcHiConBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"


// Get list of domain class properties from node NONRESPONSE_LIST (adding CATI columns to previously imported non-response-list) - added 2011-11-11 
node = "NONRESPONSE_LIST"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcNonResponseList"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"




// Get list of domain class properties from node LB_BATCH (adding flag_mail field) - added 2011-11-02 
node = "LB1_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcLbBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"
// Get list of domain class properties from node EN_BATCH (adding flag_mail field) - added 2011-11-02 
// EN_BATCH1 was added earlier. Updating domain class properties.
node = "EN_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcEnBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"




// Get list of domain class properties from node PPGFOLLOW_BATCH - added 2011-10-25
node = "PPGFOLLOW_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcPpgFollowBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"



// Get list of domain class properties from node EN_BATCH - added 2011-10-25
node = "EN_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false)
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true)
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcEnBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName)
println "Done!"





// Get list of domain class properties from node COMBO_BATCH1 - added 2011-10-24
node = "COMBO_BATCH1"		// Change for each new table
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcComboBatch"		// This is the new domain class name in web service
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"


// Get list of domain class properties from node BIRTH_BATCH1
node = "BIRTH_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcBirthBatch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node PPV1_BATCH1
node = "PPV1_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcPpv1Batch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node PV1_BATCH1
node = "PV1_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcPv1Batch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node PV2_BATCH1
node = "PV2_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcPv2Batch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

/*
// Get list of domain class properties from node NONRESPONSE_LIST
node = "NONRESPONSE_LIST"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcNonResponseList"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"
*/

/*
// Get list of domain class properties from node HH_BATCH1
node = "HH_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcHhBatch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node LOWCONSENT_BATCH1
node = "LOWCONSENT_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcLowConsentBatch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node LOWQUEX1_BATCH1
node = "LOWQUEX1_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcLowQuex1Batch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"

// Get list of domain class properties from node SCREENER_BATCH1
node = "SCREENER_BATCH1"
parsedFile.setPropertyMapList(node)
// Write properties to a file
propertyListFile = "property_list-${node}"
println "Writing output file ${propertyListFile}"
f = new File("${propertyListFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}
// Add property definitions to file
parsedFile.writeListToFile(propertyListFile, ".txt", false) 
// Add property constraints to file
parsedFile.writeListToFile(propertyListFile, ".txt", true) 
println "Done!"

// Write data import code to a file
dataImportCodeFile = "data_import_code-${node}"
tableName = "norcScreenerBatch"
println "Writing output file ${dataImportCodeFile}"
f = new File("${dataImportCodeFile}.txt")
if (f.exists()){
	try {
		f.delete()	
	}
	catch (Exception ex) {
		println "Could not delete file ${f.name} with ERROR: ${ex}"
	}
}

// Add property definitions to file
parsedFile.writeCodeToFile(dataImportCodeFile, ".txt", tableName) 
println "Done!"
*/

//def longestNodeTextLength = parsedFile.getLongestNodeTextLength()
//println "Longest node text length is ${longestNodeTextLength}"
println "Parsing complete. Huzzah"

