package portfolio

import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.getCurrenTradingHour
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Portfolio: UITest()  {

    private var email = "tradetestqa+superusernew@gmail.com"
    private var pword = "password123$"
//    private var email = "thetestdummytest+tradebuyer@gmail.com"
//    private var pword = "123123"
    private val pin = "123123"
    private val useUpdate = false
    private val broker = "1"
    private val hasSubscription = true
    private val hasStocks = false
    private val hasTradePerformance = false

    @Test
    @DisplayName("Explore Portfolio Features")
    @Order(1)
    fun explorePortfolio(){
        scenario{
            //Missing @
            case(1) {
                expectation {

                    //it.macro("[Install Code Push First]", email, pword, pin, broker, useUpdate)

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

                    //Labels Check
                    it.exist("powered by")
                    it.exist("*PH Time:*")

                    //Get Current Trading Hour
                    val currentTradingHour = getCurrenTradingHour()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Market: $currentTradingHour")
                    it.exist("Market: $currentTradingHour")

                    it.exist("Available Cash")
                    it.exist("*PHP*")

                    it.exist("Trade Analytics")
                    it.exist("Port Stats, Trade Records, Transactions")


                    //Subscriptions Tab
//                    it.macro("[Explore Subscriptions]", hasSubscription, pin)

                    //Search Stock
                    //Tap Search
                    var randomStockCodeMain = ""
                    val disabledStocks = listOf("MER", "GLO")

                    do {
                        randomStockCodeMain = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(randomStockCodeMain))

                    val stockNameMain = Trade.getStockNameByCode(randomStockCodeMain)

                    //My Stocks
                    it.macro("[Explore My Stocks]", hasStocks)

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

                    val stockNameMainInside = Trade.getStockNameByCode(randomStockCodeMainInside)

                    it.macro("[Search Stock]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Explore Asset Page]", randomStockCodeMainInside, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Save to Watcher]", randomStockCodeMainInside, stockNameMainInside)


                    //Go to Wallet Page
                    it.macro("[Explore Wallet Page]", useUpdate)

                    // Go To Trade Analytics

                    it.macro("[Explore Trade Analytics]", useUpdate, hasTradePerformance)

                    //Explore Trade Button
                    it.macro("[Explore Invest]", useUpdate, hasStocks)


                    //My Orders
                    it.macro("[Explore Orders]")

                    //Announcements
                    it.macro("[Explore Announcements Tab]")


                }


            }
        }

    }
}