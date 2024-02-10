package br.com.jonathanrios.certification_nlw.modules.questions.controllers;

import br.com.jonathanrios.certification_nlw.modules.questions.dto.AlternativesResultDTO;
import br.com.jonathanrios.certification_nlw.modules.questions.dto.QuestionResultDTO;
import br.com.jonathanrios.certification_nlw.modules.questions.entities.AlternativesEntity;
import br.com.jonathanrios.certification_nlw.modules.questions.entities.QuestionEntity;
import br.com.jonathanrios.certification_nlw.modules.questions.respositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology) {
        var result = this.questionRepository.findByTechnology(technology);

        var toMap = result.stream().map(question -> mapQuestionToDTO(question)).collect(Collectors.toList());
        return toMap;
    }

    static QuestionResultDTO mapQuestionToDTO(QuestionEntity questions) {
       var questionResultDTO =  QuestionResultDTO.builder()
               .id(questions.getId())
               .technology(questions.getTechnology())
               .description(questions.getDescription())
               .build();

       List<AlternativesResultDTO> alternativesResultDTOs =
       questions.getAlternatives()
       .stream().map(alternative -> mapAlternativeDTO(alternative))
       .collect(Collectors.toList());

       questionResultDTO.setAlternatives(alternativesResultDTOs);
       return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeDTO(AlternativesEntity alternativesResultDTO) {
        return AlternativesResultDTO.builder()
                .id(alternativesResultDTO.getId())
                .description(alternativesResultDTO.getDescription()).build();
    }
}
