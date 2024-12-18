package onboarding

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import io.bloco.faker.Faker
import macro.Onboarding
import macro.Onboarding.employmentInfo
import macro.Onboarding.formW8
import macro.Onboarding.formW9
import macro.Onboarding.generateBrokerList
import macro.Onboarding.generateRandom7DigitNumber
import macro.Onboarding.generateRandomNumber
import macro.Onboarding.generateRandomPHNumber
import macro.Onboarding.generateRandomPhoneNumber
import macro.Onboarding.getAllSources
import macro.Onboarding.getDateSixYearsFromNow
import macro.Onboarding.getRandomAlphanumericID
import macro.Onboarding.getRandomAreaCode
import macro.Onboarding.getRandomAssetsRange
import macro.Onboarding.getRandomBarangayInCity
import macro.Onboarding.getRandomBoolean
import macro.Onboarding.getRandomCityInProvince
import macro.Onboarding.getRandomCountry
import macro.Onboarding.getRandomCountryCode
import macro.Onboarding.getRandomEmploymentStatus
import macro.Onboarding.getRandomIDType
import macro.Onboarding.getRandomIndustry
import macro.Onboarding.getRandomProvince
import macro.Onboarding.getRandomRange
import macro.Onboarding.getRandomRankedInvestmentObjectives
import macro.Onboarding.getRandomSources
import macro.Onboarding.getRandomTinNumber
import macro.Onboarding.getRandomUSTIN
import macro.Onboarding.getRandomValidIDType
import macro.Onboarding.getRandomWorkTitle
import macro.Onboarding.getRandomZipCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Onboarding : UITest() {

    private var emailInstall = "intestergrams+globaladmin@gmail.com"
    private var pwordInstall = "password123$"
    private var pinInstall = "123123"
    private val useUpdate = false
    private val hasMinimumDeposit = false
    private var broker = "2"

    @Test
    @DisplayName("User with Middle Name, PH User, Different Address, Use Camera, PSE , and Political Exposed")
    @Ignore
    @Order(1)
    fun createAccount() {


        scenario {
            case(1) {
                expectation {

                    //                    it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)

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

                    val internationalCountryCode = getRandomCountryCode()
                    val useInternational = false
                    val mobileNUmber = generateRandomPHNumber()
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"
                    val birthDay = getDateSixYearsFromNow()

                    //Application Form
                    val gender = Onboarding.getRandomGender()
                    val civilStatus = Onboarding.getRandomCivilStatus()
                    val birthPlace = getRandomCountry()
                    val birthCity = faker.address.city()
                    val citizenship = getRandomCountry()

                    var randomDualCitizenship: String

                    do {
                        randomDualCitizenship = getRandomCountry()
                        println("Generated random dual citizenship: $randomDualCitizenship")
                    } while (citizenship == randomDualCitizenship)

                    val readPolicy = false
                    val skipErrorTest = true

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

//                    it.tap("Make Initial Deposit")
//                    it.exist("Follow these easy steps to start investing")

                    //Verify Email and Mobile Number

                    val willSkipEmail = true
                    val willSkipMobile = true
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = true
                    var skipErrorMobile = true

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

//                    it.tap("Make Initial Deposit")
//                    it.exist("Follow these easy steps to start investing")

                    //Complete Application Form

                    val useMiddleName = true;

                    var fullName = "$firstName $middleName $lastName"

                    if (!useMiddleName) {
                        fullName = "$firstName $lastName"
                    }

                    val isPHUser = true;
                    val hasDualCitizen = false;
                    val isUSPerson = false;
                    val hasSamePermanentAddress = true;
                    val usePHCountry = true;

                    // Address Info
                    val country = getRandomCountry()
                    val province = getRandomProvince()
                    val addressCity = getRandomCityInProvince(province)
                    val presentBarangay = getRandomBarangayInCity(addressCity, province)
                    val streetAddress = faker.address.streetAddress()
                    val zipCode = getRandomZipCode()

                    val permanentProvince = getRandomProvince()
                    val permanentCity = getRandomCityInProvince(permanentProvince)
                    val permanentBarangay = getRandomBarangayInCity(permanentCity, permanentProvince)
                    val permanentStreetAddress = faker.address.streetAddress()
                    val permanentZipCode = getRandomZipCode()

                    // Employment Status Info

                    val employmentStatus = getRandomEmploymentStatus()
                    val workTitle = getRandomWorkTitle()
                    val industry = getRandomIndustry()
                    val employerName = faker.company.name()
                    val employerCountry = faker.address.country()
                    val usePHEmployerCountry = true;
                    val employerProvince = getRandomProvince()
                    val employerCity = getRandomCityInProvince(employerProvince)
                    val employerBarangay = getRandomBarangayInCity(employerCity, employerProvince)
                    val employerStreetAddress = faker.address.streetAddress()
                    val employerZipCode = getRandomZipCode()
                    val TINNumber = getRandomTinNumber()
                    val IDType = getRandomIDType()
                    val IDNumber = generateRandomNumber(IDType)

                    //PSE Disclosures

                    val isCorporateOfficer = true;
                    val isBrokerOfficer = true;
                    val isExistingAccountHolder = true;

                    val corporateName = faker.company.name()
                    val corporatePosition = faker.company.name()

                    val brokerName = faker.company.name()
                    val brokerPosition = faker.company.name()

                    val brokers = generateBrokerList()

                    // Source of Income

                    val annualIncome = getRandomRange()
                    val sourcesOfIncome = getRandomSources(employmentStatus)
                    val isPoliticallyExpose = true
                    val allSourceIncome = getAllSources(employmentStatus)

                    // Investment Objectives , Net Worth , Assets

                    val rankedObjectives = getRandomRankedInvestmentObjectives()
                    val assetsRange = getRandomAssetsRange()
                    val netWorthRange = getRandomAssetsRange()

                    it.macro("[Personal Info]", firstName, middleName, lastName, useMiddleName, gender, civilStatus, birthDay, isPHUser, birthPlace, birthCity, citizenship, hasDualCitizen, randomDualCitizenship)

                    it.macro(
                        "[Present and Permanent Address Info]",
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
                        permanentZipCode
                    )


                    val hasSocialID = getRandomBoolean()

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
                    val isFormW9 = getRandomBoolean()
                    var fatcaList: List<String> = listOf()

                    // Generate a phone number in national format
                    val areaCode = getRandomAreaCode() // Generate an area code
                    val lineNumber = generateRandom7DigitNumber() // Generate the line number

                    val USContactNumber = "$areaCode$lineNumber"

                    val USTINNumber = getRandomUSTIN()
                    val USState = faker.address.state()
                    val USCity = faker.address.city()
                    val USStreetAddress = faker.address.streetAddress()
                    val USZipCode = getRandomZipCode()
                    //val willEnterUSAddress = getRandomBoolean()
                    val willEnterUSAddress = true

                    if(isUSPerson){

                        if(isFormW9){
                            fatcaList = formW9()
                        } else {
                            fatcaList = formW8()
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

                    it.macro("[Account Application Status]", "50", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    val scrollAgreement = true

                    it.macro("[Sign Agreement]", scrollAgreement,broker)


                    it.macro("[Account Application Status]", "75", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    val validIDType = getRandomValidIDType()
                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = true
                    val withPermissions = true

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

                    if (isBrokerOfficer) {
                        it.macro("[Consent Letter]")
                    }

                    //Tap Next
                    it.tap("\uE941")

                    it.exist("Wow! just one more step for your investing future!")
                    it.exist("Tap done to proceed")
                    it.tap("Done")

                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    //Tap Next
                    it.tap("\uE941")

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    describe("Wait for Page to Load")
                        .waitForDisplay("Search", waitSeconds = 30.0)
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

    @Test
    @DisplayName("US Person with No Middle Name, Dual Citizen, Same Address, no PSE, Not Political Exposed, Upload Library")
    @Ignore
    @Order(2)
    fun usPerson() {

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

                    val internationalCountryCode = getRandomCountryCode()
                    val useInternational = false
                    val mobileNUmber = generateRandomPHNumber()
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"
                    val birthDay = getDateSixYearsFromNow()

                    //Application Form
                    val gender = Onboarding.getRandomGender()
                    val civilStatus = Onboarding.getRandomCivilStatus()
                    val birthPlace = "United States"
                    val birthCity = faker.address.city()

                    var citizenship: String

                    do {
                        citizenship = getRandomCountry()
                    } while (citizenship == "Philippines")

                    var randomDualCitizenship: String

                    //Must not be the same
                    do {
                        randomDualCitizenship = getRandomCountry()
                    } while (citizenship == randomDualCitizenship)

                    val readPolicy = false


//                    it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)


                    val skipErrorTest = true

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
                    val country= getRandomCountry()

                    val province = getRandomProvince()
                    val addressCity = getRandomCityInProvince(province)
                    val presentBarangay = getRandomBarangayInCity(addressCity, province)
                    val streetAddress = faker.address.streetAddress()
                    val zipCode = getRandomZipCode()

                    val permanentProvince = getRandomProvince()
                    val permanentCity = getRandomCityInProvince(permanentProvince)
                    val permanentBarangay = getRandomBarangayInCity(permanentCity, permanentProvince)
                    val permanentStreetAddress = faker.address.streetAddress()
                    val permanentZipCode = getRandomZipCode()

                    // Employment Status Info

                    val employmentStatus = getRandomEmploymentStatus()
                    val workTitle = getRandomWorkTitle()
                    val industry = getRandomIndustry()
                    val employerName = faker.company.name()
                    val employerCountry = getRandomCountry()
                    val usePHEmployerCountry = false;
                    val employerProvince = getRandomProvince()
                    val employerCity = getRandomCityInProvince(employerProvince)
                    val employerBarangay = getRandomBarangayInCity(employerCity, employerProvince)
                    val employerStreetAddress = faker.address.streetAddress()
                    val employerZipCode = getRandomZipCode()
                    val TINNumber = getRandomTinNumber()
                    val IDType = "SSS"
                    val IDNumber = generateRandomNumber(IDType)

                    //PSE Disclosures

                    val isCorporateOfficer = false;
                    val isBrokerOfficer = false;
                    val isExistingAccountHolder = false;

                    val corporateName = faker.company.name()
                    val corporatePosition = faker.company.name()

                    val brokerName = faker.company.name()
                    val brokerPosition = faker.company.name()

                    val brokers = generateBrokerList()

                    // Source of Income

                    val annualIncome = getRandomRange()
                    val sourcesOfIncome = getRandomSources(employmentStatus)
                    val isPoliticallyExpose = false
                    val allSourceIncome = getAllSources(employmentStatus)

                    // Investment Objectives , Net Worth , Assets

                    val rankedObjectives = getRandomRankedInvestmentObjectives()
                    val assetsRange = getRandomAssetsRange()
                    val netWorthRange = getRandomAssetsRange()

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

                    val hasSocialID = getRandomBoolean()

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
                    val isFormW9 = getRandomBoolean()
                    var fatcaList: List<String> = listOf()

                    // Generate a phone number in national format
                    val areaCode = getRandomAreaCode() // Generate an area code
                    val lineNumber = generateRandom7DigitNumber() // Generate the line number

                    val USContactNumber = "$areaCode$lineNumber"

                    val USTINNumber = getRandomUSTIN()
                    val USState = faker.address.state()
                    val USCity = faker.address.city()
                    val USStreetAddress = faker.address.streetAddress()
                    val USZipCode = getRandomZipCode()
                    //val willEnterUSAddress = getRandomBoolean()
                    val willEnterUSAddress = true

                    if(isUSPerson){

                        if(isFormW9){
                            fatcaList = formW9()
                        } else {
                            fatcaList = formW8()
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

                    var validIDType = getRandomValidIDType()

                    if(!isPHUser){
                        validIDType = "Passport"
                    }

                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = false
                    val withPermissions = true

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
                    describe("Wait for Page to Load")
                        .waitForDisplay("Search", waitSeconds = 30.0)
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

    @Test
    @DisplayName("Random All, with International Phone Number, Edit Review Application, with Initial Deposit")
    @Ignore
    @Order(3)
    fun randomAll(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //it.tap("Logout")


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

                    val internationalCountryCode = getRandomCountryCode()
                    val useInternational = true
                    val mobileNUmber = generateRandomPhoneNumber(internationalCountryCode)
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"
                    val birthDay = getDateSixYearsFromNow()

                    //Application Form
                    val gender = Onboarding.getRandomGender()
                    val civilStatus = Onboarding.getRandomCivilStatus()
                    val birthPlace = "United States"
                    val birthCity = faker.address.city()

                    var citizenship: String

                    do {
                        citizenship = getRandomCountry()
                    } while (citizenship == "Philippines")

                    var randomDualCitizenship: String

                    //Must not be the same
                    do {
                        randomDualCitizenship = getRandomCountry()
                    } while (citizenship == randomDualCitizenship)

                    val readPolicy = false


//                    it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)


                    val skipErrorTest = true

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

                    var willSkipEmail = false
                    var willSkipMobile = false
                    var skipErrorEmailPin = true
                    var skipErrorMobilePin = true
                    var skipErrorEmail = true
                    var skipChangeMobile = true

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNUmber, password, true, willSkipMobile, skipErrorMobilePin, skipChangeMobile, useInternational, internationalCountryCode)


                    it.macro("[Account Application Status]", "25", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    if(hasMinimumDeposit){
                        it.tap("Make Initial Deposit")
                        it.exist("Follow these easy steps to start investing")
                    }

                    //Complete Application Form

                    val useMiddleName = getRandomBoolean();

                    var fullName = "$firstName $middleName $lastName"

                    if (!useMiddleName) {
                        fullName = "$firstName $lastName"
                    }

                    val isPHUser = getRandomBoolean();
                    val hasDualCitizen = getRandomBoolean();

                    val isUSPerson: Boolean;

                    if(isPHUser){
                        isUSPerson = false
                    } else {
                        isUSPerson = true
                    }


                    val hasSamePermanentAddress = getRandomBoolean();
                    val usePHCountry = getRandomBoolean();

                    // Address Info
                    val country= getRandomCountry()

                    val province = getRandomProvince()
                    val addressCity = getRandomCityInProvince(province)
                    val presentBarangay = getRandomBarangayInCity(addressCity, province)
                    val streetAddress = faker.address.streetAddress()
                    val zipCode = getRandomZipCode()

                    val permanentProvince = getRandomProvince()
                    val permanentCity = getRandomCityInProvince(permanentProvince)
                    val permanentBarangay = getRandomBarangayInCity(permanentCity, permanentProvince)
                    val permanentStreetAddress = faker.address.streetAddress()
                    val permanentZipCode = getRandomZipCode()

                    // Employment Status Info

                    val employmentStatus = getRandomEmploymentStatus()
                    val workTitle = getRandomWorkTitle()
                    val industry = getRandomIndustry()
                    val employerName = faker.company.name()
                    val employerCountry = getRandomCountry()
                    val usePHEmployerCountry = getRandomBoolean();
                    val employerProvince = getRandomProvince()
                    val employerCity = getRandomCityInProvince(employerProvince)
                    val employerBarangay = getRandomBarangayInCity(employerCity, employerProvince)
                    val employerStreetAddress = faker.address.streetAddress()
                    val employerZipCode = getRandomZipCode()
                    val TINNumber = getRandomTinNumber()
                    val IDType = "SSS"
                    val IDNumber = generateRandomNumber(IDType)

                    //PSE Disclosures

                    val isCorporateOfficer = getRandomBoolean();
                    val isBrokerOfficer = getRandomBoolean();
                    val isExistingAccountHolder = getRandomBoolean();

                    val corporateName = faker.company.name()
                    val corporatePosition = faker.company.name()

                    val brokerName = faker.company.name()
                    val brokerPosition = faker.company.name()

                    val brokers = generateBrokerList()

                    // Source of Income

                    val annualIncome = getRandomRange()
                    val sourcesOfIncome = getRandomSources(employmentStatus)
                    val isPoliticallyExpose = getRandomBoolean()
                    val allSourceIncome = getAllSources(employmentStatus)

                    // Investment Objectives , Net Worth , Assets

                    val rankedObjectives = getRandomRankedInvestmentObjectives()
                    val assetsRange = getRandomAssetsRange()
                    val netWorthRange = getRandomAssetsRange()

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

                    val hasSocialID = getRandomBoolean()

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
                        isPHUser,
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
                    val isFormW9 = getRandomBoolean()
                    var fatcaList: List<String> = listOf()

                    // Generate a phone number in national format
                    val areaCode = getRandomAreaCode() // Generate an area code
                    val lineNumber = generateRandom7DigitNumber() // Generate the line number

                    val USContactNumber = "$areaCode$lineNumber"

                    val USTINNumber = getRandomUSTIN()
                    val USState = faker.address.state()
                    val USCity = faker.address.city()
                    val USStreetAddress = faker.address.streetAddress()
                    val USZipCode = getRandomZipCode()
                    //val willEnterUSAddress = getRandomBoolean()
                    val willEnterUSAddress = true

                    if(isUSPerson){

                        if(isFormW9){
                            fatcaList = formW9()
                        } else {
                            fatcaList = formW8()
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

                    it.macro("[Account Application Status]", "50", hasMinimumDeposit)

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


                    it.macro("[Account Application Status]", "75", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    var validIDType = getRandomValidIDType()

                    if(!isPHUser){
                        validIDType = "Passport"
                    }

                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = getRandomBoolean()
                    val withPermissions = true

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

                    describe("Wait for page to load")
                        .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)

                    describe("Wait for page to load")
                        .waitForDisplay("Upload Documents", waitSeconds = 30.0)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")


                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    if(hasMinimumDeposit){
                        //do something

                        it.macro("[Account Application Status]", "125", hasMinimumDeposit)
                    }

                    //Tap Next
                    it.tap("\uE941")

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    describe("Wait for Page to Load")
                        .waitForDisplay("Search", waitSeconds = 30.0)
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

    @Test
    @DisplayName("Existing Email")
    @Order(4)
    fun existingEmail(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //it.tap("Logout")
                    //create test account info
                    val faker = Faker()
                    var firstName = faker.name.firstName()
                    var lastName = faker.name.lastName()

                    //Remove ' in last name for less flaky test
                    firstName = firstName.replace("'", "")
                    lastName = lastName.replace("'", "")
                    firstName = firstName.lowercase().replaceFirstChar { it.uppercase() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercase() }

                    val password = "123123"

                    val email = "thetestdummytest+tradebuyer@gmail.com"
                    val readPolicy = false

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)

                    describe("Wait for page to load")
                        .waitForDisplay("This email is already registered.", waitSeconds = 30.0)

                    it.exist("This email is already registered.")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }

    }

    @Test
    @DisplayName("Existing Mobile Number")
    @Order(5)
    fun existingMobileNumber(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //it.tap("Logout")
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

                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"

                    val readPolicy = false
                    val skipErrorTest = true

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


                    //Verify Email and Mobile Number

                    var willSkipEmail = true
                    var willSkipMobile = false

                    //Skip PIN Error Scenarios
                    var skipErrorEmailPin = true
                    var skipErrorMobilePin = true

                    //Skip Change Email/Mobile Scenarios
                    var skipErrorEmail = true
                    var skipChangeMobile = true

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)

                    it.exist("Is it really you?")
                    it.exist("Please enter your mobile number.")
                    it.exist("Country")
                    it.exist("Your mobile number")

                    val existingMobileNumber = "9775454345"

                    it.tap("9051234567")
                        .sendKeys(existingMobileNumber)


                    //it.select("\uE941").tap()
                    it.tap(x=927,y=2194)

//                    describe("Wait for page to load")
//                        .waitForDisplay("This mobile number is already taken. If you have an existing Investa account (or Investagrams account), kindly use the same mobile number and log in details to proceed.", waitSeconds = 30.0)


                    describe("Wait for page to load")
                        .waitForDisplay("Is it really you?", waitSeconds = 30.0)

                    it.exist("Is it really you?")

                    it.terminateApp()
                    it.launchApp()
                    it.macro("[Log Out]")
                }
            }
        }

    }

    @Test
    @DisplayName("Existing Full Name of User")
    @Order(6)
    @Ignore
    fun existingUserInfo(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //create test account info
                    val faker = Faker()

                    //Remove ' in last name for less flaky test
                    val firstName = "Test Subd"
                    val middleName = "Test"
                    val lastName = "Buyer"

                    val internationalCountryCode = getRandomCountryCode()
                    val useInternational = true
                    val mobileNUmber = generateRandomPhoneNumber(internationalCountryCode)
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email = "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".replace(" ", "").lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"
                    val birthDay = "06/16/2006"

                    //Application Form
                    val gender = "Female"
                    val civilStatus = Onboarding.getRandomCivilStatus()
                    val birthPlace = "United States"
                    val birthCity = faker.address.city()

                    var citizenship: String

                    do {
                        citizenship = getRandomCountry()
                    } while (citizenship == "Philippines")

                    var randomDualCitizenship: String

                    //Must not be the same
                    do {
                        randomDualCitizenship = getRandomCountry()
                    } while (citizenship == randomDualCitizenship)

                    val readPolicy = false


                    val skipErrorTest = true

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

                    var skipErrorEmailPin = true
                    var skipErrorMobilePin = true

                    var skipErrorEmail = true
                    var skipChangeMobile = true

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNUmber, password, true, willSkipMobile, skipErrorMobilePin, skipChangeMobile, useInternational, internationalCountryCode)


                    it.macro("[Account Application Status]", "0", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.exist("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

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

                    val isPHUser = true
                    val hasDualCitizen = false

                    val isUSPerson: Boolean;

                    if(isPHUser){
                        isUSPerson = false
                    } else {
                        isUSPerson = true
                    }


                    val hasSamePermanentAddress = true
                    val usePHCountry = false

                    // Address Info
                    val country= getRandomCountry()

                    val province = getRandomProvince()
                    val addressCity = getRandomCityInProvince(province)
                    val presentBarangay = getRandomBarangayInCity(addressCity, province)
                    val streetAddress = faker.address.streetAddress()
                    val zipCode = getRandomZipCode()

                    val permanentProvince = getRandomProvince()
                    val permanentCity = getRandomCityInProvince(permanentProvince)
                    val permanentBarangay = getRandomBarangayInCity(permanentCity, permanentProvince)
                    val permanentStreetAddress = faker.address.streetAddress()
                    val permanentZipCode = getRandomZipCode()

                    // Employment Status Info

                    val employmentStatus = "Unemployed"
                    val workTitle = getRandomWorkTitle()
                    val industry = getRandomIndustry()
                    val employerName = faker.company.name()
                    val employerCountry = getRandomCountry()
                    val usePHEmployerCountry = getRandomBoolean();
                    val employerProvince = getRandomProvince()
                    val employerCity = getRandomCityInProvince(employerProvince)
                    val employerBarangay = getRandomBarangayInCity(employerCity, employerProvince)
                    val employerStreetAddress = faker.address.streetAddress()
                    val employerZipCode = getRandomZipCode()
                    val TINNumber = getRandomTinNumber()
                    val IDType = "SSS"
                    val IDNumber = generateRandomNumber(IDType)

                    //PSE Disclosures

                    val isCorporateOfficer = false
                    val isBrokerOfficer = false
                    val isExistingAccountHolder = false

                    val corporateName = faker.company.name()
                    val corporatePosition = faker.company.name()

                    val brokerName = faker.company.name()
                    val brokerPosition = faker.company.name()

                    val brokers = generateBrokerList()

                    // Source of Income

                    val annualIncome = getRandomRange()
                    val sourcesOfIncome = getRandomSources(employmentStatus)
                    val isPoliticallyExpose = false
                    val allSourceIncome = getAllSources(employmentStatus)

                    // Investment Objectives , Net Worth , Assets

                    val rankedObjectives = getRandomRankedInvestmentObjectives()
                    val assetsRange = getRandomAssetsRange()
                    val netWorthRange = getRandomAssetsRange()

                    it.macro("[Personal Info]", firstName, middleName, lastName, useMiddleName, gender, civilStatus, birthDay, isPHUser, birthPlace, birthCity, citizenship, hasDualCitizen, randomDualCitizenship)

                    it.macro(
                        "[Fixed BirthPlace and Citizenship Scenario]",
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

                    val hasSocialID = false

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
                        isPHUser,
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
                    val isFormW9 = getRandomBoolean()
                    var fatcaList: List<String> = listOf()

                    // Generate a phone number in national format
                    val areaCode = getRandomAreaCode() // Generate an area code
                    val lineNumber = generateRandom7DigitNumber() // Generate the line number

                    val USContactNumber = "$areaCode$lineNumber"

                    val USTINNumber = getRandomUSTIN()
                    val USState = faker.address.state()
                    val USCity = faker.address.city()
                    val USStreetAddress = faker.address.streetAddress()
                    val USZipCode = getRandomZipCode()
                    //val willEnterUSAddress = getRandomBoolean()
                    val willEnterUSAddress = true

                    if(isUSPerson){

                        if(isFormW9){
                            fatcaList = formW9()
                        } else {
                            fatcaList = formW8()
                        }

                        it.macro("[FATCA Agreement]", isFormW9, fatcaList, USContactNumber, USTINNumber, USState, USCity, USStreetAddress, USZipCode, willEnterUSAddress)
                    }

                    it.exist("Review your Application")
                    it.exist("Make sure that you fill in your application details and make sure we got it right.")


                    //Tap Next

                    it.tap("\uE941")

                    //Confirm Modal

                    it.exist("Are you sure you want to submit your application form?")
                    it.exist("By submitting this form, I hereby certify that the information I provided herein is true, accurate and complete, and I agree to notify/update the entity of any change in any of the information supplied in this form.")
                    it.exist("I agree")

                    it.tap("I agree")

//                    describe("Wait for page to load")
//                        .waitForDisplay("You already have an account with us. We dont allow duplicate account. Please use your other account. Ref No: 3242.", waitSeconds = 30.0)
                    describe("Wait for page to load")
                        .waitForDisplay("Review your Application")

                    it.exist("Review your Application")
                    it.dontExist("Wow! you are now done with your application form!")

                    it.terminateApp()
                    it.launchApp()
                    it.macro("[Log Out]")

                }
            }
        }

    }


    //Cannot use Investagrams email

    @Test
    @DisplayName("Investa Name or Email")
    @Order(7)
    fun investaNameEmail(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //it.tap("Logout")
                    //create test account info
                    val faker = Faker()
                    var firstName = "Investa"
                    var lastName = faker.name.lastName()

                    //Remove ' in last name for less flaky test
                    firstName = firstName.replace("'", "")
                    lastName = lastName.replace("'", "")
                    firstName = firstName.lowercase().replaceFirstChar { it.uppercase() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercase() }

                    val password = "123123"

                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                    "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val readPolicy = false

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)

//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Sorry you cannot use investa in your name or email.", waitSeconds = 30.0)

                    describe("Wait for Page to Load")
                        .waitForDisplay(firstName, waitSeconds = 30.0)

                    it.tap(firstName)
                        .clearInput()
                        .sendKeys(faker.name.firstName())

                    it.tap(lastName)
                        .clearInput()
                        .sendKeys("Investa")

                    it.hideKeyboard()
                    it.tap("Create an Account")

//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Sorry you cannot use investa in your name or email.", waitSeconds = 30.0)

//                    it.exist("Sorry you cannot use investa in your name or email.")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Investa", waitSeconds = 30.0)

                    it.tap("Investa")
                        .clearInput()
                        .sendKeys(faker.name.lastName())

                    it.tap(email)
                        .clearInput()
                        .sendKeys("investa@gmail.com")

                    it.hideKeyboard()
                    it.tap("Create an Account")

//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Sorry you cannot use investa in your name or email.", waitSeconds = 30.0)

                    describe("Wait for Page to Load")
                        .waitForDisplay("investa@gmail.com", waitSeconds = 30.0)

                    it.tap("investa@gmail.com")
                        .clearInput()
                        .sendKeys("$lastName@investa.com")

                    it.hideKeyboard()
                    it.tap("Create an Account")

                    it.exist("First Name")

//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Sorry you cannot use investa in your name or email.", waitSeconds = 30.0)


                    it.terminateApp()
                    it.launchApp()
                }
            }
        }

    }

    @Test
    @DisplayName("Invalid Names Register")
    @Order(8)
    fun invalidNames(){
        //All Random and International Phone Number, Edit KYC, with Initial Deposit
        scenario {
            case(1) {
                expectation {

                    //it.tap("Logout")
                    //create test account info
                    val faker = Faker()
                    var firstName = "I"
                    var lastName = faker.name.lastName()


                    val password = "123123"

                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val readPolicy = false

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)

                    it.exist("Must be at least 2 characters")

                    it.tap(firstName)
                        .clearInput()
                        .sendKeys(faker.name.firstName())

                    it.tap(lastName)
                        .clearInput()
                        .sendKeys("J")

                    it.hideKeyboard()
                    it.tap("Create an Account")

                    it.exist("Must be at least 2 characters")

                    it.tap("J")
                        .clearInput()
                        .sendKeys(faker.name.lastName())

                    it.dontExist("Must be at least 2 characters")

                    it.hideKeyboard()

                    it.tap("••••••")
                        .clearInput()
                        .sendKeys("123")

                    it.hideKeyboard()

                    it.tap("Create an Account")

                    it.exist("Password must be a least 6 characters")

                    it.tap("•••")
                        .clearInput()
                        .sendKeys(password)

                    it.hideKeyboard()

                    it.dontExist("Password must be a least 6 characters")

                    it.tap("Create an Account")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Registered Successful", waitSeconds = 30.0)


                    it.terminateApp()
                    it.launchApp()

                }
            }
        }

    }

    @Test
    @DisplayName("Create Account up to Verified Number")
    @Order(9)
    fun createAccountUptoVerifiedNumber() {


        scenario {
            case(1) {
                expectation {

                    //                    it.macro("[Install Code Push First]", emailInstall, pwordInstall, pinInstall)
                    //create test account info
                    val faker = Faker()
                    var firstName = faker.name.firstName()
                    var lastName = faker.name.lastName()

                    //Remove ' in last name for less flaky test
                    firstName = firstName.replace("'", "")
                    lastName = lastName.replace("'", "")
                    firstName = firstName.lowercase().replaceFirstChar { it.uppercase() }
                    lastName = lastName.lowercase().replaceFirstChar { it.uppercase() }

                    val internationalCountryCode = getRandomCountryCode()
                    val useInternational = false
                    val mobileNUmber = generateRandomPHNumber()
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val password = "123123"
                    val pin = "123123"


                    val readPolicy = false
                    val skipErrorTest = true

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

//                    it.tap("Make Initial Deposit")
//                    it.exist("Follow these easy steps to start investing")

                    //Verify Email and Mobile Number

                    val willSkipEmail = false
                    val willSkipMobile = false
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = false
                    var skipErrorMobile = false

                    it.macro("[Verify Email]", email, password, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNUmber, password, true, willSkipMobile, skipErrorMobilePin, skipErrorMobile, useInternational, internationalCountryCode)


                    it.macro("[Account Application Status]", "25", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

//                    it.tap("Make Initial Deposit")
//                    it.exist("Follow these easy steps to start investing")

                    //redirect to application form
                    it.tap("Complete Application Form")

                    it.scrollUp()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Application Form", waitSeconds = 30.0)

                    it.exist("Application Form")

                    it.exist("Personal Info")

                    it.tap(x=55,y=193)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)

                    it.exist("Follow these easy steps to start investing")



                    //Logout
                    it.terminateApp()
                    it.launchApp()
                    it.macro("[Log Out]")

                }
            }
        }

    }

    @Test
    @DisplayName("Continue Sign Agreement , Take Picture, PH User")
    @Order(10)
    fun continueSignAgreementPHUser() {


        scenario {
            case(1) {
                expectation {

                    var email = ""
                    var pword = ""
                    var pin = ""
                    var fullName = ""
                    var isBrokerOfficer = false
                    var fatcaType = ""


                    //Get Current Trading Hour
                    val csvFile =  File("src/test/resources/continue_onboarding.csv")

                    // Open the CSV file and read the data
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Skip the header row and process the data
                    csvData.drop(1).firstOrNull()?.let { row ->
                        email = row[0]
                        pword = row[1]
                        pin = row[2]
                        fullName = row[3]
                        broker = row[4]
                        //isBrokerOfficer = row[5]
                        fatcaType = row[6]
                        println(email)
                        println(pword)

                    }


                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)

                    //Verify Email and Mobile Number
                    val willSkipEmail = true
                    val willSkipMobile = true
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = true
                    var skipErrorMobile = true

                    val isUSPerson = false;

                    it.macro("[Account Application Status]", "50", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    val scrollAgreement = false

                    it.macro("[Sign Agreement]", scrollAgreement,broker)


                    it.macro("[Account Application Status]", "75", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
                    it.tap("Verify My Mobile No. and Email")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    val validIDType = getRandomValidIDType()
                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = true
                    val withPermissions = true


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

                    if (isBrokerOfficer) {
                        it.macro("[Consent Letter]")
                    }

                    //Tap Next
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wow! just one more step for your investing future!", waitSeconds = 30.0)

                    it.exist("Wow! just one more step for your investing future!")
                    it.exist("Tap done to proceed")
                    it.tap("Done")

                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    //Tap Next
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wohooo! You are now done!", waitSeconds = 30.0)

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    describe("Wait for Page to Load")
                        .waitForDisplay("All", waitSeconds = 30.0)

                    it.exist("All")
                    it.exist("Bluechips")
                    it.exist("REITs")
                    it.exist("Dividend Stocks")

                    it.exist("A-Z")
                    it.exist("Change%")
                    it.exist("Volume")

                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

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

    @Test
    @DisplayName("Continue Sign Agreement, FATCA 1, Upload Library")
    @Order(11)
    fun continueSignAgreementFATCA1() {

        scenario {
            case(1) {
                expectation {

                    var email = ""
                    var pword = ""
                    var pin = ""
                    var fullName = ""
                    var isBrokerOfficer = true
                    var isFormW9 = false

                    //Get Current Trading Hour
                    val csvFile =  File("src/test/resources/continue_onboarding_fatca_1.csv")

                    // Open the CSV file and read the data
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Skip the header row and process the data
                    csvData.drop(1).firstOrNull()?.let { row ->
                        email = row[0]
                        pword = row[1]
                        pin = row[2]
                        fullName = row[3]
                        broker = row[4]
                        //isBrokerOfficer = row[5]
                        val fatcaType = row[6]

                        if(fatcaType == "True"){
                            isFormW9 = true
                        } else if (fatcaType == "False") {
                            isFormW9 = false
                        }

                        email = email.lowercase(Locale.getDefault())


                        println(email)
                        println(pword)

                    }


                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)


                    var willSkipEmail = true
                    var willSkipMobile = true
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = false
                    var skipErrorMobile = false

                    val isUSPerson = true;
                    val isPHUser = false;

                    it.macro("[Account Application Status]", "25", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

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

                    var validIDType = getRandomValidIDType()

                    if(!isPHUser){
                        validIDType = "Passport"
                    }

                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = false
                    val withPermissions = true

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
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wow! just one more step for your investing future!", waitSeconds = 30.0)


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

                    val internationalCountryCode = "Japan (+81)"
                    val useInternational = true
                    val mobileNumber = generateRandomPhoneNumber(internationalCountryCode)

                    it.macro("[Verify Email]", email, pword, willSkipEmail, skipErrorEmailPin, skipErrorEmail)
                    it.macro("[Verify Mobile Number]", mobileNumber, pword, true, willSkipMobile, skipErrorMobilePin,skipErrorMobile, useInternational, internationalCountryCode)

                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    if(hasMinimumDeposit){
                        //do something
                    }

                    //Tap Next
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wohooo! You are now done!", waitSeconds = 30.0)

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    describe("Wait for Page to Load")
                        .waitForDisplay("All", waitSeconds = 30.0)

                    it.exist("All")
                    it.exist("Bluechips")
                    it.exist("REITs")
                    it.exist("Dividend Stocks")

                    it.exist("A-Z")
                    it.exist("Change%")
                    it.exist("Volume")

                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

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

    @Test
    @DisplayName("Continue Sign Agreement, FATCA 2, Upload Library")
    @Order(12)
    fun continueSignAgreementFATCA2() {

        scenario {
            case(1) {
                expectation {

                    var email = ""
                    var pword = ""
                    var pin = ""
                    var fullName = ""
                    var isBrokerOfficer = false
                    var isFormW9 = false

                    //Get Current Trading Hour
                    val csvFile =  File("src/test/resources/continue_onboarding_fatca_2.csv")

                    // Open the CSV file and read the data
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Skip the header row and process the data
                    csvData.drop(1).firstOrNull()?.let { row ->
                        email = row[0]
                        pword = row[1]
                        pin = row[2]
                        fullName = row[3]
                        broker = row[4]
                        //isBrokerOfficer = row[5]
                        val fatcaType = row[6]

                        if(fatcaType == "True"){
                            isFormW9 = true
                        } else if (fatcaType == "False") {
                            isFormW9 = false
                        }

                        email = email.lowercase(Locale.getDefault())


                        println(email)
                        println(pword)

                    }


                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)


                    var willSkipEmail = true
                    var willSkipMobile = true
                    var skipErrorEmailPin = false
                    var skipErrorMobilePin = false
                    var skipErrorEmail = false
                    var skipErrorMobile = false

                    val isUSPerson = true;
                    val isPHUser = false;

                    it.macro("[Account Application Status]", "50", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")

                    val scrollAgreement = false

                    it.macro("[Sign Agreement]", scrollAgreement,broker)


                    it.macro("[Account Application Status]", "75", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    var validIDType = getRandomValidIDType()

                    if(!isPHUser){
                        validIDType = "Passport"
                    }

                    val idNumber = getRandomAlphanumericID(10)
                    val useCamera = false
                    val withPermissions = true

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

                    describe("Wait for Page to Load")
                        .waitForDisplay("Please provide all necessary documents for faster approval.", waitSeconds = 30.0)


                    //Tap Next
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wow! just one more step for your investing future!", waitSeconds = 30.0)

                    it.exist("Wow! just one more step for your investing future!")
                    it.exist("Tap done to proceed")
                    it.tap("Done")

                    it.macro("[Account Application Status]", "100", hasMinimumDeposit)

                    //Check to Tap Disabled Progress Buttons
//                    it.tap("Verify My Mobile No. and Email")
//                    it.exist("Follow these easy steps to start investing")

                    it.tap("Complete Application Form")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Sign Agreement")
                    it.exist("Follow these easy steps to start investing")

                    it.tap("Upload Documents")
                    it.exist("Follow these easy steps to start investing")


                    if(hasMinimumDeposit){
                        //do something
                    }

                    //Tap Next
                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Wohooo! You are now done!", waitSeconds = 30.0)

                    it.exist("Wohooo! You are now done!")

                    it.exist("Congrats! you are now done and your application is now under review! We will notify you as soon your application is approved!")

                    it.exist("Explore Investa Trade")
                    it.tap("Explore Investa Trade")

                    //Trade Page
                    describe("Wait for Page to Load")
                        .waitForDisplay("All", waitSeconds = 30.0)

                    it.exist("All")
                    it.exist("Bluechips")
                    it.exist("REITs")
                    it.exist("Dividend Stocks")

                    it.exist("A-Z")
                    it.exist("Change%")
                    it.exist("Volume")

                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

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

    // MF to Trade

    // Grams to Trade

    //Approval

    //Rejected

    //Returned

    //Expired KYC

}