package onboarding

import io.bloco.faker.Faker
import macro.Onboarding
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.util.*
import kotlin.random.Random
import kotlin.test.Test

class NoPermissions  : UITest() {

    private var emailInstall = "intestergrams+globaladmin@gmail.com"
    private var pwordInstall = "password123$"
    private var pinInstall = "123123"
    private val useUpdate = false
    private val hasMinimumDeposit = false
    private val broker = "2"

    @Test
    @DisplayName("Allow Permission Notif and Record Video")
    @Order(1)
    fun noPermissions() {

        scenario {
            case(1) {
                expectation {


//                   it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)


                    //create test account info
                    val faker = Faker()
                    var firstName = faker.name.firstName()
                    val middleName = faker.name.firstName()
                    var lastName = faker.name.lastName()

                    //Remove ' in last name for less flaky test
                    firstName = firstName.replace("'", "")
                    lastName = lastName.replace("'", "")
                    firstName = firstName.lowercase().replaceFirstChar { it.uppercase() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercase() }

                    val internationalCountryCode = Onboarding.getRandomCountryCode()
                    val useInternational = false
                    val mobileNUmber = Onboarding.generateRandomPHNumber()
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"
                    val birthDay = Onboarding.getDateSixYearsFromNow()

                    //Application Form
                    val gender = Onboarding.getRandomGender()
                    val civilStatus = Onboarding.getRandomCivilStatus()
                    val birthPlace = "United States"
                    val birthCity = faker.address.city()

                    var citizenship: String

                    do {
                        citizenship = Onboarding.getRandomCountry()
                    } while (citizenship == "Philippines")

                    var randomDualCitizenship: String

                    //Must not be the same
                    do {
                        randomDualCitizenship = Onboarding.getRandomCountry()
                    } while (citizenship == randomDualCitizenship)

                    val readPolicy = false


//                    it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)


                    val skipErrorTest = true

                    describe("Wait for Page to Load")
                        .waitForDisplay("Allow InvestaTrade-Dev to send you notifications?", waitSeconds = 30.0)

                    it.exist("Allow InvestaTrade-Dev to send you notifications?")
                    it.exist("Allow")
                    it.exist("Don’t allow")

                    it.tap("Allow")

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)
                    it.macro("[Create Select Broker]", broker)
                    it.macro("[Create Trading PIN]", pin, skipErrorTest)
                    it.macro("[Account Application Status]", "0", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    if(hasMinimumDeposit){
                        it.tap("Make Initial Deposit")
                        it.exist("Follow these easy steps to start investing")
                    }

//
                    //Verify Email and Mobile Number

                    var willSkipEmail = true
                    var willSkipMobile = true
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = false
                    var skipErrorMobile = false

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNUmber, password, true, willSkipMobile, skipErrorMobilePin, skipErrorMobile, useInternational, internationalCountryCode)


                    it.macro("[Account Application Status]", "0", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    if(hasMinimumDeposit){
                        it.tap("Make Initial Deposit")
                        it.exist("Follow these easy steps to start investing")
                    }

                    //Complete Application Form

                    val useMiddleName = false;

                    var fullName = "$firstName $middleName $lastName"

                    if (!useMiddleName) {
                        fullName = "$firstName $lastName"
                    }

                    val isPHUser = false;
                    val hasDualCitizen = true;
                    val isUSPerson = true;
                    val hasSamePermanentAddress = true;
                    val usePHCountry = false;

                    // Address Info
                    val country= Onboarding.getRandomCountry()

                    val province = Onboarding.getRandomProvince()
                    val addressCity = Onboarding.getRandomCityInProvince(province)
                    val presentBarangay = Onboarding.getRandomBarangayInCity(addressCity, province)
                    val streetAddress = faker.address.streetAddress()
                    val zipCode = Onboarding.getRandomZipCode()

                    val permanentProvince = Onboarding.getRandomProvince()
                    val permanentCity = Onboarding.getRandomCityInProvince(permanentProvince)
                    val permanentBarangay = Onboarding.getRandomBarangayInCity(permanentCity, permanentProvince)
                    val permanentStreetAddress = faker.address.streetAddress()
                    val permanentZipCode = Onboarding.getRandomZipCode()

                    // Employment Status Info

                    val employmentStatus = Onboarding.getRandomEmploymentStatus()
                    val workTitle = Onboarding.getRandomWorkTitle()
                    val industry = Onboarding.getRandomIndustry()
                    val employerName = faker.company.name()
                    val employerCountry = Onboarding.getRandomCountry()
                    val usePHEmployerCountry = false;
                    val employerProvince = Onboarding.getRandomProvince()
                    val employerCity = Onboarding.getRandomCityInProvince(employerProvince)
                    val employerBarangay = Onboarding.getRandomBarangayInCity(employerCity, employerProvince)
                    val employerStreetAddress = faker.address.streetAddress()
                    val employerZipCode = Onboarding.getRandomZipCode()
                    val TINNumber = Onboarding.getRandomTinNumber()
                    val IDType = "SSS"
                    val IDNumber = Onboarding.generateRandomNumber(IDType)

                    //PSE Disclosures

                    val isCorporateOfficer = false;
                    val isBrokerOfficer = false;
                    val isExistingAccountHolder = false;

                    val corporateName = faker.company.name()
                    val corporatePosition = faker.company.name()

                    val brokerName = faker.company.name()
                    val brokerPosition = faker.company.name()

                    val brokers = Onboarding.generateBrokerList()

                    // Source of Income

                    val annualIncome = Onboarding.getRandomRange()
                    val sourcesOfIncome = Onboarding.getRandomSources(employmentStatus)
                    val isPoliticallyExpose = false
                    val allSourceIncome = Onboarding.getAllSources(employmentStatus)

                    // Investment Objectives , Net Worth , Assets

                    val rankedObjectives = Onboarding.getRandomRankedInvestmentObjectives()
                    val assetsRange = Onboarding.getRandomAssetsRange()
                    val netWorthRange = Onboarding.getRandomAssetsRange()

                    it.macro("[Personal Info]", firstName, middleName, lastName, useMiddleName, gender, civilStatus)

                    it.macro(
                        "[BirthPlace and Citizenship]",
                        isPHUser,
                        birthDay,
                        birthCity,
                        hasDualCitizen,
                        citizenship,
                        isUSPerson,
                        birthPlace,
                        randomDualCitizenship
                    )

                    it.macro(
                        "[Present Address Info]",
                        hasSamePermanentAddress,
                        usePHCountry,
                        country,
                        province,
                        addressCity,
                        presentBarangay,
                        streetAddress,
                        zipCode
                    )

                    it.macro(
                        "[Permanent Address Info]",
                        hasSamePermanentAddress,
                        usePHCountry,
                        country,
                        permanentProvince,
                        permanentCity,
                        permanentBarangay,
                        permanentStreetAddress,
                        permanentZipCode
                    )

                    val hasSocialID = Onboarding.getRandomBoolean()

                    it.macro(
                        "[Employment Info]",
                        employmentStatus,
                        workTitle,
                        industry,
                        employerName,
                        employerCountry,
                        usePHEmployerCountry,
                        employerProvince,
                        employerCity,
                        employerBarangay,
                        employerStreetAddress,
                        employerZipCode,
                        TINNumber,
                        IDType,
                        IDNumber,
                        citizenship,
                        hasSocialID,
                        isPHUser
                    )

                    it.macro(
                        "[PSE Disclosures]",
                        isCorporateOfficer,
                        isBrokerOfficer,
                        isExistingAccountHolder,
                        corporateName,
                        corporatePosition,
                        brokerName,
                        brokerPosition,
                        brokers
                    )

                    it.macro("[Sources of Income]", annualIncome, sourcesOfIncome, isPoliticallyExpose, allSourceIncome)

                    it.macro("[Objectives, Assets, Net Worth]", rankedObjectives, assetsRange, netWorthRange)

                    //Random True or False
                    val isFormW9 = Onboarding.getRandomBoolean()
                    var fatcaList: List<String> = listOf()

                    // Generate a phone number in national format
                    val areaCode = Onboarding.getRandomAreaCode() // Generate an area code
                    val lineNumber = Onboarding.generateRandom7DigitNumber() // Generate the line number

                    val USContactNumber = "$areaCode$lineNumber"

                    val USTINNumber = Onboarding.getRandomUSTIN()
                    val USState = faker.address.state()
                    val USCity = faker.address.city()
                    val USStreetAddress = faker.address.streetAddress()
                    val USZipCode = Onboarding.getRandomZipCode()
                    //val willEnterUSAddress = getRandomBoolean()
                    val willEnterUSAddress = true

                    if(isUSPerson){

                        if(isFormW9){
                            fatcaList = Onboarding.formW9()
                        } else {
                            fatcaList = Onboarding.formW8()
                        }

                        it.macro("[FATCA Agreement]", isFormW9, fatcaList, USContactNumber, USTINNumber, USState, USCity, USStreetAddress, USZipCode, willEnterUSAddress)
                    }

                    it.macro(
                        "[Review Application]",
                        firstName,
                        middleName,
                        lastName,
                        useMiddleName,
                        gender,
                        civilStatus,
                        isPHUser,
                        birthDay,
                        birthCity,
                        hasDualCitizen,
                        citizenship,
                        randomDualCitizenship,
                        birthPlace,
                        isUSPerson,
                        hasSamePermanentAddress,
                        usePHCountry,
                        country,
                        province,
                        addressCity,
                        presentBarangay,
                        streetAddress,
                        zipCode,
                        permanentProvince,
                        permanentCity,
                        permanentBarangay,
                        permanentStreetAddress,
                        permanentZipCode,
                        employmentStatus,
                        workTitle,
                        industry,
                        employerName,
                        employerCountry,
                        usePHEmployerCountry,
                        employerProvince,
                        employerCity,
                        employerBarangay,
                        employerStreetAddress,
                        employerZipCode,
                        TINNumber,
                        IDType,
                        IDNumber,
                        isCorporateOfficer,
                        isBrokerOfficer,
                        isExistingAccountHolder,
                        corporateName,
                        corporatePosition,
                        brokerName,
                        brokerPosition,
                        brokers,
                        annualIncome,
                        sourcesOfIncome,
                        isPoliticallyExpose,
                        allSourceIncome,
                        rankedObjectives,
                        assetsRange,
                        netWorthRange,
                        isFormW9,
                        fatcaList,
                        USContactNumber, USTINNumber, USState, USCity, USStreetAddress, USZipCode, willEnterUSAddress, hasSocialID,
                    )

                    it.macro("[Account Application Status]", "25", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    if(hasMinimumDeposit){
                        it.tap("Make Initial Deposit")
                        it.exist("Follow these easy steps to start investing")
                    }

                    val scrollAgreement = false

                    it.macro("[Sign Agreement]", scrollAgreement,broker)


                    it.macro("[Account Application Status]", "50", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    var validIDType = Onboarding.getRandomValidIDType()

                    if(!isPHUser){
                        validIDType = "Passport"
                    }

                    val idNumber = Onboarding.getRandomAlphanumericID(10)
                    val useCamera = false
                    val withPermissions = false

                    it.macro(
                        "[Upload Valid ID]",
                        isBrokerOfficer,
                        isUSPerson,
                        validIDType,
                        fullName,
                        idNumber,
                        useCamera,
                        withPermissions,
                    )
                    it.macro("[Upload Video Selfie]")

                    if(isUSPerson){

                        if(isFormW9){

                            it.macro("[Upload FORM W9]")
                            it.macro("[US Identification]")
                        } else {

                            it.macro("[Upload Form-W8Ben]")
                            it.macro("[Non-US Identification]")
                        }
                    }

                    if (isBrokerOfficer) {
                        it.macro("[Consent Letter]")
                    }

                    //Tap Next
                    it.tap("\uE941")

                    it.exist("Wow! just one more step for your investing future!")
                    it.exist("Tap done to proceed")
                    it.tap("Done")

                    it.macro("[Account Application Status]", "75", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    willSkipEmail = false
                    willSkipMobile = false
                    skipErrorEmailPin = true
                    skipErrorMobilePin = true
                    skipErrorEmail = true
                    skipErrorMobile = true

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNUmber, password, true, willSkipMobile, skipErrorMobilePin,skipErrorMobile, useInternational, internationalCountryCode)

                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    if(hasMinimumDeposit){
                        //do something
                    }

                    //Tap Next
                    it.tap("\uE941")

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    it.exist("Invest")
                    it.exist("Search")

                    it.terminateApp()
                    it.launchApp()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Enter your PIN to unlock", waitSeconds = 30.0)

                    //Logout
                    it.macro("[Log Out]")
                }
            }
        }

    }

}