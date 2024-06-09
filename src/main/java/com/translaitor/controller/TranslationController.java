package com.translaitor.controller;

import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.service.TranslationService;
import com.translaitor.service.dto.translation.CreateTranslationDto;
import com.translaitor.service.dto.translation.UpdateTranslationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping("/translations")
    public List<Translation> getAllTranslations() {
        return translationService.findAll();
    }

    @GetMapping("/translations/{id}")
    public Translation getTranslationById(@PathVariable("id") Long id) {
        return translationService.findById(id);
    }

    @GetMapping("/translations/user")
    public List<Translation> getAllTranslationsByUser(@AuthenticationPrincipal User user) {
        return translationService.findByUser(user);
    }

    @GetMapping("/translations/user/paged-sorted")
    public ResponseEntity<Map<String, Object>> findAllTranslationsPagedAndSorted(
            @RequestParam(defaultValue = "createdAt,desc") String[] sort,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {
        Page<Translation> translationsPage = translationService.findAllTranslationsPagedAndSorted(sort, page, size);
        var pageInfo = Map.of(
                "translations", translationsPage.getContent(),
                "currentPage", translationsPage.getNumber(),
                "totalItems", translationsPage.getTotalElements(),
                "totalPages", translationsPage.getTotalPages()
        );
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("/translations/user/favorite")
    public List<Translation> getFavoriteTranslations(@AuthenticationPrincipal User user) {
        return translationService.findByFavoriteTrueAndUserId(user.getId());
    }

    @PostMapping("/translations")
    public ResponseEntity<Translation> createTranslation(@Valid @RequestBody CreateTranslationDto newTranslation) {
        Translation createdTranslation = translationService.save(newTranslation);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTranslation.getId()).toUri();

        return ResponseEntity
                .created(createdURI)
                .body(createdTranslation);
    }

    @PutMapping("/translations")
    public Translation updateTranslation(@Valid @RequestBody UpdateTranslationDto updatedTranslation) {
        return translationService.update(updatedTranslation);
    }

    @DeleteMapping("/translations/{id}")
    public ResponseEntity<?> deleteTranslationById(@PathVariable("id") Long id) {
        translationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
