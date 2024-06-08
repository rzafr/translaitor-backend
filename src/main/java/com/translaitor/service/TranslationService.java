package com.translaitor.service;

import com.translaitor.exception.EmptyTranslationListException;
import com.translaitor.exception.TranslationNotFoundException;
import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.repository.TranslationRepository;
import com.translaitor.repository.UserRepository;
import com.translaitor.service.dto.translation.CreateTranslationDto;
import com.translaitor.service.dto.translation.UpdateTranslationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final UserRepository userRepository;

    public List<Translation> findAll() {
        List<Translation> result = translationRepository.findAll();
        if (result.isEmpty()) {
            throw new EmptyTranslationListException();
        }
        return result;
    }

    public List<Translation> findByUser(User user) {
        List<Translation> result = translationRepository.findByUser(user);
        if (result.isEmpty()) {
            throw new EmptyTranslationListException();
        }
        return result;
    }

    public Translation findById(Long id) {
        return translationRepository.findById(id)
                .orElseThrow(() -> new TranslationNotFoundException(id));
    }

    @Transactional
    public Translation save(CreateTranslationDto newTranslation) {
        User user = null;
        if (newTranslation.getUser() != null && newTranslation.getUser().getId() != null) {
            user = userRepository.findById(newTranslation.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }
        Translation translation = Translation.builder()
                .sourceLanguage(newTranslation.getSourceLanguage())
                .targetLanguage(newTranslation.getTargetLanguage())
                .originalText(newTranslation.getOriginalText())
                .translatedText(newTranslation.getTranslatedText())
                .favorite(newTranslation.getFavorite())
                .user(user)
                .build();
        try {
            return translationRepository.save(translation);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving translation", ex);
        }

    }

    public Translation updateTranslation(UpdateTranslationDto updatedTranslation) {
        return translationRepository.findById(updatedTranslation.getId())
                .map(translation -> {
                    translation.setFavorite(updatedTranslation.getFavorite());
                    return translationRepository.save(translation);
                })
                .orElseThrow(() -> new TranslationNotFoundException(updatedTranslation.getId()));
    }

    public void delete(Long id) {
        if (translationRepository.existsById(id))
            translationRepository.deleteById(id);
    }

}
