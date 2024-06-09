package com.translaitor.repository;

import com.translaitor.model.Translation;
import com.translaitor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    List<Translation> findByUser(User user);

    List<Translation> findByFavoriteTrueAndUserId(Long id);

}