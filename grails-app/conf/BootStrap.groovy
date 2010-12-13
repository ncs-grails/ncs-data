import edu.umn.ncs.*
import edu.umn.ncs.data.*

class BootStrap {

    def init = { servletContext ->

        def today = new Date()
        def appName = 'ncs-data'

		println "Loading bootstrap..."

		def readMailingRole = DataExchangePartnerRole.findByRole("ROLE_READ_MAILING")
		if ( ! readMailingRole ) {
			readMailingRole = new DataExchangePartnerRole(name:"Read Mailing",
				role:"ROLE_READ_MAILING").save()
		}
		
		def writeResultRole = DataExchangePartnerRole.findByRole("ROLE_WRITE_RESULT")
		if ( ! writeResultRole ) {
			writeResultRole = new DataExchangePartnerRole(name:"Write Results",
				role:"ROLE_WRITE_RESULT").save()
		}

		def writeInstrument = DataExchangePartnerRole.findByRole("ROLE_WRITE_INSTRUMENT")
		if ( ! writeInstrument ) {
			writeInstrument = new DataExchangePartnerRole(name:"Write Instrument",
				role:"ROLE_WRITE_INSTRUMENT").save()
		}

		// EnHS Clients
		def localhost = ClientHost.findByIpvFour("127.0.0.1")
		if ( ! localhost ) {
			localhost = new ClientHost(hostname:"localhost",
				userCreated: 'ajz',
				ipvFour:"127.0.0.1",
				ipvSix:"0:0:0:0:0:0:0:1").save()
		}

		def aaronsComputer = ClientHost.findByIpvFour("160.94.224.1")
		if ( ! aaronsComputer ) {
			aaronsComputer = new ClientHost(hostname:"phantom.cccs.umn.edu",
				userCreated: 'ajz',
				ipvFour:"160.94.224.1").save()
		}

		// NORC Clients
		def norcMailServer = ClientHost.findByIpvFour("65.213.192.20")
		if ( ! norcMailServer ) {
			norcMailServer = new ClientHost(hostname:"xmx.norc.net",
				userCreated: 'ajz',
				ipvFour:"65.213.192.20").save()
		}

		def norcWorkstationMartinBarron = ClientHost.findByIpvFour("65.213.192.3")
		if ( ! norcWorkstationMartinBarron ) {
			norcWorkstationMartinBarron = new ClientHost(hostname:"nat.norc.org",
				userCreated: 'ajz',
				ipvFour:"65.213.192.3").save()
		}

		if ( norcWorkstationMartinBarron && readMailingRole ) {

			def norcPartner = DataExchangePartner.findByName("NORC")

			if (! norcPartner) {
				norcPartner = new DataExchangePartner(name:"NORC",
					contactName:"Martin Barron",
					contactEmail:"Barron-Martin@norc.org",
					privateKey:"kenah1ot5chu8Ingu7Phoh9Lionoh1eebah3saiXoetaicohl6aij4aph0eih3oi",
					userCreated:'ajz')

				if (norcPartner.save()) {
					norcPartner.addToRoles(readMailingRole)
					.addToRoles(writeResultRole)
					.addToRoles(writeInstrument)
					.addToAllowedClients(aaronsComputer)
					.addToAllowedClients(localhost)
					.addToAllowedClients(norcWorkstationMartinBarron).save()
				} else {
					println "Failed to create NORC as data exchange partner."

					norcPartner.errors.each{ e ->
						println "${e}"
					}
				
				}
			}
		} else {
			println "Either norcMartinBarron or readMailingRole is missing."
		}

		// BEGIN BATCH BOOTSTRAP CODE

        // Global BootStrap

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

		// add the NORC project ID
        def ncsNorc = StudyLink.findByNorcProjectId("6612")
        if (! ncsNorc) {
			ncsNorc = new StudyLink(study:ncs, norcProjectId:'6612').save()
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

		environments {
			development {

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

				def baseNorcSuId = 123456

				myAddressList.each{
					def sa = new StreetAddress(address:it[0],
						city:it[1], state:it[2], zipCode:it[3], zip4:it[4],
						country:us, county:'Ramsey', appCreated:'byHand').save()

					def du = new DwellingUnit(address:sa,
						appCreated:'byHand').save()

					def suId = "00${baseNorcSuId}00"
					baseNorcSuId++

					// adding NORC SU_IDs
					def duNorc = new DwellingUnitLink(dwellingUnit: du,
						deliverySequenceId: 1, norcSuId:suId).save()

					//println "Created Dwelling unit: ${du?.id}:${sa?.id}"
				}

				def advLetter = new Instrument(name:'Advance Letter',
					nickName:'ADV', study:ncs, requiresPrimaryContact:false).save()

		
				// adding NORC Link for the Doc ID
				def norcAdvLetter = new InstrumentLink(instrument: advLetter,
					norcDocId: "80", norcQuexId: "ADVBRO",
					norcDescription:"Advance Letter and Brochure").save()


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

					if (batchAdv) {
						// reload from the database
						batchAdv.refresh()

						// assign a mail date to batches 1 and 3
						if ( it == 1 || it == 3 ){
							batchAdv.mailDate = today
						}

						// assign an addressing and mailing date to batches 5 and 6
						if ( it == 5 || it == 7 ){
							batchAdv.addressAndMailingDate = today + 1
						}

						// set the instrument date for batches 4 and 8
						if ( it == 4 || it == 8 ){
							batchAdv.instrumentDate = today - 1
						}
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
			}
		}
		// END DEVELOPMENT ENVIRONMENT CODE

        // add 'capitalize()' function to Strings
        String.metaClass.capitalize = {->
            return delegate.tokenize().collect{ word ->
                word.substring(0,1).toUpperCase() + word.substring(1, word.size())
            }.join(' ')
        }

        // Add a norc doc ID to the Batch Class
        Batch.metaClass.getNorcDocId = {->
            return InstrumentLink.findByInstrument(delegate.primaryInstrument)?.norcDocId
        }

        Batch.metaClass.getNorcProjectId = {->
            return StudyLink.findByStudy(delegate.primaryInstrument.study)?.norcProjectId
        }

		// Add norcDocId to Instrument class
        Instrument.metaClass.getNorcDocId = {->
            return InstrumentLink.findByInstrument(delegate)?.norcDocId
        }

        Instrument.metaClass.getNorcProjectId = {->
            return StudyLink.findByStudy(delegate.study)?.norcProjectId
        }

        // Add a norc doc ID to the Batch Class
        Study.metaClass.getNorcProjectId = {->
            return StudyLink.findByStudy(delegate)?.norcProjectId
        }

        // Add a norc doc ID to the Batch Class
        DwellingUnit.metaClass.getNorcSuId = {->
            return DwellingUnitLink.findByDwellingUnit(delegate)?.norcSuId
        }

    }
    def destroy = {
    }
}
