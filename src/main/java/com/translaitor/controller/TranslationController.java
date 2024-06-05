package com.translaitor.controller;

import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public Translation getById(@PathVariable Long id) {
        return translationService.findById(id);
    }

    @GetMapping("/translations/user")
    public List<Translation> getAllTranslationsByUser(@AuthenticationPrincipal User user) {
        return translationService.findByUser(user);
    }

    @PostMapping("/translations")
    public ResponseEntity<Translation> createTranslation(@RequestBody Translation translation) {
        Translation createdTranslation = translationService.save(translation);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTranslation.getId()).toUri();

        return ResponseEntity
                .created(createdURI)
                .body(createdTranslation);
    }

    @PutMapping("/translations/{id}")
    public Translation updateTranslation(@PathVariable Long id, @RequestBody Translation updatedTranslation) {
        return translationService.updateTranslation(id, updatedTranslation);
    }

    @DeleteMapping("/translations/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        translationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
