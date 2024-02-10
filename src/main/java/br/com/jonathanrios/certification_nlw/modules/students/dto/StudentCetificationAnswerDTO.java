package br.com.jonathanrios.certification_nlw.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCetificationAnswerDTO {
    private String email;
    private String technology;
    private List<QuestionAnswerDTO> questionsAnswers;
}
