CREATE TABLE configuration (
  CONFIGURATION_ID         INTEGER PRIMARY KEY,
  CONFIGNAME VARCHAR(30),
  configvalue  INTEGER
);

INSERT INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (1,'minimumloginscore', 60);
INSERT  INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (2,'maximumloginrisk', 50);
INSERT  INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (3,'minimumstepupscore', 60);
INSERT  INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (4,'maximumstepuprisk', 50);
INSERT  INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (5,'minimumformscore', 60);
INSERT  INTO configuration (CONFIGURATION_ID, CONFIGNAME, CONFIGVALUE) VALUES (6,'maximumformrisk', 50);

