package research

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import macro.Portfolio
import macro.Portfolio.getUniqueStock
import macro.Trade
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.io.File
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Research : UITest() {

    private var email = "tradetestqa+superusernew@gmail.com"
    private var pword = "password123$"
    private val pin = "123123"
    private val broker = "2"
    private val useUpdate = false
    private val hasStocks = false
    private var firstName = "Tradetestqa"

    @Test
    @DisplayName("Explore Research")
    @Order(1)
    fun exploreResearch(){
        scenario {
            //Missing @
            case(1) {
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

                    //Search Stock
                    //Tap Search
                    var randomStockCodeMain = ""
                    val disabledStocks = listOf("MER", "GLO")

                    do {
                        randomStockCodeMain = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(randomStockCodeMain))

                    val stockNameMain = Trade.getStockNameByCode(randomStockCodeMain)

                    it.macro("[Search Stock]", randomStockCodeMain, stockNameMain)
                    it.macro("[Explore Asset Page]", randomStockCodeMain, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMain, stockNameMain)

                    //Inside Search
                    var randomStockCodeMainInside = ""

                    do {
                        randomStockCodeMainInside = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(randomStockCodeMainInside))

                    val stockNameMainInside = Trade.getStockNameByCode(randomStockCodeMainInside)

                    it.macro("[Search Stock]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Explore Asset Page]", randomStockCodeMainInside, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Save to Watcher]", randomStockCodeMainInside, stockNameMainInside)


                    it.macro("[Broker Research]")
                    it.macro("[Disclosure]", pin, firstName)
                    it.macro("[Financial Reports]", pin, firstName)

                    it.exist("Broker Research")
                    it.exist("Disclosure")
                    it.exist("Financial Report")

                    //Read Latest Notif
//                    it.macro("[Read Latest Notifs]")



                }

            }
        }
    }

}
