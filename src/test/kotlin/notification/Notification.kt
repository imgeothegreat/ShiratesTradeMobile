package notification

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import java.io.File
import kotlin.test.Ignore
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Notification : UITest()  {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var pin = "123123"
    private val useUpdate = false
    private val hasMinimumDeposit = false
    private val broker = "2"

    @Test
    @DisplayName("Cash In Notification")
    @Ignore
    @Order(1)
    fun cashInNotif() {
        scenario {
            //Missing @
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Latest Notification")
    @Order(2)
    fun latestNotif() {
        scenario {
            //Missing @
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    //Read Latest Notif
                    describe("Wait for Page to Load")
                        .waitForDisplay("\uE917", waitSeconds = 30.0)


                    //tap notif icon
                    it.tap("\uE917")

                    it.exist("Notifications")

                    //x icon
                    it.exist("\uE908")

                    // Path to your CSV file created in Python
                    val csvFile = File("src/test/resources/Latest_notifications.csv")

                    // Read CSV file
                    val csvReader = CsvReader()
                    val csvData = csvReader.readAll(csvFile)

                    // Print header
                    println("CSV File Header: ${csvData.firstOrNull()?.joinToString(", ")}")

                    // Print data rows
                    csvData.drop(1).forEachIndexed { index, row ->
                        println("Row ${index + 1}: ${row.joinToString(", ")}")
                    }

                    csvData.drop(1).forEachIndexed { index, row ->

                        println("Processing Row ${index + 1}:")

                        // Check if the row has at least 6 elements
                        if (row.size >= 2) {
                            val main_message = row[0]
                            val second_message = row[1]

                            it.exist(main_message)
                            it.exist(second_message)

                        } else {
                            println("Row does not have enough elements")

                        }

                        println("------------------------")


                    }

                    //tap close
                    it.tap("\uE908")

                    it.dontExist("Notifications")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }

    }
}