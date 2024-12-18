package macro

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.io.BufferedReader
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.math.*
import kotlin.random.Random

@MacroObject
object Trade  : TestDrive {

    //Default Percentages in Decimals
    val brokerCommission = 0.0025
    val VAT = 0.12
    val PSEFee = 0.00005
    val PSEVAT = 0.1212
    val SCCP = 0.0001
    val SECFee = 0.00005
    val SalesTax = 0.006


    @Macro("[View Statistics PSEi]")
    fun viewStatisticsLabel() {

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/PSE_Stats.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        var psei_current_price:String
        var psei_pclose: String
        var psei_low:String
        var psei_volume: String
        var psei_52wk_low:String
        var psei_market_cap: String
        var psei_nfbs:String
        var psei_open: String
        var psei_high:String
        var psei_shares: String
        var psei_52wk_high:String
        var psei_trades: String

        // Iterate through each row
        csvData.drop(1).firstOrNull()?.let { row ->
            println("Processing first row:")

            // Check if the row has at least 6 elements
            if (row.size >= 11) {
                psei_current_price = row[1]
                psei_pclose = row[3]
                psei_low = row[4]
                psei_volume = row[5]
                psei_52wk_low = row[6]
                psei_market_cap = row[7]
                psei_nfbs = row[8]
                psei_open = row[9]
                psei_high = row[10]
                psei_shares = row[11]
                psei_52wk_high = row[12]
                psei_trades = row[13]

                it.exist("₱$psei_current_price")
                it.exist("P. Close")
                it.exist(psei_pclose)

                it.exist("Low")
                it.exist(psei_low)

                it.exist("Volume")
                it.exist(psei_volume)

                it.exist("52wk Low")
                it.exist(psei_52wk_low)

                it.exist("Market Cap")
                it.exist(psei_market_cap)

                it.exist("NFB/S")
                it.exist(psei_nfbs)

                it.exist("Open")
                it.exist(psei_open)

                it.exist("High")
                it.exist(psei_high)

                it.exist("Shares")
                it.exist(psei_shares)

                it.exist("52wk High")
                it.exist(psei_52wk_high)

                it.exist("Trades")
                it.exist(psei_trades)

                it.dontExist("Avg. Price")


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")
        }



    }

    @Macro("[Invest Search]")
    fun tradeSearch(stockCode: String) {
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

        it.tap("Search")
            .sendKeys(stockCode)

        //Tap Stock Code
        it.tap("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup")
    }

    @Macro("[Get Last Price]")
    fun getLastPrice(stockCode: String): String {
        it.tap("Buy")
        var priceWithSockCode = it.select("*$stockCode:*")

        var lastPrice = priceWithSockCode.text.replace("$stockCode: ", "")
        lastPrice = lastPrice.replace(",","")

        //it.tap("\uE925")
        it.tap(x=996,y=1074)

        return lastPrice

    }

    @Macro("[Sell Shares]")
    fun sellShares(
        stockCode: String,
        lastPrice: String,
        desiredPrice: String,
        minQuantity: String,
        desiredQuantity: String,
        orderType: String,
        useChart: Boolean,
        buyInShares: Boolean,
        desiredPesos: String,
        usePortAllocate: Boolean,
        walletBalance: String,
        testCeilingFloorPrice:Boolean,
        ceilingPrice:String,
        floorPrice:String,
        broker:String,
        isBuy: Boolean,
        tradingHour:String,
        previousTotal:Double,
        cutlossPrice:String,
        useChartTargetPrice:Boolean,
        useEqualConditionTargetPrice:Boolean,
        useChartCutlossPrice:Boolean,
        useEqualConditionCutlossPrice:Boolean,
    ){

        val csvFile = File("src/test/resources/My_Stocks.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)
        var shares = "0"

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")

        }

        csvData.drop(1).forEachIndexed { index, row ->

            println("Processing Row ${index + 1}:")

            // Check if the row has at least 5 elements
            if (row.size >= 5) {

                val stock_code = row[0]


                if(stock_code == stockCode){
                    shares = row[2]
                    println("Stock Code: $stockCode")
                    println("Shares: $shares")

                }

            } else {
                println("Row does not have enough elements")

            }

            println("------------------------")


        }

        it.tap("Sell")

        var walletWithFeesComma = "0.00"
        if(tradingHour == "Close"){
            if(isBuy){
                val walletClean = formatNumberWithCommaPeriod((walletBalance.toDouble()-previousTotal).toString()).replace(",", "").toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean )
            } else {
                val walletClean = formatNumberWithCommaPeriod((walletBalance.toDouble()).toString()).replace(",", "").toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean )
            }

        } else {
            if(isBuy){
                val walletClean = formatNumberWithCommaPeriod(walletBalance.toDouble().toString()).replace(",", "").toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean )
            } else {
                val walletClean = formatNumberWithCommaPeriod((walletBalance.toDouble()).toString()).replace(",", "").toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean )
            }

        }

        val amountWithFees = totalAmountComputation(lastPrice, minQuantity, broker, isBuy)
        val totalwithComma = formatNumberWithCommaPeriod(amountWithFees)
        var lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)

        it.exist("Sell Order")

        //close icon
        //it.exist("\uE925")
        it.exist("$stockCode: $lastPriceWithComma")
        it.exist("Limit")
        it.exist("Price")
        it.exist("Select in Chart")
        //it.exist("\uEA0B")
        //-
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0B\"])[1]")
        //+
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0A\"])[1]")
        it.exist("Quantity")
        //-
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0B\"])[2]")
        //+
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0A\"])[2]")
        //it.exist("\uE902")
        it.select("*Shares on hand:*")
            .textContains("Shares on hand:")
        //it.exist("Shares on hand: $shares")

        it.exist("Show port allocation")
        it.exist("Total")
        it.exist("₱ $totalwithComma")
        it.exist("Available Cash")
        it.exist("₱ $walletWithFeesComma")
        it.exist("Preview Order")

        var desiredPriceWithCommas = formatNumberWithCommaPeriod(desiredPrice)
        var cutlossPriceWithCommas = formatNumberWithCommaPeriod(cutlossPrice)

        //Change Order Type
        if (orderType == "Limit") {


            //Use Chart?
            if (useChart) {
                it.tap("Select in Chart")

                it.exist("$stockCode: $lastPriceWithComma")

                it.exist("Sell Price")
                it.exist("₱")
                it.exist("1D")
                it.exist("3M")
                it.exist("6M")
                it.exist("1Y")
                it.exist("2Y")
                //+ icon
                //it.exist("\uEA0A")
                //- icon
                //it.exist("\uEA0B")
                //chart
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup")
                //line
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
                    .swipeToBottom()

                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
                    .swipeToTop()


                //it.tap("\uEA0B")
                it.tap(x=1014,y=1960)


                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")
                //it.tap("\uEA0A")
                it.tap(x=1010,y=675)


                it.tap("1D")
                it.tap("3M")
                it.tap("6M")
                it.tap("1Y")
                it.tap("2Y")

                it.tap(x=546,y=587)
                    .clearInput()
                    .sendKeys(desiredPriceWithCommas)

                it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

                it.tap("Done")

                if(stockCode == "MB"){
                    val cleanDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
                    desiredPriceWithCommas = String.format("%,.2f", cleanDesiredPrice)
                }

                it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

            } else {

                //Just enter desired price
                it.tap("$lastPriceWithComma&&.android.widget.EditText")
                    .clearInput()
                    .sendKeys(desiredPriceWithCommas)

                it.exist(desiredPriceWithCommas)
            }


        } else if (orderType == "Conditional"){
            it.tap("Limit")
            it.exist("Order Posting")

            it.exist("Limit")
            it.exist("Filled immediately based on the price you set")

            it.exist("Conditional")
            it.exist("Filled once the preset order price has been met")

            it.tap("Conditional")

            it.exist("Conditional")

            it.exist("Target Price")
            it.exist("xpath=(//android.widget.TextView[@text=\"Select in Chart\"])[1]")
            it.exist("xpath=(//android.widget.TextView[@text=\"Select in Chart\"])[2]")

            it.exist("xpath=(//android.widget.TextView[@text=\"Condition: \"])[1]")
            it.exist("xpath=(//android.widget.TextView[@text=\"Condition: \"])[2]")

            it.exist("xpath=(//android.widget.TextView[@text=\"Equal\"])[1]")
            it.exist("xpath=(//android.widget.TextView[@text=\"Equal\"])[2]")

            //caret sign
            //it.exist("xpath=(//android.widget.TextView[@text=\"\uE91B\"])[1]")
            //it.exist("xpath=(//android.widget.TextView[@text=\"\uE91B\"])[2]")

            //target price

            if(useChartTargetPrice){
                it.tap("xpath=(//android.widget.TextView[@text=\"Select in Chart\"])[1]")

                it.exist("$stockCode: $lastPriceWithComma")

                it.exist("Sell (Target Price)")
                it.exist("₱")
                it.exist("1D")
                it.exist("3M")
                it.exist("6M")
                it.exist("1Y")
                it.exist("2Y")
                //+ icon
                //it.exist("\uEA0A")
                //- icon
                //it.exist("\uEA0B")
                //chart
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup")
                //line
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
                    .swipeToBottom()

                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
                    .swipeToTop()

                //it.tap("\uEA0B")
                it.tap(x=1014,y=1960)


                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")
                //it.tap("\uEA0A")
                it.tap(x=1010,y=675)

                it.tap("1D")
                it.tap("3M")
                it.tap("6M")
                it.tap("1Y")
                it.tap("2Y")

                it.tap(x=546,y=587)
                    .clearInput()
                    .sendKeys(desiredPriceWithCommas)

                it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

                it.tap("Done")

                if(stockCode == "MB"){
                    val cleanDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
                    desiredPriceWithCommas = String.format("%,.2f", cleanDesiredPrice)
                }

                it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

            } else {

                //Just enter desired target price
                it.tap("xpath=(//android.widget.EditText[@text=\"$lastPriceWithComma\"])[1]")
                    .clearInput()
                    .sendKeys(desiredPriceWithCommas)

                it.exist(desiredPriceWithCommas)
            }

            //condition target price

            if(!useEqualConditionTargetPrice){
                it.tap("xpath=(//android.widget.TextView[@text=\"Condition: \"])[1]")

                it.exist("xpath=(//android.widget.TextView[@text=\"Equal\"])[1]")
                it.exist("Greater Than Or Equal")

                it.tap("Greater Than Or Equal")

                it.exist("Greater Than Or Equal")
            } else {
                it.exist("Equal")
            }

            //cutloss price

            if(useChartCutlossPrice){
                it.tap("xpath=(//android.widget.TextView[@text=\"Select in Chart\"])[2]")

                it.exist("$stockCode: $lastPriceWithComma")

                it.exist("Sell (Cutloss Price)")
                it.exist("₱")
                it.exist("1D")
                it.exist("3M")
                it.exist("6M")
                it.exist("1Y")
                it.exist("2Y")

                it.wait(2)
                //+ icon
                //it.exist("\uEA0A")
                //- icon
                //it.exist("\uEA0B")
                //chart
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup")
                //line
                it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
                    .swipeToBottom()

                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")

                it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]")
                    .swipeToTop()

                //it.tap("\uEA0B")
                it.tap(x=1014,y=1960)


                it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")
                //it.tap("\uEA0A")
                it.tap(x=1010,y=675)

                it.tap("1D")
                it.tap("3M")
                it.tap("6M")
                it.tap("1Y")
                it.tap("2Y")

                it.tap(x=546,y=587)
                    .clearInput()
                    .sendKeys(cutlossPriceWithCommas)

                it.exist("xpath=//android.widget.EditText[@text=\"$cutlossPriceWithCommas\"]")

                it.tap("Done")

                if(stockCode == "MB"){
                    val cleanCutlossPrice = cutlossPriceWithCommas.replace(",", "").toDouble()
                    cutlossPriceWithCommas = String.format("%,.2f", cleanCutlossPrice)
                }

                it.exist("xpath=//android.widget.EditText[@text=\"$cutlossPriceWithCommas\"]")

            } else {

                //Just enter desired target price
                it.tap("xpath=(//android.widget.EditText[@text=\"$lastPriceWithComma\"])")
                    .clearInput()
                    .sendKeys(cutlossPriceWithCommas)

                it.exist(cutlossPriceWithCommas)
            }

            //condition cutloss price

            if(!useEqualConditionCutlossPrice){
                it.tap("xpath=(//android.widget.TextView[@text=\"Condition: \"])[1]")

                it.exist("xpath=(//android.widget.TextView[@text=\"Equal\"])[1]")
                it.exist("Greater Than Or Equal")

                it.tap("Greater Than Or Equal")

                it.exist("Greater Than Or Equal")
            } else {
                it.exist("Equal")
            }



        }

        //Test Ceiling and Floor Price

        lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)

        if(testCeilingFloorPrice){

            val ceilingPriceWithComma = formatNumberWithCommaPeriod(ceilingPrice)


            //Just enter desired price
            it.tap("$lastPriceWithComma&&.android.widget.EditText")
                .clearInput()
                .sendKeys(ceilingPriceWithComma)

            it.exist("xpath=//android.widget.EditText[@text=\"$ceilingPriceWithComma\"]")

            it.tap("Preview Order")

            if(tradingHour != "Runoff" && tradingHour != "Close" && desiredPrice != lastPrice){
                it.exist("*Price is invalid. Price must be less than or equal to Ceiling Price*")

            }

            if(desiredPrice != lastPrice && tradingHour == "Runoff"){
                it.exist("Order is invalid. Only accepting orders using the current Price.")
            }


            val floorPriceWithComma = formatNumberWithCommaPeriod(floorPrice)

            it.tap("$ceilingPriceWithComma&&.android.widget.EditText")
                .clearInput()
                .sendKeys(floorPriceWithComma)

            it.exist(floorPriceWithComma)

            it.tap("Preview Order")

            if(tradingHour != "Runoff" && desiredPrice != lastPrice){
                it.exist("*Price is invalid. Price must be greater than or equal to Floor Price*")

            }

            if(desiredPrice != lastPrice && tradingHour == "Runoff"){
                it.exist("Order is invalid. Only accepting orders using the current Price.")
            }

            it.tap(floorPriceWithComma)
                .clearInput()
                .sendKeys(lastPriceWithComma)


        }


        val desiredQuantityWithComma = formatNumberWithComma(desiredQuantity)
        val minQuantityWithComma = formatNumberWithComma(minQuantity)

        //Buy in Shares
        if (buyInShares) {
            it.tap("xpath=//android.widget.EditText[@text=\"$minQuantityWithComma\"]")
                .clearInput()
                .sendKeys(desiredQuantityWithComma)

            it.exist(desiredQuantityWithComma)

            it.scrollDown()

            if(orderType == "Conditional"){
                it.exist("Total (Target Price)")

                val totalAmount = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                val totalAmountWithCommas = formatNumberWithCommaPeriod(totalAmount)

                it.exist("₱ $totalAmountWithCommas")

                it.exist("Total (Cutloss Price)")

                val totalCutlossAmount = totalAmountComputation(cutlossPrice, desiredQuantity, broker, isBuy)
                val totalCutlossWithCommas = formatNumberWithCommaPeriod(totalCutlossAmount)

                it.exist("₱ $totalCutlossWithCommas")

                it.dontExist("Total")

            } else {

                it.exist("Total")
                it.dontExist("Total (Target Price)")
                it.dontExist("Total (Cutloss Price)")

                val totalAmount = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                val totalAmountWithCommas = formatNumberWithCommaPeriod(totalAmount)

                it.exist("₱ $totalAmountWithCommas")
            }



        }

        //Use Port Allocation
        if (usePortAllocate) {
            it.tap("Show port allocation")
            it.dontExist("Show port allocation")

            it.exist("Select allocation based on available cash")
            it.exist("Hide")
            it.exist("10%")
            it.exist("20%")
            it.exist("25%")
            it.exist("50%")

            //Force to two decimals
//            val cleanStringDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
//            desiredPriceWithCommas = String.format("%,.2f", cleanStringDesiredPrice )

            it.tap(desiredPriceWithCommas)

            //Tap 10%
            it.tap("10%")
            val (allocate10Amount,allocate10Quantity)  = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.10)

            val allocate10QuantityWithComma = formatNumberWithComma(allocate10Quantity)

            val result10 = feesComputation(desiredPrice, allocate10Quantity, broker, isBuy)
            var fees10 = ""

            if(isBuy){
                fees10 = (result10["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees10 = (result10["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total10 = (fees10.toDouble() + allocate10Amount.toDouble()).toString()
            println("Total 10%: $total10")
            val cleanString10 = formatNumberWithCommaPeriod(total10).replace(",", "").toDouble()
            val totalWithComma10 = String.format("%,.2f", cleanString10 )

            it.exist("₱ $totalWithComma10")
            it.exist(allocate10QuantityWithComma)

            //Tap 20%
            it.tap("20%")
            val (allocate20Amount,allocate20Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.20)

            val allocate20QuantityWithComma = formatNumberWithComma(allocate20Quantity)

            val result20 = feesComputation(desiredPrice, allocate20Quantity, broker, isBuy)
            var fees20 = ""

            if(isBuy){
                fees20 = (result20["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees20 = (result20["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total20 = (fees20.toDouble() + allocate20Amount.toDouble()).toString()
            println("Total 20%: $total20")
            val cleanString20 = formatNumberWithCommaPeriod(total20).replace(",", "").toDouble()
            val totalWithComma20 = String.format("%,.2f", cleanString20 )

            it.exist("₱ $totalWithComma20")
            it.exist(allocate20QuantityWithComma)

            //Tap 25%
            it.tap("25%")
            val (allocate25Amount,allocate25Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.25)

            val allocate25QuantityWithComma = formatNumberWithComma(allocate25Quantity)
            val result25 = feesComputation(desiredPrice, allocate25Quantity, broker, isBuy)
            var fees25 = ""

            if(isBuy){
                fees25 = (result25["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees25 = (result25["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total25 = (fees25.toDouble() + allocate25Amount.toDouble()).toString()
            println("Total 25%: $total25")
            val cleanString25 = formatNumberWithCommaPeriod(total25).replace(",", "").toDouble()
            val totalWithComma25 = String.format("%,.2f", cleanString25)

            it.exist("₱ $totalWithComma25")
            it.exist(allocate25QuantityWithComma)

            //Tap 50%
            it.tap("50%")
            val (allocate50Amount,allocate50Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.50)

            val allocate50QuantityWithComma = formatNumberWithComma(allocate50Quantity)

            val result50 = feesComputation(desiredPrice, allocate50Quantity, broker, isBuy)
            var fees50 = ""

            if(isBuy){
                fees50 = (result50["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees50 = (result50["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total50 = (fees50.toDouble() + allocate50Amount.toDouble()).toString()
            println("Total 50%: $total50")
            val cleanString50 = formatNumberWithCommaPeriod(total50).replace(",", "").toDouble()
            val totalWithComma50 = String.format("%,.2f", cleanString50)

            it.exist("₱ $totalWithComma50")
            it.exist(allocate50QuantityWithComma)

            it.tap("Hide")
            it.dontExist("Hide")
            it.exist("Show port allocation")

            it.tap(allocate50QuantityWithComma)
                .clearInput()
                .sendKeys(desiredQuantityWithComma)

            it.exist(desiredQuantityWithComma)
        }



    }


    @Macro("[Buy Shares]")
    fun buyShares(
        stockCode: String,
        lastPrice: String,
        desiredPrice: String,
        minQuantity: String,
        desiredQuantity: String,
        orderType: String,
        useChart: Boolean,
        buyInShares: Boolean,
        desiredPesos: String,
        usePortAllocate: Boolean,
        walletBalance: String,
        testCeilingFloorPrice:Boolean,
        ceilingPrice:String,
        floorPrice:String,
        broker:String,
        isBuy: Boolean,
        tradingHour:String,
        previousTotal:Double,
    ) {
        it.tap("Buy")

        var walletWithFeesComma = "0.00"
        if(tradingHour == "Close" && previousTotal != 2.0){
            val walletClean = formatNumberWithCommaPeriod((walletBalance.toDouble()-previousTotal).toString()).replace(",", "").toDouble()
            walletWithFeesComma = String.format("%,.2f", walletClean )
        } else {
            val walletClean = formatNumberWithCommaPeriod(walletBalance.toDouble().toString()).replace(",", "").toDouble()
            walletWithFeesComma = String.format("%,.2f", walletClean )
        }

        val amountWithFees = totalAmountComputation(lastPrice, minQuantity, broker, isBuy)
        val totalwithComma = formatNumberWithCommaPeriod(amountWithFees)
        var lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)

        it.exist("Buy Order")

        //close icon
        //it.exist("\uE925")
        it.exist("$stockCode: $lastPriceWithComma")
        it.exist("Limit")
        it.exist("Price")
        it.exist("Select in Chart")
        //it.exist("\uEA0B")
        //-
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0B\"])[1]")
        //+
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0A\"])[1]")
        it.exist("Quantity")
        it.exist("Buy in")
        it.exist("Shares")
        //-
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0B\"])[2]")
        //+
        //it.exist("xpath=(//android.widget.TextView[@text=\"\uEA0A\"])[2]")
        //it.exist("\uE902")
        it.select("*Shares on hand*")
            .textContains("Shares on hand")
        it.exist("Show port allocation")
        it.exist("Total")
        it.exist("₱ $totalwithComma")
        it.exist("Available Cash")
        it.exist("₱ $walletWithFeesComma")
        it.exist("Preview Order")


        //Change Order Type
        if (orderType != "Limit") {
            it.tap("Limit")
            it.exist("Order Posting")

            it.exist("Limit")
            it.exist("Filled immediately based on the price you set")

            it.exist("Conditional")
            it.exist("Filled once the preset order price has been met")

            it.tap("Conditional")

            it.exist("Conditional")
        }

        //Test Ceiling and Floor Price

        lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)
        var desiredPriceWithCommas = formatNumberWithCommaPeriod(desiredPrice)

        if(testCeilingFloorPrice){

            val ceilingPriceNoComma = ceilingPrice.replace(",","")
            val ceilingPriceWithComma = formatNumberWithCommaPeriod(ceilingPriceNoComma)


            //Just enter desired price
            it.tap("$lastPriceWithComma&&.android.widget.EditText")
                .clearInput()
                .sendKeys(ceilingPriceWithComma)

            it.exist("xpath=//android.widget.EditText[@text=\"$ceilingPriceWithComma\"]")

            it.tap("Preview Order")

            if(tradingHour != "Runoff" && tradingHour != "Close" && desiredPrice != lastPrice){
                it.exist("*Price is invalid. Price must be less than or equal to Ceiling Price*")

            }

            if(desiredPrice != lastPrice && tradingHour == "Runoff"){
                it.exist("Order is invalid. Only accepting orders using the current Price.")
            }

            val floorPriceNoComma = floorPrice.replace(",","")

            val floorPriceWithComma = formatNumberWithCommaPeriod(floorPriceNoComma)

            it.tap("$ceilingPriceWithComma&&.android.widget.EditText")
                .clearInput()
                .sendKeys(floorPriceWithComma)

            it.exist(floorPriceWithComma)

            it.tap("Preview Order")

            if(tradingHour != "Runoff" && desiredPrice != lastPrice){
                it.exist("*Price is invalid. Price must be greater than or equal to Floor Price*")

            }

            if(desiredPrice != lastPrice && tradingHour == "Runoff"){
                it.exist("Order is invalid. Only accepting orders using the current Price.")
            }

            it.tap(floorPriceWithComma)
                .clearInput()
                .sendKeys(lastPriceWithComma)


        }

        //Use Chart?
        if (useChart) {
            it.tap("Select in Chart")

            it.exist("$stockCode: $lastPriceWithComma")

            it.exist("Buy Price")
            it.exist("₱")
            it.exist("1D")
            it.exist("3M")
            it.exist("6M")
            it.exist("1Y")
            it.exist("2Y")

            it.wait(2)
            //+ icon
//            describe("Wait for Page to Load")
//                .waitForDisplay("\uEA0A", waitSeconds = 30.0)
//
//            it.exist("\uEA0A")
            //- icon

//            describe("Wait for Page to Load")
//                .waitForDisplay("\uEA0B", waitSeconds = 30.0)
//            it.exist("\uEA0B")
            //chart
            it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup")

            //line
            it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")

            it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")
                .swipeToBottom()

            it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")

            it.select("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[4]/android.view.ViewGroup/android.view.ViewGroup")
                .swipeToTop()

            //it.tap("\uEA0B")
            it.tap(x=1014,y=1960)


            it.dontExist("xpath=//android.widget.EditText[@text=\"$lastPrice\"]")
            //it.tap("\uEA0A")
            it.tap(x=1010,y=675)

            it.tap("1D")
            it.tap("3M")
            it.tap("6M")
            it.tap("1Y")
            it.tap("2Y")

            it.tap(x=546,y=587)
                .clearInput()
                .sendKeys(desiredPriceWithCommas)

            it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

            it.tap("Done")

            if(stockCode == "MB"){
                val cleanDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
                desiredPriceWithCommas = String.format("%,.2f", cleanDesiredPrice)
            }

            it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")

        } else {

            //Just enter desired price
            it.tap("$lastPriceWithComma&&.android.widget.EditText")
                .clearInput()
                .sendKeys(desiredPriceWithCommas)

            it.exist(desiredPriceWithCommas)
        }


        val desiredQuantityWithComma = formatNumberWithComma(desiredQuantity)
        val minQuantityWithComma = formatNumberWithComma(minQuantity)

        //Buy in Shares
        if (buyInShares) {
            it.tap("xpath=//android.widget.EditText[@text=\"$minQuantityWithComma\"]")
                .clearInput()
                .sendKeys(desiredQuantityWithComma)

            it.exist(desiredQuantityWithComma)

            //Exact
            val totalAmount = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
//            val totalAmountWithCommas = formatNumberWithCommaPeriod(totalAmount)
//
//            it.exist("₱ $totalAmountWithCommas")

            //Close
            var totalPriceInt =  formatNumberWithComma((totalAmount .toDouble().toInt()).toString())
            var targetNumber = totalPriceInt.replace(",", "").toInt()
            var upperBound = targetNumber + 1
            var upperBoundFormatted = formatNumberWithComma(upperBound.toString())

            var totalPriceClose = ""
            try{
                totalPriceClose = it.select("*$totalPriceInt*").text
            } catch (e: Exception) {
                // Handle the exception
                totalPriceClose = it.select("*$upperBoundFormatted*").text
            }

            var totalPriceCloseText = totalPriceClose
            var originalTotalPriceComma = formatNumberWithCommaPeriod(totalAmount)

            var (checkValuesMatch,diff_fees) = areStringsClose(totalPriceCloseText, originalTotalPriceComma)

            if(checkValuesMatch && isBuy){
                println("Price are close")
                println("Price with Extra Fees $totalPriceCloseText")
                println("Original Total Price $originalTotalPriceComma")
            } else {
                println("Fail")
                println("Price with Extra Fees $totalPriceCloseText")
                println("Original Total Price $originalTotalPriceComma")
                it.exist("₱ $originalTotalPriceComma")
            }


        } else {

            //If in pesos

            val desiredPesosWithCommas = formatNumberWithCommaPeriod(desiredPesos)
            val currentTotal = totalAmountComputation(desiredPrice, minQuantity, broker, isBuy)
            val currentTotalWithCommas = formatNumberWithCommaPeriod(currentTotal)

            it.exist("₱ $currentTotalWithCommas")

            it.tap("Buy in")

            if(isBuy){
                it.exist("Buy in")

                it.exist("Shares")
                it.exist("Input by quantity")

                it.exist("Pesos")
                it.exist("Input by amount")

                it.tap("Pesos")

                it.exist("Pesos")
            }

            it.exist("xpath=//android.widget.EditText[@text=\"$currentTotalWithCommas\"]")
            val quantity = getQuantity(desiredPrice, minQuantity, currentTotal, orderType)
            val quantityWithComma = formatNumberWithComma(quantity)

            it.exist("$quantityWithComma shares (₱ $currentTotalWithCommas)")

            it.tap("xpath=//android.widget.EditText[@text=\"$currentTotalWithCommas\"]")
                .clearInput()
                .sendKeys(desiredPesosWithCommas)

            it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithCommas\"]")
            it.hideKeyboard()
            it.tap("Price")

//            val desiredPesosWithNoDecimal = removeDecimalAndAddZeros(desiredPesos)
//            val desiredPesosWithNoDecimalComma = formatNumberWithCommaPeriod(desiredPesosWithNoDecimal)
//
//            it.exist("xpath=//android.widget.EditText[@text=\"$desiredPesosWithNoDecimalComma\"]")
//

        }


        //Use Port Allocation
        if (usePortAllocate) {
            it.tap("Show port allocation")
            it.dontExist("Show port allocation")

            it.exist("Select allocation based on available cash")
            it.exist("Hide")
            it.exist("10%")
            it.exist("20%")
            it.exist("25%")
            it.exist("50%")

            //Force to two decimals
//            val cleanStringDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
//            desiredPriceWithCommas = String.format("%,.2f", cleanStringDesiredPrice )

            it.tap(desiredPriceWithCommas)

            //Tap 10%
            it.tap("10%")
            val (allocate10Amount,allocate10Quantity)  = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.10)

            val allocate10QuantityWithComma = formatNumberWithComma(allocate10Quantity)

            val result10 = feesComputation(desiredPrice, allocate10Quantity, broker, isBuy)
            var fees10 = ""

            if(isBuy){
                fees10 = (result10["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees10 = (result10["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total10 = (fees10.toDouble() + allocate10Amount.toDouble()).toString()
            println("Total 10%: $total10")
            val cleanString10 = formatNumberWithCommaPeriod(total10).replace(",", "").toDouble()
            val totalWithComma10 = String.format("%,.2f", cleanString10 )

            it.exist("₱ $totalWithComma10")
            it.exist(allocate10QuantityWithComma)

            //Tap 20%
            it.tap("20%")
            val (allocate20Amount,allocate20Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.20)

            val allocate20QuantityWithComma = formatNumberWithComma(allocate20Quantity)

            val result20 = feesComputation(desiredPrice, allocate20Quantity, broker, isBuy)
            var fees20 = ""

            if(isBuy){
                fees20 = (result20["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees20 = (result20["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total20 = (fees20.toDouble() + allocate20Amount.toDouble()).toString()
            println("Total 20%: $total20")
            val cleanString20 = formatNumberWithCommaPeriod(total20).replace(",", "").toDouble()
            val totalWithComma20 = String.format("%,.2f", cleanString20 )

            it.exist("₱ $totalWithComma20")
            it.exist(allocate20QuantityWithComma)

            //Tap 25%
            it.tap("25%")
            val (allocate25Amount,allocate25Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.25)

            val allocate25QuantityWithComma = formatNumberWithComma(allocate25Quantity)



            val result25 = feesComputation(desiredPrice, allocate25Quantity, broker, isBuy)
            var fees25 = ""

            if(isBuy){
                fees25 = (result25["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees25 = (result25["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total25 = (fees25.toDouble() + allocate25Amount.toDouble()).toString()
            println("Total 25%: $total25")
            val cleanString25 = formatNumberWithCommaPeriod(total25).replace(",", "").toDouble()
            val totalWithComma25 = String.format("%,.2f", cleanString25)

            it.exist("₱ $totalWithComma25")
            it.exist(allocate25QuantityWithComma)

            //Tap 50%
            it.tap("50%")
            val (allocate50Amount,allocate50Quantity) = percentAllocation(desiredPrice, minQuantity, walletBalance, 0.50)

            val allocate50QuantityWithComma = formatNumberWithComma(allocate50Quantity)

            val result50 = feesComputation(desiredPrice, allocate50Quantity, broker, isBuy)
            var fees50 = ""

            if(isBuy){
                fees50 = (result50["Buy Charges"] as? String ?: 0.0).toString()
            } else {
                fees50 = (result50["Sell Charges"] as? String ?: 0.0).toString()
            }

            val total50 = (fees50.toDouble() + allocate50Amount.toDouble()).toString()
            println("Total 50%: $total50")
            val cleanString50 = formatNumberWithCommaPeriod(total50).replace(",", "").toDouble()
            val totalWithComma50 = String.format("%,.2f", cleanString50)

            it.exist("₱ $totalWithComma50")
            it.exist(allocate50QuantityWithComma)

            it.tap("Hide")
            it.dontExist("Hide")
            it.exist("Show port allocation")

            it.tap(allocate50QuantityWithComma)
                .clearInput()
                .sendKeys(desiredQuantityWithComma)

            it.exist(desiredQuantityWithComma)
        }



    }

    @Macro("[View Available Cash]")
    fun viewAvailableCash(){

        it.tap("Available Cash")

        it.exist("CashIn")

        it.scrollUp()

        describe("Wait for Page to Load")
            .waitForDisplay("Select amount to cash in", waitSeconds = 60.0)

        it.exist("How would you like to cash in?")


        //Press Close Button
        //it.tap("\uE908")
        it.tap(x=60,y=202)



    }

    @Macro("[Preview Sell Order]")
    fun previewOrderSell(lastPrice: String, desiredPrice: String, desiredQuantity:String, minQuantity: String, amountTotal:String, fees:String, subTotal:String, walletBalance: String, orderType: String, isEndOfDay:Boolean, willAddNotes:Boolean, noteMessage:String, broker:String, isBuy: Boolean, previousTotal: Double, tradingHour: String, stockCode: String, cutlossPrice: String, subTotalCutlossPrice:String, feesCutlossPrice:String, cutlossAmountWithFees:String){
        it.wait(1)
        it.select("xpath=//android.widget.TextView[@text=\"Preview Order\"]").tap()

        val lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)

        it.exist("$stockCode: $lastPriceWithComma")

        it.exist("Order Summary")

        var quantityWithComma = formatNumberWithComma(desiredQuantity)
        var desiredPriceWithCommas = formatNumberWithCommaPeriod(desiredPrice)
        var cutlossPriceWithCommas = formatNumberWithCommaPeriod(cutlossPrice)

        if(stockCode == "MB"){
            val cleanDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
            desiredPriceWithCommas = String.format("%,.2f", cleanDesiredPrice)
        }

        //Fix this bug


        //Sell Shares
        if (orderType == "Conditional") {


            quantityWithComma = formatNumberWithComma((desiredQuantity.toDouble()).toString())


            it.exist("Selling $quantityWithComma shares @ $desiredPriceWithCommas target price")

            it.exist("Selling $quantityWithComma shares @ $cutlossPriceWithCommas cutloss price")

        } else {
            it.exist("Selling $quantityWithComma shares @ $desiredPriceWithCommas ")

        }


        var amountTotalWithComma = formatNumberWithCommaPeriod(amountTotal)


        //Fix this bug
        if(orderType == "Conditional"){


            if(minQuantity !== desiredQuantity){

                amountTotalWithComma = totalAmountComputation(desiredPrice, desiredQuantity, broker, isBuy)
                amountTotalWithComma = formatNumberWithCommaPeriod(amountTotalWithComma)
            }

        }


        it.exist("₱ $amountTotalWithComma")

        //Show Fees

        if(orderType == "Limit") {

            it.tap("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]")

            it.exist("Sub-Total")

            val cleanSubTotal = formatNumberWithCommaPeriod(subTotal).replace(",", "").toDouble()
            val subTotalWithCommas = String.format("%,.2f", cleanSubTotal)
            it.exist(subTotalWithCommas)



            val feesWithCommas = formatNumberWithCommaPeriod(fees)

            it.exist("Fees")
            it.exist(feesWithCommas)



            //Hide Fees

            it.tap("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]")
            it.dontExist("Sub-Total")
            it.dontExist(subTotalWithCommas)
            it.dontExist("Fees")
            it.dontExist(feesWithCommas)

        } else if (orderType == "Conditional"){

            //show fees target price
            it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[2]")

            it.exist("Sub-Total")

            val cleanSubTotalTargetPrice = formatNumberWithCommaPeriod(subTotal).replace(",", "").toDouble()
            val subTotalTargetPriceWithCommas = String.format("%,.2f", cleanSubTotalTargetPrice)

            it.exist(subTotalTargetPriceWithCommas)



            val feesTargetPriceWithCommas = formatNumberWithCommaPeriod(fees)

            it.exist("Fees")
            it.exist(feesTargetPriceWithCommas)



            //Hide Fees

            it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[2]")
            it.dontExist("Sub-Total")
            it.dontExist(subTotalTargetPriceWithCommas)
            it.dontExist("Fees")
            it.dontExist(feesTargetPriceWithCommas)

            //show fees cutloss price
            it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[3]")

            it.exist("Sub-Total")

            val cleanSubTotalCutlossPrice = formatNumberWithCommaPeriod(subTotalCutlossPrice).replace(",", "").toDouble()
            val subTotalCutlossPriceWithCommas = String.format("%,.2f", cleanSubTotalCutlossPrice)

            it.exist(subTotalCutlossPriceWithCommas)



            val feesCutlossPriceWithCommas = formatNumberWithCommaPeriod(feesCutlossPrice)

            it.exist("Fees")
            it.exist(feesCutlossPriceWithCommas)



            //Hide Fees

            it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[3]")
            it.dontExist("Sub-Total")
            it.dontExist(subTotalCutlossPriceWithCommas)
            it.dontExist("Fees")
            it.dontExist(feesCutlossPriceWithCommas)


        }

        it.exist("Shares on hand")
        //it.exist(shares)


        //Posting Type
        it.exist("Posting Type")
        it.exist(orderType)

        //Order Expiry
        it.exist("Order Expiry")

        if(isEndOfDay){
            it.exist("End of Day")
        } else {
            it.tap("End of Day")

            it.exist("Order Expiry")
            it.exist("End of Day")
            it.exist("GTD (Good Till Date)")

            it.exist("Until Cancelled")
            it.exist("GTC (Good Till Canceled)")

            it.tap("Until Cancelled")

            it.exist("Until Cancelled")

        }

        //Will Add Notes?
        if(willAddNotes){
            it.tap("+ Add")
            it.tap("Say something about your trade")
                .sendKeys(noteMessage)
        }

        //Submit Order

        it.tap("Submit Order")

    }


    @Macro("[Preview Order]")
    fun previewOrder(lastPrice: String, desiredPrice: String, desiredQuantity:String, minQuantity: String, amountTotal:String, fees:String, subTotal:String, walletBalance: String, orderType: String, isEndOfDay:Boolean, willAddNotes:Boolean, noteMessage:String, broker:String, isBuy: Boolean, previousTotal: Double, tradingHour: String, stockCode: String){
        it.tap("Preview Order")

        val lastPriceWithComma = formatNumberWithCommaPeriod(lastPrice)

        it.exist("$stockCode: $lastPriceWithComma")

        it.exist("Order Summary")

        var quantityWithComma = formatNumberWithComma(desiredQuantity)
        var desiredPriceWithCommas = formatNumberWithCommaPeriod(desiredPrice)

        if(stockCode == "MB"){
            val cleanDesiredPrice = desiredPriceWithCommas.replace(",", "").toDouble()
            desiredPriceWithCommas = String.format("%,.2f", cleanDesiredPrice)
        }

        //Fix this bug

        if(isBuy) {

            //Buy Shares
            if (orderType == "Conditional") {

                if (minQuantity !== desiredQuantity) {
                    quantityWithComma =
                        formatNumberWithComma((desiredQuantity.toDouble() - minQuantity.toDouble()).toString())
                }

                it.exist("Buying $quantityWithComma shares @ $desiredPriceWithCommas ${orderType.lowercase(Locale.getDefault())} price")

            } else {
                it.exist("Buying $quantityWithComma shares @ $desiredPriceWithCommas ${orderType.lowercase(Locale.getDefault())} price")

            }
        }

        var amountTotalWithComma = formatNumberWithCommaPeriod(amountTotal)


        //Fix this bug
        if(orderType == "Conditional"){

            val deductQuantity =  (desiredQuantity.toDouble()-minQuantity.toDouble()).toString()


            if(minQuantity !== desiredQuantity){

                amountTotalWithComma = totalAmountComputation(desiredPrice, deductQuantity, broker, isBuy)
                amountTotalWithComma = formatNumberWithCommaPeriod(amountTotalWithComma)
            }

        }

        describe("Wait for Page to Load")
            .waitForDisplay("₱ $amountTotalWithComma", waitSeconds = 30.0)


        it.exist("₱ $amountTotalWithComma")

        //Show Fees

        //it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[2]")
        it.tap(x=546,1592)

        describe("Wait for Toast Message")
            .waitForDisplay("Sub-Total")


        it.exist("Sub-Total")

        val cleanSubTotal = formatNumberWithCommaPeriod(subTotal).replace(",","").toDouble()
        val subTotalWithCommas = String.format("%,.2f", cleanSubTotal )
        //Uncomment after bug fix
        if(orderType !== "Conditional"){
            describe("Wait for Page to Load")
                .waitForDisplay(subTotalWithCommas, waitSeconds = 30.0)

            it.exist(subTotalWithCommas)
        }


        val feesWithCommas = formatNumberWithCommaPeriod(fees)

        it.exist("Fees")
        //Uncomment after bug fix
        if(orderType !== "Conditional"){
            it.exist(feesWithCommas)
        }


        //Hide Fees

        //it.tap("xpath=(//android.widget.TextView[@text=\"\uE902\"])[2]")
        it.tap(x=546,1592)

        it.wait(2)

        it.dontExist("Sub-Total")
        it.dontExist(subTotalWithCommas)
        it.dontExist("Fees")
        it.dontExist(feesWithCommas)

        //Available Cash
        it.exist("Available Cash")

        var walletWithFeesComma = "0.00"
        if(tradingHour == "Close" && previousTotal != 2.0){
            val walletClean = formatNumberWithCommaPeriod((walletBalance.toDouble()-previousTotal).toString()).replace(",", "").toDouble()
            walletWithFeesComma = String.format("%,.2f", walletClean )
        } else {
            val walletClean = formatNumberWithCommaPeriod(walletBalance.toDouble().toString()).replace(",", "").toDouble()
            walletWithFeesComma = String.format("%,.2f", walletClean )
        }

        it.exist("₱ $walletWithFeesComma")

        //Posting Type
        it.exist("Posting Type")
        it.exist(orderType)

        //Order Expiry
        it.exist("Order Expiry")

        if(isEndOfDay){
            it.exist("End of Day")
        } else {
            it.tap("End of Day")

            it.exist("Order Expiry")
            it.exist("End of Day")
            it.exist("GTD (Good Till Date)")

            it.exist("Until Cancelled")
            it.exist("GTC (Good Till Canceled)")

            it.tap("Until Cancelled")

            it.exist("Until Cancelled")

        }

        //Will Add Notes?
        if(willAddNotes){
            it.tap("+ Add")
            it.tap("Say something about your trade")
                .sendKeys(noteMessage)
        }

        //Submit Order

        it.tap("Submit Order")

    }

    fun getInvalidBoardLot(boardLot: Int): Int {
        val validBoardLots = listOf(
            1_000_000, 100_000, 10_000, 1_000, 100, 10, 5
        )

        val invalidBoardLots = listOf(
            500_000, 50_000, 5_000, 500, 50, 25, 2
        ).sorted()  // Sort the list in ascending order

        // Find the lowest invalid board lot that's not equal to the input board lot
        for (invalidLot in invalidBoardLots) {
            if (boardLot != invalidLot) {
                return invalidLot
            }
        }

        // If all invalid lots are equal to the input (highly unlikely), return the lowest valid lot minus 1
        return (validBoardLots.minOrNull() ?: 5) - 1
    }

    @Macro("[Get Board Lot]")
    fun getBoardLot(price: Double): Int {
            return when {
                price in 0.0001..0.0099 -> 1_000_000
                price in 0.010..0.049 -> 100_000
                price in 0.050..0.249 -> 10_000
                price in 0.250..0.495 -> 10_000
                price in 0.50..4.99 -> 1_000
                price in 5.00..9.99 -> 100
                price in 10.00..19.98 -> 100
                price in 20.00..49.95 -> 100
                price in 50.00..99.95 -> 10
                price in 100.00..199.90 -> 10
                price in 200.00..499.80 -> 10
                price in 500.00..999.50 -> 10
                price in 1_000.00..1_999.00 -> 5
                price in 2_000.00..4_998.00 -> 5
                price >= 5_000.00 -> 5
                else -> throw IllegalArgumentException("Price out of range")
            }

    }

    @Macro("[Enter Trading Pin Trade]")
    fun enterTradingPinTrade(pin: String){

        it.exist("Enter Trading Pin")
        it.exist("Kindly enter your 6-digit trading PIN")
        it.exist( "Forgot PIN? Get help here")


        pin.forEachIndexed { index, char ->
            it.tap("$char")
        }



    }

    @Macro("[Wallet Balance Test]")
    fun walletBalanceTest(desiredPrice: String, minQuantity: String, walletBalance: String, desiredPesos: String){

        //will return the next total cost that will exceed the current wallet balance
        val (totalCost, overQuantity) = overBalanceCost(desiredPrice, minQuantity, walletBalance, 1.00)

        val minQuantityWithComma = formatNumberWithComma(minQuantity)
        val overQuantityWithComma = formatNumberWithComma(overQuantity)
        val desiredPesosWithComma = formatNumberWithCommaPeriod(desiredPesos)
        val totalCostWithComma = formatNumberWithCommaPeriod(totalCost)

        val desiredPesosWithNoDecimal = removeDecimalAndAddZeros(desiredPesos)
        val desiredPesosWithNoDecimalComma = formatNumberWithCommaPeriod(desiredPesosWithNoDecimal)


        it.tap(x=520,y=1692)
            .clearInput()
            .sendKeys(totalCostWithComma)
        it.exist("xpath=//android.widget.EditText[@text=\"$totalCostWithComma\"]")

        it.tap("Preview Order")



        describe("Wait for Page to Load")
            .waitForDisplay("Order is invalid. Amount must be less than or equal Available Cash.", waitSeconds = 30.0)


        // Buy in shares again
        it.tap("Buy in")
        it.tap("Shares")
        it.exist("Shares")

        it.exist("xpath=//android.widget.EditText[@text=\"$overQuantityWithComma\"]")

        it.tap("Preview Order")



        describe("Wait for Page to Load")
            .waitForDisplay("Order is invalid. Amount must be less than or equal Available Cash.", waitSeconds = 30.0)

        it.tap("xpath=//android.widget.EditText[@text=\"$totalCostWithComma\"]")
            .clearInput()
            .sendKeys(desiredPesosWithComma)

        it.exist("xpath=//android.widget.EditText[@text=\"$desiredPesosWithComma\"]")

    }

    @Macro("[Test Tick Size and Board Lot]")
    fun testTickSize(desiredPrice: String, desiredQuantity: String, tickSize: String, minQuantity: String, tradingHour: String){

        val desiredPriceWithComma = formatNumberWithCommaPeriod(desiredPrice)
        val invalidTickSize = getInvalidTickSize(tickSize.toDouble())
        println("Invalid Tick Size: $invalidTickSize")
        println("Desired Price: $desiredPrice")

        val total = (invalidTickSize + desiredPrice.toDouble()).toString()
        println("Total: $total")
        val totalWithComma = formatNumberWithCommaPeriod(total)

        val desiredQuantityWithComma = formatNumberWithComma(desiredQuantity)
        val invalidQuantity = getInvalidBoardLot(desiredQuantity.toInt()).toString()
        println("Invalid Quantity: $invalidQuantity")
        val invalidQuantityWithComma = formatNumberWithComma(invalidQuantity)

        println("Preview Order 3")


        it.tap("xpath=//android.widget.EditText[@text=\"$desiredPriceWithComma\"]")
            .clearInput()
            .sendKeys(totalWithComma)

        it.exist("xpath=//android.widget.EditText[@text=\"$totalWithComma\"]")

        it.tap("Preview Order")

//        if(tradingHour != "Runoff" && !tradingHour.contains("Open") && !tradingHour.contains("Pre") ){
//            describe("Wait for Page to Load")
//                .waitForDisplay("Invalid Price. Please follow the stock price fluctuation. Price should be divisible by $tickSize", waitSeconds = 30.0)
//
//        }
//
//        if(tradingHour == "Runoff"){
//            describe("Wait for Page to Load")
//                .waitForDisplay("Order is invalid. Only accepting orders using the current Price.", waitSeconds = 30.0)
//        }

        it.tap("xpath=//android.widget.EditText[@text=\"$totalWithComma\"]")
            .clearInput()
            .sendKeys(desiredPriceWithComma)

        it.exist("xpath=//android.widget.EditText[@text=\"$desiredPriceWithComma\"]")

        it.tap("xpath=//android.widget.EditText[@text=\"$desiredQuantityWithComma\"]")
            .clearInput()
            .sendKeys(invalidQuantityWithComma)

        it.exist("xpath=//android.widget.EditText[@text=\"$invalidQuantityWithComma\"]")

        it.tap("Preview Order")

        if(tradingHour != "Runoff" && !tradingHour.contains("Open") && !tradingHour.contains("Pre") ){
            describe("Wait for Page to Load")
                .waitForDisplay("Invalid Quantity. Please follow the boardlot. Quantity should be divisible by $minQuantity.", waitSeconds = 30.0)

        }

        if(tradingHour == "Runoff"){
            describe("Wait for Page to Load")
                .waitForDisplay("Order is invalid. Only accepting orders using the current Price.", waitSeconds = 30.0)
        }

        it.tap("xpath=//android.widget.EditText[@text=\"$invalidQuantityWithComma\"]")
            .clearInput()
            .sendKeys(desiredQuantityWithComma)

        it.exist("xpath=//android.widget.EditText[@text=\"$desiredQuantityWithComma\"]")


    }

    @Macro("[Explore Asset Page]")
    fun exploreAssetPage(stockCode: String, hasStocks:Boolean){


        // Change Lot Type
        describe("Wait for Page to Load")
            .waitForDisplay("Normal lot")

        it.exist("Normal lot")


        it.tap("Normal lot")

        it.exist("Lot Type")
        it.exist("Normal lot")
        it.exist("Odd lot")

        describe("Wait for Page to Load")
            .waitForDisplay("Odd lot")

        it.wait(1)
        it.select("Odd lot").tap()

        describe("Wait for Page to Load")
            .waitForDisplay("Odd lot")
        it.exist("Odd lot")
        it.dontExist("Normal lot")

        it.tap("Odd lot")

        it.exist("Lot Type")
        it.exist("Normal lot")
        it.exist("Odd lot")

        it.tap("Normal lot")

        describe("Wait for Page to Load")
            .waitForDisplay("Normal lot")
        it.exist("Normal lot")
        it.dontExist("Odd lot")

        //Tap Date Ranges
        it.exist("1d")
        it.exist("3m")
        it.exist("6m")
        it.exist("1y")
        it.exist("2y")

        //Line Chart

        it.tap(x=679,y=1189)

        //Normal Chart

        it.tap(x=757, y=1189)

        //Bid and Ask

        it.exist("Bid And Ask")
        it.exist("Buy")
        it.exist("Sell")

        it.tap("Bid And Ask")

        it.exist("Volume")
        it.exist("Bid")
        it.exist("Ask")

        it.scrollDown()


        var rowCountBuyer = 0
        var rowCountSeller = 0
        var isEmptyBuyer = true
        var isEmptySeller = false

        // Path to your CSV file created in Python
        var csvFileBuyer = File("src/test/resources/$stockCode"+"_Stock_Stats_Buyer.csv")
        var csvFileSeller = File("src/test/resources/$stockCode"+"_Stock_Stats_Seller.csv")

        BufferedReader(csvFileBuyer.reader()).use { reader ->
            // Read the first line
            val firstLine = reader.readLine()

            // Check if the first cell contains "Empty Bid Sellers"
            if (firstLine?.contains("Empty Bid Buyers") == true) {
                println("CSV File Empty Bid Ask Buyer")
                //it.exist("There's no Bid and Ask")

            } else {

                isEmptyBuyer = false

                println("CSV File not Empty Bid Ask Buyer")

                it.dontExist("There's no Bid and Ask")

                // Read CSV file
                var csvReader = CsvReader()
                var csvData = csvReader.readAll(csvFileBuyer)

                rowCountBuyer = csvData.size

                // Print header
                println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

                // Print data rows
                csvData.drop(1).forEachIndexed { index, row ->
                    println("Row ${index + 1}: ${row.joinToString(", ")}")
                }

                csvData.drop(1).take(5).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")

                    // Check if the row has at least 3 elements
                    if (row.size >= 3) {
                        val count = row[0]
                        val volume = row[1]
                        val price = row[2]

                        println("Count: $count")
                        it.exist(count)
                        println("Volume: $volume")
                        it.exist(volume)
                        println("Prce: $price")
                        it.exist(price)


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }


            }
        }

        BufferedReader(csvFileSeller.reader()).use { reader ->
            // Read the first line
            val firstLine = reader.readLine()

            // Check if the first cell contains "Empty Bid Sellers"
            if (firstLine?.contains("Empty Bid Sellers") == true) {
                println("CSV File Empty Bid Ask Seller")

            } else {

                isEmptySeller = false

                println("CSV File not Empty Bid Ask Buyer")

                it.dontExist("There's no Bid and Ask")

                // Read CSV file
                var csvReader = CsvReader()
                var csvData = csvReader.readAll(csvFileSeller)

                rowCountSeller = csvData.size

                // Print header
                println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

                // Print data rows
                csvData.drop(1).forEachIndexed { index, row ->
                    println("Row ${index + 1}: ${row.joinToString(", ")}")
                }

                csvData.drop(1).take(5).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")

                    // Check if the row has at least 3 elements
                    if (row.size >= 3) {
                        var count = row[0]
                        val volume = row[1]
                        val price = row[2]

                        count = count.replace("(","").replace(")","")

                        println("Count: $count")
                        println("Volume: $volume")
                        println("Price: $price")

                        println("Count: $count")
                        it.exist(count)
                        println("Volume: $volume")
                        it.exist(volume)
                        println("Prce: $price")
                        it.exist(price)


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }


            }
        }

        if(isEmptyBuyer && isEmptySeller){
            it.exist("There's no Bid and Ask")
        }


        if(rowCountBuyer > 5 || rowCountSeller > 5){
            it.exist("View All")
            it.tap("View All")

            it.exist("Bid And Ask")
            it.exist("Volume")
            it.exist("Bid")
            it.exist("Ask")

            var csvReader = CsvReader()
            var csvData = csvReader.readAll(csvFileBuyer)

            // Print header
            println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

            // Print data rows
            csvData.drop(1).forEachIndexed { index, row ->
                println("Row ${index + 1}: ${row.joinToString(", ")}")
            }

            csvData.drop(1).forEachIndexed { index, row ->

                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 3) {
                    val count = row[0]
                    val volume = row[1]
                    val price = row[2]


                    it.exist(count)
                    it.exist(volume)
                    it.exist(price)

                    println("Count: $count")
                    println("Volume: $volume")
                    println("Price: $price")

                } else {
                    println("Row does not have enough elements")
                }

                println("------------------------")


            }

            csvReader = CsvReader()
            csvData = csvReader.readAll(csvFileSeller)

            // Print header
            println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

            // Print data rows
            csvData.drop(1).forEachIndexed { index, row ->
                println("Row ${index + 1}: ${row.joinToString(", ")}")
            }

            var countLoop = 1

            csvData.drop(1).forEachIndexed { index, row ->

                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 3) {
                    var count = row[0]
                    val volume = row[1]
                    val price = row[2]

                    count = count.replace("(","").replace(")","")

                    if(countLoop >=16){
                        it.selectWithScrollDown(price)
                    }

                    println("Count: $count")
                    it.exist(count)
                    println("Volume: $volume")
                    it.exist(volume)
                    println("Prce: $price")
                    it.exist(price)

                    countLoop +=1

                } else {
                    println("Row does not have enough elements")
                }

                println("------------------------")


            }

            it.wait(1)

            //go back
            it.tap(x=64,y=210)


        }

        describe("Wait for Page to Load")
            .waitForDisplay("Buy", waitSeconds = 30.0)

        it.exist("Buy")
        it.exist("Sell")


        it.wait(1)

        describe("Wait for Page to Load")
            .waitForDisplay("Transactions", waitSeconds = 30.0)

        //Transactions
        it.exist("Transactions")
        it.select("Transactions").tap()

        it.scrollDown()

        it.exist("Time")
        it.exist("Price")
        it.exist("Vol")
        it.exist("Buyer")
        it.exist("Seller")

        var csvFileTransaction = File("src/test/resources/$stockCode"+"_Transactions_Tab.csv")

        var isEmptyTransaction = true
        var rowCountTransaction = 0

        BufferedReader(csvFileTransaction.reader()).use { reader ->
            // Read the first line
            val firstLine = reader.readLine()

            // Check if the first cell contains "Empty Bid Sellers"
            if (firstLine?.contains("Empty Transactions") == true) {
                println("CSV File Empty Transactions")
                it.exist("There's no transaction")

            } else {

                isEmptyTransaction = false

                println("CSV File not Transactions")

                it.dontExist("There's no Bid and Ask")

                // Read CSV file
                var csvReader = CsvReader()
                var csvData = csvReader.readAll(csvFileTransaction)

                rowCountTransaction = csvData.size

                // Print header
                println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

                // Print data rows
                csvData.drop(1).forEachIndexed { index, row ->
                    println("Row ${index + 1}: ${row.joinToString(", ")}")
                }

                csvData.drop(1).take(5).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")

                    // Check if the row has at least 3 elements
                    if (row.size >= 6) {
                        val time = row[1]
                        val price = row[2]
                        val volume = row[3]
                        val buyer = row[4]
                        val seller = row[5]

                        it.exist(time)
                        it.exist(price)
                        it.exist(volume)
                        it.exist("*$buyer*")
                        it.exist("*$seller*")

                        println("Time: $time")
                        println("Price: $price")
                        println("Volume: $volume")
                        println("Buyer: $buyer")
                        println("Seller: $seller")

                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }


            }
        }

        if(rowCountTransaction > 6){

            it.exist("View All")
            it.tap("View All")

            it.exist("Time")
            it.exist("Price")
            it.exist("Vol")
            it.exist("Buyer")
            it.exist("Seller")

            var csvReader = CsvReader()
            var csvData = csvReader.readAll(csvFileTransaction)

            // Print header
            println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

            // Print data rows
            csvData.drop(1).forEachIndexed { index, row ->
                println("Row ${index + 1}: ${row.joinToString(", ")}")
            }

            csvData.drop(1).forEachIndexed { index, row ->

                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 6) {
                    val time = row[1]
                    val price = row[2]
                    val volume = row[3]
                    val buyer = row[4]
                    val seller = row[5]

                    it.exist(time)
                    it.exist(price)
                    it.exist(volume)
                    it.exist("*$buyer*")
                    it.exist(seller)

                    println("Time: $time")
                    println("Price: $price")
                    println("Volume: $volume")
                    println("Buyer: $buyer")
                    println("Seller: $seller")

                } else {
                    println("Row does not have enough elements")
                }

                println("------------------------")


            }

            it.wait(1)

            //go back
            it.tap(x=101,y=344)

        }


        it.exist("Buy")
        it.exist("Sell")

        //Statistics
        it.tap("Statistics")
        val csvFile = File("src/test/resources/Stock_Stats.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }


        try {
            val csvFile = File("src/test/resources/Stock_Stats.csv")

            // Read all lines from the CSV file
            val lines = csvFile.readLines()

            // Skip the header row and find the row for the given stock code
            val stockRow = lines.drop(1).find { it.split(",")[0] == stockCode }

            // If the stock is not found, return null
            if (stockRow == null) {
                println("Stock code $stockCode not found.")

            }

            // Split the row into columns
            val columns = stockRow?.split(",")

            // Check if the row has enough elements
            if (columns != null) {
                if (columns.size < 15) {
                    println("Row for stock code $stockCode does not have enough elements.")

                }
            }

            // Assign values to variables
            val current_price = columns?.get(1)
            val pclose = columns?.get(3)
            val low = columns?.get(4)
            val volume = columns?.get(5)
            val f52wk_low = columns?.get(6)
            val market_cap = columns?.get(7)
            val nfbs = columns?.get(8)
            val open = columns?.get(9)
            val high = columns?.get(10)
            val shares = columns?.get(11)
            val f52wk_high = columns?.get(12)
            val trades = columns?.get(13)
            val avg_price = columns?.get(14)

            println("P Close: $pclose")
            it.exist("₱$current_price")

            it.exist("P. Close")
            if (pclose != null) {
                it.exist(pclose)
            }

            println("Low: $low")
            it.exist("Low")
            if (low != null) {
                it.exist(low)
            }

            println("Volume: $volume")
            it.exist("Volume")
            if (volume != null) {
                it.exist(volume)
            }

            println("52 Week Low: $f52wk_low")
            it.exist("52wk Low")
            if (f52wk_low != null) {
                it.exist(f52wk_low)
            }

            println("Market cap: $market_cap")
            it.exist("Market Cap")
            if (market_cap != null) {
                it.exist(market_cap)
            }

            println("NFB/S: $nfbs")
            it.exist("NFB/S")
            if (nfbs != null) {
                it.exist(nfbs)
            }

            println("Open: $open")
            it.exist("Open")
            if (open != null) {
                it.exist(open)
            }

            println("High: $high")
            it.exist("High")
            if (high != null) {
                it.exist(high)
            }

            println("Shares: $shares")
            it.exist("Shares")
            if (shares != null) {
                it.exist(shares)
            }

            println("52 Week High: $f52wk_high")
            it.exist("52wk High")
            if (f52wk_high != null) {
                it.exist(f52wk_high)
            }

            println("Trades: $trades")
            it.exist("Trades")
            if (trades != null) {
                it.exist(trades)
            }

            println("Average Price: $avg_price")
            it.exist("Avg. Price")
            if (avg_price != null) {
                it.exist(avg_price)
            }


        } catch (e: Exception) {
            println("An error occurred: ${e.message}")

        }

        //Portfolio
        it.tap("My Investment")

        it.scrollDown()

        it.exist("Market Value")
        it.exist("Profit")
        it.exist("Avg. Price")
        it.exist("Shares")
        it.exist("Total Cost")

        it.select("My Investment")
            .swipeToLeft()

        //Orders
        it.tap("Orders")

        it.scrollDown()

        describe("Wait for Page to Load")
            .waitForDisplay("*Pending*", waitSeconds = 30.0)
        it.exist("*Pending*")
        it.exist("*Posted*")
        it.exist("*Completed*")
        it.exist("*Cancelled*")

        if(hasStocks){

            it.exist("*Pending*")
            it.selectWithScrollDown("*Posted*")
            it.exist("*Posted*")
            it.selectWithScrollDown("*Completed*")
            it.exist("*Completed*")
            it.selectWithScrollDown("*Cancelled*")
            it.exist("*Cancelled*")

        } else {

//            it.exist("*Pending*")
//            it.tap("*Pending*")
//            it.exist("You have no orders to send")
//            it.exist("These are orders that have not yet been sent to the exchange")
//            it.tap("*Pending*")
//            it.dontExist("You have no orders to send")
//            it.dontExist("These are orders that have not yet been sent to the exchange")
//
//            it.selectWithScrollDown("*Posted*")
//            it.tap("*Posted*")
//            it.exist("You have no orders to receive")
//            it.selectWithScrollDown("These are orders that have been sent to the exchange but is not yet matched")
//            it.exist("These are orders that have been sent to the exchange but is not yet matched")
//            it.exist("*Posted*")
//            it.tap("*Posted*")
//            it.dontExist("You have no orders to receive")
//            it.dontExist("These are orders that have been sent to the exchange but is not yet matched")
//
//            it.selectWithScrollDown("*Completed*")
//            it.exist("*Completed*")
//            it.tap("*Completed*")
//            it.selectWithScrollDown("These are orders that have been partially or completely matched by the exchange")
//            it.exist("You have no delivered orders")
//            it.exist("These are orders that have been partially or completely matched by the exchange")
//            it.tap("Completed*")
//            it.dontExist("You have no delivered orders")
//            it.dontExist("These are orders that have been partially or completely matched by the exchange")
//
//
//            it.selectWithScrollDown("*Cancelled*")
//            it.exist("*Cancelled*")
//            it.tap("*Cancelled*")
//            it.selectWithScrollDown("To cancel a Posted or Pending order, click on the stock that you want to cancel and click the Cancel Order button")
//            it.exist("You have no cancelled orders")
//            it.exist("To cancel a Posted or Pending order, click on the stock that you want to cancel and click the Cancel Order button")
//            it.tap("*Cancelled*")
//            it.dontExist("You have no cancelled orders")
//            it.dontExist("To cancel a Posted or Pending order, click on the stock that you want to cancel and click the Cancel Order button")

        }

        //Dividends

        it.wait(1)

        it.tap("xpath=//android.widget.TextView[@text=\"Dividends\"]")

//        it.wait(3)
//        it.exist("xpath=(//android.widget.TextView[@text=\"Dividends\"])[2]")

        describe("Wait for Page to Load")
            .waitForDisplay("Dividend Yield", waitSeconds = 30.0)

        it.exist("Dividend Yield")
        it.exist("Trailing 12 months")
        it.exist("*%*")
        it.exist("What does Ex-Dividend Date means?")

        it.tap("What does Ex-Dividend Date means?")
        it.exist("It is a day when a stock is sold without the buyer being eligible to receive the next dividend payment. Dividends are payments made by a company to its shareholders, and this date can affect the stock’s value and how much money an investor makes from owning it.")

        it.tap("What does Ex-Dividend Date means?")
        it.dontExist("It is a day when a stock is sold without the buyer being eligible to receive the next dividend payment. Dividends are payments made by a company to its shareholders, and this date can affect the stock’s value and how much money an investor makes from owning it.")

        it.exist("Dividends Payout Schedule")

        try {

            it.scrollDown("Yield")

            it.exist("Ex-dividend date")
            it.exist("Payment Date")
            it.exist("Type")
            it.exist("Value")
            it.exist("Yield")

            println("Payment Schedule Exists")

        }  catch (e: Exception) {
            // Handle the exception

            it.scrollDown()

            it.dontExist("Ex-dividend date")
            it.dontExist("Payment Date")
            it.dontExist("Type")
            it.dontExist("Value")
            it.dontExist("Yield")

            println("Payment Schedule Doest not Exist")
        }

        it.scrollUp()

        // Open Buy

        it.tap("Buy")

        it.dontExist("Sell Order")

        it.exist("*$stockCode:*")
        it.exist("Buy Order")
        it.exist("Limit")
        it.exist("Price")
        it.exist("Select in Chart")
        it.exist("Quantity")
        it.exist("Buy in")
        it.exist("Shares")
        it.exist("*Shares on hand:*")
        it.exist("Show port allocation")
        it.exist("Total")
        it.exist("Available Cash")
        it.exist("Preview Order")

        //close
        it.tap(x=997,y=1074)

        it.wait(2)

//        it.dontExist("Buy Order")
//        it.dontExist("Buy in")

        // Open Sell

        it.tap("Sell")

        it.dontExist("Buy Order")

        it.exist("*$stockCode:*")
        it.exist("Sell Order")
        it.exist("Limit")
        it.exist("Price")
        it.exist("Select in Chart")
        it.exist("Quantity")
        it.exist("*Shares on hand:*")
        it.exist("Show port allocation")
        it.exist("Total")
        it.exist("Available Cash")
        it.exist("Preview Order")

        //close
        it.tap(x=997,y=1074)

        it.wait(2)

        //it.dontExist("Sell Order")


    }

    @Macro("[View Order Details]")
    fun viewOrderDetails(totalPrice: String, isBuy: Boolean, stockCode: String, isEndOfDay: Boolean, orderStatus:String, willAddNotes: Boolean, noteMessage: String, desiredPrice: String, desiredQuantity: String, subTotal: String, fees: String, diff_fees:String, diff_cutloss_fees: String ?=null, executedPrice: String?=null, executedQuantity: String?=null) :String {

        // Remove commas and parse to Int
        var cleanTotalPrice = formatNumberWithCommaPeriod((totalPrice.toDouble() + diff_fees.toDouble()).toString()).replace(",","").toDouble()
        val totalPriceText = String.format("%,.2f", cleanTotalPrice )

        it.tap(totalPriceText)

        if(isBuy){
            it.exist("Buy")
            //it.exist("\uE929")
        } else {
            it.exist("Sell")
            //it.exist("\uE928")
        }

        it.exist(stockCode)

        it.exist("*Ref. No.*")

        var referenceNumber = it.select("*Ref. No.*")
        var referenceNumberText = referenceNumber.text.replace(" | Limit","").replace("Ref. No. ","")

        var currentDate = getCurrentDateFormatted()
        it.exist("*Date Posted: $currentDate*")

        if(isEndOfDay){
            it.exist("Order Expiry: End of Day")
        } else {
            it.exist("Order Expiry: Until Cancelled")
        }

        if(orderStatus == "Rejected"){
            //it.exist("\uE91F")
            it.exist("Rejected")
        } else if (orderStatus == "Pending"){
            //it.exist("\uE926")
            it.exist("Pending")
        } else if(orderStatus == "Posted"){
            //it.exist("\uE926")
            it.exist("Posted")
        } else if(orderStatus == "Filled"){
            //it.exist("\uE905")
            it.exist("Filled")

            it.exist("Matched")
            it.exist("Posted")
        } else if (orderStatus == "Expired"){
            //it.exist("\uE905")
            it.exist("Expired")
        } else if (orderStatus == "Unplaced"){
            //it.exist("\uE926")
            it.exist("Pending")
        } else if (orderStatus == "Rejected Cancellation"){
            //it.exist("\uE91F")
            it.exist("Rejected")
        } else if (orderStatus == "Error"){
            //it.exist("\uE91F")
            it.exist("Rejected")
        } else if (orderStatus == "Partial"){
            //it.exist("\uE905")
            it.exist("Partial")

            it.exist("Matched")
            it.exist("Posted")
        }

        it.exist("View Execution History")
        //it.exist("\uE90C")


        val desiredPriceWithComma = formatNumberWithCommaPeriod(desiredPrice)
        val cleanSubTotal = formatNumberWithCommaPeriod(subTotal).replace(",","").toDouble()
        val subTotalWithComma = String.format("%,.2f", cleanSubTotal )
        val cleanFees = formatNumberWithCommaPeriod((fees.toDouble()+diff_fees.toDouble()).toString()).replace(",","").toDouble()
        val feesWithComma = String.format("%,.2f", cleanFees )
        val desiredQuantityWithComma = formatNumberWithComma(desiredQuantity)
        val executedQuantityWithComma = executedQuantity?.let { it1 -> formatNumberWithComma(it1) }



        if(orderStatus != "Partial" || orderStatus != "Filled"){

            it.exist("Price")
            it.exist(desiredPriceWithComma)
            it.exist("Shares")
            it.exist(desiredQuantityWithComma)

            it.exist("Sub-Total")
            it.exist(subTotalWithComma)
            it.exist("Fees")
            it.exist(feesWithComma)
            it.exist("Total Amount")

        } else {

            //Matched
            it.exist("Shares")
            if (executedQuantityWithComma != null) {
                it.exist(executedQuantityWithComma)
            }

            it.exist("Gross Amount")


            if(executedPrice != null && executedQuantity != null && executedPrice != "" && executedQuantity != null){
                val partialNetAmount = (executedQuantity.toDouble() * executedPrice.toDouble()).toString()
                it.exist(partialNetAmount)
            }


            //Fees
            it.exist("Fees Breakdown")
            it.exist("Broker Commission")
            it.exist("Broker Commission VAT")
            it.exist("PSE Fees & SEC Fees")
            it.exist("SCCP FEes")
            it.exist("Total Fees")
            it.exist("Net Amount")
            it.exist("Avg. Price")

        }


        it.exist("Notes")
        if(willAddNotes){
            it.exist(noteMessage)
        } else {
            it.exist("No notes available")
        }

        //view execution history

        it.tap("View Execution History")

        it.exist("Execution History")

        it.exist(stockCode)


        if(isBuy){

            //it.exist("\uE929")
        } else {

           // it.exist("\uE928")
        }

        //dates


        it.exist("(Ref No. $referenceNumberText)")

        if(isEndOfDay){
            it.exist("Created order of $desiredQuantityWithComma shares at $desiredPriceWithComma (End of Day)")
        } else {
            it.exist("Created order of $desiredQuantityWithComma shares at $desiredPriceWithComma (Until Cancelled)")
        }

        if(orderStatus == "Rejected"){
            //2nd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[2]")
            it.exist("Order Request Rejected")
            it.exist("*(PSE Time: *")

            //3rd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[1]")
            it.exist("Order Rejected")
            it.exist("(Ref No. $referenceNumberText)")
            //it.exist("reject test")
        } else if (orderStatus == "Posted"){

            //2nd dot
            it.exist("Order Posted")
            it.exist("*(PSE Time: *")


        } else if (orderStatus == "Filled") {

            //2nd dot
            it.exist("Bought $desiredQuantityWithComma at $desiredPriceWithComma")
            it.exist("*(PSE Time: *")

        } else if (orderStatus == "Cancelled"){

            //2nd dot
            it.exist("Order Cancelled")
            it.exist("*(PSE Time: *")
            it.exist("Order was cancelled successfully")

        } else if (orderStatus == "Unplaced"){

            //2nd dot
            it.exist("Order Unplaced")
            it.exist("*(PSE Time: *")

        } else if (orderStatus == "Rejected Cancellation"){

            //2nd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[2]")
            it.exist("Order Request Rejected")
            it.exist("*(PSE Time: *")

            //3rd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[1]")
            it.exist("Order Rejected")
            it.exist("(Ref No. $referenceNumberText)")
            it.exist("reject cancellation test")
        } else if (orderStatus == "Error"){

            //2nd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[2]")
            it.exist("Order Error")
            it.exist("*(PSE Time: *")

            //3rd dot

            //it.exist("xpath=(//android.widget.TextView[@text=\"(96): Your user id is suspended\"])[1]")
            it.exist("Order Rejected")
            it.exist("(Ref No. $referenceNumberText)")
            it.exist("error test")
        }

        //current Date in this format Aug 20
        val currentDateDetails = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM d")
        val formattedDate = currentDateDetails.format(formatter)
        it.exist(formattedDate.toString())

        //go back
        //it.tap("\uE909")
        it.tap(x=69,y=211)

        describe("Wait for Page to Load")
            .waitForDisplay("View Execution History", waitSeconds = 30.0)
        it.exist("View Execution History")

        //exit asset page
        //it.tap("\uE909")
        it.tap(x=69,y=211)

        return referenceNumberText
    }

    fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")
        return dateFormat.format(Date())
    }

    @Macro("[Check Orders Tab]")
    fun checkOrdersTab(currentTradingHour:String, stockCode: String, isBuy: Boolean, orderType: String, desiredQuantity: String, isEndOfDay: Boolean, totalPrice: String, desiredPrice: String , countLoop:Int, desiredPesos: String, orderStatus: String, willAddNotes: Boolean, noteMessage: String, subTotal: String,fees: String , tradingHour: String, walletBalance: String, previousTotal: Double, cutlossPrice: String? = null, cutlossTotalPrice:String ? = null): Pair<String,String> {

        //Tap portfolio icon
        it.tap("Orders")


        if(currentTradingHour == "Close"){
            it.scrollDown()
            it.wait(1)
            describe("Wait for Page to Load")
                .waitForDisplay("*Pending*", waitSeconds = 30.0)
            it.tap("*Pending*")
        } else if(currentTradingHour.contains("Pre-Close") && orderType == "Conditional"){
            it.scrollDown()
            it.wait(1)
            describe("Wait for Page to Load")
                .waitForDisplay("*Pending*", waitSeconds = 30.0)
            it.tap("*Pending*")
        } else if (currentTradingHour.contains("Runoff") && orderType == "Conditional") {
            it.scrollDown()
            it.wait(1)
            describe("Wait for Page to Load")
                .waitForDisplay("*Pending*", waitSeconds = 30.0)
            it.tap("*Pending*")
        } else {
            it.selectWithScrollDown("*Cancelled*")
            it.wait(1)
            describe("Wait for Page to Load")
                .waitForDisplay("*Cancelled*", waitSeconds = 30.0)
            it.tap("*Cancelled*")
        }

        //help scrolldown
        if(isEndOfDay){
            it.scrollDown("End of Day")
        } else {
            it.scrollDown("Until Cancelled")
        }

        if(isBuy){

            //it.exist("\uE929")
        } else {
            //it.exist("\uE928")
        }

        it.exist(stockCode)
        //it.exist(orderType)

        val sharesWithComma = formatNumberWithComma(desiredQuantity)

        it.exist("$sharesWithComma shares")

        if(isEndOfDay){
            it.exist("End of Day")
        } else {
            it.exist("Until Cancelled")
        }

        // Remove commas and parse to Int
        var totalPriceInt =  formatNumberWithComma((totalPrice.toDouble().toInt()).toString())
        var targetNumber = totalPriceInt.replace(",", "").toInt()
        var upperBound = targetNumber + 1
        var upperBoundFormatted = formatNumberWithComma(upperBound.toString())
        // Use the formatted strings in the selector
        var totalPriceClose = ""
        try{
            totalPriceClose = it.select("*$totalPriceInt*").text
        } catch (e: Exception) {
            // Handle the exception
            totalPriceClose = it.select("*$upperBoundFormatted*").text
        }

        var totalPriceCloseText = totalPriceClose
        var originalTotalPriceComma = formatNumberWithCommaPeriod(totalPrice)

        var diff_cutloss = ""
        var (checkValuesMatch,diff_fees) = areStringsClose(totalPriceCloseText, originalTotalPriceComma)

        if(checkValuesMatch && isBuy){
            println("Price are close")
            println("Price with Extra Fees $totalPriceCloseText")
            println("Original Total Price $originalTotalPriceComma")
        } else {
            println("Fail")
            println("Price with Extra Fees $totalPriceCloseText")
            println("Original Total Price $originalTotalPriceComma")
            it.exist(originalTotalPriceComma)
        }

        var cutlossPriceWithComma = ""
        val desiredPriceWithComma = formatNumberWithCommaPeriod(desiredPrice)
        if(cutlossPrice != null){
            cutlossPriceWithComma = formatNumberWithCommaPeriod(cutlossPrice)
        }

        if(orderType == "Limit"){
            it.exist("Price: $desiredPriceWithComma")

        } else if (orderType == "Conditional" && !isBuy) {
            it.exist("Target Price: $desiredPriceWithComma")
            //up arrow
            //it.exist("\uE930")

            it.exist("Cutloss Price: $cutlossPriceWithComma")
            //down ar row
            //it.exist("\uE931")

            var originalcutlossTotalPriceComma = cutlossTotalPrice?.let { it1 -> formatNumberWithCommaPeriod(it1) }.toString()
            it.exist(originalcutlossTotalPriceComma)

        }

        var noViewAllTransactions:Boolean

        try {
            it.exist("View All")
            noViewAllTransactions = true

        }  catch (e: Exception) {
            // Handle the exception
            noViewAllTransactions = false
        }

        if(noViewAllTransactions){
            it.tap("View All")

            if(currentTradingHour == "Close"){
                it.exist("Pending")
            } else {
                it.exist("Cancelled")
            }

            //go back
            //it.tap("\uE909")
            it.tap(x=69,y=211)
        }

        viewOrderDetails(
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
            diff_cutloss,
        )

        //tap back button Orders Menu Tab
        //it.tap("\uE944")
        it.tap(x=69,y=211)

        describe("My Investment").waitForDisplay("My Investment", waitSeconds = 30.0)

        //Tap my investment icon
        it.tap("My Investment")

        //check wallet balance

        var walletWithFeesComma = "0.00"
        if(tradingHour == "Close"){

            if(isBuy) {
                val walletClean =
                    formatNumberWithCommaPeriod((walletBalance.toDouble() - previousTotal).toString()).replace(",", "")
                        .toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean)
            } else {
                val walletClean =
                    formatNumberWithCommaPeriod((walletBalance.toDouble() - previousTotal).toString()).replace(",", "")
                        .toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean)
            }
        } else {
            if(isBuy) {
                val walletClean =
                    formatNumberWithCommaPeriod(walletBalance.toDouble().toString()).replace(",", "").toDouble()
                walletWithFeesComma = String.format("%,.2f", walletClean)
            }
        }

        if(countLoop == 1){
            it.tap("Orders")
        }



        println("Trading Hour $currentTradingHour")
        println("Count Loop $countLoop")


        it.wait(1)

        if(currentTradingHour == "Close" && countLoop == 1){
            //it.select("*Pending*").tap()
        }  else if(currentTradingHour.contains("Pre-Close") && orderType == "Conditional"){
            //do something
        }  else if (currentTradingHour != "Close"  && countLoop == 1) {
            it.scrollDown()
            it.scrollDown()

            it.wait(1)
            it.tap(x=165,y=2033)

            var cleanTotalPrice = formatNumberWithCommaPeriod((totalPrice.toDouble() + diff_fees.toDouble()).toString()).replace(",","").toDouble()
            val totalPriceText = String.format("%,.2f", cleanTotalPrice )

            it.selectWithScrollDown(totalPriceText)
        }



        if(isBuy){
            //it.exist("\uE929")
        } else {
            //it.exist("\uE928")
        }

        it.exist(stockCode)
        //it.exist(orderType)

        it.exist("$sharesWithComma shares")

        if(isEndOfDay){
            it.exist("End of Day")
        } else {
            it.exist("Until Cancelled")
        }

        // Remove commas and parse to Int
        totalPriceInt =  formatNumberWithComma((totalPrice.toDouble().toInt()).toString())
        targetNumber = totalPriceInt.replace(",", "").toInt()
        upperBound = targetNumber + 1
        upperBoundFormatted = formatNumberWithComma(upperBound.toString())
        // Use the formatted strings in the selector
        totalPriceClose = ""
        try{
            totalPriceClose = it.select("*$totalPriceInt*").text
        } catch (e: Exception) {
            // Handle the exception
            totalPriceClose = it.select("*$upperBoundFormatted*").text
        }
        totalPriceCloseText = totalPriceClose
        originalTotalPriceComma = formatNumberWithCommaPeriod(totalPrice)


        var (checkValuesMatch1,diff_fees1) = areStringsClose(totalPriceCloseText, originalTotalPriceComma)

        if(checkValuesMatch1 as Boolean){
            println("Price are close")
            println("Price with Extra Fees $totalPriceCloseText")
            println("Original Total Price $originalTotalPriceComma")
        } else {
            println("Fail")
            println("Price with Extra Fees $totalPriceCloseText")
            println("Original Total Price $originalTotalPriceComma")
            it.exist(originalTotalPriceComma)
        }

        if(orderType == "Limit") {
            it.exist("Price: $desiredPriceWithComma")
        }  else if (orderType == "Conditional" && !isBuy) {
            it.exist("Target Price: $desiredPriceWithComma")
            //up arrow
            //it.exist("\uE930")

            it.exist("Cutloss Price: $cutlossPriceWithComma")
            //down ar row
            //it.exist("\uE931")

            it.exist("Target Price: $desiredPriceWithComma")
            //up arrow
            //it.exist("\uE930")

            it.exist("Cutloss Price: $cutlossPriceWithComma")
            //down ar row
            //it.exist("\uE931")

            var originalcutlossTotalPriceComma = cutlossTotalPrice?.let { it1 -> formatNumberWithCommaPeriod(it1) }.toString()
            it.exist(originalcutlossTotalPriceComma)
        }

        try {
            it.exist("View All")
            noViewAllTransactions = true

        }  catch (e: Exception) {
            // Handle the exception
            noViewAllTransactions = false
        }

        if(noViewAllTransactions){
            it.tap(x=537,y=2060)

            if(currentTradingHour == "Close"){

                describe("Pending").waitForDisplay("Pending", waitSeconds = 30.0)

                it.exist("Pending")
            } else {
                describe("Cancelled").waitForDisplay("Cancelled", waitSeconds = 30.0)

                it.exist("Cancelled")
            }

            describe("Wait for Page to Load").waitForDisplay("\uE909", waitSeconds = 30.0)

            //go back
            //it.tap("\uE909")
            it.tap(x=69,y=211)
        }

        return Pair(diff_fees,diff_cutloss)


    }


    fun formatToKilo(number: String): String {
        return if (number.toInt() >= 1000) {
            "${number.toInt() / 1000}K"
        } else {
            number
        }
    }

    val stockMap = mapOf(
        "BDO" to "BDO Unibank, Inc.",
        "AGI" to "Alliance Global Group, Inc.",
        //"CEB" to "Cebu Air, Inc.",
        "DMC" to "DMCI Holdings, Inc.",
        "EMP" to "Emperador Inc.",
        "FEU" to "Far Eastern University, Incorporated",
        "GLO" to "Globe Telecom, Inc.",
        "GTPPA" to "GTCAP PREF A",
        "HOME" to "AllHome Corp.",
        "KEEPR" to "DAVIN",
        //"OPM" to "Oriental Petroleum and Minerals Corporation",
        //"PAL" to "PAL Holdings, Inc.",
        "MER" to "Manila Electric Company",
        //"PRF2A" to "PCOR PREF 2A",
        "ROCK" to "Rockwell Land Corporation",
        "SHLPH" to "PILIPINAS SHELL",
        "SSI" to "SSI Group, Inc.",
     //   "TECH" to "Cirtek Holdings Philippine Corporation",
        "VITA" to "Vitarich Corporation",
        "WEB" to "PhilWeb Corporation",
        //"X" to "Xurpas Inc."
        //"URC" to "Universal Robina Corporation"
    )

    fun getDisabledRandomStock(): String {
        val stocks = listOf(
            "JFC", "ACPB1", "ACR", "CIP", "DD", "EURO", "FGEN", "GEO", "ION", "JAS",
            "KPHB", "LTG", "MA", "PA", "SEVN", "TEL", "WIN", "ZHI"
        )

        return stocks[Random.nextInt(stocks.size)]
    }

    fun getQuantity(lastPrice: String, minQuantity: String, totalPrice: String, orderType: String): String {
        // Convert inputs to appropriate types
        val lastPriceValue = lastPrice.toDouble()
        val minQuantityValue = minQuantity.toInt()
        val totalPriceValue = totalPrice.toDouble()

        println("Last Price: $lastPriceValue")
        println("Min Quantity: $minQuantityValue")
        println("Total Price: $totalPriceValue")
        println("Order Type: $orderType")

        // Calculate the maximum quantity based on total price and last price
        val maxQuantity = (totalPriceValue / lastPriceValue).toInt()

        // Ensure the base quantity is at least 3 times the minQuantity
        val baseQuantity = maxOf(3 * minQuantityValue, maxQuantity)

        val quantityMultiple = when {
            orderType == "Conditional" && minQuantityValue == 1000 -> 1000
            orderType == "Conditional" -> 100
            else -> minQuantityValue
        }

        var closestQuantity = (baseQuantity / quantityMultiple) * quantityMultiple

        // Add randomness: increase the quantity by a random amount between 3 and 5 times the quantity multiple
        val randomIncrease = Random.nextInt(3, 6) * quantityMultiple

        closestQuantity += randomIncrease

//        // Ensure we don't exceed the maximum possible quantity and remain divisible by quantityMultiple
//        closestQuantity = min(closestQuantity, maxQuantity / quantityMultiple * quantityMultiple)

        // Ensure closestQuantity is never equal to minQuantity
        if (closestQuantity <= minQuantityValue) {
            closestQuantity += quantityMultiple
        }

        println("Random Multiplier: $randomIncrease")
        println("Desired Quantity: $closestQuantity")
        return closestQuantity.toString()
    }

    fun areStringsClose(string1: String, string2: String): Pair<Boolean, String> {
        // Use a single regex to replace commas and parse to Double
        val regex = Regex(",")
        val num1 = regex.replace(string1, "").toDoubleOrNull()
        val num2 = regex.replace(string2, "").toDoubleOrNull()

        // Early return if conversion fails
        if (num1 == null || num2 == null) {
            return false to "Not Counted"
        }

        // Calculate the absolute difference and round to 2 decimal places
        val difference = (abs(num1 - num2) * 100).roundToInt() / 100.0

        // Check if the difference is between 0.02 and 0.05 (inclusive)
        val isClose = difference in 0.01..0.06

        return isClose to difference.toString()
    }

    fun getRandomStockCode(): String {
        val stockCodes = stockMap.keys.toList()
        return stockCodes[Random.nextInt(stockCodes.size)]
    }

    fun getStockNameByCode(stockCode: String): String {
        return stockMap[stockCode].toString()
    }

    fun getClosingPrice(): String {
        it.tap("Statistics")
        val closingPrice = it.select("P. Close:right()")

        println("Closing Price: " + closingPrice.text)

        // Remove the comma from the closing price before returning it
        return closingPrice.text.replace(",", "")
    }

    fun getRandomOrderType(): String {
        val orderTypes = listOf("Limit", "Conditional")
        return orderTypes[Random.nextInt(orderTypes.size)]
    }



    fun getRandomDesiredPrice(lastPrice: String, tradingHour: String, orderType: String, isBuy: Boolean): Pair<String, String> {
        val lastPriceValue = lastPrice.toDouble()
        val decimalPlaces = lastPrice.split(".").getOrNull(1)?.length ?: 0

        var randomMultiplier = 1.0

        if(tradingHour.contains("Open")){
            randomMultiplier = Random.nextDouble(0.9, 1.0)
            println("Open")
        } else if (tradingHour == "Runoff" && orderType == "Limit") {
            randomMultiplier = 1.0
            println("Runoff and Order Type Limit")
        } else if (orderType == "Conditional") {
            println("Conditional")
            if(isBuy){
                println("Buy")
                randomMultiplier = Random.nextDouble(0.9, 1.49)
            } else if (!isBuy){
                println("Sell")
                randomMultiplier = Random.nextDouble(1.1, 1.49)
            }

        }
        else  {
            println("Else")
            randomMultiplier  = Random.nextDouble(0.71, 1.49)
        }


        // Generate a preliminary price
        val preliminaryPrice = lastPriceValue * randomMultiplier

        // Determine the appropriate tick size for the preliminary price
        val tickSizeForPreliminaryPrice = getTickSize(preliminaryPrice).toDouble()
        val tickSizeText = getTickSize(preliminaryPrice)

        // Round to the nearest multiple of the tick size
        val roundedPrice = round(preliminaryPrice / tickSizeForPreliminaryPrice) * tickSizeForPreliminaryPrice

        println("Trading Hour: $tradingHour")
        println("Rounded Price: $roundedPrice")
        println("Preliminary Price: $preliminaryPrice")
        println("Tick Size: $tickSizeForPreliminaryPrice")
        println("Last Price Value: $lastPrice")
        println("Random Multiplier: $randomMultiplier")
        // Format the result based on the input decimal places
        val formattedPrice = when (decimalPlaces) {
            2 -> String.format("%.2f", roundedPrice)
            4 -> String.format("%.4f", roundedPrice)
            else -> String.format("%.2f", roundedPrice) // Default to 2 decimal places
        }

        return Pair(formattedPrice, tickSizeText)
    }

    fun getRandomCutlossPrice(lastPrice: String, tradingHour: String, desiredPrice: String): Pair<String, String> {
        val lastPriceValue = lastPrice.toDouble()
        val desiredPriceValue = desiredPrice.toDouble()
        val decimalPlaces = lastPrice.split(".").getOrNull(1)?.length ?: 0

        // Determine the lower bound for our price
        val lowerBound = minOf(lastPriceValue, desiredPriceValue)

        var randomMultiplier = 1.0

        // Generate a multiplier to ensure price is below both last price and desired price
        if(tradingHour.contains("Open") || tradingHour.contains("Pre-Close")){
            randomMultiplier = Random.nextDouble(0.71, 0.99)
        } else if (tradingHour == "Runoff") {
            randomMultiplier = 0.99 // Set to a fixed value just below 1
        } else {
            randomMultiplier = Random.nextDouble(0.71, 0.99)
        }

        // Generate a preliminary price
        var preliminaryPrice = lowerBound * randomMultiplier

        // Determine the appropriate tick size for the preliminary price
        val tickSizeForPreliminaryPrice = getTickSize(preliminaryPrice).toDouble()
        val tickSizeText = getTickSize(preliminaryPrice)

        // Round down to the nearest multiple of the tick size
        var roundedPrice = floor(preliminaryPrice / tickSizeForPreliminaryPrice) * tickSizeForPreliminaryPrice

        // Ensure the rounded price is below both the last price and desired price
        while (roundedPrice >= lastPriceValue || roundedPrice >= desiredPriceValue) {
            roundedPrice -= tickSizeForPreliminaryPrice
        }

        println("Trading Hour: $tradingHour")
        println("Rounded Price: $roundedPrice")
        println("Preliminary Price: $preliminaryPrice")
        println("Tick Size: $tickSizeForPreliminaryPrice")
        println("Last Price Value: $lastPrice")
        println("Desired Price Value: $desiredPrice")
        println("Random Multiplier: $randomMultiplier")

        // Format the result based on the input decimal places
        val formattedPrice = when (decimalPlaces) {
            2 -> String.format("%.2f", roundedPrice)
            4 -> String.format("%.4f", roundedPrice)
            else -> String.format("%.2f", roundedPrice) // Default to 2 decimal places
        }

        return Pair(formattedPrice, tickSizeText)
    }

    fun getRandomDesiredQuantity(lastPrice: String, minQuantity: String, walletBalance: String) : String{
        // Maximum quantity is 10 times the minimum quantity
        val maxQuantity = minQuantity.toInt() * 10

        // Calculate the maximum affordable quantity
        val maxAffordableQuantity = (walletBalance.toDouble() / lastPrice.toDouble()).toInt()

        // Calculate the effective maximum quantity considering both constraints
        val effectiveMaxQuantity = minOf(maxQuantity, maxAffordableQuantity)

        // If effectiveMaxQuantity is less than minQuantity, return 0 as the user cannot afford even the minimum quantity
        if (effectiveMaxQuantity < minQuantity.toInt()) {
            println("Cannot afford the minimum quantity")
        }

        val maxMultiplier = effectiveMaxQuantity / minQuantity.toInt()

        // Ensure maxMultiplier is at least 1
        val randomMultiplier = if (maxMultiplier > 0) {
            Random.nextInt(1, maxMultiplier + 1)
        } else {
            1 // Or handle this case in another way if needed
        }

        return (randomMultiplier * minQuantity.toInt()).toString()
    }


    fun calculateCeilingPrice(closingPrice: String): String {
        val decimalPlaces = determineDecimalPlaces(closingPrice)

        val price = BigDecimal(closingPrice)
        val adjustment = BigDecimal("0.${"0".repeat(decimalPlaces - 1)}1")
        val ceilingPrice = (price * BigDecimal("1.50") + adjustment)
            .setScale(decimalPlaces, RoundingMode.HALF_UP)

        return String.format("%." + decimalPlaces + "f", ceilingPrice)
    }

    fun calculateFloorPrice(closingPrice: String): String {
        val decimalPlaces = determineDecimalPlaces(closingPrice)

        val price = BigDecimal(closingPrice)
        val adjustment = BigDecimal("0.${"0".repeat(decimalPlaces - 1)}1")
        val floorPrice = (price * BigDecimal("0.70") - adjustment)
            .setScale(decimalPlaces, RoundingMode.HALF_UP)

        return String.format("%." + decimalPlaces + "f", floorPrice)
    }


    private fun determineDecimalPlaces(number: String): Int {
        return when {
            number.contains(".") -> {
                val afterDecimal = number.substringAfter(".")
                when {
                    afterDecimal.isEmpty() -> 2
                    afterDecimal.length == 1 -> 2
                    afterDecimal.length >= 4 -> 4
                    else -> afterDecimal.length
                }
            }
            else -> 2
        }
    }

    fun overBalanceCost(desiredPrice: String, minQuantity: String, walletBalance: String, percentage: Double): Pair<String, String> {
        // Calculate the maximum allowable amount to spend
        val maxSpendAmount = walletBalance.toDouble() * percentage

        // Convert inputs to appropriate types
        val price = desiredPrice.toDouble()
        val minQty = minQuantity.toInt()

        // Initialize variables
        var quantity = 0
        var totalCost = 0.0

        // Increment the quantity in multiples of minQuantity until the total cost exceeds maxSpendAmount
        while (true) {
            val nextQuantity = quantity + minQty
            val nextTotalCost = price * nextQuantity

            // If the next total cost exceeds the max spend amount, return the current quantity and the next total cost
            if (nextTotalCost > maxSpendAmount) {
                return Pair(String.format("%.2f", nextTotalCost), quantity.toString())
            }

            quantity = nextQuantity
            totalCost = nextTotalCost
        }
    }

    fun percentAllocation(desiredPrice : String, minQuantity: String, walletBalance: String, percentage:Double): Pair<String, String> {

        // Calculate the maximum allowable amount to spend
        val maxSpendAmount = walletBalance.toDouble() * percentage

        // Initialize variables
        var quantity = 0
        var totalCost = 0.0



        // Increment the quantity in multiples of minQuantity until the total cost exceeds maxSpendAmount
        while (true) {
            val nextQuantity = quantity + minQuantity.toInt()
            val nextTotalCost = desiredPrice.toDouble() * nextQuantity

            if (nextTotalCost > maxSpendAmount) {
                break
            }

            quantity = nextQuantity
            totalCost = nextTotalCost
        }


        println("Total Cost $totalCost")
        println("Quantity $quantity")
        println("Percentage $percentage")

        return Pair(String.format("%.2f", totalCost), quantity.toInt().toString())
    }

    fun getInvalidTickSize(tickSize: Double): Double {
        val validTickSizes = listOf(
            0.0001, 0.001, 0.005, 0.01, 0.02, 0.05,
            0.1, 0.2, 0.5, 1.0, 2.0, 5.0
        )

        val invalidTickSizes = listOf(
            0.0002, 0.003, 0.004, 0.015, 0.025, 0.07,
            0.15, 0.25, 0.6, 1.5, 3.0, 6.0
        )

        // Find an invalid tick size that's not related to the input tick size
        for (invalidSize in invalidTickSizes) {
            if (tickSize != invalidSize) {
                return invalidSize
            }
        }

        // If none found, just return the first invalid tick size
        return invalidTickSizes[0]
    }

    fun getTickSize(lastPrice: Double): String {
        val price = BigDecimal(lastPrice).setScale(4, RoundingMode.HALF_UP)
        val tickSize = when {
            price < BigDecimal("0.0100") -> "0.0001"
            price < BigDecimal("0.0500") -> "0.0010"
            price < BigDecimal("0.2500") -> "0.0010"
            price < BigDecimal("0.5000") -> "0.0050"
            price < BigDecimal("5.0000") -> "0.0100"
            price < BigDecimal("10.0000") -> "0.0100"
            price < BigDecimal("20.0000") -> "0.0200"
            price < BigDecimal("50.0000") -> "0.0500"
            price < BigDecimal("100.0000") -> "0.0500"
            price < BigDecimal("200.0000") -> "0.1000"
            price < BigDecimal("500.0000") -> "0.2000"
            price < BigDecimal("1000.0000") -> "0.5000"
            price < BigDecimal("2000.0000") -> "1.0000"
            price < BigDecimal("5000.0000") -> "2.0000"
            price >= BigDecimal("5000.0000") -> "5.0000"
            else -> throw IllegalArgumentException("Price out of range")
        }
        return tickSize
    }

//    fun formatNumberWithCommaPeriod(number: String): String {
//        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
//        val decimalFormat = DecimalFormat("#,###.00")
//
//        val formattedNumber = if (number.startsWith(".")) {
//            decimalFormat.format("0$number".toDouble())
//        } else if (number.contains(".")) {
//            decimalFormat.format(number.toDouble())
//        } else {
//            decimalFormat.format("$number.00".toDouble())
//        }
//
//        return formattedNumber
//    }

    fun formatNumberWithCommaPeriod(number: String): String {
        // Determine the decimal places in the input number
        val decimalPlaces = when {
            number.contains(".") -> {
                val afterDecimal = number.substringAfter(".")
                when {
                    afterDecimal.isEmpty() -> 2
                    afterDecimal.length == 1 -> 2
                    afterDecimal.length == 3 -> 4
                    afterDecimal.length >= 4 -> 4
                    else -> afterDecimal.length
                }
            }
            else -> 2
        }

        // Create a decimal format pattern based on the number of decimal places
        val pattern = "#,##0.${"0".repeat(decimalPlaces)}"
        val decimalFormat = DecimalFormat(pattern).apply {
            this.decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
        }

        val numberValue = when {
            number.startsWith(".") -> "0$number".toDouble()
            number.contains(".") -> number.toDouble()
            else -> "$number.00".toDouble()
        }

        return decimalFormat.format(numberValue)
    }

    fun formatNumberWithComma(number: String): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val parsedNumber = number.toDoubleOrNull() ?: return number // Handle invalid input gracefully

        // Ensure two decimal places
        val formattedNumber = String.format("%.2f", parsedNumber)

        // Format with commas
        val formattedWithCommas = numberFormat.format(formattedNumber.toDouble())

        return formattedWithCommas
    }

    fun feesComputation(lastPrice: String, minQuantity: String, broker:String, isBuy:Boolean): Map<String, Any>{

        var grossAmount = lastPrice.toDouble() * minQuantity.toDouble()

        //Compute Broker Commission
        var brokerCommissionValue = brokerCommission * grossAmount

//        if(brokerCommissionValue < 20){
//            brokerCommissionValue = 20.0
//        }

        println("Original Broker Commission $brokerCommissionValue")

        //Compute VAT
        var VATValue = brokerCommissionValue * VAT

        //Compute PSEFee
        var PSEFeeValue = grossAmount * PSEFee

        //Compute PSEVat
        var PSEVATValue = PSEFeeValue * PSEVAT

        //Compute SEC Fee
        var SECFEEValue = grossAmount * SECFee

        //Compute SCCP Fee
        var SCCPValue = grossAmount * SCCP

        //Compute Sales Tax
        var SalesTaxValue = grossAmount * SalesTax


        var totalBuyAmount = 0.00
        var totalSellAmount = 0.00

        //Total Fees
        var totalBuyCharges = 0.0

        //Total Sell Charges
        var totalSellCharges = 0.0
        var PSEandSECFee = 0.0

        if(broker == "1"){
            //Round of two Decimals
            grossAmount = roundToTwoDecimalPlaces(grossAmount)
            VATValue = brokerCommissionValue * VAT
            brokerCommissionValue = roundToTwoDecimalPlaces(brokerCommissionValue)

            println("Original Broker 2 Decimal $brokerCommissionValue")

            println("Original VAT Value $VATValue")
            VATValue = roundToTwoDecimalPlaces(VATValue)

            PSEFeeValue = roundToThreeDecimalPlaces(PSEFeeValue)
            SECFEEValue = roundToThreeDecimalPlaces(SECFEEValue)
            PSEandSECFee = roundToTwoDecimalPlaces(PSEFeeValue + SECFEEValue)
            SCCPValue = roundToTwoDecimalPlaces(SCCPValue)
            PSEVATValue = roundToTwoDecimalPlaces(PSEVATValue)
            SalesTaxValue = roundToTwoDecimalPlaces(SalesTaxValue)

            //Total Fees
            totalBuyCharges =  brokerCommissionValue + VATValue + PSEandSECFee + SCCPValue

            //Total Sell Charges
            totalSellCharges = brokerCommissionValue + VATValue + PSEandSECFee + SCCPValue + SalesTaxValue

        }


        val formattedTotalBuyCharges = roundToTwoDecimalPlaces(totalBuyCharges).toString()
        val formattedTotalSellCharges = roundToTwoDecimalPlaces(totalSellCharges).toString()

        println("Desired Price: $lastPrice")
        println("Desired Quantity: $minQuantity")
        println("---------------------------")
        println("Gross Amount: $grossAmount")

        println("Broker Commission: $brokerCommissionValue")
        println("Comms VAT Value: $VATValue")
        println("PSE Fee: $PSEFee")
        println("PSE Fee Value: $PSEFeeValue")
        println("SEC Fee Value: $SECFEEValue")
        println("PSE + Sec Fee: $PSEandSECFee")
        println("SCCP Fee Value: $SCCPValue")
        println("Sales Tax: $SalesTaxValue")
        println("PSE Vat Value: $PSEVATValue")
        println("Total Buy Charges: $formattedTotalBuyCharges")
        println("Total Sell Charges: $formattedTotalSellCharges")
        println("--------------------------")


        //Return Computation
        return if(isBuy){
            mapOf(
                "Buy Charges" to formattedTotalBuyCharges,
                "Broker Commission" to brokerCommissionValue,
                "Broker VAT" to VATValue,
                "PSE & SEC Fee" to PSEandSECFee,
                "SCCP Fee" to SCCPValue,
            )
        } else {
            mapOf(
                "Sell Charges" to formattedTotalSellCharges,
                "Broker Commission" to brokerCommissionValue,
                "Broker VAT" to VATValue,
                "PSE & SEC Fee" to PSEandSECFee,
                "SCCP Fee" to SCCPValue,
            )
        }
    }



    fun totalAmountComputation(lastPrice: String, minQuantity: String, broker:String, isBuy:Boolean): String {

        var grossAmount = lastPrice.toDouble() * minQuantity.toDouble()

        //Compute Broker Commission
        var brokerCommissionValue = brokerCommission * grossAmount

//        if(brokerCommissionValue < 20){
//            brokerCommissionValue = 20.0
//        }

        println("Original Broker Commission $brokerCommissionValue")

        //Compute VAT
        var VATValue = brokerCommissionValue * VAT

        //Compute PSEFee
        var PSEFeeValue = grossAmount * PSEFee

        //Compute PSEVat
        var PSEVATValue = PSEFeeValue * PSEVAT

        //Compute SEC Fee
        var SECFEEValue = grossAmount * SECFee

        //Compute SCCP Fee
        var SCCPValue = grossAmount * SCCP

        //Compute Sales Tax
        var SalesTaxValue = grossAmount * SalesTax



        var totalBuyAmount = 0.00
        var totalSellAmount = 0.00

        var buyNetAmount =  0.0
        var sellNetAmount = 0.0

        var PSEandSECFee = 0.0

        if(broker == "1"){

            var formattedTotalBuyCharges = 0.0
            var formattedTotalSellCharges = 0.0

            var result : Map<String, Any> = emptyMap()
            result = feesComputation(lastPrice, minQuantity, broker, isBuy)



            if(isBuy){

                formattedTotalBuyCharges = (result["Buy Charges"] as? String ?: "0.0").toDouble()

                println("Buy Charges: $formattedTotalBuyCharges")

            } else {

                formattedTotalSellCharges = (result["Sell Charges"] as? String ?: "0.0").toDouble()

                println("Sell Charges: $formattedTotalSellCharges")
            }




            println("Total Buy Charges: $formattedTotalBuyCharges")
            println("Total Sell Charges: $formattedTotalSellCharges")

            buyNetAmount =  grossAmount + formattedTotalBuyCharges
            sellNetAmount = grossAmount - formattedTotalSellCharges


            println("--------------------------")
            println("Buy Net Amount: $buyNetAmount")
            println("Sell Net Amount: $sellNetAmount")

        }

        //Return Computation
        if(isBuy){

            println("--------------------------------")
            println("Total Buy Amount $buyNetAmount")
            println("--------------------------------")

            return String.format("%.2f", buyNetAmount)
        } else {

            println("--------------------------------")
            println("Total Sell Amount $sellNetAmount")
            println("--------------------------------")

            return String.format("%.2f", sellNetAmount)


        }



    }

    fun roundToThreeDecimalPlaces(value: Double): Double {
        return BigDecimal(value.toString()).setScale(3, RoundingMode.HALF_UP).toDouble()
    }

    fun roundToTwoDecimalPlaces(value: Double): Double {
        return BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun getLocalTime(): String? {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")  // Example format: 02:30 PM
        val formattedTime = currentTime.format(formatter)

        return formattedTime
    }

    fun parseTime(timeString: String?): LocalTime? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            LocalTime.parse(timeString, formatter)
        } catch (e: DateTimeParseException) {
            println("Error parsing time: ${e.message}")
            null
        }
    }

    fun removeDecimalAndAddZeros(number: String): String {
        val roundedNumber = number.toDouble().roundToInt()
        return "$roundedNumber.00"
    }

    fun getCurrenTradingHour(): String {
        val currentTime = parseTime(getLocalTime())

        val nineAM = LocalTime.of(9, 0)
        val tenAM = LocalTime.of(10, 0)
        val elevenAM = LocalTime.of(11, 0)
        val twelveThirtyPM = LocalTime.of(12, 30)
        val twelveFortyPM = LocalTime.of(12, 40)
        val twoPM = LocalTime.of(14, 0)
        val twoThirtyPM = LocalTime.of(14, 30)
        val threePM = LocalTime.of(15, 0)
        val fourPM = LocalTime.of(16, 0)

        if (currentTime == null) {
            return "Time is not available"
        }

        return when {
            currentTime == nineAM || (currentTime.isAfter(nineAM) && currentTime.isBefore(tenAM)) -> "Pre-Open"
            currentTime == tenAM || (currentTime.isAfter(tenAM) && currentTime.isBefore(elevenAM)) -> "Pre-Open No Cancellation"
            currentTime == elevenAM || (currentTime.isAfter(elevenAM) && currentTime.isBefore(twelveThirtyPM)) -> "Open"
            currentTime == twelveThirtyPM || (currentTime.isAfter(twelveThirtyPM) && currentTime.isBefore(twelveFortyPM)) -> "Recess"
            currentTime == twelveFortyPM || (currentTime.isAfter(twelveFortyPM) && currentTime.isBefore(twoPM)) -> "Open"
            currentTime == twoPM || (currentTime.isAfter(twoPM) && currentTime.isBefore(twoThirtyPM)) -> "Pre-Close"
            currentTime == twoThirtyPM || (currentTime.isAfter(twoThirtyPM) && currentTime.isBefore(threePM)) -> "Pre-Close No Cancellation"
            currentTime == threePM || (currentTime.isAfter(threePM) && currentTime.isBefore(fourPM)) -> "Runoff"
            else -> "Close"
        }
    }

    fun processReitsFile(filePath: String){
        // Path to your CSV file
        val csvFile = File(filePath)

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 4 elements
            if (row.size >= 4) {
                val stock_code = row[0]
                var div_yield = row[1]
                var price = row[2]
                var change = row[3]

                price = price.replace(",","")
                price = formatNumberWithCommaPeriod(price)
                div_yield = div_yield.replace("%","")

                it.exist("Div Yield:")
                if(div_yield.startsWith('-')){
                    it.exist("- $div_yield %")
                } else if (div_yield == "0.00"){
                    it.exist(" $div_yield %")
                } else {
                    it.exist("+ $div_yield %")
                }

                // Wait for Page to Load
                describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                // Check existence
                it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[${index + 2}]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.ImageView")
                it.exist(stock_code)


                //it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[${index + 2}]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.ImageView")
                it.exist("₱$price")

                println(change)
                if (change.startsWith('-')) {
                    //Trend Down Sign
                    change = change.replace("-","")
                    it.exist("\uE915")
                } else if (change == "0.00"){
                    //Straight Sign

                    it.exist("\uE914")
                } else {
                    //Trend Up Sign
                    change = change.replace("+","")
                    it.exist("\uE91F")
                }

                it.exist("$change %")


            }
        }
    }

    fun processCsvFile(filePath: String) {
        // Path to your CSV file
        val csvFile = File(filePath)

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 4 elements
            if (row.size >= 4) {
                val stock_code = row[0]
                val volume = row[1]
                var price = row[2]
                var change = row[3]

                price = price.replace(",","")
                price = formatNumberWithCommaPeriod(price)

                // Wait for Page to Load
                describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                // Check existence
                it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[${index + 2}]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.ImageView")
                it.exist(stock_code)
                it.exist("Vol:")
                it.exist("₱$volume")
                it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[${index + 2}]/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.ImageView")
                it.exist("₱$price")

                println(change)
                if (change.startsWith('-')) {
                    //Trend Down Sign
                    change = change.replace("-","")
                    it.exist("\uE915")
                } else if (change == "0.00"){
                    //Straight Sign

                    it.exist("\uE914")
                } else {
                    //Trend Up Sign
                    change = change.replace("+", "")
                    it.exist("\uE91F")
                }

                it.exist("$change %")

                it.tap(stock_code)

                describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                it.exist("Normal lot")
                it.exist("Bid And Ask")
                it.exist("My Investment")

                //go back
                it.tap("\uE944")

                it.exist("All")
                it.exist("Bluechips")
                it.exist("REITs")
                it.exist("Dividend Stocks")
            }
        }
    }

    fun assertOrder(orderStatus:String, isEndOfDay: Boolean, isBuy: Boolean, stockCode: String, desiredQuantity: String, totalPrice: String, referenceNumberText:String, orderType: String, cutlossPrice: String? = null, cutlossTotalPrice:String ? = null, desiredPrice: String, diff_fees: String, willAddNotes: Boolean, noteMessage: String, subTotal: String, fees: String, firstTap:Boolean, diff_cutloss_fees: String ? = "", executedPrice:String ? = "", executedQuantity:String ? = "", broker: String, brokerCommission:String ? = "", brokerVAT:String ? = "", PSEandSECFee:String ? = "", SCCPVAlue:String ? = "" ){


        if(firstTap && (orderStatus == "Cancelled" || orderStatus == "Rejected" || orderStatus == "Expired" || orderStatus == "Rejected Cancellation" || orderStatus == "Error")){
            it.scrollDown()
            it.scrollDown()
            it.tap(x=165,y=2033)
        } else if (firstTap && orderStatus == "Filled"){
            it.scrollDown()
            it.scrollDown()
            it.tap("*Completed*")
        } else if (firstTap && orderStatus == "Partial"){
            it.selectWithScrollDown("*Posted*")
        }

        var cleanTotalPrice = formatNumberWithCommaPeriod((totalPrice.toDouble() + diff_fees.toDouble()).toString()).replace(",","").toDouble()
        val totalPriceText = String.format("%,.2f", cleanTotalPrice )



        it.scrollDown(totalPriceText)

        if(isBuy){
            it.exist("\uE929")
        } else {
            it.exist("\uE928")
        }

        it.exist(stockCode)

        val sharesWithComma = formatNumberWithComma(desiredQuantity)

        var executedQuantityWithComma = ""

        if(executedQuantity != "" && executedQuantity != null) {
            executedQuantityWithComma = formatNumberWithComma((desiredQuantity.toDouble() - executedQuantity.toDouble()).toString())
        }

        if (orderStatus == "Partial") {
            println("Partial Shares: $executedQuantityWithComma shares")
            it.exist("$executedQuantityWithComma shares")
        } else {
            println("Shares: $sharesWithComma shares")
            it.exist("$sharesWithComma shares")
        }

        if(isEndOfDay){
            it.exist("End of Day")
        } else {
            it.exist("Until Cancelled")
        }


        var partialNetAmount = ""
        var leftOverNetAmount = ""
        var leftOverNetAmountWithComma = ""

        if(executedQuantity != "" && executedQuantity != null && executedPrice != "" && executedPrice != null) {

            //compute average Price
            partialNetAmount = (executedQuantity.toDouble() * executedPrice.toDouble()).toString()
            println("Executed Quantity: $executedQuantity")
            println("Executed Price: $executedPrice")
            println("Partial Net Amount: $partialNetAmount")
            partialNetAmount = (partialNetAmount.toDouble()).toString()

            leftOverNetAmount = (totalPrice.toDouble() - partialNetAmount.toDouble()).toString()

            var result : Map<String, Any> = emptyMap()

            result = feesComputation(executedPrice, executedQuantity, broker, isBuy)

            var partialFees = ""

            if(isBuy){
                partialFees = result["Buy Charges"] as? String ?: ""

            } else {
                partialFees = result["Buy Charges"] as? String ?: ""

            }

            println("Partial Fees: $partialFees")

            leftOverNetAmount = (leftOverNetAmount.toDouble()-partialFees.toDouble()).toString()
            println("Total Price: $totalPrice")


            leftOverNetAmountWithComma = formatNumberWithComma((leftOverNetAmount.toDouble()+diff_fees.toDouble()).toString())

            println("LeftOver Net Amount: $leftOverNetAmount")
            println("Partial Fees: $partialFees")
        }

        if(isBuy && orderStatus == "Partial"){
            it.exist(leftOverNetAmountWithComma)
        } else if (isBuy && orderStatus != "Partial"){
            it.exist(totalPriceText)
        }

        var cutlossPriceWithComma = ""
        val desiredPriceWithComma = formatNumberWithCommaPeriod(desiredPrice)
        if(cutlossPrice != null){
            cutlossPriceWithComma = formatNumberWithCommaPeriod(cutlossPrice)
        }

        if(orderType == "Limit"){
            it.exist("Price: $desiredPriceWithComma")

        } else if (orderType == "Conditional" && !isBuy) {
            it.exist("Target Price: $desiredPriceWithComma")
            //up arrow
            it.exist("\uE930")

            it.exist("Cutloss Price: $cutlossPriceWithComma")
            //down ar row
            it.exist("\uE931")

            var originalcutlossTotalPriceComma = cutlossTotalPrice?.let { it1 -> formatNumberWithCommaPeriod(it1) }.toString()
            it.exist(originalcutlossTotalPriceComma)

        }

        if(orderStatus == "Partial"){

            //View Order Details
            viewOrderDetails(
                leftOverNetAmount,
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
                executedPrice,
                executedQuantity,
            )

        } else {
            //View Order Details
            viewOrderDetails(
                totalPrice,
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
                executedPrice,
                executedQuantity,
            )

        }

        if(orderType == "Partial"){
            it.selectWithScrollDown("Completed")
            it.select("*Completed*").tap()

            if(isBuy){

                it.exist("\uE929")
            } else {
                it.exist("\uE928")
            }

            it.exist(stockCode)

            val sharesWithComma = formatNumberWithComma(desiredQuantity)
            val executedQuantityWithComma = executedQuantity?.let { it1 -> formatNumberWithComma(it1) }

            if(orderType !== "Partial") {
                it.exist("$sharesWithComma shares")
            } else {
                it.exist("$executedQuantityWithComma shares")
            }

            if(isEndOfDay){
                it.exist("End of Day")
            } else {
                it.exist("Until Cancelled")
            }

            // Remove commas and parse to Int
            var totalPriceInt =  formatNumberWithComma((totalPrice.toDouble().toInt()).toString())
            var targetNumber = totalPriceInt.replace(",", "").toInt()
            var upperBound = targetNumber + 1
            var upperBoundFormatted = formatNumberWithComma(upperBound.toString())
            // Use the formatted strings in the selector
            var totalPriceClose = ""
            try{
                totalPriceClose = it.select("*$totalPriceInt*").text
            } catch (e: Exception) {
                // Handle the exception
                totalPriceClose = it.select("*$upperBoundFormatted*").text
            }


            var cleanTotalPrice = formatNumberWithCommaPeriod((totalPrice.toDouble() + diff_fees.toDouble()).toString()).replace(",","").toDouble()
            val totalPriceText = String.format("%,.2f", cleanTotalPrice )

            if(isBuy){
                it.exist(totalPriceText)
            }

            var cutlossPriceWithComma = ""
            val desiredPriceWithComma = formatNumberWithCommaPeriod(desiredPrice)
            if(cutlossPrice != null){
                cutlossPriceWithComma = formatNumberWithCommaPeriod(cutlossPrice)
            }

            if(orderType == "Limit"){
                it.exist("Price: $desiredPriceWithComma")

            } else if (orderType == "Conditional" && !isBuy) {
                it.exist("Target Price: $desiredPriceWithComma")
                //up arrow
                it.exist("\uE930")

                it.exist("Cutloss Price: $cutlossPriceWithComma")
                //down ar row
                it.exist("\uE931")

                var originalcutlossTotalPriceComma = cutlossTotalPrice?.let { it1 -> formatNumberWithCommaPeriod(it1) }.toString()
                it.exist(originalcutlossTotalPriceComma)

            }

            //View Order Details
            viewOrderDetails(
                totalPrice,
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
                executedPrice,
                executedQuantity,
            )

        }





    }

    fun getColumnInRow(csvData: List<List<String>>, rowIndex: Int, columnIndex: Int): String? {
        return if (rowIndex in 1 until csvData.size && columnIndex in csvData[rowIndex].indices) {
            csvData[rowIndex][columnIndex]
        } else {
            null
        }
    }


}

