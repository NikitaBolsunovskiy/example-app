package controller

import de.rhenus.scs.customs.common.stats.MethodStats
import de.rhenus.scs.customs.order.mapper.CustomsOrderMapper
import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.order.service.CustomsOrderCreateService
import de.rhenus.scs.customs.order.service.CustomsOrderProcessService
import de.rhenus.scs.customs.order.service.CustomsOrderSearchService
import de.rhenus.scs.customs.order.service.CustomsOrderService
import de.rhenus.scs.customs.security.permissions.*
import de.rhenus.scs.customs.security.service.AuthHelperService
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ValidationException

@RestController
@RequestMapping("/api/v1/customs-order")
class CustomsOrderController(
    private val authHelperService: AuthHelperService,
    private val customsOrderService: CustomsOrderService,
    private val customsOrderSearchService: CustomsOrderSearchService,
    private val customsOrderProcessService: CustomsOrderProcessService,
    private val customsOrderMapper: CustomsOrderMapper,
    private val customsOrderCreateService: CustomsOrderCreateService,
) {
    private val log = KotlinLogging.logger {}

    @MethodStats
    @CustomsOrderCreatePermission
    @PostMapping("/create")
    fun create(@RequestBody createRequest: CustomsOrderCreateRequest): ResponseEntity<CustomsOrderDto> {
        val customsOrder = customsOrderCreateService.create(createRequest)
        return ResponseEntity.ok(customsOrderMapper.toDto(customsOrder!!))
    }

    @MethodStats
    @PostMapping
    fun search(
        @RequestBody search: CustomsOrderSearchData,
        pageRequest: Pageable
    ): ResponseEntity<Page<CustomsOrderDto?>> {
        log.debug("Received search request")
        if (OrderSearchRole.ROLE_LEAD.name == search.role && !authHelperService.isLead) {
            log.info("Denying search request because user doesn't have role CCT_LEAD")
            throw ValidationException("You are not allowed to control Customs Orders for any office!")
        }
        if (OrderSearchRole.ROLE_DECLARANT.name == search.role && !authHelperService.isDeclarant) {
            log.info("Denying search request because user doesn't have role CCT_DECLARANT")
            throw ValidationException("You are not allowed to manage Customs Orders for any office!")
        }
        if (OrderSearchRole.ROLE_BOTH.name == search.role && !authHelperService.isDeclarant && !authHelperService.isLead) {
            log.info("Denying search request because user has neither role CCT_LEAD nor CCT_DECLARANT")
            throw ValidationException("You are not allowed to control or manage Customs Orders for any office!")
        }
        log.debug("Starting customs order search")
        val newPageRequest = PageRequest.of(pageRequest.pageNumber, pageRequest.pageSize)
        val page = customsOrderSearchService.search(
            search,
            authHelperService.rulesForLead ?: listOf(),
            authHelperService.rulesForDeclarant ?: listOf(),
            newPageRequest
        )
        return ResponseEntity.ok(page.map {
            customsOrderMapper.toMinimalDto( it )
        })
    }

    @MethodStats
    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long?): ResponseEntity<CustomsOrderDto> =
        ResponseEntity.ok(customsOrderMapper.toDto(customsOrderService.get(id)))

    @CustomsOrderUpdatePermission
    @PostMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody orderDto: CustomsOrderDto
    ): ResponseEntity<CustomsOrderDto> =
        ResponseEntity.ok(customsOrderMapper.toDto(customsOrderService.update(customsOrderMapper.toEntity(orderDto))!!))

    @CustomsOrderUpdatePermission
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long, @RequestParam statusId: Long): ResponseEntity<Void> {
        customsOrderProcessService.changeStatus(id, statusId)
        return ResponseEntity.ok().build()
    }

    @CustomsOrderDeletePermission
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        customsOrderService.delete(id)
        return ResponseEntity.ok().build()
    }

    @CustomsOrderCancelPermission
    @GetMapping("/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<Void> {
        customsOrderProcessService.cancel(id)
        return ResponseEntity.ok().build()
    }

    @CustomsOrderReOpenPermission
    @GetMapping("/{id}/reopen")
    fun reopen(@PathVariable id: Long): ResponseEntity<Void> {
        customsOrderProcessService.reopen(id)
        return ResponseEntity.ok().build()
    }
}
