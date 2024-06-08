package com.translaitor.service.dto.translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTranslationDto {

    @NotNull
    private Long id;

    @NotNull
    private Boolean favorite;
}
