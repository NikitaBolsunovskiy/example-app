package service

import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDate

object CustomsOrderNoGenerator {
    @JvmStatic
    fun generateOrderNo(tenantCode: String?): String {
        var reference: String? = LocalDate.now().toString()
        reference = reference!!.replace("-", "")
        reference = reference.substring(2)
        reference += tenantCode
        reference += RandomStringUtils.random(7, false, true)
        return reference
    }
}
