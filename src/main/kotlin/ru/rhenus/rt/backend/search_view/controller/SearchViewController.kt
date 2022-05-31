package ru.rhenus.rt.backend.search_view.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.search_view.model.SearchViewModel
import ru.rhenus.rt.backend.search_view.service.SearchViewService

@RestController
@RequestMapping("/search-view")
class SearchViewController(
    private val searchViewService: SearchViewService,
) {

    @GetMapping
    fun search(pageRequest: Pageable): ResponseEntity<Page<SearchViewModel>> {
        return ResponseEntity(searchViewService.search(pageRequest), HttpStatus.OK)
    }

}