package com.translaitor.service.dto.translation;

import com.translaitor.service.dto.user.UpdateUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTranslationDto {

    @NotEmpty
    private String sourceLanguage;

    @NotEmpty
    private String targetLanguage;

    @NotEmpty
    private String originalText;

    @NotEmpty
    private String translatedText;

    private Boolean favorite;

    private UpdateUserDto user;

}
