package edu.uchicago.norc

class NorcLowQuex1Batch {
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
	String majority
	String affiliate
	String prFname
	String prFnameCodes
	String prLname
	String prLnameCodes
	String tsuId
	String tsuIdCodes
	String prPpgStatus
	Date timeStamp1
	String currmo
	String currdy
	String curryr
	String quexlang
	String calltype
	String female1
	String bestTtcBestTtc1
	String bestTtcBestTtc1Codes
	String bestTtcBestTtc2
	String bestTtc3
	String phone
	String phoneNbr
	String phoneNbrCodes
	Date timeStamp2
	String ps002txt
	String pregnant
	String ppgStatus
	Date timeStamp3
	String lossInfo
	String dueDateMonth
	String dueDateMonthCodes
	String dueDateDay
	String dueDateDayCodes
	String dueDateYear
	String dueDateYearCodes
	String knowDate
	String datePeriodMonth
	String datePeriodMonthCodes
	String datePeriodDay
	String datePeriodDayCodes
	String datePeriodYear
	String datePeriodYearCodes
	String knewDate
	Date timeStamp4
	String homeTest
	String brthtxt
	String birthPlan
	String birthplaceBirthPlace
	String birthcBirPaceCds8
	String birthplaceBAddress1
	String birthcBAress1Cds9
	String birthplaceBAddress2
	String birthcBAress2Cds10
	String birthplaceBCity
	String birthplaceBCityCodes
	String birthplaceBState
	String birthplaceBZipcode
	String birthplaceBZipcodeCodes
	String pnVitamin
	String pregVitamin
	String dateVisitMonth
	String dateVisitMonthCodes
	String dateVisitDay
	String dateVisitDayCodes
	String dateVisitYear
	String dateVisitYearCodes
	String diabtxt
	String diabtxt2
	String diabetes1
	String highbpPreg
	String urine
	String preeclamp
	String earlyLabor
	String anemia
	String nausea
	String kidney
	String rhDisease
	String groupB
	String herpes
	String vaginosis
	String othCondition
	String conditionOth
	String conditionOthCodes
	Date timeStamp5
	String healthtxt
	String health
	String heightHeightFt
	String heightHeightFtCodes
	String heightHtInch
	String heightHtInchCodes
	String weight
	String weightCodes
	String asthma
	String highbpNotpreg
	String diabetesNotpreg
	String diabetes2
	String diabetes3
	String thyroid1
	String thyroid2
	String hlthCare
	Date timeStamp6
	String insure
	String insEmploy
	String insMedicaid
	String insTricare
	String insIhs
	String insMedicare
	String insOth
	Date timeStamp7
	String ageHome
	String mainHeat
	String mainHeatOth
	String mainHeatOthCodes
	String cool01
	String cool02
	String cool03
	String cool04
	String cool05
	String cool06
	String cool07
	String coolOth
	String coolOthCodes
	String waterDrink
	String waterDrinkOth
	String waterDrinkOthCodes
	Date timeStamp8
	String cigNow
	String cigNowFreq
	String cigNowNum
	String cigNowNumCodes
	String cigNowNumField
	String drinkNow
	String drinkNowNum
	String drinkNowNumCodes
	String drinkNow5
	Date timeStamp9
	String learn
	String help
	String incent
	String research
	String envir
	String community
	String knowOthers
	String family
	String doctor
	String opinSpouse
	String opinFamily
	String opinFriend
	String opinDr
	String experience
	String improve
	String intLength
	String intStress
	String intRepeat
	Date timeStamp11
	String end11txt
	Date timeStamp12
	String elapsedtime
	Date temptimevariable
	
    static constraints = {
		respondentSerial(nullable:true)
		respondentSerialSourcefile(nullable:true)
		respondentOrigin01(nullable:true)
		respondentOrigin02(nullable:true)
		respondentOrigin03(nullable:true)
		respondentOrigin04(nullable:true)
		respondentOrigin05(nullable:true)
		respondentOrigin06(nullable:true)
		respondentOriginOther(nullable:true)
		respondentId(nullable:true)
		datacollectionStatus01(nullable:true)
		datacollectionStatus02(nullable:true)
		datacollectionStatus03(nullable:true)
		datacollectionStatus04(nullable:true)
		datacollectionStatus05(nullable:true)
		datacollectionStatus06(nullable:true)
		datacollectionStatus07(nullable:true)
		datacollectionStatus08(nullable:true)
		datacollectionStatus09(nullable:true)
		datacollectionInterviewerid(nullable:true)
		datacollectionStarttime(nullable:true)
		datacollectionFinishtime(nullable:true)
		datacmtadatavrssinnumbr2(nullable:true)
		datacMtadatavrvrsinguid3(nullable:true)
		datactinRngcntxt4(nullable:true)
		datacollectionVariant(nullable:true)
		datacollectionEndquestion(nullable:true)
		datacollectionTerminatesignal(nullable:true)
		datacollectionSeedvalue(nullable:true)
		datacollectionInterviewengine(nullable:true)
		datacollectionCurrentpage(nullable:true)
		datacollectionDebug(nullable:true)
		datacollectionServertimezone(nullable:true)
		datacIntrviwrtrtimzn5(nullable:true)
		datacnRsndnttitimzn6(nullable:true)
		datacollectionBatchid(nullable:true)
		datacollectionBatchname(nullable:true)
		datactinDaentrymd7(nullable:true)
		datacollectionRemoved(nullable:true)
		datacleaningNote(nullable:true)
		datacleaningStatus01(nullable:true)
		datacleaningStatus02(nullable:true)
		datacleaningReviewstatus01(nullable:true)
		datacleaningReviewstatus02(nullable:true)
		datacleaningReviewstatus03(nullable:true)
		datacleaningReviewstatus04(nullable:true)
		suId(nullable:true)
		majority(nullable:true)
		affiliate(nullable:true)
		prFname(nullable:true)
		prFnameCodes(nullable:true)
		prLname(nullable:true)
		prLnameCodes(nullable:true)
		tsuId(nullable:true)
		tsuIdCodes(nullable:true)
		prPpgStatus(nullable:true)
		timeStamp1(nullable:true)
		currmo(nullable:true)
		currdy(nullable:true)
		curryr(nullable:true)
		quexlang(nullable:true)
		calltype(nullable:true)
		female1(nullable:true)
		bestTtcBestTtc1(nullable:true)
		bestTtcBestTtc1Codes(nullable:true)
		bestTtcBestTtc2(nullable:true)
		bestTtc3(nullable:true)
		phone(nullable:true)
		phoneNbr(nullable:true)
		phoneNbrCodes(nullable:true)
		timeStamp2(nullable:true)
		ps002txt(nullable:true)
		pregnant(nullable:true)
		ppgStatus(nullable:true)
		timeStamp3(nullable:true)
		lossInfo(nullable:true)
		dueDateMonth(nullable:true)
		dueDateMonthCodes(nullable:true)
		dueDateDay(nullable:true)
		dueDateDayCodes(nullable:true)
		dueDateYear(nullable:true)
		dueDateYearCodes(nullable:true)
		knowDate(nullable:true)
		datePeriodMonth(nullable:true)
		datePeriodMonthCodes(nullable:true)
		datePeriodDay(nullable:true)
		datePeriodDayCodes(nullable:true)
		datePeriodYear(nullable:true)
		datePeriodYearCodes(nullable:true)
		knewDate(nullable:true)
		timeStamp4(nullable:true)
		homeTest(nullable:true)
		brthtxt(nullable:true)
		birthPlan(nullable:true)
		birthplaceBirthPlace(nullable:true)
		birthcBirPaceCds8(nullable:true)
		birthplaceBAddress1(nullable:true)
		birthcBAress1Cds9(nullable:true)
		birthplaceBAddress2(nullable:true)
		birthcBAress2Cds10(nullable:true)
		birthplaceBCity(nullable:true)
		birthplaceBCityCodes(nullable:true)
		birthplaceBState(nullable:true)
		birthplaceBZipcode(nullable:true)
		birthplaceBZipcodeCodes(nullable:true)
		pnVitamin(nullable:true)
		pregVitamin(nullable:true)
		dateVisitMonth(nullable:true)
		dateVisitMonthCodes(nullable:true)
		dateVisitDay(nullable:true)
		dateVisitDayCodes(nullable:true)
		dateVisitYear(nullable:true)
		dateVisitYearCodes(nullable:true)
		diabtxt(nullable:true)
		diabtxt2(nullable:true)
		diabetes1(nullable:true)
		highbpPreg(nullable:true)
		urine(nullable:true)
		preeclamp(nullable:true)
		earlyLabor(nullable:true)
		anemia(nullable:true)
		nausea(nullable:true)
		kidney(nullable:true)
		rhDisease(nullable:true)
		groupB(nullable:true)
		herpes(nullable:true)
		vaginosis(nullable:true)
		othCondition(nullable:true)
		conditionOth(nullable:true)
		conditionOthCodes(nullable:true)
		timeStamp5(nullable:true)
		healthtxt(nullable:true)
		health(nullable:true)
		heightHeightFt(nullable:true)
		heightHeightFtCodes(nullable:true)
		heightHtInch(nullable:true)
		heightHtInchCodes(nullable:true)
		weight(nullable:true)
		weightCodes(nullable:true)
		asthma(nullable:true)
		highbpNotpreg(nullable:true)
		diabetesNotpreg(nullable:true)
		diabetes2(nullable:true)
		diabetes3(nullable:true)
		thyroid1(nullable:true)
		thyroid2(nullable:true)
		hlthCare(nullable:true)
		timeStamp6(nullable:true)
		insure(nullable:true)
		insEmploy(nullable:true)
		insMedicaid(nullable:true)
		insTricare(nullable:true)
		insIhs(nullable:true)
		insMedicare(nullable:true)
		insOth(nullable:true)
		timeStamp7(nullable:true)
		ageHome(nullable:true)
		mainHeat(nullable:true)
		mainHeatOth(nullable:true)
		mainHeatOthCodes(nullable:true)
		cool01(nullable:true)
		cool02(nullable:true)
		cool03(nullable:true)
		cool04(nullable:true)
		cool05(nullable:true)
		cool06(nullable:true)
		cool07(nullable:true)
		coolOth(nullable:true)
		coolOthCodes(nullable:true)
		waterDrink(nullable:true)
		waterDrinkOth(nullable:true)
		waterDrinkOthCodes(nullable:true)
		timeStamp8(nullable:true)
		cigNow(nullable:true)
		cigNowFreq(nullable:true)
		cigNowNum(nullable:true)
		cigNowNumCodes(nullable:true)
		cigNowNumField(nullable:true)
		drinkNow(nullable:true)
		drinkNowNum(nullable:true)
		drinkNowNumCodes(nullable:true)
		drinkNow5(nullable:true)
		timeStamp9(nullable:true)
		learn(nullable:true)
		help(nullable:true)
		incent(nullable:true)
		research(nullable:true)
		envir(nullable:true)
		community(nullable:true)
		knowOthers(nullable:true)
		family(nullable:true)
		doctor(nullable:true)
		opinSpouse(nullable:true)
		opinFamily(nullable:true)
		opinFriend(nullable:true)
		opinDr(nullable:true)
		experience(nullable:true)
		improve(nullable:true)
		intLength(nullable:true)
		intStress(nullable:true)
		intRepeat(nullable:true)
		timeStamp11(nullable:true)
		end11txt(nullable:true)
		timeStamp12(nullable:true)
		elapsedtime(nullable:true)
		temptimevariable(nullable:true)
    }

}
