package wallet

import io.appium.java_client.AppiumBy
import macro.Wallet.selectRandomPayment
import macro.Wallet.randomAmount
import macro.Wallet.randomLargeAmount
import macro.Wallet.MakeCashIn
import macro.Wallet.cashInChargesMap
import macro.Wallet.cashinDetails
import macro.Wallet.generateRandomAlphanumeric
import macro.Wallet.getCurrentDate
import macro.Wallet.viewCurrentDateandTimeDetails
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Ignore
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CashIn : UITest() {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private val pin = "123123"
    private val useUpdate = false
    private val broker = "1"

    @Test
    @DisplayName("Suggested Payment Amount")
    @Order(1)
    fun suggestedPaymentAmount() {
        scenario{
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker, pin, useUpdate)

                    it.tap("Available Cash")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Pending")

                    it.scrollDown()
                    it.scrollToTop()

                    it.exist("Wallet")
                    it.exist("Available Cash")
                    it.exist("Cash In")
                    it.exist("Cash Out")
                    it.exist("Pending")


                    it.selectWithScrollDown("History")
                    it.exist("History")

                    it.scrollToTop()
                    it.wait(1)
                    it.tap("Cash In")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Select amount to cash in")

                    it.exist("Available Cash")
                    it.exist("Cash In")
                    it.exist("Cash Out")
                    it.exist("Select amount to cash in")

                    it.exist("How would you like to cash in?")
                    it.exist("Secured Payment")

                    //Tap 500
                    it.tap("500")
                    it.exist("500")

                    //Tap 1000
                    it.tap("1,000")
                    it.exist("1,000")


                    //Tap 5000
                    it.tap("5,000")
                    it.exist("5,000")

                    //Tap 10000
                    it.tap("10,000")
                    it.exist("10,000")

                    //Tap 20000
                    it.tap("50,000")
                    it.exist("50,000")

                    //Tap 30000
                    it.tap("100,000")
                    it.exist("100,000")

                    //Tap 50000
                    it.tap("500,000")
                    it.exist("500,000")

                    //Tap 100000
                    it.tap("1,000,000")
                    it.exist("1,000,000")

                    it.scrollToBottom()

                    it.exist("All Payments must be settled within 3 working days.")
                    it.exist("By continuing, you are agreeing to our ")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Payment Amount Validation")
    @Order(2)
    fun amountValidations() {
        scenario{
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)


                    // Less than Minimum Validation
                    var depositAmt = 99.99

                    // Select Random Payment Method

                    val randomPaymentMethod = selectRandomPayment()
                    it.macro("[Make Cash In]", depositAmt.toString(), randomPaymentMethod)

                    describe("Wait for Toast Error Message")
                        .waitForDisplay("Invalid deposit amount. Amount must be at least 100.00 PHP.")


                    // View Payment Policy
                    it.macro("[View Payment Policy]")

                    describe("Wait to go back to Page")
                        .waitForDisplay("Select amount to cash in")

                    it.exist("Select amount to cash in")

                    // Empty Amount Validation

                    it.macro("[Enter Details Cash In]", "", randomPaymentMethod)


                    //0 Amount Validation

                    it.macro("[Enter Details Cash In]", "0.00", randomPaymentMethod)


                    //Negative Amount Validation

                    var negativedepositAmt = "-100"
                    it.macro("[Enter Details Cash In]", negativedepositAmt, randomPaymentMethod)
                    it.dontExist(negativedepositAmt)

                    //Invalid Amount Validation

                    val invalidAmount = ".......---"
                    it.macro("[Enter Details Cash In]", invalidAmount, randomPaymentMethod)
                    it.dontExist(invalidAmount)

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    @Test
    @DisplayName("No Payment Method Selected")
    @Order(3)
    fun nopaymentSelected(){
        scenario{
            case(3){
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", "1", pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    val depositAmt = randomAmount()
                    it.macro("[No Payment Method]", depositAmt)

                    it.exist("Select amount to cash in")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }


    @Test
    @DisplayName("Cancelled Cash In Transaction")
    @Order(4)
    fun cancelledCashIn(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    val depositAmt = randomAmount()
                    val randomPaymentMethod = selectRandomPayment()
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), randomPaymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)



                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[randomPaymentMethod].toString()

                    val dateToday = getCurrentDate().toString()

                    it.macro("[Cash In Modal Confirmation]", randomPaymentMethod, depositAmt, cashInCharge)

                    describe("Wait Page to Load")
                        .waitForDisplay("Step 1: Payment")

                    it.scrollToBottom()
                    it.selectWithScrollUp("All Payments must be settled within 3 working days.")
                    it.wait(1)
                    it.exist("*Transaction No.*")
                    var transactionNumber = it.select("*Transaction No.*")
                    var transactionNumberText = transactionNumber.text.replace("Transaction No. ", "")

                    //Tap Back Button

                    it.selectWithScrollDown("\uE909").tap()
                    it.wait(1)
                    it.selectWithScrollDown("\uE909").tap()

                    it.macro("[Balance Compare]", currentWalletBalance, cashInCharge)

                    it.macro("[View Latest Pending Cash In]",transactionNumberText, depositAmt, randomPaymentMethod, requestedDateTime, cashInCharge, dateToday)

                    it.macro("[Cancel Cash In]", depositAmt, cashInCharge)

                    //Cancel Cash In Transaction
                    it.macro("[View Latest Cancelled]", transactionNumberText, depositAmt, randomPaymentMethod, requestedDateTime, cashInCharge, dateToday)

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    @Test
    @DisplayName("BPI Cash In Scenario")
    @Order(5)
    fun cashInTestBPI() {
        scenario {
            it.macro("[Login]", email, pword)
            it.macro("[Select Broker]", "1", pin, useUpdate)
            //it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "BPI - Bank Deposit / Fund Transfer"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    it.macro("[Cash In Modal Confirmation]", paymentMethod, depositAmt, cashInCharge)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)

                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @DisplayName("ChinaBank Cash In Scenario")
    @Order(6)
    @Ignore
    fun cashInTestChinaBank() {
        scenario {
//            it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
            it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "Chinabank - Bank Deposit / Fund Transfer"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    it.macro("[Cash In Modal Confirmation]", paymentMethod, depositAmt, cashInCharge)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)

                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @DisplayName("RCBC Cash In Scenario")
    @Order(7)
    @Ignore
    fun cashInTestRCBC() {
        scenario {
//            it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
            it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "RCBC - Bank Deposit / Fund Transfer"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @DisplayName("UB Cash In Scenario")
    @Order(8)
    @Ignore
    fun cashInTestUB() {
        scenario {
//            it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
            it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "UnionBank - Bank Deposit / Fund Transfer"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    it.macro("[Cash In Modal Confirmation]", paymentMethod, depositAmt, cashInCharge)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)


                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @Disabled
    @DisplayName("BDO Cash In Scenario")
    @Order(9)
    fun cashInTestBDO() {
        scenario {
//            it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
            it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "BDO - Pay Bills"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    it.macro("[Cash In Modal Confirmation]", paymentMethod, depositAmt, cashInCharge)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)


                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @DisplayName("MetroBank Cash In Scenario")
    @Order(10)
    @Ignore
    fun cashInTestMetro() {
        scenario {
//                        it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
            it.macro("[Enter Trading Pin]", pin, useUpdate)
            case(1) {
                expectation {
                    //Random Deposit Amount Ranging from 100-200 with 2 decimals
                    val depositAmt = randomAmount()

                    val paymentMethod = "Metrobank - Bank Deposit / Fund Transfer"
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), paymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[paymentMethod].toString()

                    it.macro("[Cash In Modal Confirmation]", paymentMethod, depositAmt, cashInCharge)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, paymentMethod,referenceNumber, testRemove)


                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, paymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }

        }
    }

    @Test
    @DisplayName("Large Amount Cash In Transaction")
    @Order(11)
    fun largeAmountCashIn(){
        scenario {
            case(1) {
                expectation {
//                                it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    //Large Deposit Amount Ranging from 100,000 - 200,000 PHP
                    val depositAmt = randomLargeAmount()

                    val randomPaymentMethod = selectRandomPayment()
                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), randomPaymentMethod)
                    val referenceNumber = generateRandomAlphanumeric(20)

                    val testRemove = false
                    val transactionNumber = cashinDetails( depositAmt, currentWalletBalance, randomPaymentMethod,referenceNumber, testRemove)

                    //Check Wallet Balance
                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[randomPaymentMethod].toString()

                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction
                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, randomPaymentMethod, requestedDateTime, cashInCharge)
                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Resume Cash In")
    @Order(10)
    fun resumeCashIn(){
        scenario {
            case(1) {
                expectation {

//                                it.macro("[Login]", email, pword)
//            it.macro("[Select Broker]", "1", pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    //Random Deposit Amount Ranging from 100 to 200 PHP
                    var depositAmt = randomAmount()
                    val randomPaymentMethod = selectRandomPayment()

                    val requestedDateTime = viewCurrentDateandTimeDetails().toString()
                    val currentWalletBalance = MakeCashIn(depositAmt.toString(), randomPaymentMethod)

                    //Create Bank Map
                    val cashInChargesMap = cashInChargesMap()

                    //Choose Random Bank Account
                    val cashInCharge = cashInChargesMap[randomPaymentMethod].toString()
                    val referenceNumber = generateRandomAlphanumeric(20)

                    it.macro("[Cash In Modal Confirmation]", randomPaymentMethod, depositAmt, cashInCharge)

                    describe("Wait Page to Load")
                        .waitForDisplay("Step 1: Payment")

                    it.scrollToBottom()
                    var transactionNumber = it.select("*Transaction No.*")
                    var transactionNumberText = transactionNumber.text.replace("Transaction No. ", "")

                    //Tap Back Button
                    it.tap("\uE909")

                    describe("Wait Page to Load")
                        .waitForDisplay("Pending")


                    it.tap("Ref No. $transactionNumber")


                    depositAmt = (depositAmt.toDouble() - cashInCharge.toDouble()).toString()


                    //Check Wallet Balance
                    it.macro("[Balance Compare]", currentWalletBalance,cashInCharge)
                    //Check Latest Pending Detail Transaction

                    depositAmt = (depositAmt.toDouble() + cashInCharge.toDouble()).toString()

                    it.macro("[View Latest Pending Cash In]", transactionNumber, depositAmt, randomPaymentMethod, requestedDateTime, cashInCharge)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Unverified User Cash In")
    @Order(11)
    fun unverifiedCashIn(){
        scenario {
            case(1) {
                expectation {

                    //Change Email to User with unverified Account
                    email = "tradetestqa+uploaddocs@gmail.com"
                    pword = "password"

                    //Logs out Previous User

                    it.macro("[Log Out]")
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", "1", pin, useUpdate)

                    //Random Deposit Amount Ranging from 100 to 200 PHP
                    val depositAmt = randomAmount()
                    val randomPaymentMethod = selectRandomPayment()
                    MakeCashIn(depositAmt.toString(), randomPaymentMethod)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Sorry, kindly accomplish first your account and wait for the approval before making any deposit.")

                    it.select("Sorry, kindly accomplish first your account and wait for the approval before making any deposit.")
                        .textIs("Sorry, kindly accomplish first your account and wait for the approval before making any deposit.")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    //Cash In Approval/Rejected by Admin


    //No Permission to upload gallery
}