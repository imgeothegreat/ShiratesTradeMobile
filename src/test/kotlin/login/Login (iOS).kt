package login

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class `Login (iOS)` : UITest() {

    private var email = "tradetestqa+superusergmail.com"
    private var pword = "password123$"
    private val pin = "123123"

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
                    it.select("Invalid Email address")
                        .textIs("Invalid Email address")

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
                    it.select("Invalid Email address")
                        .textIs("Invalid Email address")

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
                    it.select("Email address is required")
                        .textIs("Email address is required")

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
                    it.select("Invalid Email address")
                        .textIs("Invalid Email address")

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
                    email = "tradetestqa+superuser@gmai!l.com"


                    it.macro("[Login]", email, pword)
                    it.select("Invalid Email address")
                        .textIs("Invalid Email address")

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
                    it.select("Invalid Email address")
                        .textIs("Invalid Email address")

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

                    describe("Wait for Toast Message")
                        .waitForDisplay("Incorrect Email or Password.")

                    it.exist("Incorrect Email or Password.")

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
                    it.select("Password is required")
                        .textIs("Password is required")

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
                    it.tap("Log In")
                    describe("Wait for Terms of Use Text")
                        .waitForDisplay("Terms of Use")
                    it.tap("Terms of Use")
                    it.select("Terms and Conditions")
                        .textIs("Terms and Conditions")
                    it.scrollToBottom()
                    it.select("GOVERNING LAW AND VENUE. These Terms and Conditions shall be governed by the laws of the Philippines, and any dispute arising from the use of the Platform or concerning these Terms and Conditions shall be brought exclusively before the courts of Makati City, Philippines.")
                        .textIs("GOVERNING LAW AND VENUE. These Terms and Conditions shall be governed by the laws of the Philippines, and any dispute arising from the use of the Platform or concerning these Terms and Conditions shall be brought exclusively before the courts of Makati City, Philippines.")

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
                    it.tap("Log In")
                    it.tap("Privacy Policy.")
                    describe("Wait for Privacy Policy Text")
                        .waitForDisplay("PRIVACY POLICY")
                    it.select("PRIVACY POLICY")
                        .textIs("PRIVACY POLICY")
                    it.scrollToBottom()
                    it.select("You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.")
                        .textIs("You may inquire or request for information regarding any matter relating to the processing of your personal data under our custody, including the data privacy and security policies implemented to ensure the protection of your personal data. You may contact us at dpo@investagrams.com so we may assist you.")

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

                    it.tap("Log In")
                    it.tap("Your email address")
                        .sendKeys(email)
                    it.tap("Your password")
                        .sendKeys(pword)

                    //Check if Eye Icon hides password by default
                    it.select("\uE933")
                        .textIs("\uE933")

                    it.select("••••••••")
                        .textIs("••••••••")

                    //Show Password
                    it.tap("\uE933")
                    it.select("\uE932")
                        .textIs("\uE932")

                    it.select("password")
                        .textIs(pword)

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
                    it.tap("Log In")

                    it.tap("Don’t have an account? Join us.")

                    it.select("Create an Account.")
                        .textIs("Create an Account.")

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
                    it.tap("Existing Investagrams user?")

                    it.select("Yey, you’re back!")
                        .textIs("Yey, you’re back!")


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
    @DisplayName("Valid Log In Scenario")
    @Order(13)
    fun validLogin(){
        scenario{
            case(1){
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", "1", pin)

                    it.select("About My Portfolio")
                        .textIs("About My Portfolio")

                }
            }
        }
    }

}