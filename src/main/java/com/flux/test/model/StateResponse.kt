package com.flux.test.model

import java.util.UUID

/**
 * Represents the API response that would be given for when retrieving the current state for an account
 */
data class StateResponse(
    /**
     * The ID for the scheme that was applied
     */
    val schemeId: SchemeId,

    /**
     * The current stamp position for the account in this scheme, e.g. if the account has 1/4 stamps then this would return 1
     */
    val currentStampCount: Int,

    /**
     * The payments that have been awareded to this account for this scheme
     */
    val payments: List<Long>
)
