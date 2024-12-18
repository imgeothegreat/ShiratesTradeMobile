package macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object UserSettings  : TestDrive {

    @Macro("[Assert Settings Page]")
    fun assertSettingsPage(){
        it.exist("Settings")
        it.exist("App")
        it.exist("Profile")
        it.exist("Bank Accounts")
        it.exist("Trading Account")
    }

    @Macro("[Change Mobile Number]")
    fun changeMobile(mobileNumber:String, newMobile:String, pword: String){
        it.tap(mobileNumber)
            .clearInput()
            .sendKeys(newMobile)

        it.hideKeyboard()
        it.tap("xpath=//android.widget.Button[@text=\"Update\"]")

        describe("Wait for Page to Load")
            .waitForDisplay("Activation", waitSeconds = 30.0)

        it.exist("Activation")

        describe("Wait for Page to Load")
            .waitForDisplay("xpath=//android.widget.EditText[@text=\"Enter activation code\"]", waitSeconds = 30.0)


        it.tap("xpath=//android.widget.EditText[@text=\"Enter activation code\"]")
            .sendKeys("202069")

        it.hideKeyboard()

        it.exist("Resend")
        it.tap("Activate")

        it.wait(1)

        it.dontExist("Activation")
        it.dontExist("Resend")
        it.dontExist("Activate")

    }

    @Macro("[Change Email]")
    fun changeEmail(email: String, newEmail:String, pword:String){
        it.tap("xpath=//android.widget.EditText[@text=\"$email\"]")
            .clearInput()
            .sendKeys(newEmail)

        it.hideKeyboard()

        it.exist("Enter your password to confirm")

        it.tap("xpath=//android.webkit.WebView[@text=\"Email Settings | InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View[2]/android.widget.EditText[2]")
            .sendKeys(pword)

        it.hideKeyboard()

        it.tap("Update Email Address")


        describe("Wait for Page to Load")
            .waitForDisplay("New Pending Email Address", waitSeconds = 30.0)

        it.exist("New Pending Email Address")

        it.exist(newEmail)
        it.exist("xpath=//android.widget.Button[@text=\"Resend\"]")

        describe("Wait for Page to Load")
            .waitForDisplay("xpath=//android.widget.EditText[@text=\"Enter activation code\"]", waitSeconds = 30.0)

        it.tap("xpath=//android.widget.EditText[@text=\"Enter activation code\"]")
            .sendKeys("202070")

        it.hideKeyboard()

        it.tap(  "xpath=//android.widget.Button[@text=\"Submit\"]")
    }

    @Macro("[Login Web View]")
    fun loginWebView(email: String, pword: String){

        describe("Wait for Page to Load")
            .waitForDisplay("Unlock the full power of Investagrams", waitSeconds = 30.0)

        it.scrollToTop()

        describe("Wait for Page to Load")
            .waitForDisplay("Login.", waitSeconds = 30.0)

        it.exist("Login.")
        it.exist("Unlock the full power of Investagrams")

        describe("Wait for Page to Load")
            .waitForDisplay("xpath=//android.widget.EditText[@resource-id=\"opLoginEmail\"]", waitSeconds = 30.0)

        //email
        it.tap("xpath=//android.widget.EditText[@resource-id=\"opLoginEmail\"]")
            .sendKeys(email)

        it.hideKeyboard()

        it.tap("xpath=//android.widget.EditText[@resource-id=\"opLoginPassword\"]")
            .sendKeys(pword)

        it.hideKeyboard()

        it.tap("Login")


    }

    @Macro("[Assert Menu Page]")
    fun assertMenuPage(fullName:String, username: String, uid: String) {


        describe("Wait for Page to Load")
            .waitForDisplay("powered by", waitSeconds = 60.0)


        it.exist("powered by")
        it.exist(fullName)

//        describe("Wait for Page to Load")
//            .waitForDisplay(username)
//        it.exist(username)
        //it.exist("UID: $uid")

        it.exist("Reports")
        it.exist("Transfer of Shares")
        it.exist("Settings")
        it.exist("Help and Support")
        it.exist("Go to Investagrams")
        it.exist("Log out")
    }

    @Macro("[Go To Profile]")
    fun goToProfile(name:String, username: String, uid:String){
        describe("Wait for Page to Load")
            .waitForDisplay("Menu", waitSeconds = 30.0)


        it.tap("Menu")


        it.exist("Menu")
        it.exist("Reports")
        it.exist("Transfer of Shares")
        it.exist("Settings")
        it.exist("Help and Support")
        it.exist("Go to Investagrams")
        it.exist("Log out")


        //it.exist(username)

        it.exist(name)

        //it.exist("UID: $uid")

        it.tap("Settings")
        it.tap("Profile")

    }

    @Macro("[Go To Trading Account Info]")
    fun goToTradingAccountInfo(){

        describe("Wait for Page to Load")
            .waitForDisplay("Menu", waitSeconds = 30.0)


        it.tap("Menu")
        it.tap("Settings")
        it.tap("Trading Account")

    }

    @Macro("[Go To Bank Account List]")
    fun goToBankAccountList(){

        describe("Wait for Page to Load")
            .waitForDisplay("Menu", waitSeconds = 30.0)


        it.tap("Menu")
        it.tap("Settings")
        it.tap("Bank Accounts")

    }

    @Macro("[Go To Help and Support]")
    fun goToHelpSupportPage(){

        describe("Wait for Home Page")
            .waitForDisplay("Menu", waitSeconds = 30.0)

        it.exist("Menu")

        it.tap("Menu")
        it.tap("Help and Support")

    }

    @Macro("[Log Out User]")
    fun logOut(){

        describe("Wait for Home Page")
            .waitForDisplay("Menu", waitSeconds = 30.0)

        it.tap("Menu")
        it.tap("Log out")

    }

    @Macro("[Check Account Info]")
    fun checkAccountInfo(
        name: String,
        sex: String,
        birthday: String,
        birthCountry: String,
        birthCity: String,
        presentAddress:String,
        permanentAddress:String,
        citizenship: String,
        civilStatus: String,
        employmentStatus: String,
        employerAddress: String,
        TINID: String,
        IDNumber: String,
        PEP: String,
        annualIncome: String,
        sourceIncome: String,
        investmentObjectives1: String,
        investmentObjectives2: String,
        investmentObjectives3: String,
        investmentObjectives4: String,
        assets: String,
        netWorth: String,
        usPerson: String
    ) {

        // Click Edit Button
        it.tap("Edit")

        it.exist("Editing your personal info requires validation. If you wish to make any changes in your account details, kindly contact us through any of the following channels:")

        // Check Email

        it.exist("Support@Investa.Trade")

        // Check Number

        it.exist("https://investatrade.tawk.help/")

        // Close
        //it.tap("\uE925")
        it.tap(x=1014,y=1680)

        describe("Wait for Toast Message")
            .waitForDisplay(name)

        // Check Name
        it.exist(name)

        // Check Sex
        it.exist(sex)

        // Check Birthday
        it.exist(birthday)

        // Check Birth Country
        it.exist(birthCountry)

        // Check Birth City
        it.exist(birthCity)

        // Check Citizenship
        it.exist(citizenship)

        // Check Civil Status
        it.exist(civilStatus)

        //Present Address
        it.exist(presentAddress)

        //Permanent Address
        it.scrollDown()
        it.exist(permanentAddress)

        // Check Employment Status
        it.exist(employmentStatus)

        if(employmentStatus == "Employed" || employmentStatus == "Self Employed" || employmentStatus == "Business Owner" || employmentStatus == "Overseas Worker"){
            // Check Employer Address
            it.exist(employerAddress)
        }

        // Check TIN ID
        it.exist(TINID)

        // Check ID Number
        it.exist(IDNumber)

        // Check PEP
        it.exist(PEP)

        it.scrollDown()

        // Check Annual Income
        it.exist(annualIncome)

        // Check Source Income
        it.exist(sourceIncome)

        // Check Investment Objectives
        it.exist(investmentObjectives1)

        it.exist(investmentObjectives2)

        it.exist(investmentObjectives3)

        it.exist(investmentObjectives4)

        it.scrollToBottom()

        // Check Assets
        it.exist(assets)

        // Check Net Worth
        it.exist(netWorth)

        // Check US Person
        it.exist(usPerson)

    }

    @Macro("[Check Profile Info]")
    fun checkProfileInfo(username:String, UID:String , email:String, phoneNumber:String, clientCode:String){
        it.exist("Profile Settings")
        it.exist("Basic Information")
        it.exist("Manage on Investagrams")

        it.tap("Manage on Investagrams")
        it.exist("https://invt-devenv.investagrams.com/Account/Settings/AccountSettings")


        describe("Wait for Page to Load")
            .waitForDisplay("Your One Place for All Things Investing", waitSeconds = 30.0)

        it.exist("Your One Place for All Things Investing")

//        describe("Wait for Page to Load")
//            .waitForDisplay("Login.", waitSeconds = 30.0)

//        it.exist("Login.")
//        it.exist("Unlock the full power of Investagrams")

        //it.tap("\uE908")
        it.tap(x=55,y=193)

        describe("Wait for Page to Load")
            .waitForDisplay("Profile Settings", waitSeconds = 30.0)

        it.exist("Profile Settings")
        it.exist("Basic Information")
        it.exist("Manage on Investagrams")

        it.exist("Username")
        //it.exist("*$username*")

        it.exist("Email")
        it.exist(email)

        it.exist("Mobile Number")
        it.exist(phoneNumber)

        it.exist("Account Details")
        it.exist("User ID")
        describe("Wait for Page to Load")
            .waitForDisplay(UID, waitSeconds = 30.0)

        it.exist(UID)

        it.exist("PSE Client Code")
        it.exist(clientCode)

        it.exist("Password")
        it.exist("Trading Pin")

    }

    @Macro("[Change Password]")
    fun changePassword(pword:String , newPassword:String){

        it.tap("Password")

        it.scrollUp()

        describe("Wait for Page to Load")
            .waitForDisplay("Change or update your password. All information under this settings are kept privately.", waitSeconds = 30.0)

        it.exist("Password Settings")
        it.exist("Change or update your password. All information under this settings are kept privately.")


        //Tap Save without data

        //it.tap("Save")

        it.exist("Current Password")

        it.tap("xpath=//android.webkit.WebView[@text=\"Password Settings | InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[3]/android.widget.EditText")
            .sendKeys(pword)

        it.hideKeyboard()

        it.exist("••••••••••••")

        //Show password

        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[1]")

        it.exist(pword)

        it.exist("New Password")
        it.tap("Enter new password")
            .sendKeys(newPassword)

        it.hideKeyboard()

        it.exist("••••••••••••")

        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[2]")

        it.exist(newPassword)

        it.exist("Confirm Password")
        it.tap("xpath=//android.webkit.WebView[@text=\"Password Settings | InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[7]/android.widget.EditText")
            .sendKeys(newPassword)

        it.hideKeyboard()

        it.exist("••••••••••••")

        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[2]")

        it.exist(newPassword)

        it.tap("xpath=//android.widget.Button[@text=\"Change Password\"]")




    }

    @Macro("[Change PIN]")
    fun changePIN(pin:String , newPin:String){

        it.tap("Trading Pin")

        describe("Wait for Page to Load")
            .waitForDisplay("Change or update your trading pin. All information under this settings are kept privately.", waitSeconds = 30.0)

        it.exist("Trading Pin Settings")
        it.exist("Change or update your trading pin. All information under this settings are kept privately.")

        it.exist("Current Trading Pin")
        it.tap("xpath=//android.webkit.WebView[@text=\"Trading Pin Settings | InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[3]/android.widget.EditText")
            .sendKeys(pin)

        it.hideKeyboard()

        it.exist("••••••")

        //Show pin

        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[1]")

        it.exist(pin)

        it.exist("New Trading Pin")
        it.tap("Enter new trading PIN")
            .sendKeys(newPin)

        it.hideKeyboard()

        it.exist("••••••")

        //Show pin
        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[2]")

        //Show pin

        it.exist(newPin)

        it.exist("Confirm Trading Pin")
        it.tap("xpath=//android.webkit.WebView[@text=\"Trading Pin Settings | InvestaTrade\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[7]/android.widget.EditText")
            .sendKeys(newPin)

        it.exist("••••••")

        //Show pin
        it.tap("xpath=(//android.widget.Button[@text=\"eye-icon-slashed.png?i_1.0.9005\"])[3]")

        it.exist(newPin)

        it.exist("Forgot Trading Pin?")

        it.tap("Change Trading Pin")


    }


}