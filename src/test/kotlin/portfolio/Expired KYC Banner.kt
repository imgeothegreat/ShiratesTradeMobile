package portfolio

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Expired KYC Banner` : UITest() {

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

    }
}