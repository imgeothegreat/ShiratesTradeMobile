package trade

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import macro.Common.decodeFromBase64
import macro.Portfolio
import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.formatNumberWithCommaPeriod
import macro.Trade.processCsvFile
import macro.Trade.processReitsFile
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.io.File
import kotlin.test.Test
import java.io.FileReader

@Testrun("testConfig/android/androidSettings/testrunProd.properties")
class `Invest Category` : UITest() {

    private lateinit var email:String
    private lateinit var pword:String
    private var pin = "111500"
    private val useUpdate = false
    private val broker = "1"
    private val hasStocks = false
    private var firstName = "Geo Pineda"


    @Test
    @DisplayName("Test Invest Categories and Filters")
    @Order(1)
    fun testCategoriesFilters() {

        scenario {

            case(1) {
                expectation {
                    //Get Current Trading Hour
                    val csvFile =  File("C:/Users/geopi/Documents/secret.csv")

                    // Open the CSV file and read the data
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Skip the header row and process the data
                    csvData.drop(1).firstOrNull()?.let { row ->
                        email = row[0]
                        pword = row[1]
                        println(email)
                        println(pword)

                    }

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    it.tap("Invest")

                    it.exist("All")
                    it.exist("Bluechips")
                    it.exist("REITs")
                    it.exist("Dividend Stocks")

                    it.exist("A-Z")
                    it.exist("Change%")
                    it.exist("Volume")

                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

                    //All Stocks Descending Volume
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processCsvFile("src/test/resources/all_stocks_descending_volume.csv")

                    //All Stocks Ascending Volume
                    it.tap("Volume")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processCsvFile("src/test/resources/all_stocks_ascending_volume.csv")

                    //All Stocks Ascending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processCsvFile("src/test/resources/all_stocks_ascending_change.csv")

                    //All Stocks Descending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processCsvFile("src/test/resources/all_stocks_descending_change.csv")

                    //All Stocks Descending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processCsvFile("src/test/resources/all_stocks_descending_name.csv")

                    //All Stocks Ascending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processCsvFile("src/test/resources/all_stocks_ascending_name.csv")

                    // Bluechips Descending Change
                    it.tap("Bluechips")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processCsvFile("src/test/resources/bluechips_descending_change.csv")

                    // Bluechips Ascending Change
                    it.tap("Change%")
                    //it.exist("\uE924") // dot
                    //it.exist("\uE930") // up sign
                    processCsvFile("src/test/resources/bluechips_ascending_change.csv")

                    // Bluechips Descending Volume
                    it.tap("Volume")
                    //it.exist("\uE924") // dot
                    //it.exist("\uE931") // down sign
                    processCsvFile("src/test/resources/bluechips_descending_volume.csv")


                    // Bluechips Ascending Volume
                    it.tap("Volume")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processCsvFile("src/test/resources/bluechips_ascending_volume.csv")

                    //Bluechips Descending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processCsvFile("src/test/resources/bluechips_descending_name.csv")


                    //Bluechips Ascending Name
                    it.tap("A-Z")
                    //it.exist("\uE924") // dot
                    //it.exist("\uE930") // up sign
                    processCsvFile("src/test/resources/bluechips_ascending_name.csv")


                    it.tap("REITs")
                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

                    //REITS Ascending Div Yield

                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/reits_ascending_yield.csv")

                    //REITS Descending Div Yield
                    it.tap("Div Yield")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/reits_descending_yield.csv")


                    //REITS Ascending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/reits_ascending_change.csv")

                    //REITS Descending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/reits_descending_change.csv")

                    //REITS Ascending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/reits_ascending_name.csv")

                    //REITS Descending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/reits_descending_name.csv")


                    //Divs Descending Div Yield
                    it.tap("Dividend Stocks")
                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/divs_descending_yield.csv")

                    //Divs Ascending Div Yield
                    it.tap("Div Yield")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/divs_ascending_yield.csv")

                    //Divs Descending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/divs_descending_change.csv")

                    //Divs Ascending Change
                    it.tap("Change%")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/divs_ascending_change.csv")

                    //Divs Descending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    //down sign
                    //it.exist("\uE931")
                    processReitsFile("src/test/resources/divs_descending_name.csv")

                    //Divs Ascending Name
                    it.tap("A-Z")
                    //dot
                    //it.exist("\uE924")
                    // up sign
                    //it.exist("\uE930")
                    processReitsFile("src/test/resources/divs_ascending_name.csv")


                    //All

                    it.tap("All")
                    it.exist("Asset")
                    it.exist("3 mos")
                    it.exist("Price")

                    it.tap("A-Z")
                    it.tap("Change%")
                    it.tap("Volume")


                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    @Test
    @DisplayName("Test Search and Notif Invest Page")
    @Order(2)
    fun testSearchAndNotif(){

//        it.macro("[Login]", email, pword)
//        it.macro("[Select Broker]", broker)
//        it.macro("[Enter Trading Pin]", pin, useUpdate)
        it.macro("[Lock Trading Pin]", pin, firstName)

        describe("Wait for Home Page")
            .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

        //Search Stock
        //Tap Search
        val randomStockCodeMain = getUniqueStock()
        val stockNameMain = Trade.getStockNameByCode(randomStockCodeMain)

        it.macro("[Search Stock]", randomStockCodeMain, stockNameMain)
        it.macro("[Explore Asset Page]", randomStockCodeMain, hasStocks)
        it.macro("[View Full Chart]", randomStockCodeMain, stockNameMain)

        //Inside Search
        val randomStockCodeMainInside = getUniqueStock()
        val stockNameMainInside = Trade.getStockNameByCode(randomStockCodeMainInside)

        it.macro("[Search Stock]", randomStockCodeMainInside, stockNameMainInside)
        it.macro("[Explore Asset Page]", randomStockCodeMainInside, hasStocks)
        it.macro("[View Full Chart]", randomStockCodeMainInside, stockNameMainInside)
        it.macro("[Save to Watcher]", randomStockCodeMainInside, stockNameMainInside)

        //Notification
        it.macro("[Explore Notifications]", useUpdate)
    }
}