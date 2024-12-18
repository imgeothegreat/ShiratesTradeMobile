package login

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.terminateApp
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Login No Internet` : UITest() {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var firstName = "Tradetest"
    private val pin = "123123"
    private val useUpdate = false

    //First you need to manually turn off the internet of the phone to simulate no signal
    @Test
    @DisplayName("Log In No Internet")
    @Order(1)
    fun validLogin(){
        scenario{
            case(1){
                expectation {

                    it.macro("[Login]", email, pword)

                    describe("Wait for Page to Load")
                        .waitForDisplay("No internet connection. Please check your internet connection and try again.")

                    it.exist("No internet connection. Please check your internet connection and try again.")

                    it.terminateApp()
                }
            }
        }
    }
}