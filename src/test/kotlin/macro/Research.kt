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
object Research: TestDrive {

    @Macro("[Financial Reports]")
    fun financialReports(pin:String, firstName:String){

        describe("Wait for Page to Load")
            .waitForDisplay("Financial Report")

        it.tap("Financial Report")
        it.exist("Financial Report")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/financial_reports.csv")

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

            // Check if the row has at least 2 elements
            if (row.size >= 2) {
                val title = row[0]
                val description = row[1]

                describe("Wait for Page to Load")
                    .waitForDisplay(title)

                it.exist(title)
                print("Title $title")
                it.exist(description)
                print("Description $description")


                //it.exist("View Financial Report")

                val value = index +1

                //it.tap("xpath=(//android.widget.TextView[@text=\"View Financial Report\"])[$value]")

                it.tap(description)

                it.wait(1)

                //it.dontExist("Financial Report")
//                it.dontExist(title)
//                it.dontExist(description)

                it.launchApp()
                it.macro("[Lock Trading Pin]", pin, firstName)

                describe("Wait for Home Page")
                    .waitForDisplay("Research", waitSeconds = 30.0)

                it.tap("Research")
                it.tap("Financial Report")

                println("Processed Title: $title")
                println("Processed Description: $description")


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")
        }

    }

    @Macro("[Disclosure]")
    fun disclosure(pin:String, firstName:String){

        describe("Wait for Page to Load")
            .waitForDisplay("Disclosure")

        it.tap("Disclosure")
        it.exist("Disclosure")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/disclosures.csv")

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
            if (row.size >= 2) {
                val title = row[0]
                val description = row[1]

                describe("Wait for Page to Load")
                    .waitForDisplay(title)

                it.exist(title)
                print("Title $title")
                it.exist(description)
                print("Description $description")


                //it.exist("View Disclosure")

                val value = index +1

                //it.tap("xpath=(//android.widget.TextView[@text=\"View Disclosure\"])[$value]")
                it.tap(description)

                it.wait(1)

               // it.dontExist("Disclosure")
//                it.dontExist(title)
//                it.dontExist(description)

                it.launchApp()
                it.macro("[Lock Trading Pin]", pin, firstName)

                describe("Wait for Home Page")
                    .waitForDisplay("Research", waitSeconds = 30.0)

                it.tap("Research")
                it.tap("Disclosure")

                println("Processed Title: $title")
                println("Processed Description: $description")


            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")
        }

    }

    @Macro("[Broker Research]")
    fun brokerResearch() {
        describe("Wait for Page to Load").waitForDisplay("Research", waitSeconds = 30.0)


        it.tap("Research")

        it.tap("Broker Research")


        //Recent Research
        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/latest_broker_research.csv")

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
            if (row.size >= 3) {
                val title = row[0]
                val description = row[1]
                val stock_codes = row[2]

                describe("Wait for Page to Load").waitForDisplay(title, waitSeconds = 30.0)


                it.exist(title)
                print("Title $title")
                //it.exist(description)
                print("Description $description")

                val stockCodesList = stock_codes.split(",").map { it.trim() }

                if (stock_codes.isNotEmpty()) {

                    for (stockCode in stockCodesList) {
                        it.exist(stockCode)
                        print("Stock Code: stockCode")
                    }

                }
                it.exist("View Research")

                val value = index +1

                describe("Wait for Page to Load").waitForDisplay("xpath=(//android.widget.TextView[@text=\"View Research\"])[$value]", waitSeconds = 30.0)


                it.tap("xpath=(//android.widget.TextView[@text=\"View Research\"])[$value]")

                it.wait(1)

                it.exist(title)
                it.dontExist(description)

                for (stockCode in stockCodesList) {
                    it.dontExist("$$stockCode")
                }

                //it.tap("\uE908")
                it.tap(x=55,y=193)



                println("Processed Title: $title")
                println("Processed Description: $description")
                println("Processed Stock Codes: $stock_codes")

            } else {
                println("Row does not have enough elements")
            }

            println("------------------------")
        }
    }
}