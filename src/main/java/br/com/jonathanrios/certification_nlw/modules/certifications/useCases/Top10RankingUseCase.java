package br.com.jonathanrios.certification_nlw.modules.certifications.useCases;

import br.com.jonathanrios.certification_nlw.modules.students.entities.CertificationsStudentEntity;
import br.com.jonathanrios.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Top10RankingUseCase {
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    public List<CertificationsStudentEntity> execute() {
        return this.certificationStudentRepository.findTop10ByOrderByGradeDesc();

    }
}
