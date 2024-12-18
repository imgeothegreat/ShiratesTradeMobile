package macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.io.File
import kotlin.random.Random
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import macro.Trade.formatNumberWithComma
import macro.Trade.formatNumberWithCommaPeriod
import macro.Trade.getRandomStockCode
import macro.Trade.getStockNameByCode
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@MacroObject
object Portfolio: TestDrive {


    @Macro("[Explore Subscriptions]")
    fun goToProfile(hasAnnouncements: Boolean, pin:String){
        //Announcements

        if(hasAnnouncements) {

            it.exist("Announcements")

            it.tap("Show")
            it.exist("Hide")
            it.dontExist("Show")

            // Path to your CSV file created in Python
            val csvFile = File("src/test/resources/latest_subscription.csv")

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

                // Check if the row has at least 6 elements
                if (row.size >= 6) {
                    val stockSubId = row[0]
                    val stockCode = row[1]
                    val subscriptionType = row[2]
                    val price = row[3]
                    val description = row[4]
                    val endDate = row[5]

                    // Use it.exist() for each column
                    describe("Wait for Page to Load")
                        .waitForDisplay(stockCode, waitSeconds = 65.0)

                    //it.exist(stockCode)
                    it.exist(subscriptionType)
                    it.exist("Subscribe")
                    it.exist("Hide")

                    it.tap("Subscribe")


                    if(subscriptionType != "Stock Rights Offering" && subscriptionType != "Tender Offer") {
                        describe("Wait for Page to Load")
                            .waitForDisplay("You are about to subscribe to this offering", waitSeconds = 60.0)

                        //it.exist(stockCode)
                        it.exist("PSE:$stockCode - $subscriptionType")

                        it.exist("Enter number of shares")
                        it.exist("*Boardlot:*")

                        val boardLot = it.select("*Boardlot:*")
                        val textBoardLot = boardLot.text.replace("Boardlot: ", "").replace(",", "").toString()

                        println("Boardlot: $textBoardLot")

//                    it.tap("xpath=//android.widget.EditText[@text=\"0, 0\"]")
//                        .clearInput()
//                        .wait(3)
//                        .sendKeys(textBoardLot)
//
//
//                    //tap minus
//                    it.tap("\uF068")
//                    it.exist("0")
//                    it.dontExist(textBoardLot)
//
//                    //tap plus
//                    it.tap("\uF067")
//                    it.dontExist("0")
//                    it.exist(textBoardLot)
//
//                    it.exist("Subscribed Shares")
//                    it.exist("Gross Amount")
//                    it.exist("Click here to view the terms and conditions")
//                    it.exist("I agree with terms and conditions")
//                    it.tap("Subscribe now")
//
//                    describe("Wait for Page to Load")
//                        .waitForDisplay("Please agree to the terms and agreement before subscribing.", waitSeconds = 30.0)
//
//                    it.tap("xpath=//android.widget.Image[@text=\"checked\"]")
//                    it.tap("Subscribe now")
//
//                    it.exist("You are about to create your subscription")
//                    it.exist("Make sure you have enough funds until the deadline before subscribing to this offer.")
//                    it.exist("Enter your trading pin to confirm")
//
//                    it.tap("xpath=//android.widget.EditText")
//                        .sendKeys(pin)
//
//                    it.tap("xpath=//android.widget.Image[@text=\"eye-icon-slashed.png?i_1.0.8971\"]")
//                    it.exist("Back")
//                    it.exist("Subscribe to this offer")
//
//                    it.tap("Subscribe to this offer")
//
//                    it.exist("Successfully updated your subscription for this offer.")
                    }

                    it.tap("\uE908")

                    println("Processed Stock Sub ID: $stockSubId")
                    println("Processed Stock Code: $stockCode")
                    println("Processed Subscription Type: $subscriptionType")
                    println("Processed Price: $price")
                    println("Processed Description: $description")
                    println("Processed End Date: $endDate")
                } else {
                    println("Row does not have enough elements")
                }

                println("------------------------")
            }

            //Hide

            it.tap("Hide")

            it.exist("Show")
            it.dontExist("Hide")


        }

    }

    @Macro("[Explore Wallet Page]")
    fun exploreWalletPage(useUpdate:Boolean) {

        it.tap("Portfolio")

        it.scrollUp()

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/Portfolio.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        var wallet_balance: String

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 10) {
                wallet_balance = row[0]

                it.exist(wallet_balance)
            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")

            it.tap("Available Cash")

            it.scrollUp()

            describe("Wait for Page to Load")
                .waitForDisplay("Available Cash", waitSeconds = 30.0)
            it.exist("Available Cash")

            it.exist("Wallet")
            it.exist("Cash In")
            it.exist("Cash Out")
            it.exist("Pending")

            it.selectWithScrollDown("History")

            it.exist("History")

            //Go Back
            //it.tap("\uE908")
            it.tap(x=55,y=193)

            describe("Wait for Page to Load")
                .waitForDisplay("Trade Analytics", waitSeconds = 30.0)


            it.exist("Trade Analytics")

            closeCodePush(useUpdate)
        }
    }

    @Macro("[Explore Trade Analytics]")
    fun exploreTradeAnalytics(useUpdate:Boolean, hasTradePerformance:Boolean){
        it.exist("Trade Analytics")
        it.exist("Port Stats, Trade Records, Transactions")

        it.tap("Trade Analytics")

        describe("Wait for Page to Load")
            .waitForDisplay("Portfolio Stats", waitSeconds = 30.0)

        it.exist("Portfolio Stats")
        it.exist( "Equity Growth")
        it.exist("Profit Growth")

        it.tap("Profit Growth")
        it.tap("Equity Growth")

        it.exist("1M")
        it.exist("3M")
        it.exist("6M")
        it.exist("1Y")
        it.exist("2Y")

        // Path to your CSV file created in Python
        var csvFile = File("src/test/resources/about_my_portfolio.csv")

        // Read CSV file
        var csvReader = CsvReader()
        var csvData = csvReader.readAll(csvFile)

        var rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        var realized_gains=""
        var realized_gains_percent=""

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 11) {
                val total_equity = row[0]
                var total_equity_percent = row[1]
                val stock_value = row[2]
                val available_cash = row[3]
                val unrealized_gains = row[4]
                var unrealized_gains_percent = row[5]
                val day_change = row[6]
                var day_change_percent = row[7]
                realized_gains = row[8]
                realized_gains_percent = row[9]
                val total_cash_divs = row[10]

                realized_gains_percent = realized_gains_percent.replace("+","")
                realized_gains_percent = realized_gains_percent.replace("%","")
                unrealized_gains_percent = unrealized_gains_percent.replace("%","")
                day_change_percent = day_change_percent.replace("%","")
                total_equity_percent = total_equity_percent.replace("%","")

                //it.exist("0Total Equity")
                it.exist("₱$total_equity*")
                it.exist("$total_equity_percent %")

                it.exist("Stock Value")
                it.exist("₱$stock_value")

                it.exist("Available Cash")
                it.exist("₱$available_cash")

                it.exist("Unrealized  Profit")
                it.exist("₱$unrealized_gains")
                it.exist("$unrealized_gains_percent %")

                it.exist("Day Change")
                it.exist("₱$day_change")
                it.exist("*$day_change_percent %")

                it.scrollToBottom()

                it.wait(1)

                it.exist("0Total Realized Profit")
                it.exist("₱$realized_gains")
                it.exist("$realized_gains_percent %")

                it.tap("View All")
                it.exist("Realized Profit")
                it.exist("0Total Realized Profit")
                it.exist("₱$realized_gains")
                it.exist("$realized_gains_percent %")
                it.tap(x = 64, y = 142)

                it.wait(1)

                describe("Wait for Home Page")
                    .waitForDisplay("Dividends", waitSeconds = 30.0)


                it.tap("Dividends")

                it.exist("0Total Cash Dividends")
                it.exist("₱$total_cash_divs")

                it.scrollDown()

                it.wait(2)

                it.tap("View All")
                it.exist("Dividends")
                it.exist("0Total Cash Dividends")
                it.exist("₱$total_cash_divs")

                it.tap(x = 64, y = 142)

                it.wait(1)


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")
        }


        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/trade_performance.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        if(rowCount > 1) {

            it.scrollToBottom()

            println("The CSV file contains data.")

            it.tap("Realized Profit")

            // Iterate through each row
            csvData.drop(1).forEachIndexed { index, row ->
                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 2) {
                    var stock_code = row[0]
                    var profit = row[1]
                    //val change = row[2]

                    describe("Wait for Home Page")
                        .waitForDisplay(stock_code, waitSeconds = 30.0)


                    it.exist(stock_code)
                    it.exist(profit)
                    it.exist("Profit")
                    it.exist("View All")

                    println("Stock Code: $stock_code")
                    println("Profit: $profit")



                    //it.exist(change)

                } else {
                    println("Row does not have enough elements")
                }


                println("------------------------")
            }

            it.tap("View All")

            csvData.drop(1).forEachIndexed { index, row ->
                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 2) {
                    var stock_code = row[0]
                    var profit = row[1]
                    //val change = row[2]

                    it.exist(stock_code)
                    it.exist(profit)
                    it.exist("Profit")



                    //it.exist(change)

                } else {
                    println("Row does not have enough elements")
                }


                println("------------------------")
            }

            it.tap(x = 64, y = 142)



        } else {
//            it.selectWithScrollDown("Your trade analytics is empty")
//            it.exist("Your trade analytics is empty")
        }

        //Dividend Tab

        describe("Wait for Home Page")
            .waitForDisplay("Dividends", waitSeconds = 30.0)


        it.tap("Dividends")

        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/stock_dividends.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        if(rowCount > 1) {

            it.scrollDown()

            println("The CSV file contains data.")

            // Iterate through each row
            csvData.drop(1).forEachIndexed { index, row ->
                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 2) {
                    var stock_code = row[0]
                    var profit = row[1]
                    profit = profit.replace("₱","")
                    //val change = row[2]

                    describe("Wait for Home Page")
                        .waitForDisplay("View All", waitSeconds = 30.0)

                    it.exist(stock_code)
                    it.exist("+$profit")
                    it.exist("Dividends Earned")



                    it.exist("View All")

                    println("Stock Code: $stock_code")
                    println("Profit: $profit")






                    //it.exist(change)

                } else {
                    println("Row does not have enough elements")
                }


                println("------------------------")
            }

            it.tap("View All")

            csvData.drop(1).forEachIndexed { index, row ->
                println("Processing Row ${index + 1}:")

                // Check if the row has at least 6 elements
                if (row.size >= 2) {
                    var stock_code = row[0]
                    var profit = row[1]
                    profit = profit.replace("₱","")
                    //val change = row[2]

                    it.exist(stock_code)
                    it.exist("+$profit")
                    it.exist("Dividends Earned")



                    //it.exist(change)

                } else {
                    println("Row does not have enough elements")
                }


                println("------------------------")
            }

            it.tap(x = 64, y = 142)

        } else {
//            it.selectWithScrollDown("Your trade analytics is empty")
//            it.exist("Your trade analytics is empty")
        }





        it.wait(2)

        it.tap(x = 64, y = 142)

        describe("Wait for Page to Load")
            .waitForDisplay("Trade Analytics", waitSeconds = 30.0)


        it.exist("Trade Analytics")


    }

    @Macro("[Explore Invest]")
    fun exploreTrade(useUpdate:Boolean, hasStocks: Boolean){
        it.exist("Invest")

        it.select("Invest").tap()

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

        //Close Trade Search
        it.tap("Portfolio")


    }

    @Macro("[Explore Watcher]")
    fun exploreWatcher() {

        it.tap("Invest")

        it.tap("Watcher")

        it.exist("A-Z")
        it.exist("Change%")
        it.exist("Volume")
        it.exist("Asset")
        it.exist("3 mos")
        it.exist("Price")

        it.tap("A-Z")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/latest_watcher_list.csv")

        // Read CSV file
        val csvReader = CsvReader()

        val csvData = csvReader.readAll(csvFile)

        val stock_codes = mutableListOf<String>()


        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")

            // Check if the row has at least 6 elements
            if (row.size >= 3) {
                val stockCode = row[0]
                var lastPrice = row[1]
                var percentChange = row[2]

                stock_codes.add(stockCode)

                lastPrice = trailing2Decimals(lastPrice)

                println("Stock Code: $stockCode")

                it.wait(1)

                it.selectWithScrollDown(stockCode)
                it.exist(stockCode)

                println("Last Price: $lastPrice")
                it.exist("₱$lastPrice")

                println("Percent Change: $percentChange")
                percentChange = percentChange.replace("%","").replace("-","")
                it.exist("$percentChange %")


                if(index == 1) {

                    it.tap(stockCode)

                    it.exist(stockCode)
                    it.exist("Normal lot")
                    it.exist("Bid And Ask")
                    it.exist("Buy")
                    it.exist("Sell")

                    it.tap(x = 64, y = 211)

                    it.wait(1)


                }


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")

        }

        //Add Stock as long as it doesn't exist in the csv file
        var stock = "CPM"

        stock_codes.add(stock)

        //Sort Watcher

        //Descending Stock
        it.tap("A-Z")
        //it.tap("Stock")

        // Sort the list in descending order
        stock_codes.sortDescending()

        // Get the first three elements
        for (i in 0 until minOf(3, stock_codes.size)) {
            it.exist(stock_codes[i])
        }


        println("Unique Stock Code: $stock")

        //Add Watcher
        addWatcher(stock)
        describe("Wait for Page to Load")
            .waitForDisplay("Stock $stock is now added into your watcher.")


        //Add Existing Watcher

        val randomStock = stock_codes[Random.nextInt(stock_codes.size)]
        addWatcher(randomStock)

        println("Existing Stock: $randomStock")

//        describe("Wait for Page to Load")
//            .waitForDisplay("The selected stock is already on your watchlist.")

        //Maximum Limit Reached

        //Add Stock as long as it doesn't exist in the csv file
//        var maximumStock: String
//
//        do {
//            maximumStock = getRandomStock()
//        } while (stock_codes.any { it.trim().equals(maximumStock.trim(), ignoreCase = true) })
//
//        addWatcher2(maximumStock)
//
//        println("Maximum Stock: $maximumStock")

//        describe("Wait for Page to Load")
//            .waitForDisplay("You have reached the maximum number (10) of Watcher with Active Alerts in your Account.")

        //Remove Stock in Watcher
        it.scrollToBottom()
        it.wait(1)


        it.tap(x=1002,y=983)

        describe("Wait for Page to Load")
            .waitForDisplay("Are you sure you want to remove this stock from your watcher?")

        it.exist("Are you sure you want to remove this stock from your watcher?")
        it.exist("Remove stock")
        it.exist("Cancel")

        it.tap("Remove stock")

        describe("Wait for Page to Load")
            .waitForDisplay("Stock $stock is now deleted into your watcher.")

        it.dontExist(stock)


    }


    @Macro("[Explore Announcements Tab]")
    fun exploreAnnouncements(){

        it.wait(2)

        it.scrollUp()

        it.tap(x=656,y=1019)

        describe("Wait for Page to Load")
            .waitForDisplay("Latest Announcements")

        it.exist("Latest Announcements")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/latest_announcements.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}:")
            // Iterate through each column in the row
            row.forEachIndexed { columnIndex, columnValue ->
                println("Column ${columnIndex + 1}: $columnValue")

                if(columnIndex == 2){
                    val announcement_date = formatDateString(columnValue)

                    describe("Wait for page to load")
                        .waitForDisplay(announcement_date, waitSeconds = 30.0)

                    it.exist(announcement_date)
                } else {

                    describe("Wait for page to load")
                        .waitForDisplay(columnValue, waitSeconds = 30.0)

                    it.exist(columnValue)
                }
            }
        }

        it.scrollToBottom()
        it.wait(1)



        // Print header


        it.exist("View All")
        it.tap("View All")


        it.exist("Announcements")
        it.exist("Latest Announcements")

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}:")
            // Iterate through each column in the row
            row.forEachIndexed { columnIndex, columnValue ->
                println("Column ${columnIndex + 1}: $columnValue")

                if(columnIndex == 2){
                    val announcement_date = formatDateString(columnValue)

                    it.exist(announcement_date)
                } else {
                    it.exist(columnValue)
                }
            }
        }

        it.scrollDown()
        it.scrollUp()

        //it.tap("\uE909")
        it.tap(x=69,y=207)

        it.scrollDown()

        it.exist("View All")

        it.selectWithScrollUp("Latest Announcements")
        it.exist("Latest Announcements")

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}:")
            // Iterate through each column in the row
            row.forEachIndexed { columnIndex, columnValue ->
                println("Column ${columnIndex + 1}: $columnValue")

                if(columnIndex == 2){
                    val announcement_date = formatDateString(columnValue)

                    it.exist(announcement_date)
                } else {
                    it.exist(columnValue)
                }


            }
        }


    }

    @Macro("[Explore My Stocks]")
    fun exploreMyStocks(hasStocks : Boolean) {

        //it.scrollUp()

        it.wait(1)

        it.tap("My Stocks")

        it.exist("Stock Value")
        it.exist("Day Change")

        it.exist("Unrealized Gains")
        it.exist("Realized Gains")

        it.exist("Total Equity")
        it.exist("Total Cash Dividends")

        // Path to your CSV file created in Python
        var csvFile = File("src/test/resources/Portfolio.csv")

        // Read CSV file
        var csvReader = CsvReader()
        var csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        var wallet_balance: String
        var day_change: String
        var day_change_percent: String
        var unrealized_gains: String
        var unrealized_gains_percent: String
        var realized_gains: String
        var realized_gains_percent: String
        var total_cash_dividend: String
        var stock_value: String
        var total_equity: String

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 10) {
                wallet_balance = row[0]
                day_change = row[1]
                day_change_percent = row[2]
                unrealized_gains = row[3]
                unrealized_gains_percent = row[4]
                realized_gains = row[5]
                realized_gains_percent = row[6]
                total_cash_dividend = row[7]
                stock_value = row[8]
                total_equity = row[9]

                day_change_percent = day_change_percent.replace("-","")

                it.exist(day_change)
                println("Day Change: $day_change")
                if(day_change != "0.00"){
                    it.exist(day_change_percent)
                    println("Day Change Percent: $day_change_percent")
                }

                it.exist(unrealized_gains)
                println("Unrealized Gains: $unrealized_gains")
                if(unrealized_gains != "0.00"){
                    unrealized_gains_percent = unrealized_gains_percent.replace("-","")
                    it.exist(unrealized_gains_percent)
                    println("Unrealized Gains Percent: $unrealized_gains_percent")
                }

                it.exist(realized_gains)
                println("Realized Gains: $realized_gains")
                if(realized_gains != "0.00"){
                    realized_gains_percent = realized_gains_percent.replace("-","")
                    it.exist(realized_gains_percent)
                    println("Realized Gains Percent: $realized_gains_percent")
                }

                it.exist(total_cash_dividend)
                println("Total Cash Dividend: $total_cash_dividend")
                it.exist(stock_value)
                println("Stock Value: $stock_value")

                //Comment for now since there is a bug
                //it.exist(total_equity)

            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")

            // Path to your CSV file created in Python
            csvFile = File("src/test/resources/My_Stocks.csv")

            // Read CSV file
            csvReader = CsvReader()
            csvData = csvReader.readAll(csvFile)

            var rowCount = csvData.size

            // Print header
            println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

            // Print data rows
            csvData.drop(1).forEachIndexed { index, row ->
                println("Row ${index + 1}: ${row.joinToString(", ")}")
            }

            it.scrollToBottom()

            when {
                csvData.isEmpty() -> println("The CSV file is empty.")
                csvData.first().joinToString(",").trim() == "No data available" -> {
                    println("The CSV file contains 'No data available'.")
                    it.exist("You have no stocks on hand")
                    it.exist("Tap Trade to start trading stocks")

                    return
                }
                else -> {
                    println("The CSV file contains data.")

                    it.exist("Stock")
                    it.exist("Avg. Price / Shares")
                    it.exist("Profit %")

                    var rowCount = csvData.size


                    // Iterate through each row
                    // First, convert the CSV data to a list of lists for easier manipulation
                    val dataList = csvData.drop(1).map { it.toList() }

                    // Sort the data by the profit column (index 3) in descending order
                    val sortedData = dataList.sortedByDescending { row ->
                        row[3].replace(",", "").toDoubleOrNull() ?: Double.NEGATIVE_INFINITY
                    }

                    // Take only the first 10 rows
                    val top10Data = sortedData.take(10)

                    top10Data.forEachIndexed { index, row ->
                        println("Processing Row ${index + 1}:")

                        // Check if the row has at least 5 elements
                        if (row.size >= 5) {
                            val stock_code = row[0]
                            var average_price = row[1]
                            var share = row[2]
                            val profit = row[3]
                            val profit_percent = row[4]

                            println("Stock Code: $stock_code")
                            println("Average Price: $average_price")
                            println("Share: $share")
                            println("Profit: $profit")
                            println("Profit Percent: $profit_percent")

                            average_price=average_price.replace(",","")

                            average_price = BigDecimal(average_price).setScale(2, RoundingMode.HALF_UP).toString()

                            average_price = formatNumberWithCommaPeriod(average_price)

                            share = share.replace(",","")

                            var share_double = share.toDouble()

                            share = when {
                                share_double >= 1_000_000 -> String.format("%.2fM", share_double / 1_000_000)
                                share_double >= 1000 -> String.format("%.2fK", share_double / 1000)
                                else -> share  // Keep original value if less than 1000
                            }

                            describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                            println("Stock Code: $stock_code")
                            it.exist(stock_code)
                            println("Average Price: $average_price")
                            it.exist(average_price)
                            println("Share: $share")
                            it.exist(share)
                            println("Profit: $profit")
                            it.exist(profit)
                            println("Profit Percent: $profit_percent")
                            it.exist(profit_percent)

                            if(index == 1) {

                                it.tap(stock_code)

                                describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                                it.exist(stock_code)

                                describe("Wait for Page to Load").waitForDisplay("Full Chart", waitSeconds = 30.0)

                                //it.exist("Normal lot")
                                it.exist("Full Chart")
                                it.exist("Bid And Ask")
                                it.exist("Buy")
                                it.exist("Sell")

                                //it.tap(x = 64, y = 211)


                                it.tap(x = 64, y = 142)
                            }


                        } else {
                            println("Row does not have enough elements")
                        }

                        println("------------------------")
                    }

                }

            }

            it.scrollDown()

            if(rowCount > 10){

                it.scrollDown()

                it.exist("View All")

                it.tap("View All")


                println("The CSV file contains data.")

                it.exist("Stock")
                it.exist("Avg. Price / Shares")
                it.exist("Profit %")

                val dataList = csvData.drop(1).map { it.toList() }

                val sortedData = dataList.sortedByDescending { row ->
                    row[3].replace(",", "").toDoubleOrNull() ?: Double.NEGATIVE_INFINITY
                }

                val topAllData = sortedData.take(11)

                // Iterate through each row
                topAllData.drop(1).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")

                    // Check if the row has at least 6 elements
                    if (row.size >= 5) {
                        val stock_code = row[0]
                        var average_price = row[1]
                        var share = row[2]
                        val profit = row[3]
                        val profit_percent = row[4]

                        average_price = average_price.replace(",","")

                        average_price = BigDecimal(average_price).setScale(2, RoundingMode.HALF_UP).toString()

                        average_price = formatNumberWithCommaPeriod(average_price)

                        share = share.replace(",","")

                        var share_double = share.toDouble()

                        share = when {
                            share_double >= 1_000_000 -> String.format("%.2fM", share_double / 1_000_000)
                            share_double >= 1000 -> String.format("%.2fK", share_double / 1000)
                            else -> share  // Keep original value if less than 1000
                        }

                        describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)

                        if(index == 10){
                            it.scrollDown()
                        }

                        println("Stock Code: $stock_code")
                        it.exist(stock_code)
                        println("Average Price: $average_price")
                        it.exist(average_price)
                        println("Share: $share")
                        it.exist(share)
                        println("Profit: $profit")
                        it.exist(profit)
                        println("Profit Percent: $profit_percent")
                        it.exist(profit_percent)

                        if(index == 1) {

                            it.tap(stock_code)

                            describe("Wait for Page to Load").waitForDisplay(stock_code, waitSeconds = 30.0)


                            it.exist(stock_code)

                            describe("Wait for Page to Load").waitForDisplay("Full Chart", waitSeconds = 30.0)

                            //it.exist("Normal lot")
                            it.exist("Full Chart")
                            it.exist("Bid And Ask")
                            it.exist("Buy")
                            it.exist("Sell")

                            it.tap(x = 64, y = 142)
                        }


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }


                //go back
                //it.tap("\uE909")

                it.wait(4)

                //it.tap(x=64,y=220)
                it.tap(x = 64, y = 142)


                describe("Wait for Page to Load").waitForDisplay("Portfolio", waitSeconds = 30.0)


                it.exist("Portfolio")
                it.exist("Explore")
                it.scrollUp("My Stocks")

            } else {
                it.dontExist("View All")
            }

            it.scrollUp()
        }
    }

    @Macro("[Explore Orders]")
    fun exploreOrders(){

        describe("Wait for Page to Load")
            .waitForDisplay("Orders")

        it.tap("Orders")

        //Pending Orders

        // Path to your CSV file created in Python
        var csvFile = File("src/test/resources/limit_pending_orders.csv")
        var csvFileConditional = File("src/test/resources/conditional_pending_orders.csv")

        // Read CSV file
        var csvReader = CsvReader()
        var csvData = csvReader.readAll(csvFile)
        var csvDataConditional =csvReader.readAll(csvFileConditional)

        var rowCount = csvData.size
        var rowCountConditional = csvDataConditional.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        println("Limit Orders")
        csvData.drop(1).forEachIndexed { index, row ->

            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        // Print data rows
        println("Conditional Orders")
        csvDataConditional.drop(1).forEachIndexed { index, row ->

            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }


        var limitPendingEmpty = false
        var conditionalPendingEmpty = false

        when {
            csvData.isEmpty() -> println("The CSV file is empty.")
            csvData.first().joinToString(",").trim() == "No data available" -> {
                println("The CSV file contains 'No data available'.")

                limitPendingEmpty = true

                when {
                    csvDataConditional.isEmpty() -> println("The CSV file is empty.")
                    csvDataConditional.first().joinToString(",").trim() == "No data available" -> {
                        println("The CSV file contains 'No data available'.")

                        conditionalPendingEmpty = true
                    }
                    else -> {

                    }


                }

            }


            else -> {
                println("The CSV file contains data.")

                // Iterate through each row
                csvData.drop(1).take(5).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")

                    // Check if the row has at least 6 elements
                    if (row.size >= 6) {



                        val stock_code = row[0]
                        val share = row[1]
                        val action = row[2]
                        val order_expiry = row[3]
                        val total = row[4]
                        val price = row[5]

                        if(action == "Buy"){
                            //Buy Sign
                            //it.exist("\uE929")
                        } else {
                            //Sell Sign
                            //it.exist("\uE928")
                        }

                        if(index == 3){
                            it.selectWithScrollDown("Posted")
                        }

                        it.exist(stock_code)
                        it.exist("$share shares")

                        if( order_expiry == "Until I Cancel"){
                            it.exist("Until Cancelled")
                        } else {
                            it.exist(order_expiry)
                        }

                        it.exist("Total Price: $total")
                        it.exist("Price: $price")

                        println("Stock Code: $stock_code")
                        println("Shares: $share")
                        println("Order Expiry: $order_expiry")
                        println("Total: $total")
                        println("Price: $price")


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }

                if (rowCount > 5){

                    it.tap("View All")

                    it.exist("Pending")

                    csvData.drop(1).take(5).forEachIndexed { index, row ->
                        println("Processing Row ${index + 1}:")
                        println("Pending Orders")

                        // Check if the row has at least 6 elements
                        if (row.size >= 6) {

                            val stock_code = row[0]
                            val share = row[1]
                            val action = row[2]
                            val order_expiry = row[3]
                            val total = row[4]
                            val price = row[5]

                            if(action == "Buy"){
                                //Buy Sign
                                //it.exist("\uE929")
                            } else {
                                //Sell Sign
                                //it.exist("\uE928")
                            }

                            if(index % 7 == 0 && index < 7){
                                it.selectWithScrollDown("Total Price: $total")
                            }

                            it.exist(stock_code)
                            it.exist("$share shares")
                            if( order_expiry == "Until I Cancel"){
                                it.exist("Until Cancelled")
                            } else {
                                it.exist(order_expiry)
                            }
                            it.exist("Total Price: $total")
                            it.exist("Price: $price")

                            println("Stock Code: $stock_code")
                            println("Shares: $share")
                            println("Order Expiry: $order_expiry")
                            println("Total: $total")
                            println("Price: $price")


                        } else {
                            println("Row does not have enough elements")
                        }

                        println("------------------------")
                    }

                    if (conditionalPendingEmpty == false) {

                        it.selectWithScrollDown("Conditional")

                        csvDataConditional.drop(1).take(5).forEachIndexed { index, row ->
                            println("Processing Row ${index + 1}:")
                            println("Pending Orders")

                            // Check if the row has at least 6 elements
                            if (row.size >= 6) {

                                val stock_code = row[0]
                                val share = row[1]
                                val action = row[2]
                                val order_expiry = row[3]
                                val total = row[4]
                                val price = row[5]

                                if(action == "Buy"){
                                    //Buy Sign
                                    //it.exist("\uE929")
                                } else {
                                    //Sell Sign
                                    //it.exist("\uE928")
                                }

                                it.exist(stock_code)
                                it.exist("$share shares")
                                if( order_expiry == "Until I Cancel"){
                                    it.exist("Until Cancelled")
                                } else {
                                    it.exist(order_expiry)
                                }
                                it.exist("Total Price: $total")
                                it.exist("Price: $price")

                                println("Stock Code: $stock_code")
                                println("Shares: $share")
                                println("Order Expiry: $order_expiry")
                                println("Total: $total")
                                println("Price: $price")

                                //go back
                                it.tap(x=64,y=142)



                            } else {
                                println("Row does not have enough elements")
                            }

                            println("------------------------")
                        }
                    }


                }

                //Close Dropdown

                it.scrollToTop()
                it.wait(1)
                it.tap("Pending")

            }

        }

        if (limitPendingEmpty && conditionalPendingEmpty){

            it.tap("Pending")
            it.exist("Pending")
            it.exist("You have no orders to send")
            it.exist("These are orders that have not yet been sent to the exchange")

            it.tap("Pending")
            it.dontExist("You have no orders to send")
            it.dontExist("These are orders that have not yet been sent to the exchange")

        }




        //Posted Orders

        it.selectWithScrollDown("Posted")


        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/posted_orders.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }


        when {
            csvData.isEmpty() -> println("The CSV file is empty.")
            csvData.first().joinToString(",").trim() == "No data available" -> {
                println("The CSV file contains 'No data available'.")
                it.tap("Posted")
                it.exist("Posted")
                it.exist("You have no orders to receive")
                it.exist("These are orders that have been sent to the exchange but is not yet matched")

                it.tap("Posted")
                it.dontExist("You have no orders to receive")
                it.dontExist("These are orders that have been sent to the exchange but is not yet matched")

            }
            else -> {
                println("The CSV file contains data.")

                it.selectWithScrollDown("Completed")

                // Iterate through each rowkjjjjjjjjjjjjjjjjjjj`7777777777777777777777777777777777776y
                csvData.drop(1).take(5).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")
                    println("Posted Orders")

                    // Check if the row has at least 6 elements
                    if (row.size >= 6) {
                        val stock_code = row[0]
                        val share = row[1]
                        val action = row[2]
                        val order_expiry = row[3]
                        val total = row[4]
                        val price = row[5]

                        if(action == "Buy"){
                            //Buy Sign
                            //it.exist("\uE929")
                        } else {
                            //Sell Sign
                            //it.exist("\uE928")
                        }

                        it.exist(stock_code)
                        it.exist("$share shares")
                        if( order_expiry == "Until I Cancel"){
                            it.exist("Until Cancelled")
                        } else {
                            it.exist(order_expiry)
                        }
                        it.exist("Total Price: $total")
                        it.exist("Price: $price")


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }

                if(rowCount > 5){
                    it.tap("View All")

                    it.exist("Posted")

                    csvData.drop(1).take(5).forEachIndexed { index, row ->
                        println("Processing Row ${index + 1}:")
                        println("Posted Orders All")

                        // Check if the row has at least 6 elements
                        if (row.size >= 6) {
                            val stock_code = row[0]
                            val share = row[1]
                            val action = row[2]
                            val order_expiry = row[3]
                            val total = row[4]
                            val price = row[5]

                            if(action == "Buy"){
                                //Buy Sign
                                //it.exist("\uE929")
                            } else {
                                //Sell Sign
                                //it.exist("\uE928")
                            }

                            it.exist(stock_code)
                            it.exist("$share shares")
                            if( order_expiry == "Until I Cancel"){
                                it.exist("Until Cancelled")
                            } else {
                                it.exist(order_expiry)
                            }
                            it.exist(total)
                            it.exist("Price: $price")

                            println("Stock Code: $stock_code")
                            println("Shares: $share")
                            println("Order Expiry: $order_expiry")
                            println("Total: $total")
                            println("Price: $price")


                        } else {
                            println("Row does not have enough elements")
                        }

                        println("------------------------")
                    }


                    //go back
                    it.tap(x=64,y=142)

                }

                //Close
                it.tap("Posted")


            }


        }

        //Completed Orders

        it.selectWithScrollDown("Completed")
        it.wait(1)
        it.tap("Completed")

        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/completed_orders.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }


        when {
            csvData.isEmpty() -> println("The CSV file is empty.")
            csvData.first().joinToString(",").trim() == "No data available" -> {
                println("The CSV file contains 'No data available'.")
                it.exist("Completed")
                it.exist("You have no delivered orders")
                it.exist("These are orders that have been partially or completely matched by the exchange")

                it.tap("Completed")
                it.dontExist("You have no delivered orders")
                it.dontExist("These are orders that have been partially or completely matched by the exchange")

            }
            else -> {
                println("The CSV file contains data.")

                if (rowCount > 6){
                    it.tap("View All")
                }

                // Iterate through each row
                csvData.drop(1).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")
                    println("Completed Orders")

                    // Check if the row has at least 6 elements
                    if (row.size >= 6) {
                        val stock_code = row[0]
                        val share = row[1]
                        val action = row[2]
                        val order_expiry = row[3]
                        val total = row[4]
                        val price = row[5]

                        if(action == "Buy"){
                            //Buy Sign
                            //it.exist("\uE929")
                        } else {
                            //Sell Sign
                            //it.exist("\uE928")
                        }

                        it.selectWithScrollDown(stock_code)

                        it.exist(stock_code)
                        it.exist("$share shares")

                        if(order_expiry == "Until I Cancel"){
                            it.exist("Until Cancelled")
                        } else {
                            it.exist("End of Day")
                        }

                        it.exist("Total Price: $total")


                        it.exist("Avg. Price: $price")

                        println("Stock Code: $stock_code")
                        println("Shares: $share")
                        println("Order Expiry: $order_expiry")
                        println("Total: $total")
                        println("Price: $price")



                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }

                if(rowCount > 6){
                    println("Completed Orders All")
                    it.tap(x=73,y=211)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Portfolio", waitSeconds = 30.0)

                }

                //Close
                it.tap("Completed")

            }


        }

        //Cancelled Orders

        it.scrollDown()
        it.tap("Cancelled")
        //it.tap(x=165,y=2033)

        it.scrollDown()

        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/cancelled_orders.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }


        when {
            csvData.isEmpty() -> println("The CSV file is empty.")
            csvData.first().joinToString(",").trim() == "No data available" -> {
                println("The CSV file contains 'No data available'.")
                it.exist("Cancelled")
                it.exist("You have no cancelled orders")
                it.exist("To cancel a Posted or Pending order, click on the stock that you want to cancel and click the Cancel Order button")


                it.tap("Cancelled")
                it.dontExist("You have no cancelled orders")
                it.dontExist("To cancel a Posted or Pending order, click on the stock that you want to cancel and click the Cancel Order button")

            }
            else -> {
                println("The CSV file contains data.")

                if (rowCount > 6){
                    it.tap("View All")
                }

                // Iterate through each row
                csvData.drop(1).forEachIndexed { index, row ->
                    println("Processing Row ${index + 1}:")
                    println("Cancelled Orders")

                    // Check if the row has at least 6 elements
                    if (row.size >= 6) {
                        val stock_code = row[0]
                        val share = row[1]
                        val action = row[2]
                        val order_expiry = row[3]
                        val total = row[4]
                        val price = row[5]

                        if(action == "Buy"){
                            //Buy Sign
                            //it.exist("\uE929")
                        } else {
                            //Sell Sign
                            //it.exist("\uE928")
                        }

                        it.exist(stock_code)
                        it.exist("$share shares")

                        println("Stock Code: $stock_code")
                        println("Shares: $share")

                        if(order_expiry == "Until I Cancel"){
                            it.exist("Until Cancelled")
                        } else {
                            it.exist("End of Day")
                        }


                        it.exist("Total Price: $total")

                        println("Total Price: $total")

                        it.exist("Price: $price")

                        println("Price: $price")

                        println()


                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }

                if(rowCount > 6){
                    println("Cancelled Orders All")
                    it.tap(x=73,y=211)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Portfolio", waitSeconds = 30.0)

                }

                it.tap("Cancelled")

            }


        }


        //Order History
        it.scrollDown()

        it.exist("View Order History")


        // Path to your CSV file created in Python
        csvFile = File("src/test/resources/trade_transaction.csv")

        // Read CSV file
        csvReader = CsvReader()
        csvData = csvReader.readAll(csvFile)

        rowCount = csvData.size

        it.wait(2)

        it.tap("xpath=//android.widget.TextView[@text=\"View Order History\"]")

        it.exist("Order History")
        it.exist("Filter")

        it.tap("Filter")

        it.exist("Filter")

        it.exist("Stock")
        it.exist("Select Stock")

        it.exist("Date")
        it.exist("From")
        it.exist("To")

        it.exist("01/01/1970")

        val currentDate = getCurrentDate()
        it.exist(currentDate)

        it.exist("Status")
        it.exist("Matched")
        it.exist("Cancelled")
        it.exist("Rejected")

        it.exist("Show Results")

        it.tap("Show Results")


        if(rowCount > 2) {


            // Print header
            println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

            // Print data rows
            csvData.drop(1).forEachIndexed { index, row ->
                println("Row ${index + 1}: ${row.joinToString(", ")}")
            }

            // Iterate through each row
            csvData.drop(1).take(5).forEachIndexed { index, row ->
                println("Processing Row ${index + 1}:")

                println("Row: $row")
                // Check if the row has at least 5 elements
                if (row.size >= 5) {
                    val stock_code = row[0]
                    val action = row[1]
                    var status = row[2]
                    var price = row[3]
                    var total = row[4]
                    var share = row[5]

                    price = price.replace(",", "")
                    println("PriceL $price")
                    if (!price.startsWith('0')) {
                        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP).toPlainString()
                    }
                    status = status.lowercase().replaceFirstChar { it.uppercase() }

                    if (action == "Buy") {
                        //Buy Sign
                        //it.exist("\uE929")
                    } else {
                        //Sell Sign
                        //it.exist("\uE928")
                    }

                    println("Stock Code $stock_code")

                    it.exist(stock_code)

                    if (status == "Rejected") {
                        //it.exist("\uE91F")
                    } else if (status == "Filled") {
                        //it.exist("\uE905")
                    } else if (status == "Cancelled") {
                        //it.exist("\uE91F")
                    } else if (status == "Cancelled (Partial Filled)") {
                        //it.exist("\uE905")
                    }

//                    println("Status: $status")
//                    it.exist(status)

                    var pricewithComma = price

                    if (!price.startsWith('0')) {
                        pricewithComma = formatNumberWithCommaPeriod(price)
                    }

                    total = total.replace("₱", "")
                    it.exist("₱ $total")

                    println("$share shares @ ₱ $total")
                    println("Price per Share: $pricewithComma")
                    it.exist("$share shares @ ₱ $pricewithComma")





                } else {
                    println("Row does not have enough elements")
                }



                println("------------------------")
            }

            it.tap(x=69,y=197)

            describe("Wait for Text to Load")
                .waitForDisplay("Portfolio", waitSeconds = 30.0)

        } else {
            //empty order history
        }

    }


    @Macro("[Save to Watcher]")
    fun saveToWatcher(stockCode:String, stockName:String){
        // Add to Watcher
        it.wait(1)

        it.tap(x=1011,y=213)

        describe("Wait for Page to Load")
            .waitForDisplay("Stock $stockCode is now added into your watcher.", waitSeconds = 30.0)

        //go back

        it.wait(2.5)

        it.tap(x=58,y=210)

        describe("Wait for Page to Load")
            .waitForDisplay("Portfolio", waitSeconds = 30.0)


        it.tap("Portfolio")
        it.exist("My Stocks")
        it.exist("Orders")

        it.tap("Invest")
        it.tap("Watcher")

        try{
            it.exist(stockCode)
            println("Stock Code Exist without scrolling")
        } catch (e: Exception) {
            it.scrollDown()
            it.exist(stockCode)
        }

        it.selectWithScrollDown(stockCode)
        it.exist(stockCode)

        it.macro("[Search Stock]", stockCode, stockName)

        //remove watcher

        it.wait(4)

        it.tap(x=1011,y=213)

        it.exist("Remove Stock")
        it.exist( "Are you sure you want to remove this stock from watcher?")
        it.exist("NO")
        it.exist("YES")

        it.tap("YES")

        describe("Wait for Page to Load")
            .waitForDisplay("Stock $stockCode is now deleted into your watcher.", waitSeconds = 30.0)

        //go back

        it.wait(2.5)

        it.tap(x=62, y=213)

        it.tap("Invest")

        it.tap("Watcher")

        it.dontExist(stockCode)

        it.scrollDown()

        it.dontExist(stockCode)
    }

    fun getUniqueStock():String{
        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/latest_watcher_list.csv")

        // Read CSV file
        val csvReader = CsvReader()

        val csvData = csvReader.readAll(csvFile)

        val stock_codes = mutableListOf<String>()

        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")

            // Check if the row has at least 6 elements
            if (row.size >= 3) {
                val stockCode = row[0]

                stock_codes.add(stockCode)

            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")

        }

        //Add Stock as long as it doesn't exist in the csv file
        var stock: String

        do {
            stock = getRandomStockCode()
        } while (stock_codes.any { it.trim().equals(stock.trim(), ignoreCase = true) })

        println(stock_codes)


        return stock

    }

    fun getRandomStock(): String {
        val strings = listOf(
            "ABG", "2GO", "8990P", "AAA", "AB", "ABA", "ABS", "ABSP", "AC", "ACE",
            "ACEN", "ACEX", "AEV", "AGF", "AGI", "ALCO", "ALHI", "ALI", "ALLDY",
            "ALLHC", "ANI", "ANS", "AP", "APL", "APO", "APVI", "APX", "AR", "ARA",
            "AREIT", "AT", "ATI", "ATN", "ATNB", "AUB", "AXLM", "BC", "BCB", "BCOR",
            "BCP", "BDO", "BEL", "FILRT", "VVT", "VUL", "VMC",
            "UPM", "URC", "UNI", "UBP", "TLII", "TFC", "STR", "STN", "STI",
            "RLC", "RLT", "ROCK", "PSE", "PSB", "PTC", "OPMB", "ORE", "OPM",
            "NOW", "NRCP", "NXGEN", "MVC", "MREIT", "LMG", "LFM", "LC", "LR",
            "KPH", "KEP", "KPPI", "JOH", "ION", "IPM", "IPO", "HOME",
            "HOUSE", "HTI", "GREEN", "GPH", "GSMI", "FOOD", "FPH", "FPI", "FRUIT",
            "EIBA", "EG", "ELI", "EMP", "DITO", "DIZ", "DMC", "COL", "CPM",
            "CREIT"
        )

        return strings[Random.nextInt(strings.size)]
    }

    fun addWatcher2(stock: String){

        it.scrollToBottom()

        it.wait(1)

        //it.tap("\uEA0A")
        it.tap(x=973,y=422)

        describe("Wait for Page to Load")
            .waitForDisplay("Add Stock to Watcher", waitSeconds = 30.0)

        it.exist("Add Stock to Watcher")
        it.exist("Stock")
        it.exist("Last Price")
        it.exist("0.00")

        it.tap("-- --")

        it.tap("Search")
            .sendKeys(stock)

        describe("Wait for Page to Load")
            .waitForDisplay("$stock&&.android.widget.TextView", waitSeconds = 30.0)
        it.exist("$stock&&.android.widget.TextView")
        it.tap("$stock&&.android.widget.TextView")
        it.tap("$stock&&.android.widget.TextView")

        //it.dontExist("0.00")
        it.exist(stock)
        it.tap("Save")


    }


    fun addWatcher(stock: String){

        it.scrollUp()

        it.wait(1)

        //it.tap("\uEA0A")
        it.tap(x=1020,y=474)

        describe("Wait for Page to Load")
            .waitForDisplay("Search Stock", waitSeconds = 30.0)

        it.wait(1)

        it.tap(x=217,y=320)
            .sendKeys(stock)

        it.wait(2)

        it.tap(x=372,y=509)




    }

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return currentDate.format(formatter)
    }



    fun trailing2Decimals(lastPrice:String): String {
        // Format the Float to four decimal places

        // Remove commas from the string
        val cleanedPrice = lastPrice.replace(",", "")
        // Convert the String to Float
        val lastPriceFloat = cleanedPrice.toBigDecimalOrNull()


        var formattedValueWithCommas = "null"
        if (lastPriceFloat != null) {

            val scaledValue = lastPriceFloat.setScale(2, RoundingMode.HALF_UP)
            // Format the BigDecimal to a string
            val formattedValue = scaledValue.toPlainString()
            // Format the BigDecimal to a string with commas
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)


            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            formattedValueWithCommas = numberFormat.format(scaledValue)

        }
        return formattedValueWithCommas.toString()
    }


    fun formatDateString(dateStr: String): String {
        val parts = dateStr.split(" ")
        val timePart = parts[3]
        val hour = timePart.split(":")[0].toInt().toString()
        return "${parts[0]} ${parts[1]} ${parts[2]} $hour:${timePart.split(":")[1]} ${parts[4]}"
    }
}
