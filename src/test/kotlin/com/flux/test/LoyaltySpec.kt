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

    val implementation: ImplementMe = TODO("Create your instance here, if you are a Java person, drop the new keyword and the ;.  e.g. `new MyImpl();` becomes `MyImpl(schemes)`")

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
        private val schemes = listOf(Scheme(schemeId, merchantId, 4, listOf("1")))
    }

}