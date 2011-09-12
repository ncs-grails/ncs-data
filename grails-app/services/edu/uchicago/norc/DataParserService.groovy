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
		parseHhBatch table, response
		parseLowConsentBatch table, response
		parseLowQuex1Batch table, response
		parseScreenerBatch table, response
		parseNonResponseList table, response
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
}
