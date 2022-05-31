package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderPositionDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderId: Long? = null,
    var sequentialNoSad32: Long? = null,
    var customerReference: String? = null,
    var unitId: Long? = null,
    var customerMaterial: String? = null,
    var materialBatchNo: String? = null,
    var customerHsCodeSad33ex: String? = null,
    var customerHsCodeSupplementalSad33ex: String? = null,
    var customerHsCodeSad33t: String? = null,
    var customerHsCodeSupplementalSad33t: String? = null,
    var customerHsCodeSad33im: String? = null,
    var customerHsCodeSupplementalSad33im: String? = null,
    var noOfPackagesSad31: Long? = null,
    var noPieces: Long? = null,
    var grossMassSad35: Float? = null,
    var grossMassUnitSad35: String? = null,
    var netMassSad38: Float? = null,
    var netMassUnitSad38: String? = null,
    var itemPriceSad42: Float? = null,
    var valuationMethodCodeSad43: String? = null,
    var goodsValueCurrency: String? = null,
    var goodsDescriptionSad31: String? = null,
    var goodsDesciptionsAsPerTariffSad31: String? = null,
    var mainPackagingUnitSourceSad31: String? = null,
    var containerNoSad31: String? = null,
    var sealNoSads28: String? = null,
    var countryOfOriginSad34: String? = null,
    var regionCodeOfOriginSad34: String? = null,
    var invoiceNo: String? = null,
    var invoicePos: String? = null,
    var customsProcedureSad37: String? = null,
    var customsProcedureAdditionalSad37: String? = null,
    var quotaSad39: String? = null,
    var preferenceSad36: String? = null,
    var supplementaryUnitsSad41: String? = null,
    var noSupplementaryUnitsSad41: Float? = null,
    var adjustmentSad45: String? = null,
    var statisticalValueSad46: Float? = null,
    var customsStatus: String? = null,
    var exciseGoodsProductCode: String? = null,
    var exciseGoodsSpecialMeasuresAlcoholicStrength: Float? = null,
    var exciseGoodsSpecialMeasuresDegreePlato: Float? = null,
    var exciseGoodsSpecialMeasuresDensity: Float? = null,
    var exciseGoodsDescription: String? = null,
    var exciseGoodsDesignationOfOrigin: String? = null,
    var exciseGoodsBrandName: String? = null,
    var exciseGoodsVatNumber: String? = null,
    var exciseGoodsAddCode1: String? = null,
    var exciseGoodsAddCode2: String? = null,
    var deliveryNotePos: String? = null,
    var warehouseStatus: String? = null,
    var itemNo: String? = null,
    var orderPos: String? = null,
    var netItemPrice: Float? = null,
    var focCode: String? = null,

    /**
     * Cost Additions.
     */
    var commissions: Float? = null,
    var brokerage: Float? = null,
    var containerPacking: Float? = null,
    var materialsComponentsParts: Float? = null,
    var toolsDiesMoulds: Float? = null,
    var consumedInProduction: Float? = null,
    var engineeringDesign: Float? = null,
    var royaltiesLicense: Float? = null,
    var proceeds: Float? = null,
    var costsOfDeliveryTo: String? = null,
    var transport: Float? = null,
    var handling: Float? = null,
    var insurance: Float? = null,

    /**
     * Cost Deductions.
     */
    var inlandTransport: Float? = null,
    var constructionCharges: Float? = null,
    var dutiesAndTaxes: Float? = null,
    var otherChargesType: String? = null,
    var otherCharges: Float? = null,
    var additionalInformations: MutableList<CustomsOrderPositionAdditionalInformationDto> = mutableListOf(),
    var taxes: MutableList<CustomsOrderPositionTaxDto> = mutableListOf(),
    var previousDocuments: MutableList<CustomsOrderPositionPreviousDocumentDto> = mutableListOf(),
) : BaseDto(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderId
}
