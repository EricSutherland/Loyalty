package com.flux.test

import com.flux.test.model.AccountId
import com.flux.test.model.Item
import com.flux.test.model.MerchantId
import com.flux.test.model.Receipt
import com.flux.test.model.Scheme
import com.flux.test.model.SchemeId
import io.kotlintest.IsolationMode
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.UUID

class LoyaltySpec : StringSpec() {

    val implementation: ImplementMe = LoyaltyProcessor(schemes)

    init {
        "Applies a stamp" {
            val receipt = Receipt(merchantId = merchantId, accountId = accountId, items = listOf(Item("1", 100, 1)))

            val response = implementation.apply(receipt)

            response shouldHaveSize (1)
            response.first().stampsGiven shouldBe 1
            response.first().currentStampCount shouldBe 1
            response.first().paymentsGiven shouldHaveSize 0
        }

        "Triggers a redemption" {
            val receipt =
                Receipt(merchantId = merchantId, accountId = accountId, items = 1.rangeTo(5).map { Item("1", 100, 1) })
            val response = implementation.apply(receipt)

            response shouldHaveSize (1)
            response.first().stampsGiven shouldBe 4
            response.first().currentStampCount shouldBe 0
            response.first().paymentsGiven shouldHaveSize 1
            response.first().paymentsGiven.first() shouldBe 100
        }

        "Cheapest item is redeemed first" {
            val receiptItems: List<Item> = 1.rangeTo(5).map { x -> Item("1",  x.toLong(), 1) }
            val receipt = Receipt(merchantId = merchantId, accountId = accountId, items = receiptItems)
            val response = implementation.apply(receipt)

            response shouldHaveSize (1)
            response.first().stampsGiven shouldBe 4
            response.first().currentStampCount shouldBe 0
            response.first().paymentsGiven shouldHaveSize 1
            response.first().paymentsGiven.first() shouldBe 1
        }

        "Only applies to the items on the scheme" {
            val receiptItems: List<Item> = listOf(
                Item("1",  100, 1),
                Item("2",  100, 1),
                Item("3",  100, 1)
            )
            val receipt = Receipt(merchantId = merchantId, accountId = accountId, items = receiptItems)
            val response = implementation.apply(receipt)

            response shouldHaveSize (1)
            response.first().stampsGiven shouldBe 2
            response.first().currentStampCount shouldBe 2
        }

        "Applies stamps for the total quantity" {
            val receipt = Receipt(merchantId = merchantId, accountId = accountId, items = listOf(Item("1", 100, 2)))

            val response = implementation.apply(receipt)

            response shouldHaveSize (1)
            response.first().stampsGiven shouldBe 2
            response.first().currentStampCount shouldBe 2
        }

        "Stores the current state for an account" {
            val receipt = Receipt(merchantId = merchantId, accountId = accountId, items = listOf(Item("1", 100, 1)))

            implementation.apply(receipt)
            val response = implementation.state(accountId)

            response shouldHaveSize (1)
            response.first().currentStampCount shouldBe 1
            response.first().payments shouldHaveSize 0
        }
    }

    override fun isolationMode() = IsolationMode.InstancePerTest

    companion object {
        private val accountId: AccountId = UUID.randomUUID()
        private val merchantId: MerchantId = UUID.randomUUID()

        private val schemeId: SchemeId = UUID.randomUUID()
        private val schemeItemIds: List<String> = listOf("1", "2")
        private val schemes = listOf(Scheme(schemeId, merchantId, 4, schemeItemIds))
    }

}