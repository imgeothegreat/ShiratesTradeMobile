package market

import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.getRandomStockCode
import macro.Trade.getStockNameByCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Market: UITest()  {

    private var email = "tradetestqa+superusernew@gmail.com"
    private var pword = "password123$"
    private val pin = "123123"
    private var broker = "1"
    private val useUpdate = false
    private val hasStocks = false
    @Test
    @DisplayName("Explore Market Tab")
    @Order(1)
    fun exploreMarketPage() {
        scenario{
            //Missing @
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)
                    
                    it.tap("Explore")
                    it.exist("PSEI")
                    it.exist("Philippines Stock Exchange Index")

                    it.macro("[Explore Sectors]")

                    it.scrollUp()

                    it.exist("PSEI")
                    it.exist("Philippines Stock Exchange Index")


                    //Tap Search
                    var randomStockCodeMain = ""
                    val disabledStocks = listOf("MER","GLO")

                    do {
                        randomStockCodeMain = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(randomStockCodeMain))

                    val stockNameMain = getStockNameByCode(randomStockCodeMain)

                    //Read Latest Notif
                    it.macro("[Read Latest Notifs]")

                    it.macro("[Search Stock]", randomStockCodeMain, stockNameMain)
                    it.macro("[Explore Asset Page]", randomStockCodeMain, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMain, stockNameMain)

                    //Inside Search
                    var randomStockCodeMainInside = ""

                    do {
                        randomStockCodeMainInside = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(randomStockCodeMainInside))

                    val stockNameMainInside = getStockNameByCode(randomStockCodeMainInside)

                    it.macro("[Search Stock]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Explore Asset Page]", randomStockCodeMainInside, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMainInside, stockNameMainInside)


                    //Go Back to Market Page
                    //it.tap("\uE944")
                    it.tap(x=60,y=216)

                    describe("Wait for Page to Load")
                        .waitForDisplay("PSEI")


                    it.exist("PSEI")
                    it.exist("Philippines Stock Exchange Index")

                    //View More
                    it.tap("Philippines Stock Exchange Index")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Philippines Stock Exchange Index")

                    it.exist("Philippines Stock Exchange Index")

                    it.exist("Statistics")

                    it.macro("[View Statistics PSEi]")

                    //Tap Different Months
                    it.macro("[Tap Month Ranges]")

                    //Line Chart

                    it.tap(x=684,y=1377)

                    //Normal Chart

                    it.tap(x=753, y=1377)

                    it.macro("[View Full Chart]", "PSEI", "Philippines Stock Exchange Index")

                    //Go back to last page
                    it.exist("Philippines Stock Exchange Index")
                    it.exist("Statistics")
                    it.exist("Full Chart")

                    it.wait(1)

                    //go back explore page
                    it.tap(x=60,y=216)


                    it.scrollToBottom()

                    it.macro("[Explore Market Picks]")


                }
            }

        }
    }


}