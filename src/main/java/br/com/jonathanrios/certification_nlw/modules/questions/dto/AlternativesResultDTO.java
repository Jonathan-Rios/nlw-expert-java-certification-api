package br.com.jonathanrios.certification_nlw.modules.questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Permite usar o .builder, não precisando instanciar(new)
public class AlternativesResultDTO {
    private UUID id;
    private String description;
}
