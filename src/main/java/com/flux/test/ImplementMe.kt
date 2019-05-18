package com.flux.test

import com.flux.test.model.AccountId
import com.flux.test.model.ApplyResponse
import com.flux.test.model.Receipt
import com.flux.test.model.Scheme
import com.flux.test.model.StateResponse
import java.util.UUID

interface ImplementMe {

    /**
     * Represents the active schemes that should be run on any `apply` call.  The schemes will not change
     * between runs and should be considered immutable
     */
    var schemes: List<Scheme>

    /**
     * Apply the receipt to all active schemes for the merchant, response should include one `ApplyResponse`
     * instance for each scheme belonging to the merchant - even if no stamps or payments where awarded for that scheme
     */
    fun apply(receipt: Receipt): List<ApplyResponse>

    /**
     * Retrieve and return the current state for an account for all the active schemes.  If they have never used a
     * scheme before then a zero state should be returned (ie 0 stamps given, 0 payments, current stamp as 0.
     *
     * Should return one `StateResponse` instance for each scheme
     */
    fun state(accountId: AccountId): List<StateResponse>
}
