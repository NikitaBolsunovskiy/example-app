package converter

import de.rhenus.scs.customs.order.model.SegmentType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
object OrderSegmentTypeConverter : AttributeConverter<SegmentType, String> {
    override fun convertToDatabaseColumn(segmentType: SegmentType?) = segmentType?.value

    override fun convertToEntityAttribute(value: String?) = value?.let {
        SegmentType.values().singleOrNull { it.value == value }
    }
}
