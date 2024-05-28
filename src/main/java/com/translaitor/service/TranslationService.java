package com.translaitor.service;

import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;

    public List<Translation> findAll(User user) {
        return translationRepository.findByUser(user);
    }


}
