package converter

import de.rhenus.scs.customs.order.model.OrderStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
object OrderStatusConverter : AttributeConverter<OrderStatus, Long> {
    override fun convertToDatabaseColumn(orderStatus: OrderStatus?) = orderStatus?.value

    override fun convertToEntityAttribute(value: Long?) = value?.let {
        OrderStatus.values().singleOrNull { it.value == value }
    }
}
