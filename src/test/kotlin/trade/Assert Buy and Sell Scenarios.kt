package trade

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import macro.Onboarding.getRandomBoolean
import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.assertOrder
import macro.Trade.calculateCeilingPrice
import macro.Trade.calculateFloorPrice
import macro.Trade.checkOrdersTab
import macro.Trade.feesComputation
import macro.Trade.getBoardLot
import macro.Trade.getClosingPrice
import macro.Trade.getColumnInRow
import macro.Trade.getCurrentDateFormatted
import macro.Trade.getLastPrice
import macro.Trade.getQuantity
import macro.Trade.getRandomCutlossPrice
import macro.Trade.getRandomDesiredPrice
import macro.Trade.getRandomOrderType
import macro.Trade.totalAmountComputation
import macro.Trade.viewOrderDetails
import macro.Wallet.getWalletBalance
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.io.File
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Assert Buy and Sell Scenarios`  : UITest() {

    private lateinit var email:String
    private lateinit var pword:String
    private var pin = "111500"
    private val useUpdate = false
    private val broker = "1"
    private val hasStocks = false
    private var firstName = "Tradetest"

    //Buy Scenarios
    @Test
    @DisplayName("Buy Scenarios")
    @Order(1)
    fun assertBuyScenarios() {
        scenario {
            //Missing @
            case(1) {
                expectation {
                    //Get Current Trading Hour
                    var currentTradingHour = Trade.getCurrenTradingHour()

                    //it.macro("[Log Out]")

                    email = "tradetestqa+superusernew@gmail.com"
                    pword = "password123$"
                    pin = "123123"

                    //it.macro("[Install Code Push First]", email, pword, pin)
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    //it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                    val walletBalance = getWalletBalance()

                    describe("Wait for Home Page")
                        .waitForDisplay("Market: $currentTradingHour", waitSeconds = 30.0)
                    it.exist("Market: $currentTradingHour")

                    val numberOfIterations = 9 // You can set this to any number
                    var previousTotal = 0.00

                    // Path to your CSV file created in Python
                    val csvFile = File("src/test/resources/reference_number_buy.csv")

                    // Read CSV file
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Print header
                    println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

                    // Print data rows
                    csvData.drop(1).forEachIndexed { index, row ->
                        println("Row ${index + 1}: ${row.joinToString(", ")}")
                    }

                    //Posted Order

                    var endOfDayCSV1 = getColumnInRow(csvData, 1, 5) ?: ""
                    var endOfDay1 = endOfDayCSV1 == "true"

                    val isBuyCSV1 = getColumnInRow(csvData, 1, 1) ?: ""
                    var isBuy1 = isBuyCSV1 == "true"

                    var endOfDayCSV2 = getColumnInRow(csvData, 2, 5) ?: ""
                    var endOfDay2 = endOfDayCSV2 == "true"

                    val isBuyCSV2 = getColumnInRow(csvData, 2, 1) ?: ""
                    var isBuy2 = isBuyCSV2 == "true"

                    var endOfDayCSV3 = getColumnInRow(csvData, 3, 5) ?: ""
                    var endOfDay3 = endOfDayCSV3 == "true"

                    val isBuyCSV3 = getColumnInRow(csvData, 3, 1) ?: ""
                    var isBuy3 = isBuyCSV3 == "true"

                    var endOfDayCSV4 = getColumnInRow(csvData, 4, 5) ?: ""
                    var endOfDay4 = endOfDayCSV4 == "true"

                    val isBuyCSV4 = getColumnInRow(csvData, 4, 1) ?: ""
                    var isBuy4 = isBuyCSV4 == "true"

                    var endOfDayCSV5 = getColumnInRow(csvData, 5, 5) ?: ""
                    var endOfDay5 = endOfDayCSV5 == "true"

                    val isBuyCSV5 = getColumnInRow(csvData, 5, 1) ?: ""
                    var isBuy5 = isBuyCSV5 == "true"

                    var endOfDayCSV6 = getColumnInRow(csvData, 6, 5) ?: ""
                    var endOfDay6 = endOfDayCSV6 == "true"

                    val isBuyCSV6 = getColumnInRow(csvData, 6, 1) ?: ""
                    var isBuy6 = isBuyCSV6 == "true"

                    var endOfDayCSV7 = getColumnInRow(csvData, 7, 5) ?: ""
                    var endOfDay7 = endOfDayCSV7 == "true"

                    val isBuyCSV7 = getColumnInRow(csvData, 7, 1) ?: ""
                    var isBuy7 = isBuyCSV7 == "true"

                    var endOfDayCSV8 = getColumnInRow(csvData, 8, 5) ?: ""
                    var endOfDay8 = endOfDayCSV8 == "true"

                    val isBuyCSV8 = getColumnInRow(csvData, 8, 1) ?: ""
                    var isBuy8 = isBuyCSV8 == "true"

                    var endOfDayCSV9 = getColumnInRow(csvData, 9, 5) ?: ""
                    var endOfDay9 = endOfDayCSV9 == "true"

                    val isBuyCSV9 = getColumnInRow(csvData, 9, 1) ?: ""
                    var isBuy9 = isBuyCSV9 == "true"

                    val willAddNotesCSV1 = getColumnInRow(csvData, 1, 14) ?: ""
                    var willAddNotes1 = willAddNotesCSV1 == "true"

                    val willAddNotesCSV2 = getColumnInRow(csvData, 2, 14) ?: ""
                    var willAddNotes2 = willAddNotesCSV2 == "true"

                    val willAddNotesCSV3 = getColumnInRow(csvData, 3, 14) ?: ""
                    var willAddNotes3 = willAddNotesCSV3 == "true"

                    val willAddNotesCSV4 = getColumnInRow(csvData, 4, 14) ?: ""
                    var willAddNotes4 = willAddNotesCSV4 == "true"

                    val willAddNotesCSV5 = getColumnInRow(csvData, 5, 14) ?: ""
                    var willAddNotes5 = willAddNotesCSV5 == "true"

                    val willAddNotesCSV6 = getColumnInRow(csvData, 6, 14) ?: ""
                    var willAddNotes6 = willAddNotesCSV6 == "true"

                    val willAddNotesCSV7 = getColumnInRow(csvData, 7, 14) ?: ""
                    var willAddNotes7 = willAddNotesCSV7 == "true"

                    val willAddNotesCSV8 = getColumnInRow(csvData, 8, 14) ?: ""
                    var willAddNotes8 = willAddNotesCSV8 == "true"

                    val willAddNotesCSV9 = getColumnInRow(csvData, 9, 14) ?: ""
                    var willAddNotes9 = willAddNotesCSV9 == "true"

                    val stock_code1 = getColumnInRow(csvData, 1, 3) ?: ""
                    val desiredQuantity1 = getColumnInRow(csvData, 1, 8) ?: ""
                    val totalPrice1 = getColumnInRow(csvData, 1, 10) ?: ""
                    val referenceNumber1 = getColumnInRow(csvData, 1, 0) ?: ""
                    val orderType1 = getColumnInRow(csvData, 1, 0) ?: ""
                    val desiredPrice1 = getColumnInRow(csvData, 1, 7) ?: ""
                    val diff_fees1 = getColumnInRow(csvData, 1, 13) ?: ""
                    val message1 = getColumnInRow(csvData, 1, 15) ?: ""
                    val subTotal1 = getColumnInRow(csvData, 1, 11) ?: ""
                    val fees1 = getColumnInRow(csvData, 1, 12) ?: ""
                    val executedPrice1 = getColumnInRow(csvData, 1, 17) ?: ""
                    val executedQuantity1 = getColumnInRow(csvData, 1, 18) ?: ""

                    val stock_code2 = getColumnInRow(csvData, 2, 3) ?: ""
                    val desiredQuantity2 = getColumnInRow(csvData, 2, 8) ?: ""
                    val totalPrice2 = getColumnInRow(csvData, 2, 10) ?: ""
                    val referenceNumber2 = getColumnInRow(csvData, 2, 0) ?: ""
                    val orderType2 = getColumnInRow(csvData, 2, 0) ?: ""
                    val desiredPrice2 = getColumnInRow(csvData, 2, 7) ?: ""
                    val diff_fees2 = getColumnInRow(csvData, 2, 13) ?: ""

                    val stock_code3 = getColumnInRow(csvData, 3, 3) ?: ""
                    val desiredQuantity3 = getColumnInRow(csvData, 3, 8) ?: ""
                    val totalPrice3 = getColumnInRow(csvData, 3, 10) ?: ""
                    val referenceNumber3 = getColumnInRow(csvData, 3, 0) ?: ""
                    val orderType3 = getColumnInRow(csvData, 3, 0) ?: ""
                    val desiredPrice3 = getColumnInRow(csvData, 3, 7) ?: ""
                    val diff_fees3 = getColumnInRow(csvData, 3, 13) ?: ""

                    val stock_code4 = getColumnInRow(csvData, 4, 3) ?: ""
                    val desiredQuantity4 = getColumnInRow(csvData, 4, 8) ?: ""
                    val totalPrice4 = getColumnInRow(csvData, 4, 10) ?: ""
                    val referenceNumber4 = getColumnInRow(csvData, 4, 0) ?: ""
                    val orderType4 = getColumnInRow(csvData, 4, 0) ?: ""
                    val desiredPrice4 = getColumnInRow(csvData, 4, 7) ?: ""
                    val diff_fees4 = getColumnInRow(csvData, 4, 13) ?: ""

                    val stock_code5 = getColumnInRow(csvData, 5, 3) ?: ""
                    val desiredQuantity5 = getColumnInRow(csvData, 5, 8) ?: ""
                    val totalPrice5 = getColumnInRow(csvData, 5, 10) ?: ""
                    val referenceNumber5 = getColumnInRow(csvData, 5, 0) ?: ""
                    val orderType5 = getColumnInRow(csvData, 5, 0) ?: ""
                    val desiredPrice5 = getColumnInRow(csvData, 5, 7) ?: ""
                    val diff_fees5 = getColumnInRow(csvData, 5, 13) ?: ""

                    val stock_code6 = getColumnInRow(csvData, 6, 3) ?: ""
                    val desiredQuantity6 = getColumnInRow(csvData, 6, 8) ?: ""
                    val totalPrice6 = getColumnInRow(csvData, 6, 10) ?: ""
                    val referenceNumber6 = getColumnInRow(csvData, 6, 0) ?: ""
                    val orderType6 = getColumnInRow(csvData, 6, 0) ?: ""
                    val desiredPrice6 = getColumnInRow(csvData, 6, 7) ?: ""
                    val diff_fees6 = getColumnInRow(csvData, 6, 13) ?: ""

                    val stock_code7 = getColumnInRow(csvData, 7, 3) ?: ""
                    val desiredQuantity7 = getColumnInRow(csvData, 7, 8) ?: ""
                    val totalPrice7 = getColumnInRow(csvData, 7, 10) ?: ""
                    val referenceNumber7 = getColumnInRow(csvData, 7, 0) ?: ""
                    val orderType7 = getColumnInRow(csvData, 7, 0) ?: ""
                    val desiredPrice7 = getColumnInRow(csvData, 7, 7) ?: ""
                    val diff_fees7 = getColumnInRow(csvData, 7, 13) ?: ""

                    val stock_code8 = getColumnInRow(csvData, 8, 3) ?: ""
                    val desiredQuantity8 = getColumnInRow(csvData, 8, 8) ?: ""
                    val totalPrice8 = getColumnInRow(csvData, 8, 10) ?: ""
                    val referenceNumber8 = getColumnInRow(csvData, 8, 0) ?: ""
                    val orderType8 = getColumnInRow(csvData, 8, 0) ?: ""
                    val desiredPrice8 = getColumnInRow(csvData, 8, 7) ?: ""
                    val diff_fees8 = getColumnInRow(csvData, 8, 13) ?: ""

                    val stock_code9 = getColumnInRow(csvData, 9, 3) ?: ""
                    val desiredQuantity9 = getColumnInRow(csvData, 9, 8) ?: ""
                    val totalPrice9 = getColumnInRow(csvData, 9, 10) ?: ""
                    val referenceNumber9 = getColumnInRow(csvData, 9, 0) ?: ""
                    val orderType9 = getColumnInRow(csvData, 9, 0) ?: ""
                    val desiredPrice9 = getColumnInRow(csvData, 9, 7) ?: ""
                    val diff_fees9 = getColumnInRow(csvData, 9, 13) ?: ""

                    val message2 = getColumnInRow(csvData, 2, 15) ?: ""
                    val subTotal2 = getColumnInRow(csvData, 2, 11) ?: ""
                    val fees2 = getColumnInRow(csvData, 2, 12) ?: ""

                    val message3 = getColumnInRow(csvData, 3, 15) ?: ""
                    val subTotal3 = getColumnInRow(csvData, 3, 11) ?: ""
                    val fees3 = getColumnInRow(csvData, 3, 12) ?: ""

                    val message4 = getColumnInRow(csvData, 4, 15) ?: ""
                    val subTotal4 = getColumnInRow(csvData, 4, 11) ?: ""
                    val fees4 = getColumnInRow(csvData, 4, 12) ?: ""

                    val message5 = getColumnInRow(csvData, 5, 15) ?: ""
                    val subTotal5 = getColumnInRow(csvData, 5, 11) ?: ""
                    val fees5 = getColumnInRow(csvData, 5, 12) ?: ""

                    val message6 = getColumnInRow(csvData, 6, 15) ?: ""
                    val subTotal6 = getColumnInRow(csvData, 6, 11) ?: ""
                    val fees6 = getColumnInRow(csvData, 6, 12) ?: ""

                    val message7 = getColumnInRow(csvData, 7, 15) ?: ""
                    val subTotal7 = getColumnInRow(csvData, 7, 11) ?: ""
                    val fees7 = getColumnInRow(csvData, 7, 12) ?: ""

                    val message8 = getColumnInRow(csvData, 8, 15) ?: ""
                    val subTotal8 = getColumnInRow(csvData, 8, 11) ?: ""
                    val fees8 = getColumnInRow(csvData, 8, 12) ?: ""

                    val message9 = getColumnInRow(csvData, 9, 15) ?: ""
                    val subTotal9 = getColumnInRow(csvData, 9, 11) ?: ""
                    val fees9 = getColumnInRow(csvData, 9, 12) ?: ""

                    val executedPrice2 = getColumnInRow(csvData, 2, 17) ?: ""
                    val executedQuantity2 = getColumnInRow(csvData, 2, 18) ?: ""

                    val executedPrice3 = getColumnInRow(csvData, 3, 17) ?: ""
                    val executedQuantity3 = getColumnInRow(csvData, 3, 18) ?: ""

                    val executedPrice4 = getColumnInRow(csvData, 4, 17) ?: ""
                    val executedQuantity4 = getColumnInRow(csvData, 4, 18) ?: ""

                    val executedPrice5 = getColumnInRow(csvData, 5, 17) ?: ""
                    val executedQuantity5 = getColumnInRow(csvData, 5, 18) ?: ""

                    val executedPrice6 = getColumnInRow(csvData, 6, 17) ?: ""
                    val executedQuantity6 = getColumnInRow(csvData, 6, 18) ?: ""

                    val executedPrice7 = getColumnInRow(csvData, 7, 17) ?: ""
                    val executedQuantity7 = getColumnInRow(csvData, 7, 18) ?: ""

                    val executedPrice8 = getColumnInRow(csvData, 8, 17) ?: ""
                    val executedQuantity8 = getColumnInRow(csvData, 8, 18) ?: ""

                    val executedPrice9 = getColumnInRow(csvData, 9, 17) ?: ""
                    val executedQuantity9 = getColumnInRow(csvData, 9, 18) ?: ""


                    // Row 2
                    val brokerCommission2 = getColumnInRow(csvData, 2, 19) ?: ""
                    val brokerVAT2 = getColumnInRow(csvData, 2, 20) ?: ""
                    val PSEandSECFee2 = getColumnInRow(csvData, 2, 21) ?: ""
                    val SCCPValue2 = getColumnInRow(csvData, 2, 22) ?: ""

                    // Row 3
                    val brokerCommission3 = getColumnInRow(csvData, 3, 19) ?: ""
                    val brokerVAT3 = getColumnInRow(csvData, 3, 20) ?: ""
                    val PSEandSECFee3 = getColumnInRow(csvData, 3, 21) ?: ""
                    val SCCPValue3 = getColumnInRow(csvData, 3, 22) ?: ""


                    it.tap("Portfolio")

                    it.tap("Orders")

                    var firstTap = true

                    assertOrder("Posted", endOfDay1, isBuy1, stock_code1, desiredQuantity1, totalPrice1, referenceNumber1, orderType1, desiredPrice = desiredPrice1, diff_fees = diff_fees1, willAddNotes = willAddNotes1, noteMessage = message1, subTotal = subTotal1, fees = fees1, firstTap=firstTap, broker = broker )

                    firstTap = false

                    assertOrder("Partial", endOfDay2, isBuy2, stock_code2, desiredQuantity2, totalPrice2, referenceNumber2, orderType2, desiredPrice = desiredPrice2, diff_fees = diff_fees2, willAddNotes = willAddNotes1, noteMessage = message2, subTotal = subTotal2, fees = fees2, executedPrice = executedPrice2, executedQuantity = executedQuantity2, firstTap=firstTap, broker = broker, brokerCommission = brokerCommission2, brokerVAT = brokerVAT2, PSEandSECFee = PSEandSECFee2, SCCPVAlue = SCCPValue2  )

                    assertOrder("Filled", endOfDay3, isBuy3, stock_code3, desiredQuantity3, totalPrice3, referenceNumber3, orderType3, desiredPrice = desiredPrice3, diff_fees = diff_fees3, willAddNotes = willAddNotes1, noteMessage = message3, subTotal = subTotal3, fees = fees3 , executedPrice = executedPrice3, executedQuantity = executedQuantity3, firstTap=firstTap , broker = broker , brokerCommission = brokerCommission3, brokerVAT = brokerVAT3, PSEandSECFee = PSEandSECFee3, SCCPVAlue = SCCPValue3 )

                    assertOrder("Cancelled", endOfDay4, isBuy4, stock_code4, desiredQuantity4, totalPrice4, referenceNumber4, orderType4, desiredPrice = desiredPrice4, diff_fees = diff_fees4, willAddNotes = willAddNotes1, noteMessage = message4, subTotal = subTotal4, fees = fees4, firstTap=firstTap , broker = broker )

                    assertOrder("Rejected", endOfDay5, isBuy5, stock_code5, desiredQuantity5, totalPrice5, referenceNumber5, orderType5, desiredPrice = desiredPrice5, diff_fees = diff_fees5, willAddNotes = willAddNotes1, noteMessage = message5, subTotal = subTotal5, fees = fees5 ,firstTap=firstTap , broker = broker )

                    assertOrder("Expired", endOfDay6, isBuy6, stock_code6, desiredQuantity6, totalPrice6, referenceNumber6, orderType6, desiredPrice = desiredPrice6, diff_fees = diff_fees6, willAddNotes = willAddNotes1, noteMessage = message6, subTotal = subTotal6, fees = fees6 , firstTap=firstTap , broker = broker )

                    assertOrder("Unplaced", endOfDay7, isBuy7, stock_code7, desiredQuantity7, totalPrice7, referenceNumber7, orderType7, desiredPrice = desiredPrice7, diff_fees = diff_fees7, willAddNotes = willAddNotes1, noteMessage = message7, subTotal = subTotal7, fees = fees7 , firstTap=firstTap , broker = broker )

                    assertOrder("Rejected Cancellation", endOfDay8, isBuy8, stock_code8, desiredQuantity8, totalPrice8, referenceNumber8, orderType8, desiredPrice = desiredPrice8, diff_fees = diff_fees8, willAddNotes = willAddNotes1, noteMessage = message8, subTotal = subTotal8, fees = fees8 , firstTap=firstTap , broker = broker )

                    assertOrder("Error", endOfDay9, isBuy9, stock_code9, desiredQuantity9, totalPrice9, referenceNumber9, orderType9, desiredPrice = desiredPrice9, diff_fees = diff_fees9, willAddNotes = willAddNotes1, noteMessage = message9, subTotal = subTotal9, fees = fees9 , firstTap=firstTap, broker = broker )

                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }

    //Sell Scenarios

    @Test
    @DisplayName("Sell Scenarios")
    @Order(2)
    fun assertSellScenarios() {
        scenario {
            //Missing @
            case(1) {
                expectation {


                    email = "thetestdummytest+tradeseller@gmail.com"
                    pword = "123123"
                    pin = "123123"

                    //it.macro("[Install Code Push First]", email, pword, pin)

                    //it.macro("[Log Out]")
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    //it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    val walletBalance = getWalletBalance()

                    //Get Current Trading Hour
                    var currentTradingHour = Trade.getCurrenTradingHour()

                    describe("Wait for Home Page")
                        .waitForDisplay("Market: $currentTradingHour", waitSeconds = 30.0)
                    it.exist("Market: $currentTradingHour")

                    val numberOfIterations = 9 // You can set this to any number
                    var previousTotal = 0.00

                    for (count in 1..numberOfIterations) {


                        val stockCode = getUniqueStock()
                        val stockName = Trade.getStockNameByCode(stockCode)

                        println("Stock Code: $stockCode")
                        println("----------------------")
                        println("Previous Total: $previousTotal")

                        it.macro("[Search Stock]", stockCode, stockName)

                        val lastPrice = getLastPrice(stockCode)
                        val closingPrice = getClosingPrice()

                        currentTradingHour = Trade.getCurrenTradingHour()
                        val isBuy = false
                        val orderType = getRandomOrderType()

                        val (desiredPrice, tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                        val (cutlossPrice, cutLossTickSize) = getRandomCutlossPrice(desiredPrice, currentTradingHour, lastPrice)
                        val minQuantity = getBoardLot(lastPrice.toDouble()).toString()

                        val currentTotal = totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
                        val desiredQuantity = getQuantity(desiredPrice, minQuantity, currentTotal, orderType)


                        val desiredPesos = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                        val useChart = getRandomBoolean()
                        val buyInShares = true

                        val usePortAllocate = false

                        //Ceiling and Floor Price
                        val testCeilingAndFloorPrice = false
                        ///val closingPrice = getLatestLastPrice(stockCode).toString()
                        val ceilingPrice = calculateCeilingPrice(closingPrice)
                        val floorPrice = calculateFloorPrice(closingPrice)

                        val useChartTargetPrice = getRandomBoolean()
                        val useEqualConditionTargetPrice = getRandomBoolean()
                        val useChartCutlossPrice:Boolean = getRandomBoolean()
                        val useEqualConditionCutlossPrice = getRandomBoolean()

                        it.macro(
                            "[Sell Shares]",
                            stockCode,
                            lastPrice,
                            desiredPrice,
                            minQuantity,
                            desiredQuantity,
                            orderType,
                            useChart,
                            buyInShares,
                            desiredPesos,
                            usePortAllocate,
                            walletBalance,
                            testCeilingAndFloorPrice,
                            ceilingPrice,
                            floorPrice,
                            broker,
                            isBuy,
                            currentTradingHour,
                            previousTotal,
                            cutlossPrice,
                            useChartTargetPrice,
                            useEqualConditionTargetPrice,
                            useChartCutlossPrice,
                            useEqualConditionCutlossPrice,
                        )


//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Order is invalid. Amount must be less than or equal Available Cash.", waitSeconds = 30.0)

                        it.exist("Sell Order")
                        it.dontExist("Order Summary")

                        val isEndOfDay = getRandomBoolean()
                        val willAddNotes = getRandomBoolean()
                        val noteMessage =
                            "Selling $stockCode with $desiredQuantity shares @ $desiredPrice $orderType Price"
                        val viewAvailableCash = getRandomBoolean()

                        val amountWithFees = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                        val cutlossAmountWithFees = totalAmountComputation(cutlossPrice, desiredQuantity, broker, isBuy)
                        var result : Map<String, Any> = emptyMap()
                        result = feesComputation(lastPrice, minQuantity, broker, isBuy)

                        var fees = ""
                        if(isBuy){
                            fees = (result["Buy Charges"] as? Double ?: 0.0).toString()
                        } else {
                            fees = (result["Sell Charges"] as? Double ?: 0.0).toString()
                        }
                        val subTotal = (amountWithFees.toDouble() + fees.toDouble()).toString()
                        var cutLossFees = ""
                        if(isBuy){
                            cutLossFees = (result["Buy Charges"] as? Double ?: 0.0).toString()
                        } else {
                            cutLossFees = (result["Sell Charges"] as? Double ?: 0.0).toString()
                        }


                        val cutLossSubTotal =  (cutlossAmountWithFees.toDouble() + cutLossFees.toDouble()).toString()

                        if (viewAvailableCash) {
                            it.macro("[View Available Cash]")
                            if(isBuy){
                                it.exist("Buy Order")
                            } else {
                                it.exist("Sell Order")
                            }

                            it.exist("Preview Order")
                        }

                        it.macro(
                            "[Preview Sell Order]",
                            lastPrice,
                            desiredPrice,
                            desiredQuantity,
                            minQuantity,
                            amountWithFees,
                            fees,
                            subTotal,
                            walletBalance,
                            orderType,
                            isEndOfDay,
                            willAddNotes,
                            noteMessage,
                            broker,
                            isBuy,
                            previousTotal,
                            currentTradingHour,
                            stockCode,
                            cutlossPrice,
                            cutLossSubTotal,
                            cutLossFees,
                            cutlossAmountWithFees,

                            )
                        it.macro("[Enter Trading Pin Trade]", pin)

                        describe("Wait for Page to Load")
                            .waitForDisplay("Order Placed", waitSeconds = 30.0)

                        it.exist("To cancel your order, go to Portfolio > Orders")

                        it.exist(stockCode)
                        it.exist("Bid And Ask")

                        var expectedStatus = "Pending"

                        var orderStatus = ""
                        if (currentTradingHour != "Close") {
                            orderStatus = "Rejected"
                            expectedStatus = "Cancelled"
                        } else if (currentTradingHour == "Close") {
                            orderStatus = "Pending"
                            expectedStatus = "Pending"
                        }

                        currentTradingHour = Trade.getCurrenTradingHour()

                        val (diff_fees, diff_cutloss_fees) = checkOrdersTab(
                            currentTradingHour,
                            stockCode,
                            isBuy,
                            orderType,
                            desiredQuantity,
                            isEndOfDay,
                            desiredPesos,
                            desiredPrice,
                            count,
                            desiredPesos,
                            orderStatus,
                            willAddNotes,
                            noteMessage,
                            subTotal,
                            fees,
                            currentTradingHour,
                            walletBalance,
                            previousTotal,
                            cutlossPrice,
                            cutlossAmountWithFees
                        )

                        //Will be used as reference later
                        previousTotal += (desiredPesos.toDouble()+diff_fees.toDouble())


                        val referenceNumber = viewOrderDetails(
                            desiredPesos,
                            isBuy,
                            stockCode,
                            isEndOfDay,
                            orderStatus,
                            willAddNotes,
                            noteMessage,
                            desiredPrice,
                            desiredQuantity,
                            subTotal,
                            fees,
                            diff_fees,
                            diff_cutloss_fees,
                        )

                        val output_directory = "C:/Users/geopi/Desktop/TradeSB/tests/test_files/reference_number_sell.csv"
                        val internal_output_directory = "src/test/resources/reference_number_sell.csv"
                        val file = File(output_directory)
                        val file2 = File(internal_output_directory)

                        if(count == 1){

                            if (file.exists()) {
                                file.delete()
                                file2.delete()
                                println("File deleted: $output_directory")
                                println("File deleted: $internal_output_directory")
                            }

                            // Recreate the file with the header
                            file.writeText("Reference Number,IsBuy,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,Subtotal,FeesWithComma,WillAddNotes,NoteMessage,Expected Status\n")
                            file2.writeText("Reference Number,IsBuy,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,Subtotal,FeesWithComma,WillAddNotes,NoteMessage,Expected Status\n")
                        }

                        val currentDate = getCurrentDateFormatted()
                        val formattedDate = "\"$currentDate\""
                        // Append the reference number to the CSV file
                        file.appendText("$referenceNumber,$isBuy,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$subTotal,$fees,$willAddNotes,$noteMessage,$expectedStatus\n")
                        file2.appendText("$referenceNumber,$isBuy,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$subTotal,$fees,$willAddNotes,$noteMessage,$expectedStatus\n")

                        println("Iteration: $count, Reference Number: $referenceNumber")



                    }

                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }

}