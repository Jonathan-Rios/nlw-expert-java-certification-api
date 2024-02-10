package br.com.jonathanrios.certification_nlw.modules.students.controllers;

import br.com.jonathanrios.certification_nlw.modules.students.dto.StudentCetificationAnswerDTO;
import br.com.jonathanrios.certification_nlw.modules.students.dto.VerifyIfHasCertificationDTO;
import br.com.jonathanrios.certification_nlw.modules.students.entities.CertificationsStudentEntity;
import br.com.jonathanrios.certification_nlw.modules.students.useCases.StudentCertificationAnswersUseCase;
import br.com.jonathanrios.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired // O Spring inicializa o useCase ( como se fosse usando o new ... )
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyIfHasCertificationDTO verifyIfHasCertificationDTO) {
        System.out.println(verifyIfHasCertificationDTO);
        var result = this.verifyIfHasCertificationUseCase.execute(verifyIfHasCertificationDTO);
        System.out.println(result);
        if (result) {
            return "Usuário já fez a prova";
        }

        return "Usuário pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswe(@RequestBody StudentCetificationAnswerDTO studentCetificationAnswerDTO) throws Exception{
        try {
            var result = this.studentCertificationAnswersUseCase.execute(studentCetificationAnswerDTO);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
