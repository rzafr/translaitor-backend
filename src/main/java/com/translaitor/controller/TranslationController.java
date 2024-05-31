package com.translaitor.controller;

import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://frontend:80",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping("/translations")
    public ResponseEntity<List<Translation>> getAllTranslationsByUser(@AuthenticationPrincipal User user) {
        return buildResponseOfAList(translationService.findAll(user));
    }

    /**
     * Build the response from a List<Translation>
     * @param list
     * @return
     */
    private ResponseEntity<List<Translation>> buildResponseOfAList(List<Translation> list) {
        return list.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(list);
    }

}
