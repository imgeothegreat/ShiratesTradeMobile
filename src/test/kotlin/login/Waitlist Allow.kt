package login

import io.bloco.faker.Faker
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.util.*
import kotlin.random.Random
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Waitlist Allow`: UITest() {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var broker = "1"
    private var firstName = "Tradetest"
    private var pin = "123123"
    private val useUpdate = false

    @Test
    @DisplayName("Create New Account Waitlist")
    @Order(1)
    fun createNewAccountWailisted(){
        scenario{
            //Missing @
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
                    val random3DigitNumber = Random.nextInt(1, 1000)
                    val email =
                        "1tradeqa+$firstName$lastName$random3DigitNumber@gmail.com".lowercase(Locale.getDefault())
                    val readPolicy = false

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)

                    describe("Wait for Broker Text To Appear")
                        .waitForDisplay("Select Broker", waitSeconds = 30.0)

                    //Check Important Labels
                    it.exist("Select Broker")
                    it.exist("Need help? Learn more")
                    it.exist("here")
                    it.exist("Log out")

                    it.wait(4)
                    it.tap("Log out")
                    it.tap("YES")

                    describe("Wait for page to load")
                        .waitForDisplay("You may log in using your investagrams account.", waitSeconds = 30.0)

                    it.exist("You may log in using your investagrams account.")

                    it.tap("Your email address")
                        .sendKeys(email)
                    it.tap("Your password")
                        .sendKeys(password)
                    it.tap("Log In")

                    describe("Wait for Broker Text To Appear")
                        .waitForDisplay("Select Broker", waitSeconds = 30.0)

                    //Check Important Labels
                    it.exist("Select Broker")
                    it.exist("Need help? Learn more")
                    it.exist("here")
                    it.exist("Log out")

                    it.wait(4)
                    it.tap("Log out")
                    it.tap("YES")

                    describe("Wait for page to load")
                        .waitForDisplay("You may log in using your investagrams account.", waitSeconds = 30.0)

                    it.exist("You may log in using your investagrams account.")


                    it.terminateApp()
                    it.launchApp()
                }


            }
        }
    }

    @Test
    @DisplayName("Existing User Can Login")
    @Order(2)
    fun existingUserLogin(){
        scenario{

            case(1) {
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    it.terminateApp()
                    it.launchApp()
                }


            }
        }
    }
}