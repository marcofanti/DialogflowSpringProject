package com.behaviosec.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "configuration_id")
    private int id;
    @Column(name = "configname")
    @NotEmpty(message = "*Please provide a configuration name")
    private String configurationName;
     @Column(name = "configvalue")
    private String configurationValue;


    @java.lang.SuppressWarnings("all")
    public static class ConfigurationBuilder {
        @java.lang.SuppressWarnings("all")
        private int id;
        @java.lang.SuppressWarnings("all")
        private String configurationName;
        @java.lang.SuppressWarnings("all")
        private String configurationValue;
 
        @java.lang.SuppressWarnings("all")
        ConfigurationBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public ConfigurationBuilder id(final int id) {
            this.id = id;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public ConfigurationBuilder configurationName(final String configurationName) {
            this.configurationName = configurationName;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public ConfigurationBuilder configvalue(final String configurationValue) {
            this.configurationValue = configurationValue;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Configuration build() {
            return new Configuration(id, configurationName, configurationValue);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "Configuration.UserBuilder(id=" + this.id + ", configurationName=" + 
        this.configurationName + ", configurationValue=" + this.configurationValue + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public int getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public String getConfigurationName() {
        return this.configurationName;
    }

    @java.lang.SuppressWarnings("all")
    public String getConfigurationValue() {
        return this.configurationValue;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final int id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setConfigurationName(final String configurationName) {
        this.configurationName = configurationName;
    }

    @java.lang.SuppressWarnings("all")
    public void setConfigurationValue(final String configurationValue) {
        this.configurationValue = configurationValue;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Configuration)) return false;
        final Configuration configuration = (Configuration) o;
        if (!configuration.canEqual((java.lang.Object) this)) return false;
        if (this.getId() != configuration.getId()) return false;
        final java.lang.Object this$configname = this.getConfigurationName();
        final java.lang.Object configurationValue$configname = configuration.getConfigurationName();
        if (this$configname == null ? configurationValue$configname != null : !this$configname.equals(configurationValue$configname)) return false;
        final java.lang.Object this$configurationValue = this.getConfigurationValue();
        final java.lang.Object configurationValue$configurationValue = configuration.getConfigurationValue();
        if (this$configurationValue == null ? configurationValue$configurationValue != null : !this$configurationValue.equals(configurationValue$configurationValue)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object configurationValue) {
        return configurationValue instanceof Configuration;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final java.lang.Object $configname = this.getConfigurationName();
        result = result * PRIME + ($configname == null ? 43 : $configname.hashCode());
        final java.lang.Object $configurationValue = this.getConfigurationValue();
        result = result * PRIME + ($configurationValue == null ? 43 : $configurationValue.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "UserReport(id=" + this.getId() + ", configname=" + this.getConfigurationName() + ", configurationValue=" + this.getConfigurationValue() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public Configuration(final int id, final String configname, final String configurationValue) {
        this.id = id;
        this.configurationName = configname;
        this.configurationValue = configurationValue;
    }

    @java.lang.SuppressWarnings("all")
    public Configuration() {
    }
}
