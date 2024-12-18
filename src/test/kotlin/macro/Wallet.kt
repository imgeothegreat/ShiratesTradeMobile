package macro

import macro.Onboarding.changeLastDigit
import macro.Onboarding.getRandomBoolean
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random
import kotlin.test.assertTrue

@MacroObject
object Wallet : TestDrive {
    @Macro("[Create Cash In]")
    fun createCashIn(depositAmt: String, cashInMethod: String){
        it.tapWithScrollDown("Available Cash")
        it.tapWithScrollDown("Cash In")
        it.tapWithScrollDown("0.00")
            .sendKeys(depositAmt)
        it.tapWithScrollDown(cashInMethod)
        it.tapWithScrollDown("Next")
    }

    @Macro("[Enter Details Cash Out]")
    fun enterDetailsCashOut(withdrawAmt: String){

        it.tap("xpath=//android.widget.EditText[@resource-id=\"inputAmount\"]")
            .clearInput()
            .sendKeys(withdrawAmt)

        it.hideKeyboard()
        
        it.tap("Next")


    }

    @Macro("[Enter Details Cash In]")
    fun enterDetails(depositAmt: String, cashInMethod: String){

        it.scrollToTop()
        it.tap("xpath=//*[@class=\"android.widget.EditText\"]")
            .sendKeys(depositAmt)

        it.hideKeyboard()

        it.tapWithScrollDown(cashInMethod)

        it.scrollToBottom()

        describe("Wait to scroll to bottom")
            .waitForDisplay("Cash In")

        it.wait(1.0)

        it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash In\"]").tap()
    }

    @Macro("[Make Cash In]")
    fun MakeCashIn(depositAmt: String, cashInMethod: String):  String {

        //get current wallet balance

        it.wait(2)

        var currentBalance = it.select("*PHP*")

        val textCurrentBalance = currentBalance.text.replace("PHP ", "").replace(",", "")

        it.tap("Available Cash")

        describe("Wait for Page to Load")
            .waitForDisplay("Pending", waitSeconds = 30.0)

        it.scrollToTop()

        it.exist("Cash In")

        it.selectWithScrollDown("Cash In").tap()

        describe("Wait for Page to Load")
            .waitForDisplay("Select amount to cash in")

        it.tap("xpath=//*[@class=\"android.widget.EditText\"]")
            .sendKeys(depositAmt)
//        it.tap("0.00")
//            .sendKeys(depositAmt)

        it.tapWithScrollDown(cashInMethod)

        it.hideKeyboard()

        it.scrollToBottom()

        describe("Wait to scroll to bottom")
            .waitForDisplay("Cash In")

        it.wait(1.0)

         it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash In\"]").tap()

        return textCurrentBalance
    }

    @Macro("[No Payment Method]")
    fun noPaymentMethod(depositAmt: String){
        //get current wallet balance
        val currentBalance = it.select("*PHP*")

        val textCurrentBalance = currentBalance.text.replace("PHP ", "").replace(",", "")

        it.tap("Available Cash")

        describe("Wait for Page to Load")
            .waitForDisplay("Pending")

        it.scrollDown()
        it.scrollToTop()

        it.exist("Cash In")

        it.tap("Cash In")

        describe("Wait for Page to Load")
            .waitForDisplay("Select amount to cash in")

        it.tap("xpath=//*[@class=\"android.widget.EditText\"]")
            .sendKeys(depositAmt)

        it.hideKeyboard()

        it.scrollToBottom()

        describe("Wait to scroll to bottom")
            .waitForDisplay("Cash In")

        it.wait(1)

        it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash In\"]").tap()

    }

    @Macro("[Cash In Modal Confirmation]")
    fun cashInModalConfirmation(paymentMethod: String, depositAmt: String, cashInCharge: String){

        val amountReceived = depositAmt.toDouble() - cashInCharge.toDouble()
        val formattedAmount = formatAmount(amountReceived.toString())

        val formatDeposit = formatAmount(depositAmt)

        it.exist("Cash In Confirmation")
        it.exist("Method")
        it.exist(paymentMethod)
        it.exist("Amount to Pay")
        it.exist("PHP $formatDeposit")
        it.exist("Fee")
        it.exist("PHP $cashInCharge")
        it.exist("You will Receive")
        it.exist("PHP $formattedAmount")

        it.exist("All Payments must be settled within 3 working days.")
        it.exist("By continuing, you are agreeing to our ")
        it.exist("Payment Policy")

        it.exist("Cancel")
        it.exist("Submit")

        it.tap("Submit")
    }

    @Macro("[Cash In Details]")
    fun cashinDetails(depositAmt: String, currentBalance:String, randomPaymentMethod:String , referenceNumber:String, testRemove:Boolean) : String {

        describe("Wait Page to Load")
            .waitForDisplay("Kindly upload the deposit slip or any proof of payment to fulfill this deposit.")

        describe("Wait Page to Load")
            .waitForDisplay("Step 1: Payment")

        it.exist(randomPaymentMethod)

        it.exist("Cash In Amount")
        it.exist("₱")

        val formattedAmount = formatTrailingZeroes(depositAmt)

        it.exist(formattedAmount)

        it.exist("Step 1: Payment")
        it.exist("Please deposit (bank deposit/online transfer) the amount to the following account:")
        it.exist("Bank")

        if(randomPaymentMethod == "BDO - Bank Deposit / Fund Transfer"){
            it.exist("BDO")
        } else if (randomPaymentMethod == "BPI - Bank Deposit / Fund Transfer"){
            it.exist("BPI")
        }

        it.exist("Account Number")

        var accountName = ""
        var accountNumber =""

        //Set values for Account Number and Account Name

        if(randomPaymentMethod == "BPI - Bank Deposit / Fund Transfer"){
            accountName = "broker 2"
            accountNumber = "123232131231"
        } else if (randomPaymentMethod == "Chinabank - Bank Deposit / Fund Transfer"){
            accountName = "ISI"
            accountNumber = "1234567890"
        } else if (randomPaymentMethod == "RCBC - Bank Deposit / Fund Transfer"){
            accountName = "ISI"
            accountNumber = "1234444111"
        } else if (randomPaymentMethod == "UnionBank - Bank Deposit / Fund Transfer"){
            accountName = "broker 4"
            accountNumber = "4444555511112222"
        } else if (randomPaymentMethod == "BDO - Pay Bills"){
            accountName = "broker 2"
            accountNumber = "123232131231"
        } else if (randomPaymentMethod == "Metrobank - Bank Deposit / Fund Transfer"){
            accountName = "test"
            accountNumber = "123"
        }

        // check if correct account number and account name
        it.exist("Account Number")
        it.exist(accountNumber)

        it.exist("Account Name")
        it.exist(accountName)

        it.exist("Amount")
        it.exist(formattedAmount)

        it.wait(2)

        if(testRemove) {
            //tap question mark
            it.tap("\uF128")
            it.exist("Amount to deposit.")

            it.tap("Step 1: Payment")
            it.dontExist("Amount to deposit.")

            it.wait(2)
            it.exist("Copy")
            // check if copy button is working

            it.tap("xpath=(//android.widget.Button[@text=\"Copy\"])[1]")
            it.exist("Successfully copied account number.")

            it.wait(2)
            it.tap("xpath=(//android.widget.Button[@text=\"Copy\"])[2]")
            it.exist("Successfully copied account name.")

            it.wait(2)
            it.tap("xpath=(//android.widget.Button[@text=\"Copy\"])[3]")
            it.exist("Successfully copied amount.")
        }

        it.exist("After paying, please take a picture of your deposit slip (or screenshot of proof of payment), including the date of the payment.")

        it.exist("Step 2: Upload Deposit Slip")

        it.exist("Upload a clear copy of your deposit slip/screenshot of proof of payment. See sample ")

        it.selectWithScrollDown("Drag and drop the image or browse to choose a file")

        it.dontExist("To replace image, drag and drop the image or browse to choose a file")
        it.exist("Drag and drop the image or browse to choose a file")
        it.tap("Drag and drop the image or browse to choose a file")

        describe("Wait Page to Load")
            .waitForDisplay("Media")

        it.wait(1)

        it.selectWithScrollDown("Media").tap()
//        it.tap("xpath=//android.widget.ImageView[@content-desc=\"More options\"]")
//        it.tap("Browse…")

        //tap proof of payment
        it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.providers.media.module:id/icon_thumbnail\"])[7]")

        it.exist("To replace image, drag and drop the image or browse to choose a file")
        it.dontExist("Drag and drop the image or browse to choose a file")

        if(testRemove){
            it.tap("xpath=//android.widget.Image[@text=\"circle-times-gray.png?i_1.0.8958\"]")
            it.exist("Drag and drop the image or browse to choose a file")
            it.dontExist("To replace image, drag and drop the image or browse to choose a file")

            //upload again

            it.tap("Drag and drop the image or browse to choose a file")

            describe("Wait Page to Load")
                .waitForDisplay("Media")

            it.tap("Media")
            it.tap("xpath=//android.widget.ImageView[@content-desc=\"More options\"]")
            it.tap("Browse…")

            it.tap("xpath=(//android.widget.ImageView[@resource-id=\"com.google.android.documentsui:id/icon_thumb\"])[2]")

            it.exist("To replace image, drag and drop the image or browse to choose a file")
            it.dontExist("Drag and drop the image or browse to choose a file")
        }

        it.scrollToBottom()
        it.selectWithScrollUp("Confirmation / Reference No.")
        it.exist("Confirmation / Reference No.")

        //Enter reference number
        //it.tap("xpath=//android.webkit.WebView[@text=\"Cash In | InvestaTrade\"]/android.view.View/android.view.View/android.widget.EditText")

        it.tap(x=241, y=1342)
            .sendKeys(referenceNumber)

//        it.swipePointToPoint(startX = 600, startY = 506, endX=600, endY =645 )
//
//        it.tap(x=600, y =645)

        //it.tap("xpath=//android.widget.EditText")

        it.hideKeyboard()
        it.exist(referenceNumber)

        it.exist("Payment Date and Time")
        it.exist("All Payments must be settled within 3 working days.")

        val AMTime = getRandomBoolean()

        if(AMTime){
            it.tap("AM")
        } else {
            it.tap("PM")
        }

        it.exist("AM")
        it.exist("PM")
        it.exist("Submit")

        //Comment for now this part cannot be read because of mobile web view limitation
//        it.exist("Cash In")
//        it.exist(randomPaymentMethod)
//        it.exist("₱ $formattedAmount")
//        it.exist("Total Cash In Amount")
//        it.exist("To complete this transaction, click Pay Now.")
//        it.exist("Payment Confirmation No.")
//        it.exist("Requested Date")
//        it.exist("Payment Date")
//        it.exist("-")
//        it.exist("Completed Date")
//        it.exist("-")
//        it.exist("Amount Paid")
//        it.exist(formattedAmount)
//        it.exist("Cancel Request")

        it.scrollToBottom()
        it.selectWithScrollUp("All Payments must be settled within 3 working days.")
        it.wait(1)
        it.exist("*Transaction No.*")
        val transactionNumber = it.select("*Transaction No.*")
        val transactionNumberText = transactionNumber.text.replace("Transaction No. ", "")

        it.selectWithScrollUp("Submit")


        it.tap("Submit")

        describe("Wait to scroll to bottom")
            .waitForDisplay("Successfully submitted proof of payment.")

        describe("Wait to scroll to bottom")
            .waitForDisplay("Pending")
        it.exist("Pending")


        return transactionNumberText

    }

    @Macro("[View Latest Pending Cash In]")
    fun viewLatest(transactionNumber: String, depositAmt: String, paymentMethod: String, requestedDateTime: String, cashInCharge:String, dateToday:String){

        val amountReceived = depositAmt.toDouble() - cashInCharge.toDouble()
        val formattedAmount = formatTrailingZeroes(amountReceived.toString())

        it.scrollToTop()

        it.wait(1.0)

        it.exist("Pending")
        it.exist("In")

        //Down Arrow
        it.exist("\uF063")

        it.exist("Cash In via $paymentMethod (#$transactionNumber)")

        it.exist(dateToday)

        //Clock icon
        it.exist("\uF017")
        it.exist("PENDING")

        it.exist("₱ $formattedAmount")
        it.exist("Amount")

        it.exist("Ref No. $transactionNumber")

        it.tap("Ref No. $transactionNumber")

        describe("Wait for Page to Load")
            .waitForDisplay("Step 1: Payment")


        //Check Payment Method
//        it.exist(paymentMethod)
//        it.exist("Transaction No. $transactionNumber")
//        it.exist("₱ $formattedAmount")
//        it.exist("Total Cash In Amount")
//        it.exist("Pending")
//
//        it.exist("To complete this transaction, click Pay Now.")
//
//        it.exist("Payment Confirmation No.")
//        it.exist("Requested Date")
//        it.exist("Payment Date")
//        it.exist("Completed Date")
//        it.exist("Amount Paid")
//        it.exist(formattedAmount)
//        it.exist("Payment Method Fee")
//        it.exist(cashInCharge)
//        it.exist("Cancel Request")

        //Compare Amount
        val formattedDepositAmount = formatTrailingZeroes(depositAmt)
        it.exist(formattedDepositAmount)


    }

    @Macro("[Balance Compare]")
    fun balanceCompare(currentBalance: String, cashInCharge: String){
        // Check Balance should not add yet

        it.scrollToTop()
        val newBalance = it.select("*₱*")

        val textCurrentBalance = newBalance.text.replace("₱ ", "").replace(",", "")
        textCurrentBalance.thisIs(currentBalance)


    }

    @Macro("[View Payment Policy Cash Out]")
    fun viewPolicyCashout(){
        //val policyElement = select("Payment Policy")

        it.scrollDown()
        it.wait(1)

        it.tap(x=720,y=2102)

        describe("Wait for Payment Policy Screen")
            .waitForDisplay("Payment Policy")

        it.exist("Wallet")

        it.scrollUp()

        it.exist("Please read this Payment Policy (referred to as “Payment Policy”, “Policy”) carefully before using any current and future Features, Products, Services and Subscriptions (referred to as “Service”, Services”) operated by InvestaTrade, Inc. (\"We,\" \"Us\" and/or \"Our\")(or an affiliated entity / Partner Broker Securities) to any User (referred as “You” and/or “User”).")

        it.selectWithScrollDown("Funds in the investment account shall only be used for the purpose of investing or buying a share from the PSE Stocks.")

        //Go Back to Cash in Page
        it.tap("\uE909")


        describe("Wait for Page to Load")
            .waitForDisplay("Select Bank Account")

    }

    @Macro("[View Payment Policy]")
    fun viewPolicy(){
        //val policyElement = select("Payment Policy")
        it.tap("Payment Policy.")
        //it.tap(x=789,y=2125)

        describe("Wait for Payment Policy Screen")
            .waitForDisplay("Payment Policy")

        it.exist("Payment Policy")

        it.exist("Please read this Payment Policy (referred to as “Payment Policy”, “Policy”) carefully before using any current and future Features, Products, Services and Subscriptions (referred to as “Service”, Services”) operated by InvestaTrade, Inc. (\"We,\" \"Us\" and/or \"Our\")(or an affiliated entity / Partner Broker Securities) to any User (referred as “You” and/or “User”).")

        it.scrollToBottom()

        it.exist("Funds in the investment account shall only be used for the purpose of investing or buying a share from the PSE Stocks.")
        //Go Back to Cash in Page
        it.tap("\uE909")

    }

    @Macro("[Select Random Payment Method]")
    fun selectRandomPayment(): String {

        fun <T> List<T>.randomItem(): T {
            val randomIndex = (0 until size).random()
            return get(randomIndex)
        }

//        val listPaymentMethods = listOf(
//            "BPI - Bank Deposit / Fund Transfer",
//            "Chinabank - Bank Deposit / Fund Transfer",
//            "RCBC - Bank Deposit / Fund Transfer",
//            "UnionBank - Bank Deposit / Fund Transfer",
//            "BDO - Pay Bills",
//            "Metrobank - Bank Deposit / Fund Transfer"
//        )

        val listPaymentMethods = listOf(
            "BPI - Bank Deposit / Fund Transfer",
            "BDO - Bank Deposit / Fund Transfer",
        )
        return listPaymentMethods.randomItem()
    }

    @Macro("[Random Payment Amount]")
    fun randomAmount(): String {
        val RandomAmt =  Random.nextDouble(100.00,200.00)
        return String.format("%.2f", RandomAmt)
    }

    @Macro("[Random CashOut Amount]")
    fun randomCashOutAmount(): String {
        val RandomAmt =  Random.nextDouble(500.00,600.00)
        return String.format("%.2f", RandomAmt)
    }

    @Macro("[Random Large Payment Amount]")
    fun randomLargeAmount(): String {
        val RandomAmt =  Random.nextDouble(100000.00,200000.00)
        return String.format("%.2f", RandomAmt)
    }

    @Macro("[Cancel Cash In]")
    fun cancelCashIn(depositAmt: String, cashInCharge: String){

        val amountReceived = depositAmt.toDouble() - cashInCharge.toDouble()
        val roundedAmount = kotlin.String.format("%.2f", amountReceived)

        //it.selectWithScrollDown("Cancel Request")
        it.scrollToBottom()
        it.wait(1)
        it.tap(x=528, y=1982)
//        it.exist("Cancel Cash In Request")
//        it.exist("Are you sure you want to cancel your request?")
//        it.exist("No")
//        it.exist("Yes")

//        it.tap("Yes")


        it.wait(1)
        it.tap(x=789,y=656)

        describe("Wait for Page to Load")
            .waitForDisplay("Cash Out")


        it.selectWithScrollDown("History")
        it.exist("History")

        it.exist("CANCELLED")


    }

    @Macro("[View Latest Cancelled]")
    fun viewLatestCancelled(transactionNumber: String, depositAmt: String, paymentMethod: String, requestedDateTime: String, cashInCharge: String, dateToday: String){

        val amountReceived = depositAmt.toDouble() - cashInCharge.toDouble()
        val formattedAmount = formatTrailingZeroes(amountReceived.toString())


        it.exist("In")

        //Down Arrow
        it.exist("\uF063")

        it.exist("Cash In via $paymentMethod (#$transactionNumber)")

        it.exist(dateToday)

        //Clock icon
        it.exist("\uF057")
        it.exist("CANCELLED")

        it.exist("₱ $formattedAmount")
        it.exist("Amount")

        it.exist("Ref No. $transactionNumber")

        it.tap("Ref No. $transactionNumber")

        describe("Wait for Page to Load")
            .waitForDisplay("You cancelled your request.")

        //Check Payment Method

        it.exist("Cash In")
        it.exist(paymentMethod)
        it.exist("₱ $formattedAmount")
        it.exist("Total Cash In Amount")
        it.exist("Cancelled")

        //Check Cash in Status
        it.exist("You cancelled your request.")

        val formatCharge = formatTrailingZeroes(cashInCharge)

        it.exist("Payment Confirmation No.")
        it.exist("Requested Date")
        it.exist("Cancel Date")
        it.exist("Amount Paid")
        it.exist(depositAmt)
        it.exist("Payment Method Fee")
        it.exist(formatCharge)
    }


    //Cash Out Macros

    @Macro("[Go to Cash Out Page]")
    fun goToCashOutPage(): String {

        it.tap("Available Cash")

        describe("Wait for Page to Load")
            .waitForDisplay("Pending", waitSeconds = 30.0)

        it.scrollUp()

        //get current wallet balance
        val currentBalance = it.select("*₱*")

        //lowercase the PHP into Php
        val textCurrentBalance = currentBalance.text.replace("₱ ", "").replace(",", "")

        it.scrollUp()

        it.exist("Cash Out")
        it.tap("Cash Out")

        describe("Wait for Page to Load")
            .waitForDisplay("Select Bank Account")


        return textCurrentBalance.toString()
    }

    @Macro("[Make Cash Out]")
    fun MakeCashOut(withdrawAmt: String , bankCharge :String, name:String, accountNumber: String, currentBalance:String) {

        //label check

        describe("Wait for Home Page")
            .waitForDisplay("Cash In", waitSeconds = 30.0)

        val formatCurrentBalance = formatNumberWithCommaPeriod(currentBalance)

        it.exist("Available Cash")
        it.exist("₱ $formatCurrentBalance")
        it.exist("Cash In")
        it.exist("Cash Out")

        it.exist("You can only cash out up to")
        it.exist("Select Bank Account")
        it.exist("Secured Payment")

        val secureAccountNumber = getLast4Digits(accountNumber)
        it.exist("Account ending *$secureAccountNumber")
        it.exist("Change")
        it.exist(name)

        it.exist("Amount")
        it.exist("Minimum amount: ₱ 50.00")
        it.exist("PHP")
        it.exist("You can only cash out up to: $formatCurrentBalance")
        it.exist("Bank Charge")

        it.exist("You will receive")

        it.tap("xpath=//android.widget.EditText[@resource-id=\"inputAmount\"]")
            .sendKeys(withdrawAmt)

        it.hideKeyboard()

        //Check Bank Charge
        val formatCharge = formatNumberWithCommaPeriod(bankCharge)
        it.exist("₱ $bankCharge")

        val decimalFormat = DecimalFormat("#.##")

        val receiveAmount = formatNumberWithCommaPeriod(  decimalFormat.format(withdrawAmt.toDouble() - bankCharge.toDouble()))

        it.tap("Bank Charge")
        //Check Receive Amount
        it.exist("₱ $receiveAmount")

        //Check Process Date and Time

        it.exist("Process Date and Time:")

        it.exist("* Transactions made after cut-off period will be processed on the next business day.")
        it.exist("* You can cancel the transaction before the processing date.")


        describe("Wait for page to load")
            .waitForDisplay("Next", waitSeconds = 30.0)

        it.exist("Reminders")

        it.exist("We will notify you once we receive your request. Please note that the amount will reflect on your bank account within 2-5 days, depending on your bank’s clearing period.")

        it.exist("Payment Policy.")

        it.exist("Next")

        it.tap("Next")

    }

    @Macro("[Get Process Date and Time]")
    fun viewProcessDateandTime(): String? {
        val currentDateTime = LocalDateTime.now().with(LocalTime.of(20, 30))
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        return currentDateTime.format(formatter)
    }

    @Macro("[Get Current Date and Time Details]")
    fun viewCurrentDateandTimeDetails(): String? {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        return currentDateTime.format(formatter)
    }



    @Macro("[Review Transaction]")
    fun reviewTransaction(withdrawAmt: String, bankName:String, accountNumber:String, accountName:String, pin:String, bankCharge: String, has200KWithdraw: Boolean){

        //Check Current Date and Time
//        val currentDateTime = getCurrentDateTime()
//        it.select(currentDateTime.toString())
//            .textIs(currentDateTime.toString())

        describe("Wait for Page to Load")
            .waitForDisplay("Review Cash Out Transaction")

        it.hideKeyboard()

        val receiveAmount = formatNumberWithCommaPeriod(String.format("%.2f", (withdrawAmt.toDouble() - bankCharge.toDouble())))
        it.exist("Review Cash Out Transaction")
        it.exist("₱ $receiveAmount")
        it.exist("You will receive")


        it.exist("Request Date")

        it.exist("Amount")
        val formatWithdrawAmt = formatNumberWithCommaPeriod(withdrawAmt)
        it.exist("₱ $formatWithdrawAmt")

        it.exist("Bank Charge")
        val formatBankCharge = formatNumberWithCommaPeriod(bankCharge)
        it.exist("₱ $formatBankCharge")

        it.exist("Bank Account")
        it.exist(bankName)

        val secureAccountNumber = getLast4Digits(accountNumber)
        it.exist("Account ending *$secureAccountNumber")
        it.exist(accountName)

        it.exist("We will notify you once the amount is reflected in your account. Please note that the verification process will take up to 5 banking days, depending on your mode of deposit.")

        it.exist("Enter Trading PIN")

        it.exist("Cancel Request")

        if(has200KWithdraw){
            it.exist("xpath=(//android.widget.Button[@text=\"Next\"])[2]")

            //Tap without pin
            it.tap("xpath=(//android.widget.Button[@text=\"Next\"])[2]")
        } else {
            it.exist("xpath=//android.widget.Button[@text=\"Submit\"]")

            it.tap("xpath=//android.widget.Button[@text=\"Submit\"]")
        }


        it.exist("Review Cash Out Transaction")
        it.exist("₱ $receiveAmount")
        it.exist("You will receive")

        //Enter Trading Pin
        it.tap("xpath=//android.widget.EditText[@resource-id=\"tradingPin\"]")
            .sendKeys(pin)

        it.hideKeyboard()

        //Tap trading pin
        it.tap("xpath=//android.widget.Image[@text=\"eye-icon-slashed.png?i_1.0.8979\"]")
        it.exist(pin)



        if(has200KWithdraw){

            //Tap without pin
            it.tap("xpath=(//android.widget.Button[@text=\"Next\"])[2]")

            //OTP when more than 200k
            it.exist("Review Cash Out Transaction")
            it.exist("₱ $receiveAmount")
            it.exist("You will receive")

            it.exist("Please enter the OTP that has been sent to your registered email.")
            it.exist("Back")
            it.exist("Cash Out")

            it.tap("xpath=//android.widget.EditText[@resource-id=\"otp\"]")
                .sendKeys("202071")

            it.selectWithScrollDown("xpath=//android.widget.Button[@text=\"Cash Out\"]").tap()

        } else {

            it.tap("xpath=//android.widget.Button[@text=\"Submit\"]")
        }

        describe("Wait for Page to Load")
            .waitForDisplay("Pending", waitSeconds = 30.0)

        it.exist("Pending")
        it.exist("Cash In")
        it.exist("Cash Out")

    }

    @Macro("[Get Current Date and Time]")
    fun getCurrentDateTime(): String? {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime
    }

    @Macro("[Get Current Date]")
    fun getCurrentDate(): String? {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime
    }

    @Macro("[New Balance]")
    fun newBalance(currentBalance: String, withdrawAmt: String){

        it.wait(10)

        it.tap("\uE908")

        describe("Wait for Redirect")
            .waitForDisplay("About My Portfolio")

        val textBalanceCurrent = currentBalance.replace(",","").replace("Php", "")
        val expectedBalance = round((textBalanceCurrent.toDouble() - withdrawAmt.toDouble()) * 100) / 100
        val fixedDecimalsExpectedBalance = formatNumberWithCommaPeriod(expectedBalance.toString())

        describe("Wait for Redirect")
            .waitForDisplay("PHP $fixedDecimalsExpectedBalance", waitSeconds = 30.0)

        it.exist("PHP $fixedDecimalsExpectedBalance")

        it.tap("Available Cash")

        describe("Wait for Page to Load")
            .waitForDisplay("Pending", waitSeconds = 30.0)

        it.exist("Available Cash")
        it.exist("Pending")


        //Round to 2 Decimals


        it.exist("₱ $fixedDecimalsExpectedBalance")
    }

    @Macro("[View Latest Pending Cash Out]")
    fun viewLatestCashOut( withdrawAmt: String, paymentMethod: String, requestedDateTime: String, bankName:String, accountName:String, accountNumber:String, bankCharge: String, currentBalance: String, time:String): String {

        val formatWithdrawAmt = formatNumberWithCommaPeriod(withdrawAmt)

        //Tap Latest Pending Transaction

        //Up Sign
        it.exist("\uF062")
        it.exist("Out")

        it.exist("*Cash Out via Bank: $bankName*")


        //clock sign
        it.exist("\uF017")
        it.exist("PENDING")

        it.exist("Amount")
        it.exist("*Ref No.*")

        var transactionNumber = it.select("*Ref No.*")
        val transactionNumberText = transactionNumber.text.replace("Ref No. ","")
        println("Transaction Number:  $transactionNumberText")

        it.tap("₱ -$formatWithdrawAmt")

        describe("Wait for Page to Load")
            .waitForDisplay("You have created cash out request.", waitSeconds = 30.0)

        val textBalanceCurrent = currentBalance.replace(",","").replace("Php", "")
        val expectedBalance = formatNumberWithCommaPeriod(((round((textBalanceCurrent.toDouble() - withdrawAmt.toDouble()) * 100) / 100).toString()))

        it.exist("₱ $expectedBalance")

        it.exist("Available Cash")
        it.exist("Cash In")
        it.exist("Cash Out")

        val receiveAmount = formatNumberWithCommaPeriod((withdrawAmt.toFloat() - bankCharge.toFloat()).toString())

        it.exist("₱ $receiveAmount")
        it.exist("You will receive")

        it.scrollDown()

        it.exist("PENDING")
        it.exist("You have created cash out request.")
        it.exist("Request Date")
        it.exist("Complete Date")
        it.exist("Amount")
        it.exist("Bank Charge")
        it.exist("Bank Account")
        it.exist(bankName)

        val secureAccountNumber = getLast4Digits(accountNumber)
        it.exist("Account ending *$secureAccountNumber")
        it.exist(accountName)

        val currentDate = getNextDayIfTimePassed(time)

        it.exist("Your cash out will be process after the cut-off time: $currentDate $time. Then Kindly wait 1-2 working days before it will be reflected to your bank account.")
        it.exist("Cancel Request")

        return transactionNumberText
    }

    @Macro("[Cancel Cash Out]")
    fun cancelCashOut(){
        it.scrollDown()

        it.tap("Cancel Request")
        it.exist("Cancel Cash Out Request")
        it.exist("Are you sure you want to cancel your request?")
        it.exist("No")
        it.exist("Yes")

        it.tap("Yes")

        describe("Wait for Toast Message")
            .waitForDisplay("Successfully cancelled withdrawal.")

        it.exist("CANCELLED")
        it.exist("You cancelled your request.")


    }

    @Macro("[View Latest Cancelled Cash Out]")
    fun viewLatestCancelledCashOut(transactionNumber: String, requestedDateTime: String, bankName:String, accountName:String, accountNumber:String, withdrawAmt: String, bankCharge: String, currentBalance: String){
        //Check History for Cancelled Transaction

        //Go Back
        it.wait(10)


        it.tap("\uE908")

        describe("Wait for Redirect")
            .waitForDisplay("About My Portfolio")

        val textBalanceCurrent = currentBalance.replace(",","").replace("Php", "")
        val expectedBalance = formatNumberWithCommaPeriod(textBalanceCurrent)

        describe("Wait for Redirect")
            .waitForDisplay("PHP $expectedBalance")

        it.exist("PHP $expectedBalance")

        it.tap("Available Cash")

        describe("Wait for Redirect")
            .waitForDisplay("Pending", waitSeconds = 30.0)

        it.exist("Available Cash")
        it.exist("Cash In")
        it.exist("Cash Out")

        it.selectWithScrollDown("History")

        it.exist("History")

        it.exist("Out")
        it.exist("Cash Out via Bank: $bankName (#$transactionNumber)")
        it.exist("CANCELLED")
        it.exist("Ref No. $transactionNumber")
        it.exist("Amount")

        val formatWithdrawAmt = formatAmount(withdrawAmt)
        it.exist("₱ -$formatWithdrawAmt")

        it.tap("Ref No. $transactionNumber")

        describe("Wait for Redirect")
            .waitForDisplay("You cancelled your request.")

        it.exist("Cash Out")

        val receiveAmount = formatAmount((withdrawAmt.toFloat() - bankCharge.toFloat()).toString())
        val formatReceiveAmount = formatAmount(receiveAmount)
        it.exist("₱ $formatReceiveAmount")
        it.exist("You will receive")
        it.exist("CANCELLED")
        it.exist("You cancelled your request.")
        it.exist("Request Date")
        it.exist("Cancel Date")

        it.exist("Amount")
        it.exist("₱ $formatWithdrawAmt")

        it.exist("Bank Charge")
        val formatBankCharge = formatNumberWithCommaPeriod(bankCharge)
        it.exist("₱ $formatBankCharge")

        it.exist("Bank Account")
        it.exist(bankName)

        val secureAccountNumber = getLast4Digits(accountNumber)
        it.exist("Account ending *$secureAccountNumber")
        it.exist(accountName)
    }

    @Macro("[Change Add Bank]")
    fun changeAddBank(pword :String, bankName:String , length: String, accountName: String): String {

        it.tap("Change")
        it.tap("+ Add Bank Account")

        //Select bank
        describe("Wait for Page to Load")
            .waitForDisplay("Bank Accounts")

        it.exist("Link your bank accounts to withdraw")
        it.exist("Add Account")

        it.exist("CIMB Bank Philippines, Inc.")
        it.exist("Default")
        it.exist(accountName)
        it.exist("Edit")


        it.tap("Add Account")
        it.exist("Add Bank Account")
        it.exist("Bank")

        it.tap("\uE908")
        it.tap(bankName)

        //Enter Branch Name

        it.exist("Branch Name")
        it.tap("xpath=//android.app.Dialog[@resource-id=\"addBankAccountDetailsModal\"]/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]")
            .sendKeys("Branch Name: $bankName")

        it.hideKeyboard()

        //Create Random Account Digit Number
        it.exist("Account Number")
        val randomAccountNumber = generateRandomAccountNumber(length.toInt())

        //Enter Account Number

        it.tap("xpath=//android.app.Dialog[@resource-id=\"addBankAccountDetailsModal\"]/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]")
            .sendKeys(randomAccountNumber)

        it.hideKeyboard()

        it.exist("Account Name")
        val name = formatName(accountName)
        it.exist(name)

        //Enter password
        it.exist("Investa Password")
        it.tap("xpath=//android.app.Dialog[@resource-id=\"addBankAccountDetailsModal\"]/android.view.View/android.view.View/android.view.View/android.widget.EditText[4]")
            .sendKeys(pword)



//        describe("Wait for Toast Message")
//            .waitForDisplay("Added Bank Details")

        describe("Wait for redirect")
            .waitForDisplay("Link your bank accounts to withdraw")

        it.exist(bankName)
        it.exist(accountName)
        val accountNumberSecure = getLastTwoDigits(randomAccountNumber)

        it.exist(accountNumberSecure)
        it.exist("Edit")
        it.exist("Set as Default")

        it.tap("$accountNumberSecure:right(3)")

        //Go Back to
        it.tap("\uE909")

        describe("Wait for redirect")
            .waitForDisplay("About My Portfolio")


        return randomAccountNumber


    }

    @Macro("[Save Changes in Bank]")
    fun saveChanges(bankName: String, randomAccountNumber:String, accountName: String, pword: String){
        it.tap("xpath=(//android.widget.Button[@text=\"Edit\"])[3]")

        it.exist(bankName)
        it.exist("Branch: $bankName")
        it.exist(randomAccountNumber)
        it.exist(accountName)

        //Enter password
        it.exist("Investa Password")
        it.tap("xpath=//android.app.Dialog/android.view.View/android.view.View/android.view.View/android.widget.EditText[4]")
            .sendKeys(pword)

        it.exist("Save Changes")
        it.exist("Delete Account")

        val newAccountNumber = changeLastDigit(randomAccountNumber)

        it.tap(randomAccountNumber)
            .clearInput()
            .sendKeys(newAccountNumber)

        it.hideKeyboard()

        it.exist(newAccountNumber)

        it.tap("Save Changes")

        val accountNumberSecure = getLastTwoDigits(randomAccountNumber)

        it.exist(accountNumberSecure)

    }

    @Macro("[Add Bank]")
    fun addBank(pword :String, bankName:String, length: String, accountName: String): String {

        //Select bank
        describe("Wait for Page to Load")
            .waitForDisplay("Bank Accounts", waitSeconds = 30.0)

        it.exist("Link your bank accounts to withdraw")
        it.exist("Add Account")

        it.exist("Default")
        it.exist(accountName.uppercase(Locale.getDefault()))
        it.exist("Edit")

        it.exist("Bank of the Philippine Islands (BPI)")

        it.tap("Add Account")

        //Select Bank
        it.tap(x=189, y=929)

        if(bankName == "Banco De Oro (BDO)"){
            it.tap(x=189,y=1048)
        } else if (bankName == "Bank of the Philippine Islands (BPI)"){
            it.tap(x=189,y=1136)
        } else if (bankName == "CIMB Bank Philippines, Inc."){
            it.tap(x=189,y=1223)
        }

        it.wait(1)
        it.exist("*$bankName*")

        it.hideKeyboard()

        //Branch Name Bank
        //it.exist("Branch Name")
        it.tap(x=189,y=1103)
            .sendKeys("Branch: $bankName")
        //it.exist("Branch: $bankName")

        it.hideKeyboard()

        //Create Random Account Digit Number
        val randomAccountNumber = generateRandomAccountNumber(length.toInt())

        //Enter Account Number
        it.exist("Account Number")
        it.tap(x=189,y=1292)
            .sendKeys(randomAccountNumber)
        //it.exist(randomAccountNumber)

        it.hideKeyboard()

        it.exist("Account Name")
        val name = formatName(accountName)
        //it.exist(name)

        //Enter password
        //it.exist("Investa Password")
        it.tap(x=189,y=1674)
            .sendKeys(pword)

        it.hideKeyboard()

        it.tap("xpath=(//android.widget.Button[@text=\"Add Account\"])[2]")

        describe("Wait for redirect")
            .waitForDisplay("Link your bank accounts to withdraw")

        it.exist(bankName)
        it.exist(accountName.uppercase(Locale.getDefault()))
        val accountNumberSecure = getLastTwoDigits(randomAccountNumber)

        it.exist(accountNumberSecure)
        it.exist("Edit")
        it.exist("Set as Default")

        return randomAccountNumber

    }

    @Macro("[Change Bank]")
    fun changeBank(bankAccountNumber:String, bankName: String) {
        it.tap("Change")

        val secureAccountNumber = getLast4Digits(bankAccountNumber)
        it.exist(bankName)
        it.exist("+ Add Bank Account")
        it.tap("******$secureAccountNumber")

        //Check if Bank is Changed Successfully

        //Look for the Account Number
        it.exist(bankName)
        it.exist("Account ending *$secureAccountNumber")


    }

    @Macro("[Select Random Bank]")
    fun selectRandomBank(): String {

        fun <T> List<T>.randomItem(): T {
            val randomIndex = (0 until size).random()
            return get(randomIndex)
        }

        val listOfBanks = listOf(
            "CIMB Bank Philippines, Inc.",
            "Banco De Oro (BDO)",
            "Bank of the Philippine Islands (BPI)",

        )
        return listOfBanks.randomItem()
    }

    @Macro("[Select Random Bank Account]")
    fun selectRandomBankAccount(lastDefaultBank :String): String {

        fun <T> List<T>.randomItem(): T {
            val randomIndex = (0 until size).random()
            return get(randomIndex)
        }

        val listOfBanks = listOf(
            "3453453453",
            "234234234234",
            "234543534534"
            )

        //removes the default bank in the random selection of bank account
        val filteredBanks = listOfBanks.filter { it != lastDefaultBank }

        return filteredBanks.randomItem()
    }

    @Macro("[Go To Bank Accounts Page]")
    fun goToBankAccountsPage(lastDefaultBankAccountNumber:String) {
        it.tap("Menu")
        it.tap("Settings")
        it.tap("Bank Accounts")

        //Select bank
        describe("Wait for Page to Load")
            .waitForDisplay("Bank Accounts")

        it.exist("Link your bank accounts to withdraw")
        it.exist("Add Account")



    }

    @Macro("[Remove Bank]")
    fun removeBank(accountNumber: String,bankName: String, pword: String, accountName: String){

        describe("Wait for Page to Load")
            .waitForDisplay("Bank Accounts")

        it.exist("Link your bank accounts to withdraw")
        it.exist("Add Account")

        val accountNumberSecure = getLastTwoDigits(accountNumber)
        it.tap("xpath=(//android.widget.Button[@text=\"Edit\"])[3]")

        it.exist("Edit Bank Account")
        it.exist(bankName)
        it.exist(accountNumber)
        it.exist(accountName)

        it.exist("Save Changes")
        it.exist("Delete Account")

        it.tap("Delete Account")

        it.exist("Delete Account")
        it.exist("Are you sure you want to delete this bank account?")

        it.exist(bankName)
        it.exist("Account $accountNumber")
        it.exist(accountName)

        it.tap("xpath=//android.widget.EditText[@resource-id=\"delete-bank-acc-password\"]")
            .sendKeys(pword)

        it.tap("xpath=(//android.widget.Button[@text=\"Delete Account\"])[2]")

        describe("Wait for Page to Load")
            .waitForDisplay("Bank Accounts")

        it.exist("Link your bank accounts to withdraw")
        it.exist("Add Account")

    }

    fun bankAccountNameMap() : Map<String, String>{
        return mapOf(
            "3453453453" to "Bank of the Philippine Islands (BPI)",
            "234234234234" to "Banco De Oro (BDO)",
            "234543534534" to "CIMB Bank Philippines, Inc.",
        )
    }

    fun bankChargesMap() : Map<String, String>{
        return mapOf(
            //"ANZ Banking Group Ltd." to "2.00",
            "Banco De Oro (BDO)" to "10.00",
            "Bank of the Philippine Islands (BPI)" to "5.00",
            "CIMB Bank Philippines, Inc." to "2.00"
        )
    }

    fun cashInChargesMap() : Map<String, String>{
        return mapOf(
            "BPI - Bank Deposit / Fund Transfer" to "0",
            "Chinabank - Bank Deposit / Fund Transfer" to "0",
            "RCBC - Bank Deposit / Fund Transfer" to "0",
            "UnionBank - Bank Deposit / Fund Transfer" to "5",
            "BDO - Pay Bills" to "0.00",
            "Metrobank - Bank Deposit / Fund Transfer" to "0",
            "BDO - Bank Deposit / Fund Transfer" to "5"
        )
    }

    fun bankLengthDigits() : Map<String, String> {
        return mapOf(
            "CIMB Bank Philippines, Inc." to "12",
            "ANZ Banking Group Ltd." to "12",
            "Banco De Oro (BDO)" to "12",
            "Bank of the Philippine Islands (BPI)" to "11",
        )
    }

    fun generateRandomAccountNumber(length: Int): String {
        require(length in 1..12) { "Length must be between 1 and 12" }

        val lowerBound = 10.0.pow(length - 1).toLong()
        val upperBound = 10.0.pow(length).toLong() - 1
        val randomAccountNumber = Random.nextLong(lowerBound, upperBound)

        return randomAccountNumber.toString()
    }

    fun generateRandomAlphanumeric(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }


    fun removeExtraZero(numberString: String): String {
        val parts = numberString.split(".")
        if (parts.size == 2 && parts[1].length == 2 && parts[1][1] == '0') {
            return "${parts[0]}.${parts[1][0]}"
        }
        return numberString
    }

    fun formatAmount(depositAmt: String): String {
        val depositAmtNumber = BigDecimal(depositAmt).setScale(2, RoundingMode.HALF_UP)
        return depositAmtNumber.stripTrailingZeros().toPlainString()
    }

    fun formatTrailingZeroes(depositAmt: String): String {
        val depositAmtNumber = BigDecimal(depositAmt).setScale(2, RoundingMode.HALF_UP)
        return String.format("%.2f", depositAmtNumber)
    }

    fun getWalletBalance(): String {

        it.exist("Available Cash")
        var textCurrentBalance: String

        do {
            it.wait(3)
            val walletBalance = it.select("*PHP*")
            textCurrentBalance = walletBalance.text.replace("PHP ", "").replace(",", "")
            println("Current Balance: $textCurrentBalance")

        } while (textCurrentBalance == "0.00")

        return textCurrentBalance
    }

    fun getLast4Digits(accountNumber: String): String {

        // Get the last 4 digits, or the whole string if it's shorter than 4 characters
        val last4Digits = accountNumber.takeLast(4)

        // Return the formatted string
        return "$last4Digits"

    }

    fun formatName(name: String): String {
        return name.trim().split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
            .joinToString(" ") { word ->
                word.lowercase(Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
    }

    fun getLastTwoDigits(input: String): String {
        // Convert the input to a string if it's not already
        val inputStr = input.toString()

        // Get the last 2 digits, or the whole string if it's shorter than 2 characters
        val last2Digits = inputStr.takeLast(2)

        // Return the formatted string
        return "********$last2Digits"
    }

    fun formatNumberWithCommaPeriod(number: String): String {
        // Determine the decimal places in the input number
        val decimalPlaces = when {
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

    fun formatDateCashOut(): String {
        // Get the current date
        val currentDate = LocalDate.now()

        // Define the desired output date format
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

        // Format the current date
        return currentDate.format(formatter)
    }

    fun getNextDayIfTimePassed(timeString: String): String {
        // Define a formatter for parsing the time string
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH)

        // Parse the time string
        val parsedTime = LocalTime.parse(timeString, timeFormatter)

        // Get the current date and time
        val currentTime = LocalTime.now()

        // Check if the current time has passed the given time
        val resultDate = if (currentTime.isAfter(parsedTime)) {
            LocalDate.now().plusDays(1)
        } else {
            LocalDate.now()
        }

        // Define a formatter for the desired date format
        val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)

        // Format the result date
        return resultDate.format(dateFormatter)
    }

}

