package user_settings

import macro.Onboarding.changeLastDigit
import macro.Portfolio
import macro.Trade
import macro.Wallet
import macro.Wallet.addBank
import macro.tapBackButton
import kotlin.test.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.testcode.UITest
import kotlin.test.Ignore
@Testrun("testConfig/android/androidSettings/testrun.properties")
class UserSettings : UITest() {

    private var email = "tradetestqa+superuser@gmail.com"
    private var pword = "password123$"
    private var newPassword = "password456$"
    private var pin = "123123"
    private var newPin = "123456"
    private var useUpdate = false
    private var broker = "2"
    private val hasStocks = false

    //user information
    private val name = "TRADETEST SDFSDF SUPERUSER"
    private var firstName = "Tradetest"
    private val shortName = "Tradetest Superuser"
    private val sex = "Female"
    private val birthday = "07/05/2006"
    private val birthCountry = "Philippines"
    private val birthCity = "dasdasd"
    private val citizenship = "Philippines"
    private val civilStatus = "Single"
    private val presentAddress = "ASD, Ambago, Butuan City (Capital), Agusan Del Norte, Philippines, 123"
    private val permanentAddress = "ASD, Santa Cruz, Paombong, Bulacan, Philippines, 324"
    private val employmentStatus = "Unemployed"
    private val employerAddress = "-- --"
    private val TINNumber = "453453453453"
    private val GSISNumber = "3453453453"
    private val PEP = "No"
    private val annualIncome = "Less than 500,000"
    private val sourceIncome = "• Family / Inheritances"
    //private val investmentObjectives1 = "• Long-Term Investment"
    private val investmentObjectives1 = "3. Long-Term Investment"
    private val investmentObjectives2 = "2. Growth"
    private val investmentObjectives3 = "4. Speculation"
    private val investmentObjectives4 = "1. Capital Preservation"
    private val assets = "Less than 5 Million"
    private val networth = "Less than 5 Million"
    private val usPerson = "No"

    //profile
    private val username = "@tradet4636"
    private val UID = "4617"
    private val phoneNumber = "+639956154144"
    private val clientCode =  "345345456"

    //order confirmation
    private val accountName = "Tradetest Sdfsdf Superuser"
    private val brokerName = "Broker 2"

    @Test
    @DisplayName("Go to Biometrics")
    @Order(1)
    fun goToBiometrics() {
        scenario {
            case(1) {
                expectation {

                    it.macro("[Login]", email, pword)
                    it.macro("[Select Broker]", broker)
                    it.macro("[Enter Trading Pin]", pin, useUpdate)

                    describe("Wait for Home Page")
                        .waitForDisplay("Portfolio", waitSeconds = 60.0)


                    it.tap("Portfolio")

                    describe("Wait for Home Page")
                        .waitForDisplay("Trade Analytics", waitSeconds = 60.0)


                    it.tap("Menu")

                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    it.tap("Settings")

                    it.macro("[Assert Settings Page]")

                    it.tap("App")

                    it.exist("App Settings")
                    it.exist("Biometrics Unlock")
                    it.exist("Note: All biometrics stored on this device can be used to access your account. We recommend this if you are not sharing this device with anyone.")

                    it.tap("xpath=//android.widget.Switch")
                    it.tap("xpath=//android.widget.Switch")


                    it.terminateApp()
                    it.launchApp()
                }

            }
        }

    }

    @Test
    @DisplayName("Profile Account")
    @Order(2)
    fun profileAccountInfo(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
//                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)
                    it.macro("[Go To Profile]", shortName,username,UID)

                    it.exist("Profile Settings")
                    it.macro("[Check Profile Info]", username, UID, email, phoneNumber, clientCode)
                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Change Email")
    @Ignore
    @Order(3)
    fun changeEmail(){
        scenario {
            case(1) {
                expectation {
//                     it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
//                     it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)
                    it.tap("Menu")

                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    it.tap("Settings")

                    it.macro("[Assert Settings Page]")

                    it.tap("Profile")

                    it.tap("Email")

                    it.exist("Email Settings")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Change and verify your email address. All information under this settings are kept privately.")

                    it.exist("Change and verify your email address. All information under this settings are kept privately.")

                    it.exist("Registered Email Address")

                    //Change Email
                    it.macro("[Change Email]", email, "1$email", pword)
                    //it.exist("You have successfully updated your email address.")
                    //it.exist("You have successfully changed your email from: $email to: 1$email.")

                    //Go Back to Original Email
                    it.macro("[Change Email]", "1$email", email, pword)
                    //it.exist("You have successfully updated your email address.")

                    it.tap("\uE908")
                    it.exist("Verified")
                    it.exist(email)

                    it.exist("Profile Settings")
                    it.exist("Basic Information")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Change Mobile Number")
    @Ignore
    @Order(4)
    fun changeMobileNumber(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)
                    it.tap("Menu")

                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    it.tap("Settings")

                    it.macro("[Assert Settings Page]")

                    it.tap("Profile")

                    it.tap("Mobile Number")


                    describe("Wait for Page to Load")
                        .waitForDisplay("Change and verify your mobile number. All information under this settings are kept privately.")

                    it.exist("Mobile Number Settings")
                    it.exist("Change and verify your mobile number. All information under this settings are kept privately.")
                    it.exist("Registered Mobile Number")

                    //Change Email

                    val newPhoneNumber = changeLastDigit(phoneNumber)

                    it.macro("[Change Mobile Number]", phoneNumber, newPhoneNumber, pword)
                    //it.exist("You have successfully updated your email address.")
                    //it.exist("You have successfully changed your email from: $email to: 1$email.")

                    //Go Back to Original Email
                    it.macro("[Change Mobile Number]", newPhoneNumber, phoneNumber, pword)
                    //it.exist("You have successfully updated your email address.")

                    it.tap("\uE908")
                    it.exist(phoneNumber)
                    it.exist("Verified")

                    it.exist("Profile Settings")
                    it.exist("Basic Information")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Check Bank Account List")
    @Order(5)
    fun bankAccountInfo(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    it.macro("[Go To Bank Account List]")

                    it.scrollUp()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Bank Accounts", waitSeconds = 30.0)


                    //Check if redirected to bank
                    it.exist("Bank Accounts")
                    it.exist("Link your bank accounts to withdraw")
                    it.exist("Add Account")

                    //Check Bank 1

                    //it.exist("Banco De Oro (BDO)")


                    it.exist("**********34")
                    it.exist("Default")
                    it.exist("Edit")


                    //Add Bank

                    //Select Random Bank

//                    val bankName = Wallet.selectRandomBank()
//
//                    val bankLengthMap = Wallet.bankLengthDigits()
//
//                    val bankLengthDigits = bankLengthMap[bankName]
//
//                    val accountNumber = addBank(pword, bankName,  bankLengthDigits.toString(), shortName)
//
//                    it.macro("[Save Changes in Bank]", bankName, accountNumber, shortName, pword)
//
//                    it.macro("[Remove Bank]", accountNumber, bankName, pword, shortName)

                    it.terminateApp()
                    it.launchApp()


                }
            }
        }

    }

    @Test
    @DisplayName("Check Trading Account Information")
    @Order(6)
    fun tradingAccountInfo() {
        scenario {
            case(1) {
                expectation {

//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    it.macro("[Go To Trading Account Info]")

                    it.exist("Trading Account")

                    it.macro("[Check Account Info]", name,
                        sex,
                        birthday,
                        birthCountry,
                        birthCity,
                        presentAddress,
                        permanentAddress,
                        citizenship,
                        civilStatus,
                        employmentStatus,
                        employerAddress,
                        TINNumber,
                        GSISNumber,
                        PEP,
                        annualIncome,
                        sourceIncome,
                        investmentObjectives1,
                        investmentObjectives2,
                        investmentObjectives3,
                        investmentObjectives4,
                        assets,
                        networth,
                        usPerson)

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }

    }

    @Test
    @DisplayName("Change Password and PIN Scenarios")
    @Ignore
    @Order(7)
    fun changeUserData() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    it.macro("[Go To Profile]", shortName,username,UID)

                    //Change Password

                    it.macro("[Change Password]", pword, newPassword)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Password changed")

                    it.exist("Password changed")

                    pword = newPassword

                    //Cannot change password to old password

                    newPassword = "password123$"

                    it.macro("[Change Password]", pword, newPassword)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Your new password must be different from your old password.")

                    it.exist("Your new password must be different from your old password.")

                    //Go Back
                    it.wait(5)
                    it.tap( "xpath=//android.widget.TextView[@text=\"\uE909\"]")

                    //Change to 2nd new password, so we can bring back the password to the original password

                    newPassword = "password789$"

                    it.macro("[Change Password]", pword, newPassword)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Password changed")

                    it.exist("Password changed")

                    pword = newPassword

                    newPassword = "password123$"

                    it.macro("[Change Password]", pword, newPassword)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Password changed")

                    //Change to new PIN

                    it.macro("[Change PIN]", pin, newPin)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Trading PIN changed")

                    //Cannot change to OLD PIN

                    pin = newPin

                    newPin = "123123"

                    it.macro("[Change PIN]", pin, newPin)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Your new trading pin must be different from your old trading pin.")


                    //Change to new 2nd PIN

                    newPin = "123789"

                    //Go Back
                    it.wait(5)
                    it.tap( "xpath=//android.widget.TextView[@text=\"\uE909\"]")

                    it.macro("[Change PIN]", pin, newPin)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Trading PIN changed")
                    //Change to Original PIN
                    pin = newPin
                    newPin = "123123"

                    it.macro("[Change PIN]", pin, newPin)

                    describe("Wait for Toast Message")
                        .waitForDisplay("Trading PIN changed")

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }

    @Test
    @DisplayName("Reports Page")
    @Order(8)
    fun reportsPage(){
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Menu", waitSeconds = 30.0)


                    it.tap("Menu")

                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    it.tap("Reports")

                    it.exist("Reports")

                    it.scrollUp()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Statement of Account", waitSeconds = 30.0)

                    describe("Wait for Page to Load")
                        .waitForDisplay("You may contact your broker for the official copy at 12311323 or send them an email at invtrade_user@gmail.com", waitSeconds = 60.0)

                    it.exist("View consolidated records from user transaction for the given month")
                    it.exist("Select month\uF078")
                    it.exist("xpath=(//android.widget.Button[@text=\"View\"])[1]")

                    it.exist("Order Confirmation")
                    it.exist("View generated daily records from user transaction")
                    it.exist("Select month\uF078")
                    it.exist("Select date\uF078")
                    it.exist("xpath=(//android.widget.Button[@text=\"View\"])[2]")

                    it.tap("xpath=(//android.widget.TextView[@text=\"Select month\uF078\"])[2]")
                    it.tap(x=147,y=1468)
                    it.tap("Select date\uF078")
                    it.tap(x=665,y=1468)
                    it.tap("xpath=(//android.widget.Button[@text=\"View\"])[2]")

                    describe("Wait for Page to Load")
                        .waitForDisplay("BROKER 2", waitSeconds = 30.0)
                    describe("Wait for Page to Load")
                        .waitForDisplay("MEMBER PHILIPPINE STOCK EXCHANGE", waitSeconds = 30.0)
                    it.exist("in the Philippines 456")
                    it.exist("VAT REG. TIN # 12312313")
                    it.exist("ORDER CONFIRMATION")
                    it.exist("Account Name")
                    it.exist(accountName)
                    it.exist("PSE Client Code")
                    it.exist(clientCode)
                    it.exist("Type")
                    it.exist("Unsolicited")
                    it.exist("Reference No.")
                    it.exist("Date - Generated")
                    it.exist("Date - First Seen")
                    it.exist("IP Address - Country")

//                    it.scrollToBottom()
//
//                    it.wait(1)
//                    it.exist("IMPORTANT:")
//                    it.exist("It is client's responsibility to review the data and amounts appearing herein which")
//                    it.exist("shall consider accurate and binding upon the client if no objection in writing is given within three (3) days from receipt hereof.")
//                    it.exist("reserves the right to determine the validity of client's objections to the transaction.")
//
//                    it.exist("** THIS DOCUMENT IS NOT VALID FOR CLAIMING INPUT TAXES **")
//                    it.exist("app_icon")
//                    it.exist("Please contact your Broker to request for the official receipt of your Transaction.")
//                    it.exist("Phone:")
//                    it.exist("1231231")
//                    it.exist("Email:")
//                    it.exist("invtrade_user@gmail.com")

                    //close
                    //it.tap("\uE908")
                    it.tap(x=55,y=193)

                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    it.terminateApp()
                    it.launchApp()
                }
            }
        }
    }


    //Go to Help and Support
    @Test
    @DisplayName("Go to Help and Support")
    @Order(9)
    fun gotoHelpSupport(){
        scenario {
            case(1) {
                expectation {

//                   it.macro("[Login]", email, pword)
//                    it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Home Page")
                        .waitForDisplay("Menu", waitSeconds = 30.0)

                    it.exist("Menu")

                    it.macro("[Go To Help and Support]")

                    it.launchApp()

                    it.terminateApp()
                    it.launchApp()

                }
            }
        }

    }

    //Go to Investagrams
    @Test
    @DisplayName("Go to Investagrams")
    @Order(10)
    fun gotoInvestagrams() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Menu", waitSeconds = 30.0)


                    it.tap("Menu")

                    it.exist("Reports")
                    it.exist("Help and Support")

                    it.tap( "Go to Investagrams")

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }


    }

    //Read Latest Notif and Seach Explore
    @Test
    @DisplayName("Explore Search and Read Latest Notif")
    @Ignore
    @Order(11)
    fun exploreSearchAndNotifs() {
        scenario {
            case(1) {
                expectation {
//                                        it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    //Read Latest Notif
                    it.macro("[Read Latest Notifs]")

                    val randomStockCodeMain = Portfolio.getUniqueStock()
                    val stockNameMain = Trade.getStockNameByCode(randomStockCodeMain)


                    it.macro("[Search Stock]", randomStockCodeMain, stockNameMain)
                    it.macro("[Explore Asset Page]", randomStockCodeMain, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMain, stockNameMain)

                    //Inside Search
                    val randomStockCodeMainInside = Portfolio.getUniqueStock()
                    val stockNameMainInside = Trade.getStockNameByCode(randomStockCodeMainInside)

                    it.macro("[Search Stock]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Explore Asset Page]", randomStockCodeMainInside, hasStocks)
                    it.macro("[View Full Chart]", randomStockCodeMainInside, stockNameMainInside)
                    it.macro("[Save to Watcher]", randomStockCodeMainInside, stockNameMainInside)

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }

    }


    //Go to Investagrams
    @Test
    @DisplayName("Transfer of Shares")
    @Order(12)
    fun gotoTransferOfShares() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)


                    describe("Wait for Page to Load")
                        .waitForDisplay("Menu", waitSeconds = 30.0)


                    it.tap("Menu")

                    it.exist("Reports")
                    it.exist("Help and Support")

                    it.tap( "Transfer of Shares")

                    it.scrollUp()

                    it.exist("Transfer of Shares")

                    describe("Wait for Page to Load")
                        .waitForDisplay("Statement of Account", waitSeconds = 60.0)


                    it.exist("Statement of Account")
                    it.exist("Transfer Stocks Coming from Another Broker\uF078")

                    it.exist("NOTE: The payment will be deducted from your trading wallet account, please make sure to fund your account and that your trading account is fully activated.")
                    it.exist("Request")

                    it.wait(2)

                    tapBackButton()

                    describe("Wait for Page to Load")
                        .waitForDisplay("Help and Support", waitSeconds = 60.0)

                    it.exist("Help and Support")
                    it.exist("Settings")

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }


    }

    //Dark mode and Light Mode
    //Go to Investagrams
    @Test
    @DisplayName("Dark and Light Mode")
    @Order(13)
    fun darkModeAndLightMode() {
        scenario {
            case(1) {
                expectation {
//                    it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    describe("Wait for Page to Load")
                        .waitForDisplay("Menu", waitSeconds = 30.0)


                    it.tap("Menu")

                    it.exist(brokerName)
                    it.exist("OFF")
                    it.dontExist("ON")

                    it.tap("OFF")

                    it.dontExist("OFF")

                    it.tap("ON")

                    it.dontExist("ON")
                    it.exist("OFF")

                    it.exist("Version")

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }


    }

    //Logout User

    @Test
    @DisplayName("Log Out User Scenario")
    @Order(14)
    fun logOutUser() {
        scenario {
            case(1) {
                expectation {
//                                        it.macro("[Login]", email, pword)
//                     it.macro("[Select Broker]", broker, pin, useUpdate)
                    //                    it.macro("[Enter Trading Pin]", pin, useUpdate)
                    it.macro("[Lock Trading Pin]", pin, firstName)

                    //Logout but press No
                    it.macro("[Log Out User]")

                    it.tap("NO")

                    //Check if user was not log out
                    it.macro("[Assert Menu Page]", shortName, username, UID)

                    //Logout but press yes

                    it.macro("[Log Out User]")

                    it.tap("YES")

                    it.exist("Let’s Get Started")

                    it.exist("Invest effortlessly in what is suitable for you")

                    it.exist("Log In")

                    it.terminateApp()
                    it.launchApp()
                }

            }
        }

    }


    //Waive PIN

    //Waive Password

    //Waive Update KYC

    //Update Expired KYC


}