package com.behaviosec.service;

import com.behaviosec.model.Registration;
import com.behaviosec.repository.RegistrationRepository;

import org.springframework.stereotype.Service;

@Service("registrationService")
public class RegistrationService {

    private RegistrationRepository registrationRepository;

    @org.springframework.beans.factory.annotation.Autowired(required=true)
    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Registration findRegistrationByName(String registrationName) {
        return registrationRepository.findRegistrationByName(registrationName);
    }

    public Registration saveRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

}