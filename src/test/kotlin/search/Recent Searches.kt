package search

import io.bloco.faker.Faker
import macro.Onboarding
import macro.Onboarding.formW8
import macro.Onboarding.formW9
import macro.Onboarding.generateBrokerList
import macro.Onboarding.generateRandom7DigitNumber
import macro.Onboarding.generateRandomNumber
import macro.Onboarding.generateRandomPHNumber
import macro.Onboarding.getAllSources
import macro.Onboarding.getDateSixYearsFromNow
import macro.Onboarding.getRandomAlphanumericID
import macro.Onboarding.getRandomAreaCode
import macro.Onboarding.getRandomAssetsRange
import macro.Onboarding.getRandomBarangayInCity
import macro.Onboarding.getRandomBoolean
import macro.Onboarding.getRandomCityInProvince
import macro.Onboarding.getRandomCountry
import macro.Onboarding.getRandomCountryCode
import macro.Onboarding.getRandomEmploymentStatus
import macro.Onboarding.getRandomIDType
import macro.Onboarding.getRandomIndustry
import macro.Onboarding.getRandomProvince
import macro.Onboarding.getRandomRange
import macro.Onboarding.getRandomRankedInvestmentObjectives
import macro.Onboarding.getRandomSources
import macro.Onboarding.getRandomTinNumber
import macro.Onboarding.getRandomUSTIN
import macro.Onboarding.getRandomValidIDType
import macro.Onboarding.getRandomWorkTitle
import macro.Onboarding.getRandomZipCode
import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.tapHomeSearch
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
class `Recent Searches` : UITest() {

    private var email = "tradetestqa+superusernew@gmail.com"
    private var pword = "password123$"
    private val pin = "123123"
    private var broker = "1"
    private val useUpdate = false
    private val hasStocks = false

    @Test
    @DisplayName("10 Recent Searches")
    @Order(1)
    fun recentSearch() {

        scenario {
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

                    // Set to keep track of used stock codes
                    val usedStockCodes = mutableSetOf<String>()
                    var randomStockCodeMain = ""
                    var stockNameMain = ""
                    var firstStockCode = ""
                    var previousStockCode = ""

                    for (i in 1..11) {
                        println("Iteration $i of 10")

                        // Keep getting new stock codes until we find an unused one



                        do {
                            randomStockCodeMain = getUniqueStock()
                        } while (usedStockCodes.contains(randomStockCodeMain))

                        // Add the new stock code to our used set
                        usedStockCodes.add(randomStockCodeMain)

                        stockNameMain = Trade.getStockNameByCode(randomStockCodeMain)

                        previousStockCode = randomStockCodeMain

                        if( i == 1){
                            it.dontExist("Recent Searches")
                            it.dontExist("Clear all")

                            firstStockCode = randomStockCodeMain
                        }



                        it.macro("[Search Stock]", randomStockCodeMain, stockNameMain)

                        // tap search
                        tapHomeSearch()
//                        it.tap(x = 899, y = 138)

                        describe("Wait for Home Page")
                            .waitForDisplay("Recent Searches", waitSeconds = 30.0)

                        it.exist("Search Stock")
                        it.exist("Cancel")
                        it.exist("Recent Searches")
                        it.exist("Clear all")

                        it.exist(randomStockCodeMain)
                        it.exist(stockNameMain)

                        it.exist(previousStockCode)
                        it.tap(previousStockCode)

                        val previousStockName = Trade.getStockNameByCode(previousStockCode)

                        it.exist(previousStockCode)
                        it.exist(previousStockName)

                        describe("Wait for Home Page")
                            .waitForDisplay("Bid And Ask", waitSeconds = 30.0)

                        it.exist("Full Chart")
                        it.exist("Bid And Ask")


                        // First Search Stock should not display anymore
                        if( i == 11){
                            it.dontExist(firstStockCode)

                            val firstStockName = Trade.getStockNameByCode(firstStockCode)
                            it.dontExist(firstStockName)
                        }
                    }


                    //Cancel Search

                    tapHomeSearch()

                    describe("Wait for Home Page")
                        .waitForDisplay("Recent Searches", waitSeconds = 30.0)


                    it.exist("Search Stock")
                    it.exist("Cancel")
                    it.exist("Recent Searches")
                    it.exist("Clear all")

                    it.tap("Cancel")

                    it.wait(2)

                    it.dontExist("Search Stock")
                    it.dontExist("Cancel")
                    it.dontExist("Recent Searches")
                    it.dontExist("Clear all")

                    //Delete Search

                    tapHomeSearch()

                    describe("Wait for Home Page")
                        .waitForDisplay("Recent Searches", waitSeconds = 30.0)


                    it.tap(x=977,y=560)

                    it.wait(4)

//                    it.dontExist(randomStockCodeMain)
//                    it.dontExist(stockNameMain)

                    //Close Search

                    it.tap("Cancel")

                    it.wait(1)

                    //it.tap(x = 899, y = 197)
                    tapHomeSearch()

                    describe("Wait for Home Page")
                        .waitForDisplay("Recent Searches", waitSeconds = 30.0)

                    it.dontExist(randomStockCodeMain)
                    it.dontExist(stockNameMain)


                    //Clear All

                    it.tap("Clear all")

                    it.exist("Search Stock")
                    it.exist("Cancel")
                    it.dontExist("Recent Searches")
                    it.dontExist("Clear all")

                    usedStockCodes.forEach { stockCode ->
                        println("Stock Code: $stockCode")

                        stockNameMain = Trade.getStockNameByCode(stockCode)
                        // Your operations here
                        it.dontExist(stockCode)
                        it.dontExist(stockNameMain)
                    }


                }
            }
        }

    }
}