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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<Translation> findAllTranslationsPagedAndSorted(String[] sort, Integer page, Integer size) {
        List<Order> orders = extractOrders(sort);
        var pageable = PageRequest.of(page, size, Sort.by(orders));
        return translationRepository.findAll(pageable);
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

    public List<Translation> findByFavoriteTrueAndUserId(Long id) {
        return translationRepository.findByFavoriteTrueAndUserId(id);
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

    public Translation update(UpdateTranslationDto updatedTranslation) {
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

    /*
    1 order
    [0]: id
    [1]: desc

    multiples orders
    [0]: id,desc
    [1]: name,asc
    [2]: email,desc
     */
    private List<Sort.Order> extractOrders(String[] sort) {
        if(sort[0].contains(","))
            return Arrays.stream(sort).map(this::extractOrder).collect(Collectors.toList());

        return List.of(extractOrder(sort[0] + "," + sort[1]));
    }

    private Sort.Order extractOrder(String sort) {
        System.out.println(sort);
        String[] pair = sort.split(",");
        String field = pair[0];
        Sort.Direction direction = pair[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort.Order(direction, field);
    }

}
