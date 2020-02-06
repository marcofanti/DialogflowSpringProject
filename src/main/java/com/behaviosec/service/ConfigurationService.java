package com.behaviosec.service;

import com.behaviosec.model.Configuration;
import com.behaviosec.repository.ConfigurationRepository;

import org.springframework.stereotype.Service;

@Service("configurationService")
public class ConfigurationService {

    private ConfigurationRepository configurationRepository;

    @org.springframework.beans.factory.annotation.Autowired(required=true)
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Configuration findConfigurationByName(String configurationName) {
        return configurationRepository.findConfigurationByConfigurationName(configurationName);
    }

    public Configuration saveConfiguration(Configuration configuration) {
    	Configuration existingConfiguration = findConfigurationByName(configuration.getConfigurationName());
    	if (existingConfiguration != null) {
    		existingConfiguration.setConfigurationValue(configuration.getConfigurationValue());
    		return configurationRepository.save(existingConfiguration);
    	}
        return configurationRepository.save(configuration);
    }
}