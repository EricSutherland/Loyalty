package com.flux.test.model

import java.util.UUID

data class Scheme(
    val id: SchemeId,
    val merchantId: MerchantId,
    val maxStamps: Int,
    val skus: List<String>
)