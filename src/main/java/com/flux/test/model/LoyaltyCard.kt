package com.flux.test.model

data class LoyaltyCard(
        val schemeId: SchemeId,
        val accountId: AccountId,
        var stampCount: Int = 0
){
    fun inreaseStampCount() {
        stampCount +=1
    }

    fun resetStampCount() {
        stampCount = 0
    }
}