package converter

import de.rhenus.scs.customs.order.model.SegmentStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
object OrderSegmentStatusConverter : AttributeConverter<SegmentStatus, Long> {
    override fun convertToDatabaseColumn(orderStatus: SegmentStatus?) = orderStatus?.code

    override fun convertToEntityAttribute(value: Long?) = value?.let {
        SegmentStatus.values().singleOrNull { it.code == value }
    }
}
