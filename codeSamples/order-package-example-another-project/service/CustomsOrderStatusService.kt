package service

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderStatus
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.repository.CustomsOrderStatusRepository
import org.springframework.stereotype.Service

@Service
class CustomsOrderStatusService(
    private val customsOrderStatusRepository: CustomsOrderStatusRepository
) {
    fun create(order: CustomsOrder, status: OrderStatus): CustomsOrderStatus {
        return customsOrderStatusRepository.save(
            CustomsOrderStatus(
                customsOrder = order, status = status
            )
        )
    }
}
