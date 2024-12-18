package macro

import com.beust.klaxon.Klaxon
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.exception.TestDriverException
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.io.File
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.random.Random

@MacroObject
object Onboarding : TestDrive {
    @Macro("[Create an Account]")
    fun createAccount(firstName:String, lastName:String, email:String, password:String, readPolicy:Boolean){
        //check page for important details

        //logo
        //it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.widget.ImageView[2]\n")
        describe("Wait for page to load")
            .waitForDisplay("Let’s Get Started", waitSeconds = 30.0)


        it.exist("Let’s Get Started")
        it.exist( "Invest effortlessly in what is suitable for you")
        it.exist("Existing Investagrams user?")
        it.exist("Use your Investagrams account to Login.")

        it.tap("Create an Account")

        describe("Wait for page to load")
            .waitForDisplay("Create an Account.", waitSeconds = 30.0)

        it.exist("Create an Account.")
        it.exist("Build your wealth and put your money to work, effortlessly.")



        //tap with empty fields
        it.tap("Create an Account")
        it.exist("First name is required")
        it.exist("Last name is required")
        it.exist("Email address is required")
        it.exist("Password is required")

        it.exist("First Name")
        it.tap( "Enter your first name")
            .sendKeys(firstName)
        it.dontExist("First name is required")

        it.exist("Last Name")
        it.tap("Enter your last name")
            .sendKeys(lastName)
        it.dontExist("Last name is required")

        it.exist("Email")
        it.tap("Enter your email address")
            .sendKeys(email)
        it.dontExist("Email address is required")

//        it.hideKeyboard()

        it.exist("Password")
        it.tap("Set your password")
            .sendKeys(password)
        it.dontExist("Password is required")

//        it.hideKeyboard()

        it.scrollDown()
        //redirect to Terms of Use

        if(readPolicy) {

            it.tap("Terms of Use")

            describe("Wait for page to load")
                .waitForDisplay("Terms and Conditions", waitSeconds = 30.0)

            it.scrollDown()
            it.scrollToTop()

            it.exist("INVESTATRADE (the “Platform”) is your online platform consisting of web and mobile sites and applications which provide information relevant to various securities in the Philippines. It acts as an online intermediary between you as the investor and your chosen securities broker to enable said parties to communicate and pass information between one another. The Platform is owned and operated by InvestaTrade Inc. (the “Company”), a corporation duly organized and existing under and by virtue of the laws of the Republic of the Philippines.")
            it.scrollToBottom()
            it.exist("GOVERNING LAW AND VENUE. These Terms and Conditions shall be governed by the laws of the Philippines, and any dispute arising from the use of the Platform or concerning these Terms and Conditions shall be brought exclusively before the courts of Makati City, Philippines.")

            //close terms of use
            //it.tap("\uE908")
            it.tap(x=55,y=193)

            describe("Wait for page to load")
                .waitForDisplay("Build your wealth and put your money to work, effortlessly.", waitSeconds = 30.0)

            it.exist("Build your wealth and put your money to work, effortlessly.")

            //redirect to privacy policy

            it.tap("Privacy Policy.")

            describe("Wait for page to load")
                .waitForDisplay("PRIVACY POLICY", waitSeconds = 30.0)

            it.scrollDown()
            it.scrollToTop()

            it.exist("We respect and value your data privacy rights, and we make sure that all personal data collected from you on INVESTATRADE (the “Platform”) are processed in adherence to the general principles of transparency, legitimate purpose, and proportionality.")
            it.scrollToBottom()

            describe("Wait for page to load")
                .waitForDisplay(
                    "You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.",
                    waitSeconds = 30.0
                )

            it.wait(1)

            it.exist("You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.")
            //close privacy policy
            //it.tap("\uE908")
            it.tap(x=55,y=193)

        }

        //create account

        it.hideKeyboard()

        it.tap("Create an Account")


    }

    @Macro("[Create Select Broker]")
    fun createSelectBroker(broker:String){

        describe("Wait for Broker Text To Appear")
            .waitForDisplay("Select Broker", waitSeconds = 30.0)

        //Check Important Labels
        it.exist("Select Broker")
        it.exist("Need help? Learn more")
        it.exist("here")
        it.exist("Log out")

        describe("Wait for Broker Text To Appear")
            .waitForDisplay("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup", waitSeconds = 30.0)

        it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
        it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
        it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
        it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup")

        var brokerNumber = "a";

        describe("Wait for Broker Text To Appear")
            .waitForDisplay("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup", waitSeconds = 30.0)

        if (broker == "1" ){
            brokerNumber = "xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"
        } else if (broker == "2"){
            brokerNumber = "xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup"
        } else if (broker == "3"){
            brokerNumber = "xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup"
        } else if (broker == "4"){
            brokerNumber = "xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup"
        }

        //Tap Broker Selected
        it.tap(brokerNumber)
    }

    @Macro("[Create Trading PIN]")
    fun createTradingPIN(pin:String, skipErrorTest:Boolean){

        describe("Wait for Page to Load")
            .waitForDisplay("Open an account with our partner broker")

        it.exist("Open an account with our partner broker")
        it.exist("Set Trading PIN")
        it.exist("Your trading PIN will be used to access the app and complete important transactions.")

        //Enter Trading PIN
        pin.forEachIndexed { index, char ->
            it.tap("$char")
        }

        it.exist("Confirm Trading Pin")

        //Enter Incorrect Trading PIN

        if(!skipErrorTest){
            val incorrectPIN = "123456"

            incorrectPIN.forEachIndexed { index, char ->
                it.tap("$char")
            }

            describe("Wait for Toast Message")
                .waitForDisplay("The new trading pin and its confirm are not the same")

            it.exist("The new trading pin and its confirm are not the same")
        }


        //Enter Correct Trading PIN
        pin.forEachIndexed { index, char ->
            it.tap("$char")
        }

    }

    @Macro("[Account Application Status]")
    fun accountApplicationStatus(numberPercent:String, hasMinimumDeposit: Boolean){
        describe("Wait for Page to Load")
            .waitForDisplay("Account Application", waitSeconds = 60.0)
        it.exist("Account Application")
        it.exist("Follow these easy steps to start investing")
        it.exist("My Progress")

        var numberPercentage = numberPercent

        if(hasMinimumDeposit){

            if(numberPercent == "25"){
                numberPercentage = "20"
            } else if (numberPercentage == "50"){
                numberPercentage = "40"
            } else if (numberPercentage == "75"){
                numberPercentage = "60"
            } else if (numberPercentage == "100"){
                numberPercentage = "80"
            }

            it.exist("$numberPercentage%")
        } else {
            it.exist("$numberPercentage%")
        }


        it.exist("Verify My Mobile No. and Email")
        it.exist( "Complete Application Form")
        it.exist("Sign Agreement")
        it.exist("Upload Documents")
        //it.exist("Make Initial Deposit")

    }

    @Macro("[Verify Email]")
    fun verifyEmail(email: String, password: String, willSkipEmail:Boolean, skipErrorPin: Boolean, skipErrorEmail:Boolean){
        it.tap("Verify My Mobile No. and Email")
        it.exist("Verify your email address")
        it.exist("Didn’t receive the message?")
        it.exist("Not your email address?")
        it.exist("Edit")
        //it.exist("Skip")

        if(willSkipEmail) {

            it.wait(4.0)

            it.tap("Skip")

            describe("Wait for Page to Load")
                .waitForDisplay("Is it really you?", waitSeconds = 30.0)

            it.exist("Is it really you?")
            it.exist("Please enter your mobile number.")

        } else {

            var hiddenEmail = obfuscateEmail(email)

            it.exist("Please enter the code sent to your email, $hiddenEmail")

            val newEmail = email.replace("1", "")

            //Edit Email

            //Empty Email and Password
            it.tap("Edit")

            it.exist(email)

            it.wait(1.0)
            it.tap(email)
                .clearInput()


            if(!skipErrorEmail) {

                //Tap Submit without Password
                it.tap("Submit")

                //Enter Invalid Email and Empty Password

                val invalidEmail = email.replace("@", "")

                it.tap("Enter email address")
                    .sendKeys(invalidEmail)

                it.tap("Submit")

                it.tap(invalidEmail)
                    .clearInput()

            }


            //Enter Valid Email and Password
            it.tap("Enter email address")
                .sendKeys(newEmail)



            it.tap("Enter password")
                .sendKeys(password)

            //Show Password


            it.wait(2)
            //it.exist("\uE933")
            //it.tap("\uE933")

//            it.tap(x=945,y=1280)
//
//            describe("Wait for Page to Load")
//                .waitForDisplay(password)
//
//            it.exist(password)



            it.tap("Submit")

            describe("Wait for Page to Load")
                .waitForDisplay("Verify your email address")

            hiddenEmail = obfuscateEmail(newEmail)

            describe("Wait for Page to Load")
                .waitForDisplay("Please enter the code sent to your email, $hiddenEmail")

            it.exist("Please enter the code sent to your email, $hiddenEmail")


            //it.exist("Email changed")


            //wait for resend code to reset
            //it.wait(40)

            //Resend Code
            //it.refreshCache()
            //it.selectWithScrollDown("Resend code").tap()

            //describe("Wait for Toast Message")
            //     .waitForDisplay("An activation code has been sent to your email address")

            //it.exist("An activation code has been sent to your email address")
            //it.exist("Activation code sent")

            //Enter Invalid Email Code

            if(!skipErrorPin){
                val invalidEmailCode = "202077"
                it.tap(x = 540, y = 1351)
                it.tap(x = 540, y = 2130)
                it.tap(x = 540, y = 1351)
                it.tap(x = 540, y = 2130)
                it.tap(x = 216, y = 1864)
                it.tap(x = 216, y = 1864)

                describe("Wait for Page to Load")
                    .waitForDisplay("Verify your email address")

//        it.exist("Invalid or Expired email verification code. Please try requesting a new email verification code.")
            }



            val emailCode = "202070"
//        emailCode.forEachIndexed { index, char ->
//            it.tap("$char")
//
//        }

            it.tap(x = 540, y = 1351)
            it.tap(x = 540, y = 2130)
            it.tap(x = 540, y = 1351)
            it.tap(x = 540, y = 2130)
            it.tap(x = 216, y = 1864)
            it.tap(x = 540, y = 2130)

            describe("Wait for Page to Load")
                .waitForDisplay("Email Verification complete!")

            it.exist("Email Verification complete!")

            it.exist("Your email, $hiddenEmail has been verified. Tap Next to continue with your mobile verification.")

            //tap next
            //it.tap("\uE941")
            it.tap(x=927,y=2194)


        }

    }

    @Macro("[Verify Mobile Number]")
    fun verifyMobile(number:String, password: String, usePHNumber:Boolean, willSkipMobile:Boolean, skipErrorPin: Boolean, skipChangeMobile:Boolean, useInterational: Boolean, internationalCountryCode :String){

        describe("Wait for Page to Load")
            .waitForDisplay("Is it really you?", waitSeconds = 30.0)

        it.exist("Is it really you?")
        it.exist("Please enter your mobile number.")
        it.exist("Country")
        it.exist("Your mobile number")
        //it.exist("Skip")

        if(willSkipMobile){

            it.tap("Skip")

            describe("Wait for Page to Load")
                .waitForDisplay("Account Application", waitSeconds = 30.0)

            it.exist("Account Application")
            it.exist("Follow these easy steps to start investing")

        } else {

            //Tap next without mobile number
            //it.tap("\uE941")
            it.tap(x=927,y=2194)

            describe("Wait for Page to Load")
                .waitForDisplay("This field is required.")

            it.exist("This field is required.")

            if(useInterational){

                it.tap("Philippines (+63)")

                search(internationalCountryCode)

                //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
                it.exist(internationalCountryCode)

                if(internationalCountryCode == "Japan (+81)"){
                    it.tap("9012345678")
                        .sendKeys(number)
                } else if (internationalCountryCode == "Singapore (+65)"){
                    it.tap("81234567")
                        .sendKeys(number)
                } else if (internationalCountryCode == "United States (+1)"){
                    it.tap("2015550123")
                        .sendKeys(number)
                }

                //tap next

                //it.tap("\uE941")
                it.tap(x=927,y=2194)

                describe("Wait for Page to Load")
                    .waitForDisplay("Follow these easy steps to start investing")

                it.exist("Follow these easy steps to start investing")

            } else {

                //Enter Invalid Phone Number

                it.tap("9051234567")
                    .sendKeys("1234567890")

                it.exist("Invalid phone number.")

                if (usePHNumber) {

                    it.exist("Philippines (+63)")

                    it.tap("1234567890")
                        .clearInput()
                        .sendKeys(number)

                    it.dontExist("Invalid phone number")
                    it.dontExist("This field is required.")

                    //tap next

                    //it.tap("\uE941")
                    it.tap(x=927,y=2194)
                    it.tap(x=927,y=2194)

                    //wait for page to load

                    describe("Wait for Page to Load")
                        .waitForDisplay("Verify your mobile number")


                    it.exist("Verify your mobile number")

                    var hiddenPHNumber = obfuscatePHNumber(number)

                    if (!skipChangeMobile) {

                        it.exist("Please enter the code sent to your mobile number, $hiddenPHNumber")
                        it.exist("Didn’t receive the message?")
                        //it.exist("Not your mobile number?")
                        it.exist("Edit mobile number")

                        //Change mobile number

                        //New Mobile Number
                        val newPhoneNumber = changeLastDigit(number)

                        it.tap("Edit mobile number")
                        it.tap(number)
                            .clearInput()
                            .sendKeys(newPhoneNumber)

                        //tap next

                        //it.tap("\uE941")
                        it.tap(x=927,y=2194)
                        it.tap(x=927,y=2194)

                        describe("Wait for Page to Load")
                            .waitForDisplay("Verify your mobile number")


                        it.exist("Verify your mobile number")

                        hiddenPHNumber = obfuscatePHNumber(newPhoneNumber)

                        describe("Wait for Page to Load")
                            .waitForDisplay("Please enter the code sent to your mobile number, $hiddenPHNumber")

                        it.exist("Please enter the code sent to your mobile number, $hiddenPHNumber")

                        it.select("Please enter the code sent to your mobile number, $hiddenPHNumber")
                            .textIs("Please enter the code sent to your mobile number, $hiddenPHNumber")

                    }

                    //tap resend code

    //            it.tap("Resend code")
    //
    //            describe("Wait for Toast Message")
    //                .waitForDisplay("An activation code has been sent to your mobile phone")
    //
    //            it.exist("An activation code has been sent to your mobile phone")

                    if (!skipErrorPin) {

                        //Enter Invalid Code
                        val invalidMobileCode = "202066"
    //            invalidMobileCode.forEachIndexed { index, char ->
    //                it.tap("$char")

    //            }
                        describe("Wait for Page to Load")
                            .waitForDisplay("Verify your mobile number", waitSeconds = 30.0)

                        it.exist("Verify your mobile number")

                        it.tap(x = 540, y = 1351)
                        it.tap(x = 540, y = 2130)
                        it.tap(x = 540, y = 1351)
                        it.tap(x = 540, y = 2130)
                        it.tap(x = 863, y = 1625)
                        it.tap(x = 863, y = 1625)



                    }
    //
    //            it.exist("The activation code you entered is either Invalid or Expired. Please try requesting a new SMS activation code.")

                    val mobileCode = "202069"
    //            mobileCode.forEachIndexed { index, char ->
    //                it.tap("$char")
    //            }

    //            it.select("The activation code you entered is either Invalid or Expired. Please try requesting a new SMS activation code.")
    //                .textIs("The activation code you entered is either Invalid or Expired. Please try requesting a new SMS activation code.")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Verify your mobile number", waitSeconds = 30.0)

                    it.exist("Verify your mobile number")

                    it.tap(x = 540, y = 1351)
                    it.tap(x = 540, y = 2130)
                    it.tap(x = 540, y = 1351)
                    it.tap(x = 540, y = 2130)
                    it.tap(x = 863, y = 1625)
                    it.tap(x = 863, y = 1882)


                    describe("Wait for page to Load")
                        .waitForDisplay("Mobile Verification Complete")

                    it.exist("Mobile Verification Complete")

                    it.exist("Your mobile number, $hiddenPHNumber has been verified. Tap Done to continue with application form.")

                    it.tap("Done")

                }


            }

        }


    }


    @Macro("[Personal Info]")
    fun personalInfo(firstName: String, middleName:String, lastName: String , useMiddleName:Boolean, gender:String, civilStatus:String, birthDay:String, isPHUser: Boolean, birthPlace: String, birthCity:String, citizenship: String, hasDualCitizen: Boolean, randomDualCitizenship: String){
        // Tab 1
        it.tap("Complete Application Form")

        describe("Wait for Click to Load")
            .waitForDisplay("Application Form", waitSeconds = 30.0)

        it.exist("Application Form")
        it.exist("Personal Info")
        it.exist("Reminder: For faster application approval, please ensure that all provided information are true and correct.")

        it.exist("First Name*")
        it.exist(firstName)

        it.exist("Last Name*")

        //Only First Letter is Capitalized
        it.exist(lastName.lowercase().replaceFirstChar { it.uppercase() })

        //Tap Next without input

        it.selectWithScrollDown("Next").tap()

        it.exist("Civil Status*")
        it.exist("Citizenship*")

        it.scrollToTop()
        it.exist("Middle Name*")
        if(useMiddleName){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[4]/android.widget.EditText\n")
                .sendKeys(middleName)
            it.exist(middleName)
        } else {
            //Tap Checkbox
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[5]/android.view.View")
            it.dontExist(middleName)
        }

        it.hideKeyboard()

        //Random Gender
        it.exist("Gender*")

        if(gender == "Male"){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[9]/android.widget.TextView[1]")
        } else if (gender == "Female"){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[9]/android.widget.TextView[2]")
        }

        it.exist("Male")
        it.exist("Female")

        //Birthday
        it.exist("Birthday*")

        //tap date icon
        it.tap("\uF133")

        val MonthAndYear = monthYear18YearsAgo()

        it.selectWithScrollUp(MonthAndYear)
        it.exist(MonthAndYear)
        it.exist("Su")
        it.exist("Mo")
        it.exist("Tu")
        it.exist("We")
        it.exist("Th")
        it.exist("Fr")
        it.exist("Sa")

        it.tap("\uF133")

        it.dontExist(MonthAndYear)
        it.dontExist("Su")
        it.dontExist("Mo")
        it.dontExist("Tu")
        it.dontExist("We")
        it.dontExist("Th")
        it.dontExist("Fr")
        it.dontExist("Sa")

        it.exist(birthDay)

        //auto adjust to valid birthday
        it.exist(birthDay)

        //Birth Place Country
        it.scrollToBottom()

        it.exist("Birth Place (Country)*")
        if(!isPHUser){
            it.tap("xpath=(//android.view.View[@text=\"Philippines\"])[1]")
            it.selectWithScrollDown(birthPlace).tap()
            it.exist(birthPlace)
        }

        //Birth City
        it.exist("Birth Place (City/Municipality)*")
        it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[10]/android.widget.EditText")
            .sendKeys(birthCity)

        it.hideKeyboard()

        //Citizenship
        if(!isPHUser){
            it.tap("xpath=//android.view.View[@text=\"Philippines\"]")
            it.selectWithScrollDown(citizenship)
            it.exist(citizenship)
        }

        //Dual Citizenship

        if(hasDualCitizen){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingPersonalInfoForm\"]/android.view.View[13]/android.view.View")
            it.exist("Dual Citizenship*")
            it.tap("Citizen")
            it.selectWithScrollDown(randomDualCitizenship).tap()
            it.exist(randomDualCitizenship)
        }

        //Civil Status

        it.exist("Civil Status*")
        it.tap("Status")
        it.tap(civilStatus)


        //Tap Next
        it.tap("Next")
    }

    @Macro("[Fixed BirthPlace and Citizenship Scenario]")
    fun fixedBirthPlaceCitizenship( isPHUser:Boolean, birthDay: String, city:String, hasDualCitizen:Boolean, randomDualCitizenship:String, isUSPerson:Boolean, birthPlace:String, citizenship:String){
        //Page 2

        describe("Wait for Click to Load")
            .waitForDisplay("We wanna know you more!", waitSeconds = 30.0)

        it.exist("We wanna know you more!")
        it.exist("Fill in your birthday and address. Please make sure we got it right.")

        it.exist("When is your birthday?")

        //Tap Next without input

        it.wait(1)
        it.tapWithScrollDown("\uE941")
        it.exist("This field is required.")

        //Enter Invalid Birthday

        it.tap("MM/DD/YYYY")

        it.exist("Select your Birthday")

        // Split the string by '/'
        val parts = birthDay.split("/")

        // Ensure the date string is correctly formatted

        val month = "June"
        val day = parts[1]
        val year = parts[2]

        println("Month: $month") // Output: Month: 05
        println("Day: $day")     // Output: Day: 23
        println("Year: $year")   // Output: Year: 2005

        printDateRangeWithNext(day.toInt())

        printMonthRangeWithNext(month)

        printYearRangeWithNext(year.toInt())

        //Close

        it.tap("\uE908")

        it.exist(birthDay)

        it.dontExist("You must be 18 years old or above")


        // Country Born and Citizenship Variables



        it.exist("Where were you born?")

        it.exist("What is your citizenship?")

        it.dontExist("What is your dual citizenship?")

        if(!isPHUser){

            // Country Born
            describe("Wait for Click to Load")
                .waitForDisplay("xpath=(//android.widget.TextView[@text=\"Philippines\"])[1]\n", waitSeconds = 30.0)

            it.wait(1)
            it.exist("xpath=(//android.widget.TextView[@text=\"Philippines\"])[1]\n")

            it.tap("xpath=(//android.widget.TextView[@text=\"Philippines\"])[1]\n")

            search(birthPlace)

            it.exist(birthPlace)

            //Citizenship

            it.tap("xpath=//android.widget.TextView[@text=\"Philippines\"]")
            it.tap("Search")
                .sendKeys(citizenship)
            it.tap("xpath=//android.widget.TextView[@text=\"$citizenship\"]\n")

            it.exist(citizenship)

        }

        // City Born

        it.exist("What city were you born?")

        it.tap("ex. Manila")
            .sendKeys(city)

        it.hideKeyboard()

        if(hasDualCitizen){

            //tap Checkbox
            it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup\n")

            it.exist("What is your dual citizenship?")

            it.tap("ex. Philippines")

            search(randomDualCitizenship)

            it.exist(randomDualCitizenship)
        }



        it.scrollToBottom()

        // Are you US Person
        if(isUSPerson){
            it.tap("No")

            it.tap("Yes")

            //Just Incase test fails for less flaky test
            try {
                it.tap("Yes")
                it.tap("Yes")
            } catch (e: TestDriverException) {
                it.tap("\uE908")
            }

            it.exist("Yes")
        }

        //Tap Next
        it.tapWithScrollDown("\uE941")
    }

    @Macro("[Present and Permanent Address Info]")
    fun presentAddress(hasSamePermanentAddress:Boolean, usePHCountry:Boolean, country:String, province: String, addressCity:String, presentBarangay: String, streetAddress:String, zipCode:String, permanentProvince: String, permanentCity: String, permanentBarangay: String, permanentStreetAddress: String, permanentZipCode: String) {
        //Tab 2 Address
        //Tap Next without Input
        it.selectWithScrollDown("Next")
        it.tap("Next")
        it.exist("Permanent Address*")
        it.exist("If you currently reside in another country for work, we highly recommend using your Philippine address for faster clearing in our account opening.")
        it.scrollToTop()

        it.exist("Application Form")
        it.exist("Contact Info")
        it.exist("Reminder: For faster application approval, please ensure that all provided information are true and correct.")

        it.exist("Present Address*")
        it.exist("If you currently reside in another country for work, we highly recommend using your Philippine address for faster clearing in our account opening.")



        if(!usePHCountry){

            it.tap("Philippines")
            it.selectWithScrollDown(country).tap()
            it.exist(country)

            it.dontExist("Province")
            it.dontExist("City/Municipality")
            it.dontExist("Barangay")

        } else {
            //Use PH Country
            it.exist("Philippines")

            //Province
            it.tap("Province")
            it.selectWithScrollDown(province).tap()
            it.exist(province)

            //City
            it.tap("City/Municipality")
            it.selectWithScrollDown(addressCity).tap()
            it.exist(addressCity)

            //Barangay
            it.tap("Barangay")
            it.selectWithScrollDown(presentBarangay).tap()
            it.exist(presentBarangay)

        }

        //Street Address

        if(!usePHCountry){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[3]/android.widget.EditText")
                .sendKeys(streetAddress)
        } else {
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[6]/android.widget.EditText")
                .sendKeys(streetAddress)
        }

        it.exist(streetAddress)
        it.hideKeyboard()

        //Zip Code

        if(!usePHCountry){
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[4]/android.widget.EditText")
                .sendKeys(zipCode)
        } else {
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[6]/android.widget.EditText")
                .sendKeys(zipCode)
        }

        it.exist(zipCode)
        it.hideKeyboard()

        it.scrollToBottom()

        if(hasSamePermanentAddress){
            // Tick Checkbox
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[4]/android.view.View")
            
            it.dontExist("Province")
            it.dontExist("City/Municipality")
            it.dontExist("Barangay")
            it.dontExist("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[9]/android.widget.EditText")
            it.dontExist("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[10]/android.widget.EditText")
        } else {

            //Permanent Address

            if(!usePHCountry){

                it.tap("Philippines")
                it.selectWithScrollDown(country).tap()
                it.exist(country)

                it.dontExist("Province")
                it.dontExist("City/Municipality")
                it.dontExist("Barangay")

            } else {
                //Use PH Country
                it.exist("Philippines")

                //Province
                it.tap("Province")
                it.selectWithScrollDown(permanentProvince).tap()
                it.exist(permanentProvince)

                //City
                it.tap("City/Municipality")
                it.selectWithScrollDown(permanentCity).tap()
                it.exist(permanentCity)

                //Barangay
                it.tap("Barangay")
                it.selectWithScrollDown(permanentBarangay).tap()
                it.exist(permanentBarangay)

            }

            //Street Address

            if(!usePHCountry){
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[9]/android.widget.EditText")
                    .sendKeys(permanentStreetAddress)
            } else {
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[9]/android.widget.EditText")
                    .sendKeys(permanentStreetAddress)
            }

            it.exist(permanentStreetAddress)
            it.hideKeyboard()

            //Zip Code
            if(!usePHCountry){
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[10]/android.widget.EditText")
                    .sendKeys(permanentZipCode)
            } else {
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingContactForm\"]/android.view.View[10]/android.widget.EditText")
                    .sendKeys(permanentZipCode)
            }

            it.exist(permanentZipCode)
            it.hideKeyboard()
        }

        it.tap("Next")

    }
    @Macro("[Employment Info]")
    fun employmentInfo(employmentStatus: String, workTitle :String, industry: String, employerName:String, employerCountry:String, usePHEmployerCountry:Boolean, employerProvince:String, employerCity:String, employerBarangay: String, employerStreetAddress: String, employerZipCode: String, TINNumber:String, IDType:String, IDNumber:String, citizenship:String, hasSocialID:Boolean, isPHUser: Boolean): String {
        //Employment Info Form

        it.selectWithScrollDown("Next").tap()
        it.exist("SSS/GSIS Number *")
        it.exist("Tax Identification Number (TIN) *")
        it.scrollToTop()

        it.exist("Application Form")
        it.exist("Source of Income")
        it.exist("Reminder: For faster application approval, please ensure that all provided information are true and correct.")


        it.exist("Employment Status*")
        it.tap("xpath=(//android.view.View[@text=\"Select\"])[1]")
        it.tap(employmentStatus)
        it.exist(employmentStatus)

        if(employmentStatus !== "Unemployed" && employmentStatus !== "Retired" &&  employmentStatus !== "Student" && employmentStatus !== "Stay-at-home Parent"){

            //Work Title
            it.exist("Occupation/Position Title*")
            it.tap("xpath=(//android.view.View[@text=\"Select\"])[1]")
            it.selectWithScrollDown(workTitle)
            it.exist(workTitle)

            //Employer Name
            it.exist("Employer/Business Name*")
            it.tap("xpath=//android.widget.EditText")
                .sendKeys(employerName)
            it.exist(employerName)
            it.hideKeyboard()

            //Industry
            it.exist("Nature of Business*")
            it.tap("xpath=//android.view.View[@text=\"Select\"]")
            it.selectWithScrollDown(industry).tap()
            it.exist(industry)

            //Employer Address

            if(!usePHEmployerCountry){
                it.exist("Business Address*")
                it.tap("xpath=//android.view.View[@text=\"Philippines\"]")
                it.selectWithScrollDown(employerCountry).tap()
                it.exist(employerCountry)

                it.dontExist("Province")
                it.dontExist("City")
                it.dontExist("Barangay")

            } else {
                //Use PH Country
                it.exist("Philippines")

                //Province
                it.tap("Province")
                it.selectWithScrollDown(employerProvince).tap()
                it.exist("*$employerProvince*")

                //City
                it.tap("City/Municipality")
                it.selectWithScrollDown(employerCity).tap()
                 it.exist(employerCity)

                //Barangay
                it.tap("Barangay")
                it.selectWithScrollDown(employerBarangay).tap()
                it.exist(employerBarangay)

                }

            //Street Address
            if(!usePHEmployerCountry){
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[7]/android.widget.EditText")
                    .sendKeys(employerStreetAddress)
            } else {
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[6]/android.widget.EditText")
                    .sendKeys(employerStreetAddress)
            }

            it.exist(employerStreetAddress)
            it.hideKeyboard()

            //Zip Code

            if(!usePHEmployerCountry){
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[8]/android.widget.EditText")
                    .sendKeys(employerZipCode)
            } else {
                it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[7]/android.widget.EditText")
                    .sendKeys(employerZipCode)
            }
            it.exist(employerZipCode)
            it.hideKeyboard()

        }

        it.exist("Tax Identification Number (TIN) *")

        when (employmentStatus) {
            "Employed", "Self Employed", "Business Owner", "Overseas Worker" -> {

                if(!usePHEmployerCountry){
                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[10]/android.widget.EditTexts")
                        .sendKeys(TINNumber)
                } else {
                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[9]/android.widget.EditText")
                        .sendKeys(TINNumber)
                }

                it.exist(TINNumber)
                it.hideKeyboard()

                it.exist("SSS/GSIS Number *")

                it.tap("xpath=//android.view.View[@text=\"Select\"]")
                it.tap(IDType)
                it.exist(IDType)

                if(!usePHEmployerCountry){
                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[14]/android.widget.EditText")
                        .sendKeys(IDNumber)
                } else {
                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[13]/android.widget.EditText")
                        .sendKeys(IDNumber)

                }

                it.hideKeyboard()

            }


            "Unemployed", "Retired", "Student", "Stay-at-home Parent" -> {

                it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[4]/android.widget.EditText")
                    .sendKeys(TINNumber)
                it.exist(TINNumber)
                it.hideKeyboard()

                if(hasSocialID){

                    it.tap("xpath=//android.view.View[@text=\"Select\"]")
                    it.tap(IDType)
                    it.exist(IDType)

                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[9]/android.widget.EditText")
                        .sendKeys(IDNumber)
                    it.hideKeyboard()
                } else {

                    //If No ID
                    it.tap("xpath=//android.view.View[@resource-id=\"onboardingSourceIncome\"]/android.view.View[8]/android.view.View")

                }


            }


        }


        it.tap("Next")

        return employerBarangay
    }

    @Macro("[PSE Disclosures]")
    fun disclosuresPSE(isCorporateOfficer:Boolean, isBrokerOfficer:Boolean, isExistingAccountHolder:Boolean, corporateName:String, corporatePosition:String, brokerName:String, brokerPosition:String, brokers:List<String>, isPoliticallyExpose:Boolean, isUSPerson: Boolean){
        //PSE Disclosures

        //tap without info
        it.selectWithScrollDown("Next")
        it.exist("Are you a US Person?")
        it.exist("Fill in your employment details and make sure we got it right.Are you a Politically-Exposed Person (PEP) or a relative/close associate of a PEP?")

        it.scrollToTop()
        it.exist("Application Form")
        it.exist("Reminder: For faster application approval, please ensure that all provided information are true and correct.")

        it.exist("I am a/an")
        it.exist("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[2]/android.view.View")
        it.exist("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[3]/android.view.View")
        it.exist("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[4]/android.view.View")
        it.exist("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[5]/android.view.View")


        if(isCorporateOfficer){
            it.tap("I'm a corporate officer or director of a PSE listed company")
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[2]/android.view.View/android.widget.EditText[1]")
                .sendKeys(corporateName)

            it.hideKeyboard()
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[2]/android.view.View/android.widget.EditText[2]")
                .sendKeys(corporatePosition)

            it.hideKeyboard()
        }

        if(isBrokerOfficer){
            it.tap("I’m a director, officer or employee of another broker/dealer")
            it.exist("You will be asked to upload a consent letter from the broker later.")
            it.tap("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[3]/android.view.View/android.widget.EditText[1]")
                .sendKeys(brokerName)

            it.hideKeyboard()

            it.tap("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[3]/android.view.View/android.widget.EditText[2]")
                .sendKeys(brokerPosition)

            it.hideKeyboard()
        }


        if(isExistingAccountHolder){

            it.tap("I'm an existing account holder of other brokers")

            it.exist("Please specify*")

            it.tap("xpath=//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[4]/android.view.View/android.widget.EditText")
                .sendKeys("Broker 0")

            it.hideKeyboard()

            brokers.forEachIndexed { index, broker ->
                it.tap("+ Add Broker")
                it.exist("Remove")

                val adjustedIndex = index + 2

                it.tap("//android.view.View[@resource-id=\"onboardingDisclosure\"]/android.view.View[2]/android.view.View/android.widget.EditText[$adjustedIndex+1]")
                    .sendKeys(broker)
                it.hideKeyboard()
            }
        }

        if(!isCorporateOfficer && !isBrokerOfficer && !isExistingAccountHolder){
            it.tap("None of the above")
        }

        it.scrollToBottom()

        it.exist("Are you a Politically-Exposed Person (PEP) or a relative/close associate of a PEP?")
        if(isPoliticallyExpose){
            it.tap("xpath=(//android.view.View[@text=\"Yes\"])[1]")
        } else {
            it.tap("xpath=(//android.view.View[@text=\"No\"])[1]")
        }

        it.exist("A PEP is an individual who is or has been entrusted with a prominent public position in the Philippines (or a foreign state) with substantial authority over policy, operations, or the use or allocation of government-owned resources.")

        it.exist("Are you a US Person?")
        it.exist("US citizen, green card holder, resident, born in any US territory.")

        if(isUSPerson){
            it.tap("xpath=(//android.view.View[@text=\"Yes\"])[2]")
        } else {
            it.tap("xpath=(//android.view.View[@text=\"No\"])[2]")
        }

        it.exist("By answering this, you are agreeing with the")
        it.exist("FATCA Agreement")

        it.tap("FATCA Agreement")

        it.exist("FATCA Agreement")
        it.exist("The Client is aware that InvestaTrade registered with the United States Internal Revenue Service (“IRS”) and consents to InvestaTrade’s compliance with the requirements under the FATCA, as the same may be amended from time to time, and its implementing rules and regulations.")
        it.scrollToBottom()
        it.wait(1)
        it.exist("Reliance. The Client understands the InvestaTrade will rely and act on the basis of the Client’s disclosures to InvestaTrade. The above notwithstanding, InvestaTrade retains the right to verify the same from whatever sources it may consider appropriate. Any misrepresentation regarding the Client’s status shall be a ground for termination of the Client’s account.")
        it.tap("Close")

        it.exist("For questions, please visit our")
        it.exist("FAQ Section")

        it.tap("Next")
    }

    @Macro("[Financial Profile]")
    fun sourcesIncome(annualIncome:String, sourcesOfIncome:List<String>, allSourceIncome:List<String>, isPoliticallyExpose: Boolean){
        // Source of Income Page

        //tap without info
        it.selectWithScrollDown("Submit").tap()
        it.exist("This field is required.")

        it.exist("Reminder: For faster application approval, please ensure that all provided information are true and correct.")
        it.exist("Fill in your employment details and make sure we got it right.")
        it.exist("What is your annual income?")



        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")

        it.exist("Select annual income")
        it.tap(annualIncome)

        it.exist(annualIncome)

        it.exist(  "What is your source of income?")

        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")

        allSourceIncome.forEach { source:String ->
            it.exist(source)
        }

        it.exist("Select source of income")
        sourcesOfIncome.forEach { source:String ->
            it.tap(source)
        }

        //Close
        it.tap("\uE908")

        it.exist("Are you Politically-Exposed Person (PEP) or a relative/close associate of a PEP?")

        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup")
        it.exist("Are you Politically-Exposed Person (PEP)")

        if(isPoliticallyExpose){
            it.tap("Yes")
            it.exist("Yes")
        } else {
            it.tap("No")
            it.exist("No")
        }

        it.dontExist("This field is required.")

        // Next Page
        it.tap("\uE941")
    }

    @Macro("[Objectives, Assets, Net Worth]")
    fun objectivesAssetsNetWorth(rankedObjectives:List<String>, assetsRange:String, netWorthRange:String){
        //Investment Objectives, Assets, and Net Worth
        it.exist( "Fill in the details and make sure we got it right.")

        //tap without info
        it.tap("\uE941")
        it.exist("This field is required.")

        //Investment Objectives
        it.exist("What is your investment objectives?")
        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
        it.exist("Select investment objectives")
        it.exist("Pleases choose according to ranking")

        rankedObjectives.forEach { objective:String ->
            it.tap(objective)
        }

        //Close
        it.tap("\uE908")


        //Assets
        it.exist("Your assets are valued at around?")
        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
        it.exist("Select value")
        it.tap(assetsRange)
        it.exist(assetsRange)

        //Net Worth
        it.exist("Your net worth are valued at around?")
        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
        it.exist("Select value")
        it.tap(netWorthRange)
        it.exist(netWorthRange)

        it.dontExist("This field is required.")

        it.tap("\uE941")

    }

    @Macro("[FATCA Agreement]")
    fun fatcaAgreement(isFormW9:Boolean, fatcaList:List<String>, USContactNumber:String, USTINNumber:String,USState:String, USCity:String, USStreetAddress:String, USZipCode:String, willEnterUSAddress:Boolean){
        it.exist("FATCA Agreement")
        it.exist("In compliance with the PSE regulations, account owners must agree to")

        //Tap Fatca Agreement
        it.tap("FATCA Agreement.")

        describe("Wait for page to load")
            .waitForDisplay("FATCA Registration", waitSeconds = 30.0)

        it.exist("FATCA Agreement")
        it.exist("FATCA Registration")
        it.exist("The Client is aware that Investa Trade is registered with the United States Internal Revenue Service (“IRS”) and consents to Investa Trade’s compliance with the requirements under the FATCA, as the same may be amended from time to time, and its implementing rules and regulations.")

        it.scrollToBottom()

        it.exist("Reliance")
        it.exist("The Client understands the InvestaTrade will rely and act on the basis of the Client’s disclosures to InvestaTrade. The above notwithstanding, InvestaTrade retains the right to verify the same from whatever sources it may consider appropriate. Any misrepresentation regarding the Client’s status shall be a ground for termination of the Client’s account.")
        it.exist("BACK TO TOP")

        it.wait(1)

        it.tap("BACK TO TOP")

        it.exist("FATCA Agreement")

        //Close
        it.tap("\uE944")

        //FATCA Question

//        it.tap("For questions, please visit our FAQ Section.")
//
//        describe("Wait for page to load")
//            .waitForDisplay("Frequently Asked Questions (FAQs)", waitSeconds = 30.0)
//
//        it.exist("Frequently Asked Questions (FAQs)")


        //Go Back
//        it.tap("\uE909")

        it.exist("You are a?")

        //Tap without info
        it.tap( "\uE941")
        it.exist("This field is required.")

        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
        it.tap("xpath=//*[@class=\"android.view.ViewGroup\" and ./*[@text=\"Select\"]]")

        it.exist("Select what applies to you")

        describe("Wait for page to load")
            .waitForDisplay("Born in the US or any US territory", waitSeconds = 30.0)

        //Tap Choices
        fatcaList.forEach { fatca ->


            it.tap(fatca)

        }

        //close
        it.tap(  "\uE908")

        if(isFormW9){

            it.exist( "You will be asked to upload an IRS Form W-9 and one US identification document later.")

            //Contact Number
            it.exist("What is your US contact number")

            it.tap("e.g 650-453-3455")
                .sendKeys(USContactNumber)

            it.exist(USContactNumber)

            it.hideKeyboard()

            it.exist("What is your US TIN is?")

            it.tap("Enter TIN")
                .sendKeys(USTINNumber)

            it.exist(USTINNumber)

            it.hideKeyboard()

            it.exist("What is your US Address?")

            it.scrollToBottom()

            it.exist("Fill in your US address and make sure we got it right.")

            it.wait(1)

            it.exist("State")

            describe("Wait for page to load")
                .waitForDisplay("Enter State", waitSeconds = 30.0)

            it.tap("Enter State")
                .sendKeys(USState)

            it.exist(USState)

            it.hideKeyboard()

            it.exist("City")

            it.tap("Enter City")
                .sendKeys(USCity)

            it.exist(USCity)

            it.hideKeyboard()

            it.exist("Street Address, Building , House No.")

            it.tap("Enter your address")
                .sendKeys(USStreetAddress)

            it.exist(USStreetAddress)

            it.hideKeyboard()

            it.exist("Zip Code")

            it.tap("Enter zip code")
                .sendKeys(USZipCode)

            it.exist(USZipCode)

            it.hideKeyboard()

            it.dontExist( "This field is required.")

            it.tap( "\uE941")



        } else {

            it.exist("You will be asked to upload an IRS Form W-8BEN and one non-US government issued identification document.")

            it.exist("What is your US Address?")

            it.scrollToBottom()

            it.wait(1)
            it.exist("Fill in your US address and make sure we got it right. If none, tap Next.")


            if(willEnterUSAddress){

                describe("Wait for page to load")
                    .waitForDisplay("Enter State", waitSeconds = 30.0)

                it.exist("State")

                it.tap("Enter State")
                    .sendKeys(USState)

                it.exist(USState)

                it.hideKeyboard()

                it.exist("City")

                it.tap("Enter City")
                    .sendKeys(USCity)

                it.exist(USCity)

                it.hideKeyboard()

                it.exist("Street Address, Building , House No.")

                it.tap("Enter your address")
                    .sendKeys(USStreetAddress)

                it.exist(USStreetAddress)

                it.hideKeyboard()

                it.exist("Zip Code")

                it.tap("Enter zip code")
                    .sendKeys(USZipCode)

                it.exist(USZipCode)

                it.hideKeyboard()

                it.dontExist( "This field is required.")

                it.tap( "\uE941")
            }
        }



    }


    @Macro("[Review Application]")
    fun reviewApplication(firstName: String, middleName:String, lastName: String, useMiddleName:Boolean, gender:String, civilStatus:String, isPHUser:Boolean, birthDay: String, city:String, hasDualCitizen:Boolean, citizenship:String, randomDualCitizenship:String, birthPlace:String, isUSPerson:Boolean, hasSamePermanentAddress:Boolean, usePHCountry:Boolean, country:String, province: String, addressCity:String, presentBarangay:String, streetAddress:String, zipCode:String, permanentProvince:String, permanentCity: String, permanentBarangay:String, permanentStreetAddress: String, permanentZipCode: String, employmentStatus: String, workTitle :String, industry: String, employerName:String, employerCountry:String, usePHEmployerCountry:Boolean, employerProvince:String, employerCity:String, employerBarangay:String, employerStreetAddress: String, employerZipCode: String, TINNumber:String, IDType:String, IDNumber:String, isCorporateOfficer:Boolean, isBrokerOfficer:Boolean, isExistingAccountHolder:Boolean, corporateName:String, corporatePosition:String, brokerName:String, brokerPosition:String, brokers:List<String>, annualIncome:String, sourcesOfIncome:List<String>, isPoliticallyExpose:Boolean, allSourceIncome:List<String>, rankedObjectives:List<String>, assetsRange:String, netWorthRange:String, isFormW9: Boolean, fatcaList: List<String>, USContactNumber: String, USTINNumber: String, USState: String, USCity: String, USStreetAddress: String, USZipCode: String, willEnterUSAddress:Boolean, hasSocialID:Boolean){
        it.exist("Review your Application")
        it.exist("Make sure that you fill in your application details and make sure we got it right.")

        //Name

        it.exist("Name")

        if(useMiddleName){
            it.exist("$firstName $middleName $lastName")
        } else {
            it.exist("$firstName $lastName")
        }


        //Sex

        it.exist("Sex")
        it.exist(gender)

        //Birthday

        it.exist("Birthday")
        it.exist(birthDay)

        //BirthPlace

        it.exist("Birth Country")

        if(isPHUser){
            it.exist("Philippines")
        } else {
            it.exist(birthPlace)
        }

        //BirthCity

        it.exist("Birth City")
        it.exist(city)

        //Citizenship

        it.exist("Citizenship")
        if(isPHUser){
            println("Citizenship Philippines")
            it.exist("Philippines")
        } else {
            println("Citizenship $citizenship")
            it.exist(citizenship)
        }

        //Dual Citizenship

        it.select("Citizenship")
            .swipeTo("Name")

        //it.swipePointToPoint(startX = 537 , startY = 2194, endX = 537 , endY =780 )

        if(hasDualCitizen){
            it.exist("Dual Citizenship")
            println("Dual Citizenship $randomDualCitizenship")
            it.exist(randomDualCitizenship)
        }


        //Civil Status

        it.exist("Civil Status")
        it.exist(civilStatus)

        //Address
        if(hasSamePermanentAddress){

            it.exist("Present Address & Permanent Address")

            if(usePHCountry){

                it.exist("$streetAddress, $presentBarangay, $addressCity, $province, Philippines, $zipCode")


            } else {

                it.exist("$streetAddress, $country, $zipCode")

            }


        } else {


            if(usePHCountry){

                it.exist("Present Address")
                it.exist("$streetAddress, $presentBarangay, $addressCity, $province, Philippines, $zipCode")

                it.exist("Permanent Address")
                it.exist("$permanentStreetAddress, $permanentBarangay, $permanentCity, $permanentProvince, Philippines, $permanentZipCode")

            } else {
                it.exist("Present Address")
                it.exist("$streetAddress, $country, $zipCode")

                it.exist("Permanent Address")
                it.exist("$permanentStreetAddress, $country, $permanentZipCode")
            }

        }


        //Employment Status

        it.exist("Employment Status")
        it.exist(employmentStatus)

        //it.swipePointToPoint(startX = 537 , startY = 2194, endX = 537 , endY =950 )

        if(employmentStatus !== "Unemployed" && employmentStatus !== "Retired" &&  employmentStatus !== "Student" && employmentStatus !== "Stay-at-home Parent"){
            it.exist("Occupation")
            it.exist(workTitle)

            it.exist("Employer/Business Name")
            it.exist(employerName)

            it.selectWithScrollDown("Nature of Business")
            it.exist("Employer Address")

            if(usePHEmployerCountry){
                it.exist("$employerStreetAddress, $employerBarangay, $employerCity, $employerProvince, Philippines, $employerZipCode")
            } else {
                it.exist("$employerStreetAddress, $employerCountry, $employerZipCode")
            }

            it.exist("Nature of Business")
            it.exist(industry)

        }

        //TIN

        it.exist("TIN No.")
        it.exist(TINNumber)


        //GSIS/SSS


        when (employmentStatus) {
            //If employed must have ID
            "Employed", "Self Employed", "Business Worker", "Overseas Worker" -> {

               // it.selectWithScrollDown("$IDType No.")

                it.exist("$IDType No.")
                it.exist(IDNumber)


            }

            //If unemployed ID is optional
            "Unemployed", "Retired", "Student", "Stay-at-home Parent" -> {

                if(hasSocialID){
                   // it.selectWithScrollDown("$IDType No.")

                    it.exist("$IDType No.")
                    it.exist(IDNumber)



                } else {

                   // it.selectWithScrollDown("ID No.")

                    it.exist("ID No.")
                    it.exist("-- --")



                }
            }
        }


        //Corporate Officer

        if(isCorporateOfficer){

            it.selectWithScrollDown("Officer / Director of a listed corporation?")

            it.exist("Officer / Director of a listed corporation?")
            it.exist("Yes")
            it.exist("Company Name")
            it.exist(corporateName)
        }


        //Broker Officer
        if(isBrokerOfficer){

            it.selectWithScrollDown("Employed or associated with another Broker or Dealer?")
            it.exist("Employed or associated with another Broker or Dealer?")

            it.exist("Yes")

            it.selectWithScrollDown("Broker/Dealer Name")
            it.exist("Broker/Dealer Name")
            it.exist(brokerName)
        }

        //Existing Account Holder

        if(isExistingAccountHolder){
            it.selectWithScrollDown("Politically exposed Person (PEP) or relative/close associate of PEP")
            it.exist("Existing account holder of other brokers")

            brokers.forEach { broker:String ->
                it.exist("• $broker")
            }
        }



        //Politically Expose
        it.selectWithScrollDown("Annual Income")
        it.exist("Politically exposed Person (PEP) or relative/close associate of PEP")
        if(isPoliticallyExpose){
            it.exist("Yes")
        } else {
            it.exist("No")
        }

        //Annual Income
        it.exist("Annual Income")
        it.exist(annualIncome)


        //Source of Income
        it.selectWithScrollDown("Assets Valued")
        it.exist("Source of income")

        sourcesOfIncome.forEachIndexed { index, source: String ->
            it.exist("${index + 1}. $source")
        }

        //Investment Objectives
        it.exist("Investment Objectives")


//        rankedObjectives.forEach { objective:String ->
//            it.exist("• $objective")
//        }

        rankedObjectives.forEachIndexed { index, objective: String ->
            it.exist("${index + 1}. $objective")
        }

        //Assets Valued

        it.scrollToBottom()

        it.exist("Assets Valued")
        it.exist(assetsRange)



        //Net Worth

        it.exist("Net worth")
        it.exist(netWorthRange)

        //US Person

        it.exist("Are you a US Person")
        if(isUSPerson){
            it.exist("Yes")
            it.exist("FATCA")


            val modifiedList = fatcaList.map { text ->
                if (text.contains("I granted a power of attorney")) {
                    "I granted a power of attorney with US address"
                } else {
                    text
                }
            }

            modifiedList.forEach { fatca:String ->
                it.exist("• $fatca")
            }

            if(isFormW9){

                it.exist("US contact No.")
                it.exist(USContactNumber)

                it.exist("US TIN")
                it.exist(USTINNumber)



            }

            it.exist("US Address")

            if(willEnterUSAddress){
                it.exist("$USStreetAddress, $USCity, $USState, United States, $USZipCode")
            } else {
                it.exist("-- --")
            }
        } else {
            it.exist("No")
        }

        it.tap("\uE941")

        //Confirm Modal

        it.exist("Are you sure you want to submit your application form?")
        it.exist("By submitting this form, I hereby certify that the information I provided herein is true, accurate and complete, and I agree to notify/update the entity of any change in any of the information supplied in this form.")
        it.exist("I agree")

        it.tap("I agree")

        describe("Wait for page to load")
            .waitForDisplay("Wow! you are now done with your application form!", waitSeconds = 30.0)

        it.exist("Wow! you are now done with your application form!")

        it.exist("Your application is now under review for approval! Tap done to proceed")

        it.exist("Done")

        it.tap("Done")


    }

    @Macro("[Sign Agreement]")
    fun signAgreement(scrollAgreement: Boolean, broker: String){
        it.tap("Sign Agreement")



        if(broker == "1"){

            describe("Wait for page to load")
                .waitForDisplay("ONLINE TRADING AGREEMENT", waitSeconds = 30.0)

            it.wait(1)

            it.scrollDown()
            it.scrollToTop()

            it.exist("ONLINE TRADING AGREEMENT")

            it.exist("I read and agree to the terms and conditions")

            it.exist("The following terms and conditions govern the use of the trading platform called the “InvestaTrade Platform” through the brokerage Investors Securities, Inc. (ISI). In consideration of ISI acting as Broker for the undersigned, I (the Client) hereby formalize and confirm the opening of an Online Trading Facility (OTF) with ISI and hereby consent and agree to the following:")

            if(scrollAgreement) {
                it.scrollToBottom()

            }

            it.exist("I read and agree to the terms and conditions")

            it.exist("Next")

            //tap checkbox
            try {
                it.tap("xpath=//android.webkit.WebView[@text=\"InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View")

            } catch (e: TestDriverException) {
                it.tap("xpath=//android.webkit.WebView[@text=\"InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.TextView[2]")

            }

            it.tap("Next")

            describe("Wait for page to load")
                .waitForDisplay("Consent to trade REITs", waitSeconds = 30.0)

            it.scrollToBottom()
            it.scrollToTop()

            it.wait(1)

            it.exist("Consent to trade REITs")
            it.exist("Real Estate Investement Trust (REIT)")
            it.exist("Creation of NOCD Sub-Accounts")

            it.scrollToBottom()

            //it.select("*In connection with my intention*")
            //    .textContains("In connection with my intention to subscribe/buy REIT in The Philippines Stock Exchange, Inc. and the Republic Act of 10173 or Date Privacy Act of 2012, I hereby give my written consent to Investors Securities, Inc. for the collection, processing and sharing of my information to be used to set-up my Name on Central Depository (“NoCD”) sub-account to Philippine Depository & Trust Corporation (PDTC).")

            it.exist("I read and agree to the terms and conditions")

            it.exist("Submit")

            //tap checkbox

            try {
                it.tap("xpath=//android.view.View[@resource-id=\"displayTermsAndConditionForREITs\"]/android.view.View/android.view.View")

            } catch (e: TestDriverException) {
                it.tap("xpath=//android.webkit.WebView[@text=\"InvestaTrade\"]/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.view.View/android.widget.TextView[2]")

            }

            it.tap("Submit")

            describe("Wait for page to load")
                .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)

            it.exist("Follow these easy steps to start investing")

        } else if (broker == "2"){

            it.scrollUp()

            describe("Wait for page to load")
                .waitForDisplay("I read and agree to the terms and conditions", waitSeconds = 30.0)


//            it.exist("ONLINE TRADING SERVICE AGREEMENT")
//            it.exist("TERMS AND CONDITIONS")

            it.exist("I read and agree to the terms and conditions")

            //it.exist("This terms and conditions set out the rights and obligations of you, the Customer, and us, Fidelity Securities, Inc. (“The Broker”), in connection with your use of the Online Trading Platform (“The Platform”). In consideration of the Broker accepting and maintaining an account for you, you hereby agree that you have read, understand, consent and agree to all terms and conditions contained in the following Agreement:")


            if(scrollAgreement) {
                it.scrollToBottom()

            }

            it.exist("I read and agree to the terms and conditions")

            it.exist("Next")

            //tap checkbox

            it.tap("xpath=//android.webkit.WebView[@text=\"InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.TextView[2]")


            it.tap("Next")

            it.wait(1)

            describe("Wait for page to load")
                .waitForDisplay("I read and agree to the terms and conditions", waitSeconds = 30.0)

            it.scrollUp()

            it.wait(1)

//            it.exist("Consent to trade REITs")
//            it.exist("Real Estate Investement Trust (REIT)")
//            it.exist("Creation of NOCD Sub-Accounts")
//            it.exist("Client Consent Form")
//
//            it.exist("I understand that for me to subscribe/purchase, trade, and continue hodling REIT products, INVESTA (the “Platform”), is required under the Amended REIT Listing Rules to create a Name-on Central Depository sub-account on my behalf to hold, manage, and administer my REIT Assets.")

            if(scrollAgreement) {
                it.scrollToBottom()

            }

            it.exist("I read and agree to the terms and conditions")

            it.exist("Submit")

            //tap checkbox
            it.tap("xpath=//android.view.View[@resource-id=\"displayTermsAndConditionForREITs\"]/android.view.View/android.view.View/android.view.View/android.widget.TextView[2]")

            it.tap("Submit")

            describe("Wait for page to load")
                .waitForDisplay("Follow these easy steps to start investing", waitSeconds = 30.0)

            it.exist("Follow these easy steps to start investing")
        }



    }

    @Macro ("[Upload Valid ID]")
    fun uploadDocuments(isBrokerOfficer: Boolean, isUSPerson: Boolean, validIDType:String, fullName:String, idNumber: String, useCamera:Boolean, withPermissions:Boolean){


        it.wait(1)

        describe("Wait for page to load")
            .waitForDisplay("Upload Documents", waitSeconds = 30.0)


        it.tap("Upload Documents")

        it.exist("Upload Documents")
        it.exist("Please provide all necessary documents for faster approval.")
        it.exist("Valid ID and E-Signature")
        it.exist("Video Selfie")


        if(isBrokerOfficer){
            it.exist("Consent Letter")
        }

        //Upload Valid ID and E-Signature
        it.tap("Valid ID and E-Signature")
        it.exist("Valid ID")
        it.exist("Please provide all necessary documents for faster approval.")

        it.exist("Valid ID")
        it.exist("Selfie with your ID")
        it.exist( "Signature")

        //Check for Disabled Buttons

        it.tap("Selfie with your ID")
        it.exist("Valid ID")
        it.exist("Signature")

        it.tap("Signature")
        it.exist("Valid ID")
        it.exist("Selfie with your ID")

        //Valid ID
        it.tap("Valid ID")
        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")

        it.exist("Your Valid ID Details")
        it.exist("Please provide all necessary documents for faster approval.")
        it.exist("What is your Valid ID?")

        it.wait(1)

        //Tap without info
        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for Home Page")
            .waitForDisplay("This field is required.", waitSeconds = 30.0)

        it.exist("This field is required.")

        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")

        it.exist("Select Valid ID")
        it.selectWithScrollDown(validIDType).tap()
        it.exist(validIDType)

        it.exist("What is your complete name on ID?")
        it.exist(fullName)

        it.exist("ID Number")
        it.tap("Enter number")
            .sendKeys(idNumber)

        it.hideKeyboard()


        it.exist(idNumber)

        if(validIDType !== "SSS" && validIDType !== "UMID Card" && validIDType !== "Voter's ID"  && validIDType !== "PhilSys ID" && validIDType !==  "OFW ID" && validIDType !== "PhilHealth ID" && validIDType !== "Senior Citizen ID" && validIDType !== "School ID Signed by Principal/Head"){
            it.exist("Your ID is valid until?")
            it.tap("MM/DD/YYYY")
            it.tap("Select expiry date")

            //Move Date to Invalid Expiry Date
            val thisMonth = getMonthToday()
            val validExpiryDate = getDateSixMonthsFromNow()

            if(validIDType !== "Police Clearance" && validIDType !== "NBI Clearance") {
                it.select(thisMonth)
                    .swipePointToPoint(381, 2102, 381, 1740, repeat = 3)

                //Compare if year is next year

                val currentYear = getCurrentYear()
                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                val date = LocalDate.parse(validExpiryDate, formatter)

                println("Current Year$currentYear")
                println("Expetected Year " + date.year)

                //Move to next year
                if(currentYear !=  date.year){
                    it.select(currentYear.toString())
                        .swipePointToPoint(725,1937,725,1684)
                }
            }

            if (validIDType == "Police Clearance" || validIDType == "NBI Clearance") {
                it.select(thisMonth)
                    .swipePointToPoint(381, 2102, 381, 1918)
            }

            val dayToday = getDayToday()

            //Swipe to Previous Day
            it.select(dayToday.toString())
                .swipePointToPoint(startX = 560, startY = 2106, endX =560, endY = 2290 )



            //Tap Close
            //it.tap("\uE908")
            it.tap(x=1010,y=1762)

            if(validIDType !== "Police Clearance" && validIDType !== "NBI Clearance") {

                describe("Wait for Page to Load")
                    .waitForDisplay("Expiration date must be more than 6 months (180 days) from now.", waitSeconds = 30.0)

                it.exist("Expiration date must be more than 6 months (180 days) from now.")

                val invalidIDExpired = getDateSixMonthsMinusOneDay()

                describe("Wait for Page to Load")
                    .waitForDisplay(invalidIDExpired, waitSeconds = 30.0)

                it.exist(invalidIDExpired)

                it.tap(invalidIDExpired)


            }
            if (validIDType == "Police Clearance" || validIDType == "NBI Clearance") {

                describe("Wait for Home Page")
                    .waitForDisplay("Expiration date must be more than 1 month (29 days) from now.", waitSeconds = 30.0)


                it.exist("Expiration date must be more than 1 month (29 days) from now.")

                val invalidIDExpired = getDateOneMonthMinusOneDay()

//                describe("Wait for Page to Load")
//                    .waitForDisplay(invalidIDExpired, waitSeconds = 30.0)
//
//                it.exist(invalidIDExpired)
//
//                it.tap(invalidIDExpired)

                it.tap(x=184, y=1606)

                it.wait(2)

            }


            val previousDay = getPreviousDay()

            it.select(previousDay.toString())
                .swipePointToPoint(startX = 560, startY = 2106, endX =560, endY = 1923 )



            //Tap Close
            //it.tap("\uE908")
            it.tap(x=1010,y=1762)

            describe("Wait for Page to Load")
                .waitForDisplay(validExpiryDate, waitSeconds = 30.0)



            it.exist(validExpiryDate)

            if(validIDType !== "Police Clearance" && validIDType !== "NBI Clearance") {


                it.dontExist("Expiration date must be more than 6 months (180 days) from now.")

            }
            if (validIDType == "Police Clearance" || validIDType == "NBI Clearance") {


                it.dontExist("Expiration date must be more than 1 month (29 days) from now.")
            }


        }



        //Tap Next

        //it.tap("\uE941")
        it.tap(x=927,y=2194)
        it.tap(x=927,y=2194)

        if(!withPermissions){

            describe("Wait for Page to Load")
                .waitForDisplay("Allow InvestaTrade-Dev to take pictures and record video?", waitSeconds = 30.0)


            it.exist("Allow InvestaTrade-Dev to take pictures and record video?")
            it.exist("While using the app")
            it.exist("Only this time")
            it.exist("Don’t allow")

            it.tap("While using the app")
        }

        //ID Selfie

        val validIDTypes = setOf("Passport", "PRC ID", "PhilSys ID", "PhilHealth ID")

        if (validIDType in validIDTypes) {
            describe("Wait for Page to Load")
                .waitForDisplay(validIDType, waitSeconds = 30.0)
            it.exist(validIDType)
        } else {

            describe("Wait for Page to Load")
                .waitForDisplay("Take a pic of your ID", waitSeconds = 30.0)
            it.exist("Take a pic of your ID")
        }

        describe("Wait for Page to Load")
            .waitForDisplay("Take a pic: Front of your ID Card", waitSeconds = 30.0)
        //Front Camera
        it.exist("Take a pic: Front of your ID Card")

        //ID Description

        if(validIDType == "Passport"){
            it.exist("Please make sure that your passport has your signature. See sample below.")
            it.exist("For faster processing, please make sure that you have included your signature on your passport.")
        } else if (validIDType == "PhilSys ID") {
            it.exist("Please include 3 specimen signature written on a piece of paper.")
            it.exist("Please make sure you have your 3 specimen signature for faster processing.")
        } else if (validIDType == "PhilHealth ID") {
            it.exist("Only new version of the PhilHealth ID is accepted. See sample Below.")
            it.exist("We are only accepting the new version of the PhilHealth ID. If you have the old version, please upload a different ID type for faster processing.")
        } else if (validIDType == "PRD ID") {
            it.exist("Please Make sure the photo clearly showed the details on your card for faster approval.")
            it.exist("Please provide all necessary documents for faster approval.")
        } else {
            it.exist("Please make sure the photo clearly showed the details on your card for faster approval.")
            it.exist("Please provide all necessary documents for faster approval.")
        }

        it.exist("Open Camera")
        it.exist("Open Library")



        // Upload Valid ID by Camera
        if(useCamera){

            var isFront = true

            cameraPicture(validIDType, isFront)

            val willRetakeFrontPhoto = getRandomBoolean()

            //Will Retake Photo?
            if(willRetakeFrontPhoto){
                it.tap("Retake Photo")

                //Shoot Camera
                cameraPicture(validIDType, isFront)

                //Followed Sample Format?
                sampleFormat()

                val willRetakePhotoAgain = getRandomBoolean()

                //Take Picture Again?
                if(willRetakePhotoAgain){
                    it.tap("Retake Photo")

                    //Shoot Camera
                    cameraPicture(validIDType, isFront)

                    //Followed Sample Format?
                    sampleFormat()


                    it.tap("Yes, I did")
                } else {

                    //Don't Retake
                    it.tap("Yes, I did")
                }

            } else {

                sampleFormat()

                val willRetakePhotoAgain = getRandomBoolean()

                //Take Picture Again?
                if(willRetakePhotoAgain){
                    it.tap("Retake Photo")

                    //Shoot Camera
                    cameraPicture(validIDType, isFront)

                    //Followed Sample Format?
                    sampleFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }
            }

            //Back ID
            it.exist("Take a pic: Back of your ID Card")
            it.exist("Open Camera")
            it.exist("Open Library")

            //checks if camera is for Back ID
            isFront = false

            cameraPicture(validIDType, isFront)


            val willRetakeBackPhoto = getRandomBoolean()

            if(willRetakeBackPhoto){
                it.tap("Retake Photo")

                //Take Picture Again?
                cameraPicture(validIDType, isFront)

                //Followed Sample Format?
                sampleFormat()

                val willRetakePhotoAgain = getRandomBoolean()

                if(willRetakePhotoAgain){
                    it.tap("Retake Photo")
                    cameraPicture(validIDType, isFront)

                    sampleFormat()

                    it.tap("Yes, I did")
                } else {

                    //Don't Retake
                    it.tap("Yes, I did")
                }

            } else {
                sampleFormat()

                val willRetakePhotoAgain = getRandomBoolean()

                if(willRetakePhotoAgain){
                    it.tap("Retake Photo")
                    cameraPicture(validIDType, isFront)

                    sampleFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }
            }


        } else {
            //Upload Library

            var isFront = true

            //Front ID

            openLibrary(validIDType, isFront)

            val willReuploadImage = getRandomBoolean()

            if(willReuploadImage){
                it.tap("Retake Photo")
                openLibrary(validIDType, isFront)
                openLibraryFormat()

                val willReuploadAgain = getRandomBoolean()

                if(willReuploadAgain){
                    it.tap("Retake Photo")
                    openLibrary(validIDType, isFront)
                    openLibraryFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }


            } else {

                //Don't Retake

                openLibraryFormat()

                val willReuploadAgain = getRandomBoolean()

                if(willReuploadAgain){
                    it.tap("Retake Photo")
                    openLibrary(validIDType, isFront)
                    openLibraryFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }


            }

            //Back ID

            isFront = false

            it.exist("Take a pic: Back of your ID Card")
            it.exist("Open Camera")
            it.exist("Open Library")

            openLibrary(validIDType, isFront)


            it.exist("Yes, looks good!")
            it.exist("Retake Photo")

            val willReuploadBackImage = getRandomBoolean()

            if(willReuploadBackImage){
                it.tap("Retake Photo")
                openLibrary(validIDType, isFront)
                openLibraryFormat()

                val willReuploadAgain = getRandomBoolean()

                if(willReuploadAgain){
                    it.tap("Retake Photo")
                    openLibrary(validIDType, isFront)
                    openLibraryFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }


            } else {

                //Don't Retake
                openLibraryFormat()

                val willReuploadAgain = getRandomBoolean()

                if(willReuploadAgain){
                    it.tap("Retake Photo")
                    openLibrary(validIDType, isFront)
                    openLibraryFormat()

                    it.tap("Yes, I did")
                } else {

                    it.tap("Yes, I did")
                }


            }



        }

        describe("Wait for page to load")
            .waitForDisplay("Valid ID", waitSeconds = 30.0)

        //Selfie ID Upload
        it.exist("Valid ID")
        it.exist("Please provide all necessary documents for faster approval.")

        it.exist("Valid ID")
        it.exist("Selfie with your ID")
        it.exist( "Signature")

        //Check for Disabled Buttons

        it.tap("Valid ID")
        it.exist("Selfie with your ID")
        it.exist("Signature")

        it.tap("Signature")
        it.exist("Valid ID")
        it.exist("Selfie with your ID")


        it.tap("Selfie with your ID")

        it.exist("Take a pic with your ID")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")
        it.exist("Open Camera")
        it.exist("Open Library")

        if(useCamera){

            cameraIDSelfie()

            val willRetakeIDSelfie = getRandomBoolean()

            if(willRetakeIDSelfie){
                it.tap("Retake Photo")

                //Take Picture Again?
                cameraIDSelfie()

                val willRetakePhotoAgain = getRandomBoolean()

                if(willRetakePhotoAgain){
                    it.tap("Retake Photo")
                    //Take Picture Again?
                    cameraIDSelfie()

                    it.tap("Yes, looks good!")
                } else {

                    //Don't Retake
                    it.tap("Yes, looks good!")
                }

            } else {
                it.tap("Yes, looks good!")

            }

        } else {
            //Upload Library

            openLibraryIDSelfie(validIDType)

            val willReuploadImage = getRandomBoolean()

            if(willReuploadImage){
                it.tap("Retake Photo")
                openLibraryIDSelfie(validIDType)
                openLibraryIDSelfieFormat()

                val willReuploadAgain = getRandomBoolean()

                if(willReuploadAgain){
                    it.tap("Retake Photo")
                    openLibraryIDSelfie(validIDType)
                    openLibraryIDSelfieFormat()

                    it.tap("Yes, looks good!")
                } else {

                    it.tap("Yes, looks good!")
                }


            } else {

                //Don't Retake

                openLibraryIDSelfieFormat()

                it.tap("Yes, looks good!")

            }


        }

        describe("Wait for page to load")
            .waitForDisplay("Valid ID", waitSeconds = 30.0)

        it.exist("Valid ID")
        it.exist("Please provide all necessary documents for faster approval.")

        it.exist("Valid ID")
        it.exist("Selfie with your ID")
        it.exist( "Signature")

        //Check for Disabled Buttons

        it.tap("Valid ID")
        it.exist("Selfie with your ID")
        it.exist("Signature")

        it.tap("Selfie with your ID")
        it.exist("Valid ID")
        it.exist("Signature")

        it.tap("Signature")

        it.exist(  "Can we have your autograph?")
        it.exist("Please sign below. Use the signature found on your valid ID.")
        it.exist("CLEAR")
        it.exist("Sign here")

        it.swipePointToPoint(startX = 1057, startY = 470, endX = 1500, endY = 725)

        it.tap("CLEAR")

        //Tap next
        //it.tap("\uE941")
        it.tap(x=609,y=869)
        it.tap(x=609,y=869)

        describe("Wait for page to load")
            .waitForDisplay("Signature is empty", waitSeconds = 30.0)


        it.exist("Signature is empty")
        it.exist("please sign your signature")
        it.exist("OK")

        it.tap("OK")

        it.swipePointToPoint(startX = 1057, startY = 470, endX = 1500, endY = 725)

        //Tap next
        //it.tap("\uE941")
        it.tap(x=609,y=869)
        it.tap(x=609,y=869)

        describe("Wait for page to load")
            .waitForDisplay("Nice! You are getting closer to your dreams!", waitSeconds = 30.0)

        it.exist("Nice! You are getting closer to your dreams!")
        it.exist("Tap done to continue")
        it.exist("Done")

        it.tap("Done")

        it.exist("Upload Documents")
        it.exist("Please provide all necessary documents for faster approval.")
        it.exist("Valid ID and E-Signature")
        it.exist("Video Selfie")

    }

    @Macro ("[Upload Video Selfie]")
    fun videoSelfie(){
        it.tap("Video Selfie")

        it.exist("Open Camera")

        videoRecord()

        val retakeVideo = getRandomBoolean()

        if(retakeVideo){
            it.tap("Retake Video")

            videoRecord()

            val retakeVideoAgain = getRandomBoolean()

            if(retakeVideoAgain){
                it.tap("Retake Video")
                videoRecord()
            }

            it.tap("Yes, looks good!")

        } else {
            it.tap("Yes, looks good!")
        }

        describe("Wait for page to load")
            .waitForDisplay("Wow! looks so good!", waitSeconds = 30.0)

        it.exist("Wow! looks so good!")
        it.exist("Tap done to continue")
        it.exist("Done")

        it.tap("Done")

        it.exist("Upload Documents")
        it.exist("Please provide all necessary documents for faster approval.")
        it.exist("Valid ID and E-Signature")
        it.exist("Video Selfie")


    }

    @Macro ("[Consent Letter]")
    fun consentLetter(){
        it.tap("Consent Letter")

        it.exist("Consent Letter")
        it.exist("Kindly upload a copy of your Consent Letter")
        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        //Tap Consent Letter
        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[4]")
        it.tap(x=157,y=1164)

        it.wait(2)

        //Remove
        //it.tap("xpath=(//android.widget.TextView[@text=\"\uE908\"])[2]")
        it.tap(x=945,y=720)

        describe("Wait for page to load")
            .waitForDisplay("Browse to choose file.", waitSeconds = 30.0)

        it.exist("Upload File")

        it.tap("Upload File")

        //Tap Consent Letter
        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[4]")
        it.tap(x=157,y=1164)

        //it.exist("FileName.jpg")

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //Tap Next
        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for page to load")
            .waitForDisplay("Upload Documents", waitSeconds = 30.0)


        it.exist("Upload Documents")
        it.exist("Please provide all necessary documents for faster approval.")
        it.exist("Valid ID and E-Signature")
        it.exist("Video Selfie")

    }

    @Macro("[Non-US Identification]")
    fun nonUSIdentification(){
        it.tap("US Non-Identification")
        it.exist("Non US Identification")
        it.exist("In compliance with FATCA, kindly upload a copy of your Non US Identification")

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[8]")
        it.tap(x=525,y=1565)


        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //Remove

        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
        it.tap(x=945,y=794)

        describe("Wait for page to load")
            .waitForDisplay("Browse to choose file.", waitSeconds = 30.0)

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[8]")
        it.tap(x=525,y=1565)

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for page to load")
            .waitForDisplay("You did great!", waitSeconds = 30.0)

        it.exist( "You did great!")
        it.exist("Tap done to continue")

        it.tap("Done")
    }

    @Macro ("[Upload Form-W8Ben]")
    fun uploadW8(){

        it.tap("IRS Form-W8Ben")
        it.exist("IRS Form W-8Ben")
        it.exist("In compliance with FATCA, download and fill up this IRS Form W-8Ben then kindly upload here")

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        it.tap("IRS FORM W-8BEN.pdf")
        it.exist("IRS FORM W-8BEN.pdf")

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //Remove

        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
        it.tap(x=945,y=794)

        describe("Wait for page to load")
            .waitForDisplay("Browse to choose file.", waitSeconds = 30.0)

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        it.wait(1)

//
//        //tap hamburger menu
//        it.tap("xpath=//android.widget.ImageButton[@content-desc=\"Show roots\"]")
//        describe("Wait for page to load")
//            .waitForDisplay("Downloads", waitSeconds = 30.0)
//
//        it.tap("Downloads")

        it.tap("IRS FORM W-8BEN.pdf")
        it.exist("IRS FORM W-8BEN.pdf")

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for page to load")
            .waitForDisplay("Nicely done!", waitSeconds = 30.0)

        it.exist("Nicely done!")
        it.exist("Tap done to continue")

        it.tap("Done")
    }


    @Macro ("[Upload FORM W9]")
    fun uploadFormW9(){

        it.exist("IRS Form-W9")
        it.tap("IRS Form-W9")

        it.exist("IRS Form W-9")

        it.exist("In compliance with FATCA, download and fill up this IRS Form W-9 then kindly upload here")

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        it.wait(1)

        describe("Wait for page to load")
            .waitForDisplay("IRS FORM W-9.pdf", waitSeconds = 30.0)

        it.tap("IRS FORM W-9.pdf")
        it.exist("IRS FORM W-9.pdf")

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //Remove

        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
        it.tap(x=945,y=794)

        describe("Wait for page to load")
            .waitForDisplay("Browse to choose file.", waitSeconds = 30.0)

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        it.tap("IRS FORM W-9.pdf")
        it.exist("IRS FORM W-9.pdf")

        it.dontExist("Browse to choose file.")
        it.dontExist("Upload File")

        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for page to load")
            .waitForDisplay("Nicely done!", waitSeconds = 30.0)

        it.exist("Nicely done!")
        it.exist("Tap done to continue")

        it.tap("Done")




    }

    @Macro("[US Identification]")
    fun usIdentification(){
        it.tap("US Identification")
        it.exist("US Identification")
        it.exist("In compliance with FATCA, kindly upload a copy of your US Identification")

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")


        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[6]")
        it.tap(x=913,y=1179)


        it.wait(2)

//        it.dontExist("Browse to choose file.")
//        it.dontExist("Upload File")

        //Remove

        //it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
        it.tap(x=945,y=794)

        describe("Wait for page to load")
            .waitForDisplay("Browse to choose file.", waitSeconds = 30.0)

        it.exist("Browse to choose file.")
        it.exist("Upload File")

        it.tap("Upload File")

        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[6]")
        it.tap(x=913,y=1179)

        it.wait(2)

//        it.dontExist("Browse to choose file.")
//        it.dontExist("Upload File")

        //it.tap("\uE941")
        it.tap(x=927,y=2194)

        describe("Wait for page to load")
            .waitForDisplay("You did great!", waitSeconds = 30.0)

        it.exist( "You did great!")
        it.exist("Tap done to continue")

        it.tap("Done")
    }

    @Macro("[Continue Sign Agreement]")
    fun continueSignAgreement(email: String, password: String){

        describe("Wait for page to load")
            .waitForDisplay("Let’s Get Started", waitSeconds = 30.0)

        it.exist("Let’s Get Started")
        it.exist( "Invest effortlessly in what is suitable for you")
        it.exist("Existing Investagrams user?")
        it.exist("Use your Investagrams account to Login.")

        it.tap("Log In")

        it.tap("Your email address")
            .sendKeys(email)
        it.tap("Your password")
            .sendKeys(password)
        it.tap("Log In")
    }

    fun generateRandomPhoneNumber(country: String): String {
        return when (country) {
            "Singapore (+65)" -> "8" + generateRandomDigits(7)
            "Japan (+81)" -> generateRandomJapanesePhoneNumber()
            "United States (+1)" -> generateRandomUSPhoneNumber()
            else -> throw IllegalArgumentException("Invalid country")
        }
    }

    fun generateRandomDigits(length: Int): String {
        val firstDigit = Random.nextInt(2, 10) // Generate a random first digit between 1 and 9
        val otherDigits = (1 until length).map { Random.nextInt(0, 10) } // Generate the rest of the digits
        return "$firstDigit${otherDigits.joinToString("")}"
    }

    fun generateRandomJapanesePhoneNumber(): String {
        val areaCodes = listOf("075", "052", "011", "092", "078", "022", "082")
        val areaCode = areaCodes.random()
        val localNumber = generateRandomDigits(7)
        return "$areaCode$localNumber"
    }

    fun generateRandomUSPhoneNumber(): String {
        val areaCode = generateRandomUSAreaCode()
        val localNumber = generateRandomDigits(7)
        return "$areaCode$localNumber"
    }

    fun generateRandomUSAreaCode(): String {
        val invalidN11Codes = setOf("211", "311", "411", "511", "611", "711", "811", "911")
        var areaCode: String
        do {
            areaCode = "${Random.nextInt(2, 10)}${Random.nextInt(0, 2)}${Random.nextInt(0, 10)}"
        } while (areaCode in invalidN11Codes)
        return areaCode
    }

    fun getRandomCountryCode(): String {

        val countries = listOf(
            "Singapore (+65)",
            "Japan (+81)",
            "United States (+1)"
        )

        return countries.random()

    }

    fun getRandomAreaCode(): Int {
        val areaCodes = listOf(
            212, 213, 312, 305, 415, 202, 404, 617, 713, 602
        )
        return areaCodes.random()
    }

    fun generateRandom7DigitNumber(): Int {
        return Random.nextInt(2000000, 10000000)
    }

    fun getRandomUSTIN(): String {
        val min = 100_000_000L  // Minimum 9-digit number
        val max = 999_999_999L  // Maximum 9-digit number
        val randomTin = Random.nextLong(min, max + 1)
        return randomTin.toString()
    }
    fun formW9(): List<String> {

        val statuses = listOf("US citizen", "Green card holder", "US resident")
        val numberOfStatuses = if (statuses.size == 1) 1 else Random.nextInt(1, statuses.size + 1)
        return statuses.shuffled().take(numberOfStatuses)  // Shuffle the list and take the first 'numberOfStatuses' elements

    }

    fun formW8(): List<String>{
        val statuses = listOf("Born in the US or any US territory", "I granted a power of attorney to a person with a US address to open this account")
        val numberOfStatuses = if (statuses.size == 1) 1 else Random.nextInt(1, statuses.size + 1)
        return statuses.shuffled().take(numberOfStatuses)  // Shuffle the list and take the first 'numberOfStatuses' elements

    }

    fun openLibraryIDSelfieFormat(){
        it.exist("Is this photo clear?")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")

        it.exist("Yes, looks good!")
        it.exist("Retake Photo")


    }

    fun openLibraryIDSelfie(validIDType: String){
        it.tap("Open Library")


        //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[2]")
        it.tap(x=528,y=831)

        describe("Wait for page to load")
            .waitForDisplay("Is this photo clear?", waitSeconds = 30.0)

        it.exist("Is this photo clear?")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")



    }


    fun openLibraryFormat(){
        it.tap("Yes, looks good!")
        it.exist("Did you follow the sample format?")
        it.exist("Make sure that you followed the sample format for faster approval.")

        it.exist("Yes, I did")
        it.exist("Retake Photo")

        describe("Wait for page to load")
            .waitForDisplay("Yes, I did", waitSeconds = 30.0)

    }

    fun openLibrary(validIDType: String, isFront: Boolean){
        it.tap("Open Library")

        if(isFront){

            it.wait(2)

            //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[3]")
            it.tap(x=918,y=835)


        } else{

            it.wait(2)


            //it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[5]")
            it.tap(x=546,y=1216)
        }


        describe("Wait for page to load")
            .waitForDisplay("Is this photo clear?", waitSeconds = 30.0)

        it.exist("Is this photo clear?")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")
        it.exist("See Sample")

        //See Sample Again
        it.tap("See Sample")

        if(isFront){
            if(validIDType == "Passport"){
                it.exist("Please make sure that your passport has your signature. See sample below.")
            } else if (validIDType == "PhilSys ID") {
                it.exist("Please include 3 specimen signature written on a piece of paper.")
                it.exist("Please make sure you have your 3 specimen signature for faster processing.")
            } else if (validIDType == "PhilHealth ID") {
                it.exist("Only new version of the PhilHealth ID is accepted. See sample Below.")
                it.exist("We are only accepting the new version of the PhilHealth ID. If you have the old version, please upload a different ID type for faster processing.")
            } else if (validIDType == "PRD ID") {
                it.exist("Please Make sure the photo clearly showed the details on your card for faster approval.")
                it.exist("Please provide all necessary documents for faster approval.")
            } else {
                it.exist("Please make sure the photo clearly showed the details on your card for faster approval.")
                it.exist("Please provide all necessary documents for faster approval.")
            }
        } else {
            if(validIDType == "Passport"){
                it.exist("Take a dedicated photo of the second page of your passport with your signature")
            } else if (validIDType == "PhilSys ID") {
                it.exist("Please include 3 specimen signature written on a piece of paper.")
                it.exist("Please make sure you have your 3 specimen signature for faster processing.")
            } else if (validIDType == "PhilHealth ID") {
                it.exist("Only new version of the PhilHealth ID is accepted. See sample Below.")
                it.exist("We are only accepting the new version of the PhilHealth ID. If you have the old version, please upload a different ID type for faster processing.")
            } else if (validIDType == "PRD ID") {
                it.exist("Please Make sure the photo clearly showed the details on your card for faster approval.")
                it.exist("Please provide all necessary documents for faster approval.")
            } else {
                it.exist("Please make sure the photo clearly showed the details on your card for faster approval.")
                it.exist("Please provide all necessary documents for faster approval.")
            }
        }


        it.tap("Got it!")


    }

    fun videoRecord(){
        it.tap("Open Camera")

        it.exist("Instructions")
        it.tap("Instructions")

        it.exist("Take a video selfie with your ID")
        it.exist("Hold your phone at eye level and make sure your whole face is visible. Slowly turn your head from left to right.")
        it.exist("Please tap the camera button to start recording.")

        //Tap Close
        //it.tap("\uE908")
        it.tap(x=945,y=688)

        it.wait(3)

        //Start Recording
        it.tap(x=535, y=2203)
        it.tap(x=535, y=2203)

        it.wait(6)

        describe("Wait for page to load")
            .waitForDisplay("Is this video clear?", waitSeconds = 30.0)

        it.exist("Is this video clear?")
        it.exist("Make sure that your whole face is visible and you slowly turned your head")

        it.exist("Yes, looks good!")
        it.exist("Retake Video")
    }

    fun cameraIDSelfie(){
        it.tap("Open Camera")
        it.exist("Instructions")

        it.tap("Instructions")
        it.exist("Take a pic with your ID")
        it.exist("Hold your Valid ID either on your left or right. Make sure that your whole face is visible on the screen. No funny poses. We will use this to scan to authenticate your account when needed.")

        //Tap Close
        //it.tap(  "\uE908")
        it.tap(x=941,y=642)

        //Shoot Camera
        //it.tap("\uE940")
        it.tap(x=542,y=2171)

        describe("Wait for page to load")
            .waitForDisplay("Is this photo clear?", waitSeconds = 30.0)

        it.exist("Is this photo clear?")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")
        it.exist("Yes, looks good!")
        it.exist("Retake Photo")
    }

    fun sampleFormat(){
        it.tap("Yes, looks good!")

        it.exist("Did you follow the sample format?")
        it.exist("Make sure that you followed the sample format for faster approval.")

        it.exist("Yes, I did")
        it.exist("Retake Photo")
    }

    fun cameraPicture(validIDType: String, isFront:Boolean){
        it.tap("Open Camera")
        it.exist("See Sample")
        it.exist("Please provide all necessary documents for faster approval.")

        //See Sample
        it.tap("See Sample")

        val validIDTypes = setOf("Passport", "PRC ID", "PhilSys ID", "PhilHealth ID")

        if (validIDType in validIDTypes) {
            it.exist(validIDType)
        } else {

            if(isFront){
                it.exist("Take a pic of your ID")
            } else {
                it.exist("Take a back pic of your ID")
            }
        }


        it.exist("Got it!")
        //it.exist("Description")
        it.tap("Got it!")

        if (validIDType in validIDTypes) {
            it.dontExist(validIDType)
        } else {

            if(isFront){
                it.dontExist("Take a pic of your ID")
            } else {
                it.dontExist("Take a back pic of your ID")
            }
        }

        //Shoot Camera
        //it.tap("\uE940")
        it.tap(x=542,y=2171)

        describe("Wait for page to load")
            .waitForDisplay("Is this photo clear?", waitSeconds = 30.0)

        it.exist("Is this photo clear?")
        it.exist("Make sure the photo clearly showed the details on your card for faster approval.")
        it.exist("See Sample")

        //See Sample Again
        it.tap("See Sample")


        if (validIDType in validIDTypes) {
            it.exist(validIDType)
        } else {

            if(isFront){
                it.exist("Take a pic of your ID")
            } else {
                it.exist("Take a back pic of your ID")
            }
        }

        it.exist("Got it!")

        it.tap("Got it!")


        if (validIDType in validIDTypes) {
            it.dontExist(validIDType)
        } else {

            if(isFront){
                it.dontExist("Take a pic of your ID")
            } else {
                it.dontExist("Take a back pic of your ID")
            }
        }

        it.exist("Yes, looks good!")
        it.exist("Retake Photo")
    }

    fun printDateRangeWithNext(targetDate: Int) {
        val today = LocalDate.now().dayOfMonth
        val daysInMonth = LocalDate.now().lengthOfMonth()

        fun getNextDay(current: Int): Int = when {
            today <= targetDate -> if (current == targetDate) current else if (current == daysInMonth) 1 else current + 1
            else -> if (current == targetDate) current else if (current == 1) daysInMonth else current - 1
        }

        var currentDay = today
        while (true) {
            val nextDay = getNextDay(currentDay)
            if (nextDay == currentDay) {
                println("$currentDay")
                break
            } else {

                it.select(nextDay.toString())
                    .swipeTo(currentDay.toString())

                println("$currentDay, next expected: $nextDay")


            }
            currentDay = nextDay
        }
    }

    fun printMonthRangeWithNext(targetMonthString: String) {
        val currentMonth = YearMonth.now().month
        val targetMonth = try {
            Month.values().first { it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(targetMonthString, ignoreCase = true) }
        } catch (e: IllegalArgumentException) {
            println("Invalid month input: $targetMonthString")
            return
        }

        fun getNextMonth(current: Month): Month = when {
            currentMonth <= targetMonth -> if (current == targetMonth) current else current.plus(1)
            else -> if (current == targetMonth) current else if (current == Month.JANUARY) Month.DECEMBER else current.minus(1)
        }

        var month = currentMonth
        while (true) {
            val nextMonth = getNextMonth(month)
            if (nextMonth == month) {
                println(month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                break
            } else {

                it.select(nextMonth.toString().lowercase(Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                    .swipeTo(month.toString().lowercase(Locale.getDefault())
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })


                println("${month.getDisplayName(TextStyle.FULL, Locale.getDefault())}, next expected: ${nextMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())}")

            }
            month = nextMonth
        }
    }

    fun printYearRangeWithNext(targetYear: Int) {
        val currentYear = Year.now().value

        fun getNextYear(current: Int): Int = when {
            currentYear <= targetYear -> if (current == targetYear) current else current + 1
            else -> if (current == targetYear) current else current - 1
        }

        var year = currentYear
        while (true) {
            val nextYear = getNextYear(year)
            if (nextYear == year) {
                println("$year")
                break
            } else {

                it.select(nextYear.toString())
                    .swipeTo(year.toString())


                println("$year, next expected: $nextYear")


            }
            year = nextYear
        }
    }

    fun getRandomBoolean(): Boolean {
        return Random.nextBoolean() // Generates a random boolean value
    }

    fun getRandomAlphanumericID(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun getRandomValidIDType(): String {
        val ids = listOf(
            "Passport",
            "Driver's License",
            "PRC ID",
            "UMID Card",
            "SSS",
            "PhilSys ID",
            "GSIS ID",
            "Postal ID (New Version)",
            "PhilHealth ID",
            "Senior Citizen ID",
            "OFW ID",
            "Seaman's Book",
            "Government Office and GOCC ID",
            "School ID Signed by Principal/Head",
            )
        return ids[Random.nextInt(ids.size)]
    }

    fun getCurrentYear(): Int {
        return LocalDate.now().year
    }


    fun getDateSixYearsFromNow(): String {
        // Get the current date
        val currentDate = LocalDate.now()

        // Subtract 18 years from the current date
        val dateEighteenYearsAgo = currentDate.minusYears(18)

        // Define the date formatter
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

        // Format and return the date
        return dateEighteenYearsAgo.format(formatter)
    }
    fun getRandomAssetsRange(): String {
        val investmentRanges = listOf(
            "Less than 500,000",
            "Less than 1 Million",
            "Less than 5 Million",
            "Less than 10 Million",
            "Over 10 Million"
        )
        return investmentRanges.random()  // Randomly select one string from the list
    }

    fun getRandomRankedInvestmentObjectives(): List<String> {
        val investmentObjectives = listOf(
            "Capital Preservation",
            "Long-Term Investment",
            "Growth",
            "Speculation"
        )
        return investmentObjectives.shuffled()  // Shuffle the list to get a random ranking
    }

    fun getAllSources(employmentStatus: String): List<String> {
        return when (employmentStatus) {
            "Employed", "Self Employed", "Business Owner", "Overseas Worker" -> listOf(
                "Salary",
                "Business",
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Unemployed" -> listOf(
                "Salary from Last Work",
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Retired" -> listOf(
                "Salary from Last Work",
                "Investments",
                "Retirement / Pension",
                "Family / Inheritances",
                "Remittances"
            )
            "Student" -> listOf(
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Stay-at-home Parent" -> listOf(
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            else -> emptyList()
        }
    }

    fun getRandomSources(employmentStatus: String): List<String> {
        val sources = when (employmentStatus) {
            "Employed", "Self Employed", "Business Owner", "Overseas Worker" -> listOf(
                "Salary",
                "Business",
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Unemployed" -> listOf(
                "Salary from Last Work",
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Retired" -> listOf(
                "Salary from Last Work",
                "Investments",
                "Retirement / Pension",
                "Family / Inheritances",
                "Remittances"
            )
            "Student" -> listOf(
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            "Stay-at-home Parent" -> listOf(
                "Investments",
                "Family / Inheritances",
                "Remittances"
            )
            else -> emptyList()
        }

        if (sources.isEmpty()) {
            return emptyList()
        }

        // Randomly choose a number between 1 and the size of the list (inclusive)
        val numberOfSources = if (sources.size == 1) 1 else Random.nextInt(1, sources.size + 1)
        return sources.shuffled().take(numberOfSources)  // Shuffle the list and take the first 'numberOfSources' elements
    }

    fun getRandomRange(): String {
        val ranges = listOf(
            "Less than 200,000",
            "Less than 500,000",
            "Less than 1 Million",
            "Over 1 Million"
        )
        return ranges.random()
    }

    fun generateBrokerList(): List<String> {
        // Generate a random number between 1 and 4
        val numberOfBrokers = Random.nextInt(1, 4)

        // Create a list based on the generated random number
        val brokerList = mutableListOf<String>()
        for (i in 1..numberOfBrokers) {
            brokerList.add("Broker $i")
        }

        return brokerList
    }

    fun generateRandomNumber(agency: String): String {
        return when (agency) {
            "GSIS" -> generateRandomNumber(11)
            "SSS" -> generateRandomNumber(10)
            else -> "Invalid agency"
        }
    }

    private fun generateRandomNumber(length: Int): String {
        val stringBuilder = StringBuilder()
        repeat(length) {
            stringBuilder.append(Random.nextInt(0, 10))
        }
        return stringBuilder.toString()
    }

    fun getRandomIDType(): String {
        val random = java.util.Random().nextInt(2)
        return if (random == 0) "GSIS" else "SSS"
    }

    fun getRandomTinNumber(): String {
        val min = 100_000_000_000L  // Minimum 12-digit number
        val max = 999_999_999_999L  // Maximum 12-digit number
        val randomTin = Random.nextLong(min, max + 1)
        return randomTin.toString()
    }

    fun getRandomZipCode(): String {
        val philippinesZipCodes = listOf(
            // NCR
            "1000", "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009",
            "1010", "1011", "1012", "1013", "1014", "1015", "1016", "1017", "1018", "1019",
            "1020", "1021", "1022", "1023", "1024", "1025", "1026", "1027", "1028", "1029",
            // CAR
            "2600", "2601", "2602", "2603", "2604", "2605", "2606", "2607", "2608", "2609",
            "2610", "2611", "2612", "2613", "2614", "2615", "2616", "2617", "2618", "2619",
            "2620", "2621", "2622", "2623", "2624", "2625", "2626", "2627", "2628", "2629",
            // Region I
            "2700", "2701", "2702", "2703", "2704", "2705", "2706", "2707", "2708", "2709",
            "2710", "2711", "2712", "2713", "2714", "2715", "2716", "2717", "2718", "2719",
            "2720", "2721", "2722", "2723", "2724", "2725", "2726", "2727", "2728", "2729",
            // Region II
            "3500", "3501", "3502", "3503", "3504", "3505", "3506", "3507", "3508", "3509",
            "3510", "3511", "3512", "3513", "3514", "3515", "3516", "3517", "3518", "3519",
            "3520", "3521", "3522", "3523", "3524", "3525", "3526", "3527", "3528", "3529",
            // Region III
            "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
            "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
            "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029",
            // Region IV-A
            "4000", "4001", "4002", "4003", "4004", "4005", "4006", "4007", "4008", "4009",
            "4010", "4011", "4012", "4013", "4014", "4015", "4016", "4017", "4018", "4019",
            "4020", "4021", "4022", "4023", "4024", "4025", "4026", "4027", "4028", "4029",
            // Region IV-B
            "5200", "5201", "5202", "5203", "5204", "5205", "5206", "5207", "5208", "5209",
            "5210", "5211", "5212", "5213", "5214", "5215", "5216", "5217", "5218", "5219",
            "5220", "5221", "5222", "5223", "5224", "5225", "5226", "5227", "5228", "5229",
            // Region V
            "4500", "4501", "4502", "4503", "4504", "4505", "4506", "4507", "4508", "4509",
            "4510", "4511", "4512", "4513", "4514", "4515", "4516", "4517", "4518", "4519",
            "4520", "4521", "4522", "4523", "4524", "4525", "4526", "4527", "4528", "4529",
            // Region VI
            "5000", "5001", "5002", "5003", "5004", "5005", "5006", "5007", "5008", "5009",
            "5010", "5011", "5012", "5013", "5014", "5015", "5016", "5017", "5018", "5019",
            "5020", "5021", "5022", "5023", "5024", "5025", "5026", "5027", "5028", "5029",
            // Region VII
            "6000", "6001", "6002", "6003", "6004", "6005", "6006", "6007", "6008", "6009",
            "6010", "6011", "6012", "6013", "6014", "6015", "6016", "6017", "6018", "6019",
            "6020", "6021", "6022", "6023", "6024", "6025", "6026", "6027", "6028", "6029",
            // Region VIII
            "6500", "6501", "6502", "6503", "6504", "6505", "6506", "6507", "6508", "6509",
            "6510", "6511", "6512", "6513", "6514", "6515", "6516", "6517", "6518", "6519",
            "6520", "6521", "6522", "6523", "6524", "6525", "6526", "6527", "6528", "6529",
            // Region IX
            "7000", "7001", "7002", "7003", "7004", "7005", "7006", "7007", "7008", "7009",
            "7010", "7011", "7012", "7013", "7014", "7015", "7016", "7017", "7018", "7019",
            "7020", "7021", "7022", "7023", "7024", "7025", "7026", "7027", "7028", "7029",
            // Region X
            "9000", "9001", "9002", "9003", "9004", "9005", "9006", "9007", "9008", "9009",
            "9010", "9011", "9012", "9013", "9014", "9015", "9016", "9017", "9018", "9019",
            "9020", "9021", "9022", "9023", "9024", "9025", "9026", "9027", "9028", "9029",
            // Region XI
            "8000", "8001", "8002", "8003", "8004", "8005", "8006", "8007", "8008", "8009",
            "8010", "8011", "8012", "8013", "8014", "8015", "8016", "8017", "8018", "8019",
            "8020", "8021", "8022", "8023", "8024", "8025", "8026", "8027", "8028", "8029",
            // Region XII
            "9500", "9501", "9502", "9503", "9504", "9505", "9506", "9507", "9508", "9509",
            "9510", "9511", "9512", "9513", "9514", "9515", "9516", "9517", "9518", "9519",
            "9520", "9521", "9522", "9523", "9524", "9525", "9526", "9527", "9528", "9529",
            // Region XIII
            "8600", "8601", "8602", "8603", "8604", "8605", "8606", "8607", "8608", "8609",
            "8610", "8611", "8612", "8613", "8614", "8615", "8616", "8617", "8618", "8619",
            "8620", "8621", )

        return philippinesZipCodes[Random.nextInt(philippinesZipCodes.size)]

    }

    fun getRandomIndustry(): String {
        val industries = listOf(
            "Accounting Firm",
            "Adult Entertainment",
            "Advertising Firms",
            "Agriculture",
            "Aircraft Dealers",
            "Alcohol/Liquor Stores",
            "Arms Manufacturer and Dealers",
            "Art Dealers",
            "Auctioneer/Auction House",
            "Auditing Firm",
            "Banks",
            "BPO",
            "Broker/Dealer in Securities",
            "Bus/Ship Line Operator",
            "Casino",
            "Charity House",
            "Chemical Industry",
            "Churches or any Religious Organizations",
            "Cigarette Distributor",
            "Communication",
            "Companies Mining/Dealing with Precious Metal",
            "Construction",
            "Convenience / Grocery / Retail Store",
            "Crypto Currency",
            "Domestic Service",
            "Electricity, Gas & Water",
            "Fishing",
            "Foods Services / Food Processing",
            "Foundation",
            "Fund Transfer / International Wire Transfer",
            "Gadgets and Electronics",
            "Gaming",
            "Gem / Precious Metal / Stone Dealer",
            "Government Agencies",
            "Health and Social Work",
            "Hotels and Restaurant",
            "Insurance Companies",
            "Investment Companies/Mutual Fund Companies",
            "Investment Company Advisers",
            "Investment House",
            "IT / Software",
            "Jewelry Stores",
            "Law Firm",
            "Lottery / Betting Outlet",
            "Manufacturing",
            "Media",
            "Mutual Fund Distributors",
            //"Non-Government Organizations (NGO)/Charity Business",
            "Nuclear Power Plant",
            "Oil Companies/Explorations",
            "Online Pharmaceutical",
            "Other Capital Market Participants",
            "Pawnshops",
            "Private Education",
            "Publicly Listed Companies",
            "Real Estate Brokerage",
            "Remittance Center",
            "Restaurant/Diner/Caterer",
            "Self-Practicing Financial Consultants",
            "Tourism",
            "Transportation",
            "Travel Agency / Ticketing Office",
            "Unregulated NonProfit Organization (UNO)",
            "Used Car Dealers",
            "Vehicle Dealership",
            "Vending Machine Operator",
            "Wholesale and Retail Trade"
        )
        return industries[Random.nextInt(industries.size)]
    }


    fun getRandomEmploymentStatus(): String {
        val employmentStatuses = listOf(
            "Employed",
            "Self Employed",
            "Unemployed",
            "Retired",
            "Student",
            "Stay-at-home Parent",
            "Business Owner",
            "Overseas Worker"
        )
        return employmentStatuses[Random.nextInt(employmentStatuses.size)]
    }

    fun getRandomWorkTitle(): String {
        val workTitles = listOf(
            "Associate to Manager",
            "Authorized Signatory",
            "Barangay Officials",
            "Board of Director",
            "Consultant",
            "Contractual",
            "Executive Official",
            "Government Employee",
            "Intern",
            "Local Government Official",
            "Manpower Service Providers",
            "National Government Official",
            "OFW",
            "Part Time Staff",
            "Regional Government Official",
            "Senior Management",
            "Staff",
            "Stockholder",
            "Teacher"
        )
        return workTitles[Random.nextInt(workTitles.size)]
    }

    fun getRandomBarangayInCity(city:String, province :String):String {

        val cities = Klaxon().parse<Map<String, Map<String, List<String>>>>(
            File("src/test/resources/barangay.json").readText()
        ) ?: emptyMap()

        // Find the province key
        val provinceData = cities[province]

        // Find the city key
        val cityData = provinceData?.get(city)

        return cityData?.random() ?: ""
    }

    fun getRandomCityInProvince(province: String): String {

            val provinces = mapOf(

                // CAR
                "Abra" to listOf("Bangued (Capital)", "Boliney", "Bucay", "Bucloc", "Daguioman", "Danglas", "Dolores", "La Paz", "Lacub", "Lagangilang", "Lagayan", "Langiden", "Licuan-Baay (Licuan)", "Luba", "Malibcong", "Manabo", "Peñarrubia", "Pidigan", "Pilar", "Sallapadan", "San Isidro", "San Juan", "San Quintin", "Tayum", "Tineg", "Tubo", "Villaviciosa"),
                "Apayao" to listOf("Calanasan (Bayag)", "Conner", "Flora", "Kabugao (Capital)", "Luna", "Pudtol", "Santa Marcela"),
                "Benguet" to listOf("Atok", "Baguio City", "Bakun", "Bokod", "Buguias", "Itogon", "Kabayan", "Kapangan", "Kibungan", "La Trinidad (Capital)", "Mankayan", "Sablan", "Tuba", "Tublay"),
                "Ifugao" to listOf("Aguinaldo", "Alfonso Lista (Potia)", "Asipulo", "Banaue", "Hingyon", "Hungduan", "Kiangan", "Lagawe (Capital)", "Lamut", "Mayoyao", "Tinoc"),
                "Kalinga" to listOf("Balbalan", "Lubuagan", "Pasil", "Pinukpuk", "Rizal", "Tabuk", "Tanudan", "Tinglayan"),
                "Mountain Province" to listOf("Barlig", "Bauko", "Besao", "Bontoc", "Natonin", "Paracelis", "Sabangan", "Sadanga", "Sagada", "Tadian"),

                // NCR
                "Metro Manila" to listOf("Caloocan", "Las Piñas", "Makati", "Malabon", "Mandaluyong", "Manila", "Marikina", "Muntinlupa", "Navotas", "Parañaque", "Pasay", "Pasig", "Pateros", "Quezon City", "San Juan", "Taguig", "Valenzuela"
                ),

                // Region I: Ilocos Region
                "Ilocos Norte" to listOf("Laoag City (Capital)", "Batac City", "Adams", "Bacarra", "Badoc", "Bangui", "Banna (Espiritu)", "Burgos", "Carasi", "Currimao", "Dingras", "Dumalneg", "Marcos", "Nueva Era", "Pagudpud", "Paoay", "Pasuquin", "Piddig", "Pinili", "San Nicolas", "Sarrat", "Solsona", "Vintar"),
                "Ilocos Sur" to listOf("Vigan", "Candon City", "Alilem", "Banayoyo", "Bantay", "Burgos", "Cabugao", "Caoayan", "Cervantes", "Galimuyod", "Gregorio Del Pilar (Concepcion)", "Lidlidda", "Magsingal", "Nagbukel", "Narvacan", "Quirino (Angkaki)", "Salcedo (Baugen)", "San Emilio", "San Esteban", "San Ildefonso", "San Juan (Lapog)", "San Vicente", "Santa", "Santa Catalina", "Santa Cruz", "Santa Lucia", "Santa Maria", "Santiago", "Santo Domingo", "Sigay", "Sinait", "Sugpon", "Suyo", "Tagudin"),
                "La Union" to listOf("San Fernando", "Agoo", "Aringay", "Bacnotan", "Bagulin", "Balaoan", "Bangar", "Bauang", "Burgos", "Caba", "Luna", "Naguilian", "Pugo", "Rosario", "San Gabriel", "San Juan", "Santo Tomas", "Santol", "Sudipen", "Tubao"),
                "Pangasinan" to listOf("Dagupan", "San Carlos", "Urdaneta City", "Alaminos City", "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui", "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales", "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara", "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo", "Villasis", "Agno", "Anda", "Asingan", "Balungao", "Basista"),

                // Region II: Cagayan Valley
                "Batanes" to listOf("Basco (Capital)", "Itbayat", "Ivana", "Mahatao", "Sabtang", "Uyugan"),
                "Cagayan" to listOf("Tuguegarao City (Capital)", "Abulug", "Alcala", "Allacapan", "Amulung", "Aparri", "Baggao", "Ballesteros", "Buguey", "Calayan", "Camalaniugan", "Claveria", "Enrile", "Gattaran", "Gonzaga", "Iguig", "Lal-lo", "Lasam", "Pamplona", "Penablanca", "Piat", "Rizal", "Sanchez-Mira", "Santa Ana", "Santa Praxedes", "Santa Teresita", "Santo Niño (Faire)", "Solana", "Tuao"),
                "Isabela" to listOf("Cauayan", "Ilagan City (Capital)", "Santiago", "Alicia", "Angadanan", "Aurora", "Benito Soliven", "Burgos", "Cabagan", "Cabatuan", "Cordon", "Delfin Albano", "Dinapigue", "Divilacan", "Echague", "Gamu", "Ilagan City", "Jones", "Luna", "Maconacon", "Mallig", "Naguilian", "Palanan", "Quezon", "Quirino", "Ramon", "Reina Mercedes", "Roxas", "San Agustin", "San Guillermo", "San Isidro", "San Manuel", "San Mariano", "San Mateo", "San Pablo", "Santa Maria", "Santo Tomas", "Tumauini"),
                "Nueva Vizcaya" to listOf("Bayombong", "Alfonso Castaneda", "Ambaguio", "Aritao", "Bagabag", "Bambang", "Diadi", "Dupax Del Norte", "Dupax Del Sur", "Kasibu", "Kayapa", "Quezon", "Santa Fe", "Solano", "Villaverde"),
                "Quirino" to listOf("Cabarroguis", "Aglipay", "Diffun", "Maddela", "Nagtipunan", "Saguday"),

                // Region III: Central Luzon
                "Bataan" to listOf("Balanga (Capital) City", "Abucay", "Bagac", "Dinalupihan", "Hermosa", "Limay", "Mariveles", "Morong", "Orani", "Orion", "Pilar", "Samal"),
                "Bulacan" to listOf("Malolos (Capital) City", "Meycauayan City", "San Jose del Monte City", "Angat", "Balagtas (Bigaa)", "Baliuag", "Bocaue", "Bulacan", "Bustos", "Calumpit", "Doña Remedios Trinidad", "Guiguinto", "Hagonoy", "Marilao", "Norzagaray", "Obando", "Pandi", "Paombong", "Plaridel", "Pulilan", "San Ildefonso", "San Miguel", "San Rafael", "Santa Maria"),
                "Nueva Ecija" to listOf("Cabanatuan", "Gapan", "San Jose", "Palayan", "Aliaga", "Bongabon", "Cabiao", "Carranglan", "Cuyapo", "Gabaldon (Bitulok & Sabani)", "General Mamerto Natividad", "General Tinio", "Guimba", "Jaen", "Laur", "Licab", "Llanera", "Lupao", "Nampicuan", "Pantabangan", "Peñaranda", "Quezon", "Rizal", "San Antonio", "San Isidro", "San Leonardo", "Santa Rosa", "Santo Domingo", "Talavera", "Talugtug", "Zaragoza"),
                "Pampanga" to listOf("Angeles City", "San Fernando", "Apalit", "Arayat", "Bacolor", "Candaba", "Floridablanca", "Guagua", "Lubao", "Mabalacat", "Macabebe", "Magalang", "Masantol", "Mexico", "Minalin", "Porac", "San Luis", "San Simon", "Santa Ana", "Santa Rita", "Santo Tomas", "Sasmuan"),
                "Tarlac" to listOf("Tarlac (Capital) City", "Anao", "Bamban", "Camiling", "Capas", "Concepcion", "Gerona", "La Paz", "Mayantoc", "Moncada", "Paniqui", "Pura", "Ramos", "San Clemente", "San Jose", "San Manuel", "Santa Ignacia", "Victoria"),
                "Zambales" to listOf("Olongapo", "Botolan", "Cabangan", "Candelaria", "Castillejos", "Iba (Capital)", "Masinloc", "Palauig", "San Antonio", "San Felipe", "San Marcelino", "San Narciso", "Santa Cruz", "Subic"),
                "Aurora" to listOf("Baler (Capital)", "Casiguran", "Dilasag", "Dinalungan", "Dipaculao", "Maria Aurora", "San Luis"),

                // Region IV-A: CALABARZON
                "Cavite" to listOf("Cavite City", "Tagaytay", "Trece Martires City (Capital)", "Alfonso", "Amadeo", "Bacoor City", "Carmona", "Dasmariñas City", "Gen. Mariano Alvarez", "Gen. Emilio Aguinaldo", "General Trias", "Imus City", "Indang", "Kawit", "Magallanes", "Maragondon", "Mendez (Mendez-Nuñez)", "Naic", "Noveleta", "Rosario", "Silang", "Tanza", "Ternate"),
                "Laguna" to listOf("Biñan City", "Cabuyao", "San Pablo", "San Pedro", "Santa Rosa", "Calamba", "Alaminos", "Bay", "Calauan", "Cavinti", "Famy", "Kalayaan", "Liliw", "Los Baños", "Luisiana", "Lumban", "Mabitac", "Magdalena", "Majayjay", "Nagcarlan", "Paete", "Pagsanjan", "Pakil", "Pangil", "Pila", "Rizal", "Santa Cruz", "Santa Maria", "Siniloan", "Victoria"),
                "Batangas" to listOf("Batangas City (Capital)", "Lipa City", "Tanauan City", "Agoncillo", "Alitagtag", "Balayan", "Balete", "Bauan", "Calaca", "Calatagan", "Cuenca", "Ibaan", "Laurel", "Lemery", "Lian", "Lobo", "Mabini", "Malvar", "Mataas na Kahoy", "Nasugbu", "Padre Garcia", "Rosario", "San Jose", "San Juan", "San Luis", "San Nicolas", "San Pascual", "Santa Teresita", "Santo Tomas", "Taal", "Talisay", "Taysan", "Tingloy", "Tuy"),
                "Rizal" to listOf("Antipolo", "Angono", "Baras", "Binangonan", "Cainta", "Cardona", "Jalajala", "Morong", "Pililla", "Rodriguez", "San Mateo", "Tanay", "Taytay", "Teresa"),
                "Quezon" to listOf("Lucena", "Tayabas", "Agdangan", "Alabat", "Atimonan", "Buenavista", "Burdeos", "Calauag", "Candelaria", "Catanauan", "Dolores", "General Luna", "General Nakar", "Guinayangan", "Gumaca", "Infanta", "Jomalig", "Lopez", "Lucban", "Macalelon", "Mauban", "Mulanay", "Padre Burgos", "Pagbilao", "Panukulan", "Patnanungan", "Perez", "Pitogo", "Plaridel", "Polillo", "Quezon", "Real", "Sampaloc", "San Andres", "San Antonio", "San Francisco", "San Narciso", "Sariaya", "Tagkawayan", "Tiaong", "Unisan"),

                // Region IV-B: MIMAROPA
                "Marinduque" to listOf("Boac", "Buenavista", "Gasan", "Mogpog", "Santa Cruz", "Torrijos"),
                "Occidental Mindoro" to listOf("Abra de Ilog", "Calintaan", "Looc", "Lubang", "Magsaysay", "Mamburao (Capital)", "Paluan", "Rizal", "Sablayan", "San Jose", "Santa Cruz"),
                "Oriental Mindoro" to listOf("Calapan", "Baco", "Bansud", "Bongabong", "Bulalacao", "Gloria", "Mansalay", "Naujan", "Pinamalayan", "Pola", "Puerto Galera", "Roxas", "San Teodoro", "Socorro", "Victoria"),
                "Palawan" to listOf("Puerto Princesa", "Aborlan", "Agutaya", "Araceli", "Balabac", "Bataraza", "Brooke's Point", "Busuanga", "Cagayancillo", "Coron", "Culion", "Cuyo", "Dumaran", "El Nido", "Kalayaan", "Linapacan", "Magsaysay", "Narra", "Quezon", "Rizal (Marcos)", "Roxas", "San Vicente", "Sofronio Española", "Taytay"),
                "Romblon" to listOf("Alcantara", "Banton", "Cajidiocan", "Calatrava", "Concepcion", "Corcuera", "Ferrol", "Looc", "Magdiwang", "Odiongan", "Romblon", "San Agustin", "San Andres", "San Fernando", "San Jose", "Santa Fe", "Santa Maria (Imelda)"),

                // Region V: Bicol Region
                "Albay" to listOf("Legazpi City (Capital)", "Daraga (Locsin)", "Ligao", "Tabaco", "Bacacay", "Camalig", "Daraga", "Guinobatan", "Jovellar", "Libon", "Malilipot", "Malinao", "Manito", "Oas", "Pio Duran", "Polangui", "Rapu-Rapu", "Santo Domingo (Libog)", "Tiwi"),
                "Camarines Norte" to listOf("Daet (Capital)", "Basud", "Capalonga", "Jose Panganiban", "Labo", "Mercedes", "Paracale", "San Lorenzo Ruiz (Imelda)", "San Vicente", "Santa Elena", "Talisay", "Vinzons"),
                "Camarines Sur" to listOf("Naga City", "Iriga City", "Baao", "Balatan", "Bato", "Bombon", "Buhi", "Bula", "Cabusao", "Calabanga", "Camaligan", "Canaman", "Caramoan", "Del Gallego", "Gainza", "Garchitorena", "Goa", "Lagonoy", "Libmanan", "Lupi", "Magarao", "Milaor", "Minalabac", "Nabua", "Ocampo", "Pamplona", "Pasacao", "Pili (Capital)", "Presentacion (Parubcan)", "Ragay", "Sagñay", "San Fernando", "San Jose", "Sipocot", "Siruma", "Tigaon", "Tinambac"),
                "Catanduanes" to listOf("Virac", "Bagamanoc", "Baras", "Bato", "Caramoran", "Gigmoto", "Pandan", "Panganiban (Payo)", "San Andres (Calolbon)", "San Miguel", "Viga"),
                "Masbate" to listOf("Masbate (Capital) City", "Aroroy", "Baleno", "Balud", "Batuan", "Cataingan", "Cawayan", "Claveria", "Dimasalang", "Esperanza", "Mandaon", "Milagros", "Mobo", "Monreal", "Palanas", "Pio V. Corpuz", "Placer", "San Fernando", "San Jacinto", "San Pascual", "Uson"),
                "Sorsogon" to listOf("Sorsogon City", "Barcelona", "Bulan", "Bulusan", "Casiguran", "Castilla", "Donsol", "Gubat", "Irosin", "Juban", "Magallanes", "Matnog", "Pilar", "Prieto Diaz", "Santa Magdalena"),

                // Region VI: Western Visayas
                "Aklan" to listOf("Kalibo (Capital)", "Altavas", "Balete", "Banga", "Batan", "Buruanga", "Ibajay", "Lezo", "Libacao", "Madalag", "Makato", "Malay", "Malinao", "Nabas", "New Washington", "Numancia", "Tangalan"),
                "Antique" to listOf("San Jose (Capital)", "Anini-Y", "Barbaza", "Belison", "Bugasong", "Caluya", "Culasi", "Hamtic", "Laua-An", "Libertad", "Pandan", "Patnongon", "San Remigio", "Sebaste", "Sibalom", "Tibiao", "Tobias Fornier (Dao)", "Valderrama"),
                "Capiz" to listOf("Roxas City (Capital)", "Cuartero", "Dao", "Dumalag", "Dumarao", "Ivisan", "Jamindan", "Ma-ayon", "Mambusao", "Panay", "Panitan", "Pilar", "Pontevedra", "President Roxas", "Sapi-an", "Sigma", "Tapaz"),
                "Guimaras" to listOf("Jordan (Capital)", "Buenavista", "Nueva Valencia", "San Lorenzo", "Sibunag"),
                "Iloilo" to listOf("Iloilo City", "Passi", "Ajuy", "Alimodian", "Anilao", "Badiangan", "Balasan", "Banate", "Barotac Nuevo", "Barotac Viejo", "Batad", "Bingawan", "Cabatuan", "Calinog", "Carles", "Concepcion", "Dingle", "Dueñas", "Dumangas", "Estancia", "Guimbal", "Igbaras", "Janiuay", "Lambunao", "Leganes", "Lemery", "Leon", "Maasin", "Miagao", "Mina", "New Lucena", "Oton", "Pavia", "Pototan", "San Dionisio", "San Enrique", "San Joaquin", "San Miguel", "San Rafael", "Santa Barbara", "Sara", "Tigbauan", "Tubungan", "Zarraga"),
                "Negros Occidental" to listOf("Bacolod", "Bago", "Cadiz", "Escalante", "Himamaylan", "Kabankalan", "La Carlota", "Sagay", "San Carlos", "Silay", "Sipalay", "Talisay", "Victorias", "Binalbagan", "Calatrava", "Candoni", "Cauayan", "Enrique B. Magalona", "Hinigaran", "Hinoba-an", "Ilog", "Isabela", "La Castellana", "Manapla", "Moises Padilla", "Murcia", "Pontevedra", "Pulupandan", "Salvador Benedicto", "San Enrique", "Toboso", "Valladolid"),

                // Region VII: Central Visayas
                "Bohol" to listOf("Tagbilaran City (Capital)", "Alburquerque", "Alicia", "Anda", "Antequera", "Baclayon", "Balilihan", "Batuan", "Bien Unido", "Bilar", "Buenavista", "Calape", "Candijay", "Carmen", "Catigbian", "Clarin", "Corella", "Cortes", "Dagohoy", "Danao", "Dauis", "Dimiao", "Duero", "Garcia Hernandez", "Getafe", "Guindulman", "Inabanga", "Jagna", "Lila", "Loay", "Loboc", "Loon", "Mabini", "Maribojoc", "Panglao", "Pilar", "Pres. Carlos P. Garcia (Pitogo)", "Sagbayan (Borja)", "San Isidro", "San Miguel", "Sevilla", "Sierra Bullones", "Sikatuna", "Talibon", "Trinidad", "Tubigon", "Ubay", "Valencia"),
                "Cebu" to listOf("Cebu City (Capital)", "Naga City", "Lapu-Lapu", "Danao City", "Talisay City", "Toledo City", "Bogo City", "Carcar City", "Naga", "Alcantara", "Alcoy", "Alegria", "Aloguinsan", "Argao", "Asturias", "Badian", "Balamban", "Bantayan", "Barili", "Boljoon", "Borbon", "Carmen", "Catmon", "Compostela", "Consolacion", "Cordoba", "Daanbantayan", "Dalaguete", "Dumanjug", "Ginatilan", "Lapu-Lapu City (Opon)","Liloan", "Madridejos", "Malabuyoc", "Mandaue City", "Medellin", "Minglanilla", "Moalboal", "Oslob", "Pilar", "Pinamungajan", "Poro", "Ronda", "Samboan", "San Fernando", "San Francisco", "San Remigio", "Santa Fe", "Santander", "Sibonga", "Sogod", "Tabogon", "Tabuelan", "Tuburan", "Tudela"),
                "Negros Oriental" to listOf("Dumaguete", "Bais", "Bayawan", "Canlaon", "Guihulngan", "Tanjay", "Amlan", "Ayungon", "Bacong", "Basay", "Bindoy", "Dauin", "Jimalalud", "La Libertad", "Mabinay", "Manjuyod", "Pamplona", "San Jose", "Santa Catalina", "Siaton", "Sibulan", "Tayasan", "Valencia", "Vallehermoso", "Zamboanguita"),
                "Siquijor" to listOf("Siquijor", "Enrique Villanueva", "Larena", "Lazi", "Maria", "San Juan"),

                // Region VIII: Eastern Visayas
                "Biliran" to listOf("Naval (Capital)", "Almeria", "Biliran", "Cabucgayan", "Caibiran", "Culaba", "Kawayan", "Maripipi"),
                "Eastern Samar" to listOf("Borongan (Capital) City", "Arteche", "Balangiga", "Balangkayan", "Can-Avid", "Dolores", "General MacArthur", "Giporlos", "Guiuan", "Hernani", "Jipapad", "Lawaan", "Llorente", "Maslog", "Maydolong", "Mercedes", "Oras", "Quinapondan", "Salcedo", "San Julian", "San Policarpo", "Sulat", "Taft"),
                "Leyte" to listOf("Tacloban", "Ormoc City", "Abuyog", "Alangalang", "Albuera", "Babatngon", "Barugo", "Bato", "Baybay", "Burauen", "Calubian", "Capoocan", "Carigara", "Dagami", "Dulag", "Hilongos", "Hindang", "Inopacan", "Isabel", "Jaro", "Javier", "Julita", "Kananga", "La Paz", "Leyte", "MacArthur", "Mahaplag", "Matag-ob", "Matalom", "Mayorga", "Merida", "Palo", "Palompon", "Pastrana", "San Isidro", "San Miguel", "Santa Fe", "Tabango", "Tabontabon", "Tanauan", "Tolosa", "Tunga", "Villaba"),
                "Northern Samar" to listOf("Catarman", "Allen", "Biri", "Bobon", "Capul", "Catubig", "Gamay", "Laoang", "Lapinig", "Las Navas", "Lavezares", "Lope de Vega", "Mapanas", "Mondragon", "Palapag", "Pambujan", "Rosario", "San Antonio", "San Isidro", "San Jose", "San Roque", "San Vicente", "Silvino Lobos", "Victoria"),
                "Samar (Western Samar)" to listOf("Calbayog", "Catbalogan", "Almagro", "Basey", "Calbiga", "Daram", "Gandara", "Hinabangan", "Jiabong", "Marabut", "Matuguinao", "Motiong", "Pagsanghan", "Paranas", "Pinabacdao", "San Jorge", "San Jose de Buan", "San Sebastian", "Santa Margarita", "Santa Rita", "Santo Niño", "Tagapul-an", "Talalora", "Tarangnan", "Villareal", "Zumarraga"),
                "Southern Leyte" to listOf("Maasin", "Anahawan", "Bontoc", "Hinunangan", "Hinundayan", "Libagon", "Liloan", "Limasawa", "Macrohon", "Malitbog", "Padre Burgos", "Pintuyan", "Saint Bernard", "San Francisco", "San Juan", "San Ricardo", "Silago", "Sogod", "Tomas Oppus"),

                // Region IX: Zamboanga Peninsula
                "Zamboanga Del Norte" to listOf("Dipolog", "Dapitan", "Bacungan", "Baliguian", "Godod", "Gutalac", "Jose Dalman (Ponot)", "Kalawit", "Katipunan", "La Libertad", "Labason", "Liloy", "Manukan", "Mutia", "Piñan", "Polanco", "Pres. Manuel A. Roxas", "Rizal", "Salug", "Sergio Osmeña Sr.", "Siayan", "Sibuco", "Sibutad", "Sindangan", "Siocon", "Sirawai", "Tampilisan"),
                "Zamboanga Del Sur" to listOf("Pagadian", "Zamboanga City", "Aurora", "Bayog", "Dimataling", "Dinas", "Dumalinao", "Dumingag", "Guipos", "Josefina", "Kumalarang", "Labangan", "Lakewood", "Lapuyan", "Mahayag", "Margosatubig", "Midsalip", "Molave", "Pitogo", "Ramon Magsaysay", "San Miguel", "San Pablo", "Sominot", "Tabina", "Tambulig", "Tigbao", "Tukuran", "Vincenzo A. Sagun"),
                "Zamboanga Sibugay" to listOf("Ipil", "Alicia", "Buug", "Diplahan", "Imelda", "Kabasalan", "Mabuhay", "Malangas", "Naga", "Olutanga", "Payao", "Roseller Lim", "Siay", "Talusan", "Titay", "Tungawan"),

                // Region X: Northern Mindanao
                "Bukidnon" to listOf("Malaybalay (Capital) City", "Valencia City", "Baungon", "Cabanglasan", "Damulog", "Dangcagan", "Don Carlos", "Impasugong", "Kadingilan", "Kalilangan", "Kibawe", "Kitaotao", "Lantapan", "Libona", "Malitbog", "Manolo Fortich", "Maramag", "Pangantucan", "Quezon", "San Fernando", "Sumilao", "Talakag"),
                "Camiguin" to listOf("Mambajao (Capital)", "Catarman", "Guinsiliban", "Mahinog", "Sagay"),
                "Lanao Del Norte" to listOf("Iligan", "Bacolod", "Baloi", "Baroy", "Kapatagan", "Kauswagan", "Kolambugan", "Lala", "Linamon", "Magsaysay", "Maigo", "Matungao", "Munai", "Nunungan", "Pantao Ragat", "Pantar", "Poona Piagapo", "Salvador", "Sapad", "Sultan Naga Dimaporo", "Tagoloan", "Tangcal", "Tubod"),
                "Misamis Occidental" to listOf("Oroquieta City (Capital)", "Ozamiz", "Tangub City", "Aloran", "Baliangao", "Bonifacio", "Calamba", "Clarin", "Concepcion", "Don Victoriano Chiongbian", "Jimenez", "Lopez Jaena", "Panaon", "Plaridel", "Sapang Dalaga", "Sinacaban", "Tudela"),
                "Misamis Oriental" to listOf("Cagayan de Oro City (Capital)", "Gingoog", "Alubijid", "Balingasag", "Balingoan", "Binuangan", "Claveria", "El Salvador", "Gitagum", "Initao", "Jasaan", "Kinoguitan", "Lagonglong", "Laguindingan", "Libertad", "Lugait", "Magsaysay", "Manticao", "Medina", "Naawan", "Opol", "Salay", "Sugbongcogon", "Tagoloan", "Talisayan", "Villanueva"),

                // Region XI: Davao Region
                "Compostela Valley" to listOf("Nabunturan (Capital)", "Compostela", "Laak (San Vicente)", "Mabini (Doña Alicia)", "Maco", "Maragusan (San Mariano)", "Mawab", "Monkayo", "Montevista", "New Bataan", "Pantukan"),
                "Davao Del Norte" to listOf("Island Garden  Samal City","Tagum (Capital) City", "Panabo City", "Samal", "Asuncion (Saug)", "Braulio E. Dujali", "Carmen", "Kapalong", "New Corella", "San Isidro", "Santo Tomas", "Talaingod"),
                "Davao Del Sur" to listOf("Digos (Capital) City", "Bansalan", "Don Marcelino", "Hagonoy", "Jose Abad Santos", "Kiblawan", "Magsaysay", "Malalag", "Matanao", "Padada", "Santa Cruz", "Sulop"),
                "Davao Occidental" to listOf("Malita", "Don Marcelino", "Jose Abad Santos (Trinidad)", "Santa Maria", "Sarangani"),
                "Davao Oriental" to listOf("Mati (Capital) City", "Baganga", "Banaybanay", "Boston", "Caraga", "Cateel", "Governor Generoso", "Lupon", "Manay", "San Isidro", "Tarragona"),

                // Region XII: SOCCSKSARGEN
                "Cotabato (North Cotabato)" to listOf("Kidapawan (Capital) City", "Alamada", "Aleosan", "Antipas", "Arakan", "Banisilan", "Carmen", "Kabacan", "Libungan", "M'lang", "Magpet", "Makilala", "Matalam", "Midsayap", "Pigcawayan", "Pikit", "President Roxas", "Tulunan"),
                "Cotabato City" to listOf("Cotabato City"),
                "Sarangani" to listOf("Alabel (Capital)", "Glan", "Kiamba", "Maasim", "Maitum", "Malapatan", "Malungon"),
                "South Cotabato" to listOf("Koronadal (Capital) City", "General Santos City (Dadiangas)", "Banga", "Lake Sebu", "Norala", "Polomolok", "Santo Niño", "Surallah", "Tampakan", "Tantangan", "Tupi"),
                "Sultan Kudarat" to listOf("Isulan", "Tacurong", "Bagumbayan", "Columbio", "Esperanza", "Kalamansig", "Lambayong (Mariano Marcos)", "Lebak", "Lutayan", "Palimbang", "President Quirino", "Sen. Ninoy Aquino"),

                // Region XIII: Caraga
                "Agusan Del Norte" to listOf("Butuan City (Capital)", "Cabadbaran City", "Buenavista", "Carmen", "Jabonga", "Kitcharao", "Las Nieves", "Magallanes", "Nasipit", "Remedios T. Romualdez", "Santiago", "Tubay"),
                "Agusan Del Sur" to listOf("Bayugan City", "Bunawan", "Esperanza", "La Paz", "Loreto", "Prosperidad (Capital)", "Rosario", "San Francisco", "San Luis", "Santa Josefa", "Sibagat", "Talacogon", "Trento", "Veruela"),
                "Dinagat Islands" to listOf("Basilisa (Rizal)", "Cagdianao", "Dinagat", "Libjo (Albor)", "Loreto", "San Jose (Capital)", "Tubajon"),
                "Surigao Del Norte" to listOf("Surigao City", "Alegria", "Bacuag", "Burgos", "Claver", "Dapa", "Del Carmen", "General Luna", "Gigaquit", "Mainit", "Malimono", "Pilar", "Placer", "San Benito", "San Francisco (Anao-Aon)", "San Isidro", "Santa Monica", "Sison", "Socorro", "Tagana-an", "Tubod"),
                "Surigao Del Sur" to listOf("Bislig City", "Tandag", "Barobo", "Bayabas", "Cagwait", "Cantilan", "Carmen", "Carrascal", "Cortes", "Hinatuan", "Lanuza", "Lianga", "Lingig", "Madrid", "Marihatag", "San Agustin", "San Miguel", "Tagbina", "Tago"),

                // BARMM: Bangsamoro Autonomous Region in Muslim Mindanao
                "Basilan" to listOf("Lamitan City", "Akbar", "Al-Barka", "Hadji Mohammad Ajul", "Hadji Muhtamad", "Lantawan", "Maluso", "Sumisip", "Tabuan-Lasa", "Tipo-Tipo", "Tuburan", "Ungkaya Pukan"),
                "Lanao Del Sur" to listOf("Marawi", "Bacolod-Kalawi", "Balabagan", "Balindong", "Bayang", "Binidayan", "Buadiposo-Buntong", "Bubong", "Butig", "Calanogas", "Ditsaan-Ramain", "Ganassi", "Kapai", "Kapatagan", "Lumba-Bayabao", "Lumbaca-Unayan", "Lumbatan", "Lumbayanague", "Madalum", "Madamba", "Maguing", "Malabang", "Marantao", "Marogong", "Masiu", "Mulondo", "Pagayawan", "Piagapo", "Poona Bayabao", "Pualas", "Saguiaran", "Sultan Dumalondong", "Picong", "Tagoloan Ii", "Tamparan", "Taraka", "Tubaran", "Tugaya", "Wao"),
                "Maguindanao Del Norte" to listOf("Datu Odin Sinsuat", "Datu Blah T. Sinsuat", "Kabuntalan", "Matanog", "Northern Kabuntalan", "Parang", "Sultan Kudarat", "Sultan Mastura", "Upi"),
                "Maguindanao Del Sur" to listOf("Buluan", "Ampatuan", "Barira", "Buldon", "Datu Abdullah Sangki", "Datu Anggal Midtimbang", "Datu Hoffer Ampatuan", "Datu Montawal", "Datu Paglas", "Datu Piang", "Datu Saudi Ampatuan", "Datu Unsay", "Gen. Salipada K. Pendatun", "Guindulungan", "Mamasapano", "Mangudadatu", "Pagalungan", "Paglat", "Pandag", "Rajah Buayan", "Shariff Aguak", "Shariff Saydona Mustapha", "Sultan Sa Barongis", "Talayan", "Talitay", "South Upi"),
                //"Sulu" to listOf("Jolo", "Banguingui", "Hadji Panglima Tahil", "Indanan", "Kalingalan Caluang", "Lugus", "Luuk", "Maimbung", "Old Panamao", "Omar", "Pandami", "Panglima Estino", "Pangutaran", "Parang", "Pata", "Patikul", "Siasi", "Talipao", "Tapul", "Tongkil"),
                "Sulu" to listOf("Jolo", "Hadji Panglima Tahil", "Indanan", "Kalingalan Caluang", "Lugus", "Luuk", "Maimbung", "Old Panamao", "Omar", "Pandami", "Panglima Estino", "Pangutaran", "Parang", "Pata", "Patikul", "Siasi", "Talipao", "Tapul", "Tongkil"),
                "Tawi-Tawi" to listOf("Bongao (Capital)", "Languyan", "Mapun (Cagayan De Tawi-Tawi)", "Panglima Sugala", "Sapa-Sapa", "Sibutu", "Simunul", "Sitangkai", "South Ubian", "Tandubas", "Turtle Islands"),


            )


                val cities = provinces[province]
                return cities?.random() ?: ""
        }

    fun getRandomProvince(): String {

        //Original List
//        val provinces = listOf(
//            "Ilocos Norte", "Ilocos Sur", "La Union", "Pangasinan",
//            "Batanes", "Cagayan", "Isabela", "Nueva Vizcaya", "Quirino",
//            "Bataan", "Bulacan", "Nueva Ecija", "Pampanga", "Tarlac", "Zambales", "Aurora",
//            "Batangas", "Cavite", "Laguna", "Quezon", "Rizal",
//            "Marinduque", "Occidental Mindoro", "Oriental Mindoro", "Palawan", "Romblon",
//            "Albay", "Camarines Norte", "Camarines Sur", "Catanduanes", "Masbate", "Sorsogon",
//            "Aklan", "Antique", "Capiz", "Guimaras", "Iloilo", "Negros Occidental",
//            "Bohol", "Cebu", "Negros Oriental", "Siquijor",
//            "Biliran", "Eastern Samar", "Leyte", "Northern Samar", "Samar", "Southern Leyte",
//            "Zamboanga Del Norte", "Zamboanga Del Sur", "Zamboanga Sibugay",
//            "Bukidnon", "Camiguin", "Lanao Del Norte", "Misamis Occidental", "Misamis Oriental",
//            "Davao de Oro", "Davao Del Norte", "Davao Del Sur", "Davao Occidental", "Davao Oriental",
//            "Cotabato (North Cotobato)", "South Cotabato", "Sultan Kudarat", "Sarangani",
//            "Agusan Del Norte", "Agusan Del Sur", "Dinagat Islands", "Surigao Del Norte", "Surigao Del Sur",
//            "Basilan", "Lanao Del Sur", "Maguindanao Del Norte", "Maguindanao Del Sur", "Sulu", "Tawi-Tawi"
//        )

        val provinces = listOf(
            "Ilocos Norte", "Ilocos Sur", "La Union", "Pangasinan",
            "Batanes", "Cagayan", "Isabela", "Nueva Vizcaya", "Quirino",
            "Bataan", "Bulacan", "Nueva Ecija", "Pampanga", "Tarlac", "Zambales", "Aurora",
            "Batangas", "Cavite", "Laguna", "Quezon", "Rizal",
            "Marinduque", "Occidental Mindoro", "Oriental Mindoro", "Palawan", "Romblon",
            "Albay", "Camarines Norte", "Camarines Sur", "Catanduanes", "Masbate", "Sorsogon",
            "Aklan", "Antique", "Capiz", "Guimaras", "Iloilo",
            "Bohol", "Cebu", "Siquijor",
            "Biliran", "Leyte", "Samar (Western Samar)", "Southern Leyte",
            "Zamboanga Del Norte", "Zamboanga Del Sur", "Zamboanga Sibugay",
            "Bukidnon", "Camiguin", "Lanao Del Norte", "Misamis Occidental", "Misamis Oriental",
            "Davao Del Norte", "Davao Del Sur", "Davao Oriental",
            "Cotabato (North Cotabato)", "Sultan Kudarat", "Sarangani",
            "Agusan Del Norte", "Agusan Del Sur", "Dinagat Islands", "Surigao Del Norte", "Surigao Del Sur",
            "Basilan", "Lanao Del Sur", "Sulu", "Tawi-Tawi"
        )

        val randomIndex = Random.nextInt(provinces.size)
        return provinces[randomIndex]
    }

    fun getDateSixMonthsFromNow(): String {
        val today = LocalDate.now()
        val sixMonthsFromNow = today.plusMonths(6)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return sixMonthsFromNow.format(formatter)
    }

    fun getDateOneMonthFromNow(): String {
        val today = LocalDate.now()
        val oneMonthFromNow = today.plusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
        return oneMonthFromNow.format(formatter)
    }

    fun getDateOneMonthMinusOneDay(): String {
        val today = LocalDate.now()
        val oneMonthFromNow = today.plusMonths(1).minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
        return oneMonthFromNow.format(formatter)
    }

    fun getDateSixMonthsMinusOneDay(): String {
        val today = LocalDate.now()
        val sixMonthsFromNow = today.plusMonths(6).minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return sixMonthsFromNow.format(formatter)
    }

    fun createInvalidBirthdayDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val currentDate = LocalDate.now()
        val validBirthday = currentDate.minusYears(18)
        val invalidBirthday = validBirthday.plusDays(1)
        return invalidBirthday.format(formatter)
    }

    fun getYearToday(): Int{
        val currentDate = LocalDate.now()
        return currentDate.year
    }

    fun getYearMinus18Years(): Int {
        val currentDate = LocalDate.now()
        val yearMinus18Years = currentDate.minusYears(18).year
        return yearMinus18Years
    }

    fun getMonthToday(): String {
        // Get the current date
        val currentDate = LocalDate.now()

        // Get the current month
        val currentMonth = currentDate.month

        // Get the display name of the current month
        return currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    fun getNextMonth(): String {
        // Get the current date
        val currentDate = LocalDate.now()

        // Get the next month
        val nextMonth = currentDate.plusMonths(1)

        // Format the next month to a string
        val formatter = DateTimeFormatter.ofPattern("MMMM")
        return nextMonth.format(formatter)
    }

    private fun getDayToday(): Int {
        val currentDate = LocalDate.now()
        return currentDate.dayOfMonth
    }

    private fun getNextDay(): Int {
        val currentDate = LocalDate.now()
        val nextDay = currentDate.plusDays(1)
        return nextDay.dayOfMonth
    }

    private fun getPreviousDay(): Int {
        val currentDate = LocalDate.now()
        val previousDay = currentDate.minusDays(1)
        return previousDay.dayOfMonth
    }

    fun getRandomCivilStatus(): String {
        val statuses = listOf("Single", "Married", "Divorced", "Widowed", "Separated")
        return statuses[Random.nextInt(statuses.size)]
    }

    fun getRandomGender(): String {
        val genders = listOf("Male", "Female")
        return genders[Random.nextInt(genders.size)]
    }

    fun generateRandomPHNumber(): String {
//        val firstDigits = listOf("81", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99")
        val firstDigits = listOf("81", "89", "91", "92", "93", "94", "95", "96", "97", "98", "99")
        val randomTwoDigits = firstDigits.random()
        val remainingDigits = (0..6)
            .map { (0..9).random() }
            .joinToString("")

        return "9$randomTwoDigits$remainingDigits"
    }


    fun obfuscateEmail(email: String): String {
        val firstPart = email.take(4) // Get the first 4 characters
        val ending = "om"

        // Calculate the number of asterisks needed
        val asteriskCount = email.length - firstPart.length - ending.length
        val asterisks = "*".repeat(asteriskCount)

        // Concatenate the parts
        val obfuscatedEmail = firstPart + asterisks + ending

        return obfuscatedEmail
    }

    fun obfuscatePHNumber(phoneNumber: String): String {
        val visibleDigits = phoneNumber.takeLast(2) // Get the last 2 digits
        val asterisks = "*".repeat(11) // Generate 11 asterisks

        return asterisks + visibleDigits
    }

    fun changeLastDigit(phoneNumber: String): String {
        // Get the last digit
        val lastDigit = phoneNumber.last()

        // Generate a different random digit
        var newLastDigit: Char
        do {
            newLastDigit = Random.nextInt(0, 10).toString().single()
        } while (newLastDigit == lastDigit)

        // Replace the last digit with the new random digit
        return phoneNumber.dropLast(1) + newLastDigit
    }

    fun search(key:String){

        describe("Wait for Page to Load")
            .waitForDisplay("Search", waitSeconds = 30.0)
        it.exist("Search")

        it.tap("Search")
            .sendKeys(key)

        describe("Wait for Page to Load")
            .waitForDisplay("xpath=//android.widget.TextView[@text=\"$key\"]\n", waitSeconds = 30.0)
        it.tap("xpath=//android.widget.TextView[@text=\"$key\"]\n")

        it.exist(key)
    }

    fun getRandomCountry(): String {
        val countries = listOf(
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola",
            "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia",
            "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso",
            "Burundi", "Cambodia", "Cameroon", "Canada", "Central African Republic",
            "Chad", "Chile", "China", "Colombia", "Comoros", "Congo - Brazzaville", "Congo - Kinshasa",
            "Congo, Republic of the", "Costa Rica", "Croatia", "Cuba", "Cyprus",
            "Czechia", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador",
            "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
            "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece",
            "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary",
            "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "North Korea", "South Korea",
            "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives",
            "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia",
            "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar",
            "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria",
            "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territories", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Poland", "Portugal", "Qatar",
            "Romania", "Russia", "Rwanda", "Saint Lucia", "Saint Vincent and the Grenadines",
            "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Serbia",
            "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname",
            "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste",
            "Togo", "Tonga", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu",
            "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay",
            "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        )

        val randomIndex = Random.nextInt(countries.size)
        return countries[randomIndex]
    }

    fun monthYear18YearsAgo(): String {
        val currentDate = LocalDate.now()
        val pastDate = currentDate.minusYears(18)
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return pastDate.format(formatter)
    }
}