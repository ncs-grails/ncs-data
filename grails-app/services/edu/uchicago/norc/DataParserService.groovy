package edu.uchicago.norc

import grails.converters.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.*
import groovyx.gpars.GParsPool

class DataParserService {
	def debug = false
	
	static transactional = true
	def sessionFactory

	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
	DateTimeFormatter fmtDateOnly = DateTimeFormat.forPattern("yyyy-MM-dd")
	
	def parseEverything(table, response) {
		// TODO send data by table name so the entire file is not parsed each time 
		parseFormats table, response
		parseConsent table, response
		parseIncentive table, response
		parseBirthBatch table, response
		parseHhBatch table, response
		parseLowConsentBatch table, response
		parseLowQuex1Batch table, response
		parsePpv1Batch table, response
		parsePv1Batch table, response
		parsePv2Batch table, response
		parseScreenerBatch table, response
		parseNonResponseList table, response
		parseComboBatch table, response
	}

	def parseHhBatch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing HH_BATCH1"			
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.HH_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.HH_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcHhBatch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcHhBatch = NorcHhBatch.findByRespondentSerial(checkSerial)				
		
					if (!norcHhBatch) {
						norcHhBatch = new NorcHhBatch()
						response <<  " + Creating new NorcHhBatch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcHhBatch.lock()
						response <<  " ~ Updating existing NorcHhBatch(${checkSerial})\n"
					}
					
					norcHhBatch.respondentSerial = c.Respondent_Serial.toString()
					
					norcHhBatch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcHhBatch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcHhBatch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcHhBatch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcHhBatch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcHhBatch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcHhBatch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcHhBatch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcHhBatch.respondentId = c.Respondent_ID.toString()
					norcHhBatch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcHhBatch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcHhBatch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcHhBatch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcHhBatch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcHhBatch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcHhBatch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcHhBatch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcHhBatch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcHhBatch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcHhBatch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcHhBatch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcHhBatch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcHhBatch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcHhBatch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcHhBatch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcHhBatch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcHhBatch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcHhBatch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcHhBatch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcHhBatch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcHhBatch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcHhBatch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcHhBatch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcHhBatch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcHhBatch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcHhBatch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcHhBatch.datacleaningNote = c.DataCleaning_Note.toString()
					norcHhBatch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcHhBatch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcHhBatch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcHhBatch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcHhBatch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcHhBatch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcHhBatch.suId = c.SU_ID.toString()
					norcHhBatch.sampleUnitKey = c.sample_unit_key.toString()
					norcHhBatch.affiliate = c.AFFILIATE.toString()
					norcHhBatch.majority = c.MAJORITY.toString()
					norcHhBatch.method = c.METHOD.toString()
					norcHhBatch.hiqflag = c.HIQflag.toString()
					norcHhBatch.hiqTreatment = c.HIQ_treatment.toString()
					norcHhBatch.hiq = c.HIQ.toString()
					norcHhBatch.hiqreturn = c.HIQRETURN.toString()
					norcHhBatch.incentive = c.INCENTIVE.toString()
					norcHhBatch.psuId = c.PSU_ID.toString()
					norcHhBatch.psuIdCodes = c.PSU_ID_Codes.toString()
					norcHhBatch.institution = c.INSTITUTION.toString()
					norcHhBatch.institutionCodes = c.INSTITUTION_Codes.toString()
					norcHhBatch.prAddress1 = c.PR_ADDRESS_1.toString()
					norcHhBatch.prAddress1Codes = c.PR_ADDRESS_1_Codes.toString()
					norcHhBatch.prAddress2 = c.PR_ADDRESS_2.toString()
					norcHhBatch.prAddress2Codes = c.PR_ADDRESS_2_Codes.toString()
					norcHhBatch.prUnit = c.PR_UNIT.toString()
					norcHhBatch.prUnitCodes = c.PR_UNIT_Codes.toString()
					norcHhBatch.prCity = c.PR_CITY.toString()
					norcHhBatch.prCityCodes = c.PR_CITY_Codes.toString()
					norcHhBatch.prState = c.PR_STATE.toString()
					norcHhBatch.prStateCodes = c.PR_STATE_Codes.toString()
					norcHhBatch.prZip = c.PR_ZIP.toString()
					norcHhBatch.prZipCodes = c.PR_ZIP_Codes.toString()
					norcHhBatch.prZip4 = c.PR_ZIP4.toString()
					norcHhBatch.prZip4Codes = c.PR_ZIP4_Codes.toString()
					norcHhBatch.hiqRFname = c.HIQ_R_FName.toString()
					norcHhBatch.hiqRFnameCodes = c.HIQ_R_FName_Codes.toString()
					norcHhBatch.hiqRLname = c.HIQ_R_LName.toString()
					norcHhBatch.hiqRLnameCodes = c.HIQ_R_LName_Codes.toString()
					norcHhBatch.hiqPhoneNbr = c.HIQ_phone_NBR.toString()
					norcHhBatch.hiqPhoneNbrCodes = c.HIQ_phone_NBR_Codes.toString()
					norcHhBatch.hiqPhoneType = c.HIQ_phone_type.toString()
					norcHhBatch.hiqPhoneTypeCodes = c.HIQ_phone_type_Codes.toString()
					norcHhBatch.hiqPhoneTypeOth = c.HIQ_phone_type_OTH.toString()
					norcHhBatch.hiqPhoneTypeOthCodes = c.HIQ_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqRGender = c.HIQ_R_gender.toString()
					norcHhBatch.hiqRGenderCodes = c.HIQ_R_gender_Codes.toString()
					norcHhBatch.hiqHhMember = c.HIQ_HH_member.toString()
					norcHhBatch.hiqHhMemberCodes = c.HIQ_HH_member_Codes.toString()
					norcHhBatch.hiqPrivate = c.HIQ_private.toString()
					norcHhBatch.hiqPrivateCodes = c.HIQ_private_Codes.toString()
					norcHhBatch.hiqNumAdult = c.HIQ_NUM_ADULT.toString()
					norcHhBatch.hiqNumAdultCodes = c.HIQ_NUM_ADULT_Codes.toString()
					norcHhBatch.hiqNumMale = c.HIQ_num_male.toString()
					norcHhBatch.hiqNumMaleCodes = c.HIQ_num_male_Codes.toString()
					norcHhBatch.hiqNumFemale = c.HIQ_num_female.toString()
					norcHhBatch.hiqNumFemaleCodes = c.HIQ_num_female_Codes.toString()
					norcHhBatch.hiqP1Fname = c.HIQ_P1_Fname.toString()
					norcHhBatch.hiqP1FnameCodes = c.HIQ_P1_Fname_Codes.toString()
					norcHhBatch.hiqP1Lname = c.HIQ_P1_Lname.toString()
					norcHhBatch.hiqP1LnameCodes = c.HIQ_P1_Lname_Codes.toString()
					norcHhBatch.hiqP1Age = c.HIQ_P1_age.toString()
					norcHhBatch.hiqP1AgeCodes = c.HIQ_P1_age_Codes.toString()
					norcHhBatch.hiqP1Preg = c.HIQ_P1_preg.toString()
					norcHhBatch.hiqP1PregCodes = c.HIQ_P1_preg_Codes.toString()
					norcHhBatch.hiqP1PhoneNbr = c.HIQ_P1_phone_NBR.toString()
					norcHhBatch.hiqP1PhoneNbrCodes = c.HIQ_P1_phone_NBR_Codes.toString()
					norcHhBatch.hiqP1PhoneType = c.HIQ_P1_phone_type.toString()
					norcHhBatch.hiqP1PhoneTypeCodes = c.HIQ_P1_phone_type_Codes.toString()
					norcHhBatch.hiqP1PhoneTypeOth = c.HIQ_P1_phone_type_OTH.toString()
					norcHhBatch.hiqP1PhoneTypeOthCodes = c.HIQ_P1_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP1Email = c.HIQ_P1_email.toString()
					norcHhBatch.hiqP1EmailCodes = c.HIQ_P1_email_Codes.toString()
					norcHhBatch.hiqP2Fname = c.HIQ_P2_Fname.toString()
					norcHhBatch.hiqP2FnameCodes = c.HIQ_P2_Fname_Codes.toString()
					norcHhBatch.hiqP2Lname = c.HIQ_P2_Lname.toString()
					norcHhBatch.hiqP2LnameCodes = c.HIQ_P2_Lname_Codes.toString()
					norcHhBatch.hiqP2Age = c.HIQ_P2_age.toString()
					norcHhBatch.hiqP2AgeCodes = c.HIQ_P2_age_Codes.toString()
					norcHhBatch.hiqP2Preg = c.HIQ_P2_preg.toString()
					norcHhBatch.hiqP2PregCodes = c.HIQ_P2_preg_Codes.toString()
					norcHhBatch.hiqP2PhoneNbr = c.HIQ_P2_phone_NBR.toString()
					norcHhBatch.hiqP2PhoneNbrCodes = c.HIQ_P2_phone_NBR_Codes.toString()
					norcHhBatch.hiqP2PhoneType = c.HIQ_P2_phone_type.toString()
					norcHhBatch.hiqP2PhoneTypeCodes = c.HIQ_P2_phone_type_Codes.toString()
					norcHhBatch.hiqP2PhoneTypeOth = c.HIQ_P2_phone_type_OTH.toString()
					norcHhBatch.hiqP2PhoneTypeOthCodes = c.HIQ_P2_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP2Email = c.HIQ_P2_email.toString()
					norcHhBatch.hiqP2EmailCodes = c.HIQ_P2_email_Codes.toString()
					norcHhBatch.hiqP3Fname = c.HIQ_P3_Fname.toString()
					norcHhBatch.hiqP3FnameCodes = c.HIQ_P3_Fname_Codes.toString()
					norcHhBatch.hiqP3Lname = c.HIQ_P3_Lname.toString()
					norcHhBatch.hiqP3LnameCodes = c.HIQ_P3_Lname_Codes.toString()
					norcHhBatch.hiqP3Age = c.HIQ_P3_age.toString()
					norcHhBatch.hiqP3AgeCodes = c.HIQ_P3_age_Codes.toString()
					norcHhBatch.hiqP3Preg = c.HIQ_P3_preg.toString()
					norcHhBatch.hiqP3PregCodes = c.HIQ_P3_preg_Codes.toString()
					norcHhBatch.hiqP3PhoneNbr = c.HIQ_P3_phone_NBR.toString()
					norcHhBatch.hiqP3PhoneNbrCodes = c.HIQ_P3_phone_NBR_Codes.toString()
					norcHhBatch.hiqP3PhoneType = c.HIQ_P3_phone_type.toString()
					norcHhBatch.hiqP3PhoneTypeCodes = c.HIQ_P3_phone_type_Codes.toString()
					norcHhBatch.hiqP3PhoneTypeOth = c.HIQ_P3_phone_type_OTH.toString()
					norcHhBatch.hiqP3PhoneTypeOthCodes = c.HIQ_P3_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP3Email = c.HIQ_P3_email.toString()
					norcHhBatch.hiqP3EmailCodes = c.HIQ_P3_email_Codes.toString()
					norcHhBatch.hiqP4Fname = c.HIQ_P4_Fname.toString()
					norcHhBatch.hiqP4FnameCodes = c.HIQ_P4_Fname_Codes.toString()
					norcHhBatch.hiqP4Lname = c.HIQ_P4_Lname.toString()
					norcHhBatch.hiqP4LnameCodes = c.HIQ_P4_Lname_Codes.toString()
					norcHhBatch.hiqP4Age = c.HIQ_P4_age.toString()
					norcHhBatch.hiqP4AgeCodes = c.HIQ_P4_age_Codes.toString()
					norcHhBatch.hiqP4Preg = c.HIQ_P4_preg.toString()
					norcHhBatch.hiqP4PregCodes = c.HIQ_P4_preg_Codes.toString()
					norcHhBatch.hiqP4PhoneNbr = c.HIQ_P4_phone_NBR.toString()
					norcHhBatch.hiqP4PhoneNbrCodes = c.HIQ_P4_phone_NBR_Codes.toString()
					norcHhBatch.hiqP4PhoneType = c.HIQ_P4_phone_type.toString()
					norcHhBatch.hiqP4PhoneTypeCodes = c.HIQ_P4_phone_type_Codes.toString()
					norcHhBatch.hiqP4PhoneTypeOth = c.HIQ_P4_phone_type_OTH.toString()
					norcHhBatch.hiqP4PhoneTypeOthCodes = c.HIQ_P4_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP4Email = c.HIQ_P4_email.toString()
					norcHhBatch.hiqP4EmailCodes = c.HIQ_P4_email_Codes.toString()
					norcHhBatch.hiqP5Fname = c.HIQ_P5_Fname.toString()
					norcHhBatch.hiqP5FnameCodes = c.HIQ_P5_Fname_Codes.toString()
					norcHhBatch.hiqP5Lname = c.HIQ_P5_Lname.toString()
					norcHhBatch.hiqP5LnameCodes = c.HIQ_P5_Lname_Codes.toString()
					norcHhBatch.hiqP5Age = c.HIQ_P5_age.toString()
					norcHhBatch.hiqP5AgeCodes = c.HIQ_P5_age_Codes.toString()
					norcHhBatch.hiqP5Preg = c.HIQ_P5_preg.toString()
					norcHhBatch.hiqP5PregCodes = c.HIQ_P5_preg_Codes.toString()
					norcHhBatch.hiqP5PhoneNbr = c.HIQ_P5_phone_NBR.toString()
					norcHhBatch.hiqP5PhoneNbrCodes = c.HIQ_P5_phone_NBR_Codes.toString()
					norcHhBatch.hiqP5PhoneType = c.HIQ_P5_phone_type.toString()
					norcHhBatch.hiqP5PhoneTypeCodes = c.HIQ_P5_phone_type_Codes.toString()
					norcHhBatch.hiqP5PhoneTypeOth = c.HIQ_P5_phone_type_OTH.toString()
					norcHhBatch.hiqP5PhoneTypeOthCodes = c.HIQ_P5_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP5Email = c.HIQ_P5_email.toString()
					norcHhBatch.hiqP5EmailCodes = c.HIQ_P5_email_Codes.toString()
					norcHhBatch.hiqP6Fname = c.HIQ_P6_Fname.toString()
					norcHhBatch.hiqP6FnameCodes = c.HIQ_P6_Fname_Codes.toString()
					norcHhBatch.hiqP6Lname = c.HIQ_P6_Lname.toString()
					norcHhBatch.hiqP6LnameCodes = c.HIQ_P6_Lname_Codes.toString()
					norcHhBatch.hiqP6Age = c.HIQ_P6_age.toString()
					norcHhBatch.hiqP6AgeCodes = c.HIQ_P6_age_Codes.toString()
					norcHhBatch.hiqP6Preg = c.HIQ_P6_preg.toString()
					norcHhBatch.hiqP6PregCodes = c.HIQ_P6_preg_Codes.toString()
					norcHhBatch.hiqP6PhoneNbr = c.HIQ_P6_phone_NBR.toString()
					norcHhBatch.hiqP6PhoneNbrCodes = c.HIQ_P6_phone_NBR_Codes.toString()
					norcHhBatch.hiqP6PhoneType = c.HIQ_P6_phone_type.toString()
					norcHhBatch.hiqP6PhoneTypeCodes = c.HIQ_P6_phone_type_Codes.toString()
					norcHhBatch.hiqP6PhoneTypeOth = c.HIQ_P6_phone_type_OTH.toString()
					norcHhBatch.hiqP6PhoneTypeOthCodes = c.HIQ_P6_phone_type_OTH_Codes.toString()
					norcHhBatch.hiqP6Email = c.HIQ_P6_email.toString()
					norcHhBatch.hiqP6EmailCodes = c.HIQ_P6_email_Codes.toString()
					norcHhBatch.hiqPregnant = c.HIQ_PREGNANT.toString()
					norcHhBatch.hiqNumPreg = c.HIQ_NUM_PREG.toString()
					norcHhBatch.hiqAgeElig = c.HIQ_AGE_ELIG.toString()
					norcHhBatch.hhAgeElig = c.HH_AGE_ELIG.toString()
					norcHhBatch.hiqpregP1Pfname = c.HIQPreg_p1_pfname.toString()
					norcHhBatch.hiqpregP2Pfname = c.HIQPreg_p2_pfname.toString()
					norcHhBatch.hiqpregP3Pfname = c.HIQPreg_p3_pfname.toString()
					norcHhBatch.hiqpregP4Pfname = c.HIQPreg_p4_pfname.toString()
					norcHhBatch.hiqpregP5Pfname = c.HIQPreg_p5_pfname.toString()
					norcHhBatch.hiqpregP6Pfname = c.HIQPreg_p6_pfname.toString()
					norcHhBatch.hiqpregP1Plname = c.HIQPreg_p1_plname.toString()
					norcHhBatch.hiqpregP2Plname = c.HIQPreg_p2_plname.toString()
					norcHhBatch.hiqpregP3Plname = c.HIQPreg_p3_plname.toString()
					norcHhBatch.hiqpregP4Plname = c.HIQPreg_p4_plname.toString()
					norcHhBatch.hiqpregP5Plname = c.HIQPreg_p5_plname.toString()
					norcHhBatch.hiqpregP6Plname = c.HIQPreg_p6_plname.toString()
					norcHhBatch.hiqpregP1PAge = c.HIQPreg_p1_p_age.toString()
					norcHhBatch.hiqpregP2PAge = c.HIQPreg_p2_p_age.toString()
					norcHhBatch.hiqpregP3PAge = c.HIQPreg_p3_p_age.toString()
					norcHhBatch.hiqpregP4PAge = c.HIQPreg_p4_p_age.toString()
					norcHhBatch.hiqpregP5PAge = c.HIQPreg_p5_p_age.toString()
					norcHhBatch.hiqpregP6PAge = c.HIQPreg_p6_p_age.toString()
					norcHhBatch.hiqpregP1Preg = c.HIQPreg_p1_preg.toString()
					norcHhBatch.hiqpregP2Preg = c.HIQPreg_p2_preg.toString()
					norcHhBatch.hiqpregP3Preg = c.HIQPreg_p3_preg.toString()
					norcHhBatch.hiqpregP4Preg = c.HIQPreg_p4_preg.toString()
					norcHhBatch.hiqpregP5Preg = c.HIQPreg_p5_preg.toString()
					norcHhBatch.hiqpregP6Preg = c.HIQPreg_p6_preg.toString()
					norcHhBatch.hiqpregP1Pphone = c.HIQPreg_p1_pphone.toString()
					norcHhBatch.hiqpregP2Pphone = c.HIQPreg_p2_pphone.toString()
					norcHhBatch.hiqpregP3Pphone = c.HIQPreg_p3_pphone.toString()
					norcHhBatch.hiqpregP4Pphone = c.HIQPreg_p4_pphone.toString()
					norcHhBatch.hiqpregP5Pphone = c.HIQPreg_p5_pphone.toString()
					norcHhBatch.hiqpregP6Pphone = c.HIQPreg_p6_pphone.toString()
					norcHhBatch.hiqpregP1Pphonetyp = c.HIQPreg_p1_pphonetyp.toString()
					norcHhBatch.hiqpregP2Pphonetyp = c.HIQPreg_p2_pphonetyp.toString()
					norcHhBatch.hiqpregP3Pphonetyp = c.HIQPreg_p3_pphonetyp.toString()
					norcHhBatch.hiqpregP4Pphonetyp = c.HIQPreg_p4_pphonetyp.toString()
					norcHhBatch.hiqpregP5Pphonetyp = c.HIQPreg_p5_pphonetyp.toString()
					norcHhBatch.hiqpregP6Pphonetyp = c.HIQPreg_p6_pphonetyp.toString()
					norcHhBatch.hiqpregP1Email = c.HIQPreg_p1_email.toString()
					norcHhBatch.hiqpregP2Email = c.HIQPreg_p2_email.toString()
					norcHhBatch.hiqpregP3Email = c.HIQPreg_p3_email.toString()
					norcHhBatch.hiqpregP4Email = c.HIQPreg_p4_email.toString()
					norcHhBatch.hiqpregP5Email = c.HIQPreg_p5_email.toString()
					norcHhBatch.hiqpregP6Email = c.HIQPreg_p6_email.toString()
					norcHhBatch.hiqeligE1Efname = c.HIQElig_e1_efname.toString()
					norcHhBatch.hiqeligE2Efname = c.HIQElig_e2_efname.toString()
					norcHhBatch.hiqeligE3Efname = c.HIQElig_e3_efname.toString()
					norcHhBatch.hiqeligE4Efname = c.HIQElig_e4_efname.toString()
					norcHhBatch.hiqeligE5Efname = c.HIQElig_e5_efname.toString()
					norcHhBatch.hiqeligE6Efname = c.HIQElig_e6_efname.toString()
					norcHhBatch.hiqeligE1Elname = c.HIQElig_e1_elname.toString()
					norcHhBatch.hiqeligE2Elname = c.HIQElig_e2_elname.toString()
					norcHhBatch.hiqeligE3Elname = c.HIQElig_e3_elname.toString()
					norcHhBatch.hiqeligE4Elname = c.HIQElig_e4_elname.toString()
					norcHhBatch.hiqeligE5Elname = c.HIQElig_e5_elname.toString()
					norcHhBatch.hiqeligE6Elname = c.HIQElig_e6_elname.toString()
					norcHhBatch.hiqeligE1EAge = c.HIQElig_e1_e_age.toString()
					norcHhBatch.hiqeligE2EAge = c.HIQElig_e2_e_age.toString()
					norcHhBatch.hiqeligE3EAge = c.HIQElig_e3_e_age.toString()
					norcHhBatch.hiqeligE4EAge = c.HIQElig_e4_e_age.toString()
					norcHhBatch.hiqeligE5EAge = c.HIQElig_e5_e_age.toString()
					norcHhBatch.hiqeligE6EAge = c.HIQElig_e6_e_age.toString()
					norcHhBatch.hiqeligE1Preg = c.HIQElig_e1_preg.toString()
					norcHhBatch.hiqeligE2Preg = c.HIQElig_e2_preg.toString()
					norcHhBatch.hiqeligE3Preg = c.HIQElig_e3_preg.toString()
					norcHhBatch.hiqeligE4Preg = c.HIQElig_e4_preg.toString()
					norcHhBatch.hiqeligE5Preg = c.HIQElig_e5_preg.toString()
					norcHhBatch.hiqeligE6Preg = c.HIQElig_e6_preg.toString()
					norcHhBatch.hiqeligE1Ephone = c.HIQElig_e1_ephone.toString()
					norcHhBatch.hiqeligE2Ephone = c.HIQElig_e2_ephone.toString()
					norcHhBatch.hiqeligE3Ephone = c.HIQElig_e3_ephone.toString()
					norcHhBatch.hiqeligE4Ephone = c.HIQElig_e4_ephone.toString()
					norcHhBatch.hiqeligE5Ephone = c.HIQElig_e5_ephone.toString()
					norcHhBatch.hiqeligE6Ephone = c.HIQElig_e6_ephone.toString()
					norcHhBatch.hiqeligE1Ephonetyp = c.HIQElig_e1_ephonetyp.toString()
					norcHhBatch.hiqeligE2Ephonetyp = c.HIQElig_e2_ephonetyp.toString()
					norcHhBatch.hiqeligE3Ephonetyp = c.HIQElig_e3_ephonetyp.toString()
					norcHhBatch.hiqeligE4Ephonetyp = c.HIQElig_e4_ephonetyp.toString()
					norcHhBatch.hiqeligE5Ephonetyp = c.HIQElig_e5_ephonetyp.toString()
					norcHhBatch.hiqeligE6Ephonetyp = c.HIQElig_e6_ephonetyp.toString()
					norcHhBatch.hiqeligE1Email = c.HIQElig_e1_email.toString()
					norcHhBatch.hiqeligE2Email = c.HIQElig_e2_email.toString()
					norcHhBatch.hiqeligE3Email = c.HIQElig_e3_email.toString()
					norcHhBatch.hiqeligE4Email = c.HIQElig_e4_email.toString()
					norcHhBatch.hiqeligE5Email = c.HIQElig_e5_email.toString()
					norcHhBatch.hiqeligE6Email = c.HIQElig_e6_email.toString()
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcHhBatch.incoming = c.INCOMING.toString()
					norcHhBatch.incomingCodes = c.INCOMING_Codes.toString()
					norcHhBatch.quexlang = c.QUEXLANG.toString()
					norcHhBatch.calltype = c.CALLTYPE.toString()
					norcHhBatch.giftcard = c.giftcard.toString()
					norcHhBatch.respondent1 = c.RESPONDENT_1.toString()
					norcHhBatch.bestTtcBestTtc1 = c.BEST_TTC_BEST_TTC_1.toString()
					norcHhBatch.bestTtcBestTtc1Codes = c.BEST_TTC_BEST_TTC_1_Codes.toString()
					norcHhBatch.bestTtcBestTtc2 = c.BEST_TTC_BEST_TTC_2.toString()
					norcHhBatch.bestTtc3 = c.BEST_TTC_3.toString()
					norcHhBatch.phone = c.PHONE.toString()
					norcHhBatch.phoneNbr2 = c.PHONE_NBR_2.toString()
					norcHhBatch.phoneNbr2Codes = c.PHONE_NBR_2_Codes.toString()
					norcHhBatch.dkrefaddr01 = c.DKREFADDR_01.toString()
					norcHhBatch.dkrefaddr02 = c.DKREFADDR_02.toString()
					norcHhBatch.dkrefaddr03 = c.DKREFADDR_03.toString()
					norcHhBatch.address1 = c.ADDRESS_1.toString()
					norcHhBatch.address1Codes = c.ADDRESS_1_Codes.toString()
					norcHhBatch.address2 = c.ADDRESS_2.toString()
					norcHhBatch.address2Codes = c.ADDRESS_2_Codes.toString()
					norcHhBatch.unit = c.UNIT.toString()
					norcHhBatch.unitCodes = c.UNIT_Codes.toString()
					norcHhBatch.city = c.CITY.toString()
					norcHhBatch.cityCodes = c.CITY_Codes.toString()
					norcHhBatch.state = c.STATE.toString()
					norcHhBatch.zip = c.ZIP.toString()
					norcHhBatch.zipCodes = c.ZIP_Codes.toString()
					norcHhBatch.zip4 = c.ZIP4.toString()
					norcHhBatch.zip4Codes = c.ZIP4_Codes.toString()
					norcHhBatch.duEligConfirm = c.DU_ELIG_CONFIRM.toString()
					norcHhBatch.privateVar = c.PRIVATE.toString()
					norcHhBatch.rGender = c.R_GENDER.toString()
					norcHhBatch.numAdult = c.NUM_ADULT.toString()
					norcHhBatch.numAdultCodes = c.NUM_ADULT_Codes.toString()
					norcHhBatch.nummaletxt01 = c.nummaletxt_01.toString()
					norcHhBatch.nummaletxt02 = c.nummaletxt_02.toString()
					norcHhBatch.nummaletxt03 = c.nummaletxt_03.toString()
					norcHhBatch.nummaletxt04 = c.nummaletxt_04.toString()
					norcHhBatch.nummaletxt201 = c.nummaletxt2_01.toString()
					norcHhBatch.nummaletxt202 = c.nummaletxt2_02.toString()
					norcHhBatch.nummaletxt203 = c.nummaletxt2_03.toString()
					norcHhBatch.nummaletxt204 = c.nummaletxt2_04.toString()
					norcHhBatch.numMale = c.NUM_MALE.toString()
					norcHhBatch.numMaleCodes = c.NUM_MALE_Codes.toString()
					norcHhBatch.numfemtxt01 = c.numfemtxt_01.toString()
					norcHhBatch.numfemtxt02 = c.numfemtxt_02.toString()
					norcHhBatch.numfemtxt03 = c.numfemtxt_03.toString()
					norcHhBatch.numfemtxt04 = c.numfemtxt_04.toString()
					norcHhBatch.numfemtxt201 = c.numfemtxt2_01.toString()
					norcHhBatch.numfemtxt202 = c.numfemtxt2_02.toString()
					norcHhBatch.numfemtxt203 = c.numfemtxt2_03.toString()
					norcHhBatch.numfemtxt204 = c.numfemtxt2_04.toString()
					norcHhBatch.numFemale = c.NUM_FEMALE.toString()
					norcHhBatch.numFemaleCodes = c.NUM_FEMALE_Codes.toString()
					norcHhBatch.ageElig = c.AGE_ELIG.toString()
					norcHhBatch.ageEligCodes = c.AGE_ELIG_Codes.toString()
					norcHhBatch.numAgeElig = c.NUM_AGE_ELIG.toString()
					norcHhBatch.numAgeEligCodes = c.NUM_AGE_ELIG_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcHhBatch.pregnantHh = c.PREGNANT_HH.toString()
					norcHhBatch.numPreg = c.NUM_PREG.toString()
					norcHhBatch.numPregCodes = c.NUM_PREG_Codes.toString()
					norcHhBatch.pgintrotxt101 = c.pgintrotxt1_01.toString()
					norcHhBatch.pgintrotxt102 = c.pgintrotxt1_02.toString()
					norcHhBatch.pgintrotxt201 = c.pgintrotxt2_01.toString()
					norcHhBatch.pgintrotxt202 = c.pgintrotxt2_02.toString()
					norcHhBatch.pregloopP1PAgetxt = c.PregLoop_p1_p_agetxt.toString()
					norcHhBatch.pregloopP2PAgetxt = c.PregLoop_p2_p_agetxt.toString()
					norcHhBatch.pregloopP3PAgetxt = c.PregLoop_p3_p_agetxt.toString()
					norcHhBatch.pregloopP4PAgetxt = c.PregLoop_p4_p_agetxt.toString()
					norcHhBatch.pregloopP5PAgetxt = c.PregLoop_p5_p_agetxt.toString()
					norcHhBatch.pregloopP6PAgetxt = c.PregLoop_p6_p_agetxt.toString()
					norcHhBatch.pregloopP7PAgetxt = c.PregLoop_p7_p_agetxt.toString()
					norcHhBatch.pregloopP8PAgetxt = c.PregLoop_p8_p_agetxt.toString()
					norcHhBatch.pregloopP9PAgetxt = c.PregLoop_p9_p_agetxt.toString()
					norcHhBatch.pregloopP10PAgetxt = c.PregLoop_p10_p_agetxt.toString()
					norcHhBatch.pregloopP1PAge = c.PregLoop_p1_P_AGE.toString()
					norcHhBatch.pregloopP2PAge = c.PregLoop_p2_P_AGE.toString()
					norcHhBatch.pregloopP3PAge = c.PregLoop_p3_P_AGE.toString()
					norcHhBatch.pregloopP4PAge = c.PregLoop_p4_P_AGE.toString()
					norcHhBatch.pregloopP5PAge = c.PregLoop_p5_P_AGE.toString()
					norcHhBatch.pregloopP6PAge = c.PregLoop_p6_P_AGE.toString()
					norcHhBatch.pregloopP7PAge = c.PregLoop_p7_P_AGE.toString()
					norcHhBatch.pregloopP8PAge = c.PregLoop_p8_P_AGE.toString()
					norcHhBatch.pregloopP9PAge = c.PregLoop_p9_P_AGE.toString()
					norcHhBatch.pregloopP10PAge = c.PregLoop_p10_P_AGE.toString()
					norcHhBatch.pregloopP1PAgeCodes = c.PregLoop_p1_P_AGE_Codes.toString()
					norcHhBatch.pregloopP2PAgeCodes = c.PregLoop_p2_P_AGE_Codes.toString()
					norcHhBatch.pregloopP3PAgeCodes = c.PregLoop_p3_P_AGE_Codes.toString()
					norcHhBatch.pregloopP4PAgeCodes = c.PregLoop_p4_P_AGE_Codes.toString()
					norcHhBatch.pregloopP5PAgeCodes = c.PregLoop_p5_P_AGE_Codes.toString()
					norcHhBatch.pregloopP6PAgeCodes = c.PregLoop_p6_P_AGE_Codes.toString()
					norcHhBatch.pregloopP7PAgeCodes = c.PregLoop_p7_P_AGE_Codes.toString()
					norcHhBatch.pregloopP8PAgeCodes = c.PregLoop_p8_P_AGE_Codes.toString()
					norcHhBatch.pregloopP9PAgeCodes = c.PregLoop_p9_P_AGE_Codes.toString()
					norcHhBatch.pregloopP10PAgeCodes = c.PregLoop_p10_P_AGE_Codes.toString()
					norcHhBatch.pregloopP1PFnametxt = c.PregLoop_p1_p_fnametxt.toString()
					norcHhBatch.pregloopP2PFnametxt = c.PregLoop_p2_p_fnametxt.toString()
					norcHhBatch.pregloopP3PFnametxt = c.PregLoop_p3_p_fnametxt.toString()
					norcHhBatch.pregloopP4PFnametxt = c.PregLoop_p4_p_fnametxt.toString()
					norcHhBatch.pregloopP5PFnametxt = c.PregLoop_p5_p_fnametxt.toString()
					norcHhBatch.pregloopP6PFnametxt = c.PregLoop_p6_p_fnametxt.toString()
					norcHhBatch.pregloopP7PFnametxt = c.PregLoop_p7_p_fnametxt.toString()
					norcHhBatch.pregloopP8PFnametxt = c.PregLoop_p8_p_fnametxt.toString()
					norcHhBatch.pregloopP9PFnametxt = c.PregLoop_p9_p_fnametxt.toString()
					norcHhBatch.pregloopP10PFnametxt = c.PregLoop_p10_p_fnametxt.toString()
					norcHhBatch.pregloopP1PFname = c.PregLoop_p1_P_FNAME.toString()
					norcHhBatch.pregloopP2PFname = c.PregLoop_p2_P_FNAME.toString()
					norcHhBatch.pregloopP3PFname = c.PregLoop_p3_P_FNAME.toString()
					norcHhBatch.pregloopP4PFname = c.PregLoop_p4_P_FNAME.toString()
					norcHhBatch.pregloopP5PFname = c.PregLoop_p5_P_FNAME.toString()
					norcHhBatch.pregloopP6PFname = c.PregLoop_p6_P_FNAME.toString()
					norcHhBatch.pregloopP7PFname = c.PregLoop_p7_P_FNAME.toString()
					norcHhBatch.pregloopP8PFname = c.PregLoop_p8_P_FNAME.toString()
					norcHhBatch.pregloopP9PFname = c.PregLoop_p9_P_FNAME.toString()
					norcHhBatch.pregloopP10PFname = c.PregLoop_p10_P_FNAME.toString()
					norcHhBatch.pregloopP1PFnameCodes = c.PregLoop_p1_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP2PFnameCodes = c.PregLoop_p2_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP3PFnameCodes = c.PregLoop_p3_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP4PFnameCodes = c.PregLoop_p4_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP5PFnameCodes = c.PregLoop_p5_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP6PFnameCodes = c.PregLoop_p6_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP7PFnameCodes = c.PregLoop_p7_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP8PFnameCodes = c.PregLoop_p8_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP9PFnameCodes = c.PregLoop_p9_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP10PFnameCodes = c.PregLoop_p10_P_FNAME_Codes.toString()
					norcHhBatch.pregloopP1PRelate = c.PregLoop_p1_P_RELATE.toString()
					norcHhBatch.pregloopP2PRelate = c.PregLoop_p2_P_RELATE.toString()
					norcHhBatch.pregloopP3PRelate = c.PregLoop_p3_P_RELATE.toString()
					norcHhBatch.pregloopP4PRelate = c.PregLoop_p4_P_RELATE.toString()
					norcHhBatch.pregloopP5PRelate = c.PregLoop_p5_P_RELATE.toString()
					norcHhBatch.pregloopP6PRelate = c.PregLoop_p6_P_RELATE.toString()
					norcHhBatch.pregloopP7PRelate = c.PregLoop_p7_P_RELATE.toString()
					norcHhBatch.pregloopP8PRelate = c.PregLoop_p8_P_RELATE.toString()
					norcHhBatch.pregloopP9PRelate = c.PregLoop_p9_P_RELATE.toString()
					norcHhBatch.pregloopP10PRelate = c.PregLoop_p10_P_RELATE.toString()
					norcHhBatch.pregloopP1Hiqphontyp = c.PregLoop_p1_hiqphontyp.toString()
					norcHhBatch.pregloopP2Hiqphontyp = c.PregLoop_p2_hiqphontyp.toString()
					norcHhBatch.pregloopP3Hiqphontyp = c.PregLoop_p3_hiqphontyp.toString()
					norcHhBatch.pregloopP4Hiqphontyp = c.PregLoop_p4_hiqphontyp.toString()
					norcHhBatch.pregloopP5Hiqphontyp = c.PregLoop_p5_hiqphontyp.toString()
					norcHhBatch.pregloopP6Hiqphontyp = c.PregLoop_p6_hiqphontyp.toString()
					norcHhBatch.pregloopP7Hiqphontyp = c.PregLoop_p7_hiqphontyp.toString()
					norcHhBatch.pregloopP8Hiqphontyp = c.PregLoop_p8_hiqphontyp.toString()
					norcHhBatch.pregloopP9Hiqphontyp = c.PregLoop_p9_hiqphontyp.toString()
					norcHhBatch.pregloopP10Hiqphontyp = c.PregLoop_p10_hiqphontyp.toString()
					norcHhBatch.pregloopP1PPhoneNbr = c.PregLoop_p1_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP2PPhoneNbr = c.PregLoop_p2_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP3PPhoneNbr = c.PregLoop_p3_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP4PPhoneNbr = c.PregLoop_p4_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP5PPhoneNbr = c.PregLoop_p5_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP6PPhoneNbr = c.PregLoop_p6_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP7PPhoneNbr = c.PregLoop_p7_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP8PPhoneNbr = c.PregLoop_p8_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP9PPhoneNbr = c.PregLoop_p9_P_PHONE_NBR.toString()
					norcHhBatch.pregloopP10PPhoneNbr = c.PregLoop_p10_P_PHONE_NBR.toString()
					norcHhBatch.prg1phonerCds8 = c.Prg_1PHONER_Cds8.toString()
					norcHhBatch.prg2phonerCds9 = c.Prg_2PHONER_Cds9.toString()
					norcHhBatch.prg3phonerCds10 = c.Prg_3PHONER_Cds10.toString()
					norcHhBatch.prg4phonerCds11 = c.Prg_4PHONER_Cds11.toString()
					norcHhBatch.prg5phonerCds12 = c.Prg_5PHONER_Cds12.toString()
					norcHhBatch.prg6phonerCds13 = c.Prg_6PHONER_Cds13.toString()
					norcHhBatch.prg7phonerCds14 = c.Prg_7PHONER_Cds14.toString()
					norcHhBatch.prg8phonerCds15 = c.Prg_8PHONER_Cds15.toString()
					norcHhBatch.prg9phonerCds16 = c.Prg_9PHONER_Cds16.toString()
					norcHhBatch.prg1phonerCds17 = c.Prg_1PHONER_Cds17.toString()
					norcHhBatch.pregloopP1PPhoneType = c.PregLoop_p1_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP2PPhoneType = c.PregLoop_p2_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP3PPhoneType = c.PregLoop_p3_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP4PPhoneType = c.PregLoop_p4_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP5PPhoneType = c.PregLoop_p5_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP6PPhoneType = c.PregLoop_p6_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP7PPhoneType = c.PregLoop_p7_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP8PPhoneType = c.PregLoop_p8_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP9PPhoneType = c.PregLoop_p9_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP10PPhoneType = c.PregLoop_p10_P_PHONE_TYPE.toString()
					norcHhBatch.pregloopP1PEmail = c.PregLoop_p1_P_EMAIL.toString()
					norcHhBatch.pregloopP2PEmail = c.PregLoop_p2_P_EMAIL.toString()
					norcHhBatch.pregloopP3PEmail = c.PregLoop_p3_P_EMAIL.toString()
					norcHhBatch.pregloopP4PEmail = c.PregLoop_p4_P_EMAIL.toString()
					norcHhBatch.pregloopP5PEmail = c.PregLoop_p5_P_EMAIL.toString()
					norcHhBatch.pregloopP6PEmail = c.PregLoop_p6_P_EMAIL.toString()
					norcHhBatch.pregloopP7PEmail = c.PregLoop_p7_P_EMAIL.toString()
					norcHhBatch.pregloopP8PEmail = c.PregLoop_p8_P_EMAIL.toString()
					norcHhBatch.pregloopP9PEmail = c.PregLoop_p9_P_EMAIL.toString()
					norcHhBatch.pregloopP10PEmail = c.PregLoop_p10_P_EMAIL.toString()
					norcHhBatch.pregloopP1PEmailCodes = c.PregLoop_p1_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP2PEmailCodes = c.PregLoop_p2_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP3PEmailCodes = c.PregLoop_p3_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP4PEmailCodes = c.PregLoop_p4_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP5PEmailCodes = c.PregLoop_p5_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP6PEmailCodes = c.PregLoop_p6_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP7PEmailCodes = c.PregLoop_p7_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP8PEmailCodes = c.PregLoop_p8_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP9PEmailCodes = c.PregLoop_p9_P_EMAIL_Codes.toString()
					norcHhBatch.pregloopP10PEmailCodes = c.PregLoop_p10_P_EMAIL_Codes.toString()
					norcHhBatch.numPregAdult = c.NUM_PREG_ADULT.toString()
					norcHhBatch.numPregMinor = c.NUM_PREG_MINOR.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcHhBatch.selfpreg = c.SELFPREG.toString()
					norcHhBatch.selfelig = c.SELFELIG.toString()
					norcHhBatch.eligloopE1EFnametxt = c.EligLoop_e1_E_fnametxt.toString()
					norcHhBatch.eligloopE2EFnametxt = c.EligLoop_e2_E_fnametxt.toString()
					norcHhBatch.eligloopE3EFnametxt = c.EligLoop_e3_E_fnametxt.toString()
					norcHhBatch.eligloopE4EFnametxt = c.EligLoop_e4_E_fnametxt.toString()
					norcHhBatch.eligloopE5EFnametxt = c.EligLoop_e5_E_fnametxt.toString()
					norcHhBatch.eligloopE6EFnametxt = c.EligLoop_e6_E_fnametxt.toString()
					norcHhBatch.eligloopE7EFnametxt = c.EligLoop_e7_E_fnametxt.toString()
					norcHhBatch.eligloopE8EFnametxt = c.EligLoop_e8_E_fnametxt.toString()
					norcHhBatch.eligloopE9EFnametxt = c.EligLoop_e9_E_fnametxt.toString()
					norcHhBatch.eligloopE10EFnametxt = c.EligLoop_e10_E_fnametxt.toString()
					norcHhBatch.eligloopE1EAgetxt = c.EligLoop_e1_e_agetxt.toString()
					norcHhBatch.eligloopE2EAgetxt = c.EligLoop_e2_e_agetxt.toString()
					norcHhBatch.eligloopE3EAgetxt = c.EligLoop_e3_e_agetxt.toString()
					norcHhBatch.eligloopE4EAgetxt = c.EligLoop_e4_e_agetxt.toString()
					norcHhBatch.eligloopE5EAgetxt = c.EligLoop_e5_e_agetxt.toString()
					norcHhBatch.eligloopE6EAgetxt = c.EligLoop_e6_e_agetxt.toString()
					norcHhBatch.eligloopE7EAgetxt = c.EligLoop_e7_e_agetxt.toString()
					norcHhBatch.eligloopE8EAgetxt = c.EligLoop_e8_e_agetxt.toString()
					norcHhBatch.eligloopE9EAgetxt = c.EligLoop_e9_e_agetxt.toString()
					norcHhBatch.eligloopE10EAgetxt = c.EligLoop_e10_e_agetxt.toString()
					norcHhBatch.eligloopE1Fnamfill = c.EligLoop_e1_fnamfill.toString()
					norcHhBatch.eligloopE2Fnamfill = c.EligLoop_e2_fnamfill.toString()
					norcHhBatch.eligloopE3Fnamfill = c.EligLoop_e3_fnamfill.toString()
					norcHhBatch.eligloopE4Fnamfill = c.EligLoop_e4_fnamfill.toString()
					norcHhBatch.eligloopE5Fnamfill = c.EligLoop_e5_fnamfill.toString()
					norcHhBatch.eligloopE6Fnamfill = c.EligLoop_e6_fnamfill.toString()
					norcHhBatch.eligloopE7Fnamfill = c.EligLoop_e7_fnamfill.toString()
					norcHhBatch.eligloopE8Fnamfill = c.EligLoop_e8_fnamfill.toString()
					norcHhBatch.eligloopE9Fnamfill = c.EligLoop_e9_fnamfill.toString()
					norcHhBatch.eligloopE10Fnamfill = c.EligLoop_e10_fnamfill.toString()
					norcHhBatch.eligloopE1AgeEligFname = c.EligLoop_e1_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE2AgeEligFname = c.EligLoop_e2_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE3AgeEligFname = c.EligLoop_e3_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE4AgeEligFname = c.EligLoop_e4_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE5AgeEligFname = c.EligLoop_e5_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE6AgeEligFname = c.EligLoop_e6_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE7AgeEligFname = c.EligLoop_e7_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE8AgeEligFname = c.EligLoop_e8_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE9AgeEligFname = c.EligLoop_e9_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE10AgeEligFname = c.EligLoop_e10_AGE_ELIG_FNAME.toString()
					norcHhBatch.eligloopE1AgeEligFnameCodes = c.EligLoop_e1_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE2AgeEligFnameCodes = c.EligLoop_e2_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE3AgeEligFnameCodes = c.EligLoop_e3_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE4AgeEligFnameCodes = c.EligLoop_e4_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE5AgeEligFnameCodes = c.EligLoop_e5_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE6AgeEligFnameCodes = c.EligLoop_e6_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE7AgeEligFnameCodes = c.EligLoop_e7_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE8AgeEligFnameCodes = c.EligLoop_e8_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligloopE9AgeEligFnameCodes = c.EligLoop_e9_AGE_ELIG_FNAME_Codes.toString()
					norcHhBatch.eligEEigFnamameCds18 = c.Elig_E_EIG_FNAMAME_Cds18.toString()
					norcHhBatch.eligloopE1AgeEligAge = c.EligLoop_e1_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE2AgeEligAge = c.EligLoop_e2_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE3AgeEligAge = c.EligLoop_e3_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE4AgeEligAge = c.EligLoop_e4_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE5AgeEligAge = c.EligLoop_e5_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE6AgeEligAge = c.EligLoop_e6_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE7AgeEligAge = c.EligLoop_e7_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE8AgeEligAge = c.EligLoop_e8_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE9AgeEligAge = c.EligLoop_e9_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE10AgeEligAge = c.EligLoop_e10_AGE_ELIG_AGE.toString()
					norcHhBatch.eligloopE1AgeEligAgeCodes = c.EligLoop_e1_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE2AgeEligAgeCodes = c.EligLoop_e2_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE3AgeEligAgeCodes = c.EligLoop_e3_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE4AgeEligAgeCodes = c.EligLoop_e4_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE5AgeEligAgeCodes = c.EligLoop_e5_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE6AgeEligAgeCodes = c.EligLoop_e6_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE7AgeEligAgeCodes = c.EligLoop_e7_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE8AgeEligAgeCodes = c.EligLoop_e8_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE9AgeEligAgeCodes = c.EligLoop_e9_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE10AgeEligAgeCodes = c.EligLoop_e10_AGE_ELIG_AGE_Codes.toString()
					norcHhBatch.eligloopE1AgeEligRelate = c.EligLoop_e1_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE2AgeEligRelate = c.EligLoop_e2_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE3AgeEligRelate = c.EligLoop_e3_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE4AgeEligRelate = c.EligLoop_e4_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE5AgeEligRelate = c.EligLoop_e5_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE6AgeEligRelate = c.EligLoop_e6_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE7AgeEligRelate = c.EligLoop_e7_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE8AgeEligRelate = c.EligLoop_e8_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligloopE9AgeEligRelate = c.EligLoop_e9_AGE_ELIG_RELATE.toString()
					norcHhBatch.eligAgeEReate19 = c.Elig_AGE_E_REATE19.toString()
					norcHhBatch.eligloopE1Hiqphontyp = c.EligLoop_e1_hiqphontyp.toString()
					norcHhBatch.eligloopE2Hiqphontyp = c.EligLoop_e2_hiqphontyp.toString()
					norcHhBatch.eligloopE3Hiqphontyp = c.EligLoop_e3_hiqphontyp.toString()
					norcHhBatch.eligloopE4Hiqphontyp = c.EligLoop_e4_hiqphontyp.toString()
					norcHhBatch.eligloopE5Hiqphontyp = c.EligLoop_e5_hiqphontyp.toString()
					norcHhBatch.eligloopE6Hiqphontyp = c.EligLoop_e6_hiqphontyp.toString()
					norcHhBatch.eligloopE7Hiqphontyp = c.EligLoop_e7_hiqphontyp.toString()
					norcHhBatch.eligloopE8Hiqphontyp = c.EligLoop_e8_hiqphontyp.toString()
					norcHhBatch.eligloopE9Hiqphontyp = c.EligLoop_e9_hiqphontyp.toString()
					norcHhBatch.eligloopE10Hiqphontyp = c.EligLoop_e10_hiqphontyp.toString()
					norcHhBatch.eligloopE1AgeEligPhone = c.EligLoop_e1_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE2AgeEligPhone = c.EligLoop_e2_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE3AgeEligPhone = c.EligLoop_e3_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE4AgeEligPhone = c.EligLoop_e4_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE5AgeEligPhone = c.EligLoop_e5_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE6AgeEligPhone = c.EligLoop_e6_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE7AgeEligPhone = c.EligLoop_e7_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE8AgeEligPhone = c.EligLoop_e8_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE9AgeEligPhone = c.EligLoop_e9_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligloopE10AgeEligPhone = c.EligLoop_e10_AGE_ELIG_PHONE.toString()
					norcHhBatch.eligEigECds20 = c.Elig__EIG_E_Cds20.toString()
					norcHhBatch.eligEigECds21 = c.Elig__EIG_E_Cds21.toString()
					norcHhBatch.eligEigECds22 = c.Elig__EIG_E_Cds22.toString()
					norcHhBatch.eligEigECds23 = c.Elig__EIG_E_Cds23.toString()
					norcHhBatch.eligEigECds24 = c.Elig__EIG_E_Cds24.toString()
					norcHhBatch.eligEigECds25 = c.Elig__EIG_E_Cds25.toString()
					norcHhBatch.eligEigECds26 = c.Elig__EIG_E_Cds26.toString()
					norcHhBatch.eligEigECds27 = c.Elig__EIG_E_Cds27.toString()
					norcHhBatch.eligEigECds28 = c.Elig__EIG_E_Cds28.toString()
					norcHhBatch.eligEEigPhononeCds29 = c.Elig_E_EIG_PHONONE_Cds29.toString()
					norcHhBatch.eligloopE1AgeEligPtype = c.EligLoop_e1_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE2AgeEligPtype = c.EligLoop_e2_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE3AgeEligPtype = c.EligLoop_e3_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE4AgeEligPtype = c.EligLoop_e4_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE5AgeEligPtype = c.EligLoop_e5_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE6AgeEligPtype = c.EligLoop_e6_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE7AgeEligPtype = c.EligLoop_e7_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE8AgeEligPtype = c.EligLoop_e8_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE9AgeEligPtype = c.EligLoop_e9_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE10AgeEligPtype = c.EligLoop_e10_AGE_ELIG_PTYPE.toString()
					norcHhBatch.eligloopE1AgeEligEmail = c.EligLoop_e1_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE2AgeEligEmail = c.EligLoop_e2_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE3AgeEligEmail = c.EligLoop_e3_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE4AgeEligEmail = c.EligLoop_e4_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE5AgeEligEmail = c.EligLoop_e5_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE6AgeEligEmail = c.EligLoop_e6_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE7AgeEligEmail = c.EligLoop_e7_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE8AgeEligEmail = c.EligLoop_e8_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE9AgeEligEmail = c.EligLoop_e9_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligloopE10AgeEligEmail = c.EligLoop_e10_AGE_ELIG_EMAIL.toString()
					norcHhBatch.eligEigCds30 = c.Elig__EIG__Cds30.toString()
					norcHhBatch.eligEigCds31 = c.Elig__EIG__Cds31.toString()
					norcHhBatch.eligEigCds32 = c.Elig__EIG__Cds32.toString()
					norcHhBatch.eligEigCds33 = c.Elig__EIG__Cds33.toString()
					norcHhBatch.eligEigCds34 = c.Elig__EIG__Cds34.toString()
					norcHhBatch.eligEigCds35 = c.Elig__EIG__Cds35.toString()
					norcHhBatch.eligEigCds36 = c.Elig__EIG__Cds36.toString()
					norcHhBatch.eligEigCds37 = c.Elig__EIG__Cds37.toString()
					norcHhBatch.eligEigCds38 = c.Elig__EIG__Cds38.toString()
					norcHhBatch.eligEEigEmaiaiCds39 = c.Elig_E_EIG_EMAIAI_Cds39.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcHhBatch.allElig = c.ALL_ELIG.toString()
					norcHhBatch.finloopP1Fname = c.FinLoop_p1_FNAME.toString()
					norcHhBatch.finloopP2Fname = c.FinLoop_p2_FNAME.toString()
					norcHhBatch.finloopP3Fname = c.FinLoop_p3_FNAME.toString()
					norcHhBatch.finloopP4Fname = c.FinLoop_p4_FNAME.toString()
					norcHhBatch.finloopP5Fname = c.FinLoop_p5_FNAME.toString()
					norcHhBatch.finloopP6Fname = c.FinLoop_p6_FNAME.toString()
					norcHhBatch.finloopP7Fname = c.FinLoop_p7_FNAME.toString()
					norcHhBatch.finloopP8Fname = c.FinLoop_p8_FNAME.toString()
					norcHhBatch.finloopP9Fname = c.FinLoop_p9_FNAME.toString()
					norcHhBatch.finloopP10Fname = c.FinLoop_p10_FNAME.toString()
					norcHhBatch.finloopE1Fname = c.FinLoop_e1_FNAME.toString()
					norcHhBatch.finloopE2Fname = c.FinLoop_e2_FNAME.toString()
					norcHhBatch.finloopE3Fname = c.FinLoop_e3_FNAME.toString()
					norcHhBatch.finloopE4Fname = c.FinLoop_e4_FNAME.toString()
					norcHhBatch.finloopE5Fname = c.FinLoop_e5_FNAME.toString()
					norcHhBatch.finloopE6Fname = c.FinLoop_e6_FNAME.toString()
					norcHhBatch.finloopE7Fname = c.FinLoop_e7_FNAME.toString()
					norcHhBatch.finloopE8Fname = c.FinLoop_e8_FNAME.toString()
					norcHhBatch.finloopE9Fname = c.FinLoop_e9_FNAME.toString()
					norcHhBatch.finloopE10Fname = c.FinLoop_e10_FNAME.toString()
					norcHhBatch.finloopP1FnameCodes = c.FinLoop_p1_FNAME_Codes.toString()
					norcHhBatch.finloopP2FnameCodes = c.FinLoop_p2_FNAME_Codes.toString()
					norcHhBatch.finloopP3FnameCodes = c.FinLoop_p3_FNAME_Codes.toString()
					norcHhBatch.finloopP4FnameCodes = c.FinLoop_p4_FNAME_Codes.toString()
					norcHhBatch.finloopP5FnameCodes = c.FinLoop_p5_FNAME_Codes.toString()
					norcHhBatch.finloopP6FnameCodes = c.FinLoop_p6_FNAME_Codes.toString()
					norcHhBatch.finloopP7FnameCodes = c.FinLoop_p7_FNAME_Codes.toString()
					norcHhBatch.finloopP8FnameCodes = c.FinLoop_p8_FNAME_Codes.toString()
					norcHhBatch.finloopP9FnameCodes = c.FinLoop_p9_FNAME_Codes.toString()
					norcHhBatch.finloopP10FnameCodes = c.FinLoop_p10_FNAME_Codes.toString()
					norcHhBatch.finloopE1FnameCodes = c.FinLoop_e1_FNAME_Codes.toString()
					norcHhBatch.finloopE2FnameCodes = c.FinLoop_e2_FNAME_Codes.toString()
					norcHhBatch.finloopE3FnameCodes = c.FinLoop_e3_FNAME_Codes.toString()
					norcHhBatch.finloopE4FnameCodes = c.FinLoop_e4_FNAME_Codes.toString()
					norcHhBatch.finloopE5FnameCodes = c.FinLoop_e5_FNAME_Codes.toString()
					norcHhBatch.finloopE6FnameCodes = c.FinLoop_e6_FNAME_Codes.toString()
					norcHhBatch.finloopE7FnameCodes = c.FinLoop_e7_FNAME_Codes.toString()
					norcHhBatch.finloopE8FnameCodes = c.FinLoop_e8_FNAME_Codes.toString()
					norcHhBatch.finloopE9FnameCodes = c.FinLoop_e9_FNAME_Codes.toString()
					norcHhBatch.finloopE10FnameCodes = c.FinLoop_e10_FNAME_Codes.toString()
					norcHhBatch.finloopP1Fage = c.FinLoop_p1_FAGE.toString()
					norcHhBatch.finloopP2Fage = c.FinLoop_p2_FAGE.toString()
					norcHhBatch.finloopP3Fage = c.FinLoop_p3_FAGE.toString()
					norcHhBatch.finloopP4Fage = c.FinLoop_p4_FAGE.toString()
					norcHhBatch.finloopP5Fage = c.FinLoop_p5_FAGE.toString()
					norcHhBatch.finloopP6Fage = c.FinLoop_p6_FAGE.toString()
					norcHhBatch.finloopP7Fage = c.FinLoop_p7_FAGE.toString()
					norcHhBatch.finloopP8Fage = c.FinLoop_p8_FAGE.toString()
					norcHhBatch.finloopP9Fage = c.FinLoop_p9_FAGE.toString()
					norcHhBatch.finloopP10Fage = c.FinLoop_p10_FAGE.toString()
					norcHhBatch.finloopE1Fage = c.FinLoop_e1_FAGE.toString()
					norcHhBatch.finloopE2Fage = c.FinLoop_e2_FAGE.toString()
					norcHhBatch.finloopE3Fage = c.FinLoop_e3_FAGE.toString()
					norcHhBatch.finloopE4Fage = c.FinLoop_e4_FAGE.toString()
					norcHhBatch.finloopE5Fage = c.FinLoop_e5_FAGE.toString()
					norcHhBatch.finloopE6Fage = c.FinLoop_e6_FAGE.toString()
					norcHhBatch.finloopE7Fage = c.FinLoop_e7_FAGE.toString()
					norcHhBatch.finloopE8Fage = c.FinLoop_e8_FAGE.toString()
					norcHhBatch.finloopE9Fage = c.FinLoop_e9_FAGE.toString()
					norcHhBatch.finloopE10Fage = c.FinLoop_e10_FAGE.toString()
					norcHhBatch.finloopP1FageCodes = c.FinLoop_p1_FAGE_Codes.toString()
					norcHhBatch.finloopP2FageCodes = c.FinLoop_p2_FAGE_Codes.toString()
					norcHhBatch.finloopP3FageCodes = c.FinLoop_p3_FAGE_Codes.toString()
					norcHhBatch.finloopP4FageCodes = c.FinLoop_p4_FAGE_Codes.toString()
					norcHhBatch.finloopP5FageCodes = c.FinLoop_p5_FAGE_Codes.toString()
					norcHhBatch.finloopP6FageCodes = c.FinLoop_p6_FAGE_Codes.toString()
					norcHhBatch.finloopP7FageCodes = c.FinLoop_p7_FAGE_Codes.toString()
					norcHhBatch.finloopP8FageCodes = c.FinLoop_p8_FAGE_Codes.toString()
					norcHhBatch.finloopP9FageCodes = c.FinLoop_p9_FAGE_Codes.toString()
					norcHhBatch.finloopP10FageCodes = c.FinLoop_p10_FAGE_Codes.toString()
					norcHhBatch.finloopE1FageCodes = c.FinLoop_e1_FAGE_Codes.toString()
					norcHhBatch.finloopE2FageCodes = c.FinLoop_e2_FAGE_Codes.toString()
					norcHhBatch.finloopE3FageCodes = c.FinLoop_e3_FAGE_Codes.toString()
					norcHhBatch.finloopE4FageCodes = c.FinLoop_e4_FAGE_Codes.toString()
					norcHhBatch.finloopE5FageCodes = c.FinLoop_e5_FAGE_Codes.toString()
					norcHhBatch.finloopE6FageCodes = c.FinLoop_e6_FAGE_Codes.toString()
					norcHhBatch.finloopE7FageCodes = c.FinLoop_e7_FAGE_Codes.toString()
					norcHhBatch.finloopE8FageCodes = c.FinLoop_e8_FAGE_Codes.toString()
					norcHhBatch.finloopE9FageCodes = c.FinLoop_e9_FAGE_Codes.toString()
					norcHhBatch.finloopE10FageCodes = c.FinLoop_e10_FAGE_Codes.toString()
					norcHhBatch.finloopP1Frelate = c.FinLoop_p1_FRELATE.toString()
					norcHhBatch.finloopP2Frelate = c.FinLoop_p2_FRELATE.toString()
					norcHhBatch.finloopP3Frelate = c.FinLoop_p3_FRELATE.toString()
					norcHhBatch.finloopP4Frelate = c.FinLoop_p4_FRELATE.toString()
					norcHhBatch.finloopP5Frelate = c.FinLoop_p5_FRELATE.toString()
					norcHhBatch.finloopP6Frelate = c.FinLoop_p6_FRELATE.toString()
					norcHhBatch.finloopP7Frelate = c.FinLoop_p7_FRELATE.toString()
					norcHhBatch.finloopP8Frelate = c.FinLoop_p8_FRELATE.toString()
					norcHhBatch.finloopP9Frelate = c.FinLoop_p9_FRELATE.toString()
					norcHhBatch.finloopP10Frelate = c.FinLoop_p10_FRELATE.toString()
					norcHhBatch.finloopE1Frelate = c.FinLoop_e1_FRELATE.toString()
					norcHhBatch.finloopE2Frelate = c.FinLoop_e2_FRELATE.toString()
					norcHhBatch.finloopE3Frelate = c.FinLoop_e3_FRELATE.toString()
					norcHhBatch.finloopE4Frelate = c.FinLoop_e4_FRELATE.toString()
					norcHhBatch.finloopE5Frelate = c.FinLoop_e5_FRELATE.toString()
					norcHhBatch.finloopE6Frelate = c.FinLoop_e6_FRELATE.toString()
					norcHhBatch.finloopE7Frelate = c.FinLoop_e7_FRELATE.toString()
					norcHhBatch.finloopE8Frelate = c.FinLoop_e8_FRELATE.toString()
					norcHhBatch.finloopE9Frelate = c.FinLoop_e9_FRELATE.toString()
					norcHhBatch.finloopE10Frelate = c.FinLoop_e10_FRELATE.toString()
					norcHhBatch.finloopP1Fpreg = c.FinLoop_p1_FPREG.toString()
					norcHhBatch.finloopP2Fpreg = c.FinLoop_p2_FPREG.toString()
					norcHhBatch.finloopP3Fpreg = c.FinLoop_p3_FPREG.toString()
					norcHhBatch.finloopP4Fpreg = c.FinLoop_p4_FPREG.toString()
					norcHhBatch.finloopP5Fpreg = c.FinLoop_p5_FPREG.toString()
					norcHhBatch.finloopP6Fpreg = c.FinLoop_p6_FPREG.toString()
					norcHhBatch.finloopP7Fpreg = c.FinLoop_p7_FPREG.toString()
					norcHhBatch.finloopP8Fpreg = c.FinLoop_p8_FPREG.toString()
					norcHhBatch.finloopP9Fpreg = c.FinLoop_p9_FPREG.toString()
					norcHhBatch.finloopP10Fpreg = c.FinLoop_p10_FPREG.toString()
					norcHhBatch.finloopE1Fpreg = c.FinLoop_e1_FPREG.toString()
					norcHhBatch.finloopE2Fpreg = c.FinLoop_e2_FPREG.toString()
					norcHhBatch.finloopE3Fpreg = c.FinLoop_e3_FPREG.toString()
					norcHhBatch.finloopE4Fpreg = c.FinLoop_e4_FPREG.toString()
					norcHhBatch.finloopE5Fpreg = c.FinLoop_e5_FPREG.toString()
					norcHhBatch.finloopE6Fpreg = c.FinLoop_e6_FPREG.toString()
					norcHhBatch.finloopE7Fpreg = c.FinLoop_e7_FPREG.toString()
					norcHhBatch.finloopE8Fpreg = c.FinLoop_e8_FPREG.toString()
					norcHhBatch.finloopE9Fpreg = c.FinLoop_e9_FPREG.toString()
					norcHhBatch.finloopE10Fpreg = c.FinLoop_e10_FPREG.toString()
					norcHhBatch.conftext = c.conftext.toString()
					norcHhBatch.confirm1 = c.CONFIRM_1.toString()
					norcHhBatch.confirm2 = c.CONFIRM_2.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcHhBatch.rFname = c.R_FNAME.toString()
					norcHhBatch.rFnameCodes = c.R_FNAME_Codes.toString()
					norcHhBatch.rLname = c.R_LNAME.toString()
					norcHhBatch.rLnameCodes = c.R_LNAME_Codes.toString()
					norcHhBatch.phoneNbr = c.PHONE_NBR.toString()
					norcHhBatch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					norcHhBatch.hiqrphontyp = c.hiqrphontyp.toString()
					norcHhBatch.phoneType = c.PHONE_TYPE.toString()
					norcHhBatch.phoneTypeOth = c.PHONE_TYPE_OTH.toString()
					norcHhBatch.phoneTypeOthCodes = c.PHONE_TYPE_OTH_Codes.toString()
					norcHhBatch.phoneAlt = c.PHONE_ALT.toString()
					norcHhBatch.phoneAltCodes = c.PHONE_ALT_Codes.toString()
					norcHhBatch.phoneAltType = c.PHONE_ALT_TYPE.toString()
					norcHhBatch.phoneAltTypeOth = c.PHONE_ALT_TYPE_OTH.toString()
					norcHhBatch.phoneAltTypeOthCodes = c.PHONE_ALT_TYPE_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcHhBatch.incentiveRequest = c.INCENTIVE_REQUEST.toString()
					norcHhBatch.incentiveChoice = c.INCENTIVE_CHOICE.toString()
					norcHhBatch.hhElig = c.HH_ELIG.toString()
					norcHhBatch.option1 = c.OPTION_1.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcHhBatch.contactType = c.CONTACT_TYPE.toString()
					norcHhBatch.contactTypeOther = c.CONTACT_TYPE_OTHER.toString()
					norcHhBatch.english = c.ENGLISH.toString()
					norcHhBatch.contactLang = c.CONTACT_LANG.toString()
					norcHhBatch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcHhBatch.interpret = c.INTERPRET.toString()
					norcHhBatch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcHhBatch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					norcHhBatch.timeStamp9 = c.TIME_STAMP_9.toString()
					norcHhBatch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcHhBatch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					norcHhBatch.barcode = c.barcode.toString()
					norcHhBatch.tracking = c.TRACKING__.toString()
					try {
						dateTimeString = c.TRANSML_DATE.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.transmlDate = null
						} else {
							DateTime dt = fmtDateOnly.parseDateTime(dateTimeString)
							norcHhBatch.transmlDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TRANSML_DATE: ${c.TRANSML_DATE.toString()}'
						// println '! parse TRANSML_DATE Input: ${c.TRANSML_DATE.toString()}'
						// println '! parse TRANSML_DATE Exception: ${e.toString()}'
					}
					norcHhBatch.comments = c.COMMENTS.toString()
					try {
						dateTimeString = c.DATE.toString()
						if ( ! dateTimeString ) {
							norcHhBatch.theDate = null
						} else {
							DateTime dt = fmtDateOnly.parseDateTime(dateTimeString)
							norcHhBatch.theDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DATE: ${c.DATE.toString()}'
						// println '! parse DATE Input: ${c.DATE.toString()}'
						// println '! parse DATE Exception: ${e.toString()}'
					}
					norcHhBatch.rcuser = c.RCUSER.toString()
					norcHhBatch.flagMail = c.flag_mail.toString()
					
					// Save the record
					if (norcHhBatch.hasErrors()) {
						response << "! norcHhBatch has errors.\n"
					} else if (norcHhBatch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcHhBatch record with Respondent Serial ${norcHhBatch.respondentSerial}"
						norcHhBatch.errors.each{ e ->
							// println "norcHhBatch:error::${e}"
							e.fieldErrors.each{ fe -> 
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n" 
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'" 
							}
						}
					} 				
				}
			}
		}
		
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcHhBatch in ${diff} seconds"			
		}
		// end HH_BATCH1
	}
		
	def parseLowConsentBatch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
		
		def saveCount = 0
		if (debug) {
			println "Parsing LOWCONSENT_BATCH1"
		}
		def startTime = System.nanoTime()
		//table?.LOWCONSENT_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.LOWCONSENT_BATCH1?.eachParallel { c ->
				NorcLowConsentBatch.withTransaction {
					def checkSerial = c.Respondent_Serial.toString()
					def dateTimeString = ""
					def norcLowConsentBatch = NorcLowConsentBatch.findByRespondentSerial(checkSerial)
					
					if (!norcLowConsentBatch) {
						norcLowConsentBatch = new NorcLowConsentBatch()
						response <<  " + Creating new NorcLowConsentBatch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcLowConsentBatch.lock()
						response <<  " ~ Updating existing NorcLowConsentBatch(${checkSerial})\n"
					}
					norcLowConsentBatch.respondentSerial = c.Respondent_Serial.toString()
					norcLowConsentBatch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcLowConsentBatch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcLowConsentBatch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcLowConsentBatch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcLowConsentBatch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcLowConsentBatch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcLowConsentBatch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcLowConsentBatch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcLowConsentBatch.respondentId = c.Respondent_ID.toString()
					norcLowConsentBatch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcLowConsentBatch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcLowConsentBatch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcLowConsentBatch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcLowConsentBatch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcLowConsentBatch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcLowConsentBatch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcLowConsentBatch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcLowConsentBatch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcLowConsentBatch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcLowConsentBatch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcLowConsentBatch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcLowConsentBatch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcLowConsentBatch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcLowConsentBatch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcLowConsentBatch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcLowConsentBatch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcLowConsentBatch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcLowConsentBatch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcLowConsentBatch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcLowConsentBatch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcLowConsentBatch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcLowConsentBatch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcLowConsentBatch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcLowConsentBatch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcLowConsentBatch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcLowConsentBatch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcLowConsentBatch.datacleaningNote = c.DataCleaning_Note.toString()
					norcLowConsentBatch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcLowConsentBatch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcLowConsentBatch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcLowConsentBatch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcLowConsentBatch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcLowConsentBatch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcLowConsentBatch.suId = c.SU_ID.toString()
					norcLowConsentBatch.majority = c.MAJORITY.toString()
					norcLowConsentBatch.affiliate = c.AFFILIATE.toString()
					norcLowConsentBatch.prFname = c.PR_FNAME.toString()
					norcLowConsentBatch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcLowConsentBatch.prLname = c.PR_LNAME.toString()
					norcLowConsentBatch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcLowConsentBatch.dobMo = c.DOB_MO.toString()
					norcLowConsentBatch.dobMoCodes = c.DOB_MO_Codes.toString()
					norcLowConsentBatch.dobDy = c.DOB_DY.toString()
					norcLowConsentBatch.dobDyCodes = c.DOB_DY_Codes.toString()
					norcLowConsentBatch.dobYr = c.DOB_YR.toString()
					norcLowConsentBatch.dobYrCodes = c.DOB_YR_Codes.toString()
					norcLowConsentBatch.psuId = c.PSU_ID.toString()
					norcLowConsentBatch.agecalc = c.AGECALC.toString()
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcLowConsentBatch.currmo = c.CURRMO.toString()
					norcLowConsentBatch.currdy = c.CURRDY.toString()
					norcLowConsentBatch.curryr = c.CURRYR.toString()
					norcLowConsentBatch.quexver = c.quexver.toString()
					norcLowConsentBatch.quexlang = c.QUEXLANG.toString()
					norcLowConsentBatch.sameday = c.SAMEDAY.toString()
					norcLowConsentBatch.calltype = c.CALLTYPE.toString()
					norcLowConsentBatch.female1 = c.FEMALE_1.toString()
					norcLowConsentBatch.bestTtcBestTtc1 = c.BEST_TTC_BEST_TTC_1.toString()
					norcLowConsentBatch.bestTtcBestTtc1Codes = c.BEST_TTC_BEST_TTC_1_Codes.toString()
					norcLowConsentBatch.bestTtcBestTtc2 = c.BEST_TTC_BEST_TTC_2.toString()
					norcLowConsentBatch.bestTtc3 = c.BEST_TTC_3.toString()
					norcLowConsentBatch.phone = c.PHONE.toString()
					norcLowConsentBatch.phoneNbr = c.PHONE_NBR.toString()
					norcLowConsentBatch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						// println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						// println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcLowConsentBatch.whoConsented = c.WHO_CONSENTED.toString()
					norcLowConsentBatch.cn004c = c.CN004C.toString()
					try {
						dateTimeString = c.TIME_STAMP_2A.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.timeStamp2a = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.timeStamp2a = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2A: ${c.TIME_STAMP_2A.toString()}'
						// println '! parse TIME_STAMP_2A Input: ${c.TIME_STAMP_2A.toString()}'
						// println '! parse TIME_STAMP_2A Exception: ${e.toString()}'
					}
					norcLowConsentBatch.consentComments = c.CONSENT_COMMENTS.toString()
					norcLowConsentBatch.cn006 = c.CN006.toString()
					norcLowConsentBatch.cn006a = c.CN006A.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.CONSENT_DATE.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.consentDate = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.consentDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid CONSENT_DATE: ${c.CONSENT_DATE.toString()}'
						// println '! parse CONSENT_DATE Input: ${c.CONSENT_DATE.toString()}'
						// println '! parse CONSENT_DATE Exception: ${e.toString()}'
					}
					norcLowConsentBatch.english = c.ENGLISH.toString()
					norcLowConsentBatch.contactLang = c.CONTACT_LANG.toString()
					norcLowConsentBatch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcLowConsentBatch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcLowConsentBatch.interpret = c.INTERPRET.toString()
					norcLowConsentBatch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcLowConsentBatch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					norcLowConsentBatch.disp = c.disp.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcLowConsentBatch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcLowConsentBatch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowConsentBatch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcLowConsentBatch.hasErrors()) {
						response << "! norcLowConsentBatch has errors.\n"
					} else if (norcLowConsentBatch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcLowConsentBatch record with Respondent Serial ${norcLowConsentBatch.respondentSerial}"
						norcLowConsentBatch.errors.each{ e ->
							// println "norcLowConsentBatch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}				
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug){
			println "    Done! ${saveCount} records saved to norcLowConsentBatch in ${diff} seconds"			
		}
		// end LOWCONSENT_BATCH1

	}
	
	def parseLowQuex1Batch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"

		now = new Date()

		def saveCount = 0
		if (debug) {
			println "Parsing LOWQUEX1_BATCH1"
		}
		def startTime = System.nanoTime()
		//table?.LOWQUEX1_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.LOWQUEX1_BATCH1?.eachParallel { c ->
				NorcLowQuex1Batch.withTransaction {
					def checkSerial = c.Respondent_Serial.toString()
					def dateTimeString = ""
					def norcLowQuex1Batch = NorcLowQuex1Batch.findByRespondentSerial(checkSerial)
					if (!norcLowQuex1Batch) {
						norcLowQuex1Batch = new NorcLowQuex1Batch()
						response <<  " + Creating new NorcLowQuex1Batch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcLowQuex1Batch.lock()
						response <<  " ~ Updating existing NorcLowQuex1Batch(${checkSerial})\n"
					}
					norcLowQuex1Batch.respondentSerial = c.Respondent_Serial.toString()
					norcLowQuex1Batch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcLowQuex1Batch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcLowQuex1Batch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcLowQuex1Batch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcLowQuex1Batch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcLowQuex1Batch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcLowQuex1Batch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcLowQuex1Batch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcLowQuex1Batch.respondentId = c.Respondent_ID.toString()
					norcLowQuex1Batch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcLowQuex1Batch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcLowQuex1Batch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcLowQuex1Batch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcLowQuex1Batch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcLowQuex1Batch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcLowQuex1Batch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcLowQuex1Batch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcLowQuex1Batch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcLowQuex1Batch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcLowQuex1Batch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcLowQuex1Batch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcLowQuex1Batch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcLowQuex1Batch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcLowQuex1Batch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcLowQuex1Batch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcLowQuex1Batch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcLowQuex1Batch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcLowQuex1Batch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcLowQuex1Batch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcLowQuex1Batch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcLowQuex1Batch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcLowQuex1Batch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcLowQuex1Batch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcLowQuex1Batch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcLowQuex1Batch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcLowQuex1Batch.datacleaningNote = c.DataCleaning_Note.toString()
					norcLowQuex1Batch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcLowQuex1Batch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcLowQuex1Batch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcLowQuex1Batch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcLowQuex1Batch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcLowQuex1Batch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcLowQuex1Batch.suId = c.SU_ID.toString()
					norcLowQuex1Batch.majority = c.MAJORITY.toString()
					norcLowQuex1Batch.affiliate = c.AFFILIATE.toString()
					norcLowQuex1Batch.prFname = c.PR_FNAME.toString()
					norcLowQuex1Batch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcLowQuex1Batch.prLname = c.PR_LNAME.toString()
					norcLowQuex1Batch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcLowQuex1Batch.tsuId = c.TSU_ID.toString()
					norcLowQuex1Batch.tsuIdCodes = c.TSU_ID_Codes.toString()
					norcLowQuex1Batch.prPpgStatus = c.PR_PPG_STATUS.toString()
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.currmo = c.CURRMO.toString()
					norcLowQuex1Batch.currdy = c.CURRDY.toString()
					norcLowQuex1Batch.curryr = c.CURRYR.toString()
					norcLowQuex1Batch.quexlang = c.QUEXLANG.toString()
					norcLowQuex1Batch.calltype = c.CALLTYPE.toString()
					norcLowQuex1Batch.female1 = c.FEMALE_1.toString()
					norcLowQuex1Batch.bestTtcBestTtc1 = c.BEST_TTC_BEST_TTC_1.toString()
					norcLowQuex1Batch.bestTtcBestTtc1Codes = c.BEST_TTC_BEST_TTC_1_Codes.toString()
					norcLowQuex1Batch.bestTtcBestTtc2 = c.BEST_TTC_BEST_TTC_2.toString()
					norcLowQuex1Batch.bestTtc3 = c.BEST_TTC_3.toString()
					norcLowQuex1Batch.phone = c.PHONE.toString()
					norcLowQuex1Batch.phoneNbr = c.PHONE_NBR.toString()
					norcLowQuex1Batch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						// println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						// println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.ps002txt = c.ps002txt.toString()
					norcLowQuex1Batch.pregnant = c.PREGNANT.toString()
					norcLowQuex1Batch.ppgStatus = c.PPG_STATUS.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						// println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.lossInfo = c.LOSS_INFO.toString()
					norcLowQuex1Batch.dueDateMonth = c.DUE_DATE_MONTH.toString()
					norcLowQuex1Batch.dueDateMonthCodes = c.DUE_DATE_MONTH_Codes.toString()
					norcLowQuex1Batch.dueDateDay = c.DUE_DATE_DAY.toString()
					norcLowQuex1Batch.dueDateDayCodes = c.DUE_DATE_DAY_Codes.toString()
					norcLowQuex1Batch.dueDateYear = c.DUE_DATE_YEAR.toString()
					norcLowQuex1Batch.dueDateYearCodes = c.DUE_DATE_YEAR_Codes.toString()
					norcLowQuex1Batch.knowDate = c.KNOW_DATE.toString()
					norcLowQuex1Batch.datePeriodMonth = c.DATE_PERIOD_MONTH.toString()
					norcLowQuex1Batch.datePeriodMonthCodes = c.DATE_PERIOD_MONTH_Codes.toString()
					norcLowQuex1Batch.datePeriodDay = c.DATE_PERIOD_DAY.toString()
					norcLowQuex1Batch.datePeriodDayCodes = c.DATE_PERIOD_DAY_Codes.toString()
					norcLowQuex1Batch.datePeriodYear = c.DATE_PERIOD_YEAR.toString()
					norcLowQuex1Batch.datePeriodYearCodes = c.DATE_PERIOD_YEAR_Codes.toString()
					norcLowQuex1Batch.knewDate = c.KNEW_DATE.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.homeTest = c.HOME_TEST.toString()
					norcLowQuex1Batch.brthtxt = c.brthtxt.toString()
					norcLowQuex1Batch.birthPlan = c.BIRTH_PLAN.toString()
					norcLowQuex1Batch.birthplaceBirthPlace = c.BirthPlace_BIRTH_PLACE.toString()
					norcLowQuex1Batch.birthcBirPaceCds8 = c.Birthc_BIR_PACE_Cds8.toString()
					norcLowQuex1Batch.birthplaceBAddress1 = c.BirthPlace_B_ADDRESS_1.toString()
					norcLowQuex1Batch.birthcBAress1Cds9 = c.Birthc_B_ARESS_1_Cds9.toString()
					norcLowQuex1Batch.birthplaceBAddress2 = c.BirthPlace_B_ADDRESS_2.toString()
					norcLowQuex1Batch.birthcBAress2Cds10 = c.Birthc_B_ARESS_2_Cds10.toString()
					norcLowQuex1Batch.birthplaceBCity = c.BirthPlace_B_CITY.toString()
					norcLowQuex1Batch.birthplaceBCityCodes = c.BirthPlace_B_CITY_Codes.toString()
					norcLowQuex1Batch.birthplaceBState = c.BirthPlace_B_STATE.toString()
					norcLowQuex1Batch.birthplaceBZipcode = c.BirthPlace_B_ZIPCODE.toString()
					norcLowQuex1Batch.birthplaceBZipcodeCodes = c.BirthPlace_B_ZIPCODE_Codes.toString()
					norcLowQuex1Batch.pnVitamin = c.PN_VITAMIN.toString()
					norcLowQuex1Batch.pregVitamin = c.PREG_VITAMIN.toString()
					norcLowQuex1Batch.dateVisitMonth = c.DATE_VISIT_MONTH.toString()
					norcLowQuex1Batch.dateVisitMonthCodes = c.DATE_VISIT_MONTH_Codes.toString()
					norcLowQuex1Batch.dateVisitDay = c.DATE_VISIT_DAY.toString()
					norcLowQuex1Batch.dateVisitDayCodes = c.DATE_VISIT_DAY_Codes.toString()
					norcLowQuex1Batch.dateVisitYear = c.DATE_VISIT_YEAR.toString()
					norcLowQuex1Batch.dateVisitYearCodes = c.DATE_VISIT_YEAR_Codes.toString()
					norcLowQuex1Batch.diabtxt = c.diabtxt.toString()
					norcLowQuex1Batch.diabtxt2 = c.diabtxt2.toString()
					norcLowQuex1Batch.diabetes1 = c.DIABETES_1.toString()
					norcLowQuex1Batch.highbpPreg = c.HIGHBP_PREG.toString()
					norcLowQuex1Batch.urine = c.URINE.toString()
					norcLowQuex1Batch.preeclamp = c.PREECLAMP.toString()
					norcLowQuex1Batch.earlyLabor = c.EARLY_LABOR.toString()
					norcLowQuex1Batch.anemia = c.ANEMIA.toString()
					norcLowQuex1Batch.nausea = c.NAUSEA.toString()
					norcLowQuex1Batch.kidney = c.KIDNEY.toString()
					norcLowQuex1Batch.rhDisease = c.RH_DISEASE.toString()
					norcLowQuex1Batch.groupB = c.GROUP_B.toString()
					norcLowQuex1Batch.herpes = c.HERPES.toString()
					norcLowQuex1Batch.vaginosis = c.VAGINOSIS.toString()
					norcLowQuex1Batch.othCondition = c.OTH_CONDITION.toString()
					norcLowQuex1Batch.conditionOth = c.CONDITION_OTH.toString()
					norcLowQuex1Batch.conditionOthCodes = c.CONDITION_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.healthtxt = c.healthtxt.toString()
					norcLowQuex1Batch.health = c.HEALTH.toString()
					norcLowQuex1Batch.heightHeightFt = c.Height_HEIGHT_FT.toString()
					norcLowQuex1Batch.heightHeightFtCodes = c.Height_HEIGHT_FT_Codes.toString()
					norcLowQuex1Batch.heightHtInch = c.Height_HT_INCH.toString()
					norcLowQuex1Batch.heightHtInchCodes = c.Height_HT_INCH_Codes.toString()
					norcLowQuex1Batch.weight = c.WEIGHT.toString()
					norcLowQuex1Batch.weightCodes = c.WEIGHT_Codes.toString()
					norcLowQuex1Batch.asthma = c.ASTHMA.toString()
					norcLowQuex1Batch.highbpNotpreg = c.HIGHBP_NOTPREG.toString()
					norcLowQuex1Batch.diabetesNotpreg = c.DIABETES_NOTPREG.toString()
					norcLowQuex1Batch.diabetes2 = c.DIABETES_2.toString()
					norcLowQuex1Batch.diabetes3 = c.DIABETES_3.toString()
					norcLowQuex1Batch.thyroid1 = c.THYROID_1.toString()
					norcLowQuex1Batch.thyroid2 = c.THYROID_2.toString()
					norcLowQuex1Batch.hlthCare = c.HLTH_CARE.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.insure = c.INSURE.toString()
					norcLowQuex1Batch.insEmploy = c.INS_EMPLOY.toString()
					norcLowQuex1Batch.insMedicaid = c.INS_MEDICAID.toString()
					norcLowQuex1Batch.insTricare = c.INS_TRICARE.toString()
					norcLowQuex1Batch.insIhs = c.INS_IHS.toString()
					norcLowQuex1Batch.insMedicare = c.INS_MEDICARE.toString()
					norcLowQuex1Batch.insOth = c.INS_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.ageHome = c.AGE_HOME.toString()
					norcLowQuex1Batch.mainHeat = c.MAIN_HEAT.toString()
					norcLowQuex1Batch.mainHeatOth = c.MAIN_HEAT_OTH.toString()
					norcLowQuex1Batch.mainHeatOthCodes = c.MAIN_HEAT_OTH_Codes.toString()
					norcLowQuex1Batch.cool01 = c.COOL_01.toString()
					norcLowQuex1Batch.cool02 = c.COOL_02.toString()
					norcLowQuex1Batch.cool03 = c.COOL_03.toString()
					norcLowQuex1Batch.cool04 = c.COOL_04.toString()
					norcLowQuex1Batch.cool05 = c.COOL_05.toString()
					norcLowQuex1Batch.cool06 = c.COOL_06.toString()
					norcLowQuex1Batch.cool07 = c.COOL_07.toString()
					norcLowQuex1Batch.coolOth = c.COOL_OTH.toString()
					norcLowQuex1Batch.coolOthCodes = c.COOL_OTH_Codes.toString()
					norcLowQuex1Batch.waterDrink = c.WATER_DRINK.toString()
					norcLowQuex1Batch.waterDrinkOth = c.WATER_DRINK_OTH.toString()
					norcLowQuex1Batch.waterDrinkOthCodes = c.WATER_DRINK_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.cigNow = c.CIG_NOW.toString()
					norcLowQuex1Batch.cigNowFreq = c.CIG_NOW_FREQ.toString()
					norcLowQuex1Batch.cigNowNum = c.CIG_NOW_NUM.toString()
					norcLowQuex1Batch.cigNowNumCodes = c.CIG_NOW_NUM_Codes.toString()
					norcLowQuex1Batch.cigNowNumField = c.CIG_NOW_NUM_FIELD.toString()
					norcLowQuex1Batch.drinkNow = c.DRINK_NOW.toString()
					norcLowQuex1Batch.drinkNowNum = c.DRINK_NOW_NUM.toString()
					norcLowQuex1Batch.drinkNowNumCodes = c.DRINK_NOW_NUM_Codes.toString()
					norcLowQuex1Batch.drinkNow5 = c.DRINK_NOW_5.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						// println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						// println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.learn = c.LEARN.toString()
					norcLowQuex1Batch.help = c.HELP.toString()
					norcLowQuex1Batch.incent = c.INCENT.toString()
					norcLowQuex1Batch.research = c.RESEARCH.toString()
					norcLowQuex1Batch.envir = c.ENVIR.toString()
					norcLowQuex1Batch.community = c.COMMUNITY.toString()
					norcLowQuex1Batch.knowOthers = c.KNOW_OTHERS.toString()
					norcLowQuex1Batch.family = c.FAMILY.toString()
					norcLowQuex1Batch.doctor = c.DOCTOR.toString()
					norcLowQuex1Batch.opinSpouse = c.OPIN_SPOUSE.toString()
					norcLowQuex1Batch.opinFamily = c.OPIN_FAMILY.toString()
					norcLowQuex1Batch.opinFriend = c.OPIN_FRIEND.toString()
					norcLowQuex1Batch.opinDr = c.OPIN_DR.toString()
					norcLowQuex1Batch.experience = c.EXPERIENCE.toString()
					norcLowQuex1Batch.improve = c.IMPROVE.toString()
					norcLowQuex1Batch.intLength = c.INT_LENGTH.toString()
					norcLowQuex1Batch.intStress = c.INT_STRESS.toString()
					norcLowQuex1Batch.intRepeat = c.INT_REPEAT.toString()
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						// println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						// println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.end11txt = c.END1_1txt.toString()
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						// println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						// println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcLowQuex1Batch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcLowQuex1Batch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcLowQuex1Batch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcLowQuex1Batch.hasErrors()) {
						response << "! norcLowQuex1Batch has errors.\n"
					} else if (norcLowQuex1Batch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcLowQuex1Batch record with Respondent Serial ${norcLowQuex1Batch.respondentSerial}"
						norcLowQuex1Batch.errors.each{ e ->
							// println "norcLowQuex1Batch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}				
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000

		if (debug){
			println "    Done! ${saveCount} records saved to norcLowQuex1Batch"			
		}
		// end LOWQUEX1_BATCH1

	}

	def parseScreenerBatch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"

		now = new Date()

		def saveCount = 0
		if (debug) {
			println "Parsing SCREENER_BATCH1"
		}
		def startTime = System.nanoTime()
		//table?.SCREENER_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.SCREENER_BATCH1?.eachParallel { c ->
				NorcScreenerBatch.withTransaction {
					def checkSerial = c.Respondent_Serial.toString()
					def dateTimeString = ""
					def norcScreenerBatch = NorcScreenerBatch.findByRespondentSerial(checkSerial)				
		
					if (!norcScreenerBatch) {
						norcScreenerBatch = new NorcScreenerBatch()
						response <<  " + Creating new NorcScreenerBatch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcScreenerBatch.lock()
						response <<  " ~ Updating existing NorcScreenerBatch(${checkSerial})\n"
					}
					norcScreenerBatch.respondentSerial = c.Respondent_Serial.toString()
					norcScreenerBatch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcScreenerBatch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcScreenerBatch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcScreenerBatch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcScreenerBatch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcScreenerBatch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcScreenerBatch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcScreenerBatch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcScreenerBatch.respondentId = c.Respondent_ID.toString()
					norcScreenerBatch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcScreenerBatch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcScreenerBatch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcScreenerBatch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcScreenerBatch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcScreenerBatch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcScreenerBatch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcScreenerBatch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcScreenerBatch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcScreenerBatch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						// println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						// println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcScreenerBatch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcScreenerBatch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcScreenerBatch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcScreenerBatch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcScreenerBatch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcScreenerBatch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcScreenerBatch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcScreenerBatch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcScreenerBatch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcScreenerBatch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcScreenerBatch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcScreenerBatch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcScreenerBatch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcScreenerBatch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcScreenerBatch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcScreenerBatch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcScreenerBatch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcScreenerBatch.datacleaningNote = c.DataCleaning_Note.toString()
					norcScreenerBatch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcScreenerBatch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcScreenerBatch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcScreenerBatch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcScreenerBatch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcScreenerBatch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcScreenerBatch.suId = c.SU_ID.toString()
					norcScreenerBatch.affiliate = c.AFFILIATE.toString()
					norcScreenerBatch.majority = c.MAJORITY.toString()
					norcScreenerBatch.prFname = c.PR_FNAME.toString()
					norcScreenerBatch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcScreenerBatch.prLname = c.PR_LNAME.toString()
					norcScreenerBatch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcScreenerBatch.method = c.METHOD.toString()
					norcScreenerBatch.hilosamp = c.HILOSAMP.toString()
					norcScreenerBatch.rGender = c.R_GENDER.toString()
					norcScreenerBatch.rGenderCodes = c.R_GENDER_Codes.toString()
					norcScreenerBatch.institution = c.INSTITUTION.toString()
					norcScreenerBatch.institutionCodes = c.INSTITUTION_Codes.toString()
					norcScreenerBatch.provider = c.PROVIDER.toString()
					norcScreenerBatch.providerCodes = c.PROVIDER_Codes.toString()
					norcScreenerBatch.prAddress1 = c.PR_ADDRESS_1.toString()
					norcScreenerBatch.prAddress1Codes = c.PR_ADDRESS_1_Codes.toString()
					norcScreenerBatch.prAddress2 = c.PR_ADDRESS_2.toString()
					norcScreenerBatch.prAddress2Codes = c.PR_ADDRESS_2_Codes.toString()
					norcScreenerBatch.prUnit = c.PR_UNIT.toString()
					norcScreenerBatch.prUnitCodes = c.PR_UNIT_Codes.toString()
					norcScreenerBatch.prCity = c.PR_CITY.toString()
					norcScreenerBatch.prCityCodes = c.PR_CITY_Codes.toString()
					norcScreenerBatch.prState = c.PR_STATE.toString()
					norcScreenerBatch.prStateCodes = c.PR_STATE_Codes.toString()
					norcScreenerBatch.prZip = c.PR_ZIP.toString()
					norcScreenerBatch.prZipCodes = c.PR_ZIP_Codes.toString()
					norcScreenerBatch.prZip4 = c.PR_ZIP4.toString()
					norcScreenerBatch.prZip4Codes = c.PR_ZIP4_Codes.toString()
					norcScreenerBatch.tsuId = c.TSU_ID.toString()
					norcScreenerBatch.tsuIdCodes = c.TSU_ID_Codes.toString()
					norcScreenerBatch.psuId = c.PSU_ID.toString()
					norcScreenerBatch.psuIdCodes = c.PSU_ID_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						// println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcScreenerBatch.quexlang = c.QUEXLANG.toString()
					norcScreenerBatch.timeStamp2 = c.TIME_STAMP_2.toString()
					norcScreenerBatch.sameday = c.SAMEDAY.toString()
					norcScreenerBatch.sameresp = c.SAMERESP.toString()
					norcScreenerBatch.calltype = c.CALLTYPE.toString()
					norcScreenerBatch.female1 = c.FEMALE_1.toString()
					norcScreenerBatch.bestTtcBestTtc1 = c.BEST_TTC_BEST_TTC_1.toString()
					norcScreenerBatch.bestTtcBestTtc1Codes = c.BEST_TTC_BEST_TTC_1_Codes.toString()
					norcScreenerBatch.bestTtcBestTtc2 = c.BEST_TTC_BEST_TTC_2.toString()
					norcScreenerBatch.bestTtc3 = c.BEST_TTC_3.toString()
					norcScreenerBatch.phone = c.PHONE.toString()
					norcScreenerBatch.phoneNbr2 = c.PHONE_NBR_2.toString()
					norcScreenerBatch.phoneNbr2Codes = c.PHONE_NBR_2_Codes.toString()
					norcScreenerBatch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcScreenerBatch.rNameRFnameCodes = c.R_NAME_R_FNAME_Codes.toString()
					norcScreenerBatch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcScreenerBatch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcScreenerBatch.currmo = c.CURRMO.toString()
					norcScreenerBatch.currdy = c.CURRDY.toString()
					norcScreenerBatch.curryr = c.CURRYR.toString()
					norcScreenerBatch.personDobMonth = c.PERSON_DOB_MONTH.toString()
					norcScreenerBatch.personDobMonthCodes = c.PERSON_DOB_MONTH_Codes.toString()
					norcScreenerBatch.personDobDay = c.PERSON_DOB_DAY.toString()
					norcScreenerBatch.personDobDayCodes = c.PERSON_DOB_DAY_Codes.toString()
					norcScreenerBatch.personDobYear = c.PERSON_DOB_YEAR.toString()
					norcScreenerBatch.personDobYearCodes = c.PERSON_DOB_YEAR_Codes.toString()
					norcScreenerBatch.age = c.AGE.toString()
					norcScreenerBatch.ageCodes = c.AGE_Codes.toString()
					norcScreenerBatch.ageRange = c.AGE_RANGE.toString()
					norcScreenerBatch.ageElig = c.AGE_ELIG.toString()
					norcScreenerBatch.ppgFirst = c.PPG_FIRST.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						// println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcScreenerBatch.addressDkrefaddr = c.ADDRESS_DKREFADDR.toString()
					norcScreenerBatch.addressAddress1 = c.ADDRESS_ADDRESS_1.toString()
					norcScreenerBatch.addressAddress1Codes = c.ADDRESS_ADDRESS_1_Codes.toString()
					norcScreenerBatch.addressAddress2 = c.ADDRESS_ADDRESS_2.toString()
					norcScreenerBatch.addressAddress2Codes = c.ADDRESS_ADDRESS_2_Codes.toString()
					norcScreenerBatch.addressUnit = c.ADDRESS_UNIT.toString()
					norcScreenerBatch.addressUnitCodes = c.ADDRESS_UNIT_Codes.toString()
					norcScreenerBatch.addressCity = c.ADDRESS_CITY.toString()
					norcScreenerBatch.addressCityCodes = c.ADDRESS_CITY_Codes.toString()
					norcScreenerBatch.addressState = c.ADDRESS_STATE.toString()
					norcScreenerBatch.addressZip = c.ADDRESS_ZIP.toString()
					norcScreenerBatch.addressZipCodes = c.ADDRESS_ZIP_Codes.toString()
					norcScreenerBatch.addressZip4 = c.ADDRESS_ZIP4.toString()
					norcScreenerBatch.addressZip4Codes = c.ADDRESS_ZIP4_Codes.toString()
					norcScreenerBatch.duEligConfirm = c.DU_ELIG_CONFIRM.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						// println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcScreenerBatch.knowNcs = c.KNOW_NCS.toString()
					norcScreenerBatch.howKnowNcs01 = c.HOW_KNOW_NCS_01.toString()
					norcScreenerBatch.howKnowNcs02 = c.HOW_KNOW_NCS_02.toString()
					norcScreenerBatch.howKnowNcs03 = c.HOW_KNOW_NCS_03.toString()
					norcScreenerBatch.howKnowNcs04 = c.HOW_KNOW_NCS_04.toString()
					norcScreenerBatch.howKnowNcs05 = c.HOW_KNOW_NCS_05.toString()
					norcScreenerBatch.howKnowNcs06 = c.HOW_KNOW_NCS_06.toString()
					norcScreenerBatch.howKnowNcs07 = c.HOW_KNOW_NCS_07.toString()
					norcScreenerBatch.howKnowNcs08 = c.HOW_KNOW_NCS_08.toString()
					norcScreenerBatch.howKnowNcs09 = c.HOW_KNOW_NCS_09.toString()
					norcScreenerBatch.howKnowNcs10 = c.HOW_KNOW_NCS_10.toString()
					norcScreenerBatch.howKnowNcs11 = c.HOW_KNOW_NCS_11.toString()
					norcScreenerBatch.howKnowNcs12 = c.HOW_KNOW_NCS_12.toString()
					norcScreenerBatch.howKnowNcs13 = c.HOW_KNOW_NCS_13.toString()
					norcScreenerBatch.howKnowNcs14 = c.HOW_KNOW_NCS_14.toString()
					norcScreenerBatch.howKnowNcs15 = c.HOW_KNOW_NCS_15.toString()
					norcScreenerBatch.howKnowNcs16 = c.HOW_KNOW_NCS_16.toString()
					norcScreenerBatch.howKnowNcs17 = c.HOW_KNOW_NCS_17.toString()
					norcScreenerBatch.howKnowNcs18 = c.HOW_KNOW_NCS_18.toString()
					norcScreenerBatch.howKnowNcs19 = c.HOW_KNOW_NCS_19.toString()
					norcScreenerBatch.howKnowNcs20 = c.HOW_KNOW_NCS_20.toString()
					norcScreenerBatch.howKnowNcs21 = c.HOW_KNOW_NCS_21.toString()
					norcScreenerBatch.howKnowNcs22 = c.HOW_KNOW_NCS_22.toString()
					norcScreenerBatch.howKnowNcs23 = c.HOW_KNOW_NCS_23.toString()
					norcScreenerBatch.howKnowNcsOth = c.HOW_KNOW_NCS_OTH.toString()
					norcScreenerBatch.howKnowNcsOthCodes = c.HOW_KNOW_NCS_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						// println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcScreenerBatch.pregnant = c.PREGNANT.toString()
					norcScreenerBatch.origDueDateMonth = c.ORIG_DUE_DATE_MONTH.toString()
					norcScreenerBatch.origDueDateMonthCodes = c.ORIG_DUE_DATE_MONTH_Codes.toString()
					norcScreenerBatch.origDueDateDay = c.ORIG_DUE_DATE_DAY.toString()
					norcScreenerBatch.origDueDateDayCodes = c.ORIG_DUE_DATE_DAY_Codes.toString()
					norcScreenerBatch.origDueDateYear = c.ORIG_DUE_DATE_YEAR.toString()
					norcScreenerBatch.origDueDateYearCodes = c.ORIG_DUE_DATE_YEAR_Codes.toString()
					norcScreenerBatch.datePeriodMonth = c.DATE_PERIOD_MONTH.toString()
					norcScreenerBatch.datePeriodMonthCodes = c.DATE_PERIOD_MONTH_Codes.toString()
					norcScreenerBatch.datePeriodDay = c.DATE_PERIOD_DAY.toString()
					norcScreenerBatch.datePeriodDayCodes = c.DATE_PERIOD_DAY_Codes.toString()
					norcScreenerBatch.datePeriodYear = c.DATE_PERIOD_YEAR.toString()
					norcScreenerBatch.datePeriodYearCodes = c.DATE_PERIOD_YEAR_Codes.toString()
					norcScreenerBatch.weeksPreg = c.WEEKS_PREG.toString()
					norcScreenerBatch.weeksPregCodes = c.WEEKS_PREG_Codes.toString()
					norcScreenerBatch.monthPreg = c.MONTH_PREG.toString()
					norcScreenerBatch.monthPregCodes = c.MONTH_PREG_Codes.toString()
					norcScreenerBatch.trimester = c.TRIMESTER.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						// println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcScreenerBatch.trying = c.TRYING.toString()
					norcScreenerBatch.hyster = c.HYSTER.toString()
					norcScreenerBatch.ovaries = c.OVARIES.toString()
					norcScreenerBatch.tubesTied = c.TUBES_TIED.toString()
					norcScreenerBatch.menopause = c.MENOPAUSE.toString()
					norcScreenerBatch.medUnable = c.MED_UNABLE.toString()
					norcScreenerBatch.medUnableOth = c.MED_UNABLE_OTH.toString()
					norcScreenerBatch.medUnableOthCodes = c.MED_UNABLE_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						// println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcScreenerBatch.maristat = c.MARISTAT.toString()
					norcScreenerBatch.educ = c.EDUC.toString()
					norcScreenerBatch.employ = c.EMPLOY.toString()
					norcScreenerBatch.employOth = c.EMPLOY_OTH.toString()
					norcScreenerBatch.employOthCodes = c.EMPLOY_OTH_Codes.toString()
					norcScreenerBatch.ethnicity = c.ETHNICITY.toString()
					norcScreenerBatch.race01 = c.RACE_01.toString()
					norcScreenerBatch.race02 = c.RACE_02.toString()
					norcScreenerBatch.race03 = c.RACE_03.toString()
					norcScreenerBatch.race04 = c.RACE_04.toString()
					norcScreenerBatch.race05 = c.RACE_05.toString()
					norcScreenerBatch.race06 = c.RACE_06.toString()
					norcScreenerBatch.race07 = c.RACE_07.toString()
					norcScreenerBatch.race08 = c.RACE_08.toString()
					norcScreenerBatch.raceOth = c.RACE_OTH.toString()
					norcScreenerBatch.raceOthCodes = c.RACE_OTH_Codes.toString()
					norcScreenerBatch.personLang = c.PERSON_LANG.toString()
					norcScreenerBatch.personLangOth = c.PERSON_LANG_OTH.toString()
					norcScreenerBatch.personLangOthCodes = c.PERSON_LANG_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						// println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						// println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcScreenerBatch.hhMembers = c.HH_MEMBERS.toString()
					norcScreenerBatch.hhMembersCodes = c.HH_MEMBERS_Codes.toString()
					norcScreenerBatch.numChild = c.NUM_CHILD.toString()
					norcScreenerBatch.numChildCodes = c.NUM_CHILD_Codes.toString()
					norcScreenerBatch.income = c.INCOME.toString()
					try {
						dateTimeString = c.TIME_STAMP_10.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp10 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp10 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_10: ${c.TIME_STAMP_10.toString()}'
						// println '! parse TIME_STAMP_10 Input: ${c.TIME_STAMP_10.toString()}'
						// println '! parse TIME_STAMP_10 Exception: ${e.toString()}'
					}
					norcScreenerBatch.phoneNbr = c.PHONE_NBR.toString()
					norcScreenerBatch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					norcScreenerBatch.phoneType = c.PHONE_TYPE.toString()
					norcScreenerBatch.phoneTypeOth = c.PHONE_TYPE_OTH.toString()
					norcScreenerBatch.phoneTypeOthCodes = c.PHONE_TYPE_OTH_Codes.toString()
					norcScreenerBatch.homePhone = c.HOME_PHONE.toString()
					norcScreenerBatch.homePhoneCodes = c.HOME_PHONE_Codes.toString()
					norcScreenerBatch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcScreenerBatch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcScreenerBatch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcScreenerBatch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcScreenerBatch.cellPhone = c.CELL_PHONE.toString()
					norcScreenerBatch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					norcScreenerBatch.noPhNum = c.NO_PH_NUM.toString()
					norcScreenerBatch.sameAddr = c.SAME_ADDR.toString()
					norcScreenerBatch.mailaddrMailAddress1 = c.MAILADDR_MAIL_ADDRESS1.toString()
					norcScreenerBatch.maiadmaiAress1Cds8 = c.MAIADMAI_ARESS1_Cds8.toString()
					norcScreenerBatch.mailaddrMailAddress2 = c.MAILADDR_MAIL_ADDRESS2.toString()
					norcScreenerBatch.maiadmaiAress2Cds9 = c.MAIADMAI_ARESS2_Cds9.toString()
					norcScreenerBatch.mailaddrMailUnit = c.MAILADDR_MAIL_UNIT.toString()
					norcScreenerBatch.mailaddrMailUnitCodes = c.MAILADDR_MAIL_UNIT_Codes.toString()
					norcScreenerBatch.mailaddrMailCity = c.MAILADDR_MAIL_CITY.toString()
					norcScreenerBatch.mailaddrMailCityCodes = c.MAILADDR_MAIL_CITY_Codes.toString()
					norcScreenerBatch.mailaddrMailState = c.MAILADDR_MAIL_STATE.toString()
					norcScreenerBatch.mailaddrMailZip = c.MAILADDR_MAIL_ZIP.toString()
					norcScreenerBatch.mailaddrMailZipCodes = c.MAILADDR_MAIL_ZIP_Codes.toString()
					norcScreenerBatch.mailaddrMailZip4 = c.MAILADDR_MAIL_ZIP4.toString()
					norcScreenerBatch.mailaddrMailZip4Codes = c.MAILADDR_MAIL_ZIP4_Codes.toString()
					norcScreenerBatch.haveEmail = c.HAVE_EMAIL.toString()
					norcScreenerBatch.email = c.EMAIL.toString()
					norcScreenerBatch.emailCodes = c.EMAIL_Codes.toString()
					norcScreenerBatch.emailType = c.EMAIL_TYPE.toString()
					norcScreenerBatch.emailShare = c.EMAIL_SHARE.toString()
					norcScreenerBatch.planMove = c.PLAN_MOVE.toString()
					norcScreenerBatch.whereMove = c.WHERE_MOVE.toString()
					norcScreenerBatch.moveInfo = c.MOVE_INFO.toString()
					norcScreenerBatch.newaddrNewAddress1 = c.NEWADDR_NEW_ADDRESS1.toString()
					norcScreenerBatch.newaddrNewAddress1Codes = c.NEWADDR_NEW_ADDRESS1_Codes.toString()
					norcScreenerBatch.newaddrNewAddress2 = c.NEWADDR_NEW_ADDRESS2.toString()
					norcScreenerBatch.newaddrNewAddress2Codes = c.NEWADDR_NEW_ADDRESS2_Codes.toString()
					norcScreenerBatch.newaddrNewUnit = c.NEWADDR_NEW_UNIT.toString()
					norcScreenerBatch.newaddrNewUnitCodes = c.NEWADDR_NEW_UNIT_Codes.toString()
					norcScreenerBatch.newaddrNewCity = c.NEWADDR_NEW_CITY.toString()
					norcScreenerBatch.newaddrNewCityCodes = c.NEWADDR_NEW_CITY_Codes.toString()
					norcScreenerBatch.newaddrNewState = c.NEWADDR_NEW_STATE.toString()
					norcScreenerBatch.newaddrNewZip = c.NEWADDR_NEW_ZIP.toString()
					norcScreenerBatch.newaddrNewZipCodes = c.NEWADDR_NEW_ZIP_Codes.toString()
					norcScreenerBatch.newaddrNewZip4 = c.NEWADDR_NEW_ZIP4.toString()
					norcScreenerBatch.newaddrNewZip4Codes = c.NEWADDR_NEW_ZIP4_Codes.toString()
					norcScreenerBatch.whenMove = c.WHEN_MOVE.toString()
					norcScreenerBatch.dateMoveMonth = c.DATE_MOVE_MONTH.toString()
					norcScreenerBatch.dateMoveMonthCodes = c.DATE_MOVE_MONTH_Codes.toString()
					norcScreenerBatch.dateMoveYear = c.DATE_MOVE_YEAR.toString()
					norcScreenerBatch.dateMoveYearCodes = c.DATE_MOVE_YEAR_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						// println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						// println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						// println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						// println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcScreenerBatch.otherFemale = c.OTHER_FEMALE.toString()
					norcScreenerBatch.femaleEnd1a = c.FEMALE_END_1A.toString()
					norcScreenerBatch.femaleEnd4 = c.FEMALE_END_4.toString()
					try {
						dateTimeString = c.TIME_STAMP_13.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp13 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp13 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_13: ${c.TIME_STAMP_13.toString()}'
						// println '! parse TIME_STAMP_13 Input: ${c.TIME_STAMP_13.toString()}'
						// println '! parse TIME_STAMP_13 Exception: ${e.toString()}'
					}
					norcScreenerBatch.english = c.ENGLISH.toString()
					norcScreenerBatch.contactLang = c.CONTACT_LANG.toString()
					norcScreenerBatch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcScreenerBatch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcScreenerBatch.interpret = c.INTERPRET.toString()
					norcScreenerBatch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcScreenerBatch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_14.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.timeStamp14 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.timeStamp14 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_14: ${c.TIME_STAMP_14.toString()}'
						// println '! parse TIME_STAMP_14 Input: ${c.TIME_STAMP_14.toString()}'
						// println '! parse TIME_STAMP_14 Exception: ${e.toString()}'
					}
					norcScreenerBatch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcScreenerBatch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcScreenerBatch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						// println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcScreenerBatch.hasErrors()) {
						response << "! norcScreenerBatch has errors.\n"
					} else if (norcScreenerBatch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcHhBatch record with Respondent Serial ${norcScreenerBatch.respondentSerial}"
						norcScreenerBatch.errors.each{ e ->
							// println "norcScreenerBatch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}				
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcScreenerBatch in ${diff} seconds"			
		}
		// end SCREENER_BATCH1
	}

	def parseNonResponseList(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"

		now = new Date()
		
		def saveCount = 0
		if (debug){
			println "Parsing NONRESPONSE_LIST"			
		}
		def startTime = System.nanoTime()
		//table?.NONRESPONSE_LIST?.each{ c ->
		GParsPool.withPool {
			table?.NONRESPONSE_LIST?.eachParallel { c ->
				NorcNonResponseList.withTransaction {
					def dateTimeString = ""
					def checkSerial  = c.su_id.toString()
					def norcNonResponseList = NorcNonResponseList.findBySuId(checkSerial)
					if (!norcNonResponseList) {
						norcNonResponseList = new NorcNonResponseList()
						response <<  " + Creating new NorcNonResponseList(${checkSerial})\n"
					} else {
						response <<  " ~ Updating existing NorcNonResponseList(${checkSerial})\n"
					}
		
					norcNonResponseList.suId = c.su_id.toString()
					norcNonResponseList.promptDisp = c.prompt_disp.toString()
					norcNonResponseList.promptDispDate = c.prompt_disp_date.toString()
					norcNonResponseList.promptDispLabel = c.prompt_disp_label.toString()
					norcNonResponseList.mail = c.mail.toString()
					norcNonResponseList.mailUndeliverable = c.mail_undeliverable.toString()
					
					// Save the record
					if (norcNonResponseList.hasErrors()) {
						response << "! norcNonResponseList has errors.\n"
					} else if (norcNonResponseList.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcNonResponseList record with Respondent Serial ${norcNonResponseList.suId}"
						norcNonResponseList.errors.each{ e ->
							// println "norcNonResponseList:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}				
				}
			}
		}		
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {			
			println "    Done! ${saveCount} records saved to norcNonResponseList in ${diff} seconds"
		}
		// end NONRESPONSE_LIST
	}
	
	def parseIncentive(table, response) {
		//table?.INCENTIVE_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.INCENTIVE_BATCH1?.eachParallel { c ->
				NorcIncentive.withTransaction {
	
					def mailingId = c.MailingID.toString()
		
					def norcIncentive = NorcIncentive.findByMailingId(mailingId)
		
					if (! norcIncentive) {
						norcIncentive = new NorcIncentive()
						response <<  " + Creating new NorcIncentive(${mailingId})\n"
					} else {
						response <<  " ~ Updating existing NorcIncentive(${mailingId})\n"
					}
		
		
					norcIncentive.firstName = c.fName.toString()
					norcIncentive.lastName = c.lName.toString()
					norcIncentive.address1 = c.Address1.toString()
					norcIncentive.address2 = c.Address2.toString()
					norcIncentive.addressUnit = c.Unit.toString()
					norcIncentive.city = c.City.toString()
					norcIncentive.state = c.State.toString()
					norcIncentive.zipCode = c.Zip.toString()
					norcIncentive.zipFour = c.Zip4.toString()
					norcIncentive.norcSuId = c.SU_ID.toString()
					norcIncentive.mailingId = c.MailingID.toString()
		
					def incentiveAmountString = c.incentiveAmount.toString()
					try {
						if (incentiveAmountString.isBigDecimal()) {
							norcIncentive.incentiveAmount = incentiveAmountString.toBigDecimal()
						} else {
							response <<  "! Invalid incentiveAmount: ${c.incentiveAmount.toString()}.\n"
						}
					} catch (Exception e) {
						println "! parse incentiveAmount Exception: ${e.toString()}"
						response <<  "! Invalid incentiveAmount: ${c.incentiveAmount.toString()}.\n"
					}
		
					def incentiveMailed = c.incentiveMailed.toString()
					if (incentiveMailed == "1") {
						norcIncentive.incentiveMailed = true
					} else if (incentiveMailed == "0") {
						norcIncentive.incentiveMailed = false
					} else if ( ! incentiveMailed ) {
						norcIncentive.incentiveMailed = null
					} else {
						response <<  "! Unknown incentiveMailed: ${incentiveMailed}.  1 or 0 expect.\n"
					}
		
		
		
					// Read, parse, and lookup the docType
					def docTypeString = c.DocType.toString()
					try {
						if ( ! docTypeString ) {
							norcIncentive.docType = null
						} else {
							def docType = NorcDocType.findByValue(Integer.parseInt(docTypeString))
							if (docType) {
								norcIncentive.docType = docType
							} else {
								response <<  "! Unknown DocType: ${docTypeString}.  Please update FMTS.DOCTYPE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid DocType: ${docTypeString}"
					}
		
					// Read, parse, and lookup the Source
					def sourceString = c.Source.toString()
					try {
						if ( ! sourceString ) {
							norcIncentive.source = null
						} else {
							def source = NorcSource.findByValue(Integer.parseInt(sourceString))
							if (source) {
								norcIncentive.source = source
							} else {
								response <<  "! Unknown Source: ${sourceString}.  Please update FMTS.SOURCE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Source: ${sourceString}"
					}
		
					// Read, parse, and lookup the Status
					def statusString = c.Status.toString()
					try {
						if ( ! statusString ) {
							norcIncentive.status = null
						} else {
							def status = NorcStatus.findByValue(Integer.parseInt(statusString))
							if (status) {
								norcIncentive.status = status
							} else {
								response <<  "! Unknown Status: ${statusString}.  Please update FMTS.STATUS first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Status: ${statusString}\n"
						println "! parse Status Exception: ${e.toString()}"
					}
		
					// Read, parse, and lookup the Mode
					def modeString = c.Mode.toString()
					try {
						if ( ! modeString ) {
							norcIncentive.mode = null
						} else {
							def mode = NorcDocMode.findByValue(Integer.parseInt(modeString))
							if (mode) {
								norcIncentive.mode = mode
							} else {
								response <<  "! Unknown Mode: ${modeString}.  Please update FMTS.MODE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Mode: ${modeString}\n"
						println "! parse Mode Exception: ${e.toString()}"
					}
		
					// Incentive Date
					try {
						def incentiveDateString = c.DateIn.toString()
						if ( ! incentiveDateString ) {
							norcIncentive.incentiveDate = null
						} else {
							DateTime dt = fmt.parseDateTime(incentiveDateString)
							norcIncentive.incentiveDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << "! Invalid DateIn: ${c.DateIn.toString()}\n"
						println "! parse DateIn Input: ${c.DateIn.toString()}"
						println "! parse DateIn Exception: ${e.toString()}"
					}
		
					// App Time & Date
					try {
						def appTimeString = c.AppTime.toString()
						if ( ! appTimeString ) {
							norcIncentive.appTime = null
						} else {
							DateTime dt = fmt.parseDateTime(appTimeString)
							norcIncentive.appTime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << "! Invalid AppTime: ${c.AppTime.toString()}\n"
						println "! parse AppTime Input: ${c.AppTime.toString()}"
						println "! parse AppTime Exception: ${e.toString()}"
					}
		
					// PS Date
					try {
						def pregnancyScreenerDateString = c.DatePS.toString()
						if ( ! pregnancyScreenerDateString ) {
							norcIncentive.pregnancyScreenerDate = null
						} else {
							DateTime dt = fmt.parseDateTime(pregnancyScreenerDateString)
							norcIncentive.pregnancyScreenerDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << "! Invalid DatePS: ${c.DatePS.toString()}\n"
						println "! parse DatePS Input: ${c.DatePS.toString()}"
						println "! parse DatePS Exception: ${e.toString()}"
					}
		
		
					norcIncentive.email = c.email.toString()
					norcIncentive.phoneNumber = c.phoneNumber.toString()
					try {
						norcIncentive.segmentId = Integer.parseInt(c.SegmentID.toString())
					} catch (Exception e) {
						response << "! Invalid SegmentID: ${c.SegmentID.toString()}\n"
					}
		
					// DONE SETTING VALUES, NOW WE SAVE.
		
					if (norcIncentive.hasErrors()) {
						response << "! consent has errors.\n"
					} else if (norcIncentive.save(flush:true)) {
						response << "= saved data\n"
					} else {
						response << "! unable to save data.\n"
		
						norcIncentive.errors.each{ e ->
							// println "norcIncentive:error::${e}"
							e.fieldErrors.each{ fe -> response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n" }
						}
					}
				}
			}
		}
	}

	def parseConsent(table, response) {
		//table?.CONSENT_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.CONSENT_BATCH1?.eachParallel { c ->
				NorcConsent.withTransaction {
	
					def mailingId = c.MailingID.toString()
					
					def norcConsent = NorcConsent.findByMailingId(mailingId)
					
					if (! norcConsent) {
						norcConsent = new NorcConsent()
						response <<  " + Creating new NorcConsent(${mailingId})\n"
					} else {
						response <<  " ~ Updating existing NorcConsent(${mailingId})\n"
					}
					
					
					norcConsent.firstName = c.fName.toString()
					norcConsent.lastName = c.lName.toString()
					norcConsent.address1 = c.Address1.toString()
					norcConsent.address2 = c.Address2.toString()
					norcConsent.addressUnit = c.Unit.toString()
					norcConsent.city = c.City.toString()
					norcConsent.state = c.State.toString()
					norcConsent.zipCode = c.Zip.toString()
					norcConsent.zipFour = c.Zip4.toString()
					norcConsent.norcSuId = c.SU_ID.toString()
					norcConsent.mailingId = c.MailingID.toString()
					norcConsent.email = c.email.toString()
					norcConsent.phoneNumber = c.phoneNumber.toString()
		
					// Read, parse, and lookup the docType
					def docTypeString = c.DocType.toString()
					try {
						if ( ! docTypeString ) {
							norcConsent.docType = null
						} else {
							def docType = NorcDocType.findByValue(Integer.parseInt(docTypeString))
							if (docType) {
								norcConsent.docType = docType
							} else {
								response <<  "! Unknown DocType: ${docTypeString}.  Please update FMTS.DOCTYPE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid DocType: ${docTypeString}"
					}
					
					// Read, parse, and lookup the Source
					def sourceString = c.Source.toString()
					try {
						if ( ! sourceString ) {
							norcConsent.source = null
						} else {
							def source = NorcSource.findByValue(Integer.parseInt(sourceString))
							if (source) {
								norcConsent.source = source
							} else {
								response <<  "! Unknown Source: ${sourceString}.  Please update FMTS.SOURCE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Source: ${sourceString}"
					}
					
					// Read, parse, and lookup the Status
					def statusString = c.Status.toString()
					try {
						if ( ! statusString ) {
							norcConsent.status = null
						} else {
							def status = NorcStatus.findByValue(Integer.parseInt(statusString))
							if (status) {
								norcConsent.status = status
							} else {
								response <<  "! Unknown Status: ${statusString}.  Please update FMTS.STATUS first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Status: ${statusString}\n"
						println "! parse Status Exception: ${e.toString()}"
					}
					
					// Read, parse, and lookup the Mode
					def modeString = c.Mode.toString()
					try {
						if ( ! modeString ) {
							norcConsent.mode = null
						} else {
							def mode = NorcDocMode.findByValue(Integer.parseInt(modeString))
							if (mode) {
								norcConsent.mode = mode
							} else {
								response <<  "! Unknown Mode: ${modeString}.  Please update FMTS.MODE first.\n"
							}
						}
					} catch (Exception e) {
						response << "! Invalid Mode: ${modeString}\n"
						println "! parse Mode Exception: ${e.toString()}"
					}
		
					// PS Date
					try {
						def pregnancyScreenerDateString = c.DatePS.toString()
						if ( ! pregnancyScreenerDateString ) {
							norcConsent.pregnancyScreenerDate = null
						} else {
							DateTime dt = fmt.parseDateTime(pregnancyScreenerDateString)
							norcConsent.pregnancyScreenerDate = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << "! Invalid DatePS: ${c.DatePS.toString()}\n"
						println "! parse DatePS Input: ${c.DatePS.toString()}"
						println "! parse DatePS Exception: ${e.toString()}"
					}
					
					// App Time & Date
					try {
						def appTimeString = c.AppTime.toString()
						if ( ! appTimeString ) {
							norcConsent.appTime = null
						} else {
							DateTime dt = fmt.parseDateTime(appTimeString)
							norcConsent.appTime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << "! Invalid AppTime: ${c.AppTime.toString()}\n"
						println "! parse AppTime Input: ${c.AppTime.toString()}"
						println "! parse AppTime Exception: ${e.toString()}"
					}
					
					try {
						norcConsent.segmentId = Integer.parseInt(c.SegmentID.toString())
					} catch (Exception e) {
						response << "! Invalid SegmentID: ${c.SegmentID.toString()}\n"
					}
					
					if (norcConsent.hasErrors()) {
						response << "! consent has errors.\n"
					} else if (norcConsent.save(flush:true)) {
						response << "= saved data\n"
					} else {
						response << "! unable to save data.\n"
						
						norcConsent.errors.each{ e ->
							// println "NorcConsent:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
							}							
						}		
					}
				}
			}
		}
	}

	def parseFormats(table, response) {

		table?.FMTS?.each{ f ->
			
			def fName = f.FMTNAME.toString()
			def fLabel = f.LABEL.toString()
			def fValue = f.START.toString()
			
			switch (fName) {
				case "DOCTYPE":
					def norcDocType = NorcDocType.findByValue(fValue)
					if ( ! norcDocType ) {
						norcDocType = new NorcDocType(name:fName, label:fLabel, value:fValue)
						norcDocType.save(flush:true)
						response << "Saved new NorcDocType: ${norcDocType.label}\n"
					} else {
						if (norcDocType.label != fLabel) {
							norcDocType.label = fLabel
							norcDocType.save(flush:true)
	
							response << "Updated NorcDocType(${norcDocType.value}) = ${norcDocType.name}\n"
						} else {
							response << "NorcDocType(${norcDocType.value}) already in the system.\n"
						}
					}
					break
				case "MODE":
					def norcDocMode = NorcDocMode.findByValue(fValue)
					if ( ! norcDocMode ) {
						norcDocMode = new NorcDocMode(name:fName, label:fLabel, value:fValue)
						norcDocMode.save(flush:true)
						response << "Saved new NorcDocType: ${norcDocMode.label}\n"
					} else {
						if (norcDocMode.label != fLabel) {
							norcDocMode.label = fLabel
							norcDocMode.save(flush:true)
	
							response << "Updated NorcDocMode(${norcDocMode.value}) = ${norcDocMode.name}\n"
						} else {
							response << "NorcDocMode(${norcDocMode.value}) already in the system.\n"
						}
					}
					break
				case "SOURCE":
					def norcSource = NorcSource.findByValue(fValue)
					if ( ! norcSource ) {
						norcSource = new NorcSource(name:fName, label:fLabel, value:fValue)
						norcSource.save(flush:true)
						response << "Saved new NorcSource: ${norcSource.label}\n"
					} else {
						if (norcSource.label != fLabel) {
							norcSource.label = fLabel
							norcSource.save(flush:true)
	
							response << "Updated NorcSource(${norcSource.value}) = ${norcSource.name}\n"
						} else {
							response << "NorcSource(${norcSource.value}) already in the system.\n"
						}
					}
					break
				case "STATUS":
					def norcStatus = NorcStatus.findByValue(fValue)
					if ( ! norcStatus ) {
						norcStatus = new NorcStatus(name:fName, label:fLabel, value:fValue)
						norcStatus.save(flush:true)
						response << "Saved new NorcStatus: ${norcStatus.label}\n"
					} else {
						if (norcStatus.label != fLabel) {
							norcStatus.label = fLabel
							norcStatus.save(flush:true)
	
							response << "Updated NorcStatus(${norcStatus.value}) = ${norcStatus.name}\n"
						} else {
							response << "NorcStatus(${norcStatus.value}) already in the system.\n"
						}
					}
					break
				default:
					response << "Unknown FMTS: ${fName}\n"
			}
		}
	}
	
	def parseBirthBatch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing BIRTH_BATCH1"
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.HH_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.BIRTH_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcBirthBatch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcBirthBatch = NorcBirthBatch.findByRespondentSerial(checkSerial)
		
					if (!norcBirthBatch) {
						norcBirthBatch = new NorcBirthBatch()
						response <<  " + Creating new NorcBirthBatch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcBirthBatch.lock()
						response <<  " ~ Updating existing NorcBirthBatch(${checkSerial})\n"
					}

					norcBirthBatch.respondentSerial = c.Respondent_Serial.toString()
					norcBirthBatch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcBirthBatch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcBirthBatch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcBirthBatch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcBirthBatch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcBirthBatch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcBirthBatch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcBirthBatch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcBirthBatch.respondentId = c.Respondent_ID.toString()
					norcBirthBatch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcBirthBatch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcBirthBatch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcBirthBatch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcBirthBatch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcBirthBatch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcBirthBatch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcBirthBatch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcBirthBatch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcBirthBatch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcBirthBatch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcBirthBatch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcBirthBatch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcBirthBatch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcBirthBatch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcBirthBatch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcBirthBatch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcBirthBatch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcBirthBatch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcBirthBatch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcBirthBatch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcBirthBatch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcBirthBatch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcBirthBatch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcBirthBatch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcBirthBatch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcBirthBatch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcBirthBatch.datacleaningNote = c.DataCleaning_Note.toString()
					norcBirthBatch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcBirthBatch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcBirthBatch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcBirthBatch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcBirthBatch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcBirthBatch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcBirthBatch.prOwnHome = c.PR_OWN_HOME.toString()
					norcBirthBatch.suId = c.SU_ID.toString()
					norcBirthBatch.prFname = c.PR_FNAME.toString()
					norcBirthBatch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcBirthBatch.prLname = c.PR_LNAME.toString()
					norcBirthBatch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcBirthBatch.dobMo = c.DOB_MO.toString()
					norcBirthBatch.dobMoCodes = c.DOB_MO_Codes.toString()
					norcBirthBatch.dobDy = c.DOB_DY.toString()
					norcBirthBatch.dobDyCodes = c.DOB_DY_Codes.toString()
					norcBirthBatch.dobYr = c.DOB_YR.toString()
					norcBirthBatch.dobYrCodes = c.DOB_YR_Codes.toString()
					norcBirthBatch.agecalc = c.AGECALC.toString()
					norcBirthBatch.ageRange = c.AGE_RANGE.toString()
					try {
						dateTimeString = c.TIME_STAMP_1A.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp1a = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp1a = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1A: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Input: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Exception: ${e.toString()}'
					}
					norcBirthBatch.quexlang = c.QUEXLANG.toString()
					norcBirthBatch.birthvisitLocation = c.BIRTHVISIT_LOCATION.toString()
					norcBirthBatch.visWhoConsented = c.VIS_WHO_CONSENTED.toString()
					norcBirthBatch.visConsentType1 = c.VIS_CONSENT_TYPE_1.toString()
					norcBirthBatch.visConsentType2 = c.VIS_CONSENT_TYPE_2.toString()
					norcBirthBatch.visConsentResponse1 = c.VIS_CONSENT_RESPONSE_1.toString()
					norcBirthBatch.visConsentResponse2 = c.VIS_CONSENT_RESPONSE_2.toString()
					norcBirthBatch.visConsentType = c.VIS_CONSENT_TYPE.toString()
					norcBirthBatch.visConsentResponse = c.VIS_CONSENT_RESPONSE.toString()
					norcBirthBatch.visComments = c.VIS_COMMENTS.toString()
					norcBirthBatch.visCommentsCodes = c.VIS_COMMENTS_Codes.toString()
					norcBirthBatch.english = c.ENGLISH.toString()
					norcBirthBatch.contactLang = c.CONTACT_LANG.toString()
					norcBirthBatch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcBirthBatch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcBirthBatch.interpret = c.INTERPRET.toString()
					norcBirthBatch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcBirthBatch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_1B.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp1b = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp1b = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1B: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Input: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcBirthBatch.currmo = c.CURRMO.toString()
					norcBirthBatch.currdy = c.CURRDY.toString()
					norcBirthBatch.curryr = c.CURRYR.toString()
					norcBirthBatch.age = c.AGE.toString()
					norcBirthBatch.multiple = c.MULTIPLE.toString()
					norcBirthBatch.multipleNum = c.MULTIPLE_NUM.toString()
					norcBirthBatch.childDobMonth = c.CHILD_DOB_MONTH.toString()
					norcBirthBatch.childDobMonthCodes = c.CHILD_DOB_MONTH_Codes.toString()
					norcBirthBatch.childDobDay = c.CHILD_DOB_DAY.toString()
					norcBirthBatch.childDobDayCodes = c.CHILD_DOB_DAY_Codes.toString()
					norcBirthBatch.childDobYear = c.CHILD_DOB_YEAR.toString()
					norcBirthBatch.childDobYearCodes = c.CHILD_DOB_YEAR_Codes.toString()
					norcBirthBatch.babytxt01 = c.babytxt_01.toString()
					norcBirthBatch.babytxt02 = c.babytxt_02.toString()
					norcBirthBatch.multi01 = c.multi_01.toString()
					norcBirthBatch.multi02 = c.multi_02.toString()
					norcBirthBatch.multi03 = c.multi_03.toString()
					norcBirthBatch.babyloopFirstBabyName = c.BabyLoop_first_BABY_NAME.toString()
					norcBirthBatch.babyloopSecondBabyName = c.BabyLoop_second_BABY_NAME.toString()
					norcBirthBatch.babyloopThirdBabyName = c.BabyLoop_third_BABY_NAME.toString()
					norcBirthBatch.babyloopFourthBabyName = c.BabyLoop_fourth_BABY_NAME.toString()
					norcBirthBatch.babyloopFifthBabyName = c.BabyLoop_fifth_BABY_NAME.toString()
					norcBirthBatch.babyloopSixthBabyName = c.BabyLoop_sixth_BABY_NAME.toString()
					norcBirthBatch.babyloopSeventhBabyName = c.BabyLoop_seventh_BABY_NAME.toString()
					norcBirthBatch.babyloopEighthBabyName = c.BabyLoop_eighth_BABY_NAME.toString()
					norcBirthBatch.babyAbyName2BabyFname8 = c.Baby_ABY_NAME2__BABY_FNAME8.toString()
					norcBirthBatch.babyByName2BabyFname9 = c.Baby_BY_NAME2_BABY_FNAME9.toString()
					norcBirthBatch.babyAbyName2BabyFname10 = c.Baby_ABY_NAME2__BABY_FNAME10.toString()
					norcBirthBatch.babyAbyName2BabyFname11 = c.Baby_ABY_NAME2_BABY_FNAME11.toString()
					norcBirthBatch.babyAbyName2BabyFname12 = c.Baby_ABY_NAME2__BABY_FNAME12.toString()
					norcBirthBatch.babyAbyName2BabyFname13 = c.Baby_ABY_NAME2__BABY_FNAME13.toString()
					norcBirthBatch.babyByName2BabyFname14 = c.Baby_BY_NAME2_BABY_FNAME14.toString()
					norcBirthBatch.babyAbyName2BabyFname15 = c.Baby_ABY_NAME2_BABY_FNAME15.toString()
					norcBirthBatch.babyName2BabfnameCds16 = c.Baby__NAME2_BABFNAME_Cds16.toString()
					norcBirthBatch.babyName2BabyameCds17 = c.Baby_NAME2_BABYAME_Cds17.toString()
					norcBirthBatch.babyName2BabfnameCds18 = c.Baby__NAME2_BABFNAME_Cds18.toString()
					norcBirthBatch.babyName2BabnameCds19 = c.Baby__NAME2_BABNAME_Cds19.toString()
					norcBirthBatch.babyName2BabfnameCds20 = c.Baby__NAME2_BABFNAME_Cds20.toString()
					norcBirthBatch.babyName2BabfnameCds21 = c.Baby__NAME2_BABFNAME_Cds21.toString()
					norcBirthBatch.babyName2BabyameCds22 = c.Baby_NAME2_BABYAME_Cds22.toString()
					norcBirthBatch.babyName2BabnameCds23 = c.Baby__NAME2_BABNAME_Cds23.toString()
					norcBirthBatch.babyAbyName2BabyMname24 = c.Baby_ABY_NAME2__BABY_MNAME24.toString()
					norcBirthBatch.babyByName2BabyMname25 = c.Baby_BY_NAME2_BABY_MNAME25.toString()
					norcBirthBatch.babyAbyName2BabyMname26 = c.Baby_ABY_NAME2__BABY_MNAME26.toString()
					norcBirthBatch.babyAbyName2BabyMname27 = c.Baby_ABY_NAME2_BABY_MNAME27.toString()
					norcBirthBatch.babyAbyName2BabyMname28 = c.Baby_ABY_NAME2__BABY_MNAME28.toString()
					norcBirthBatch.babyAbyName2BabyMname29 = c.Baby_ABY_NAME2__BABY_MNAME29.toString()
					norcBirthBatch.babyByName2BabyMname30 = c.Baby_BY_NAME2_BABY_MNAME30.toString()
					norcBirthBatch.babyAbyName2BabyMname31 = c.Baby_ABY_NAME2_BABY_MNAME31.toString()
					norcBirthBatch.babyName2BabmnameCds32 = c.Baby__NAME2_BABMNAME_Cds32.toString()
					norcBirthBatch.babyName2BabyameCds33 = c.Baby_NAME2_BABYAME_Cds33.toString()
					norcBirthBatch.babyName2BabmnameCds34 = c.Baby__NAME2_BABMNAME_Cds34.toString()
					norcBirthBatch.babyName2BabnameCds35 = c.Baby__NAME2_BABNAME_Cds35.toString()
					norcBirthBatch.babyName2BabmnameCds36 = c.Baby__NAME2_BABMNAME_Cds36.toString()
					norcBirthBatch.babyName2BabmnameCds37 = c.Baby__NAME2_BABMNAME_Cds37.toString()
					norcBirthBatch.babyName2BabyameCds38 = c.Baby_NAME2_BABYAME_Cds38.toString()
					norcBirthBatch.babyName2BabnameCds39 = c.Baby__NAME2_BABNAME_Cds39.toString()
					norcBirthBatch.babyAbyName2BabyName40 = c.Baby_ABY_NAME2__BABY_NAME40.toString()
					norcBirthBatch.babyByName2BabyName41 = c.Baby_BY_NAME2_BABY_NAME41.toString()
					norcBirthBatch.babyAbyName2BabyName42 = c.Baby_ABY_NAME2__BABY_NAME42.toString()
					norcBirthBatch.babyAbyName2BabyName43 = c.Baby_ABY_NAME2_BABY_NAME43.toString()
					norcBirthBatch.babyAbyName2BabyName44 = c.Baby_ABY_NAME2__BABY_NAME44.toString()
					norcBirthBatch.babyAbyName2BabyName45 = c.Baby_ABY_NAME2__BABY_NAME45.toString()
					norcBirthBatch.babyByName2BabyName46 = c.Baby_BY_NAME2_BABY_NAME46.toString()
					norcBirthBatch.babyAbyName2BabyName47 = c.Baby_ABY_NAME2_BABY_NAME47.toString()
					norcBirthBatch.babyName2BabnameCds48 = c.Baby__NAME2_BABNAME_Cds48.toString()
					norcBirthBatch.babyName2BabymeCds49 = c.Baby_NAME2_BABYME_Cds49.toString()
					norcBirthBatch.babyName2BabnameCds50 = c.Baby__NAME2_BABNAME_Cds50.toString()
					norcBirthBatch.babyName2BabameCds51 = c.Baby__NAME2_BABAME_Cds51.toString()
					norcBirthBatch.babyName2BabnameCds52 = c.Baby__NAME2_BABNAME_Cds52.toString()
					norcBirthBatch.babyName2BabnameCds53 = c.Baby__NAME2_BABNAME_Cds53.toString()
					norcBirthBatch.babyName2BabymeCds54 = c.Baby_NAME2_BABYME_Cds54.toString()
					norcBirthBatch.babyName2BabameCds55 = c.Baby__NAME2_BABAME_Cds55.toString()
					norcBirthBatch.babyloopFirstBabySex = c.BabyLoop_first_BABY_SEX.toString()
					norcBirthBatch.babyloopSecondBabySex = c.BabyLoop_second_BABY_SEX.toString()
					norcBirthBatch.babyloopThirdBabySex = c.BabyLoop_third_BABY_SEX.toString()
					norcBirthBatch.babyloopFourthBabySex = c.BabyLoop_fourth_BABY_SEX.toString()
					norcBirthBatch.babyloopFifthBabySex = c.BabyLoop_fifth_BABY_SEX.toString()
					norcBirthBatch.babyloopSixthBabySex = c.BabyLoop_sixth_BABY_SEX.toString()
					norcBirthBatch.babyloopSeventhBabySex = c.BabyLoop_seventh_BABY_SEX.toString()
					norcBirthBatch.babyloopEighthBabySex = c.BabyLoop_eighth_BABY_SEX.toString()
					norcBirthBatch.babyloopFirstBabygend = c.BabyLoop_first_babygend.toString()
					norcBirthBatch.babyloopSecondBabygend = c.BabyLoop_second_babygend.toString()
					norcBirthBatch.babyloopThirdBabygend = c.BabyLoop_third_babygend.toString()
					norcBirthBatch.babyloopFourthBabygend = c.BabyLoop_fourth_babygend.toString()
					norcBirthBatch.babyloopFifthBabygend = c.BabyLoop_fifth_babygend.toString()
					norcBirthBatch.babyloopSixthBabygend = c.BabyLoop_sixth_babygend.toString()
					norcBirthBatch.babyloopSeventhBabygend = c.BabyLoop_seventh_babygend.toString()
					norcBirthBatch.babyloopEighthBabygend = c.BabyLoop_eighth_babygend.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtBs56 = c.Baby_ABY_BWT_BAABY_BWT_BS56.toString()
					norcBirthBatch.babyByBwtBabyBwtBs57 = c.Baby_BY_BWT_BABY_BWT_BS57.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtBs58 = c.Baby_ABY_BWT_BAABY_BWT_BS58.toString()
					norcBirthBatch.babyAbyBwtBabyBwtBs59 = c.Baby_ABY_BWT_BABY_BWT_BS59.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtBs60 = c.Baby_ABY_BWT_BAABY_BWT_BS60.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtBs61 = c.Baby_ABY_BWT_BAABY_BWT_BS61.toString()
					norcBirthBatch.babyByBwtBabyBwtBs62 = c.Baby_BY_BWT_BABY_BWT_BS62.toString()
					norcBirthBatch.babyAbyBwtBabyBwtBs63 = c.Baby_ABY_BWT_BABY_BWT_BS63.toString()
					norcBirthBatch.babyBwtBabyTBsCds64 = c.Baby__BWT_BABY_T_BS_Cds64.toString()
					norcBirthBatch.babyBwtBabyBbsCds65 = c.Baby_BWT_BABY_BBS_Cds65.toString()
					norcBirthBatch.babyBwtBabyTBsCds66 = c.Baby__BWT_BABY_T_BS_Cds66.toString()
					norcBirthBatch.babyBwtBabyBsCds67 = c.Baby__BWT_BABY__BS_Cds67.toString()
					norcBirthBatch.babyBwtBabyTBsCds68 = c.Baby__BWT_BABY_T_BS_Cds68.toString()
					norcBirthBatch.babyBwtBabyTBsCds69 = c.Baby__BWT_BABY_T_BS_Cds69.toString()
					norcBirthBatch.babyBwtBabyBbsCds70 = c.Baby_BWT_BABY_BBS_Cds70.toString()
					norcBirthBatch.babyBwtBabyBsCds71 = c.Baby__BWT_BABY__BS_Cds71.toString()
					norcBirthBatch.babyBabyBwtBbabyBwtOz72 = c.Baby_BABY_BWT_BBABY_BWT_OZ72.toString()
					norcBirthBatch.babyByBwtBabbyBwtOz73 = c.Baby_BY_BWT_BABBY_BWT_OZ73.toString()
					norcBirthBatch.babyBabyBwtBbabyBwtOz74 = c.Baby_BABY_BWT_BBABY_BWT_OZ74.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtOz75 = c.Baby_ABY_BWT_BAABY_BWT_OZ75.toString()
					norcBirthBatch.babyBabyBwtBbabyBwtOz76 = c.Baby_BABY_BWT_BBABY_BWT_OZ76.toString()
					norcBirthBatch.babyBabyBwtBbabyBwtOz77 = c.Baby_BABY_BWT_BBABY_BWT_OZ77.toString()
					norcBirthBatch.babyAbyBwtBabyBwtOz78 = c.Baby_ABY_BWT_BABY_BWT_OZ78.toString()
					norcBirthBatch.babyAbyBwtBaabyBwtOz79 = c.Baby_ABY_BWT_BAABY_BWT_OZ79.toString()
					norcBirthBatch.babyYBwtBabywtOzCds80 = c.Baby_Y_BWT_BABYWT_OZ_Cds80.toString()
					norcBirthBatch.babyBwtBabyBOzCds81 = c.Baby_BWT_BABY_B_OZ_Cds81.toString()
					norcBirthBatch.babyYBwtBabywtOzCds82 = c.Baby_Y_BWT_BABYWT_OZ_Cds82.toString()
					norcBirthBatch.babyBwtBabyTOzCds83 = c.Baby__BWT_BABY_T_OZ_Cds83.toString()
					norcBirthBatch.babyYBwtBabywtOzCds84 = c.Baby_Y_BWT_BABYWT_OZ_Cds84.toString()
					norcBirthBatch.babyYBwtBabywtOzCds85 = c.Baby_Y_BWT_BABYWT_OZ_Cds85.toString()
					norcBirthBatch.babyBwtBabyOzCds86 = c.Baby__BWT_BABY__OZ_Cds86.toString()
					norcBirthBatch.babyBwtBabyTOzCds87 = c.Baby__BWT_BABY_T_OZ_Cds87.toString()
					norcBirthBatch.babytxt201 = c.babytxt2_01.toString()
					norcBirthBatch.babytxt202 = c.babytxt2_02.toString()
					norcBirthBatch.babytxt203 = c.babytxt2_03.toString()
					norcBirthBatch.babytxt204 = c.babytxt2_04.toString()
					norcBirthBatch.babygend = c.babygend.toString()
					norcBirthBatch.babygend2 = c.babygend2.toString()
					norcBirthBatch.liveMom = c.LIVE_MOM.toString()
					norcBirthBatch.liveOth = c.LIVE_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcBirthBatch.recentMove = c.RECENT_MOVE.toString()
					norcBirthBatch.ownHome = c.OWN_HOME.toString()
					norcBirthBatch.ownHomeOth = c.OWN_HOME_OTH.toString()
					norcBirthBatch.ownHomeOthCodes = c.OWN_HOME_OTH_Codes.toString()
					norcBirthBatch.ageHome = c.AGE_HOME.toString()
					norcBirthBatch.lengthReside = c.LENGTH_RESIDE.toString()
					norcBirthBatch.lengthResideCodes = c.LENGTH_RESIDE_Codes.toString()
					norcBirthBatch.lengthResideUnit = c.LENGTH_RESIDE_UNIT.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcBirthBatch.renovate = c.RENOVATE.toString()
					norcBirthBatch.renovateRoom01 = c.RENOVATE_ROOM_01.toString()
					norcBirthBatch.renovateRoom02 = c.RENOVATE_ROOM_02.toString()
					norcBirthBatch.renovateRoom03 = c.RENOVATE_ROOM_03.toString()
					norcBirthBatch.renovateRoom04 = c.RENOVATE_ROOM_04.toString()
					norcBirthBatch.renovateRoom05 = c.RENOVATE_ROOM_05.toString()
					norcBirthBatch.renovateRoom06 = c.RENOVATE_ROOM_06.toString()
					norcBirthBatch.renovateRoom07 = c.RENOVATE_ROOM_07.toString()
					norcBirthBatch.renovateRoom08 = c.RENOVATE_ROOM_08.toString()
					norcBirthBatch.renovateRoom09 = c.RENOVATE_ROOM_09.toString()
					norcBirthBatch.renovateRoom10 = c.RENOVATE_ROOM_10.toString()
					norcBirthBatch.renovateRoomOth = c.RENOVATE_ROOM_OTH.toString()
					norcBirthBatch.renovateRoomOthCodes = c.RENOVATE_ROOM_OTH_Codes.toString()
					norcBirthBatch.decorate = c.DECORATE.toString()
					norcBirthBatch.decorateRoom01 = c.DECORATE_ROOM_01.toString()
					norcBirthBatch.decorateRoom02 = c.DECORATE_ROOM_02.toString()
					norcBirthBatch.decorateRoom03 = c.DECORATE_ROOM_03.toString()
					norcBirthBatch.decorateRoom04 = c.DECORATE_ROOM_04.toString()
					norcBirthBatch.decorateRoom05 = c.DECORATE_ROOM_05.toString()
					norcBirthBatch.decorateRoom06 = c.DECORATE_ROOM_06.toString()
					norcBirthBatch.decorateRoom07 = c.DECORATE_ROOM_07.toString()
					norcBirthBatch.decorateRoom08 = c.DECORATE_ROOM_08.toString()
					norcBirthBatch.decorateRoom09 = c.DECORATE_ROOM_09.toString()
					norcBirthBatch.decorateRoom10 = c.DECORATE_ROOM_10.toString()
					norcBirthBatch.decorateRoomOth = c.DECORATE_ROOM_OTH.toString()
					norcBirthBatch.decorateRoomOthCodes = c.DECORATE_ROOM_OTH_Codes.toString()
					norcBirthBatch.smoke = c.SMOKE.toString()
					norcBirthBatch.smokeLocate = c.SMOKE_LOCATE.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcBirthBatch.babytxt301 = c.babytxt3_01.toString()
					norcBirthBatch.babytxt302 = c.babytxt3_02.toString()
					norcBirthBatch.fedBaby = c.FED_BABY.toString()
					norcBirthBatch.howFed = c.HOW_FED.toString()
					norcBirthBatch.planfeedtxt = c.planfeedtxt.toString()
					norcBirthBatch.planFeed = c.PLAN_FEED.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcBirthBatch.postxt = c.postxt.toString()
					norcBirthBatch.babygend3 = c.babygend3.toString()
					norcBirthBatch.posHosp = c.POS_HOSP.toString()
					norcBirthBatch.postxt2 = c.postxt2.toString()
					norcBirthBatch.posHome = c.POS_HOME.toString()
					norcBirthBatch.sleeptxt = c.sleeptxt.toString()
					norcBirthBatch.sleeptxt2 = c.sleeptxt2.toString()
					norcBirthBatch.sleepRoom = c.SLEEP_ROOM.toString()
					norcBirthBatch.bed = c.BED.toString()
					norcBirthBatch.bedOth = c.BED_OTH.toString()
					norcBirthBatch.bedOthCodes = c.BED_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcBirthBatch.hcare = c.HCARE.toString()
					norcBirthBatch.hcareOth = c.HCARE_OTH.toString()
					norcBirthBatch.hcareOthCodes = c.HCARE_OTH_Codes.toString()
					norcBirthBatch.vaccine = c.VACCINE.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcBirthBatch.employ2 = c.EMPLOY2.toString()
					norcBirthBatch.returnJobReturnJobNum = c.RETURN_JOB_RETURN_JOB_NUM.toString()
					norcBirthBatch.returbRetjobNumCds88 = c.RETURB_RETJOB_NUM_Cds88.toString()
					norcBirthBatch.returnJobReturnJobUnit = c.RETURN_JOB_RETURN_JOB_UNIT.toString()
					norcBirthBatch.babytxt401 = c.babytxt4_01.toString()
					norcBirthBatch.babytxt402 = c.babytxt4_02.toString()
					norcBirthBatch.childcare = c.CHILDCARE.toString()
					norcBirthBatch.ccareType = c.CCARE_TYPE.toString()
					norcBirthBatch.ccareTypeOth = c.CCARE_TYPE_OTH.toString()
					norcBirthBatch.ccareTypeOthCodes = c.CCARE_TYPE_OTH_Codes.toString()
					norcBirthBatch.ccareWho = c.CCARE_WHO.toString()
					norcBirthBatch.relCareOth = c.REL_CARE_OTH.toString()
					norcBirthBatch.relCareOthCodes = c.REL_CARE_OTH_Codes.toString()
					norcBirthBatch.ccareWhoOth = c.CCARE_WHO_OTH.toString()
					norcBirthBatch.ccareWhoOthCodes = c.CCARE_WHO_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcBirthBatch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcBirthBatch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcBirthBatch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcBirthBatch.phoneNbr = c.PHONE_NBR.toString()
					norcBirthBatch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					norcBirthBatch.phoneType = c.PHONE_TYPE.toString()
					norcBirthBatch.phoneTypeOth = c.PHONE_TYPE_OTH.toString()
					norcBirthBatch.phoneTypeOthCodes = c.PHONE_TYPE_OTH_Codes.toString()
					norcBirthBatch.friendPhoneOth = c.FRIEND_PHONE_OTH.toString()
					norcBirthBatch.friendPhoneOthCodes = c.FRIEND_PHONE_OTH_Codes.toString()
					norcBirthBatch.homePhone = c.HOME_PHONE.toString()
					norcBirthBatch.homePhoneCodes = c.HOME_PHONE_Codes.toString()
					norcBirthBatch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcBirthBatch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcBirthBatch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcBirthBatch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcBirthBatch.cellPhone = c.CELL_PHONE.toString()
					norcBirthBatch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcBirthBatch.recmoveInfo = c.RECMOVE_INFO.toString()
					norcBirthBatch.recaddrRecAddress1 = c.RECADDR_REC_ADDRESS1.toString()
					norcBirthBatch.recaddrRecAddress1Codes = c.RECADDR_REC_ADDRESS1_Codes.toString()
					norcBirthBatch.recaddrRecAddress2 = c.RECADDR_REC_ADDRESS2.toString()
					norcBirthBatch.recaddrRecAddress2Codes = c.RECADDR_REC_ADDRESS2_Codes.toString()
					norcBirthBatch.recaddrRecUnit = c.RECADDR_REC_UNIT.toString()
					norcBirthBatch.recaddrRecUnitCodes = c.RECADDR_REC_UNIT_Codes.toString()
					norcBirthBatch.recaddrRecCity = c.RECADDR_REC_CITY.toString()
					norcBirthBatch.recaddrRecCityCodes = c.RECADDR_REC_CITY_Codes.toString()
					norcBirthBatch.recaddrRecState = c.RECADDR_REC_STATE.toString()
					norcBirthBatch.recaddrRecZip = c.RECADDR_REC_ZIP.toString()
					norcBirthBatch.recaddrRecZipCodes = c.RECADDR_REC_ZIP_Codes.toString()
					norcBirthBatch.recaddrRecZip4 = c.RECADDR_REC_ZIP4.toString()
					norcBirthBatch.recaddrRecZip4Codes = c.RECADDR_REC_ZIP4_Codes.toString()
					norcBirthBatch.sameAddr = c.SAME_ADDR.toString()
					norcBirthBatch.mailaddrMailAddress1 = c.MAILADDR_MAIL_ADDRESS1.toString()
					norcBirthBatch.maiadmaiAress1Cds89 = c.MAIADMAI_ARESS1_Cds89.toString()
					norcBirthBatch.mailaddrMailAddress2 = c.MAILADDR_MAIL_ADDRESS2.toString()
					norcBirthBatch.maiadmaiAress2Cds90 = c.MAIADMAI_ARESS2_Cds90.toString()
					norcBirthBatch.mailaddrMailUnit = c.MAILADDR_MAIL_UNIT.toString()
					norcBirthBatch.mailaddrMailUnitCodes = c.MAILADDR_MAIL_UNIT_Codes.toString()
					norcBirthBatch.mailaddrMailCity = c.MAILADDR_MAIL_CITY.toString()
					norcBirthBatch.mailaddrMailCityCodes = c.MAILADDR_MAIL_CITY_Codes.toString()
					norcBirthBatch.mailaddrMailState = c.MAILADDR_MAIL_STATE.toString()
					norcBirthBatch.mailaddrMailZip = c.MAILADDR_MAIL_ZIP.toString()
					norcBirthBatch.mailaddrMailZipCodes = c.MAILADDR_MAIL_ZIP_Codes.toString()
					norcBirthBatch.mailaddrMailZip4 = c.MAILADDR_MAIL_ZIP4.toString()
					norcBirthBatch.mailaddrMailZip4Codes = c.MAILADDR_MAIL_ZIP4_Codes.toString()
					norcBirthBatch.haveEmail = c.HAVE_EMAIL.toString()
					norcBirthBatch.email = c.EMAIL.toString()
					norcBirthBatch.emailCodes = c.EMAIL_Codes.toString()
					norcBirthBatch.emailType = c.EMAIL_TYPE.toString()
					norcBirthBatch.emailShare = c.EMAIL_SHARE.toString()
					norcBirthBatch.planMove = c.PLAN_MOVE.toString()
					norcBirthBatch.whereMove = c.WHERE_MOVE.toString()
					norcBirthBatch.moveInfo = c.MOVE_INFO.toString()
					norcBirthBatch.newaddrNewAddress1 = c.NEWADDR_NEW_ADDRESS1.toString()
					norcBirthBatch.newaddrNewAddress1Codes = c.NEWADDR_NEW_ADDRESS1_Codes.toString()
					norcBirthBatch.newaddrNewAddress2 = c.NEWADDR_NEW_ADDRESS2.toString()
					norcBirthBatch.newaddrNewAddress2Codes = c.NEWADDR_NEW_ADDRESS2_Codes.toString()
					norcBirthBatch.newaddrNewUnit = c.NEWADDR_NEW_UNIT.toString()
					norcBirthBatch.newaddrNewUnitCodes = c.NEWADDR_NEW_UNIT_Codes.toString()
					norcBirthBatch.newaddrNewCity = c.NEWADDR_NEW_CITY.toString()
					norcBirthBatch.newaddrNewCityCodes = c.NEWADDR_NEW_CITY_Codes.toString()
					norcBirthBatch.newaddrNewState = c.NEWADDR_NEW_STATE.toString()
					norcBirthBatch.newaddrNewZip = c.NEWADDR_NEW_ZIP.toString()
					norcBirthBatch.newaddrNewZipCodes = c.NEWADDR_NEW_ZIP_Codes.toString()
					norcBirthBatch.newaddrNewZip4 = c.NEWADDR_NEW_ZIP4.toString()
					norcBirthBatch.newaddrNewZip4Codes = c.NEWADDR_NEW_ZIP4_Codes.toString()
					norcBirthBatch.whenMove = c.WHEN_MOVE.toString()
					norcBirthBatch.dateMoveMonth = c.DATE_MOVE_MONTH.toString()
					norcBirthBatch.dateMoveMonthCodes = c.DATE_MOVE_MONTH_Codes.toString()
					norcBirthBatch.dateMoveYear = c.DATE_MOVE_YEAR.toString()
					norcBirthBatch.dateMoveYearCodes = c.DATE_MOVE_YEAR_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_10.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp10 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp10 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_10: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Input: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					norcBirthBatch.respndnt = c.RESPNDNT.toString()
					norcBirthBatch.contactType = c.CONTACT_TYPE.toString()
					norcBirthBatch.contactLocation = c.CONTACT_LOCATION.toString()
					norcBirthBatch.contactLocationOth = c.CONTACT_LOCATION_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcBirthBatch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcBirthBatch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcBirthBatch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcBirthBatch.hasErrors()) {
						response << "! norcBirthBatch has errors.\n"
					} else if (norcBirthBatch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcBirthBatch record with Respondent Serial ${norcBirthBatch.suId}"
						norcBirthBatch.errors.each{ e ->
							// println "norcBirthBatch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcBirthBatch in ${diff} seconds"
		}
		// end BIRTH_BATCH
	}

	def parsePpv1Batch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing PPV1_BATCH1"
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.HH_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.PPV1_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcPpv1Batch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcPpv1Batch = NorcPpv1Batch.findByRespondentSerial(checkSerial)
		
					if (!norcPpv1Batch) {
						norcPpv1Batch = new NorcPpv1Batch()
						response <<  " + Creating new NorcPpv1Batch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcPpv1Batch.lock()
						response <<  " ~ Updating existing NorcPpv1Batch(${checkSerial})\n"
					}

					norcPpv1Batch.respondentSerial = c.Respondent_Serial.toString()
					norcPpv1Batch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcPpv1Batch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcPpv1Batch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcPpv1Batch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcPpv1Batch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcPpv1Batch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcPpv1Batch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcPpv1Batch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcPpv1Batch.respondentId = c.Respondent_ID.toString()
					norcPpv1Batch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcPpv1Batch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcPpv1Batch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcPpv1Batch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcPpv1Batch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcPpv1Batch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcPpv1Batch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcPpv1Batch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcPpv1Batch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcPpv1Batch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcPpv1Batch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcPpv1Batch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcPpv1Batch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcPpv1Batch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcPpv1Batch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcPpv1Batch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcPpv1Batch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcPpv1Batch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcPpv1Batch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcPpv1Batch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcPpv1Batch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcPpv1Batch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcPpv1Batch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcPpv1Batch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcPpv1Batch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcPpv1Batch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcPpv1Batch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcPpv1Batch.datacleaningNote = c.DataCleaning_Note.toString()
					norcPpv1Batch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcPpv1Batch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcPpv1Batch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcPpv1Batch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcPpv1Batch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcPpv1Batch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcPpv1Batch.suId = c.SU_ID.toString()
					norcPpv1Batch.affiliate = c.AFFILIATE.toString()
					norcPpv1Batch.majority = c.MAJORITY.toString()
					norcPpv1Batch.prFname = c.PR_FNAME.toString()
					norcPpv1Batch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcPpv1Batch.prLname = c.PR_LNAME.toString()
					norcPpv1Batch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcPpv1Batch.method = c.METHOD.toString()
					norcPpv1Batch.dobMo = c.DOB_MO.toString()
					norcPpv1Batch.dobDy = c.DOB_DY.toString()
					norcPpv1Batch.dobYr = c.DOB_YR.toString()
					norcPpv1Batch.agecalc = c.AGECALC.toString()
					try {
						dateTimeString = c.TIME_STAMP_1A.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp1a = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp1a = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1A: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Input: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Exception: ${e.toString()}'
					}
					norcPpv1Batch.quexlang = c.QUEXLANG.toString()
					norcPpv1Batch.sameday = c.SAMEDAY.toString()
					norcPpv1Batch.visWhoConsented = c.VIS_WHO_CONSENTED.toString()
					norcPpv1Batch.visConsentType1 = c.VIS_CONSENT_TYPE_1.toString()
					norcPpv1Batch.visConsentType2 = c.VIS_CONSENT_TYPE_2.toString()
					norcPpv1Batch.visConsentResponse1 = c.VIS_CONSENT_RESPONSE_1.toString()
					norcPpv1Batch.visConsentResponse2 = c.VIS_CONSENT_RESPONSE_2.toString()
					norcPpv1Batch.visComments = c.VIS_COMMENTS.toString()
					norcPpv1Batch.visCommentsCodes = c.VIS_COMMENTS_Codes.toString()
					norcPpv1Batch.english = c.ENGLISH.toString()
					norcPpv1Batch.contactLang = c.CONTACT_LANG.toString()
					norcPpv1Batch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcPpv1Batch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcPpv1Batch.interpret = c.INTERPRET.toString()
					norcPpv1Batch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcPpv1Batch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_1B.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp1b = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp1b = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1B: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Input: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcPpv1Batch.nameConfirm = c.NAME_CONFIRM.toString()
					norcPpv1Batch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcPpv1Batch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcPpv1Batch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcPpv1Batch.currmo = c.CURRMO.toString()
					norcPpv1Batch.currdy = c.CURRDY.toString()
					norcPpv1Batch.curryr = c.CURRYR.toString()
					norcPpv1Batch.dobConfirm = c.DOB_CONFIRM.toString()
					norcPpv1Batch.age = c.AGE.toString()
					norcPpv1Batch.personDobMonth = c.PERSON_DOB_MONTH.toString()
					norcPpv1Batch.personDobMonthCodes = c.PERSON_DOB_MONTH_Codes.toString()
					norcPpv1Batch.personDobDay = c.PERSON_DOB_DAY.toString()
					norcPpv1Batch.personDobDayCodes = c.PERSON_DOB_DAY_Codes.toString()
					norcPpv1Batch.personDobYear = c.PERSON_DOB_YEAR.toString()
					norcPpv1Batch.personDobYearCodes = c.PERSON_DOB_YEAR_Codes.toString()
					norcPpv1Batch.ageElig = c.AGE_ELIG.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcPpv1Batch.health = c.HEALTH.toString()
					norcPpv1Batch.everPreg = c.EVER_PREG.toString()
					norcPpv1Batch.asthma = c.ASTHMA.toString()
					norcPpv1Batch.highbp = c.HIGHBP.toString()
					norcPpv1Batch.diabtext = c.diabtext.toString()
					norcPpv1Batch.diabetes1 = c.DIABETES_1.toString()
					norcPpv1Batch.diabetes2 = c.DIABETES_2.toString()
					norcPpv1Batch.diabetes3 = c.DIABETES_3.toString()
					norcPpv1Batch.thyroid1 = c.THYROID_1.toString()
					norcPpv1Batch.thyroid2 = c.THYROID_2.toString()
					norcPpv1Batch.vitamin = c.VITAMIN.toString()
					norcPpv1Batch.hlthCare = c.HLTH_CARE.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcPpv1Batch.insure = c.INSURE.toString()
					norcPpv1Batch.insEmploy = c.INS_EMPLOY.toString()
					norcPpv1Batch.insMedicaid = c.INS_MEDICAID.toString()
					norcPpv1Batch.insTricare = c.INS_TRICARE.toString()
					norcPpv1Batch.insIhs = c.INS_IHS.toString()
					norcPpv1Batch.insMedicare = c.INS_MEDICARE.toString()
					norcPpv1Batch.insOth = c.INS_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcPpv1Batch.recentMove = c.RECENT_MOVE.toString()
					norcPpv1Batch.ownHome = c.OWN_HOME.toString()
					norcPpv1Batch.ownHomeOth = c.OWN_HOME_OTH.toString()
					norcPpv1Batch.ownHomeOthCodes = c.OWN_HOME_OTH_Codes.toString()
					norcPpv1Batch.ageHome = c.AGE_HOME.toString()
					norcPpv1Batch.lengthReside = c.LENGTH_RESIDE.toString()
					norcPpv1Batch.lengthResideCodes = c.LENGTH_RESIDE_Codes.toString()
					norcPpv1Batch.lengthResideUnit = c.LENGTH_RESIDE_UNIT.toString()
					norcPpv1Batch.mainHeat = c.MAIN_HEAT.toString()
					norcPpv1Batch.mainHeatOth = c.MAIN_HEAT_OTH.toString()
					norcPpv1Batch.mainHeatOthCodes = c.MAIN_HEAT_OTH_Codes.toString()
					norcPpv1Batch.heat201 = c.HEAT2_01.toString()
					norcPpv1Batch.heat202 = c.HEAT2_02.toString()
					norcPpv1Batch.heat203 = c.HEAT2_03.toString()
					norcPpv1Batch.heat204 = c.HEAT2_04.toString()
					norcPpv1Batch.heat205 = c.HEAT2_05.toString()
					norcPpv1Batch.heat206 = c.HEAT2_06.toString()
					norcPpv1Batch.heat207 = c.HEAT2_07.toString()
					norcPpv1Batch.heat208 = c.HEAT2_08.toString()
					norcPpv1Batch.heat209 = c.HEAT2_09.toString()
					norcPpv1Batch.heat210 = c.HEAT2_10.toString()
					norcPpv1Batch.heat211 = c.HEAT2_11.toString()
					norcPpv1Batch.heat212 = c.HEAT2_12.toString()
					norcPpv1Batch.heat2Oth = c.HEAT2_OTH.toString()
					norcPpv1Batch.heat2OthCodes = c.HEAT2_OTH_Codes.toString()
					norcPpv1Batch.cooling = c.COOLING.toString()
					norcPpv1Batch.cool01 = c.COOL_01.toString()
					norcPpv1Batch.cool02 = c.COOL_02.toString()
					norcPpv1Batch.cool03 = c.COOL_03.toString()
					norcPpv1Batch.cool04 = c.COOL_04.toString()
					norcPpv1Batch.cool05 = c.COOL_05.toString()
					norcPpv1Batch.cool06 = c.COOL_06.toString()
					norcPpv1Batch.cool07 = c.COOL_07.toString()
					norcPpv1Batch.coolOth = c.COOL_OTH.toString()
					norcPpv1Batch.coolOthCodes = c.COOL_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcPpv1Batch.water = c.WATER.toString()
					norcPpv1Batch.mold = c.MOLD.toString()
					norcPpv1Batch.roomMold01 = c.ROOM_MOLD_01.toString()
					norcPpv1Batch.roomMold02 = c.ROOM_MOLD_02.toString()
					norcPpv1Batch.roomMold03 = c.ROOM_MOLD_03.toString()
					norcPpv1Batch.roomMold04 = c.ROOM_MOLD_04.toString()
					norcPpv1Batch.roomMold05 = c.ROOM_MOLD_05.toString()
					norcPpv1Batch.roomMold06 = c.ROOM_MOLD_06.toString()
					norcPpv1Batch.roomMold07 = c.ROOM_MOLD_07.toString()
					norcPpv1Batch.roomMold08 = c.ROOM_MOLD_08.toString()
					norcPpv1Batch.roomMold09 = c.ROOM_MOLD_09.toString()
					norcPpv1Batch.roomMold10 = c.ROOM_MOLD_10.toString()
					norcPpv1Batch.roomMoldOth = c.ROOM_MOLD_OTH.toString()
					norcPpv1Batch.roomMoldOthCodes = c.ROOM_MOLD_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcPpv1Batch.renovate = c.RENOVATE.toString()
					norcPpv1Batch.renovateRoom01 = c.RENOVATE_ROOM_01.toString()
					norcPpv1Batch.renovateRoom02 = c.RENOVATE_ROOM_02.toString()
					norcPpv1Batch.renovateRoom03 = c.RENOVATE_ROOM_03.toString()
					norcPpv1Batch.renovateRoom04 = c.RENOVATE_ROOM_04.toString()
					norcPpv1Batch.renovateRoom05 = c.RENOVATE_ROOM_05.toString()
					norcPpv1Batch.renovateRoom06 = c.RENOVATE_ROOM_06.toString()
					norcPpv1Batch.renovateRoom07 = c.RENOVATE_ROOM_07.toString()
					norcPpv1Batch.renovateRoom08 = c.RENOVATE_ROOM_08.toString()
					norcPpv1Batch.renovateRoom09 = c.RENOVATE_ROOM_09.toString()
					norcPpv1Batch.renovateRoom10 = c.RENOVATE_ROOM_10.toString()
					norcPpv1Batch.renovateRoomOth = c.RENOVATE_ROOM_OTH.toString()
					norcPpv1Batch.renovateRoomOthCodes = c.RENOVATE_ROOM_OTH_Codes.toString()
					norcPpv1Batch.decorate = c.DECORATE.toString()
					norcPpv1Batch.decorateRoom01 = c.DECORATE_ROOM_01.toString()
					norcPpv1Batch.decorateRoom02 = c.DECORATE_ROOM_02.toString()
					norcPpv1Batch.decorateRoom03 = c.DECORATE_ROOM_03.toString()
					norcPpv1Batch.decorateRoom04 = c.DECORATE_ROOM_04.toString()
					norcPpv1Batch.decorateRoom05 = c.DECORATE_ROOM_05.toString()
					norcPpv1Batch.decorateRoom06 = c.DECORATE_ROOM_06.toString()
					norcPpv1Batch.decorateRoom07 = c.DECORATE_ROOM_07.toString()
					norcPpv1Batch.decorateRoom08 = c.DECORATE_ROOM_08.toString()
					norcPpv1Batch.decorateRoom09 = c.DECORATE_ROOM_09.toString()
					norcPpv1Batch.decorateRoom10 = c.DECORATE_ROOM_10.toString()
					norcPpv1Batch.decorateRoomOth = c.DECORATE_ROOM_OTH.toString()
					norcPpv1Batch.decorateRoomOthCodes = c.DECORATE_ROOM_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcPpv1Batch.waterDrink = c.WATER_DRINK.toString()
					norcPpv1Batch.waterDrinkOth = c.WATER_DRINK_OTH.toString()
					norcPpv1Batch.waterDrinkOthCodes = c.WATER_DRINK_OTH_Codes.toString()
					norcPpv1Batch.waterCook = c.WATER_COOK.toString()
					norcPpv1Batch.waterCookOth = c.WATER_COOK_OTH.toString()
					norcPpv1Batch.waterCookOthCodes = c.WATER_COOK_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcPpv1Batch.educ = c.EDUC.toString()
					norcPpv1Batch.working = c.WORKING.toString()
					norcPpv1Batch.hours = c.HOURS.toString()
					norcPpv1Batch.hoursCodes = c.HOURS_Codes.toString()
					norcPpv1Batch.shiftWork = c.SHIFT_WORK.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcPpv1Batch.maristat = c.MARISTAT.toString()
					norcPpv1Batch.spEduc = c.SP_EDUC.toString()
					norcPpv1Batch.spEthnicity = c.SP_ETHNICITY.toString()
					norcPpv1Batch.spRace01 = c.SP_RACE_01.toString()
					norcPpv1Batch.spRace02 = c.SP_RACE_02.toString()
					norcPpv1Batch.spRace03 = c.SP_RACE_03.toString()
					norcPpv1Batch.spRace04 = c.SP_RACE_04.toString()
					norcPpv1Batch.spRace05 = c.SP_RACE_05.toString()
					norcPpv1Batch.spRace06 = c.SP_RACE_06.toString()
					norcPpv1Batch.spRace07 = c.SP_RACE_07.toString()
					norcPpv1Batch.spRace08 = c.SP_RACE_08.toString()
					norcPpv1Batch.spRaceOth = c.SP_RACE_OTH.toString()
					norcPpv1Batch.spRaceOthCodes = c.SP_RACE_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_10.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp10 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp10 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_10: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Input: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Exception: ${e.toString()}'
					}
					norcPpv1Batch.hhMembers = c.HH_MEMBERS.toString()
					norcPpv1Batch.hhMembersCodes = c.HH_MEMBERS_Codes.toString()
					norcPpv1Batch.numChild = c.NUM_CHILD.toString()
					norcPpv1Batch.numChildCodes = c.NUM_CHILD_Codes.toString()
					norcPpv1Batch.income = c.INCOME.toString()
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					norcPpv1Batch.haveEmail = c.HAVE_EMAIL.toString()
					norcPpv1Batch.email2 = c.EMAIL_2.toString()
					norcPpv1Batch.email3 = c.EMAIL_3.toString()
					norcPpv1Batch.email = c.EMAIL.toString()
					norcPpv1Batch.emailCodes = c.EMAIL_Codes.toString()
					norcPpv1Batch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcPpv1Batch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcPpv1Batch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcPpv1Batch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcPpv1Batch.cellPhone = c.CELL_PHONE.toString()
					norcPpv1Batch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					norcPpv1Batch.contact1 = c.CONTACT_1.toString()
					norcPpv1Batch.contactFname1 = c.CONTACT_FNAME_1.toString()
					norcPpv1Batch.contactLname1 = c.CONTACT_LNAME_1.toString()
					norcPpv1Batch.contactLname1Codes = c.CONTACT_LNAME_1_Codes.toString()
					norcPpv1Batch.contactRelate1 = c.CONTACT_RELATE_1.toString()
					norcPpv1Batch.contactRelate1Oth = c.CONTACT_RELATE1_OTH.toString()
					norcPpv1Batch.contactRelate1OthCodes = c.CONTACT_RELATE1_OTH_Codes.toString()
					norcPpv1Batch.cAddr11 = c.C_ADDR1_1.toString()
					norcPpv1Batch.cAddr11Codes = c.C_ADDR1_1_Codes.toString()
					norcPpv1Batch.cAddr21 = c.C_ADDR2_1.toString()
					norcPpv1Batch.cAddr21Codes = c.C_ADDR2_1_Codes.toString()
					norcPpv1Batch.cUnit1 = c.C_UNIT_1.toString()
					norcPpv1Batch.cUnit1Codes = c.C_UNIT_1_Codes.toString()
					norcPpv1Batch.cCity1 = c.C_CITY_1.toString()
					norcPpv1Batch.cCity1Codes = c.C_CITY_1_Codes.toString()
					norcPpv1Batch.cState1 = c.C_STATE_1.toString()
					norcPpv1Batch.cState1Codes = c.C_STATE_1_Codes.toString()
					norcPpv1Batch.cZipcode1 = c.C_ZIPCODE_1.toString()
					norcPpv1Batch.cZipcode1Codes = c.C_ZIPCODE_1_Codes.toString()
					norcPpv1Batch.cZip41 = c.C_ZIP4_1.toString()
					norcPpv1Batch.cZip41Codes = c.C_ZIP4_1_Codes.toString()
					norcPpv1Batch.contactPhone1 = c.CONTACT_PHONE_1.toString()
					norcPpv1Batch.contactPhone1Codes = c.CONTACT_PHONE_1_Codes.toString()
					norcPpv1Batch.contact2 = c.CONTACT_2.toString()
					norcPpv1Batch.contactFname2 = c.CONTACT_FNAME_2.toString()
					norcPpv1Batch.contactLname2 = c.CONTACT_LNAME_2.toString()
					norcPpv1Batch.contactLname2Codes = c.CONTACT_LNAME_2_Codes.toString()
					norcPpv1Batch.contactRelate2 = c.CONTACT_RELATE_2.toString()
					norcPpv1Batch.contactRelate2Oth = c.CONTACT_RELATE2_OTH.toString()
					norcPpv1Batch.contactRelate2OthCodes = c.CONTACT_RELATE2_OTH_Codes.toString()
					norcPpv1Batch.cAddr12 = c.C_ADDR1_2.toString()
					norcPpv1Batch.cAddr12Codes = c.C_ADDR1_2_Codes.toString()
					norcPpv1Batch.cAddr22 = c.C_ADDR2_2.toString()
					norcPpv1Batch.cAddr22Codes = c.C_ADDR2_2_Codes.toString()
					norcPpv1Batch.cUnit2 = c.C_UNIT_2.toString()
					norcPpv1Batch.cUnit2Codes = c.C_UNIT_2_Codes.toString()
					norcPpv1Batch.cCity2 = c.C_CITY_2.toString()
					norcPpv1Batch.cCity2Codes = c.C_CITY_2_Codes.toString()
					norcPpv1Batch.cState2 = c.C_STATE_2.toString()
					norcPpv1Batch.cState2Codes = c.C_STATE_2_Codes.toString()
					norcPpv1Batch.cZipcode2 = c.C_ZIPCODE_2.toString()
					norcPpv1Batch.cZipcode2Codes = c.C_ZIPCODE_2_Codes.toString()
					norcPpv1Batch.cZip42 = c.C_ZIP4_2.toString()
					norcPpv1Batch.cZip42Codes = c.C_ZIP4_2_Codes.toString()
					norcPpv1Batch.contactPhone2 = c.CONTACT_PHONE_2.toString()
					norcPpv1Batch.contactPhone2Codes = c.CONTACT_PHONE_2_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcPpv1Batch.contactLocation = c.CONTACT_LOCATION.toString()
					norcPpv1Batch.contactLocationOth = c.CONTACT_LOCATION_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_13.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp13 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp13 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_13: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Input: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Exception: ${e.toString()}'
					}
					norcPpv1Batch.saqintro = c.SAQINTRO.toString()
					norcPpv1Batch.saqagree = c.SAQAGREE.toString()
					norcPpv1Batch.saqint2 = c.SAQINT2.toString()
					norcPpv1Batch.saqint3 = c.SAQINT3.toString()
					norcPpv1Batch.learn = c.LEARN.toString()
					norcPpv1Batch.help = c.HELP.toString()
					norcPpv1Batch.incent = c.INCENT.toString()
					norcPpv1Batch.research = c.RESEARCH.toString()
					norcPpv1Batch.envir = c.ENVIR.toString()
					norcPpv1Batch.community = c.COMMUNITY.toString()
					norcPpv1Batch.knowOthers = c.KNOW_OTHERS.toString()
					norcPpv1Batch.family = c.FAMILY.toString()
					norcPpv1Batch.doctor = c.DOCTOR.toString()
					norcPpv1Batch.staff = c.STAFF.toString()
					norcPpv1Batch.opinSpouse = c.OPIN_SPOUSE.toString()
					norcPpv1Batch.opinFamily = c.OPIN_FAMILY.toString()
					norcPpv1Batch.opinFriend = c.OPIN_FRIEND.toString()
					norcPpv1Batch.opinDr = c.OPIN_DR.toString()
					norcPpv1Batch.experience = c.EXPERIENCE.toString()
					norcPpv1Batch.improve = c.IMPROVE.toString()
					norcPpv1Batch.intLength = c.INT_LENGTH.toString()
					norcPpv1Batch.intStress = c.INT_STRESS.toString()
					norcPpv1Batch.intRepeat = c.INT_REPEAT.toString()
					norcPpv1Batch.passwrd2 = c.PASSWRD2.toString()
					try {
						dateTimeString = c.TIME_STAMP_14.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.timeStamp14 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.timeStamp14 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_14: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Input: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Exception: ${e.toString()}'
					}
					norcPpv1Batch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcPpv1Batch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPpv1Batch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcPpv1Batch.hasErrors()) {
						response << "! norcPpv1Batch has errors.\n"
					} else if (norcPpv1Batch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcPpv1Batch record with Respondent Serial ${norcPpv1Batch.suId}"
						norcPpv1Batch.errors.each{ e ->
							// println "norcPpv1Batch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcPpv1Batch in ${diff} seconds"
		}
		// end PPV1_BATCH
	}

	def parsePv1Batch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing PV1_BATCH1"
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.HH_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.PV1_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcPv1Batch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcPv1Batch = NorcPv1Batch.findByRespondentSerial(checkSerial)
		
					if (!norcPv1Batch) {
						norcPv1Batch = new NorcPv1Batch()
						response <<  " + Creating new NorcPv1Batch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcPv1Batch.lock()
						response <<  " ~ Updating existing NorcPv1Batch(${checkSerial})\n"
					}

					norcPv1Batch.respondentSerial = c.Respondent_Serial.toString()
					norcPv1Batch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcPv1Batch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcPv1Batch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcPv1Batch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcPv1Batch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcPv1Batch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcPv1Batch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcPv1Batch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcPv1Batch.respondentId = c.Respondent_ID.toString()
					norcPv1Batch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcPv1Batch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcPv1Batch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcPv1Batch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcPv1Batch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcPv1Batch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcPv1Batch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcPv1Batch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcPv1Batch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcPv1Batch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcPv1Batch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcPv1Batch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcPv1Batch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcPv1Batch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcPv1Batch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcPv1Batch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcPv1Batch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcPv1Batch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcPv1Batch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcPv1Batch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcPv1Batch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcPv1Batch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcPv1Batch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcPv1Batch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcPv1Batch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcPv1Batch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcPv1Batch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcPv1Batch.datacleaningNote = c.DataCleaning_Note.toString()
					norcPv1Batch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcPv1Batch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcPv1Batch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcPv1Batch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcPv1Batch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcPv1Batch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcPv1Batch.suId = c.SU_ID.toString()
					norcPv1Batch.majority = c.MAJORITY.toString()
					norcPv1Batch.prFname = c.PR_FNAME.toString()
					norcPv1Batch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcPv1Batch.prLname = c.PR_LNAME.toString()
					norcPv1Batch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcPv1Batch.dobMo = c.DOB_MO.toString()
					norcPv1Batch.dobMoCodes = c.DOB_MO_Codes.toString()
					norcPv1Batch.dobDy = c.DOB_DY.toString()
					norcPv1Batch.dobDyCodes = c.DOB_DY_Codes.toString()
					norcPv1Batch.dobYr = c.DOB_YR.toString()
					norcPv1Batch.dobYrCodes = c.DOB_YR_Codes.toString()
					norcPv1Batch.agecalc = c.AGECALC.toString()
					norcPv1Batch.ageRange = c.AGE_RANGE.toString()
					norcPv1Batch.trimester = c.TRIMESTER.toString()
					norcPv1Batch.prepreg = c.PREPREG.toString()
					norcPv1Batch.prRecentMove = c.PR_RECENT_MOVE.toString()
					norcPv1Batch.prOwnHome = c.PR_OWN_HOME.toString()
					try {
						dateTimeString = c.TIME_STAMP_1A.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp1a = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp1a = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1A: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Input: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Exception: ${e.toString()}'
					}
					norcPv1Batch.quexlang = c.QUEXLANG.toString()
					norcPv1Batch.sameday = c.SAMEDAY.toString()
					norcPv1Batch.visWhoConsented = c.VIS_WHO_CONSENTED.toString()
					norcPv1Batch.visConsentType1 = c.VIS_CONSENT_TYPE_1.toString()
					norcPv1Batch.visConsentType2 = c.VIS_CONSENT_TYPE_2.toString()
					norcPv1Batch.visConsentResponse1 = c.VIS_CONSENT_RESPONSE_1.toString()
					norcPv1Batch.visConsentResponse2 = c.VIS_CONSENT_RESPONSE_2.toString()
					norcPv1Batch.visComments = c.VIS_COMMENTS.toString()
					norcPv1Batch.visCommentsCodes = c.VIS_COMMENTS_Codes.toString()
					norcPv1Batch.english = c.ENGLISH.toString()
					norcPv1Batch.contactLang = c.CONTACT_LANG.toString()
					norcPv1Batch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcPv1Batch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcPv1Batch.interpret = c.INTERPRET.toString()
					norcPv1Batch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcPv1Batch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_1B.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp1b = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp1b = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1B: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Input: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcPv1Batch.nameConfirm = c.NAME_CONFIRM.toString()
					norcPv1Batch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcPv1Batch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcPv1Batch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcPv1Batch.currmo = c.CURRMO.toString()
					norcPv1Batch.currdy = c.CURRDY.toString()
					norcPv1Batch.curryr = c.CURRYR.toString()
					norcPv1Batch.dobConfirm = c.DOB_CONFIRM.toString()
					norcPv1Batch.age = c.AGE.toString()
					norcPv1Batch.personDobMonth = c.PERSON_DOB_MONTH.toString()
					norcPv1Batch.personDobMonthCodes = c.PERSON_DOB_MONTH_Codes.toString()
					norcPv1Batch.personDobDay = c.PERSON_DOB_DAY.toString()
					norcPv1Batch.personDobDayCodes = c.PERSON_DOB_DAY_Codes.toString()
					norcPv1Batch.personDobYear = c.PERSON_DOB_YEAR.toString()
					norcPv1Batch.personDobYearCodes = c.PERSON_DOB_YEAR_Codes.toString()
					norcPv1Batch.ageElig = c.AGE_ELIG.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcPv1Batch.pregnant = c.PREGNANT.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcPv1Batch.lossInfo = c.LOSS_INFO.toString()
					norcPv1Batch.dueDateMonth = c.DUE_DATE_MONTH.toString()
					norcPv1Batch.dueDateMonthCodes = c.DUE_DATE_MONTH_Codes.toString()
					norcPv1Batch.dueDateDay = c.DUE_DATE_DAY.toString()
					norcPv1Batch.dueDateDayCodes = c.DUE_DATE_DAY_Codes.toString()
					norcPv1Batch.dueDateYear = c.DUE_DATE_YEAR.toString()
					norcPv1Batch.dueDateYearCodes = c.DUE_DATE_YEAR_Codes.toString()
					norcPv1Batch.knowDate = c.KNOW_DATE.toString()
					norcPv1Batch.datePeriodMonth = c.DATE_PERIOD_MONTH.toString()
					norcPv1Batch.datePeriodMonthCodes = c.DATE_PERIOD_MONTH_Codes.toString()
					norcPv1Batch.datePeriodDay = c.DATE_PERIOD_DAY.toString()
					norcPv1Batch.datePeriodDayCodes = c.DATE_PERIOD_DAY_Codes.toString()
					norcPv1Batch.datePeriodYear = c.DATE_PERIOD_YEAR.toString()
					norcPv1Batch.datePeriodYearCodes = c.DATE_PERIOD_YEAR_Codes.toString()
					norcPv1Batch.knewDate = c.KNEW_DATE.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcPv1Batch.homeTest = c.HOME_TEST.toString()
					norcPv1Batch.multipleGestation = c.MULTIPLE_GESTATION.toString()
					norcPv1Batch.brthtxt = c.brthtxt.toString()
					norcPv1Batch.birthPlan = c.BIRTH_PLAN.toString()
					norcPv1Batch.birthplaceBirthPlace = c.BirthPlace_BIRTH_PLACE.toString()
					norcPv1Batch.birthcBirPaceCds8 = c.Birthc_BIR_PACE_Cds8.toString()
					norcPv1Batch.birthplaceBAddress1 = c.BirthPlace_B_ADDRESS_1.toString()
					norcPv1Batch.birthcBAress1Cds9 = c.Birthc_B_ARESS_1_Cds9.toString()
					norcPv1Batch.birthplaceBAddress2 = c.BirthPlace_B_ADDRESS_2.toString()
					norcPv1Batch.birthcBAress2Cds10 = c.Birthc_B_ARESS_2_Cds10.toString()
					norcPv1Batch.birthplaceBCity = c.BirthPlace_B_CITY.toString()
					norcPv1Batch.birthplaceBCityCodes = c.BirthPlace_B_CITY_Codes.toString()
					norcPv1Batch.birthplaceBState = c.BirthPlace_B_STATE.toString()
					norcPv1Batch.birthplaceBZipcode = c.BirthPlace_B_ZIPCODE.toString()
					norcPv1Batch.birthplaceBZipcodeCodes = c.BirthPlace_B_ZIPCODE_Codes.toString()
					norcPv1Batch.pnVitamin = c.PN_VITAMIN.toString()
					norcPv1Batch.pregVitamin = c.PREG_VITAMIN.toString()
					norcPv1Batch.dateVisitMonth = c.DATE_VISIT_MONTH.toString()
					norcPv1Batch.dateVisitMonthCodes = c.DATE_VISIT_MONTH_Codes.toString()
					norcPv1Batch.dateVisitDay = c.DATE_VISIT_DAY.toString()
					norcPv1Batch.dateVisitDayCodes = c.DATE_VISIT_DAY_Codes.toString()
					norcPv1Batch.dateVisitYear = c.DATE_VISIT_YEAR.toString()
					norcPv1Batch.dateVisitYearCodes = c.DATE_VISIT_YEAR_Codes.toString()
					norcPv1Batch.diabtxt = c.diabtxt.toString()
					norcPv1Batch.diabetes1 = c.DIABETES_1.toString()
					norcPv1Batch.highbpPreg = c.HIGHBP_PREG.toString()
					norcPv1Batch.urine = c.URINE.toString()
					norcPv1Batch.preeclamp = c.PREECLAMP.toString()
					norcPv1Batch.earlyLabor = c.EARLY_LABOR.toString()
					norcPv1Batch.anemia = c.ANEMIA.toString()
					norcPv1Batch.nausea = c.NAUSEA.toString()
					norcPv1Batch.kidney = c.KIDNEY.toString()
					norcPv1Batch.rhDisease = c.RH_DISEASE.toString()
					norcPv1Batch.groupB = c.GROUP_B.toString()
					norcPv1Batch.herpes = c.HERPES.toString()
					norcPv1Batch.vaginosis = c.VAGINOSIS.toString()
					norcPv1Batch.othCondition = c.OTH_CONDITION.toString()
					norcPv1Batch.conditionOth = c.CONDITION_OTH.toString()
					norcPv1Batch.conditionOthCodes = c.CONDITION_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcPv1Batch.health = c.HEALTH.toString()
					norcPv1Batch.heightHeightFt = c.Height_HEIGHT_FT.toString()
					norcPv1Batch.heightHeightFtCodes = c.Height_HEIGHT_FT_Codes.toString()
					norcPv1Batch.heightHtInch = c.Height_HT_INCH.toString()
					norcPv1Batch.heightHtInchCodes = c.Height_HT_INCH_Codes.toString()
					norcPv1Batch.weight = c.WEIGHT.toString()
					norcPv1Batch.weightCodes = c.WEIGHT_Codes.toString()
					norcPv1Batch.asthma = c.ASTHMA.toString()
					norcPv1Batch.highbpNotpreg = c.HIGHBP_NOTPREG.toString()
					norcPv1Batch.diabetesNotpreg = c.DIABETES_NOTPREG.toString()
					norcPv1Batch.diabetes2 = c.DIABETES_2.toString()
					norcPv1Batch.diabetes3 = c.DIABETES_3.toString()
					norcPv1Batch.thyroid1 = c.THYROID_1.toString()
					norcPv1Batch.thyroid2 = c.THYROID_2.toString()
					norcPv1Batch.hlthCare = c.HLTH_CARE.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcPv1Batch.insure = c.INSURE.toString()
					norcPv1Batch.insEmploy = c.INS_EMPLOY.toString()
					norcPv1Batch.insMedicaid = c.INS_MEDICAID.toString()
					norcPv1Batch.insTricare = c.INS_TRICARE.toString()
					norcPv1Batch.insIhs = c.INS_IHS.toString()
					norcPv1Batch.insMedicare = c.INS_MEDICARE.toString()
					norcPv1Batch.insOth = c.INS_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcPv1Batch.recentMove = c.RECENT_MOVE.toString()
					norcPv1Batch.ownHome = c.OWN_HOME.toString()
					norcPv1Batch.ownHomeOth = c.OWN_HOME_OTH.toString()
					norcPv1Batch.ownHomeOthCodes = c.OWN_HOME_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcPv1Batch.ageHome = c.AGE_HOME.toString()
					norcPv1Batch.lengthReside = c.LENGTH_RESIDE.toString()
					norcPv1Batch.lengthResideCodes = c.LENGTH_RESIDE_Codes.toString()
					norcPv1Batch.lengthResideUnit = c.LENGTH_RESIDE_UNIT.toString()
					norcPv1Batch.mainHeat = c.MAIN_HEAT.toString()
					norcPv1Batch.mainHeatOth = c.MAIN_HEAT_OTH.toString()
					norcPv1Batch.mainHeatOthCodes = c.MAIN_HEAT_OTH_Codes.toString()
					norcPv1Batch.heat201 = c.HEAT2_01.toString()
					norcPv1Batch.heat202 = c.HEAT2_02.toString()
					norcPv1Batch.heat203 = c.HEAT2_03.toString()
					norcPv1Batch.heat204 = c.HEAT2_04.toString()
					norcPv1Batch.heat205 = c.HEAT2_05.toString()
					norcPv1Batch.heat206 = c.HEAT2_06.toString()
					norcPv1Batch.heat207 = c.HEAT2_07.toString()
					norcPv1Batch.heat208 = c.HEAT2_08.toString()
					norcPv1Batch.heat209 = c.HEAT2_09.toString()
					norcPv1Batch.heat210 = c.HEAT2_10.toString()
					norcPv1Batch.heat211 = c.HEAT2_11.toString()
					norcPv1Batch.heat212 = c.HEAT2_12.toString()
					norcPv1Batch.heat2Oth = c.HEAT2_OTH.toString()
					norcPv1Batch.heat2OthCodes = c.HEAT2_OTH_Codes.toString()
					norcPv1Batch.cooling = c.COOLING.toString()
					norcPv1Batch.cool01 = c.COOL_01.toString()
					norcPv1Batch.cool02 = c.COOL_02.toString()
					norcPv1Batch.cool03 = c.COOL_03.toString()
					norcPv1Batch.cool04 = c.COOL_04.toString()
					norcPv1Batch.cool05 = c.COOL_05.toString()
					norcPv1Batch.cool06 = c.COOL_06.toString()
					norcPv1Batch.cool07 = c.COOL_07.toString()
					norcPv1Batch.coolOth = c.COOL_OTH.toString()
					norcPv1Batch.coolOthCodes = c.COOL_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcPv1Batch.waterDrink = c.WATER_DRINK.toString()
					norcPv1Batch.waterDrinkOth = c.WATER_DRINK_OTH.toString()
					norcPv1Batch.waterDrinkOthCodes = c.WATER_DRINK_OTH_Codes.toString()
					norcPv1Batch.waterCook = c.WATER_COOK.toString()
					norcPv1Batch.waterCookOth = c.WATER_COOK_OTH.toString()
					norcPv1Batch.waterCookOthCodes = c.WATER_COOK_OTH_Codes.toString()
					norcPv1Batch.water = c.WATER.toString()
					norcPv1Batch.mold = c.MOLD.toString()
					norcPv1Batch.roomMold01 = c.ROOM_MOLD_01.toString()
					norcPv1Batch.roomMold02 = c.ROOM_MOLD_02.toString()
					norcPv1Batch.roomMold03 = c.ROOM_MOLD_03.toString()
					norcPv1Batch.roomMold04 = c.ROOM_MOLD_04.toString()
					norcPv1Batch.roomMold05 = c.ROOM_MOLD_05.toString()
					norcPv1Batch.roomMold06 = c.ROOM_MOLD_06.toString()
					norcPv1Batch.roomMold07 = c.ROOM_MOLD_07.toString()
					norcPv1Batch.roomMold08 = c.ROOM_MOLD_08.toString()
					norcPv1Batch.roomMold09 = c.ROOM_MOLD_09.toString()
					norcPv1Batch.roomMold10 = c.ROOM_MOLD_10.toString()
					norcPv1Batch.roomMoldOth = c.ROOM_MOLD_OTH.toString()
					norcPv1Batch.roomMoldOthCodes = c.ROOM_MOLD_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_10.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp10 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp10 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_10: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Input: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Exception: ${e.toString()}'
					}
					norcPv1Batch.prenovate = c.PRENOVATE.toString()
					norcPv1Batch.prenovateRoom01 = c.PRENOVATE_ROOM_01.toString()
					norcPv1Batch.prenovateRoom02 = c.PRENOVATE_ROOM_02.toString()
					norcPv1Batch.prenovateRoom03 = c.PRENOVATE_ROOM_03.toString()
					norcPv1Batch.prenovateRoom04 = c.PRENOVATE_ROOM_04.toString()
					norcPv1Batch.prenovateRoom05 = c.PRENOVATE_ROOM_05.toString()
					norcPv1Batch.prenovateRoom06 = c.PRENOVATE_ROOM_06.toString()
					norcPv1Batch.prenovateRoom07 = c.PRENOVATE_ROOM_07.toString()
					norcPv1Batch.prenovateRoom08 = c.PRENOVATE_ROOM_08.toString()
					norcPv1Batch.prenovateRoom09 = c.PRENOVATE_ROOM_09.toString()
					norcPv1Batch.prenovateRoom10 = c.PRENOVATE_ROOM_10.toString()
					norcPv1Batch.prenovateRoomOth = c.PRENOVATE_ROOM_OTH.toString()
					norcPv1Batch.prenovateRoomOthCodes = c.PRENOVATE_ROOM_OTH_Codes.toString()
					norcPv1Batch.pdecorate = c.PDECORATE.toString()
					norcPv1Batch.pdecorateRoom01 = c.PDECORATE_ROOM_01.toString()
					norcPv1Batch.pdecorateRoom02 = c.PDECORATE_ROOM_02.toString()
					norcPv1Batch.pdecorateRoom03 = c.PDECORATE_ROOM_03.toString()
					norcPv1Batch.pdecorateRoom04 = c.PDECORATE_ROOM_04.toString()
					norcPv1Batch.pdecorateRoom05 = c.PDECORATE_ROOM_05.toString()
					norcPv1Batch.pdecorateRoom06 = c.PDECORATE_ROOM_06.toString()
					norcPv1Batch.pdecorateRoom07 = c.PDECORATE_ROOM_07.toString()
					norcPv1Batch.pdecorateRoom08 = c.PDECORATE_ROOM_08.toString()
					norcPv1Batch.pdecorateRoom09 = c.PDECORATE_ROOM_09.toString()
					norcPv1Batch.pdecorateRoom10 = c.PDECORATE_ROOM_10.toString()
					norcPv1Batch.pdecorateRoomOth = c.PDECORATE_ROOM_OTH.toString()
					norcPv1Batch.pdecorateRoomOthCodes = c.PDECORATE_ROOM_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					norcPv1Batch.pets = c.PETS.toString()
					norcPv1Batch.petType01 = c.PET_TYPE_01.toString()
					norcPv1Batch.petType02 = c.PET_TYPE_02.toString()
					norcPv1Batch.petType03 = c.PET_TYPE_03.toString()
					norcPv1Batch.petType04 = c.PET_TYPE_04.toString()
					norcPv1Batch.petType05 = c.PET_TYPE_05.toString()
					norcPv1Batch.petType06 = c.PET_TYPE_06.toString()
					norcPv1Batch.petType07 = c.PET_TYPE_07.toString()
					norcPv1Batch.petType08 = c.PET_TYPE_08.toString()
					norcPv1Batch.petTypeOth = c.PET_TYPE_OTH.toString()
					norcPv1Batch.petTypeOthCodes = c.PET_TYPE_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcPv1Batch.educ = c.EDUC.toString()
					norcPv1Batch.working = c.WORKING.toString()
					norcPv1Batch.hours = c.HOURS.toString()
					norcPv1Batch.hoursCodes = c.HOURS_Codes.toString()
					norcPv1Batch.shiftWork = c.SHIFT_WORK.toString()
					norcPv1Batch.commute01 = c.COMMUTE_01.toString()
					norcPv1Batch.commute02 = c.COMMUTE_02.toString()
					norcPv1Batch.commute03 = c.COMMUTE_03.toString()
					norcPv1Batch.commute04 = c.COMMUTE_04.toString()
					norcPv1Batch.commute05 = c.COMMUTE_05.toString()
					norcPv1Batch.commute06 = c.COMMUTE_06.toString()
					norcPv1Batch.commute07 = c.COMMUTE_07.toString()
					norcPv1Batch.commute08 = c.COMMUTE_08.toString()
					norcPv1Batch.commuteOth = c.COMMUTE_OTH.toString()
					norcPv1Batch.commuteOthCodes = c.COMMUTE_OTH_Codes.toString()
					norcPv1Batch.commuteTime = c.COMMUTE_TIME.toString()
					norcPv1Batch.commuteTimeCodes = c.COMMUTE_TIME_Codes.toString()
					norcPv1Batch.localTrav01 = c.LOCAL_TRAV_01.toString()
					norcPv1Batch.localTrav02 = c.LOCAL_TRAV_02.toString()
					norcPv1Batch.localTrav03 = c.LOCAL_TRAV_03.toString()
					norcPv1Batch.localTrav04 = c.LOCAL_TRAV_04.toString()
					norcPv1Batch.localTrav05 = c.LOCAL_TRAV_05.toString()
					norcPv1Batch.localTrav06 = c.LOCAL_TRAV_06.toString()
					norcPv1Batch.localTrav07 = c.LOCAL_TRAV_07.toString()
					norcPv1Batch.localTravOth = c.LOCAL_TRAV_OTH.toString()
					norcPv1Batch.localTravOthCodes = c.LOCAL_TRAV_OTH_Codes.toString()
					norcPv1Batch.pumpGas = c.PUMP_GAS.toString()
					try {
						dateTimeString = c.TIME_STAMP_13.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp13 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp13 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_13: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Input: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Exception: ${e.toString()}'
					}
					norcPv1Batch.maristat = c.MARISTAT.toString()
					norcPv1Batch.spEduc = c.SP_EDUC.toString()
					norcPv1Batch.spEthnicity = c.SP_ETHNICITY.toString()
					norcPv1Batch.spRace01 = c.SP_RACE_01.toString()
					norcPv1Batch.spRace02 = c.SP_RACE_02.toString()
					norcPv1Batch.spRace03 = c.SP_RACE_03.toString()
					norcPv1Batch.spRace04 = c.SP_RACE_04.toString()
					norcPv1Batch.spRace05 = c.SP_RACE_05.toString()
					norcPv1Batch.spRace06 = c.SP_RACE_06.toString()
					norcPv1Batch.spRace07 = c.SP_RACE_07.toString()
					norcPv1Batch.spRace08 = c.SP_RACE_08.toString()
					norcPv1Batch.spRaceOth = c.SP_RACE_OTH.toString()
					norcPv1Batch.spRaceOthCodes = c.SP_RACE_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_14.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp14 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp14 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_14: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Input: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Exception: ${e.toString()}'
					}
					norcPv1Batch.hhMembers = c.HH_MEMBERS.toString()
					norcPv1Batch.hhMembersCodes = c.HH_MEMBERS_Codes.toString()
					norcPv1Batch.numChild = c.NUM_CHILD.toString()
					norcPv1Batch.numChildCodes = c.NUM_CHILD_Codes.toString()
					norcPv1Batch.income = c.INCOME.toString()
					try {
						dateTimeString = c.TIME_STAMP_15.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp15 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp15 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_15: ${c.TIME_STAMP_15.toString()}'
						println '! parse TIME_STAMP_15 Input: ${c.TIME_STAMP_15.toString()}'
						println '! parse TIME_STAMP_15 Exception: ${e.toString()}'
					}
					norcPv1Batch.commEmail = c.COMM_EMAIL.toString()
					norcPv1Batch.haveEmail = c.HAVE_EMAIL.toString()
					norcPv1Batch.email2 = c.EMAIL_2.toString()
					norcPv1Batch.email3 = c.EMAIL_3.toString()
					norcPv1Batch.email = c.EMAIL.toString()
					norcPv1Batch.emailCodes = c.EMAIL_Codes.toString()
					norcPv1Batch.commCell = c.COMM_CELL.toString()
					norcPv1Batch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcPv1Batch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcPv1Batch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcPv1Batch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcPv1Batch.cellPhone = c.CELL_PHONE.toString()
					norcPv1Batch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_16.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp16 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp16 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_16: ${c.TIME_STAMP_16.toString()}'
						println '! parse TIME_STAMP_16 Input: ${c.TIME_STAMP_16.toString()}'
						println '! parse TIME_STAMP_16 Exception: ${e.toString()}'
					}
					norcPv1Batch.commContact = c.COMM_CONTACT.toString()
					norcPv1Batch.contact1 = c.CONTACT_1.toString()
					norcPv1Batch.contactFname1 = c.CONTACT_FNAME_1.toString()
					norcPv1Batch.contactFname1Codes = c.CONTACT_FNAME_1_Codes.toString()
					norcPv1Batch.contactLname1 = c.CONTACT_LNAME_1.toString()
					norcPv1Batch.contactLname1Codes = c.CONTACT_LNAME_1_Codes.toString()
					norcPv1Batch.contactRelate1 = c.CONTACT_RELATE_1.toString()
					norcPv1Batch.contactRelate1Oth = c.CONTACT_RELATE1_OTH.toString()
					norcPv1Batch.contactRelate1OthCodes = c.CONTACT_RELATE1_OTH_Codes.toString()
					norcPv1Batch.cAddr11 = c.C_ADDR1_1.toString()
					norcPv1Batch.cAddr11Codes = c.C_ADDR1_1_Codes.toString()
					norcPv1Batch.cAddr21 = c.C_ADDR2_1.toString()
					norcPv1Batch.cAddr21Codes = c.C_ADDR2_1_Codes.toString()
					norcPv1Batch.cUnit1 = c.C_UNIT_1.toString()
					norcPv1Batch.cUnit1Codes = c.C_UNIT_1_Codes.toString()
					norcPv1Batch.cCity1 = c.C_CITY_1.toString()
					norcPv1Batch.cCity1Codes = c.C_CITY_1_Codes.toString()
					norcPv1Batch.cState1 = c.C_STATE_1.toString()
					norcPv1Batch.cZipcode1 = c.C_ZIPCODE_1.toString()
					norcPv1Batch.cZipcode1Codes = c.C_ZIPCODE_1_Codes.toString()
					norcPv1Batch.cZip41 = c.C_ZIP4_1.toString()
					norcPv1Batch.cZip41Codes = c.C_ZIP4_1_Codes.toString()
					norcPv1Batch.contactPhone1 = c.CONTACT_PHONE_1.toString()
					norcPv1Batch.contactPhone1Codes = c.CONTACT_PHONE_1_Codes.toString()
					norcPv1Batch.contact2 = c.CONTACT_2.toString()
					norcPv1Batch.contactFname2 = c.CONTACT_FNAME_2.toString()
					norcPv1Batch.contactFname2Codes = c.CONTACT_FNAME_2_Codes.toString()
					norcPv1Batch.contactLname2 = c.CONTACT_LNAME_2.toString()
					norcPv1Batch.contactLname2Codes = c.CONTACT_LNAME_2_Codes.toString()
					norcPv1Batch.contactRelate2 = c.CONTACT_RELATE_2.toString()
					norcPv1Batch.contactRelate2Oth = c.CONTACT_RELATE2_OTH.toString()
					norcPv1Batch.contactRelate2OthCodes = c.CONTACT_RELATE2_OTH_Codes.toString()
					norcPv1Batch.cAddr12 = c.C_ADDR1_2.toString()
					norcPv1Batch.cAddr12Codes = c.C_ADDR1_2_Codes.toString()
					norcPv1Batch.cAddr22 = c.C_ADDR2_2.toString()
					norcPv1Batch.cAddr22Codes = c.C_ADDR2_2_Codes.toString()
					norcPv1Batch.cUnit2 = c.C_UNIT_2.toString()
					norcPv1Batch.cUnit2Codes = c.C_UNIT_2_Codes.toString()
					norcPv1Batch.cCity2 = c.C_CITY_2.toString()
					norcPv1Batch.cCity2Codes = c.C_CITY_2_Codes.toString()
					norcPv1Batch.cState2 = c.C_STATE_2.toString()
					norcPv1Batch.cZipcode2 = c.C_ZIPCODE_2.toString()
					norcPv1Batch.cZipcode2Codes = c.C_ZIPCODE_2_Codes.toString()
					norcPv1Batch.cZip42 = c.C_ZIP4_2.toString()
					norcPv1Batch.cZip42Codes = c.C_ZIP4_2_Codes.toString()
					norcPv1Batch.contactPhone2 = c.CONTACT_PHONE_2.toString()
					norcPv1Batch.contactPhone2Codes = c.CONTACT_PHONE_2_Codes.toString()
					norcPv1Batch.contactLocation = c.CONTACT_LOCATION.toString()
					norcPv1Batch.contactLocationOth = c.CONTACT_LOCATION_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_17.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp17 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp17 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_17: ${c.TIME_STAMP_17.toString()}'
						println '! parse TIME_STAMP_17 Input: ${c.TIME_STAMP_17.toString()}'
						println '! parse TIME_STAMP_17 Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_18.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp18 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp18 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_18: ${c.TIME_STAMP_18.toString()}'
						println '! parse TIME_STAMP_18 Input: ${c.TIME_STAMP_18.toString()}'
						println '! parse TIME_STAMP_18 Exception: ${e.toString()}'
					}
					norcPv1Batch.saqintro = c.SAQINTRO.toString()
					norcPv1Batch.saqagree = c.SAQAGREE.toString()
					norcPv1Batch.saqint2 = c.SAQINT2.toString()
					norcPv1Batch.saqint3 = c.SAQINT3.toString()
					norcPv1Batch.planned = c.PLANNED.toString()
					norcPv1Batch.monthTry = c.MONTH_TRY.toString()
					norcPv1Batch.monthTryCodes = c.MONTH_TRY_Codes.toString()
					norcPv1Batch.wanted = c.WANTED.toString()
					norcPv1Batch.timing = c.TIMING.toString()
					try {
						dateTimeString = c.TIME_STAMP_19.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp19 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp19 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_19: ${c.TIME_STAMP_19.toString()}'
						println '! parse TIME_STAMP_19 Input: ${c.TIME_STAMP_19.toString()}'
						println '! parse TIME_STAMP_19 Exception: ${e.toString()}'
					}
					norcPv1Batch.pastPreg = c.PAST_PREG.toString()
					norcPv1Batch.numPreg = c.NUM_PREG.toString()
					norcPv1Batch.numPregCodes = c.NUM_PREG_Codes.toString()
					norcPv1Batch.ageFirst = c.AGE_FIRST.toString()
					norcPv1Batch.ageFirstCodes = c.AGE_FIRST_Codes.toString()
					norcPv1Batch.premature = c.PREMATURE.toString()
					norcPv1Batch.miscarry = c.MISCARRY.toString()
					try {
						dateTimeString = c.TIME_STAMP_20.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp20 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp20 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_20: ${c.TIME_STAMP_20.toString()}'
						println '! parse TIME_STAMP_20 Input: ${c.TIME_STAMP_20.toString()}'
						println '! parse TIME_STAMP_20 Exception: ${e.toString()}'
					}
					norcPv1Batch.cigPast = c.CIG_PAST.toString()
					norcPv1Batch.cigPastFreq = c.CIG_PAST_FREQ.toString()
					norcPv1Batch.cigPastNum = c.CIG_PAST_NUM.toString()
					norcPv1Batch.cigPastNumCodes = c.CIG_PAST_NUM_Codes.toString()
					norcPv1Batch.cigPastNumField = c.CIG_PAST_NUM_FIELD.toString()
					norcPv1Batch.cigNow = c.CIG_NOW.toString()
					norcPv1Batch.cigNowFreq = c.CIG_NOW_FREQ.toString()
					norcPv1Batch.cigNowNum = c.CIG_NOW_NUM.toString()
					norcPv1Batch.cigNowNumCodes = c.CIG_NOW_NUM_Codes.toString()
					norcPv1Batch.cigNowNumField = c.CIG_NOW_NUM_FIELD.toString()
					norcPv1Batch.drinkPast = c.DRINK_PAST.toString()
					norcPv1Batch.drinkPastNum = c.DRINK_PAST_NUM.toString()
					norcPv1Batch.drinkPastNumCodes = c.DRINK_PAST_NUM_Codes.toString()
					norcPv1Batch.drinkPast5 = c.DRINK_PAST_5.toString()
					norcPv1Batch.drinkNow = c.DRINK_NOW.toString()
					norcPv1Batch.drinkNowNum = c.DRINK_NOW_NUM.toString()
					norcPv1Batch.drinkNowNumCodes = c.DRINK_NOW_NUM_Codes.toString()
					norcPv1Batch.drinkNow5 = c.DRINK_NOW_5.toString()
					try {
						dateTimeString = c.TIME_STAMP_21.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp21 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp21 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_21: ${c.TIME_STAMP_21.toString()}'
						println '! parse TIME_STAMP_21 Input: ${c.TIME_STAMP_21.toString()}'
						println '! parse TIME_STAMP_21 Exception: ${e.toString()}'
					}
					norcPv1Batch.learn = c.LEARN.toString()
					norcPv1Batch.help = c.HELP.toString()
					norcPv1Batch.incent = c.INCENT.toString()
					norcPv1Batch.research = c.RESEARCH.toString()
					norcPv1Batch.envir = c.ENVIR.toString()
					norcPv1Batch.community = c.COMMUNITY.toString()
					norcPv1Batch.knowOthers = c.KNOW_OTHERS.toString()
					norcPv1Batch.family = c.FAMILY.toString()
					norcPv1Batch.doctor = c.DOCTOR.toString()
					norcPv1Batch.staff = c.STAFF.toString()
					norcPv1Batch.opinSpouse = c.OPIN_SPOUSE.toString()
					norcPv1Batch.opinFamily = c.OPIN_FAMILY.toString()
					norcPv1Batch.opinFriend = c.OPIN_FRIEND.toString()
					norcPv1Batch.opinDr = c.OPIN_DR.toString()
					norcPv1Batch.experience = c.EXPERIENCE.toString()
					norcPv1Batch.improve = c.IMPROVE.toString()
					norcPv1Batch.intLength = c.INT_LENGTH.toString()
					norcPv1Batch.intStress = c.INT_STRESS.toString()
					norcPv1Batch.intRepeat = c.INT_REPEAT.toString()
					norcPv1Batch.passwrd2 = c.PASSWRD2.toString()
					try {
						dateTimeString = c.TIME_STAMP_22.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.timeStamp22 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.timeStamp22 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_22: ${c.TIME_STAMP_22.toString()}'
						println '! parse TIME_STAMP_22 Input: ${c.TIME_STAMP_22.toString()}'
						println '! parse TIME_STAMP_22 Exception: ${e.toString()}'
					}
					norcPv1Batch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcPv1Batch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv1Batch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcPv1Batch.hasErrors()) {
						response << "! norcPv1Batch has errors.\n"
					} else if (norcPv1Batch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcPv1Batch record with Respondent Serial ${norcPv1Batch.suId}"
						norcPv1Batch.errors.each{ e ->
							// println "norcPv1Batch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcPv1Batch in ${diff} seconds"
		}
		// end PV1_BATCH
	}

	def parsePv2Batch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing PV2_BATCH1"
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.HH_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.PV2_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcPv2Batch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcPv2Batch = NorcPv2Batch.findByRespondentSerial(checkSerial)
		
					if (!norcPv2Batch) {
						norcPv2Batch = new NorcPv2Batch()
						response <<  " + Creating new NorcPv2Batch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcPv2Batch.lock()
						response <<  " ~ Updating existing NorcPv2Batch(${checkSerial})\n"
					}

					norcPv2Batch.respondentSerial = c.Respondent_Serial.toString()
					norcPv2Batch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcPv2Batch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcPv2Batch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcPv2Batch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcPv2Batch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcPv2Batch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcPv2Batch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcPv2Batch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcPv2Batch.respondentId = c.Respondent_ID.toString()
					norcPv2Batch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcPv2Batch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcPv2Batch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcPv2Batch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcPv2Batch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcPv2Batch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcPv2Batch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcPv2Batch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcPv2Batch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcPv2Batch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					try {
						dateTimeString = c.DataCollection_StartTime.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.datacollectionStarttime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.datacollectionStarttime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_StartTime: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Input: ${c.DataCollection_StartTime.toString()}'
						println '! parse DataCollection_StartTime Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.DataCollection_FinishTime.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.datacollectionFinishtime = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.datacollectionFinishtime = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid DataCollection_FinishTime: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Input: ${c.DataCollection_FinishTime.toString()}'
						println '! parse DataCollection_FinishTime Exception: ${e.toString()}'
					}
					norcPv2Batch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcPv2Batch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcPv2Batch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcPv2Batch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcPv2Batch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcPv2Batch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcPv2Batch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcPv2Batch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcPv2Batch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcPv2Batch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcPv2Batch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcPv2Batch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcPv2Batch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcPv2Batch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcPv2Batch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcPv2Batch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcPv2Batch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcPv2Batch.datacleaningNote = c.DataCleaning_Note.toString()
					norcPv2Batch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcPv2Batch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcPv2Batch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcPv2Batch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcPv2Batch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcPv2Batch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcPv2Batch.suId = c.SU_ID.toString()
					norcPv2Batch.majority = c.MAJORITY.toString()
					norcPv2Batch.prFname = c.PR_FNAME.toString()
					norcPv2Batch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcPv2Batch.prLname = c.PR_LNAME.toString()
					norcPv2Batch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcPv2Batch.dobMo = c.DOB_MO.toString()
					norcPv2Batch.dobMoCodes = c.DOB_MO_Codes.toString()
					norcPv2Batch.dobDy = c.DOB_DY.toString()
					norcPv2Batch.dobDyCodes = c.DOB_DY_Codes.toString()
					norcPv2Batch.dobYr = c.DOB_YR.toString()
					norcPv2Batch.dobYrCodes = c.DOB_YR_Codes.toString()
					norcPv2Batch.agecalc = c.AGECALC.toString()
					norcPv2Batch.ageRange = c.AGE_RANGE.toString()
					norcPv2Batch.trimester = c.TRIMESTER.toString()
					norcPv2Batch.prepreg = c.PREPREG.toString()
					norcPv2Batch.multipleGestation = c.MULTIPLE_GESTATION.toString()
					norcPv2Batch.prOwnHome = c.PR_OWN_HOME.toString()
					try {
						dateTimeString = c.TIME_STAMP_1A.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp1a = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp1a = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1A: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Input: ${c.TIME_STAMP_1A.toString()}'
						println '! parse TIME_STAMP_1A Exception: ${e.toString()}'
					}
					norcPv2Batch.quexlang = c.QUEXLANG.toString()
					norcPv2Batch.visWhoConsented = c.VIS_WHO_CONSENTED.toString()
					norcPv2Batch.visConsentType1 = c.VIS_CONSENT_TYPE_1.toString()
					norcPv2Batch.visConsentType2 = c.VIS_CONSENT_TYPE_2.toString()
					norcPv2Batch.visConsentResponse1 = c.VIS_CONSENT_RESPONSE_1.toString()
					norcPv2Batch.visConsentResponse2 = c.VIS_CONSENT_RESPONSE_2.toString()
					norcPv2Batch.visComments = c.VIS_COMMENTS.toString()
					norcPv2Batch.visCommentsCodes = c.VIS_COMMENTS_Codes.toString()
					norcPv2Batch.english = c.ENGLISH.toString()
					norcPv2Batch.contactLang = c.CONTACT_LANG.toString()
					norcPv2Batch.contactLangOth = c.CONTACT_LANG_OTH.toString()
					norcPv2Batch.contactLangOthCodes = c.CONTACT_LANG_OTH_Codes.toString()
					norcPv2Batch.interpret = c.INTERPRET.toString()
					norcPv2Batch.contactInterpret = c.CONTACT_INTERPRET.toString()
					norcPv2Batch.contactInterpretOth = c.CONTACT_INTERPRET_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_1B.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp1b = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp1b = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1B: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Input: ${c.TIME_STAMP_1B.toString()}'
						println '! parse TIME_STAMP_1B Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_1.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp1 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp1 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_1: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Input: ${c.TIME_STAMP_1.toString()}'
						println '! parse TIME_STAMP_1 Exception: ${e.toString()}'
					}
					norcPv2Batch.nameConfirm = c.NAME_CONFIRM.toString()
					norcPv2Batch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcPv2Batch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcPv2Batch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcPv2Batch.currmo = c.CURRMO.toString()
					norcPv2Batch.currdy = c.CURRDY.toString()
					norcPv2Batch.curryr = c.CURRYR.toString()
					norcPv2Batch.dobConfirm = c.DOB_CONFIRM.toString()
					norcPv2Batch.age = c.AGE.toString()
					norcPv2Batch.personDobMonth = c.PERSON_DOB_MONTH.toString()
					norcPv2Batch.personDobMonthCodes = c.PERSON_DOB_MONTH_Codes.toString()
					norcPv2Batch.personDobDay = c.PERSON_DOB_DAY.toString()
					norcPv2Batch.personDobDayCodes = c.PERSON_DOB_DAY_Codes.toString()
					norcPv2Batch.personDobYear = c.PERSON_DOB_YEAR.toString()
					norcPv2Batch.personDobYearCodes = c.PERSON_DOB_YEAR_Codes.toString()
					norcPv2Batch.ageElig = c.AGE_ELIG.toString()
					try {
						dateTimeString = c.TIME_STAMP_2.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp2 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp2 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_2: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Input: ${c.TIME_STAMP_2.toString()}'
						println '! parse TIME_STAMP_2 Exception: ${e.toString()}'
					}
					norcPv2Batch.pregnant = c.PREGNANT.toString()
					try {
						dateTimeString = c.TIME_STAMP_3.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp3 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp3 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_3: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Input: ${c.TIME_STAMP_3.toString()}'
						println '! parse TIME_STAMP_3 Exception: ${e.toString()}'
					}
					norcPv2Batch.lossInfo = c.LOSS_INFO.toString()
					norcPv2Batch.dueDateMonth = c.DUE_DATE_MONTH.toString()
					norcPv2Batch.dueDateMonthCodes = c.DUE_DATE_MONTH_Codes.toString()
					norcPv2Batch.dueDateDay = c.DUE_DATE_DAY.toString()
					norcPv2Batch.dueDateDayCodes = c.DUE_DATE_DAY_Codes.toString()
					norcPv2Batch.dueDateYear = c.DUE_DATE_YEAR.toString()
					norcPv2Batch.dueDateYearCodes = c.DUE_DATE_YEAR_Codes.toString()
					norcPv2Batch.dateKnown = c.DATE_KNOWN.toString()
					norcPv2Batch.brthtxt = c.brthtxt.toString()
					norcPv2Batch.bplanChange = c.BPLAN_CHANGE.toString()
					norcPv2Batch.bplantxt = c.bplantxt.toString()
					norcPv2Batch.birthPlan = c.BIRTH_PLAN.toString()
					norcPv2Batch.birthplaceBirthPlace = c.BirthPlace_BIRTH_PLACE.toString()
					norcPv2Batch.birthcBirPaceCds8 = c.Birthc_BIR_PACE_Cds8.toString()
					norcPv2Batch.birthplaceBAddress1 = c.BirthPlace_B_ADDRESS_1.toString()
					norcPv2Batch.birthcBAress1Cds9 = c.Birthc_B_ARESS_1_Cds9.toString()
					norcPv2Batch.birthplaceBAddress2 = c.BirthPlace_B_ADDRESS_2.toString()
					norcPv2Batch.birthcBAress2Cds10 = c.Birthc_B_ARESS_2_Cds10.toString()
					norcPv2Batch.birthplaceBCity = c.BirthPlace_B_CITY.toString()
					norcPv2Batch.birthplaceBCityCodes = c.BirthPlace_B_CITY_Codes.toString()
					norcPv2Batch.birthplaceBState = c.BirthPlace_B_STATE.toString()
					norcPv2Batch.birthplaceBZipcode = c.BirthPlace_B_ZIPCODE.toString()
					norcPv2Batch.birthplaceBZipcodeCodes = c.BirthPlace_B_ZIPCODE_Codes.toString()
					norcPv2Batch.dateVisitMonth = c.DATE_VISIT_MONTH.toString()
					norcPv2Batch.dateVisitMonthCodes = c.DATE_VISIT_MONTH_Codes.toString()
					norcPv2Batch.dateVisitDay = c.DATE_VISIT_DAY.toString()
					norcPv2Batch.dateVisitDayCodes = c.DATE_VISIT_DAY_Codes.toString()
					norcPv2Batch.dateVisitYear = c.DATE_VISIT_YEAR.toString()
					norcPv2Batch.dateVisitYearCodes = c.DATE_VISIT_YEAR_Codes.toString()
					norcPv2Batch.diabtxt = c.diabtxt.toString()
					norcPv2Batch.diabetes1 = c.DIABETES_1.toString()
					norcPv2Batch.highbpPreg = c.HIGHBP_PREG.toString()
					norcPv2Batch.urine = c.URINE.toString()
					norcPv2Batch.preeclamp = c.PREECLAMP.toString()
					norcPv2Batch.earlyLabor = c.EARLY_LABOR.toString()
					norcPv2Batch.anemia = c.ANEMIA.toString()
					norcPv2Batch.nausea = c.NAUSEA.toString()
					norcPv2Batch.kidney = c.KIDNEY.toString()
					norcPv2Batch.rhDisease = c.RH_DISEASE.toString()
					norcPv2Batch.groupB = c.GROUP_B.toString()
					norcPv2Batch.herpes = c.HERPES.toString()
					norcPv2Batch.vaginosis = c.VAGINOSIS.toString()
					norcPv2Batch.othCondition = c.OTH_CONDITION.toString()
					norcPv2Batch.conditionOth = c.CONDITION_OTH.toString()
					norcPv2Batch.conditionOthCodes = c.CONDITION_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_4.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp4 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp4 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_4: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Input: ${c.TIME_STAMP_4.toString()}'
						println '! parse TIME_STAMP_4 Exception: ${e.toString()}'
					}
					norcPv2Batch.hospital = c.HOSPITAL.toString()
					norcPv2Batch.adminDateMonth = c.ADMIN_DATE_MONTH.toString()
					norcPv2Batch.adminDateMonthCodes = c.ADMIN_DATE_MONTH_Codes.toString()
					norcPv2Batch.adminDateDay = c.ADMIN_DATE_DAY.toString()
					norcPv2Batch.adminDateDayCodes = c.ADMIN_DATE_DAY_Codes.toString()
					norcPv2Batch.adminDateYear = c.ADMIN_DATE_YEAR.toString()
					norcPv2Batch.adminDateYearCodes = c.ADMIN_DATE_YEAR_Codes.toString()
					norcPv2Batch.hospNights = c.HOSP_NIGHTS.toString()
					norcPv2Batch.hospNightsCodes = c.HOSP_NIGHTS_Codes.toString()
					norcPv2Batch.diagnose = c.DIAGNOSE.toString()
					norcPv2Batch.diagnose201 = c.DIAGNOSE_2_01.toString()
					norcPv2Batch.diagnose202 = c.DIAGNOSE_2_02.toString()
					norcPv2Batch.diagnose203 = c.DIAGNOSE_2_03.toString()
					norcPv2Batch.diagnose204 = c.DIAGNOSE_2_04.toString()
					norcPv2Batch.diagnose205 = c.DIAGNOSE_2_05.toString()
					norcPv2Batch.diagnose206 = c.DIAGNOSE_2_06.toString()
					norcPv2Batch.diagnose207 = c.DIAGNOSE_2_07.toString()
					norcPv2Batch.diagnose208 = c.DIAGNOSE_2_08.toString()
					norcPv2Batch.diagnose209 = c.DIAGNOSE_2_09.toString()
					norcPv2Batch.diagnosisOth = c.DIAGNOSIS_OTH.toString()
					norcPv2Batch.diagnosisOthCodes = c.DIAGNOSIS_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_5.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp5 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp5 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_5: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Input: ${c.TIME_STAMP_5.toString()}'
						println '! parse TIME_STAMP_5 Exception: ${e.toString()}'
					}
					norcPv2Batch.recentMove = c.RECENT_MOVE.toString()
					norcPv2Batch.ownHome = c.OWN_HOME.toString()
					norcPv2Batch.ownHomeOth = c.OWN_HOME_OTH.toString()
					norcPv2Batch.ownHomeOthCodes = c.OWN_HOME_OTH_Codes.toString()
					norcPv2Batch.ageHome = c.AGE_HOME.toString()
					norcPv2Batch.lengthReside = c.LENGTH_RESIDE.toString()
					norcPv2Batch.lengthResideCodes = c.LENGTH_RESIDE_Codes.toString()
					norcPv2Batch.lengthResideUnit = c.LENGTH_RESIDE_UNIT.toString()
					norcPv2Batch.mainHeat = c.MAIN_HEAT.toString()
					norcPv2Batch.mainHeatOth = c.MAIN_HEAT_OTH.toString()
					norcPv2Batch.mainHeatOthCodes = c.MAIN_HEAT_OTH_Codes.toString()
					norcPv2Batch.heat201 = c.HEAT2_01.toString()
					norcPv2Batch.heat202 = c.HEAT2_02.toString()
					norcPv2Batch.heat203 = c.HEAT2_03.toString()
					norcPv2Batch.heat204 = c.HEAT2_04.toString()
					norcPv2Batch.heat205 = c.HEAT2_05.toString()
					norcPv2Batch.heat206 = c.HEAT2_06.toString()
					norcPv2Batch.heat207 = c.HEAT2_07.toString()
					norcPv2Batch.heat208 = c.HEAT2_08.toString()
					norcPv2Batch.heat209 = c.HEAT2_09.toString()
					norcPv2Batch.heat210 = c.HEAT2_10.toString()
					norcPv2Batch.heat211 = c.HEAT2_11.toString()
					norcPv2Batch.heat212 = c.HEAT2_12.toString()
					norcPv2Batch.heat2Oth = c.HEAT2_OTH.toString()
					norcPv2Batch.heat2OthCodes = c.HEAT2_OTH_Codes.toString()
					norcPv2Batch.cooling = c.COOLING.toString()
					norcPv2Batch.cool01 = c.COOL_01.toString()
					norcPv2Batch.cool02 = c.COOL_02.toString()
					norcPv2Batch.cool03 = c.COOL_03.toString()
					norcPv2Batch.cool04 = c.COOL_04.toString()
					norcPv2Batch.cool05 = c.COOL_05.toString()
					norcPv2Batch.cool06 = c.COOL_06.toString()
					norcPv2Batch.cool07 = c.COOL_07.toString()
					norcPv2Batch.coolOth = c.COOL_OTH.toString()
					norcPv2Batch.coolOthCodes = c.COOL_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_6.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp6 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp6 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_6: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Input: ${c.TIME_STAMP_6.toString()}'
						println '! parse TIME_STAMP_6 Exception: ${e.toString()}'
					}
					norcPv2Batch.waterDrink = c.WATER_DRINK.toString()
					norcPv2Batch.waterDrinkOth = c.WATER_DRINK_OTH.toString()
					norcPv2Batch.waterDrinkOthCodes = c.WATER_DRINK_OTH_Codes.toString()
					norcPv2Batch.waterCook = c.WATER_COOK.toString()
					norcPv2Batch.waterCookOth = c.WATER_COOK_OTH.toString()
					norcPv2Batch.waterCookOthCodes = c.WATER_COOK_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_7.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp7 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp7 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_7: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Input: ${c.TIME_STAMP_7.toString()}'
						println '! parse TIME_STAMP_7 Exception: ${e.toString()}'
					}
					norcPv2Batch.water = c.WATER.toString()
					norcPv2Batch.mold = c.MOLD.toString()
					norcPv2Batch.roomMold01 = c.ROOM_MOLD_01.toString()
					norcPv2Batch.roomMold02 = c.ROOM_MOLD_02.toString()
					norcPv2Batch.roomMold03 = c.ROOM_MOLD_03.toString()
					norcPv2Batch.roomMold04 = c.ROOM_MOLD_04.toString()
					norcPv2Batch.roomMold05 = c.ROOM_MOLD_05.toString()
					norcPv2Batch.roomMold06 = c.ROOM_MOLD_06.toString()
					norcPv2Batch.roomMold07 = c.ROOM_MOLD_07.toString()
					norcPv2Batch.roomMold08 = c.ROOM_MOLD_08.toString()
					norcPv2Batch.roomMold09 = c.ROOM_MOLD_09.toString()
					norcPv2Batch.roomMold10 = c.ROOM_MOLD_10.toString()
					norcPv2Batch.roomMoldOth = c.ROOM_MOLD_OTH.toString()
					norcPv2Batch.roomMoldOthCodes = c.ROOM_MOLD_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_8.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp8 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp8 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_8: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Input: ${c.TIME_STAMP_8.toString()}'
						println '! parse TIME_STAMP_8 Exception: ${e.toString()}'
					}
					norcPv2Batch.prenovate2 = c.PRENOVATE2.toString()
					norcPv2Batch.prenovate2Room01 = c.PRENOVATE2_ROOM_01.toString()
					norcPv2Batch.prenovate2Room02 = c.PRENOVATE2_ROOM_02.toString()
					norcPv2Batch.prenovate2Room03 = c.PRENOVATE2_ROOM_03.toString()
					norcPv2Batch.prenovate2Room04 = c.PRENOVATE2_ROOM_04.toString()
					norcPv2Batch.prenovate2Room05 = c.PRENOVATE2_ROOM_05.toString()
					norcPv2Batch.prenovate2Room06 = c.PRENOVATE2_ROOM_06.toString()
					norcPv2Batch.prenovate2Room07 = c.PRENOVATE2_ROOM_07.toString()
					norcPv2Batch.prenovate2Room08 = c.PRENOVATE2_ROOM_08.toString()
					norcPv2Batch.prenovate2Room09 = c.PRENOVATE2_ROOM_09.toString()
					norcPv2Batch.prenovate2Room10 = c.PRENOVATE2_ROOM_10.toString()
					norcPv2Batch.prenovate2RoomOth = c.PRENOVATE2_ROOM_OTH.toString()
					norcPv2Batch.prenovate2RoomOthCodes = c.PRENOVATE2_ROOM_OTH_Codes.toString()
					norcPv2Batch.pdecorate2 = c.PDECORATE2.toString()
					norcPv2Batch.pdecorate2Room01 = c.PDECORATE2_ROOM_01.toString()
					norcPv2Batch.pdecorate2Room02 = c.PDECORATE2_ROOM_02.toString()
					norcPv2Batch.pdecorate2Room03 = c.PDECORATE2_ROOM_03.toString()
					norcPv2Batch.pdecorate2Room04 = c.PDECORATE2_ROOM_04.toString()
					norcPv2Batch.pdecorate2Room05 = c.PDECORATE2_ROOM_05.toString()
					norcPv2Batch.pdecorate2Room06 = c.PDECORATE2_ROOM_06.toString()
					norcPv2Batch.pdecorate2Room07 = c.PDECORATE2_ROOM_07.toString()
					norcPv2Batch.pdecorate2Room08 = c.PDECORATE2_ROOM_08.toString()
					norcPv2Batch.pdecorate2Room09 = c.PDECORATE2_ROOM_09.toString()
					norcPv2Batch.pdecorate2Room10 = c.PDECORATE2_ROOM_10.toString()
					norcPv2Batch.pdecorate2RoomOth = c.PDECORATE2_ROOM_OTH.toString()
					norcPv2Batch.pdecorate2RoomOthCodes = c.PDECORATE2_ROOM_OTH_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_9.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp9 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp9 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_9: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Input: ${c.TIME_STAMP_9.toString()}'
						println '! parse TIME_STAMP_9 Exception: ${e.toString()}'
					}
					norcPv2Batch.working = c.WORKING.toString()
					norcPv2Batch.hours = c.HOURS.toString()
					norcPv2Batch.hoursCodes = c.HOURS_Codes.toString()
					norcPv2Batch.shiftWork = c.SHIFT_WORK.toString()
					try {
						dateTimeString = c.TIME_STAMP_10.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp10 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp10 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_10: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Input: ${c.TIME_STAMP_10.toString()}'
						println '! parse TIME_STAMP_10 Exception: ${e.toString()}'
					}
					norcPv2Batch.listen = c.LISTEN.toString()
					norcPv2Batch.advice = c.ADVICE.toString()
					norcPv2Batch.affection = c.AFFECTION.toString()
					norcPv2Batch.dailyHelp = c.DAILY_HELP.toString()
					norcPv2Batch.emotSupport = c.EMOT_SUPPORT.toString()
					norcPv2Batch.amtSupport = c.AMT_SUPPORT.toString()
					try {
						dateTimeString = c.TIME_STAMP_11.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp11 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp11 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_11: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Input: ${c.TIME_STAMP_11.toString()}'
						println '! parse TIME_STAMP_11 Exception: ${e.toString()}'
					}
					norcPv2Batch.insure = c.INSURE.toString()
					norcPv2Batch.insEmploy = c.INS_EMPLOY.toString()
					norcPv2Batch.insMedicaid = c.INS_MEDICAID.toString()
					norcPv2Batch.insTricare = c.INS_TRICARE.toString()
					norcPv2Batch.insIhs = c.INS_IHS.toString()
					norcPv2Batch.insMedicare = c.INS_MEDICARE.toString()
					norcPv2Batch.insOth = c.INS_OTH.toString()
					try {
						dateTimeString = c.TIME_STAMP_12.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp12 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp12 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_12: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Input: ${c.TIME_STAMP_12.toString()}'
						println '! parse TIME_STAMP_12 Exception: ${e.toString()}'
					}
					norcPv2Batch.commEmail = c.COMM_EMAIL.toString()
					norcPv2Batch.emailtxt = c.emailtxt.toString()
					norcPv2Batch.haveEmail = c.HAVE_EMAIL.toString()
					norcPv2Batch.email2 = c.EMAIL_2.toString()
					norcPv2Batch.email3 = c.EMAIL_3.toString()
					norcPv2Batch.email = c.EMAIL.toString()
					norcPv2Batch.emailCodes = c.EMAIL_Codes.toString()
					norcPv2Batch.commCell = c.COMM_CELL.toString()
					norcPv2Batch.celltxt = c.celltxt.toString()
					norcPv2Batch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcPv2Batch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcPv2Batch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcPv2Batch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcPv2Batch.cellPhone = c.CELL_PHONE.toString()
					norcPv2Batch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_13.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp13 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp13 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_13: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Input: ${c.TIME_STAMP_13.toString()}'
						println '! parse TIME_STAMP_13 Exception: ${e.toString()}'
					}
					norcPv2Batch.commContact = c.COMM_CONTACT.toString()
					norcPv2Batch.conttxt = c.conttxt.toString()
					norcPv2Batch.contact1 = c.CONTACT_1.toString()
					norcPv2Batch.contactFname1 = c.CONTACT_FNAME_1.toString()
					norcPv2Batch.contactFname1Codes = c.CONTACT_FNAME_1_Codes.toString()
					norcPv2Batch.contactLname1 = c.CONTACT_LNAME_1.toString()
					norcPv2Batch.contactLname1Codes = c.CONTACT_LNAME_1_Codes.toString()
					norcPv2Batch.contactRelate1 = c.CONTACT_RELATE_1.toString()
					norcPv2Batch.contactRelate1Oth = c.CONTACT_RELATE1_OTH.toString()
					norcPv2Batch.contactRelate1OthCodes = c.CONTACT_RELATE1_OTH_Codes.toString()
					norcPv2Batch.cAddr11 = c.C_ADDR1_1.toString()
					norcPv2Batch.cAddr11Codes = c.C_ADDR1_1_Codes.toString()
					norcPv2Batch.cAddr21 = c.C_ADDR2_1.toString()
					norcPv2Batch.cAddr21Codes = c.C_ADDR2_1_Codes.toString()
					norcPv2Batch.cUnit1 = c.C_UNIT_1.toString()
					norcPv2Batch.cUnit1Codes = c.C_UNIT_1_Codes.toString()
					norcPv2Batch.cCity1 = c.C_CITY_1.toString()
					norcPv2Batch.cCity1Codes = c.C_CITY_1_Codes.toString()
					norcPv2Batch.cState1 = c.C_STATE_1.toString()
					norcPv2Batch.cZipcode1 = c.C_ZIPCODE_1.toString()
					norcPv2Batch.cZipcode1Codes = c.C_ZIPCODE_1_Codes.toString()
					norcPv2Batch.cZip41 = c.C_ZIP4_1.toString()
					norcPv2Batch.cZip41Codes = c.C_ZIP4_1_Codes.toString()
					norcPv2Batch.contactPhone1 = c.CONTACT_PHONE_1.toString()
					norcPv2Batch.contactPhone1Codes = c.CONTACT_PHONE_1_Codes.toString()
					norcPv2Batch.contact2 = c.CONTACT_2.toString()
					norcPv2Batch.contactFname2 = c.CONTACT_FNAME_2.toString()
					norcPv2Batch.contactFname2Codes = c.CONTACT_FNAME_2_Codes.toString()
					norcPv2Batch.contactLname2 = c.CONTACT_LNAME_2.toString()
					norcPv2Batch.contactLname2Codes = c.CONTACT_LNAME_2_Codes.toString()
					norcPv2Batch.contactRelate2 = c.CONTACT_RELATE_2.toString()
					norcPv2Batch.contactRelate2Oth = c.CONTACT_RELATE2_OTH.toString()
					norcPv2Batch.contactRelate2OthCodes = c.CONTACT_RELATE2_OTH_Codes.toString()
					norcPv2Batch.cAddr12 = c.C_ADDR1_2.toString()
					norcPv2Batch.cAddr12Codes = c.C_ADDR1_2_Codes.toString()
					norcPv2Batch.cAddr22 = c.C_ADDR2_2.toString()
					norcPv2Batch.cAddr22Codes = c.C_ADDR2_2_Codes.toString()
					norcPv2Batch.cUnit2 = c.C_UNIT_2.toString()
					norcPv2Batch.cUnit2Codes = c.C_UNIT_2_Codes.toString()
					norcPv2Batch.cCity2 = c.C_CITY_2.toString()
					norcPv2Batch.cCity2Codes = c.C_CITY_2_Codes.toString()
					norcPv2Batch.cState2 = c.C_STATE_2.toString()
					norcPv2Batch.cZipcode2 = c.C_ZIPCODE_2.toString()
					norcPv2Batch.cZipcode2Codes = c.C_ZIPCODE_2_Codes.toString()
					norcPv2Batch.cZip42 = c.C_ZIP4_2.toString()
					norcPv2Batch.cZip42Codes = c.C_ZIP4_2_Codes.toString()
					norcPv2Batch.contactPhone2 = c.CONTACT_PHONE_2.toString()
					norcPv2Batch.contactPhone2Codes = c.CONTACT_PHONE_2_Codes.toString()
					try {
						dateTimeString = c.TIME_STAMP_14.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp14 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp14 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_14: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Input: ${c.TIME_STAMP_14.toString()}'
						println '! parse TIME_STAMP_14 Exception: ${e.toString()}'
					}
					try {
						dateTimeString = c.TIME_STAMP_15.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp15 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp15 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_15: ${c.TIME_STAMP_15.toString()}'
						println '! parse TIME_STAMP_15 Input: ${c.TIME_STAMP_15.toString()}'
						println '! parse TIME_STAMP_15 Exception: ${e.toString()}'
					}
					norcPv2Batch.contactLocation = c.CONTACT_LOCATION.toString()
					norcPv2Batch.contactLocationOth = c.CONTACT_LOCATION_OTH.toString()
					norcPv2Batch.saqagree = c.SAQAGREE.toString()
					norcPv2Batch.saqint2 = c.SAQINT2.toString()
					norcPv2Batch.saqint3 = c.SAQINT3.toString()
					norcPv2Batch.learn = c.LEARN.toString()
					norcPv2Batch.help = c.HELP.toString()
					norcPv2Batch.incent = c.INCENT.toString()
					norcPv2Batch.research = c.RESEARCH.toString()
					norcPv2Batch.envir = c.ENVIR.toString()
					norcPv2Batch.community = c.COMMUNITY.toString()
					norcPv2Batch.knowOthers = c.KNOW_OTHERS.toString()
					norcPv2Batch.family = c.FAMILY.toString()
					norcPv2Batch.doctor = c.DOCTOR.toString()
					norcPv2Batch.staff = c.STAFF.toString()
					norcPv2Batch.opinSpouse = c.OPIN_SPOUSE.toString()
					norcPv2Batch.opinFamily = c.OPIN_FAMILY.toString()
					norcPv2Batch.opinFriend = c.OPIN_FRIEND.toString()
					norcPv2Batch.opinDr = c.OPIN_DR.toString()
					norcPv2Batch.experience = c.EXPERIENCE.toString()
					norcPv2Batch.improve = c.IMPROVE.toString()
					norcPv2Batch.intLength = c.INT_LENGTH.toString()
					norcPv2Batch.intStress = c.INT_STRESS.toString()
					norcPv2Batch.intRepeat = c.INT_REPEAT.toString()
					norcPv2Batch.passwrd2 = c.PASSWRD2.toString()
					try {
						dateTimeString = c.TIME_STAMP_16.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.timeStamp16 = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.timeStamp16 = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TIME_STAMP_16: ${c.TIME_STAMP_16.toString()}'
						println '! parse TIME_STAMP_16 Input: ${c.TIME_STAMP_16.toString()}'
						println '! parse TIME_STAMP_16 Exception: ${e.toString()}'
					}
					norcPv2Batch.elapsedtime = c.ElapsedTime.toString()
					try {
						dateTimeString = c.TempTimeVariable.toString()
						if ( ! dateTimeString ) {
							norcPv2Batch.temptimevariable = null
						} else {
							DateTime dt = fmt.parseDateTime(dateTimeString)
							norcPv2Batch.temptimevariable = dt.toCalendar().getTime()
						}
					} catch (Exception e) {
						response << '! Invalid TempTimeVariable: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Input: ${c.TempTimeVariable.toString()}'
						println '! parse TempTimeVariable Exception: ${e.toString()}'
					}
					
					// Save the record
					if (norcPv2Batch.hasErrors()) {
						response << "! norcPv2Batch has errors.\n"
					} else if (norcPv2Batch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcPv2Batch record with Respondent Serial ${norcPv2Batch.suId}"
						norcPv2Batch.errors.each{ e ->
							// println "norcPv2Batch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcPv2Batch in ${diff} seconds"
		}
		// end PV2_BATCH
	}

	// Start COMBO_BATCH
	def parseComboBatch(table, response) {
		def now = new Date()
		// println "ready to load. ${now}"
				
		def saveCount = 0
		if (debug){
			println "Parsing COMBO_BATCH1"
		}
		// Save data in batches
		def startTime = System.nanoTime()
		//table?.COMBO_BATCH1?.each{ c ->
		GParsPool.withPool {
			table?.COMBO_BATCH1?.eachParallel { c ->
				
				//def affiliate = c.AFFILIATE.toString()
				//def institutionName = c.INSTITUTION.toString()
				//println "${affiliate}: ${institutionName}"
				NorcComboBatch.withTransaction {
					def dateTimeString = ""
					def checkSerial = c.Respondent_Serial.toString()
					
					// Verify that the current respondent serial has not already been saved
					def norcComboBatch = NorcComboBatch.findByRespondentSerial(checkSerial)
		
					if (!norcComboBatch) {
						norcComboBatch = new NorcComboBatch()
						response <<  " + Creating new NorcComboBatch(${checkSerial})\n"
					} else {
						// Lock object for update
						norcComboBatch.lock()
						response <<  " ~ Updating existing NorcComboBatch(${checkSerial})\n"
					}
					
					norcComboBatch.respondentSerial = c.Respondent_Serial.toString()
					norcComboBatch.respondentSerialSourcefile = c.Respondent_Serial_SourceFile.toString()
					norcComboBatch.respondentOrigin01 = c.Respondent_Origin_01.toString()
					norcComboBatch.respondentOrigin02 = c.Respondent_Origin_02.toString()
					norcComboBatch.respondentOrigin03 = c.Respondent_Origin_03.toString()
					norcComboBatch.respondentOrigin04 = c.Respondent_Origin_04.toString()
					norcComboBatch.respondentOrigin05 = c.Respondent_Origin_05.toString()
					norcComboBatch.respondentOrigin06 = c.Respondent_Origin_06.toString()
					norcComboBatch.respondentOriginOther = c.Respondent_Origin_Other.toString()
					norcComboBatch.respondentId = c.Respondent_ID.toString()
					norcComboBatch.datacollectionStatus01 = c.DataCollection_Status_01.toString()
					norcComboBatch.datacollectionStatus02 = c.DataCollection_Status_02.toString()
					norcComboBatch.datacollectionStatus03 = c.DataCollection_Status_03.toString()
					norcComboBatch.datacollectionStatus04 = c.DataCollection_Status_04.toString()
					norcComboBatch.datacollectionStatus05 = c.DataCollection_Status_05.toString()
					norcComboBatch.datacollectionStatus06 = c.DataCollection_Status_06.toString()
					norcComboBatch.datacollectionStatus07 = c.DataCollection_Status_07.toString()
					norcComboBatch.datacollectionStatus08 = c.DataCollection_Status_08.toString()
					norcComboBatch.datacollectionStatus09 = c.DataCollection_Status_09.toString()
					norcComboBatch.datacollectionInterviewerid = c.DataCollection_InterviewerID.toString()
					norcComboBatch.datacollectionStarttime = c.DataCollection_StartTime.toString()
					norcComboBatch.datacollectionFinishtime = c.DataCollection_FinishTime.toString()
					norcComboBatch.datacmtadatavrssinnumbr2 = c.DataCMtadataVrssinNumbr2.toString()
					norcComboBatch.datacMtadatavrvrsinguid3 = c.DataC_MtadataVrVrsinGUID3.toString()
					norcComboBatch.datactinRngcntxt4 = c.DataCtin_RngCntxt4.toString()
					norcComboBatch.datacollectionVariant = c.DataCollection_Variant.toString()
					norcComboBatch.datacollectionEndquestion = c.DataCollection_EndQuestion.toString()
					norcComboBatch.datacollectionTerminatesignal = c.DataCollection_TerminateSignal.toString()
					norcComboBatch.datacollectionSeedvalue = c.DataCollection_SeedValue.toString()
					norcComboBatch.datacollectionInterviewengine = c.DataCollection_InterviewEngine.toString()
					norcComboBatch.datacollectionCurrentpage = c.DataCollection_CurrentPage.toString()
					norcComboBatch.datacollectionDebug = c.DataCollection_Debug.toString()
					norcComboBatch.datacollectionServertimezone = c.DataCollection_ServerTimeZone.toString()
					norcComboBatch.datacIntrviwrtrtimzn5 = c.DataC_IntrviwrTrTimZn5.toString()
					norcComboBatch.datacnRsndnttitimzn6 = c.DataCn_RsndntTiTimZn6.toString()
					norcComboBatch.datacollectionBatchid = c.DataCollection_BatchID.toString()
					norcComboBatch.datacollectionBatchname = c.DataCollection_BatchName.toString()
					norcComboBatch.datactinDaentrymd7 = c.DataCtin_DaEntryMd7.toString()
					norcComboBatch.datacollectionRemoved = c.DataCollection_Removed.toString()
					norcComboBatch.datacleaningNote = c.DataCleaning_Note.toString()
					norcComboBatch.datacleaningStatus01 = c.DataCleaning_Status_01.toString()
					norcComboBatch.datacleaningStatus02 = c.DataCleaning_Status_02.toString()
					norcComboBatch.datacleaningReviewstatus01 = c.DataCleaning_ReviewStatus_01.toString()
					norcComboBatch.datacleaningReviewstatus02 = c.DataCleaning_ReviewStatus_02.toString()
					norcComboBatch.datacleaningReviewstatus03 = c.DataCleaning_ReviewStatus_03.toString()
					norcComboBatch.datacleaningReviewstatus04 = c.DataCleaning_ReviewStatus_04.toString()
					norcComboBatch.suId = c.SU_ID.toString()
					norcComboBatch.majority = c.MAJORITY.toString()
					norcComboBatch.prFname = c.PR_FNAME.toString()
					norcComboBatch.prFnameCodes = c.PR_FNAME_Codes.toString()
					norcComboBatch.prLname = c.PR_LNAME.toString()
					norcComboBatch.prLnameCodes = c.PR_LNAME_Codes.toString()
					norcComboBatch.method = c.METHOD.toString()
					norcComboBatch.hilosamp = c.HILOSAMP.toString()
					norcComboBatch.rGender = c.R_GENDER.toString()
					norcComboBatch.rGenderCodes = c.R_GENDER_Codes.toString()
					norcComboBatch.scId = c.SC_ID.toString()
					norcComboBatch.scIdCodes = c.SC_ID_Codes.toString()
					norcComboBatch.scName = c.SC_NAME.toString()
					norcComboBatch.scNameCodes = c.SC_NAME_Codes.toString()
					norcComboBatch.sc800num = c.SC_800NUM.toString()
					norcComboBatch.prAddress1 = c.PR_ADDRESS_1.toString()
					norcComboBatch.prAddress1Codes = c.PR_ADDRESS_1_Codes.toString()
					norcComboBatch.prAddress2 = c.PR_ADDRESS_2.toString()
					norcComboBatch.prAddress2Codes = c.PR_ADDRESS_2_Codes.toString()
					norcComboBatch.prUnit = c.PR_UNIT.toString()
					norcComboBatch.prUnitCodes = c.PR_UNIT_Codes.toString()
					norcComboBatch.prCity = c.PR_CITY.toString()
					norcComboBatch.prCityCodes = c.PR_CITY_Codes.toString()
					norcComboBatch.prState = c.PR_STATE.toString()
					norcComboBatch.prStateCodes = c.PR_STATE_Codes.toString()
					norcComboBatch.prZip = c.PR_ZIP.toString()
					norcComboBatch.prZipCodes = c.PR_ZIP_Codes.toString()
					norcComboBatch.prZip4 = c.PR_ZIP4.toString()
					norcComboBatch.prZip4Codes = c.PR_ZIP4_Codes.toString()
					norcComboBatch.tsuId = c.TSU_ID.toString()
					norcComboBatch.tsuIdCodes = c.TSU_ID_Codes.toString()
					norcComboBatch.psuId = c.PSU_ID.toString()
					norcComboBatch.psuIdCodes = c.PSU_ID_Codes.toString()
					norcComboBatch.incentive = c.INCENTIVE.toString()
					norcComboBatch.incentivetx = c.INCENTIVETX.toString()
					norcComboBatch.psTimeStamp1 = c.PS_TIME_STAMP_1.toString()
					norcComboBatch.mode = c.MODE.toString()
					norcComboBatch.quexlang = c.QUEXLANG.toString()
					norcComboBatch.calltype = c.CALLTYPE.toString()
					norcComboBatch.psTimeStamp2 = c.PS_TIME_STAMP_2.toString()
					norcComboBatch.sameday = c.SAMEDAY.toString()
					norcComboBatch.sameresp = c.SAMERESP.toString()
					norcComboBatch.female1 = c.FEMALE_1.toString()
					norcComboBatch.giftcard = c.giftcard.toString()
					norcComboBatch.rNameRFname = c.R_NAME_R_FNAME.toString()
					norcComboBatch.rNameRFnameCodes = c.R_NAME_R_FNAME_Codes.toString()
					norcComboBatch.rNameRLname = c.R_NAME_R_LNAME.toString()
					norcComboBatch.rNameRLnameCodes = c.R_NAME_R_LNAME_Codes.toString()
					norcComboBatch.currmo = c.CURRMO.toString()
					norcComboBatch.currdy = c.CURRDY.toString()
					norcComboBatch.curryr = c.CURRYR.toString()
					norcComboBatch.personDobMonth = c.PERSON_DOB_MONTH.toString()
					norcComboBatch.personDobMonthCodes = c.PERSON_DOB_MONTH_Codes.toString()
					norcComboBatch.personDobDay = c.PERSON_DOB_DAY.toString()
					norcComboBatch.personDobDayCodes = c.PERSON_DOB_DAY_Codes.toString()
					norcComboBatch.personDobYear = c.PERSON_DOB_YEAR.toString()
					norcComboBatch.personDobYearCodes = c.PERSON_DOB_YEAR_Codes.toString()
					norcComboBatch.age = c.AGE.toString()
					norcComboBatch.ageCodes = c.AGE_Codes.toString()
					norcComboBatch.ageRange = c.AGE_RANGE.toString()
					norcComboBatch.ageElig = c.AGE_ELIG.toString()
					norcComboBatch.ppgFirst = c.PPG_FIRST.toString()
					norcComboBatch.psTimeStamp4 = c.PS_TIME_STAMP_4.toString()
					norcComboBatch.addressDkrefaddr = c.ADDRESS_DKREFADDR.toString()
					norcComboBatch.addressAddress1 = c.ADDRESS_ADDRESS_1.toString()
					norcComboBatch.addressAddress1Codes = c.ADDRESS_ADDRESS_1_Codes.toString()
					norcComboBatch.addressAddress2 = c.ADDRESS_ADDRESS_2.toString()
					norcComboBatch.addressAddress2Codes = c.ADDRESS_ADDRESS_2_Codes.toString()
					norcComboBatch.addressUnit = c.ADDRESS_UNIT.toString()
					norcComboBatch.addressUnitCodes = c.ADDRESS_UNIT_Codes.toString()
					norcComboBatch.addressCity = c.ADDRESS_CITY.toString()
					norcComboBatch.addressCityCodes = c.ADDRESS_CITY_Codes.toString()
					norcComboBatch.addressState = c.ADDRESS_STATE.toString()
					norcComboBatch.addressZip = c.ADDRESS_ZIP.toString()
					norcComboBatch.addressZipCodes = c.ADDRESS_ZIP_Codes.toString()
					norcComboBatch.addressZip4 = c.ADDRESS_ZIP4.toString()
					norcComboBatch.addressZip4Codes = c.ADDRESS_ZIP4_Codes.toString()
					norcComboBatch.duEligConfirm = c.DU_ELIG_CONFIRM.toString()
					norcComboBatch.psTimeStamp5 = c.PS_TIME_STAMP_5.toString()
					norcComboBatch.knowNcs = c.KNOW_NCS.toString()
					norcComboBatch.howKnowNcs01 = c.HOW_KNOW_NCS_01.toString()
					norcComboBatch.howKnowNcs02 = c.HOW_KNOW_NCS_02.toString()
					norcComboBatch.howKnowNcs03 = c.HOW_KNOW_NCS_03.toString()
					norcComboBatch.howKnowNcs04 = c.HOW_KNOW_NCS_04.toString()
					norcComboBatch.howKnowNcs05 = c.HOW_KNOW_NCS_05.toString()
					norcComboBatch.howKnowNcs06 = c.HOW_KNOW_NCS_06.toString()
					norcComboBatch.howKnowNcs07 = c.HOW_KNOW_NCS_07.toString()
					norcComboBatch.howKnowNcs08 = c.HOW_KNOW_NCS_08.toString()
					norcComboBatch.howKnowNcs09 = c.HOW_KNOW_NCS_09.toString()
					norcComboBatch.howKnowNcs10 = c.HOW_KNOW_NCS_10.toString()
					norcComboBatch.howKnowNcs11 = c.HOW_KNOW_NCS_11.toString()
					norcComboBatch.howKnowNcs12 = c.HOW_KNOW_NCS_12.toString()
					norcComboBatch.howKnowNcs13 = c.HOW_KNOW_NCS_13.toString()
					norcComboBatch.howKnowNcs14 = c.HOW_KNOW_NCS_14.toString()
					norcComboBatch.howKnowNcs15 = c.HOW_KNOW_NCS_15.toString()
					norcComboBatch.howKnowNcs16 = c.HOW_KNOW_NCS_16.toString()
					norcComboBatch.howKnowNcs17 = c.HOW_KNOW_NCS_17.toString()
					norcComboBatch.howKnowNcs18 = c.HOW_KNOW_NCS_18.toString()
					norcComboBatch.howKnowNcs19 = c.HOW_KNOW_NCS_19.toString()
					norcComboBatch.howKnowNcs20 = c.HOW_KNOW_NCS_20.toString()
					norcComboBatch.howKnowNcs21 = c.HOW_KNOW_NCS_21.toString()
					norcComboBatch.howKnowNcs22 = c.HOW_KNOW_NCS_22.toString()
					norcComboBatch.howKnowNcs23 = c.HOW_KNOW_NCS_23.toString()
					norcComboBatch.howKnowNcsOth = c.HOW_KNOW_NCS_OTH.toString()
					norcComboBatch.howKnowNcsOthCodes = c.HOW_KNOW_NCS_OTH_Codes.toString()
					norcComboBatch.psTimeStamp6 = c.PS_TIME_STAMP_6.toString()
					norcComboBatch.pregnant = c.PREGNANT.toString()
					norcComboBatch.origDueDateMonth = c.ORIG_DUE_DATE_MONTH.toString()
					norcComboBatch.origDueDateMonthCodes = c.ORIG_DUE_DATE_MONTH_Codes.toString()
					norcComboBatch.origDueDateDay = c.ORIG_DUE_DATE_DAY.toString()
					norcComboBatch.origDueDateDayCodes = c.ORIG_DUE_DATE_DAY_Codes.toString()
					norcComboBatch.origDueDateYear = c.ORIG_DUE_DATE_YEAR.toString()
					norcComboBatch.origDueDateYearCodes = c.ORIG_DUE_DATE_YEAR_Codes.toString()
					norcComboBatch.datePeriodMonth = c.DATE_PERIOD_MONTH.toString()
					norcComboBatch.datePeriodMonthCodes = c.DATE_PERIOD_MONTH_Codes.toString()
					norcComboBatch.datePeriodDay = c.DATE_PERIOD_DAY.toString()
					norcComboBatch.datePeriodDayCodes = c.DATE_PERIOD_DAY_Codes.toString()
					norcComboBatch.datePeriodYear = c.DATE_PERIOD_YEAR.toString()
					norcComboBatch.datePeriodYearCodes = c.DATE_PERIOD_YEAR_Codes.toString()
					norcComboBatch.weeksPreg = c.WEEKS_PREG.toString()
					norcComboBatch.weeksPregCodes = c.WEEKS_PREG_Codes.toString()
					norcComboBatch.monthPreg = c.MONTH_PREG.toString()
					norcComboBatch.monthPregCodes = c.MONTH_PREG_Codes.toString()
					norcComboBatch.trimester = c.TRIMESTER.toString()
					norcComboBatch.psTimeStamp7 = c.PS_TIME_STAMP_7.toString()
					norcComboBatch.trying = c.TRYING.toString()
					norcComboBatch.hyster = c.HYSTER.toString()
					norcComboBatch.ovaries = c.OVARIES.toString()
					norcComboBatch.tubesTied = c.TUBES_TIED.toString()
					norcComboBatch.menopause = c.MENOPAUSE.toString()
					norcComboBatch.medUnable = c.MED_UNABLE.toString()
					norcComboBatch.medUnableOth = c.MED_UNABLE_OTH.toString()
					norcComboBatch.medUnableOthCodes = c.MED_UNABLE_OTH_Codes.toString()
					norcComboBatch.psTimeStamp8 = c.PS_TIME_STAMP_8.toString()
					norcComboBatch.maristat = c.MARISTAT.toString()
					norcComboBatch.educ = c.EDUC.toString()
					norcComboBatch.employ = c.EMPLOY.toString()
					norcComboBatch.employOth = c.EMPLOY_OTH.toString()
					norcComboBatch.employOthCodes = c.EMPLOY_OTH_Codes.toString()
					norcComboBatch.ethnicity = c.ETHNICITY.toString()
					norcComboBatch.race01 = c.RACE_01.toString()
					norcComboBatch.race02 = c.RACE_02.toString()
					norcComboBatch.race03 = c.RACE_03.toString()
					norcComboBatch.race04 = c.RACE_04.toString()
					norcComboBatch.race05 = c.RACE_05.toString()
					norcComboBatch.race06 = c.RACE_06.toString()
					norcComboBatch.race07 = c.RACE_07.toString()
					norcComboBatch.race08 = c.RACE_08.toString()
					norcComboBatch.raceOth = c.RACE_OTH.toString()
					norcComboBatch.raceOthCodes = c.RACE_OTH_Codes.toString()
					norcComboBatch.personLang = c.PERSON_LANG.toString()
					norcComboBatch.personLangOth = c.PERSON_LANG_OTH.toString()
					norcComboBatch.personLangOthCodes = c.PERSON_LANG_OTH_Codes.toString()
					norcComboBatch.psTimeStamp9 = c.PS_TIME_STAMP_9.toString()
					norcComboBatch.hhMembers = c.HH_MEMBERS.toString()
					norcComboBatch.hhMembersCodes = c.HH_MEMBERS_Codes.toString()
					norcComboBatch.numChild = c.NUM_CHILD.toString()
					norcComboBatch.numChildCodes = c.NUM_CHILD_Codes.toString()
					norcComboBatch.income = c.INCOME.toString()
					norcComboBatch.psTimeStamp10 = c.PS_TIME_STAMP_10.toString()
					norcComboBatch.phoneNbr = c.PHONE_NBR.toString()
					norcComboBatch.phoneNbrCodes = c.PHONE_NBR_Codes.toString()
					norcComboBatch.phoneType = c.PHONE_TYPE.toString()
					norcComboBatch.phoneTypeOth = c.PHONE_TYPE_OTH.toString()
					norcComboBatch.phoneTypeOthCodes = c.PHONE_TYPE_OTH_Codes.toString()
					norcComboBatch.homePhone = c.HOME_PHONE.toString()
					norcComboBatch.homePhoneCodes = c.HOME_PHONE_Codes.toString()
					norcComboBatch.cellPhone1 = c.CELL_PHONE_1.toString()
					norcComboBatch.cellPhone2 = c.CELL_PHONE_2.toString()
					norcComboBatch.cellPhone3 = c.CELL_PHONE_3.toString()
					norcComboBatch.cellPhone4 = c.CELL_PHONE_4.toString()
					norcComboBatch.cellPhone = c.CELL_PHONE.toString()
					norcComboBatch.cellPhoneCodes = c.CELL_PHONE_Codes.toString()
					norcComboBatch.noPhNum = c.NO_PH_NUM.toString()
					norcComboBatch.sameAddr = c.SAME_ADDR.toString()
					norcComboBatch.mailaddrMailAddress1 = c.MAILADDR_MAIL_ADDRESS1.toString()
					norcComboBatch.maiadmaiAress1Cds8 = c.MAIADMAI_ARESS1_Cds8.toString()
					norcComboBatch.mailaddrMailAddress2 = c.MAILADDR_MAIL_ADDRESS2.toString()
					norcComboBatch.maiadmaiAress2Cds9 = c.MAIADMAI_ARESS2_Cds9.toString()
					norcComboBatch.mailaddrMailUnit = c.MAILADDR_MAIL_UNIT.toString()
					norcComboBatch.mailaddrMailUnitCodes = c.MAILADDR_MAIL_UNIT_Codes.toString()
					norcComboBatch.mailaddrMailCity = c.MAILADDR_MAIL_CITY.toString()
					norcComboBatch.mailaddrMailCityCodes = c.MAILADDR_MAIL_CITY_Codes.toString()
					norcComboBatch.mailaddrMailState = c.MAILADDR_MAIL_STATE.toString()
					norcComboBatch.mailaddrMailZip = c.MAILADDR_MAIL_ZIP.toString()
					norcComboBatch.mailaddrMailZipCodes = c.MAILADDR_MAIL_ZIP_Codes.toString()
					norcComboBatch.mailaddrMailZip4 = c.MAILADDR_MAIL_ZIP4.toString()
					norcComboBatch.mailaddrMailZip4Codes = c.MAILADDR_MAIL_ZIP4_Codes.toString()
					norcComboBatch.haveEmail = c.HAVE_EMAIL.toString()
					norcComboBatch.email = c.EMAIL.toString()
					norcComboBatch.emailCodes = c.EMAIL_Codes.toString()
					norcComboBatch.emailType = c.EMAIL_TYPE.toString()
					norcComboBatch.emailShare = c.EMAIL_SHARE.toString()
					norcComboBatch.planMove = c.PLAN_MOVE.toString()
					norcComboBatch.whereMove = c.WHERE_MOVE.toString()
					norcComboBatch.moveInfo = c.MOVE_INFO.toString()
					norcComboBatch.newaddrNewAddress1 = c.NEWADDR_NEW_ADDRESS1.toString()
					norcComboBatch.newaddrNewAddress1Codes = c.NEWADDR_NEW_ADDRESS1_Codes.toString()
					norcComboBatch.newaddrNewAddress2 = c.NEWADDR_NEW_ADDRESS2.toString()
					norcComboBatch.newaddrNewAddress2Codes = c.NEWADDR_NEW_ADDRESS2_Codes.toString()
					norcComboBatch.newaddrNewUnit = c.NEWADDR_NEW_UNIT.toString()
					norcComboBatch.newaddrNewUnitCodes = c.NEWADDR_NEW_UNIT_Codes.toString()
					norcComboBatch.newaddrNewCity = c.NEWADDR_NEW_CITY.toString()
					norcComboBatch.newaddrNewCityCodes = c.NEWADDR_NEW_CITY_Codes.toString()
					norcComboBatch.newaddrNewState = c.NEWADDR_NEW_STATE.toString()
					norcComboBatch.newaddrNewZip = c.NEWADDR_NEW_ZIP.toString()
					norcComboBatch.newaddrNewZipCodes = c.NEWADDR_NEW_ZIP_Codes.toString()
					norcComboBatch.newaddrNewZip4 = c.NEWADDR_NEW_ZIP4.toString()
					norcComboBatch.newaddrNewZip4Codes = c.NEWADDR_NEW_ZIP4_Codes.toString()
					norcComboBatch.whenMove = c.WHEN_MOVE.toString()
					norcComboBatch.dateMoveMonth = c.DATE_MOVE_MONTH.toString()
					norcComboBatch.dateMoveMonthCodes = c.DATE_MOVE_MONTH_Codes.toString()
					norcComboBatch.dateMoveYear = c.DATE_MOVE_YEAR.toString()
					norcComboBatch.dateMoveYearCodes = c.DATE_MOVE_YEAR_Codes.toString()
					norcComboBatch.incentiveRequest = c.INCENTIVE_REQUEST.toString()
					norcComboBatch.incentiveChoice = c.INCENTIVE_CHOICE.toString()
					norcComboBatch.psTimeStamp11 = c.PS_TIME_STAMP_11.toString()
					norcComboBatch.psTimeStamp12 = c.PS_TIME_STAMP_12.toString()
					norcComboBatch.femaleEnd1a = c.FEMALE_END_1A.toString()
					norcComboBatch.femaleEnd4 = c.FEMALE_END_4.toString()
					norcComboBatch.noInterest = c.NO_INTEREST_.toString()
					norcComboBatch.psTimeStamp13 = c.PS_TIME_STAMP_13.toString()
					norcComboBatch.psTimeStamp14 = c.PS_TIME_STAMP_14.toString()
					norcComboBatch.lcTimeStamp1 = c.LC_TIME_STAMP_1.toString()
					norcComboBatch.quexver = c.quexver.toString()
					norcComboBatch.lcTimeStamp2 = c.LC_TIME_STAMP_2.toString()
					norcComboBatch.cn004c = c.CN004C.toString()
					norcComboBatch.lcTimeStamp2a = c.LC_TIME_STAMP_2A.toString()
					norcComboBatch.consentComments = c.CONSENT_COMMENTS.toString()
					norcComboBatch.cn006 = c.CN006.toString()
					norcComboBatch.cn006a = c.CN006A.toString()
					norcComboBatch.lcTimeStamp3 = c.LC_TIME_STAMP_3.toString()
					norcComboBatch.consentDate = c.CONSENT_DATE.toString()
					norcComboBatch.cn007a = c.CN007A.toString()
					norcComboBatch.lcTimeStamp4 = c.LC_TIME_STAMP_4.toString()
					norcComboBatch.lqTimeStamp1 = c.LQ_TIME_STAMP_1.toString()
					norcComboBatch.lqTimeStamp2 = c.LQ_TIME_STAMP_2.toString()
					norcComboBatch.ps002txt = c.ps002txt.toString()
					norcComboBatch.pregnantloq = c.PREGNANTLOQ.toString()
					norcComboBatch.ppgStatus = c.PPG_STATUS.toString()
					norcComboBatch.lqTimeStamp3 = c.LQ_TIME_STAMP_3.toString()
					norcComboBatch.dueDateMonth = c.DUE_DATE_MONTH.toString()
					norcComboBatch.dueDateMonthCodes = c.DUE_DATE_MONTH_Codes.toString()
					norcComboBatch.dueDateDay = c.DUE_DATE_DAY.toString()
					norcComboBatch.dueDateDayCodes = c.DUE_DATE_DAY_Codes.toString()
					norcComboBatch.dueDateYear = c.DUE_DATE_YEAR.toString()
					norcComboBatch.dueDateYearCodes = c.DUE_DATE_YEAR_Codes.toString()
					norcComboBatch.knowDate = c.KNOW_DATE.toString()
					norcComboBatch.datePeriodloqMonth = c.DATE_PERIODLOQ_MONTH.toString()
					norcComboBatch.datePeriodloqMonthCodes = c.DATE_PERIODLOQ_MONTH_Codes.toString()
					norcComboBatch.datePeriodloqDay = c.DATE_PERIODLOQ_DAY.toString()
					norcComboBatch.datePeriodloqDayCodes = c.DATE_PERIODLOQ_DAY_Codes.toString()
					norcComboBatch.datePeriodloqYear = c.DATE_PERIODLOQ_YEAR.toString()
					norcComboBatch.datePeriodloqYearCodes = c.DATE_PERIODLOQ_YEAR_Codes.toString()
					norcComboBatch.knewDate = c.KNEW_DATE.toString()
					norcComboBatch.lqTimeStamp4 = c.LQ_TIME_STAMP_4.toString()
					norcComboBatch.homeTest = c.HOME_TEST.toString()
					norcComboBatch.brthtxt = c.brthtxt.toString()
					norcComboBatch.birthPlan = c.BIRTH_PLAN.toString()
					norcComboBatch.birthplaceBirthPlace = c.BirthPlace_BIRTH_PLACE.toString()
					norcComboBatch.birthcBirPaceCds10 = c.Birthc_BIR_PACE_Cds10.toString()
					norcComboBatch.birthplaceBAddress1 = c.BirthPlace_B_ADDRESS_1.toString()
					norcComboBatch.birthcBAress1Cds11 = c.Birthc_B_ARESS_1_Cds11.toString()
					norcComboBatch.birthplaceBAddress2 = c.BirthPlace_B_ADDRESS_2.toString()
					norcComboBatch.birthcBAress2Cds12 = c.Birthc_B_ARESS_2_Cds12.toString()
					norcComboBatch.birthplaceBCity = c.BirthPlace_B_CITY.toString()
					norcComboBatch.birthplaceBCityCodes = c.BirthPlace_B_CITY_Codes.toString()
					norcComboBatch.birthplaceBState = c.BirthPlace_B_STATE.toString()
					norcComboBatch.birthplaceBZipcode = c.BirthPlace_B_ZIPCODE.toString()
					norcComboBatch.birthplaceBZipcodeCodes = c.BirthPlace_B_ZIPCODE_Codes.toString()
					norcComboBatch.pnVitamin = c.PN_VITAMIN.toString()
					norcComboBatch.pregVitamin = c.PREG_VITAMIN.toString()
					norcComboBatch.dateVisitMonth = c.DATE_VISIT_MONTH.toString()
					norcComboBatch.dateVisitMonthCodes = c.DATE_VISIT_MONTH_Codes.toString()
					norcComboBatch.dateVisitDay = c.DATE_VISIT_DAY.toString()
					norcComboBatch.dateVisitDayCodes = c.DATE_VISIT_DAY_Codes.toString()
					norcComboBatch.dateVisitYear = c.DATE_VISIT_YEAR.toString()
					norcComboBatch.dateVisitYearCodes = c.DATE_VISIT_YEAR_Codes.toString()
					norcComboBatch.diabtxt = c.diabtxt.toString()
					norcComboBatch.diabtxt2 = c.diabtxt2.toString()
					norcComboBatch.diabetes1 = c.DIABETES_1.toString()
					norcComboBatch.highbpPreg = c.HIGHBP_PREG.toString()
					norcComboBatch.urine = c.URINE.toString()
					norcComboBatch.preeclamp = c.PREECLAMP.toString()
					norcComboBatch.earlyLabor = c.EARLY_LABOR.toString()
					norcComboBatch.anemia = c.ANEMIA.toString()
					norcComboBatch.nausea = c.NAUSEA.toString()
					norcComboBatch.kidney = c.KIDNEY.toString()
					norcComboBatch.rhDisease = c.RH_DISEASE.toString()
					norcComboBatch.groupB = c.GROUP_B.toString()
					norcComboBatch.herpes = c.HERPES.toString()
					norcComboBatch.vaginosis = c.VAGINOSIS.toString()
					norcComboBatch.othCondition = c.OTH_CONDITION.toString()
					norcComboBatch.conditionOth = c.CONDITION_OTH.toString()
					norcComboBatch.conditionOthCodes = c.CONDITION_OTH_Codes.toString()
					norcComboBatch.lqTimeStamp5 = c.LQ_TIME_STAMP_5.toString()
					norcComboBatch.healthtxt = c.healthtxt.toString()
					norcComboBatch.health = c.HEALTH.toString()
					norcComboBatch.heightHeightFt = c.Height_HEIGHT_FT.toString()
					norcComboBatch.heightHeightFtCodes = c.Height_HEIGHT_FT_Codes.toString()
					norcComboBatch.heightHtInch = c.Height_HT_INCH.toString()
					norcComboBatch.heightHtInchCodes = c.Height_HT_INCH_Codes.toString()
					norcComboBatch.weight = c.WEIGHT.toString()
					norcComboBatch.weightCodes = c.WEIGHT_Codes.toString()
					norcComboBatch.asthma = c.ASTHMA.toString()
					norcComboBatch.highbpNotpreg = c.HIGHBP_NOTPREG.toString()
					norcComboBatch.diabetesNotpreg = c.DIABETES_NOTPREG.toString()
					norcComboBatch.diabetes2 = c.DIABETES_2.toString()
					norcComboBatch.diabetes3 = c.DIABETES_3.toString()
					norcComboBatch.thyroid1 = c.THYROID_1.toString()
					norcComboBatch.thyroid2 = c.THYROID_2.toString()
					norcComboBatch.hlthCare = c.HLTH_CARE.toString()
					norcComboBatch.lqTimeStamp6 = c.LQ_TIME_STAMP_6.toString()
					norcComboBatch.insure = c.INSURE.toString()
					norcComboBatch.insEmploy = c.INS_EMPLOY.toString()
					norcComboBatch.insMedicaid = c.INS_MEDICAID.toString()
					norcComboBatch.insTricare = c.INS_TRICARE.toString()
					norcComboBatch.insIhs = c.INS_IHS.toString()
					norcComboBatch.insMedicare = c.INS_MEDICARE.toString()
					norcComboBatch.insOth = c.INS_OTH.toString()
					norcComboBatch.lqTimeStamp7 = c.LQ_TIME_STAMP_7.toString()
					norcComboBatch.ageHome = c.AGE_HOME.toString()
					norcComboBatch.mainHeat = c.MAIN_HEAT.toString()
					norcComboBatch.mainHeatOth = c.MAIN_HEAT_OTH.toString()
					norcComboBatch.mainHeatOthCodes = c.MAIN_HEAT_OTH_Codes.toString()
					norcComboBatch.cool01 = c.COOL_01.toString()
					norcComboBatch.cool02 = c.COOL_02.toString()
					norcComboBatch.cool03 = c.COOL_03.toString()
					norcComboBatch.cool04 = c.COOL_04.toString()
					norcComboBatch.cool05 = c.COOL_05.toString()
					norcComboBatch.cool06 = c.COOL_06.toString()
					norcComboBatch.cool07 = c.COOL_07.toString()
					norcComboBatch.coolOth = c.COOL_OTH.toString()
					norcComboBatch.coolOthCodes = c.COOL_OTH_Codes.toString()
					norcComboBatch.waterDrink = c.WATER_DRINK.toString()
					norcComboBatch.waterDrinkOth = c.WATER_DRINK_OTH.toString()
					norcComboBatch.waterDrinkOthCodes = c.WATER_DRINK_OTH_Codes.toString()
					norcComboBatch.lqTimeStamp8 = c.LQ_TIME_STAMP_8.toString()
					norcComboBatch.cigNow = c.CIG_NOW.toString()
					norcComboBatch.cigNowFreq = c.CIG_NOW_FREQ.toString()
					norcComboBatch.cigNowNum = c.CIG_NOW_NUM.toString()
					norcComboBatch.cigNowNumCodes = c.CIG_NOW_NUM_Codes.toString()
					norcComboBatch.cigNowNumField = c.CIG_NOW_NUM_FIELD.toString()
					norcComboBatch.drinkNow = c.DRINK_NOW.toString()
					norcComboBatch.drinkNowNum = c.DRINK_NOW_NUM.toString()
					norcComboBatch.drinkNowNumCodes = c.DRINK_NOW_NUM_Codes.toString()
					norcComboBatch.drinkNow5 = c.DRINK_NOW_5.toString()
					norcComboBatch.lqTimeStamp9 = c.LQ_TIME_STAMP_9.toString()
					norcComboBatch.learn = c.LEARN.toString()
					norcComboBatch.help = c.HELP.toString()
					norcComboBatch.incent = c.INCENT.toString()
					norcComboBatch.research = c.RESEARCH.toString()
					norcComboBatch.envir = c.ENVIR.toString()
					norcComboBatch.community = c.COMMUNITY.toString()
					norcComboBatch.knowOthers = c.KNOW_OTHERS.toString()
					norcComboBatch.family = c.FAMILY.toString()
					norcComboBatch.doctor = c.DOCTOR.toString()
					norcComboBatch.opinSpouse = c.OPIN_SPOUSE.toString()
					norcComboBatch.opinFamily = c.OPIN_FAMILY.toString()
					norcComboBatch.opinFriend = c.OPIN_FRIEND.toString()
					norcComboBatch.opinDr = c.OPIN_DR.toString()
					norcComboBatch.experience = c.EXPERIENCE.toString()
					norcComboBatch.improve = c.IMPROVE.toString()
					norcComboBatch.intLength = c.INT_LENGTH.toString()
					norcComboBatch.intStress = c.INT_STRESS.toString()
					norcComboBatch.intRepeat = c.INT_REPEAT.toString()
					norcComboBatch.lqTimeStamp11 = c.LQ_TIME_STAMP_11.toString()
					norcComboBatch.end11txt = c.END1_1txt.toString()
					norcComboBatch.lqTimeStamp12 = c.LQ_TIME_STAMP_12.toString()
					norcComboBatch.elapsedtime = c.ElapsedTime.toString()
					norcComboBatch.temptimevariable = c.TempTimeVariable.toString()
										
					// Save the record
					if (norcComboBatch.hasErrors()) {
						response << "! norcComboBatch has errors.\n"
					} else if (norcComboBatch.save()) {
						saveCount++
					}
					else {
						// println "Error saving norcComboBatch record with Respondent Serial ${norcComboBatch.suId}"
						norcComboBatch.errors.each{ e ->
							// println "norcComboBatch:error::${e}"
							e.fieldErrors.each{ fe ->
								response <<  "! Rejected '${fe.rejectedValue}' for field '${fe.field}'\n"
								// println "! Rejected '${fe.rejectedValue}' for field '${fe.field}'"
							}
						}
					}
				}
			}
		}
		def endTime = System.nanoTime()
		def diff = (endTime - startTime)/1000000000
		if (debug) {
			println "    Done! ${saveCount} records saved to norcPv2Batch in ${diff} seconds"
		}
		// end COMBO_BATCH
	}
}
