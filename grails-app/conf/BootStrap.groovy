import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->

		println "Loading bootstrap..."

		def demoRole = new DataExchangePartnerRole(name:"Demonstration",
			role:"ROLE_DEMO")
		
		if ( ! demoRole.save() ) {
			println "Error saving 'demoRole'"
			println "Error:  ${demoRole.errors}"
			println ""
		}

		def anywhere = new ClientHost(hostname:"*", userCreated:"ajz")

		if ( ! anywhere.save() ) {
			println "Error saving 'anywhere'"
			println "Errors: ${anywhere.errors}"
			println ""
		}

		def aaronsComputer = new ClientHost(hostname:"phantom.cccs.umn.edu",
			ipvFour:"160.94.224.1").save()

		if (anywhere && demoRole) {
			def demoPartner = new DataExchangePartner(name:"Demo",
				contactName:"Demonstration",
				contactEmail:"info@ncs.umn.edu",
				privatekey:"DEMO",
				userCreated:"ajz")
			.addToRoles(demoRole)
			.addToAllowedClients(anywhere).save()
		} else {
			println "Either anywhere or demoRole is missing."
		}


		// BEGIN BATCH BOOTSTRAP CODE

        // Global BootStrap

        def today = new Date()
        def appName = 'ncs-case-management'

        /*** People Seciton ***/
        /* Items: AddressType, ContactRole, Country, EmailType,
         * EnrollmentType, Ethnicity, Gender, PhoneType, RelationshipType,
         * Study */

        //AddressType
        def homeAddress = AddressType.findByName('home')
        if (! homeAddress) {
            homeAddress = new AddressType(name:'home').save()
        }
        def seasonalAddress = AddressType.findByName('seasonal')
        if (! homeAddress) {
            seasonalAddress = new AddressType(name:'seasonal').save()
        }
        //ContactRoleType
        def emergencyContact = ContactRoleType.findByName('emergency')
        if (! emergencyContact) {
            emergencyContact = new ContactRoleType(name:'emergency').save()
        }
        //Country
        def us = Country.findByAbbreviation('us')
        if (! us) {
            us = new Country(name:'United States', abbreviation:'us').save()
        }
        //EmailType
        def personalEmail = EmailType.findByName('personal')
        if (! personalEmail) {
            personalEmail = new EmailType(name:'personal').save()
        }
        //EnrollmentType
        def highIntensity = EnrollmentType.findByName('high intensity')
        if (! highIntensity) {
            highIntensity = new EnrollmentType(name:'high intensity').save()
        }
        //Ethnicity
        def latino = Ethnicity.findByName('latino')
        if (! latino) {
            latino = new Ethnicity(name:'latino').save()
        }
        //Gender
        def male = Gender.findByName('male')
        if (! male) {
            male = new Gender(name:'male').save()
        }
        def female = Gender.findByName('female')
        if (! female) {
            female = new Gender(name:'female').save()
        }
        //PhoneType
        def mobilePhone = PhoneType.findByName('mobile')
        if (! mobilePhone) {
            mobilePhone = new PhoneType(name:'mobile').save()
        }
        //RelationshipType
        def mother = RelationshipType.findByName('mother')
        if (! mother) {
            mother = new RelationshipType(name:'mother').save()
        }
        //Study
        def ncs = Study.findByName("National Children's Study")
        if (! ncs) {
            ncs = new Study(name:"NCS",
                fullName:"National Children's Study",
                active:true, sponsor:'NCI/NICHD',
                coordinator:"NICHD",
                collaborator:"TBA",
                purpose:"improving the health of america's children",
                exclusionary: false)
            if (!ncs.save()) {
                ncs.errors.each{
                    println "${it}"
                }
            }
        }

        /*** Tracking Seciton ***/
        /* Items: BatchDirection, InstrumentFormat, IsInitial, Result
         */

        // BatchDirection
        def outgoing = BatchDirection.findByName('outgoing')
        if (! outgoing) {
            outgoing = new BatchDirection(name:'outgoing').save()
        }
        def incoming = BatchDirection.findByName('incoming')
        if (! incoming) {
            incoming = new BatchDirection(name:'incoming').save()
        }
        def internal = BatchDirection.findByName('internal')
        if (! internal) {
            internal = new BatchDirection(name:'internal').save()
        }

        // InstrumentFormat
        def firstClassMail = InstrumentFormat.findByName('first class mail')
        if (!firstClassMail) {
            firstClassMail = new InstrumentFormat(name:'first class mail', groupName:'mail').save()
        }
        def inPerson = InstrumentFormat.findByName('in person')
        if (!inPerson) {
            inPerson = new InstrumentFormat(name:'in person', groupName:'person').save()
        }
        def onThePhone = InstrumentFormat.findByName('phone')
        if (!onThePhone) {
            onThePhone = new InstrumentFormat(name:'phone', groupName:'phone').save()
        }

        // IsInitial
        def initial = IsInitial.findByName('initial')
        if (!initial) {
            initial = new IsInitial(name:'initial').save()
        }
        def reminder = IsInitial.findByName('reminder')
        if (!reminder) {
            reminder = new IsInitial(name:'reminder').save()
        }

        // Result
        def received = Result.findByName('received')
        if (!received) {
            received = new Result(name:'received').save()
        }

        // Relationship between sub batches
        // id = 1
        def attachmentOf = BatchCreationItemRelation.findByName('attachment')
        if (!attachmentOf) {
            attachmentOf = new BatchCreationItemRelation(name:'attachment').save()
        }
        // id = 2
        def childOf = BatchCreationItemRelation.findByName('child')
        if (!childOf) {
            childOf = new BatchCreationItemRelation(name:'child').save()
        }
        // id = 3
        def sisterOf = BatchCreationItemRelation.findByName('sister')
        if (!sisterOf) {
            sisterOf = new BatchCreationItemRelation(name:'sister').save()
        }

        // Source of the Batch
        // id = 1
        def personSource = BatchCreationQueueSource.findByName("person")
        if (!personSource) {
            personSource = new BatchCreationQueueSource(name:'person').save()
        }

        // id = 2
        def householdSource = BatchCreationQueueSource.findByName("household")
        if (!householdSource) {
            householdSource = new BatchCreationQueueSource(name:'household').save()
        }

        // id = 3
        def dwellingUnitSource = BatchCreationQueueSource.findByName("dwellingUnit")
        if (!dwellingUnitSource) {
            dwellingUnitSource = new BatchCreationQueueSource(name:'dwellingUnit').save()
        }

        // Test Data

		/*
		 * 1636 BREDA AVE            |           |          | SAINT PAUL | MN         | 55108 | 2701 |
		 * 4 WYOMING ST E            |           |          | SAINT PAUL | MN         | 55107 | 3240 |
		 * 1372 HAZEL ST N           |           |          | SAINT PAUL | MN         | 55119 | 4507 |
		 * 180 WAYZATA ST            | APT       | 114      | SAINT PAUL | MN         | 55117 | 5351 |
		 * 2122 WOODLYNN AVE         | APT       | 4        | SAINT PAUL | MN         | 55109 | 1480 |
		 * 3744 CLEVELAND AVE N      | APT       | 104      | SAINT PAUL | MN         | 55112 | 3264 |
		 * 1255 FLANDRAU ST          |           |          | SAINT PAUL | MN         | 55106 | 2302 |
		 * 4310 OLD WHITE BEAR AVE N |           |          | SAINT PAUL | MN         | 55110 | 3874 |
		 * 1131 MARION ST            |           |          | SAINT PAUL | MN         | 55117 | 4461 |
		 * 305 EDMUND AVE            |           |          | SAINT PAUL | MN         | 55103 | 1708 |
		 * 1412 COUNTY ROAD E W      |           |          | SAINT PAUL | MN         | 55112 | 3653 |
		 * 1952 OAK KNOLL DR         |           |          | SAINT PAUL | MN         | 55110 | 4263 |
		 * 480 GERANIUM AVE E        |           |          | SAINT PAUL | MN         | 55130 | 3709 |
		 * 1140 4TH ST E             | APT       | 306      | SAINT PAUL | MN         | 55106 | 5353 |
		 * 1793 MORGAN AVE           |           |          | SAINT PAUL | MN         | 55116 | 2721 |
		 * 346 CLEVELAND AVE SW      | APT       | 14       | SAINT PAUL | MN         | 55112 | 3535 |
		 * 1575 SAINT PAUL AVE       | APT       | 9        | SAINT PAUL | MN         | 55116 | 2862 |
		 * 4041 BETHEL DR            | APT       | 27       | SAINT PAUL | MN         | 55112 | 6921 |
		 * 1265 3RD ST E             |           |          | SAINT PAUL | MN         | 55106 | 5778 |
		 * 1528 BREDA AVE            |           |          | SAINT PAUL | MN         | 55108 | 2610 |
		 */

		def myAddressList = [
			['1636 BREDA AVE', 'SAINT PAUL', 'MN', '55108', '2701'],
			['WYOMING ST E', 'SAINT PAUL', 'MN', '55107', '3240'],
			['1372 HAZEL ST N', 'SAINT PAUL', 'MN', '55119', '4507 '],
			['180 WAYZATA ST APT 114', 'SAINT PAUL', 'MN', '55117', '5351'],
			['2122 WOODLYNN AVE APT 4', 'SAINT PAUL', 'MN', '55109', '1480'],
			['3744 CLEVELAND AVE N APT 104', 'SAINT PAUL', 'MN', '55112', '3264'],
			['1255 FLANDRAU ST', 'SAINT PAUL', 'MN', '55106', '2302'],
			['4310 OLD WHITE BEAR AVE N', 'SAINT PAUL', 'MN', '55110', '3874'],
			['1131 MARION ST', 'SAINT PAUL', 'MN', '55117', '4461'],
			['305 EDMUND AVE', 'SAINT PAUL', 'MN', '55103', '1708'],
			['1412 COUNTY ROAD E W', 'SAINT PAUL', 'MN', '55112', '3653'],
			['1952 OAK KNOLL DR', 'SAINT PAUL', 'MN', '55110', '4263'],
			['480 GERANIUM AVE E', 'SAINT PAUL', 'MN', '55130', '3709'],
			['1140 4TH ST E APT 306', 'SAINT PAUL', 'MN', '55106', '5353'],
			['1793 MORGAN AVE', 'SAINT PAUL', 'MN', '55116', '2721'],
			['346 CLEVELAND AVE SW APT 14', 'SAINT PAUL', 'MN', '55112', '3535'],
			['1575 SAINT PAUL AVE APT 9', 'SAINT PAUL', 'MN', '55116', '2862'],
			['4041 BETHEL DR APT 27', 'SAINT PAUL', 'MN', '55112', '6921'],
			['1265 3RD ST E','SAINT PAUL', 'MN', '55106', '5778'],
			['1528 BREDA AVE','SAINT PAUL', 'MN', '55108', '2610']
		]

		myAddressList.each{
			def sa = new StreetAddress(address:it[0],
				city:it[1], state:it[2], zipCode:it[3], zip4:it[4],
				country:us, county:'Ramsey', appCreated:'byHand').save()

			def du = new DwellingUnit(address:sa,
				appCreated:'byHand').save()

			//println "Created Dwelling unit: ${du?.id}:${sa?.id}"
		}

		def advLetter = new Instrument(name:'Advance Letter',
			nickName:'ADV', study:ncs, requiresPrimaryContact:false).save()


		def bccAdv = new BatchCreationConfig(name:'Advance Mailing',
			instrument:advLetter, format:firstClassMail, direction: outgoing,
			isInitial:initial, selectionQuery:"", active:true, manualSelection:true,
			oneBatchEventPerson:true).save()

		// add a document
		bccAdv.addToDocuments(
			documentLocation:'n:/production documents/advance letter/ncs_advance_letter_merge.docx')
		.save()


		// 10 Fake Mailings
		// generate a batch

		(1..10).each{
					
			def batchAdv = new Batch(format:firstClassMail,
				direction: outgoing, instrumentDate: today, batchRunBy:'ajz',
				batchRunByWhat: appName, trackingDocumentSent:false,
				creationConfig: bccAdv)

			bccAdv.addToBatches(batchAdv)

			if (! bccAdv.save() ) {
				println "ERRORS:"
				bccAdv.errors.each{ error ->
					println "ERROR>> ${error} "
				}
				println ""
			} else {
				// add an instrument
				batchAdv.addToInstruments(instrument:advLetter, isInitial:initial).save()

			}

			// Add Items to batch
			def dwellingUnitInstanceList = DwellingUnit.list()

			dwellingUnitInstanceList.each{ du ->
				batchAdv.addToItems(dwellingUnit:du)
			}

			if (! batchAdv.save() ) {
				println "ERRORS adding to batch:"
				batchAdv.errors.each{ error ->
					println "ERROR>> ${error} "
				}
				println ""
			}

			println "Created Batch: ${batchAdv.id}"

        }

        // add 'capitalize()' function to Strings
        String.metaClass.capitalize = {->
            return delegate.tokenize().collect{ word ->
                word.substring(0,1).toUpperCase() + word.substring(1, word.size())
            }.join(' ')
        }

    }
    def destroy = {
    }
}
