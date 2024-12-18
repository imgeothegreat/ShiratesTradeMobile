package macro

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import macro.Common.it
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.io.File
import java.util.*

@MacroObject
object Common : TestDrive {

    @Macro("[Login]")
    fun loginUser(email: String, password: String ) {

        describe("Wait for Page to Load")
            .waitForDisplay("Log In", waitSeconds = 60.0)

        it.tap("Log In")
        it.tap("Your email address")
            .sendKeys(email)
        it.tap("Your password")
            .sendKeys(password)
        it.tap("Log In")

    }

    fun encodeToBase64(input: String): String {
        return Base64.getEncoder().encodeToString(input.toByteArray())
    }

    fun decodeFromBase64(encoded: String): String {
        return String(Base64.getDecoder().decode(encoded))
    }

    @Macro("[Read Latest Notifs]")
    fun readLatestNotifs(){

        it.wait(2)

        //tap notif icon

        it.tap(x=1000, y=197)

        describe("Wait for Toast Message")
            .waitForDisplay("Notifications")


        it.exist("Notifications")

        //x icon
        //it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")

        // Path to your CSV file created in Python
        val csvFile = File("src/test/resources/latest_notifications.csv")

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
                println(main_message)

                if(second_message != "empty"){
                    it.exist(second_message)
                    println(second_message)
                } else{
                    println("Notif row does not have secondary message")
                }


            } else {
                println("Row does not have enough elements")

            }

            println("------------------------")


        }

        //tap close
        it.tap(x=982,y=197)

        it.wait(2)


        }

    @Macro("[Select Broker]")
    fun selectBroker(broker: String){

        it.wait(2)

        describe("Wait for Broker Text To Appear")
            .waitForDisplay("Select Broker", waitSeconds = 30.0)

        var brokerNumber = "a";

        describe("Wait for Broker Text To Appear")
            .waitForDisplay("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup", waitSeconds = 30.0)

        if (broker == "1" ){
            brokerNumber = "xpath=//android.widget.TextView[@text=\"Investors Securities, Inc.\"]"
        } else if (broker == "2"){
            //brokerNumber = "xpath=//android.widget.TextView[@text=\"Fidelity Securities, Inc.\"]"
            brokerNumber = "xpath=//android.widget.TextView[@text=\"Broker 2\"]"
        } else if (broker == "3"){
            brokerNumber = "xpath=//android.widget.TextView[@text=\"Broker 3\"]"
        } else if (broker == "4"){
            brokerNumber = "xpath=//android.widget.TextView[@text=\"Broker 4\"]"
        }

        //Tap Broker Selected
        it.tap(brokerNumber)


    }

    @Macro("[Lock Trading Pin]")
    fun lockTradingPin(pin: String, firstName: String){

        describe("Wait for Page to Load")
            .waitForDisplay("Hi $firstName!", waitSeconds = 30.0)

        it.exist("Hi $firstName!")
        it.exist("Enter your PIN to unlock")

        //circle
        //it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[2]")

        //last circle
        //it.exist("xpath=//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[7]")

        it.exist("Not $firstName?")
        it.exist("Log out")
        it.exist("Help Center")
        it.exist("Forgot PIN?")

        pin.forEachIndexed { index, char ->
            it.tap("$char")
        }
    }

    @Macro("[Enter Trading Pin]")
    fun enterTradingPin(pin: String, useUpdate: Boolean){

        it.exist("Enter Trading Pin")
        it.exist("Kindly enter your 6-digit trading PIN")
        it.exist( "Forgot PIN? Get help here")

        describe("Wait for Page to Load")
            .waitForDisplay("1", waitSeconds = 30.0)

        it.exist("1")

        pin.forEachIndexed { index, char ->
            it.tap("$char")
        }


    }

    @Macro("[Enter Trading Pin Only]")
    fun enterTradingPinOnly(pin: String, has200KWithdraw:Boolean){

        //Enter Trading Pin
        it.tap("xpath=//android.widget.EditText[@resource-id=\"tradingPin\"]")
            .sendKeys(pin)

        it.hideKeyboard()

        if(has200KWithdraw){

            //Tap without pin
            it.tap("xpath=(//android.widget.Button[@text=\"Next\"])[2]")

            //OTP when more than 200k
            it.tap("xpath=//android.widget.EditText[@resource-id=\"otp\"]")
                .sendKeys("202070")

            it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash Out\"]").tap()

            it.exist("Review Cash Out Transaction")

            it.tap("xpath=//android.widget.EditText[@resource-id=\"otp\"]")
                .clearInput()

            it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash Out\"]").tap()

            it.exist("Review Cash Out Transaction")

            it.tap("xpath=//android.widget.EditText[@resource-id=\"otp\"]")
                .clearInput()
                .sendKeys("20207")

            it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash Out\"]").tap()

            it.exist("Review Cash Out Transaction")

            it.tap("xpath=//android.widget.EditText[@resource-id=\"otp\"]")
                .clearInput()
                .sendKeys("202071")

            it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash Out\"]").tap()


        } else {

            it.tap("xpath=//android.widget.Button[@text=\"Submit\"]")
        }

    }

    @Macro("[Search Stock]")
    fun searchStock(stockCode:String, stockName:String){

        it.wait(3)

//        it.tap(x=886, y=193)
        tapHomeSearch()

        it.wait(2)

//        it.tap(x=216, y=330)
        it.tap(x=216, y=271)
            .sendKeys(stockCode)

        //Tap Stock Name
        describe("Wait for Page to Load")
            .waitForDisplay(stockName)

        it.tap(stockName)

        describe("Wait for Page to Load")
            .waitForDisplay(stockCode)

        it.exist(stockCode)
        it.exist(stockName)

        describe("Wait for Page to Load")
            .waitForDisplay("Full Chart")
        it.exist("Full Chart")


    }

    @Macro("[Check Notification Content]")
    fun checkNotif(message:String){

        describe("Wait for Page to Load")
            .waitForDisplay("\uE917", waitSeconds = 30.0)

        it.wait(7)
        it.exist("\uE917")

        it.tap("\uE917")
        it.exist("Notifications")
        it.exist("\uE908")

        it.exist("xpath=//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.ImageView")
        it.exist(message)

        it.tap("\uE908")

    }

    @Macro("[Exit App]")
    fun exitApp(firstName:String){

        it.scrollUp()

        it.swipePointToPoint(startX=1055, startY=1069, endX=560, endY =1069 )

        it.exist("Exit")
        it.exist("Are you sure you want to exit InvestaTrade App?\n")
        it.exist("NO")
        it.exist("YES")

        it.tap("NO")

        it.exist("Trade Analytics")
        it.exist("Invest")

        it.swipePointToPoint(startX=1055, startY=1069, endX=560, endY =1069 )

        it.exist("Exit")
        it.exist("Are you sure you want to exit InvestaTrade App?\n")
        it.exist("NO")
        it.exist("YES")

        it.tap("YES")

        it.launchApp()

        describe("Wait for Page to Load")
            .waitForDisplay("Hi $firstName!")

        it.exist( "Hi $firstName!")
        it.exist(   "Enter your PIN to unlock")
        it.exist("Not $firstName?")
        it.exist(  "Log out")
        it.exist("Help Center")
        it.exist("Forgot PIN?")

        it.tap("Log out")

        it.exist("Let’s Get Started")

    }


    @Macro("[Log Out]")
    fun logOut(){
        describe("Wait for Page to Load")
            .waitForDisplay("Enter your PIN to unlock", waitSeconds = 30.0)

        it.exist("Log out")
        it.tap("Log out")
    }

    @Macro("[Install Code Push First]")
    fun installCodePushFirst(email: String, pword:String, pin: String, broker: String, useUpdate: Boolean){
        it.macro("[Login]", email, pword)
        it.macro("[Select Broker]", broker)
        it.macro("[Enter Trading Pin]", pin, useUpdate)

        describe("Wait for Home Page")
            .waitForDisplay("Trade Analytics", waitSeconds = 30.0)

        it.tap("Market")
        it.tap("Portfolio")

        describe("Wait for Home Page")
            .waitForDisplay("INSTALL UPDATE")

        it.exist("INSTALL UPDATE")

        it.tap("INSTALL UPDATE")

        describe("Wait for Home Page")
            .waitForDisplay("Enter your PIN to unlock", waitSeconds = 180.0)

        it.exist("Enter your PIN to unlock")

        it.tap("Log out")

        describe("Wait for Home Page")
            .waitForDisplay("Create an Account")

        it.exist("Create an Account")
    }

    @Macro("[Refresh Page]")
    fun refreshPage(){

        //go to top page first
        it.scrollUp()
        it.scrollUp()

        it.wait(1)
        it.swipePointToPoint(startX = 537, startY = 349, endX = 537, endY = 1446)

        it.wait(2)
    }

}

fun tapHomeSearch(){
    it.tap(x=866, y=197)
    //it.tap(x=866,y=128)
}

fun tapBackButton(){
    it.tap(x=59,y=176)
    //it.tap(x=59,y=128)
}

fun closeCodePush(useUpdate:Boolean){

    if(useUpdate){

        it.wait(3.0)
        //it.tap("\uE908")


    }


}


