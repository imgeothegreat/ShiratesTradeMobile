package wallet

import macro.Wallet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test
import macro.Wallet.randomCashOutAmount
import macro.Wallet.addBank
import macro.Wallet.bankAccountNameMap
import macro.Wallet.bankChargesMap
import macro.Wallet.bankLengthDigits
import macro.Wallet.changeAddBank
import macro.Wallet.formatAmount
import macro.Wallet.formatNumberWithCommaPeriod
import macro.Wallet.generateRandomAccountNumber
import macro.Wallet.getCurrentDateTime
import macro.Wallet.goToBankAccountsPage
import macro.Wallet.goToCashOutPage
import macro.Wallet.selectRandomBank
import macro.Wallet.selectRandomBankAccount
import macro.Wallet.viewLatestCashOut
import shirates.core.driver.wait
import kotlin.test.Ignore


@Testrun("testConfig/android/androidSettings/testrun.properties")
class CashOut : UITest() {

    private val shortName = "Tradetest Superuser"
    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var broker = "2"
    private var pin = "123123"
    private var accountName = "Tradetest Superuser"
    private val useUpdate = false
    private val has200KWithdraw = false
    private val time = "4:30:00 PM"
    private var firstName = "Tradetest"

    // use for reference later in the test scenarios
    //private var DefaultBankAccountName = "Banco De Oro (BDO)"
    private var DefaultBankAccountName = "Bank of the Philippine Islands (BPI)"
    private var DefaultBankAccountNumber = "3453453453"


    // Below Minimum Amount
    // Empty Amount
    // 0 Amount
    // Negative Amount
    // Over Wallet Balance
    @Test
    @DisplayName("Cash Out Amount Scenarios")
    @Order(1)
    fun cashOutAmountTest() {
        scenario {
            case(1) {
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                    //Less than minimum amount
                    val withdrawAmt = "49.99"

                    //Enter Amount
                    val currentBalance =  goToCashOutPage()

                    //Create Bank Map
                    val bankChargesMap = bankChargesMap()

                    //Choose Random Bank Account
                    val bankCharge = bankChargesMap[DefaultBankAccountName].toString()

                    println("Current Balance: $currentBalance")

                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                    describe("Wait for Toast Error Message")
                        .waitForDisplay("Amount is below minimum.")


                    //View Payment Policy
                    it.macro("[View Payment Policy Cash Out]")

                    // Empty Amount Validation

                    it.macro("[Enter Details Cash Out]", "")

                    describe("Wait for Toast Error Message")
                        .waitForDisplay("Amount is below minimum.")


                    //0 Amount Validation

                    it.macro("[Enter Details Cash Out]", "0.00")
                    it.exist("We will notify you once we receive your request. Please note that the amount will reflect on your bank account within 2-5 days, depending on your bank’s clearing period.")

                    //Negative Amount Validation

                    var negativedepositAmt = "-500"

                    it.macro("[Enter Details Cash Out]", negativedepositAmt)
                    it.exist("We will notify you once we receive your request. Please note that the amount will reflect on your bank account within 2-5 days, depending on your bank’s clearing period.")


                    //Not Enough Wallet Balance

                    //Add 1 to the current wallet balance
                    val balanceNumberOnly = currentBalance.replace(",","").replace("Php", "")

                    val greaterAmount = (balanceNumberOnly.toDouble() + 1).toString()
                    print(greaterAmount)
                    val formatBalance = formatNumberWithCommaPeriod(balanceNumberOnly)
                    val formatBankCharge = formatNumberWithCommaPeriod(bankCharge)

                    //it.tap(invalidAmount)
                    it.macro("[Enter Details Cash Out]", greaterAmount)

                    val receiveAmount = formatNumberWithCommaPeriod((balanceNumberOnly.toDouble() - bankCharge.toDouble()).toString())
                    it.exist("Review Cash Out Transaction")
                    it.exist("₱ $receiveAmount")
                    it.exist("You will receive")
                    it.exist("₱ $formatBalance")
                    it.exist("Bank Charge")
                    it.exist("₱ $formatBankCharge")

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }

    }

    //Valid Cash Out Scenario and View Pending Details
    @Test
    @DisplayName("Valid Cash Out Scenario")
    @Order(2)
    fun validCashOutTest() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Random Withdraw Amount ranging from 500.00 - 600.00
                    val withdrawAmt = randomCashOutAmount()

                    // Go to Cash Out Page
                    val currentBalance =  goToCashOutPage()

                    //Create Bank Charges Map
                    val bankChargesMap = bankChargesMap()

                    //Choose Random Bank Account
                    val bankCharge = bankChargesMap[DefaultBankAccountName].toString()

                    //Create Cash Out
                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                    //Review Transaction

                    val requestedDateTime = getCurrentDateTime().toString()
                    it.macro("[Review Transaction]",withdrawAmt, DefaultBankAccountName, DefaultBankAccountNumber, accountName, pin, bankCharge, has200KWithdraw)

                    //Compare New Balance
                    it.macro("[New Balance]", currentBalance, withdrawAmt)

                    //View Latest Cash Out Pending
                    val paymentMethod = "Cash Out via Bank"
                    it.macro("[View Latest Pending Cash Out]", withdrawAmt, paymentMethod, requestedDateTime, DefaultBankAccountName, accountName, DefaultBankAccountNumber, bankCharge, currentBalance, time)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Cancel Cash Out Scenario")
    @Order(3)
    fun cancelCashOutTest() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Random Withdraw Amount ranging from 500.00 - 600.00
                    val withdrawAmt = randomCashOutAmount()

                    // Go to Cash Out Page
                    val currentBalance =  goToCashOutPage()

                    //Create Bank Charges Map
                    val bankChargesMap = bankChargesMap()

                    //Select Bank Charge on Map
                    val bankCharge = bankChargesMap[DefaultBankAccountName].toString()

                    //Create Cash Out
                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                    //Review Transaction
                    val requestedDateTime = getCurrentDateTime().toString()
                    it.macro("[Review Transaction]",withdrawAmt, DefaultBankAccountName, DefaultBankAccountNumber, accountName, pin, bankCharge, has200KWithdraw)

                    //Compare New Balance
                    it.macro("[New Balance]", currentBalance, withdrawAmt)

                    //View Latest Cash Out Pending
                    val paymentMethod = "Cash Out via Bank"
                    val transactionNumber = viewLatestCashOut( withdrawAmt, paymentMethod, requestedDateTime, DefaultBankAccountName, accountName, DefaultBankAccountNumber, bankCharge, currentBalance, time)
                    it.macro("[Cancel Cash Out]")

                    //View Latest Cancelled History
                    it.macro("[View Latest Cancelled Cash Out]", transactionNumber, requestedDateTime, DefaultBankAccountName, accountName, DefaultBankAccountNumber, withdrawAmt, bankCharge, currentBalance)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Change Add Bank in Cash Out")
    @Order(4)
    @Ignore
    fun addBankCashOut() {
        scenario {
            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", "1", pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                //Random Withdraw Amount ranging from 500.00 - 600.00
                val withdrawAmt = randomCashOutAmount()

                //Choose Random Bank
                val bankName = selectRandomBank()

                // Go to Cash Out Page
                val currentBalance =  goToCashOutPage()

                //Add Bank

                    val bankLengthMap = bankLengthDigits()

                    val bankLengthDigits = bankLengthMap[bankName]

                    val accountNumber = changeAddBank(pword, bankName,  bankLengthDigits.toString(), accountName)


                //Create Bank Charges Map
                val bankChargesMap = bankChargesMap()

                //Select Bank Charge on Map
                val bankCharge = bankChargesMap[bankName].toString()

                //Create Cash Out

                    it.macro("[Make Cash Out]",withdrawAmt, "Php $bankCharge", DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                //Review Transaction
                val requestedDateTime = getCurrentDateTime().toString()
                it.macro("[Review Transaction]",withdrawAmt, bankName, accountNumber, accountName, pin, bankCharge, useUpdate)

                //Compare New Balance
                it.macro("[New Balance]", currentBalance, withdrawAmt)

                //View Latest Cash Out Pending
                val paymentMethod = "Cash Out via Bank"
                val transactionNumber = viewLatestCashOut( withdrawAmt, paymentMethod, requestedDateTime, bankName, accountName, accountNumber, bankCharge, currentBalance, time)

                it.terminateApp()
                it.launchApp()
            }

            }
        }
    }

    @Test
    @DisplayName("Change Bank in Cash Out Page")
    @Order(5)
    fun changeBankCashOut() {
        scenario {
            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                //Random Withdraw Amount ranging from 500.00 - 600.00
                val withdrawAmt = randomCashOutAmount()

                //Create Bank Map
                val bankMap = bankAccountNameMap()

                //Choose Random Bank Account
                val newBankAccountNumber = selectRandomBankAccount(DefaultBankAccountNumber)
                val newBankName = bankMap[newBankAccountNumber].toString()


                // Go to Cash Out Page
                val currentBalance =  goToCashOutPage()

                //Change Bank

                it.macro("[Change Bank]", newBankAccountNumber, newBankName)

                //Create Bank Charges Map
                val bankChargesMap = bankChargesMap()

                //Select Bank Charge on Map
                val bankCharge = bankChargesMap[newBankName].toString()

                //Create Cash Out

                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, newBankName, newBankAccountNumber, currentBalance)

                //Review Transaction
                val requestedDateTime = getCurrentDateTime().toString()
                it.macro("[Review Transaction]",withdrawAmt, newBankName, newBankAccountNumber, accountName, pin , bankCharge, has200KWithdraw)

                //Compare New Balance
                it.macro("[New Balance]", currentBalance, withdrawAmt)

                //View Latest Cash Out Pending
                val paymentMethod = "Cash Out via Bank"
                val transactionNumber = viewLatestCashOut( withdrawAmt, paymentMethod, requestedDateTime, newBankName, accountName, newBankAccountNumber, bankCharge, currentBalance, time)

                it.terminateApp()
                it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Add/Remove Bank Scenarios")
    @Order(6)
    @Ignore
    fun addRemoveBank() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", "1", pin, useUpdate)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    //Choose Random Bank
                    val bankName = selectRandomBank()

                    goToBankAccountsPage(DefaultBankAccountNumber)

                    val bankLengthMap = bankLengthDigits()

                    val bankLengthDigits = bankLengthMap[bankName]

                    val accountNumber = addBank(pword, bankName, bankLengthDigits.toString(), shortName)


                    it.macro("[Remove Bank]", accountNumber, bankName, pword, accountName)

                    //Check if Bank Account is not in the list anymore
                    it.dontExist(accountNumber)

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    @Test
    @DisplayName("Trading PIN Cash Out Scenarios")
    @Order(7)
    fun tradingPINCashOut(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Random Withdraw Amount ranging from 500.00 - 600.00
                    val withdrawAmt = randomCashOutAmount()

                    // Go to Cash Out Page
                    val currentBalance = goToCashOutPage()

                    //Create Bank Charges Map
                    val bankChargesMap = bankChargesMap()

                    //Select Bank Charge on Map
                    val bankCharge = bankChargesMap[DefaultBankAccountName].toString()

                    //Create Cash Out

                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                    //Change PIN to invalid trading PIN
                    pin = "111111"
                    //Review Transaction
                    val requestedDateTime = getCurrentDateTime().toString()

                    it.macro("[Enter Trading Pin Only]", pin, has200KWithdraw)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Invalid trading pin.")

                    it.exist("Invalid trading pin.")

                    //Enter Correct PIN
                    pin = "123123"

                    it.macro("[Enter Trading Pin Only]", pin, has200KWithdraw)

                    //Compare New Balance
                    it.macro("[New Balance]", currentBalance, withdrawAmt)

                    //View Latest Cash Out Pending
                    val paymentMethod = "Cash Out via Bank"
                    val transactionNumber = viewLatestCashOut(
                        withdrawAmt,
                        paymentMethod,
                        requestedDateTime,
                        DefaultBankAccountName,
                        accountName,
                        DefaultBankAccountNumber,
                        bankCharge, currentBalance, time
                    )


                    it.terminateApp()
                    it.launchApp()
                }
            }
        }

    }

    @Test
    @DisplayName("Unverified Cash Out")
    @Order(8)
    @Ignore
    fun unverifiedCashOut() {
        scenario {
            case(1) {
                expectation {
                    email = "tradetestqa+uploaddocs@gmail.com"
                    pword = "password"

                    it.macro("[Log Out]")

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)
//                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)


                    // Go to Cash Out Page
                    it.macro("[Go to Cash Out Page]")

                    //Check if Add Bank Account Text Exist

                    it.exist("Link your bank accounts to withdraw")
                    it.tap("Add Account")

                    val bankName = selectRandomBank()

                    //Add Bank

                    val bankLengthMap = bankLengthDigits()

                    val bankLengthDigits = bankLengthMap[bankName].toString()


                    it.tap("Add")

                    //Select bank

                    it.tap("Select Bank")
                    it.tap(bankName)

                    //Enter Branch Name

                    it.tap("Enter branch name (e.g. Ayala Avenue)")
                        .sendKeys("Branch Name $bankName")

                    //Create Random Account Digit Number

                    val randomAccountNumber = generateRandomAccountNumber(bankLengthDigits.toInt())

                    //Enter Account Number

                    it.tap("Enter account number")
                        .sendKeys(randomAccountNumber.toString())

                    it.hideKeyboard()

                    it.scrollToBottom()


                    //Enter password

                    it.select("Password")
                        .textIs("Password")

                    it.tapWithScrollDown("Password&&.android.widget.EditText")
                        .sendKeys(pword)

                    //Click Add

                    it.tap("Add")

                    // Wait For Toast Error Message

                    describe("Wait for Toast Message")
                        .waitForDisplay("Updating bank details failed. Please wait for your ID to be approved first.")

                    it.select("Updating bank details failed. Please wait for your ID to be approved first.")
                        .textIs("Updating bank details failed. Please wait for your ID to be approved first.")

                    //Go Back

                    it.tap("\uE909")

                    //Check if Add Bank Account Text Exist

                    it.select("Add Bank Account")


                    it.terminateApp()
                    it.launchApp()

                }
            }
        }
    }

    //Cash Out Approval/Rejected by Admin

    //Cash Out large amount
    
}