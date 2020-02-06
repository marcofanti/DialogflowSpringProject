package com.behaviosec.repository;

import com.behaviosec.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ConfigurationRepository")
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
	Configuration findConfigurationByConfigurationName(String ConfigurationName);
}