package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderDocumentMapper
import de.rhenus.scs.customs.order.model.CustomsOrderDocumentDto
import de.rhenus.scs.customs.order.service.CustomsOrderDocumentService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/document")
class CustomsOrderDocumentController(
    private val documentService: CustomsOrderDocumentService,
    private val documentMapper: CustomsOrderDocumentMapper,
) {
    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun getDocument(@PathVariable id: Long): ResponseEntity<CustomsOrderDocumentDto> {
        val document = documentService.get(id)
        return ResponseEntity(documentMapper.toDto(document), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#document, 'UPDATE')")
    @PostMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun update(
        @ModelAttribute(binding = false) document: CustomsOrderDocumentDto,
        @RequestPart(required = false) file: MultipartFile?
    ): ResponseEntity<CustomsOrderDocumentDto> {
        val doc = documentMapper.toEntity(document)
        return ResponseEntity(documentMapper.toDto(documentService.update(doc, file)), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#document, 'CREATE')")
    @PostMapping(
        path = ["/"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun create(
        @ModelAttribute(binding = false) document: CustomsOrderDocumentDto,
        @RequestPart(required = false) file: MultipartFile?
    ): ResponseEntity<CustomsOrderDocumentDto> {
        val doc = documentMapper.toEntity(document)
        return ResponseEntity(documentMapper.toDto(documentService.create(doc, file)), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}/file"], produces = [MediaType.ALL_VALUE])
    fun getFile(
        @PathVariable id: Long
    ): ResponseEntity<*> {
        val document = documentService.getDocument(id)
        val reference = document?.archiveReference
        return if(reference !== null) {
            val file = documentService.getFile(reference)
            if(file === null) {
                ResponseEntity("", HttpStatus.NOT_FOUND)
            } else {
                val resource = ByteArrayResource(file)
                ResponseEntity.ok()
                    .contentLength(file.size.toLong())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=${document.documentFilename}")
                    .body(resource)
            }
        } else {
            ResponseEntity("", HttpStatus.NOT_FOUND)
        }
    }
}
