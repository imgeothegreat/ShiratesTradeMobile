package wallet

import macro.Wallet
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.terminateApp
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Test

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CashOutAdmin  : UITest() {

    private val shortName = "Tradetest Superuser"
    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var broker = "2"
    private var pin = "123123"
    private var accountName = "Tradetest Superuser"
    private val useUpdate = false
    private val has200KWithdraw = false

    // use for reference later in the test scenarios
    //private var DefaultBankAccountName = "Banco De Oro (BDO)"
    private var DefaultBankAccountName = "Bank of the Philippine Islands (BPI)"
    private var DefaultBankAccountNumber = "3453453453"

    @Test
    @DisplayName("Approve Cash Out Admin")
    @Order(1)
    fun validCashOutTest() {
        scenario {
            case(1) {
                expectation {
                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("About My Portfolio", waitSeconds = 30.0)

                    //Random Withdraw Amount ranging from 500.00 - 600.00
                    val withdrawAmt = Wallet.randomCashOutAmount()

                    // Go to Cash Out Page
                    val currentBalance = Wallet.goToCashOutPage()

                    //Create Bank Charges Map
                    val bankChargesMap = Wallet.bankChargesMap()

                    //Choose Random Bank Account
                    val bankCharge = bankChargesMap[DefaultBankAccountName].toString()

                    //Create Cash Out
                    it.macro("[Make Cash Out]",withdrawAmt, bankCharge, DefaultBankAccountName, DefaultBankAccountNumber, currentBalance)

                    //Review Transaction

                    val requestedDateTime = Wallet.getCurrentDateTime().toString()
                    it.macro("[Review Transaction]",withdrawAmt, DefaultBankAccountName, DefaultBankAccountNumber, accountName, pin, bankCharge, has200KWithdraw)

                    //Compare New Balance
                    it.macro("[New Balance]", currentBalance, withdrawAmt)

                    //View Latest Cash Out Pending
                    val paymentMethod = "Cash Out via Bank"
                    it.macro("[View Latest Pending Cash Out]", withdrawAmt, paymentMethod, requestedDateTime, DefaultBankAccountName, accountName, DefaultBankAccountNumber, bankCharge)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

}