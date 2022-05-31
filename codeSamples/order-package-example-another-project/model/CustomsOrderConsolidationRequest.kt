package model

import java.io.Serializable

class CustomsOrderConsolidationRequest(
    var customerReference: String? = null,
    var strategy: String? = null,
    var customsOrderIdList: List<Long>? = null,
    var masterCustomsOrderId: Long? = null,
) : Serializable {
    companion object {
        /**
         * Clear fields, to be checked later manually.
         */
        const val STRATEGY_CLEAR_FIELDS = "0"

        /**
         * Select Master.
         */
        const val STRATEGY_SELECT_MASTER = "1"

    }
}
