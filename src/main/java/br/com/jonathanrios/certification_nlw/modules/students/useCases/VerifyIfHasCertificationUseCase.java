package br.com.jonathanrios.certification_nlw.modules.students.useCases;

import br.com.jonathanrios.certification_nlw.CertificationNlwApplication;
import br.com.jonathanrios.certification_nlw.modules.students.dto.VerifyIfHasCertificationDTO;
import br.com.jonathanrios.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyIfHasCertificationUseCase {
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;
    public boolean execute(VerifyIfHasCertificationDTO dto) {
        var result = this.certificationStudentRepository.findByStudentEmailAndTechnology(dto.getEmail(), dto.getTechnology());
        if(!result.isEmpty()) {
            return true;
        }
        return false;
    }
}
