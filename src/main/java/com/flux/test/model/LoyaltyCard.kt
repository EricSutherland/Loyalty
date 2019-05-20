package com.flux.test.model

data class LoyaltyCard(
        val schemeId: SchemeId,
        val accountId: AccountId,
        var stampCount: Int = 0,
        var paymentsAwarded: MutableList<Long> = mutableListOf()
){
    fun inreaseStampCount() {
        stampCount +=1
    }

    fun resetStampCount() {
        stampCount = 0
    }

    fun awardPayment(payment: Long) {
        paymentsAwarded.add(payment)
    }
}