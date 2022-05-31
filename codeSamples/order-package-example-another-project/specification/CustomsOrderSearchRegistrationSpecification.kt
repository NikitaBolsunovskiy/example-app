package specification

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrder_
import de.rhenus.scs.customs.orderregistration.model.CustomsOrderRegistrationSearchDto
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.*


//TODO unused but possibly usable in future?
object CustomsOrderSearchRegistrationSpecification {

    @JvmStatic
    fun build(
        searchDto: CustomsOrderRegistrationSearchDto
    ): Specification<CustomsOrder> {
        return Specification { root: Root<CustomsOrder>, criteriaQuery: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = ArrayList<Predicate>()

            predicates.add(cb.equal(root.get<Long>(CustomsOrder_.CUSTOMS_ORDER_STATUS), 99))
//            predicates.add(cb.equal(root.get<Long>(CustomsOrder_.CUSTOMER_ID_CCT), searchDto.getCustomerIdCct()))

            if (!searchDto.customsOrderNo.isNullOrBlank()) {
                predicates.add(buildLikeIgnoreCase(root.get(CustomsOrder_.CUSTOMS_ORDER_NO), searchDto.customsOrderNo, cb))
            }

            searchDto.customsOrderDateFrom?.let { cb.greaterThanOrEqualTo(root.get(CustomsOrder_.CUSTOMS_ORDER_DATETIME), it) }?.let { predicates.add(it) }

            searchDto.customsOrderDateTo?.let { cb.lessThanOrEqualTo(root.get(CustomsOrder_.CUSTOMS_ORDER_DATETIME), it) }?.let { predicates.add(it) }


            searchDto.countryOfExport?.let { cb.equal(root.get<Long>(CustomsOrder_.COUNTRY_OF_EXPORT_SAD15), it) }?.let { predicates.add(it) }


            searchDto.countryOfDestination?.let { cb.equal(root.get<Long>(CustomsOrder_.COUNTRY_OF_DESTINATION_SAD17), it) }?.let { predicates.add(it) }

            searchDto.deliveryTerm
                ?.let { buildLikeIgnoreCase(root.get(CustomsOrder_.DELIVERY_TERM_SAD20), searchDto.deliveryTerm, cb) }
                ?.let { predicates.add(it) }

            if (!searchDto.customerReference.isNullOrBlank()) {
                predicates.add(buildLikeIgnoreCase(root.get(CustomsOrder_.CUSTOMER_REFERENCE), searchDto.customerReference, cb))
            }

            if (!searchDto.text.isNullOrBlank()) {
                predicates.add(cb.or(*listOf(
                    buildLikeIgnoreCase(root.get(CustomsOrder_.CUSTOMS_ORDER_NO), searchDto.text, cb),
                    buildLikeIgnoreCase(root.get(CustomsOrder_.CUSTOMER_REFERENCE), searchDto.text, cb),
                    buildLikeIgnoreCase(root.get(CustomsOrder_.DELIVERY_TERM_SAD20), searchDto.text, cb),
                ).toTypedArray()))
            }

            cb.and(*predicates.toTypedArray())
        }
    }

    private fun buildLikeIgnoreCase(path: Path<String>, value: String?, criteriaBuilder: CriteriaBuilder): Predicate {
        return criteriaBuilder.like(criteriaBuilder.upper(path), "%" + value!!.uppercase(Locale.getDefault()) + "%")
    }
}
