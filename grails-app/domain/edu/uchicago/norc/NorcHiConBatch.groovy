package edu.uchicago.norc

class NorcHiConBatch {

	String respondentSerial
	String respondentSerialSourcefile
	String respondentOrigin01
	String respondentOrigin02
	String respondentOrigin03
	String respondentOrigin04
	String respondentOrigin05
	String respondentOrigin06
	String respondentOriginOther
	String respondentId
	String datacollectionStatus01
	String datacollectionStatus02
	String datacollectionStatus03
	String datacollectionStatus04
	String datacollectionStatus05
	String datacollectionStatus06
	String datacollectionStatus07
	String datacollectionStatus08
	String datacollectionStatus09
	String datacollectionInterviewerid
	Date datacollectionStarttime
	Date datacollectionFinishtime
	String datacmtadatavrssinnumbr2
	String datacMtadatavrvrsinguid3
	String datactinRngcntxt4
	String datacollectionVariant
	String datacollectionEndquestion
	String datacollectionTerminatesignal
	String datacollectionSeedvalue
	String datacollectionInterviewengine
	String datacollectionCurrentpage
	String datacollectionDebug
	String datacollectionServertimezone
	String datacIntrviwrtrtimzn5
	String datacnRsndnttitimzn6
	String datacollectionBatchid
	String datacollectionBatchname
	String datactinDaentrymd7
	String datacollectionRemoved
	String datacleaningNote
	String datacleaningStatus01
	String datacleaningStatus02
	String datacleaningReviewstatus01
	String datacleaningReviewstatus02
	String datacleaningReviewstatus03
	String datacleaningReviewstatus04
	String suId
	String prFname
	String prLname
	String dobMo
	String dobMoCodes
	String dobDy
	String dobDyCodes
	String dobYr
	String dobYrCodes
	String agecalc
	String ageRange
	String ppgFirst
	String ppgStatus
	String currmo
	String currdy
	String curryr
	Date timeStamp1
	String quexlang
	String visWhoConsented
	String cn003txt
	Date timeStamp2
	Date timeStamp2a
	String discussBook
	Date timeStamp2b
	String cn005text
	String consentType1
	String consentType2
	String consentType3
	String consentType4
	String consentGiven1
	String consentGiven2
	String consentGiven3
	String consentGiven4
	String consentGiven5
	String consentGiven6
	String consentComments
	String cn008txt
	Date timeStamp3
	Date consentDate
	String english
	String contactLang
	String contactLangOth
	String contactLangOthCodes
	String interpret
	String contactInterpret
	String contactInterpretOth
	String contactLocation
	String contactLocationOth
	String disp
	Date timeStamp4
	String elapsedtime
	Date temptimevariable

    static constraints = {
		
		respondentSerial(nullable:true,maxSize:75)
		respondentSerialSourcefile(nullable:true,maxSize:75)
		respondentOrigin01(nullable:true,maxSize:75)
		respondentOrigin02(nullable:true,maxSize:75)
		respondentOrigin03(nullable:true,maxSize:75)
		respondentOrigin04(nullable:true,maxSize:75)
		respondentOrigin05(nullable:true,maxSize:75)
		respondentOrigin06(nullable:true,maxSize:75)
		respondentOriginOther(nullable:true,maxSize:75)
		respondentId(nullable:true,maxSize:75)
		datacollectionStatus01(nullable:true,maxSize:75)
		datacollectionStatus02(nullable:true,maxSize:75)
		datacollectionStatus03(nullable:true,maxSize:75)
		datacollectionStatus04(nullable:true,maxSize:75)
		datacollectionStatus05(nullable:true,maxSize:75)
		datacollectionStatus06(nullable:true,maxSize:75)
		datacollectionStatus07(nullable:true,maxSize:75)
		datacollectionStatus08(nullable:true,maxSize:75)
		datacollectionStatus09(nullable:true,maxSize:75)
		datacollectionInterviewerid(nullable:true,maxSize:75)
		datacollectionStarttime(nullable:true)
		datacollectionFinishtime(nullable:true)
		datacmtadatavrssinnumbr2(nullable:true,maxSize:75)
		datacMtadatavrvrsinguid3(nullable:true,maxSize:75)
		datactinRngcntxt4(nullable:true,maxSize:75)
		datacollectionVariant(nullable:true,maxSize:75)
		datacollectionEndquestion(nullable:true,maxSize:75)
		datacollectionTerminatesignal(nullable:true,maxSize:75)
		datacollectionSeedvalue(nullable:true,maxSize:75)
		datacollectionInterviewengine(nullable:true,maxSize:75)
		datacollectionCurrentpage(nullable:true,maxSize:75)
		datacollectionDebug(nullable:true,maxSize:75)
		datacollectionServertimezone(nullable:true,maxSize:75)
		datacIntrviwrtrtimzn5(nullable:true,maxSize:75)
		datacnRsndnttitimzn6(nullable:true,maxSize:75)
		datacollectionBatchid(nullable:true,maxSize:75)
		datacollectionBatchname(nullable:true,maxSize:75)
		datactinDaentrymd7(nullable:true,maxSize:75)
		datacollectionRemoved(nullable:true,maxSize:75)
		datacleaningNote(nullable:true,maxSize:75)
		datacleaningStatus01(nullable:true,maxSize:75)
		datacleaningStatus02(nullable:true,maxSize:75)
		datacleaningReviewstatus01(nullable:true,maxSize:75)
		datacleaningReviewstatus02(nullable:true,maxSize:75)
		datacleaningReviewstatus03(nullable:true,maxSize:75)
		datacleaningReviewstatus04(nullable:true,maxSize:75)
		suId(nullable:true,maxSize:75)
		prFname(nullable:true,maxSize:75)
		prLname(nullable:true,maxSize:75)
		dobMo(nullable:true,maxSize:75)
		dobMoCodes(nullable:true,maxSize:75)
		dobDy(nullable:true,maxSize:75)
		dobDyCodes(nullable:true,maxSize:75)
		dobYr(nullable:true,maxSize:75)
		dobYrCodes(nullable:true,maxSize:75)
		agecalc(nullable:true,maxSize:75)
		ageRange(nullable:true,maxSize:75)
		ppgFirst(nullable:true,maxSize:75)
		ppgStatus(nullable:true,maxSize:75)
		currmo(nullable:true,maxSize:75)
		currdy(nullable:true,maxSize:75)
		curryr(nullable:true,maxSize:75)
		timeStamp1(nullable:true)
		quexlang(nullable:true,maxSize:75)
		visWhoConsented(nullable:true,maxSize:75)
		cn003txt(nullable:true,maxSize:75)
		timeStamp2(nullable:true)
		timeStamp2a(nullable:true)
		discussBook(nullable:true,maxSize:75)
		timeStamp2b(nullable:true)
		cn005text(nullable:true,maxSize:75)
		consentType1(nullable:true,maxSize:75)
		consentType2(nullable:true,maxSize:75)
		consentType3(nullable:true,maxSize:75)
		consentType4(nullable:true,maxSize:75)
		consentGiven1(nullable:true,maxSize:75)
		consentGiven2(nullable:true,maxSize:75)
		consentGiven3(nullable:true,maxSize:75)
		consentGiven4(nullable:true,maxSize:75)
		consentGiven5(nullable:true,maxSize:75)
		consentGiven6(nullable:true,maxSize:75)
		consentComments(nullable:true,maxSize:75)
		cn008txt(nullable:true,maxSize:75)
		timeStamp3(nullable:true)
		consentDate(nullable:true)
		english(nullable:true,maxSize:75)
		contactLang(nullable:true,maxSize:75)
		contactLangOth(nullable:true,maxSize:75)
		contactLangOthCodes(nullable:true,maxSize:75)
		interpret(nullable:true,maxSize:75)
		contactInterpret(nullable:true,maxSize:75)
		contactInterpretOth(nullable:true,maxSize:75)
		contactLocation(nullable:true,maxSize:75)
		contactLocationOth(nullable:true,maxSize:75)
		disp(nullable:true,maxSize:75)
		timeStamp4(nullable:true)
		elapsedtime(nullable:true,maxSize:75)
		temptimevariable(nullable:true)
    }
}
