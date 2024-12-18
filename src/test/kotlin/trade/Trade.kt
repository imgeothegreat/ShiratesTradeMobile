package trade

import macro.*
import macro.Onboarding.getRandomBoolean
import macro.Portfolio.getRandomStock
import macro.Portfolio.getUniqueStock
import macro.Trade
import macro.Trade.calculateCeilingPrice
import macro.Trade.calculateFloorPrice
import macro.Trade.feesComputation
import macro.Trade.formatNumberWithCommaPeriod
import macro.Trade.formatToKilo
import macro.Trade.getBoardLot
import macro.Trade.getClosingPrice
import macro.Trade.getCurrenTradingHour
import macro.Trade.getDisabledRandomStock
import macro.Trade.getInvalidTickSize
import macro.Trade.getLastPrice
import macro.Trade.getLocalTime
import macro.Trade.getQuantity
import macro.Trade.getRandomDesiredPrice
import macro.Trade.getRandomDesiredQuantity
import macro.Trade.getTickSize
import macro.Trade.totalAmountComputation
import macro.Wallet.getWalletBalance
import net.bytebuddy.asm.Advice.Local
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.logging.TestLog.case
import shirates.core.logging.TestLog.scenario
import shirates.core.testcode.UITest
import java.time.LocalTime
import kotlin.math.floor
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.asserter

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Trade : UITest() {

//    private var email = "tradetestqa+superuser@gmail.com"
//    private var pword = "password123$"
    private var email = "thetestdummytest+tradebuyer@gmail.com"
    private var pword = "123123"
    private var pin = "123123"
    private var accountName = "CHARLES XAVIER"
    private val useUpdate = false
    val broker = "1"
    private var firstName = "Dummy"



    //Test Projected Price Open
    //Test Projected Price Close
    @Test
    @DisplayName("Projected Price")
    @Order(1)
    fun testProjectedPrice() {

        scenario {

            case(1) {
                expectation {
                    //Get Current Trading Hour
                    val currentTradingHour = Trade.getCurrenTradingHour()

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Code will only run during pre-open and pre-close hours
                    if(currentTradingHour.contains("Pre")){

                        //it.macro("[Enter Trading Pin]", pin, useUpdate)

                        var stockCode = ""

                        val disabledStocks = listOf("MER")

                        do {
                            stockCode = getUniqueStock()  // Assuming getUniqueStock() returns a string
                        } while (disabledStocks.contains(stockCode))

                        val stockName= Trade.getStockNameByCode(stockCode)

                        println("Stock Code: $stockCode")
                        println("----------------------")

                        if(currentTradingHour == "Pre-Open"){
                            it.exist("Market: Pre-Open")
                        } else if(currentTradingHour == "Pre-Open No Cancellation"){
                            it.exist("Market: Pre-Open No Cancellation")
                        } else if (currentTradingHour == "Pre-Close") {
                            it.exist("Market: Pre-Close")
                        } else if (currentTradingHour == "Pre-Close No Cancellation") {
                            it.exist("Market: Pre-Close No Cancellation")
                        } else {
                            it.exist("*Market: Pre*")
                            println("Market Hour is not pre-open or pre-close")
                        }


                        it.macro("[Search Stock]", stockCode, stockName)

                        if(currentTradingHour.contains("Pre-Open")){
                            it.exist("Projected Open")
                        } else if (currentTradingHour.contains("Pre-Close")){
                            it.exist("Projected Close")
                        }

                        it.exist("Price")
                        //it.exist(price)
                        it.exist("Shares")
                        //it.exist(shares)
                        it.exist("Chg (%)")
                        //it.exist(chg)
                        it.exist("Volume")
                        //it.exist(volume)

                    }
                    else {
                        println("Test ignored Market Hours not during Pre-Close or Pre-Open")
                    }

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    //Test Invalid Price Fluctuation
    //Test Invalid Board Lot
    //Test Port Allocate and Computation
    //Test Limit Orders
    //End of Day
    @Test
    @DisplayName("Limit Buy Shares with Static Threshold and Port Allocate Computation")
    @Order(2)
    fun testLimitBuySharesQuantityPortAmount() {

        scenario {
            //Missing @
            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Get Current Trading Hour
                    val currentTradingHour = getCurrenTradingHour()


                    val walletBalance = getWalletBalance()
                    var stockCode = ""

                    val disabledStocks = listOf("MER")

                    do {
                        stockCode = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(stockCode))

                    val stockName= Trade.getStockNameByCode(stockCode)


                    println("Stock Code: $stockCode")
                    println("----------------------")


                    it.exist("Market: $currentTradingHour")

                    it.macro("[Search Stock]", stockCode, stockName)

                    val lastPrice = getLastPrice(stockCode)
                    val closingPrice = getClosingPrice()
                    val isBuy = true
                    val orderType = "Limit"
                    val (desiredPrice,tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                    val minQuantity = getBoardLot(lastPrice.toDouble()).toString()


                    val currentTotal = totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
                    val desiredQuantity = getQuantity(desiredPrice, minQuantity, currentTotal, orderType)

                    val desiredPesos = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    val useChart = false
                    val buyInShares = true


                    val amountWithFees = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                    val usePortAllocate = true
                    val viewAvailableCash = false
                    var result : Map<String, Any> = emptyMap()
                    result =feesComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    var fees = ""
                    if(isBuy){
                        fees = (result["Buy Charges"] as? String ?: "0.0")
                    } else {
                        fees = (result["Sell Charges"] as? String ?: "0.0")
                    }
                    val subTotal = (amountWithFees.toDouble() - fees.toDouble()).toString()

                    //Ceiling and Floor Price
                    val testCeilingAndFloorPrice = true
                    ///val closingPrice = getLatestLastPrice(stockCode).toString()
                    var ceilingPrice = calculateCeilingPrice(closingPrice)
                    var floorPrice = calculateFloorPrice(closingPrice)
                    ceilingPrice = formatNumberWithCommaPeriod(ceilingPrice)
                    floorPrice = formatNumberWithCommaPeriod(floorPrice)

                    val previousTotal = 2.0


                    it.macro("[Buy Shares]", stockCode, lastPrice, desiredPrice, minQuantity, desiredQuantity, orderType, useChart, buyInShares, desiredPesos, usePortAllocate, walletBalance,  testCeilingAndFloorPrice, ceilingPrice, floorPrice, broker, isBuy, currentTradingHour, previousTotal)
                    it.macro("[Test Tick Size and Board Lot]", desiredPrice, desiredQuantity, tickSize, minQuantity, currentTradingHour)

                    val isEndOfDay = true
                    val willAddNotes = getRandomBoolean()
                    val noteMessage = "Buying $stockCode with $desiredQuantity shares @ $desiredPrice $orderType Price"

                    if(viewAvailableCash){
                        it.macro("[View Available Cash]")
                    }



                    if(currentTradingHour != "Runoff") {
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

                        it.wait(3)

//                        describe("Wait for Page to Load")
//                            .waitForDisplay("Order Placed", waitSeconds = 30.0)
//
//                        it.exist("To cancel your order, go to Portfolio > Orders")

                        it.exist(stockCode)
                        it.exist("Statistics")

                        var expectedStatus = "Pending"

                        val countLoop = 1
                        var orderStatus = ""
                        if (currentTradingHour != "Close") {
                            orderStatus = "Rejected"
                            expectedStatus = "Cancelled"
                        } else if (currentTradingHour == "Close") {
                            orderStatus = "Pending"
                            expectedStatus = "Pending"
                        }

                        it.macro("[Check Orders Tab]", currentTradingHour, stockCode, isBuy, orderType,desiredQuantity, isEndOfDay, desiredPesos, desiredPrice, countLoop, desiredPesos, orderStatus, willAddNotes, noteMessage, subTotal,fees , currentTradingHour, walletBalance, previousTotal)

                    }



                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }

    //Test Conditional Orders
    //Test Chart Selection
    //Not Enough Balance
    //Wallet Balance Test
    @Test
    @DisplayName("Conditional Buy Pesos with Wallet Balance Test and Chart Selection")
    @Order(3)
    @Ignore
    fun testConditionalBuyPesoAmount() {

        scenario {

            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Get Current Trading Hour
                    val currentTradingHour = getCurrenTradingHour()

                    val walletBalance = getWalletBalance()
                    //val stockCode = getRandomStock()
                    val stockCode= getUniqueStock()
                    val stockName= Trade.getStockNameByCode(stockCode)

                    println("Stock Code: $stockCode")
                    println("----------------------")

                    it.exist("Market: $currentTradingHour")

                    it.macro("[Search Stock]", stockCode, stockName)

                    val lastPrice = getLastPrice(stockCode)
                    val closingPrice = getClosingPrice()
                    val isBuy = true
                    val orderType = "Conditional"
                    val (desiredPrice,tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                    val minQuantity = getBoardLot(lastPrice.toDouble()).toString()
                    val desiredQuantity = getRandomDesiredQuantity(desiredPrice, minQuantity, walletBalance)

                    val desiredPesos = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    val useChart = true
                    val buyInShares = false


                    val amountWithFees = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                    val usePortAllocate = false
                    val viewAvailableCash = true
                    var result : Map<String, Any> = emptyMap()
                    result = feesComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    var fees = ""
                    if(isBuy){
                        fees = (result["Buy Charges"] as? String ?: "0.0")
                    } else {
                        fees = (result["Sell Charges"] as? String ?: "0.0")
                    }
                    val subTotal = (amountWithFees.toDouble() - fees.toDouble()).toString()

                    //Ceiling and Floor Price
                    val testCeilingAndFloorPrice = false
                    ///val closingPrice = getLatestLastPrice(stockCode).toString()
                    val ceilingPrice = calculateCeilingPrice(closingPrice)
                    val floorPrice = calculateFloorPrice(closingPrice)

                    val previousTotal = 2.0

                    it.macro("[Buy Shares]", stockCode, lastPrice, desiredPrice, minQuantity, desiredQuantity, orderType, useChart, buyInShares, desiredPesos, usePortAllocate, walletBalance,  testCeilingAndFloorPrice, ceilingPrice, floorPrice, broker, isBuy, currentTradingHour , previousTotal)

                    val isEndOfDay = false
                    val willAddNotes = getRandomBoolean()
                    val noteMessage = "Buying $stockCode with $desiredQuantity shares @ $desiredPrice $orderType Price"

                    if(viewAvailableCash){
                        it.macro("[View Available Cash]")
                    }

                    // There's a bug comment for now to proceed with testing
                    //it.macro("[Wallet Balance Test]", desiredPrice, minQuantity, walletBalance, desiredPesos)



                    if(currentTradingHour != "Runoff") {
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

                        val countLoop = 1
                        var orderStatus = ""
                        if (currentTradingHour != "Close") {
                            orderStatus = "Rejected"
                            expectedStatus = "Cancelled"
                        } else if (currentTradingHour == "Close") {
                            orderStatus = "Pending"
                            expectedStatus = "Pending"
                        }

                        it.macro("[Check Orders Tab]", currentTradingHour, stockCode, isBuy, orderType,desiredQuantity, isEndOfDay, desiredPesos, desiredPrice, countLoop, desiredPesos, orderStatus, willAddNotes, noteMessage, subTotal,fees , currentTradingHour, walletBalance, previousTotal)

                    }



                    it.terminateApp()
                    it.launchApp()



                }
            }
        }
    }

    //Disable Stock
    @Test
    @DisplayName("Open Disabled Stock")
    @Order(4)
    fun testDisabledStock() {

        scenario {

            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Get Current Trading Hour
                    val currentTradingHour = getCurrenTradingHour()

                    val stockCode = "JFC"
                    val stockName = "Jollibee Foods Corporation"
                    println("Stock Code: $stockCode")
                    println("----------------------")

                    describe("Wait for Home Page")
                        .waitForDisplay("Market: $currentTradingHour", waitSeconds = 30.0)

                    it.exist("Market: $currentTradingHour")

                    it.macro("[Search Stock]", stockCode, stockName)


                    it.tap("Buy")

                    it.dontExist("Buy Order")
                    it.dontExist("Quantity")
                    it.dontExist("Preview Order")
                    it.dontExist("Total")
                    it.dontExist("Available Cash")

                    it.tap("Sell")

                    it.dontExist("Sell Order")
                    it.dontExist("Quantity")
                    it.dontExist("Preview Order")
                    it.dontExist("Total")
                    it.dontExist("Available Cash")

                    it.exist("Suspended")

                    it.dontExist("Normal Lot")

                    it.terminateApp()
                    it.launchApp()



                }
            }
        }
    }

    @Test
    @DisplayName("Invalid Buy TC Code")
    @Order(5)
    fun testInvalidTCCode() {
        scenario {
            //Missing @
            case(1) {
                expectation {

                    //Get Current Trading Hour
                    val currentTradingHour = Trade.getCurrenTradingHour()
                    //it.macro("[Install Code Push First]", email, pword, pin)


                    // Will Ignore test when trading hours is outside open market hours
                    if(currentTradingHour != "Runoff" && currentTradingHour != "Close") {



                        //it.macro("[Log Out]")
//                        it.macro("[Login]", email, pword)
//                        it.macro("[Select Broker]", broker, pin, useUpdate)

                        it.macro("[Lock Trading Pin]", pin, firstName)

                        describe("Wait for Home Page")
                            .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                        val walletBalance = getWalletBalance()
                        var stockCode = ""

                        val disabledStocks = listOf("MER")

                        do {
                            stockCode = getUniqueStock()  // Assuming getUniqueStock() returns a string
                        } while (disabledStocks.contains(stockCode))

                        val stockName= Trade.getStockNameByCode(stockCode)


                        println("Stock Code: $stockCode")
                        println("----------------------")

                        it.exist("Market: $currentTradingHour")

                        it.macro("[Search Stock]", stockCode, stockName)

                        val lastPrice = Trade.getLastPrice(stockCode)
                        val closingPrice = Trade.getClosingPrice()
                        val isBuy = true
                        val orderType = "Limit"
                        val (desiredPrice,tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                        val minQuantity = Trade.getBoardLot(lastPrice.toDouble()).toString()


                        val currentTotal = Trade.totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
                        val desiredQuantity = Trade.getQuantity(desiredPrice, minQuantity, currentTotal, orderType)

                        val desiredPesos = Trade.totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                        val useChart = false
                        val buyInShares = true


                        val amountWithFees = Trade.totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                        val usePortAllocate = false
                        val viewAvailableCash = false

                        var result : Map<String, Any> = emptyMap()
                        result = feesComputation(desiredPrice, desiredQuantity, broker, isBuy)

                        var fees = ""
                        if(isBuy){
                            fees = (result["Buy Charges"] as? String ?: "0.0")
                        } else {
                            fees = (result["Sell Charges"] as? String ?: "0.0")
                        }

                        val subTotal = (amountWithFees.toDouble() - fees.toDouble()).toString()

                        //Ceiling and Floor Price
                        val testCeilingAndFloorPrice = false
                        ///val closingPrice = getLatestLastPrice(stockCode).toString()
                        val ceilingPrice = Trade.calculateCeilingPrice(closingPrice)
                        val floorPrice = Trade.calculateFloorPrice(closingPrice)

                        val previousTotal = 2.0

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

                        val isEndOfDay = true
                        val willAddNotes = Onboarding.getRandomBoolean()
                        val noteMessage =
                            "Buying $stockCode with $desiredQuantity shares @ $desiredPrice $orderType Price"

                        if (viewAvailableCash) {
                            it.macro("[View Available Cash]")
                        }



                        if (currentTradingHour != "Runoff") {
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

                            describe("Wait for Page to Load")
                                .waitForDisplay(stockCode, waitSeconds = 30.0)
                            it.exist(stockCode)
                            it.exist("Bid And Ask")

                            val quantityKilo = formatToKilo(desiredQuantity)
                            val message = "[Order Cancelled]: $stockCode Buy $quantityKilo $desiredPrice"

                            //go back
                            it.wait(7)
                            it.tap("\uE944")

                            describe("Wait for Page to Load")
                                .waitForDisplay("Portfolio", waitSeconds = 30.0)

                            it.exist("Portfolio")
                            it.exist("Volume")
                            it.exist("Orders")

                            it.macro("[Check Notification Content]", message)

                            var expectedStatus = "Pending"

                            val countLoop = 1
                            var orderStatus = ""
                            if (currentTradingHour != "Close") {
                                orderStatus = "Rejected"
                                expectedStatus = "Cancelled"
                            } else if (currentTradingHour == "Close") {
                                orderStatus = "Pending"
                                expectedStatus = "Pending"
                            }

                            it.macro("[Check Orders Tab]", currentTradingHour, stockCode, isBuy, orderType,desiredQuantity, isEndOfDay, desiredPesos, desiredPrice, countLoop, desiredPesos, orderStatus, willAddNotes, noteMessage, subTotal,fees , currentTradingHour, walletBalance, previousTotal)


                        }


                        it.terminateApp()
                        it.launchApp()
                    }


                }
            }
        }
    }

    //Not Verified User

    @Test
    @DisplayName("Unverified User")
    @Order(6)
    fun testUnverifiedUser() {
        scenario {
            //Missing @
            case(1) {
                expectation {
                    //Get Current Trading Hour
                    val currentTradingHour = Trade.getCurrenTradingHour()

                    //it.macro("[Log Out]")

                    email = "tradetestqa+superuser2@gmail.com"
                    pword = "123123"
                    pin = "123123"

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                    val walletBalance = "0.00"
                    var stockCode = ""

                    val disabledStocks = listOf("MER")

                    do {
                        stockCode = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(stockCode))

                    val stockName= Trade.getStockNameByCode(stockCode)


                    println("Stock Code: $stockCode")
                    println("----------------------")

                    it.exist("Market: $currentTradingHour")

                    it.macro("[Search Stock]", stockCode, stockName)

                    val lastPrice = getLastPrice(stockCode)
                    val closingPrice = getClosingPrice()
                    val isBuy = true
                    val orderType = "Limit"
                    val (desiredPrice,tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                    val minQuantity = getBoardLot(lastPrice.toDouble()).toString()


                    val currentTotal = totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
                    val desiredQuantity = getQuantity(desiredPrice, minQuantity, currentTotal, orderType)

                    val desiredPesos = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    val useChart = false
                    val buyInShares = true

                    val usePortAllocate = false

                    //Ceiling and Floor Price
                    val testCeilingAndFloorPrice = false
                    ///val closingPrice = getLatestLastPrice(stockCode).toString()
                    val ceilingPrice = calculateCeilingPrice(closingPrice)
                    val floorPrice = calculateFloorPrice(closingPrice)

                    val previousTotal = 2.0

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


                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }

    //No Wallet Balance
    @Test
    @DisplayName("No Wallet Balance")
    @Order(7)
    fun testNoWalletBalance() {
        scenario {
            //Missing @
            case(1) {
                expectation {
                    //Get Current Trading Hour
                    val currentTradingHour = Trade.getCurrenTradingHour()

                    //it.macro("[Log Out]")

                    email = "tradetestqa+superusernew@gmail.com"
                    pword = "password123$"
                    pin = "123123"

                    //it.macro("[Install Code Push First]", email, pword, pin)
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                    val walletBalance = "0.00"
                    var stockCode = ""

                    val disabledStocks = listOf("MER")

                    do {
                        stockCode = getUniqueStock()  // Assuming getUniqueStock() returns a string
                    } while (disabledStocks.contains(stockCode))

                    val stockName= Trade.getStockNameByCode(stockCode)

                    println("Stock Code: $stockCode")
                    println("----------------------")

                    it.exist("Market: $currentTradingHour")

                    it.macro("[Search Stock]", stockCode, stockName)

                    val lastPrice = getLastPrice(stockCode)
                    val closingPrice = getClosingPrice()
                    val isBuy = true
                    val orderType = "Limit"
                    val (desiredPrice,tickSize) = getRandomDesiredPrice(lastPrice, currentTradingHour, orderType, isBuy)
                    val minQuantity = getBoardLot(lastPrice.toDouble()).toString()


                    val currentTotal = totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
                    val desiredQuantity = getQuantity(desiredPrice, minQuantity, currentTotal, orderType)

                    val desiredPesos = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)

                    val useChart = false
                    val buyInShares = true

                    val usePortAllocate = false

                    //Ceiling and Floor Price
                    val testCeilingAndFloorPrice = false
                    ///val closingPrice = getLatestLastPrice(stockCode).toString()
                    val ceilingPrice = calculateCeilingPrice(closingPrice)
                    val floorPrice = calculateFloorPrice(closingPrice)

                    val previousTotal = 2.0

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

                    it.terminateApp()
                    it.launchApp()


                }
            }
        }
    }



    //Bid and Ask

    //Transaction Details

    //Different Board Lot

    //test Dynamic Threshold

    //Wash Sale

    //Sort and Filter


}