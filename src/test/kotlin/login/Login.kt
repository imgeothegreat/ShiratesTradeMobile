package login

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Ignore
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Login : UITest() {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var broker = "1"
    private var firstName = "Tradetest"
    private var pin = "123123"
    private val useUpdate = false

    @Test
    @DisplayName("Invalid Email Missing @ Symbol")
    @Order(1)
    fun invalidEmail(){
        scenario{
            //Missing @
            case(1) {
                expectation {

                    email = "tradetestqa+superusergmail.com"


                    it.macro("[Login]", email, pword)
                    it.exist("Invalid Email address")

                    it.terminateApp()
                    //it.launchApp()
                }


            }
        }
    }

    @Test
    @DisplayName("Invalid Email Missing Domain")
    @Order(2)
    fun invalidDomain(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser"


                    it.macro("[Login]", email, pword)
                    it.exist("Invalid Email address")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Empty Email")
    @Order(3)
    fun emptyEmail(){
        scenario{

            case(1) {
                expectation {
                    email = ""


                    it.macro("[Login]", email, pword)
                    it.exist("Email address is required")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Multiple @@ in Email")
    @Order(4)
    fun multipleAtEmail(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser@@gmail.com"


                    it.macro("[Login]", email, pword)
                    it.exist("Invalid Email address")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Invalid Characters in Domain")
    @Order(5)
    fun invalidCharactersInDomain(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser@gma!l.com"


                    it.macro("[Login]", email, pword)
                    it.exist("Invalid Email address")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Missing Top Level Domain")
    @Order(6)
    fun missingTopLevelDomain(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser@gmail"


                    it.macro("[Login]", email, pword)
                    it.exist("Invalid Email address")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Incorrect Password")
    @Order(7)
    fun incorrectPassword(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser@gmail.com"
                    val pword = "pasword"

                    it.macro("[Login]", email, pword)

                    describe("Wait for page to load")
                        .waitForDisplay("You may log in using your investagrams account.", waitSeconds = 30.0)

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Empty Password")
    @Order(8)
    fun emptyPassword(){
        scenario{

            //Empty Password
            case(1) {
                expectation {



                    val pword = ""

                    it.macro("[Login]", email, pword)

                    it.hideKeyboard()

                    it.scrollDown()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Password is required", waitSeconds = 30.0)

                    it.exist("Password is required")
                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Read Terms and Conditions")
    @Order(9)
    fun readTerms(){
        scenario{

            case(1) {
                expectation {

                    describe("Wait for Terms of Use Text")
                        .waitForDisplay("Log In")

                    it.tap("Log In")

                    describe("Wait for Terms of Use Text")
                        .waitForDisplay("Terms of Use")
                    it.tap("Terms of Use")

                    describe("Wait for page to load")
                        .waitForDisplay("Terms and Conditions", waitSeconds = 60.0)
                    it.exist("Terms and Conditions")

                    it.scrollDown()
                    it.scrollToTop()

                    it.scrollToBottom()
                    it.select("GOVERNING LAW AND VENUE. These Terms and Conditions shall be governed by the laws of the Philippines, and any dispute arising from the use of the Platform or concerning these Terms and Conditions shall be brought exclusively before the courts of Makati City, Philippines.")
                        .textIs("GOVERNING LAW AND VENUE. These Terms and Conditions shall be governed by the laws of the Philippines, and any dispute arising from the use of the Platform or concerning these Terms and Conditions shall be brought exclusively before the courts of Makati City, Philippines.")
                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Read Privacy Policy")
    @Order(10)
    fun readPrivacyPolicy(){
        scenario{

            case(1) {
                expectation {
                    describe("Wait for Privacy Policy Text")
                        .waitForDisplay("Log In")

                    it.tap("Log In")
                    it.tap("Privacy Policy.")

                    describe("Wait for Privacy Policy Text")
                        .waitForDisplay("PRIVACY POLICY")
                    it.exist("PRIVACY POLICY")

                    it.scrollDown()
                    it.scrollToTop()

                    it.scrollToBottom()
                    it.select("You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.")
                        .textIs("You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.")
                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Password Visibility")
    @Order(11)
    fun passwordVisibility(){
        scenario{

            case(1) {
                expectation {
                    email = "tradetestqa+superuser@gmail.com"
                    pword = "password123$"

                    describe("Wait for Home Page")
                        .waitForDisplay("Log In", waitSeconds = 30.0)


                    it.tap("Log In")
                    it.tap("Your email address")
                        .sendKeys(email)
                    it.tap("Your password")
                        .sendKeys(pword)

                    //Check if Eye Icon hides password by default
                    it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")

                    it.exist("••••••••••••")

                    //Show Password
                    it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
                    it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")

                    it.exist("password123$")
                        .textIs(pword)

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Join us clickable redirect")
    @Order(12)
    fun joinUsRedirect(){
        scenario{

            case(1) {
                expectation {

                    describe("Wait for Loading")
                        .waitForDisplay("Log In", waitSeconds = 30.0)

                    it.tap("Log In")

                    it.tap("Don’t have an account? Join us.")

                    it.exist("Create an Account.")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Existing Investagrams User Redirect")
    @Order(13)
    fun existingUserRedirect(){
        scenario{

            case(1) {
                expectation {

                    it.wait(2)

                    //it.tap("Existing Investagrams user?")
                    it.tap(x=537, y=2161)

                    describe("Wait for Page to Load")
                        .waitForDisplay("You may log in using your investagrams account.", waitSeconds = 30.0)
                    it.exist("You may log in using your investagrams account.")

                    it.terminateApp()
                    //it.launchApp()


                }
            }
        }
    }

//    @Test
//    @DisplayName("Forgot Password")
//    @Order(9)
//    fun forgotPassword(){
//        scenario{
//
//            //Missing Domain
//            case(1) {
//
//                val email = "tradetestqa+superuser@gmail.com"
//                val pword = ""
//
//                it.macro("[Login]", email, pword)
//                it.tap("Forgot Password?")
//                it.tap("Enter email address")
//                    .sendKeys(email)
//                it.tap("Send Link")
//                it.terminateApp()
//                it.launchApp()
//            }
//        }
//    }
    @Test
    @DisplayName("Forgot Password")
    @Order(14)
    fun forgotPassword(){
        scenario{
            case(1){
                expectation {

                    describe("Wait for Terms of Use Text")
                        .waitForDisplay("Log In")


                    it.tap("Log In")
                    it.tap("Forgot Password?")

                    it.exist("Forgot Password")

                    //Tap without email
                    it.tap("Send Link")
                    it.exist("Email is required")

                    val invalidEmail = "tradetestqa"

                    it.tap( "Enter email address")
                        .sendKeys(invalidEmail)

                    it.exist("Invalid Email address")

                    it.tap(invalidEmail)
                        .clearInput()
                        .sendKeys(email)

                    it.dontExist("Email is required")
                    it.dontExist("Invalid Email address")

                    it.tap("Send Link")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Change password link is sent to your email")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Yey, you’re back!")

                    it.exist("Yey, you’re back!")
                    it.exist("You may log in using your investagrams account.")

                    it.terminateApp()
                }
            }
        }
    }

    //Your access has been locked enter wrong password multiple times
    @Test
    @DisplayName("Access Locked Wrong Password")
    @Order(15)
    @Ignore
    fun accessLocked(){
        scenario{
            case(1){
                expectation {

                    email = "tradetestqa+superuser2@gmail.com"
                    pword = "123456"

                    it.macro("[Login]", email, pword)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.wait(3)
                    it.tap("Log In")
                    describe("Wait for Page to Load")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.wait(3)
                    it.tap("Log In")
                    describe("Wait for Page to Load")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.wait(3)
                    it.tap("Log In")
                    describe("Wait for Page to Load")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.wait(3)
                    it.tap("Log In")

                    describe("Wait for Page to Load")
                        .waitForDisplay( "Incorrect Email or Password. Your access has been locked. Try again in 1 minute.")


                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Non-Existing User")
    @Order(16)
    fun nonExistingUser(){
        scenario{
            case(1){
                expectation {

                    email = "tradetestqa+nonexistinguser@gmail.com"
                    pword = "123123"

                    it.macro("[Login]", email, pword)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Empty Email and Password")
    @Order(17)
    fun emptyEmailAndPassword(){
        scenario{
            case(1){
                expectation {

                    email = ""
                    pword = ""

                    it.macro("[Login]", email, pword)

                    it.exist("Email address is required")
                    it.exist("Password is required")

                    it.terminateApp()
                    //it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Login With Invalid PIN")
    @Order(18)
    fun loginWithInvalidPin() {
        scenario {
            case(1) {
                expectation {

                    email = "tradetestqa+superuser@gmail.com"
                    pword = "password123$"

                    pin = "789789"


                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for page to load")
                        .waitForDisplay("Invalid trading pin.", waitSeconds = 30.0)

                    it.exist("Invalid trading pin.")

                    //go back
                    it.wait(7)

                    // it.tap("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")

                    it.tap(x=55,y=128)
//                    it.tap(x=73,y=184)

                    it.macro("[Select Broker]", broker)

                    it.tap("Forgot PIN? Get help here")

//                    describe("Wait for page to load")
//                        .waitForDisplay(
//                            "An email has been sent to tradetestqa+superuser@gmail.com. Kindly check the email and follow the instructions on how to reset your Trading Pin. Make sure to check your spam folder as well.",
//                            waitSeconds = 30.0
//                        )
//
//                    it.exist("An email has been sent to tradetestqa+superuser@gmail.com. Kindly check the email and follow the instructions on how to reset your Trading Pin. Make sure to check your spam folder as well.")

                    pin = "12312"

                    pin.forEach { char ->
                        it.tap("$char")
                    }

                    //test remove entered digit
                    pin.forEach { char ->
                        //it.tap("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[4]/android.view.ViewGroup[12]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
                        it.tap(x=922,y=2249)
                    }

                    pin = "789789"

                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for page to load")
                        .waitForDisplay("Invalid trading pin.", waitSeconds = 30.0)

                    it.terminateApp()
                    //it.launchApp()

                }
            }
        }
    }

    @Test
    @DisplayName("Valid Log In Scenario")
    @Order(19)
    fun validLogin(){
        scenario{
            case(1){
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

                    it.terminateApp()
                    it.launchApp()

                    describe("Wait for Home Page")
                        .waitForDisplay("Help Center", waitSeconds = 30.0)

                    it.tap("Help Center")
                    it.exist("Help and Support")

                    describe("Wait for Home Page")
                        .waitForDisplay("Hello! How can we help you?", waitSeconds = 30.0)


                    it.wait(3)
                    //close
                    //it.tap("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
                    it.tap(x=55,y=128)
//                    it.tap(x=73,y=184)

                    describe("Wait for Home Page")
                        .waitForDisplay("Enter your PIN to unlock", waitSeconds = 30.0)
                    it.exist("Enter your PIN to unlock")

                    it.tap("Forgot PIN?")

//                    describe("Wait for Home Page")
//                        .waitForDisplay("An email has been sent to tradetestqa+superuser@gmail.com. Kindly check the email and follow the instructions on how to reset your Trading Pin. Make sure to check your spam folder as well.", waitSeconds = 30.0)

                    it.macro("[Lock Trading Pin]", pin, firstName)

                    it.macro("[Exit App]" , firstName)

                    it.terminateApp()
                }
            }
        }

        //Inactive
        //Ban
        //Active
    }








}