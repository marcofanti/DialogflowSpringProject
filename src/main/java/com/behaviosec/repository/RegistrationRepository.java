package com.behaviosec.repository;

import com.behaviosec.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RegistrationRepository")
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Registration findRegistrationByName(String registrationName);
}