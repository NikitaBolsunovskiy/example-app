package converter

import de.rhenus.scs.customs.order.model.ModeOfTransport
import de.rhenus.scs.customs.order.model.OrderStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
object ModeOfTransportConverter : AttributeConverter<ModeOfTransport, Long> {
    override fun convertToDatabaseColumn(mot: ModeOfTransport?) = mot?.code

    override fun convertToEntityAttribute(value: Long?) = value?.let {
        ModeOfTransport.values().singleOrNull { it.code == value }
    }
}
