package trade

import macro.Onboarding.getRandomBoolean
import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.calculateCeilingPrice
import macro.Trade.calculateFloorPrice
import macro.Trade.checkOrdersTab
import macro.Trade.feesComputation
import macro.Trade.getBoardLot
import macro.Trade.getClosingPrice
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
import shirates.core.macro.Macro
import shirates.core.testcode.UITest
import java.io.File
import kotlin.math.round
import kotlin.random.Random
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class `Buy and Sell Scenarios` : UITest() {

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
    fun testBuyScenarios() {
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

                    for (count in 1..numberOfIterations) {

                        currentTradingHour = Trade.getCurrenTradingHour()


                        val stockCode = getUniqueStock()
                        val stockName = Trade.getStockNameByCode(stockCode)

                        println("Stock Code: $stockCode")
                        println("----------------------")
                        println("Previous Total: $previousTotal")

                        it.macro("[Search Stock]", stockCode, stockName)

                        val lastPrice = getLastPrice(stockCode)
                        val closingPrice = getClosingPrice()
                        val isBuy = true
                        val orderType = "Limit"
                        val (desiredPrice, tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
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

                        it.macro(
                            "[Buy Shares]",
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
                            previousTotal
                        )


//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Order is invalid. Amount must be less than or equal Available Cash.", waitSeconds = 30.0)

                        it.exist("Buy Order")
                        it.dontExist("Order Summary")

                        val isEndOfDay = getRandomBoolean()
                        val willAddNotes = getRandomBoolean()
                        val noteMessage =
                            "Buying $stockCode with $desiredQuantity shares @ $desiredPrice $orderType Price"
                        val viewAvailableCash = getRandomBoolean()

                        val amountWithFees = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                        var result : Map<String, Any> = emptyMap()
                        result = feesComputation(desiredPrice, desiredQuantity, broker, isBuy)

                        println("Results")
                        result.forEach { (key, value) ->
                            println("$key: $value")
                        }

                        var fees = ""
                        if(isBuy){
                            fees = (result["Buy Charges"] as? String ?: "0.0")
                        } else {
                            fees = (result["Sell Charges"] as? String ?: "0.0")
                        }
                        val subTotal = (amountWithFees.toDouble() - fees.toDouble()).toString()

                        println("Subtotal: $subTotal")
                        println("Fees: $fees")
                        println("Amount with Fees: $amountWithFees")

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
                            "[Preview Order]",
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
                            stockCode
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
                            diff_cutloss_fees
                        )

                        val output_directory = "C:/Users/geopi/Desktop/TradeSB/tests/test_files/reference_number_buy.csv"
                        val internal_output_directory = "src/test/resources/reference_number_buy.csv"
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
                            file.writeText("Reference Number,IsBuy,OrderType,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,TotalPrice,Subtotal,FeesWithComma,DIFF Fees,WillAddNotes,NoteMessage,Expected Status,Executed Price, Executed Quantity,Broker Commission,Broker VAT, PSE and SEC Fee, SCCP Fee\n")
                            file2.writeText("Reference Number,IsBuy,OrderType,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,TotalPrice,Subtotal,FeesWithComma,DIFF Fees,WillAddNotes,NoteMessage,Expected Status,Executed Price, Executed Quantity\n")
                        }

                        val currentDate = getCurrentDateFormatted()
                        val formattedDate = "\"$currentDate\""
                        // Append the reference number to the CSV file

                        var executedPrice = ""
                        var executedQuantity = ""

                        var brokerCommissionValue = ""
                        var VATValue = ""
                        var PSEandSECFee = ""
                        var SCCPValue = ""

                        if(count == 2 || count == 3){
                            // Convert the input string to a double
                            val inputNumber = desiredPrice.toDouble()

                            // Generate a random double between 0.71 and 1.0
                            val randomPercentage = Random.nextDouble(0.71, 1.0)

                            // Calculate the result and round to two decimal places
                            executedPrice = (round(inputNumber * randomPercentage * 100) / 100).toString()

                            val quantityInput = desiredQuantity.toInt()
                            val minQuantityInput = minQuantity.toInt()

                            // Calculate the possible maximum deduction that keeps the remaining quantity non-zero
                            val maxDeductible = quantityInput - minQuantityInput

                            // Generate a random deduction that is a multiple of the minimum quantity
                            val randomDeduction = if (maxDeductible >= minQuantityInput) {
                                Random.nextInt(0, maxDeductible / minQuantityInput + 1) * minQuantityInput
                            } else {
                                0
                            }

                            executedQuantity = (quantityInput - randomDeduction).toString()
                            brokerCommissionValue = (result["Broker Commission"] as? Double ?: 0.0).toString()
                            VATValue = (result["Broker VAT"] as? Double ?: 0.0).toString()
                            PSEandSECFee = (result["PSE & SEC Fee"] as? Double ?: 0.0).toString()
                            SCCPValue =  (result["SCCP Fee"] as? Double ?: 0.0).toString()

                        }





                        file.appendText("$referenceNumber,$isBuy,$orderType,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$amountWithFees,$subTotal,$fees,$diff_fees,$willAddNotes,$noteMessage,$expectedStatus,$executedPrice,$executedQuantity, $brokerCommissionValue, $VATValue, $PSEandSECFee, $SCCPValue\n")
                        file2.appendText("$referenceNumber,$isBuy,$orderType,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$amountWithFees,$subTotal,$fees,$diff_fees,$willAddNotes,$noteMessage,$expectedStatus,$executedPrice,$executedQuantity, $brokerCommissionValue, $VATValue, $PSEandSECFee, $SCCPValue\n")

                        println("Iteration: $count, Reference Number: $referenceNumber")



                    }

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
    fun testSellScenarios() {
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
                        result =  feesComputation(desiredPrice, desiredQuantity, broker, isBuy)

                        var fees = ""
                        if(isBuy){
                            fees = (result["Buy Charges"] as? String ?: "0.0")
                        } else {
                            fees = (result["Sell Charges"] as? String ?: "0.0")
                        }
                        val subTotal = (amountWithFees.toDouble() + fees.toDouble()).toString()


                        result = feesComputation(lastPrice, minQuantity, broker, isBuy)

                        var cutLossFees = ""
                        if(isBuy){
                            cutLossFees = (result["Buy Charges"] as? String ?: "0.0")
                        } else {
                            cutLossFees = (result["Sell Charges"] as? String ?: "0.0")
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
                            file.writeText("Reference Number,IsBuy,OrderType,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,TotalPrice,Subtotal,FeesWithComma,DIFF Fees,WillAddNotes,NoteMessage,Expected Status,ExecutedPrice,ExecutedQuantity\n")
                            file2.writeText("Reference Number,IsBuy,OrderType,StockCode,CurrentDate,IsEndOfDay,OrderStatus,DesiredPrice,DesiredQuantity,MinQuantity,TotalPrice,Subtotal,FeesWithComma,DIFF Fees,WillAddNotes,NoteMessage,Expected Status,ExecutedPrice,ExecutedQuantity\n")
                        }

                        val currentDate = getCurrentDateFormatted()
                        val formattedDate = "\"$currentDate\""

                        var executedPrice = ""
                        var executedQuantity = ""

                        var brokerCommissionValue = ""
                        var VATValue = ""
                        var PSEandSECFee = ""
                        var SCCPValue = ""

                        if (count == 2 || count == 3) {
                            // Convert the input string to a double
                            val inputNumber = desiredPrice.toDouble()

                            // Generate a random double between 0.71 and 1.0
                            val randomPercentage = Random.nextDouble(0.71, 1.0)

                            // Calculate the result and round to two decimal places
                            executedPrice = (round(inputNumber * randomPercentage * 100) / 100).toString()

                            val quantityInput = desiredQuantity.toInt()
                            val minQuantityInput = minQuantity.toInt()

                            // Calculate the maximum number of minQuantity units that can be included
                            val maxUnits = quantityInput / minQuantityInput

                            // Generate a random number of units, ensuring at least one unit less than maxUnits
                            val randomUnits = if (maxUnits > 1) {
                                Random.nextInt(1, maxUnits)
                            } else {
                                0
                            }

                            executedQuantity = (randomUnits * minQuantityInput).toString()
                            brokerCommissionValue = (result["Broker Commission"] as? Double ?: 0.0).toString()
                            VATValue = (result["Broker VAT"] as? Double ?: 0.0).toString()
                            PSEandSECFee = (result["PSE & SEC Fee"] as? Double ?: 0.0).toString()
                            SCCPValue =  (result["SCCP Fee"] as? Double ?: 0.0).toString()
                        }

                        // Append the reference number to the CSV file
                        file.appendText("$referenceNumber,$isBuy,$orderType,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$amountWithFees,$subTotal,$fees,$diff_fees,$willAddNotes,$noteMessage,$expectedStatus,$executedPrice,$executedQuantity, $brokerCommissionValue, $VATValue, $PSEandSECFee, $SCCPValue\n")
                        file2.appendText("$referenceNumber,$isBuy,$orderType,$stockCode,$formattedDate,$isEndOfDay,$orderStatus,$desiredPrice,$desiredQuantity,$minQuantity,$amountWithFees,$subTotal,$fees,$diff_fees,$willAddNotes,$noteMessage,$expectedStatus,$executedPrice,$executedQuantity, $brokerCommissionValue, $VATValue, $PSEandSECFee, $SCCPValue\n")

                        println("Iteration: $count, Reference Number: $referenceNumber")


                    }

                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }

    //Order with Multiple Status

    //Cancel Buy Order and Can't Cancel Order for every Order Status

    //Cancel Sell Order and Can't Cancel Order for every Order Status

    //Sell with 0 shares
}