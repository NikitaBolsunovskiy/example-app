package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "customs_order_position")
@Audited(withModifiedFlag = true)
data class CustomsOrderPosition(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "sequential_no_sad32")
    var sequentialNoSad32: Int? = null,

    @Column(name = "customer_reference", length = 35)
    var customerReference: String? = null,

    @Column(name = "unit_id")
    var unitId: Long? = null,

    @Column(name = "customer_material", length = 30)
    var customerMaterial: String? = null,

    @Column(name = "material_batch_no", length = 35)
    var materialBatchNo: String? = null,

    @Column(name = "customer_hs_code_sad33ex", length = 11)
    var customerHsCodeSad33ex: String? = null,

    @Column(name = "no_of_packages_sad31")
    var noOfPackagesSad31: Int? = null,

    @Column(name = "no_pieces")
    var noPieces: Int? = null,

    @Column(name = "gross_mass_sad35", precision = 19, scale = 3)
    var grossMassSad35: BigDecimal? = null,

    @Column(name = "gross_mass_unit_sad35", length = 10)
    var grossMassUnitSad35: String? = null,

    @Column(name = "net_mass_sad38", precision = 19, scale = 3)
    var netMassSad38: BigDecimal? = null,

    @Column(name = "net_mass_unit_sad38", length = 10)
    var netMassUnitSad38: String? = null,

    @Column(name = "item_price_sad42", precision = 19, scale = 2)
    var itemPriceSad42: BigDecimal? = null,

    @Column(name = "valuation_method_code_sad43", length = 1)
    var valuationMethodCodeSad43: String? = null,

    @Column(name = "goods_value_currency", length = 3)
    var goodsValueCurrency: String? = null,

    @Column(name = "goods_description_sad31", length = 512)
    var goodsDescriptionSad31: String? = null,

    @Column(name = "goods_desciptions_as_per_tariff_sad31", length = 512)
    var goodsDesciptionsAsPerTariffSad31: String? = null,

    @Column(name = "main_packaging_unit_source_sad31", length = 512)
    var mainPackagingUnitSourceSad31: String? = null,

    @Column(name = "container_no_sad31", length = 17)
    var containerNoSad31: String? = null,

    @Column(name = "seal_no_sads28", length = 8)
    var sealNoSads28: String? = null,

    @Column(name = "country_of_origin_sad34", length = 3)
    var countryOfOriginSad34: String? = null,

    @Column(name = "region_code_of_origin_sad34", length = 15)
    var regionCodeOfOriginSad34: String? = null,

    @Column(name = "invoice_no", length = 35)
    var invoiceNo: String? = null,

    @Column(name = "customs_procedure_sad37", length = 4)
    var customsProcedureSad37: String? = null,

    @Column(name = "customs_procedure_additional_sad37", length = 3)
    var customsProcedureAdditionalSad37: String? = null,

    @Column(name = "quota_sad39", length = 6)
    var quotaSad39: String? = null,

    @Column(name = "preference_sad36", length = 3)
    var preferenceSad36: String? = null,

    @Column(name = "supplementary_units_sad41", length = 35)
    var supplementaryUnitsSad41: String? = null,

    @Column(name = "no_supplementary_units_sad41", precision = 19, scale = 3)
    var noSupplementaryUnitsSad41: BigDecimal? = null,

    @Column(name = "adjustment_sad45", length = 35)
    var adjustmentSad45: String? = null,

    @Column(name = "statistical_value_sad46", precision = 19, scale = 2)
    var statisticalValueSad46: BigDecimal? = null,

    @Column(name = "customs_status", length = 35)
    var customsStatus: String? = null,

    @Column(name = "excise_goods_product_code", length = 4)
    var exciseGoodsProductCode: String? = null,

    @Column(name = "excise_goods_special_measures_alcoholic_strength", precision = 19, scale = 3)
    var exciseGoodsSpecialMeasuresAlcoholicStrength: BigDecimal? = null,

    @Column(name = "excise_goods_special_measures_degree_plato", precision = 19, scale = 3)
    var exciseGoodsSpecialMeasuresDegreePlato: BigDecimal? = null,

    @Column(name = "excise_goods_special_measures_density", precision = 19, scale = 3)
    var exciseGoodsSpecialMeasuresDensity: BigDecimal? = null,

    @Column(name = "excise_goods_description", length = 512)
    var exciseGoodsDescription: String? = null,

    @Column(name = "excise_goods_designation_of_origin", length = 512)
    var exciseGoodsDesignationOfOrigin: String? = null,

    @Column(name = "excise_goods_brand_name", length = 512)
    var exciseGoodsBrandName: String? = null,

    @Column(name = "excise_goods_vat_number", length = 512)
    var exciseGoodsVatNumber: String? = null,

    @Column(name = "excise_goods_add_code1", length = 4)
    var exciseGoodsAddCode1: String? = null,

    @Column(name = "excise_goods_add_code2", length = 4)
    var exciseGoodsAddCode2: String? = null,

    @Column(name = "delivery_note_pos", length = 35)
    var deliveryNotePos: String? = null,

    @Column(name = "warehouse_status", length = 35)
    var warehouseStatus: String? = null,

    @Column(name = "invoice_pos", length = 35)
    var invoicePos: String? = null,

    @Column(name = "foc_code")
    var focCode: Char? = null,

    @Column(name = "item_no", length = 35)
    var itemNo: String? = null,

    @Column(name = "order_pos", length = 35)
    var orderPos: String? = null,

    @Column(name = "net_item_price", precision = 19, scale = 2)
    var netItemPrice: BigDecimal? = null,

    @Column(name = "customer_hs_code_sad33im", length = 11)
    var customerHsCodeSad33im: String? = null,

    @Column(name = "customer_hs_code_sad33t", length = 11)
    var customerHsCodeSad33t: String? = null,

    @Column(name = "customer_hs_code_supplemental_sad33ex", length = 4)
    var customerHsCodeSupplementalSad33ex: String? = null,

    @Column(name = "customer_hs_code_supplemental_sad33t", length = 4)
    var customerHsCodeSupplementalSad33t: String? = null,

    @Column(name = "customer_hs_code_supplemental_sad33im", length = 4)
    var customerHsCodeSupplementalSad33im: String? = null,

    @Column(name = "commissions", precision = 19, scale = 2)
    var commissions: BigDecimal? = null,

    @Column(name = "brokerage", precision = 19, scale = 2)
    var brokerage: BigDecimal? = null,

    @Column(name = "container_packing", precision = 19, scale = 2)
    var containerPacking: BigDecimal? = null,

    @Column(name = "materials_components_parts", precision = 19, scale = 2)
    var materialsComponentsParts: BigDecimal? = null,

    @Column(name = "tools_dies_moulds", precision = 19, scale = 2)
    var toolsDiesMoulds: BigDecimal? = null,

    @Column(name = "consumed_in_production", precision = 19, scale = 2)
    var consumedInProduction: BigDecimal? = null,

    @Column(name = "engineering_design", precision = 19, scale = 2)
    var engineeringDesign: BigDecimal? = null,

    @Column(name = "royalties_license", precision = 19, scale = 2)
    var royaltiesLicense: BigDecimal? = null,

    @Column(name = "proceeds", precision = 19, scale = 2)
    var proceeds: BigDecimal? = null,

    @Column(name = "costs_of_delivery_to", length = 70)
    var costsOfDeliveryTo: String? = null,

    @Column(name = "transport", precision = 19, scale = 2)
    var transport: BigDecimal? = null,

    @Column(name = "handling", precision = 19, scale = 2)
    var handling: BigDecimal? = null,

    @Column(name = "insurance", precision = 19, scale = 2)
    var insurance: BigDecimal? = null,

    @Column(name = "inland_transport", precision = 19, scale = 2)
    var inlandTransport: BigDecimal? = null,

    @Column(name = "construction_charges", precision = 19, scale = 2)
    var constructionCharges: BigDecimal? = null,

    @Column(name = "duties_and_taxes", precision = 19, scale = 2)
    var dutiesAndTaxes: BigDecimal? = null,

    @Column(name = "other_charges_type", length = 70)
    var otherChargesType: String? = null,

    @Column(name = "other_charges", precision = 19, scale = 2)
    var otherCharges: BigDecimal? = null,

    @OneToMany(mappedBy = "customsOrderPosition", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderPositionTaxes: MutableList<CustomsOrderPositionTax> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrderPosition", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderPositionAdditionalInformations: MutableList<CustomsOrderPositionAdditionalInformation> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrderPosition", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderPositionPreviousDocuments: MutableList<CustomsOrderPositionPreviousDocument> = mutableListOf(),
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderPosition) return false

        if (id != other.id) return false
        if (sequentialNoSad32 != other.sequentialNoSad32) return false
        if (customerReference != other.customerReference) return false
        if (unitId != other.unitId) return false
        if (customerMaterial != other.customerMaterial) return false
        if (materialBatchNo != other.materialBatchNo) return false
        if (customerHsCodeSad33ex != other.customerHsCodeSad33ex) return false
        if (noOfPackagesSad31 != other.noOfPackagesSad31) return false
        if (noPieces != other.noPieces) return false
        if (grossMassSad35 != other.grossMassSad35) return false
        if (grossMassUnitSad35 != other.grossMassUnitSad35) return false
        if (netMassSad38 != other.netMassSad38) return false
        if (netMassUnitSad38 != other.netMassUnitSad38) return false
        if (itemPriceSad42 != other.itemPriceSad42) return false
        if (valuationMethodCodeSad43 != other.valuationMethodCodeSad43) return false
        if (goodsValueCurrency != other.goodsValueCurrency) return false
        if (goodsDescriptionSad31 != other.goodsDescriptionSad31) return false
        if (goodsDesciptionsAsPerTariffSad31 != other.goodsDesciptionsAsPerTariffSad31) return false
        if (mainPackagingUnitSourceSad31 != other.mainPackagingUnitSourceSad31) return false
        if (containerNoSad31 != other.containerNoSad31) return false
        if (sealNoSads28 != other.sealNoSads28) return false
        if (countryOfOriginSad34 != other.countryOfOriginSad34) return false
        if (regionCodeOfOriginSad34 != other.regionCodeOfOriginSad34) return false
        if (invoiceNo != other.invoiceNo) return false
        if (customsProcedureSad37 != other.customsProcedureSad37) return false
        if (customsProcedureAdditionalSad37 != other.customsProcedureAdditionalSad37) return false
        if (quotaSad39 != other.quotaSad39) return false
        if (preferenceSad36 != other.preferenceSad36) return false
        if (supplementaryUnitsSad41 != other.supplementaryUnitsSad41) return false
        if (noSupplementaryUnitsSad41 != other.noSupplementaryUnitsSad41) return false
        if (adjustmentSad45 != other.adjustmentSad45) return false
        if (statisticalValueSad46 != other.statisticalValueSad46) return false
        if (customsStatus != other.customsStatus) return false
        if (exciseGoodsProductCode != other.exciseGoodsProductCode) return false
        if (exciseGoodsSpecialMeasuresAlcoholicStrength != other.exciseGoodsSpecialMeasuresAlcoholicStrength) return false
        if (exciseGoodsSpecialMeasuresDegreePlato != other.exciseGoodsSpecialMeasuresDegreePlato) return false
        if (exciseGoodsSpecialMeasuresDensity != other.exciseGoodsSpecialMeasuresDensity) return false
        if (exciseGoodsDescription != other.exciseGoodsDescription) return false
        if (exciseGoodsDesignationOfOrigin != other.exciseGoodsDesignationOfOrigin) return false
        if (exciseGoodsBrandName != other.exciseGoodsBrandName) return false
        if (exciseGoodsVatNumber != other.exciseGoodsVatNumber) return false
        if (exciseGoodsAddCode1 != other.exciseGoodsAddCode1) return false
        if (exciseGoodsAddCode2 != other.exciseGoodsAddCode2) return false
        if (deliveryNotePos != other.deliveryNotePos) return false
        if (warehouseStatus != other.warehouseStatus) return false
        if (invoicePos != other.invoicePos) return false
        if (focCode != other.focCode) return false
        if (itemNo != other.itemNo) return false
        if (orderPos != other.orderPos) return false
        if (netItemPrice != other.netItemPrice) return false
        if (customerHsCodeSad33im != other.customerHsCodeSad33im) return false
        if (customerHsCodeSad33t != other.customerHsCodeSad33t) return false
        if (customerHsCodeSupplementalSad33ex != other.customerHsCodeSupplementalSad33ex) return false
        if (customerHsCodeSupplementalSad33t != other.customerHsCodeSupplementalSad33t) return false
        if (customerHsCodeSupplementalSad33im != other.customerHsCodeSupplementalSad33im) return false
        if (commissions != other.commissions) return false
        if (brokerage != other.brokerage) return false
        if (containerPacking != other.containerPacking) return false
        if (materialsComponentsParts != other.materialsComponentsParts) return false
        if (toolsDiesMoulds != other.toolsDiesMoulds) return false
        if (consumedInProduction != other.consumedInProduction) return false
        if (engineeringDesign != other.engineeringDesign) return false
        if (royaltiesLicense != other.royaltiesLicense) return false
        if (proceeds != other.proceeds) return false
        if (costsOfDeliveryTo != other.costsOfDeliveryTo) return false
        if (transport != other.transport) return false
        if (handling != other.handling) return false
        if (insurance != other.insurance) return false
        if (inlandTransport != other.inlandTransport) return false
        if (constructionCharges != other.constructionCharges) return false
        if (dutiesAndTaxes != other.dutiesAndTaxes) return false
        if (otherChargesType != other.otherChargesType) return false
        if (otherCharges != other.otherCharges) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (sequentialNoSad32 ?: 0)
        result = 31 * result + (customerReference?.hashCode() ?: 0)
        result = 31 * result + (unitId?.hashCode() ?: 0)
        result = 31 * result + (customerMaterial?.hashCode() ?: 0)
        result = 31 * result + (materialBatchNo?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSad33ex?.hashCode() ?: 0)
        result = 31 * result + (noOfPackagesSad31 ?: 0)
        result = 31 * result + (noPieces ?: 0)
        result = 31 * result + (grossMassSad35?.hashCode() ?: 0)
        result = 31 * result + (grossMassUnitSad35?.hashCode() ?: 0)
        result = 31 * result + (netMassSad38?.hashCode() ?: 0)
        result = 31 * result + (netMassUnitSad38?.hashCode() ?: 0)
        result = 31 * result + (itemPriceSad42?.hashCode() ?: 0)
        result = 31 * result + (valuationMethodCodeSad43?.hashCode() ?: 0)
        result = 31 * result + (goodsValueCurrency?.hashCode() ?: 0)
        result = 31 * result + (goodsDescriptionSad31?.hashCode() ?: 0)
        result = 31 * result + (goodsDesciptionsAsPerTariffSad31?.hashCode() ?: 0)
        result = 31 * result + (mainPackagingUnitSourceSad31?.hashCode() ?: 0)
        result = 31 * result + (containerNoSad31?.hashCode() ?: 0)
        result = 31 * result + (sealNoSads28?.hashCode() ?: 0)
        result = 31 * result + (countryOfOriginSad34?.hashCode() ?: 0)
        result = 31 * result + (regionCodeOfOriginSad34?.hashCode() ?: 0)
        result = 31 * result + (invoiceNo?.hashCode() ?: 0)
        result = 31 * result + (customsProcedureSad37?.hashCode() ?: 0)
        result = 31 * result + (customsProcedureAdditionalSad37?.hashCode() ?: 0)
        result = 31 * result + (quotaSad39?.hashCode() ?: 0)
        result = 31 * result + (preferenceSad36?.hashCode() ?: 0)
        result = 31 * result + (supplementaryUnitsSad41?.hashCode() ?: 0)
        result = 31 * result + (noSupplementaryUnitsSad41?.hashCode() ?: 0)
        result = 31 * result + (adjustmentSad45?.hashCode() ?: 0)
        result = 31 * result + (statisticalValueSad46?.hashCode() ?: 0)
        result = 31 * result + (customsStatus?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsProductCode?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsSpecialMeasuresAlcoholicStrength?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsSpecialMeasuresDegreePlato?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsSpecialMeasuresDensity?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsDescription?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsDesignationOfOrigin?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsBrandName?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsVatNumber?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsAddCode1?.hashCode() ?: 0)
        result = 31 * result + (exciseGoodsAddCode2?.hashCode() ?: 0)
        result = 31 * result + (deliveryNotePos?.hashCode() ?: 0)
        result = 31 * result + (warehouseStatus?.hashCode() ?: 0)
        result = 31 * result + (invoicePos?.hashCode() ?: 0)
        result = 31 * result + (focCode?.hashCode() ?: 0)
        result = 31 * result + (itemNo?.hashCode() ?: 0)
        result = 31 * result + (orderPos?.hashCode() ?: 0)
        result = 31 * result + (netItemPrice?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSad33im?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSad33t?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSupplementalSad33ex?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSupplementalSad33t?.hashCode() ?: 0)
        result = 31 * result + (customerHsCodeSupplementalSad33im?.hashCode() ?: 0)
        result = 31 * result + (commissions?.hashCode() ?: 0)
        result = 31 * result + (brokerage?.hashCode() ?: 0)
        result = 31 * result + (containerPacking?.hashCode() ?: 0)
        result = 31 * result + (materialsComponentsParts?.hashCode() ?: 0)
        result = 31 * result + (toolsDiesMoulds?.hashCode() ?: 0)
        result = 31 * result + (consumedInProduction?.hashCode() ?: 0)
        result = 31 * result + (engineeringDesign?.hashCode() ?: 0)
        result = 31 * result + (royaltiesLicense?.hashCode() ?: 0)
        result = 31 * result + (proceeds?.hashCode() ?: 0)
        result = 31 * result + (costsOfDeliveryTo?.hashCode() ?: 0)
        result = 31 * result + (transport?.hashCode() ?: 0)
        result = 31 * result + (handling?.hashCode() ?: 0)
        result = 31 * result + (insurance?.hashCode() ?: 0)
        result = 31 * result + (inlandTransport?.hashCode() ?: 0)
        result = 31 * result + (constructionCharges?.hashCode() ?: 0)
        result = 31 * result + (dutiesAndTaxes?.hashCode() ?: 0)
        result = 31 * result + (otherChargesType?.hashCode() ?: 0)
        result = 31 * result + (otherCharges?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderPosition(sequentialNoSad32=$sequentialNoSad32, customerReference=$customerReference, unitId=$unitId, customerMaterial=$customerMaterial, materialBatchNo=$materialBatchNo, customerHsCodeSad33ex=$customerHsCodeSad33ex, noOfPackagesSad31=$noOfPackagesSad31, noPieces=$noPieces, grossMassSad35=$grossMassSad35, grossMassUnitSad35=$grossMassUnitSad35, netMassSad38=$netMassSad38, netMassUnitSad38=$netMassUnitSad38, itemPriceSad42=$itemPriceSad42, valuationMethodCodeSad43=$valuationMethodCodeSad43, goodsValueCurrency=$goodsValueCurrency, goodsDescriptionSad31=$goodsDescriptionSad31, goodsDesciptionsAsPerTariffSad31=$goodsDesciptionsAsPerTariffSad31, mainPackagingUnitSourceSad31=$mainPackagingUnitSourceSad31, containerNoSad31=$containerNoSad31, sealNoSads28=$sealNoSads28, countryOfOriginSad34=$countryOfOriginSad34, regionCodeOfOriginSad34=$regionCodeOfOriginSad34, invoiceNo=$invoiceNo, customsProcedureSad37=$customsProcedureSad37, customsProcedureAdditionalSad37=$customsProcedureAdditionalSad37, quotaSad39=$quotaSad39, preferenceSad36=$preferenceSad36, supplementaryUnitsSad41=$supplementaryUnitsSad41, noSupplementaryUnitsSad41=$noSupplementaryUnitsSad41, adjustmentSad45=$adjustmentSad45, statisticalValueSad46=$statisticalValueSad46, customsStatus=$customsStatus, exciseGoodsProductCode=$exciseGoodsProductCode, exciseGoodsSpecialMeasuresAlcoholicStrength=$exciseGoodsSpecialMeasuresAlcoholicStrength, exciseGoodsSpecialMeasuresDegreePlato=$exciseGoodsSpecialMeasuresDegreePlato, exciseGoodsSpecialMeasuresDensity=$exciseGoodsSpecialMeasuresDensity, exciseGoodsDescription=$exciseGoodsDescription, exciseGoodsDesignationOfOrigin=$exciseGoodsDesignationOfOrigin, exciseGoodsBrandName=$exciseGoodsBrandName, exciseGoodsVatNumber=$exciseGoodsVatNumber, exciseGoodsAddCode1=$exciseGoodsAddCode1, exciseGoodsAddCode2=$exciseGoodsAddCode2, deliveryNotePos=$deliveryNotePos, warehouseStatus=$warehouseStatus, invoicePos=$invoicePos, focCode=$focCode, itemNo=$itemNo, orderPos=$orderPos, netItemPrice=$netItemPrice, customerHsCodeSad33im=$customerHsCodeSad33im, customerHsCodeSad33t=$customerHsCodeSad33t, customerHsCodeSupplementalSad33ex=$customerHsCodeSupplementalSad33ex, customerHsCodeSupplementalSad33t=$customerHsCodeSupplementalSad33t, customerHsCodeSupplementalSad33im=$customerHsCodeSupplementalSad33im, commissions=$commissions, brokerage=$brokerage, containerPacking=$containerPacking, materialsComponentsParts=$materialsComponentsParts, toolsDiesMoulds=$toolsDiesMoulds, consumedInProduction=$consumedInProduction, engineeringDesign=$engineeringDesign, royaltiesLicense=$royaltiesLicense, proceeds=$proceeds, costsOfDeliveryTo=$costsOfDeliveryTo, transport=$transport, handling=$handling, insurance=$insurance, inlandTransport=$inlandTransport, constructionCharges=$constructionCharges, dutiesAndTaxes=$dutiesAndTaxes, otherChargesType=$otherChargesType, otherCharges=$otherCharges)"
}
