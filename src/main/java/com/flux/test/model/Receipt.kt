package com.flux.test.model

import java.util.UUID

data class Receipt(
    val id: ReceiptId = UUID.randomUUID(),
    val accountId: AccountId,
    val merchantId: MerchantId,
    val items: List<Item>
)

data class Item(
    /**
     * The unique ID of the item - only unique for one merchant and may be duplicated across merchants
     */
    val sku: String,

    /**
     * The price of one instance of this item, the total amount of the receipt is the sum of item.price * item.quantity for all the items in the receipt
     */
    val price: Long,

    /**
     * Quantity of this item purchased, if an item has a quantity of 2 then it can generate 2 stamps
     */
    val quantity: Int
)