package converter

import de.rhenus.scs.customs.order.model.CustomsOrderSegmentReferenceType
import de.rhenus.scs.customs.order.model.OrderStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
object SegmentReferenceTypeConverter : AttributeConverter<CustomsOrderSegmentReferenceType, String> {
    override fun convertToDatabaseColumn(referenceType: CustomsOrderSegmentReferenceType?) = referenceType?.value

    override fun convertToEntityAttribute(value: String?) = value?.let {
        CustomsOrderSegmentReferenceType.values().singleOrNull { it.value == value }
    }
}
