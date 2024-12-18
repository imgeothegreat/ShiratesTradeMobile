package macro

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.io.File

@MacroObject
object Market: TestDrive {

    @Macro("[Tap Month Ranges]")
    fun tapMonthRanges() {
        //tap 1D
        it.tap("1d")

        //tap 3M
        it.tap("3m")

        //tap 6M
        it.tap("6m")

        //tap 1Y
        it.tap("1y")

        //tap 2Y
        it.tap("2y")
    }

    @Macro("[View Full Chart]")
    fun viewFullChart(stockCode :String, stockName:String) {
        describe("Wait for Page to Load")
            .waitForDisplay("Full Chart", waitSeconds = 30.0)


        it.exist("Full Chart")

        it.tap("Full Chart")

        it.wait(1)
        it.scrollUp()

        describe("Wait for Page to Load")
            .waitForDisplay("Date Range", waitSeconds = 30.0)
        it.exist(stockCode)
        it.exist("Date Range")
        it.exist("Toggle Log Scale")

        //Close

        it.wait(3)

        it.tap(x=55,y=191)

        it.scrollUp()

        it.exist(stockCode)



        it.exist(stockName)
        it.exist("Full Chart")
    }

    @Macro("[Explore Sectors]")
    fun exploreSectors(){



        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/sectors.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        var psei_value: String
        var psei_percent:String
        var all_shares: String
        var all_shares_percent:String
        var financials: String
        var financials_percent:String
        var holdings: String
        var holdings_percent:String
        var industrial: String
        var industrial_percent:String
        var mining: String
        var mining_percent:String
        var property: String
        var property_percent:String
        var services: String
        var services_percent:String

        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->
            println("Processing Row ${index + 1}:")

            // Check if the row has at least 11 elements
            if (row.size >= 11) {
                psei_value = row[0]
                psei_percent = row[1]
                all_shares = row[2]
                all_shares_percent = row[3]
                financials = row[4]
                financials_percent = row[5]
                holdings = row[6]
                holdings_percent = row[7]
                industrial = row[8]
                industrial_percent = row[9]
                mining = row[10]
                mining_percent = row[11]
                property = row[12]
                property_percent = row[13]
                services = row[14]
                services_percent = row[15]

                psei_percent = psei_percent.replace("%","")

                it.exist("₱$psei_value")
                it.exist("$psei_percent %")
                // add PSEI Change
                it.exist(  "PSEI")

                val csvFileStats = File("src/test/resources/PSE_Stats.csv")

                // Read CSV file
                val csvReaderStats = CsvReader()
                val csvDataStats = csvReaderStats.readAll(csvFileStats)

                // Print header
                println("CSV File Header: ${csvDataStats.firstOrNull()?.joinToString(", ")}")

                // Print data rows
                csvDataStats.drop(1).forEachIndexed { index, row ->
                    println("Row ${index + 1}: ${row.joinToString(", ")}")

                }

                // Define sector information
                data class SectorInfo(val name: String, val value: String, val percent: String)

                val sectorInfoList = listOf(
                    SectorInfo("PSEI", psei_value, psei_percent),
                    SectorInfo("All Shares", all_shares, all_shares_percent),
                    SectorInfo("Financials", financials, financials_percent),
                    SectorInfo("Holding Firms", holdings, holdings_percent),
                    SectorInfo("Industrial", industrial, industrial_percent),
                    SectorInfo("Mining", mining, mining_percent),
                    SectorInfo("Property", property, property_percent),
                    SectorInfo("Services", services, services_percent)
                )


                it.scrollDown()

                csvDataStats.drop(1).zip(sectorInfoList).forEachIndexed { index, (row, sectorInfo) ->
                    val (sectorName, sectorValue, sectorPercent) = sectorInfo
                    println("Processing Row ${index + 1} for sector $sectorName:")

                    // Check if the row has at least 14 elements
                    if (row.size >= 14) {
                        val stock_code = row[0]
                        val current_price = row[1]
                        val pclose = row[3]
                        val low = row[4]
                        val volume = row[5]
                        val f52wk_low = row[6]
                        val market_cap = row[7]
                        val nfbs = row[8]
                        val open = row[9]
                        val high = row[10]
                        val shares = row[11]
                        val f52wk_high = row[12]
                        val trades = row[13]




                        describe("Wait for Sector To Show")
                            .waitForDisplay(sectorName, waitSeconds = 30.0)

                        it.tap(sectorName)

                        it.wait(1)

                        describe("Wait for Page to Load")
                            .waitForDisplay(sectorName, waitSeconds = 30.0)


                        it.exist(sectorName)

                        it.exist("₱$sectorValue")
                        //it.exist("*($sectorPercent)")

                        //it.tap(current_price)
                        println("Sector $sectorName")

                        println("Current Price: $current_price")


                        describe("Wait for Page to Load")
                            .waitForDisplay("₱$current_price", waitSeconds = 30.0)

                        // Existing checks
                        it.exist("₱$current_price")
                        it.exist("P. Close")
                        it.exist(pclose)
                        it.exist("Low")
                        it.exist(low)
                        it.exist("Volume")
                        it.exist(volume)
                        it.exist("52wk Low")
                        it.exist(f52wk_low)
                        it.exist("Market Cap")
                        it.exist(market_cap)
                        it.exist("NFB/S")
                        it.exist(nfbs)
                        it.exist("Open")
                        it.exist(open)
                        it.exist("High")
                        it.exist(high)
                        it.exist("Shares")
                        it.exist(shares)
                        it.exist("52wk High")
                        it.exist(f52wk_high)
                        it.exist("Trades")
                        it.exist(trades)
                        //it.dontExist("Avg. Price")

                        //Tap Date Ranges
                        it.exist("1d")
                        it.exist("3m")
                        it.exist("6m")
                        it.exist("1y")
                        it.exist("2y")

                        //Tap Search
                        //it.tap("\uE911")
                        it.tap(x=890,y=211)

                        describe("Wait for Home Page")
                            .waitForDisplay("Search Stock", waitSeconds = 30.0)


                        it.exist("Search Stock")
                        it.exist("Search")
                        //it.exist("\uE908")
                        it.exist("No Stock Result")
                        //it.tap("\uE908")
                        it.tap(x=987,y=207)

                        it.wait(1)

                        //Line Chart

                        it.tap(x=679,y=1189)

                        //Normal Chart

                        it.tap(x=757, y=1189)

                        if(sectorName == "PSEI"){
                            it.macro("[View Full Chart]", "PSEI", "Philippines Stock Exchange Index")
                        } else if (sectorName == "Mining"){
                            it.macro("[View Full Chart]", stock_code, "Mining and Oil")
                        }  else {
                            it.macro("[View Full Chart]", stock_code, sectorName)
                        }

                        // Sector-specific checks

                        // Close
                        //it.tap("\uE944")

//
                        if (sectorName == "Mining"){
                            it.exist("Mining and Oil")
                        } else {
                            it.exist(sectorName)
                        }


                        //back
                        it.tap(x=60,y=216)

//                        describe("Wait for page to load")
//                            .waitForDisplay(sectorValue, waitSeconds = 30.0)
//
//                        describe("Wait for page to load")
//                            .waitForDisplay(sectorPercent, waitSeconds = 30.0)
//
//                        it.exist(sectorValue)
//                        it.exist(sectorPercent)

                    } else {
                        println("Row does not have enough elements")
                    }

                    println("------------------------")
                }



            }
        }


    }

    @Macro("[Explore Market Picks]")
    fun exploreMarketPicks(){


        var stock_code:String
        var last_price:String
        var change:String
        var volume:String

        it.exist("Top Gainers")
        it.exist("Highest stock risers")
        it.tap("Top Gainers")

        it.exist("Top Gainers")
        it.exist("Asset")
        it.exist("3 mos")
        it.exist("Price")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/top_gains.csv")

        // Read CSV file
        val csvReader = CsvReader()
        val csvData = csvReader.readAll(csvFile)

        // Print header
        println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvData.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        println("Top Gainers")
        // Iterate through each row
        csvData.drop(1).forEachIndexed { index, row ->

            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 4) {
                stock_code = row[0]
                last_price = row[1]
                change = row[2]
                volume = row[3]

                describe("Wait for page to load")
                    .waitForDisplay(stock_code, waitSeconds = 30.0)

                it.exist("Vol:")
                it.exist(volume)
                it.exist(stock_code)
                it.exist("₱$last_price")
                it.exist(change)

                if(index == 1) {

                    it.tap(stock_code)

                    it.exist(stock_code)
                    it.exist("Normal lot")
                    it.exist("Bid And Ask")
                    it.exist("Buy")
                    it.exist("Sell")

                    it.tap(x = 64, y = 211)



                }





            } else {
                println("Row does not have enough elements")

            }

            println("------------------------")


        }


        it.exist("Top Losers")
        it.exist("Biggest stock declines")

        it.tap("Top Losers")

        it.exist("Top Losers")
        it.exist("Asset")
        it.exist("3 mos")
        it.exist("Price")

        it.wait(1)

        // Path to your CSV file created in Python
        val csvFileLosers = File("src/test/resources/top_losers.csv")

        // Read CSV file
        val csvReaderLosers = CsvReader()
        val csvDataLosers = csvReaderLosers.readAll(csvFileLosers)

        // Print header
        println("CSV File Header: ${csvDataLosers.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvDataLosers.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        println("Top Losers")
        // Iterate through each row
        csvDataLosers.drop(1).forEachIndexed { index, row ->

            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 4) {
                stock_code = row[0]
                last_price = row[1]
                change = row[2]
                volume = row[3]

                describe("Wait for page to load")
                    .waitForDisplay(stock_code, waitSeconds = 30.0)

                it.exist("Vol:")
                it.exist(volume)
                it.exist(stock_code)
                it.exist("₱$last_price")
                it.exist(change)

                if(index == 1) {

                    it.tap(stock_code)

                    it.exist(stock_code)
                    it.exist("Normal lot")
                    it.exist("Bid And Ask")
                    it.exist("Buy")
                    it.exist("Sell")

                    it.tap(x = 64, y = 211)


                }


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")


        }

        describe("Wait for Page to Load")
            .waitForDisplay("Most Active", waitSeconds = 30.0)


        it.exist("Most Active")
        it.exist("Highest trading volume")

        it.tap("Most Active")

        it.exist("Most Active")
        it.exist("Asset")
        it.exist("3 mos")
        it.exist("Price")

        it.wait(1)


        // Path to your CSV file created in Python
        val csvFileActive = File("src/test/resources/top_active.csv")

        // Read CSV file
        val csvReaderActive = CsvReader()
        val csvDataActive = csvReaderActive.readAll(csvFileActive)

        // Print header
        println("CSV File Header: ${csvDataActive.firstOrNull()?.joinToString(", ")}")

        // Print data rows
        csvDataActive.drop(1).forEachIndexed { index, row ->
            println("Row ${index + 1}: ${row.joinToString(", ")}")
        }

        println("Top Active")
        // Iterate through each row
        csvDataActive.drop(1).forEachIndexed { index, row ->

            println("Processing Row ${index + 1}:")

            // Check if the row has at least 6 elements
            if (row.size >= 3) {
                stock_code = row[0]
                last_price = row[1]
                change =  row[2]
                volume = row[3]

                describe("Wait for page to load")
                    .waitForDisplay(stock_code, waitSeconds = 30.0)

                it.exist("Vol:")
                it.exist(volume)
                it.exist(stock_code)
                it.exist("₱$last_price")
                it.exist(change)

                if(index == 1) {

                    it.tap(stock_code)

                    it.exist(stock_code)
                    it.exist("Normal lot")
                    it.exist("Bid And Ask")
                    it.exist("Buy")
                    it.exist("Sell")

                    it.tap(x = 64, y = 211)



                }


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")


        }

    }
}