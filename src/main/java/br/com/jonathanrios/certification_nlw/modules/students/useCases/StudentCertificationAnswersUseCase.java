package br.com.jonathanrios.certification_nlw.modules.students.useCases;

import br.com.jonathanrios.certification_nlw.modules.questions.entities.QuestionEntity;
import br.com.jonathanrios.certification_nlw.modules.questions.respositories.QuestionRepository;
import br.com.jonathanrios.certification_nlw.modules.students.dto.StudentCetificationAnswerDTO;
import br.com.jonathanrios.certification_nlw.modules.students.dto.VerifyIfHasCertificationDTO;
import br.com.jonathanrios.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import br.com.jonathanrios.certification_nlw.modules.students.entities.CertificationsStudentEntity;
import br.com.jonathanrios.certification_nlw.modules.students.entities.StudentEntity;
import br.com.jonathanrios.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import br.com.jonathanrios.certification_nlw.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    // throws Exception: faz com que quem for consumir esse metodo seja responsável por tratar.
    public CertificationsStudentEntity execute(StudentCetificationAnswerDTO dto ) throws Exception{

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyIfHasCertificationDTO(
            dto.getEmail(),
            dto.getTechnology()
        ));

        if(hasCertification){
            throw new Exception("Você já tirou sua certificação!");
        }

        List<QuestionEntity> questionsEntities = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0); // Permite ser alterado dentro do lambda

        dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
            var question = questionsEntities.stream()
                    .filter(q -> q.getId()
                    .equals(questionAnswer.getQuestionID()))
                    .findFirst().get();

            var findCorrectAlternative = question.getAlternatives().stream()
                    .filter(alternative -> alternative.getIsCorrect()).findFirst().get();

            if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())){
                questionAnswer.setCorrect(true);
                correctAnswers.incrementAndGet();
            } else {
                questionAnswer.setCorrect(false);
            }


            var answersCertificationsEntity = AnswersCertificationsEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestionID())
                    .isCorrect(questionAnswer.isCorrect())
                    .build();

            answersCertifications.add(answersCertificationsEntity);
        });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;

        if(student.isEmpty()) {
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }


        CertificationsStudentEntity certificationsStudentEntity = CertificationsStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build();



        var certificationStudentCreated = certificationStudentRepository.save(certificationsStudentEntity);

        answersCertifications.stream().forEach(answerCertification -> {
            answerCertification.setCertificationID(certificationsStudentEntity.getId());
            answerCertification.setCertificationsStudentEntity(certificationsStudentEntity);
        });

        certificationsStudentEntity.setAnswersCertificationsEntities(answersCertifications);

        certificationStudentRepository.save(certificationsStudentEntity);

        return certificationStudentCreated;
    }
}
