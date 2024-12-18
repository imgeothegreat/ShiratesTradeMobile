package onboarding

import io.bloco.faker.Faker
import macro.Onboarding
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.terminateApp
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.util.*
import kotlin.random.Random
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Onboarding No Internet` : UITest() {

    private var emailInstall = "intestergrams+globaladmin@gmail.com"
    private var pwordInstall = "password123$"
    private var pinInstall = "123123"
    private val useUpdate = false
    private val hasMinimumDeposit = false
    private val broker = "2"

    @Test
    @DisplayName("Create Account with no Internet")
    @Order(1)
    fun createAccount() {


        scenario {
            case(1) {
                expectation {

                    //create test account info
                    val faker = Faker()
                    var firstName = faker.name.firstName()

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
                    val readPolicy = false

                    it.macro("[Create an Account]", firstName, lastName, email, password, readPolicy)

                    describe("Wait for Page to Load")
                        .waitForDisplay("No internet connection. Please check your internet connection and try again.")

                    it.exist("No internet connection. Please check your internet connection and try again.")

                    it.terminateApp()
                }
            }
        }
    }
}