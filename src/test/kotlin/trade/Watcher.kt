package trade

import macro.Portfolio.getUniqueStock
import macro.Trade
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Watcher : UITest() {

    private var email = "tradetestqa+superusernew@gmail.com"
    private var pword = "password123$"
    private val pin = "123123"
    private val broker = "1"
    private val useUpdate = false
    private val hasStocks = false
    private var firstName = "Tradetestqa"

    @Test
    @DisplayName("Explore Watcher")
    @Order(1)
    fun exploreWatcher(){
        scenario {
            //Missing @
            case(1) {
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

                    it.macro("[Explore Watcher]")

                    describe("Wait for Home Page")
                        .waitForDisplay("Watcher", waitSeconds = 30.0)

                    it.exist("Watcher")

                }

            }
        }
    }
}