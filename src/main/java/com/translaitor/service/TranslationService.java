package com.translaitor.service;

import com.translaitor.exception.EmptyTranslationListException;
import com.translaitor.exception.TranslationNotFoundException;
import com.translaitor.model.Translation;
import com.translaitor.model.User;
import com.translaitor.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;

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

    public Translation save(Translation translation) { return translationRepository.save(translation); }

    public Translation updateTranslation(Long id, Translation updatedTranslation) {
        return translationRepository.findById(id)
                .map(translation -> {
                    translation.setSourceLanguage(updatedTranslation.getSourceLanguage());
                    translation.setTargetLanguage(updatedTranslation.getTargetLanguage());
                    translation.setOriginalText(updatedTranslation.getOriginalText());
                    translation.setTranslatedText(updatedTranslation.getTranslatedText());
                    translation.setFavorite(updatedTranslation.getFavorite());
                    return translationRepository.save(translation);
                })
                .orElseThrow(() -> new TranslationNotFoundException(id));
    }

    public void delete(Long id) {
        if (translationRepository.existsById(id))
            translationRepository.deleteById(id);
    }

}
