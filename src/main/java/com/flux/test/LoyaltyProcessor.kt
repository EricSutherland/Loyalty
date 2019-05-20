package com.flux.test

import com.flux.test.model.*

class LoyaltyProcessor(scheme: List<Scheme>) : ImplementMe {
    override var schemes: List<Scheme> = scheme

    private var loyaltyCards: MutableList<LoyaltyCard> = mutableListOf()

   override fun apply(receipt: Receipt): List<ApplyResponse>{
       val schemeItems: List<Scheme>  =  schemes.filter { scheme: Scheme -> scheme.merchantId == scheme.merchantId }
       val response: MutableList<ApplyResponse> = mutableListOf()

       schemeItems.forEach { currentScheme: Scheme ->
           val card = getLoyaltyCard(currentScheme.id, receipt.accountId)
           val redeemableItems: List<Item> = sortRedeemableItems(receipt, currentScheme.skus)

           response.add(calculateSchemeStamps(currentScheme, card, redeemableItems))
       }
       return response
   }

    override fun state(accountId: AccountId): List<StateResponse> {
        val accountCards = loyaltyCards.filter { loyaltyCard: LoyaltyCard -> loyaltyCard.accountId == accountId }
        val response: MutableList<StateResponse> = mutableListOf()

        accountCards.forEach { loyaltyCard: LoyaltyCard ->
            response.add(StateResponse(loyaltyCard.schemeId, loyaltyCard.stampCount, loyaltyCard.paymentsAwarded))
        }
        return response
    }

    private fun getLoyaltyCard(schemeId: SchemeId, accountId: AccountId): LoyaltyCard {
        var card: LoyaltyCard? = loyaltyCards.find { loyaltyCard: LoyaltyCard ->
            loyaltyCard.accountId == accountId && loyaltyCard.schemeId == schemeId
        }

        if (card == null) {
            card = LoyaltyCard(schemeId, accountId, 0)
            loyaltyCards.add(card)
        }

        return card
    }

    private fun sortRedeemableItems(receipt: Receipt, schemeItemIds: List<String>): List<Item> {
        val redeemableItems: MutableList<Item> = mutableListOf()
        receipt.items.forEach { item ->
            if (schemeItemIds.contains(item.sku)) {
                for (count in 1..item.quantity){
                    redeemableItems.add(item)
                }
            }
        }

        return redeemableItems.sortedBy { item: Item -> item.price  }
    }

    private fun calculateSchemeStamps(scheme: Scheme, loyaltyCard: LoyaltyCard, redeemableItems: List<Item>): ApplyResponse {
        val payments: MutableList<Long> = mutableListOf()
        var stampsGiven = 0
        var redemptions = 0
        var validItemCount = redeemableItems.size

        while (stampsGiven < validItemCount) {
            if (loyaltyCard.stampCount == scheme.maxStamps) {
                validItemCount -= 1
                redemptions += 1
                loyaltyCard.resetStampCount()
            } else {
                stampsGiven += 1
                loyaltyCard.inreaseStampCount()
            }
        }

        for (count in 1..redemptions) {
            val award: Long = redeemableItems[count - 1].price
            payments.add(award)
            loyaltyCard.awardPayment(award)
        }

        return ApplyResponse(scheme.id, loyaltyCard.stampCount, stampsGiven, payments)
    }
}