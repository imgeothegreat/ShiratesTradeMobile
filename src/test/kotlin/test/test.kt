package test

import macro.Trade
import macro.Trade.totalAmountComputation
import kotlin.math.max
import kotlin.math.min


fun main() {

    val lastPrice = "31"
    val minQuantity = "200"
    val broker = "1"
    val isBuy = true

    val grossAmount = lastPrice.toDouble() * minQuantity.toDouble()

    //Compute Broker Commission
    var brokerCommissionValue = Trade.brokerCommission * grossAmount
    if(brokerCommissionValue < 20){
        brokerCommissionValue = 20.0
    }

    //Compute VAT
    var VATValue = brokerCommissionValue * Trade.VAT

    //Compute PSEFee
    var PSEFeeValue = grossAmount * Trade.PSEFee

    //Compute PSEVat
    var PSEVATValue = PSEFeeValue * Trade.PSEVAT

    //Compute SEC Fee
    val SECFEEValue = grossAmount * Trade.SECFee

    //Compute SCCP Fee
    val SCCPValue = grossAmount * Trade.SCCP

    //Compute Sales Tax
    val SalesTaxValue = grossAmount * Trade.SalesTax

    //Total Fees
    var totalBuyCharges =  brokerCommissionValue + VATValue + PSEFeeValue + PSEVATValue + SECFEEValue + SCCPValue

    //Total Sell Charges
    var totalSellCharges = brokerCommissionValue + VATValue + PSEFeeValue + PSEVATValue + SECFEEValue + SCCPValue + SalesTaxValue

    var totalBuyAmount = 0.00
    var totalSellAmount = 0.00

    if(broker == "1"){

        //No PSE VAT
        totalBuyCharges = totalBuyCharges - PSEVATValue
        totalSellCharges = totalSellCharges - PSEVATValue

    }

    //Return Computation
    if(isBuy){

        print( String.format("%.2f", totalBuyCharges))
    } else {
        print(String.format("%.2f", totalSellCharges))
    }

}