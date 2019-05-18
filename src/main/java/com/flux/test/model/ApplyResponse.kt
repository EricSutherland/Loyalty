package com.flux.test.model

/**
 * Represents the API response that would be given for the apply resource
 */
data class ApplyResponse(
    /**
     * The ID for the scheme that was applied
     */
    val schemeId: SchemeId,

    /**
     * The current stamp position for the account in this scheme, e.g. if the account has 1/4 stamps then this would return 1
     */
    val currentStampCount: Int,

    /**
     * The total number of stamps given to the account in this apply, not including stamps given in previous apply operations
     */
    val stampsGiven: Int,

    /**
     * The total number of payments awarded to the account in this apply, not including payments given in previous apply operations
     */
    val paymentsGiven: List<Long>
)