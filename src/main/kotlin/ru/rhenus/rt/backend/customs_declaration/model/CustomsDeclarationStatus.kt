package ru.rhenus.rt.backend.customs_declaration.model

@Suppress("unused")
enum class CustomsDeclarationStatus {
    NOT_OPEN,           // НеПодана
    OPEN,               // Подана
    CLEARED,            // Выпущена
    CLEARED_PARTIALLY,  // ВыпущенаЧастично
    INSPECTION,         // Досмотр
    RECALL,             // Отзыв
    REFUSAL,            // Отказ
    PTD,                // ПТД
    DECISION_VARIES,    // РешениеПоТоварамРазлично

}
