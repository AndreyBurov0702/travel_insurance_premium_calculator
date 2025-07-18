
CREATE TABLE classifiers (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_classifiers_title ON classifiers(title);

CREATE TABLE classifier_values (
  id BIGINT NOT NULL AUTO_INCREMENT,
  classifier_id BIGINT NOT NULL,
  ic VARCHAR(200) NOT NULL,
  description VARCHAR(500) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE classifier_values
ADD FOREIGN KEY (classifier_id) REFERENCES classifiers(id);

CREATE UNIQUE INDEX ix_classifier_values_ic
ON classifier_values(ic);


CREATE TABLE country_default_day_rate (
  id BIGINT NOT NULL AUTO_INCREMENT,
  country_ic VARCHAR(200) NOT NULL,
  default_day_rate NUMERIC(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_country_default_day_rate_country_ic
ON country_default_day_rate (country_ic);

CREATE TABLE IF NOT EXISTS age_coefficient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  age_from INT NOT NULL,
  age_to INT NOT NULL,
  coefficient DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS medical_risk_limit_level (
  id BIGINT NOT NULL AUTO_INCREMENT,
  medical_risk_limit_level_ic VARCHAR(200) NOT NULL,
  coefficient DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_medical_risk_limit_level_limit_level_ic
ON medical_risk_limit_level (medical_risk_limit_level_ic);

CREATE TABLE persons (
  id BIGINT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(200) NOT NULL,
  last_name VARCHAR(200) NOT NULL,
  person_code VARCHAR(200) NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_unique_persons ON persons(first_name, last_name, person_code);

CREATE TABLE agreements (
  id BIGINT NOT NULL AUTO_INCREMENT,
  date_from TIMESTAMP NOT NULL,
  date_to TIMESTAMP NOT NULL,
  country VARCHAR(100) NOT NULL,
  premium DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE selected_risks (
  id BIGINT NOT NULL AUTO_INCREMENT,
  agreement_id BIGINT NOT NULL,
  risk_ic VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  foreign key (agreement_id) references agreements(id)
);

CREATE UNIQUE INDEX ix_selected_risks_agreement_id_risk_ic
ON selected_risks(agreement_id, risk_ic);

CREATE TABLE agreement_persons (
  id BIGINT NOT NULL AUTO_INCREMENT,
  agreement_id BIGINT NOT NULL,
  person_id BIGINT NOT NULL,
  medical_risk_limit_level VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  foreign key (agreement_id) references agreements(id),
  foreign key (person_id) references persons(id)
);

CREATE TABLE agreement_person_risks (
  id BIGINT NOT NULL AUTO_INCREMENT,
  agreement_person_id BIGINT NOT NULL,
  risk_ic VARCHAR(100) NOT NULL,
  premium DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  foreign key (agreement_person_id) references agreement_persons(id)
);

CREATE UNIQUE INDEX ix_agreement_person_risks_agreement_person_id_risk_ic
ON agreement_person_risks(agreement_person_id, risk_ic);

ALTER TABLE agreements ADD uuid VARCHAR(255) NOT NULL;

CREATE TABLE IF NOT EXISTS travel_cost_coefficient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  travel_cost_from DECIMAL(10,2) NOT NULL,
  travel_cost_to DECIMAL(10,2) NOT NULL,
  coefficient DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE age_coefficient
  RENAME TO travel_medical_age_coefficient;

ALTER TABLE travel_cost_coefficient
  RENAME TO travel_cancellation_travel_cost_coefficient;

ALTER TABLE country_default_day_rate
  RENAME TO travel_medical_country_default_day_rate;

ALTER TABLE medical_risk_limit_level
  RENAME TO travel_medical_risk_limit_level;


CREATE TABLE IF NOT EXISTS travel_cancellation_age_coefficient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  age_from INT NOT NULL,
  age_to INT NOT NULL,
  coefficient DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE travel_cancellation_country_safety_rating (
  id BIGINT NOT NULL AUTO_INCREMENT,
  country_ic VARCHAR(200) NOT NULL,
  coefficient NUMERIC(10,2) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE agreement_persons
    ADD COLUMN travel_cost DECIMAL(10,2);

ALTER TABLE agreement_persons
ALTER COLUMN medical_risk_limit_level DROP NOT NULL;

ALTER TABLE agreements
ADD CONSTRAINT unique_uuid UNIQUE (uuid);

CREATE TABLE agreements_xml_export (
  id BIGINT NOT NULL AUTO_INCREMENT,
  agreement_uuid VARCHAR(255) NOT NULL,
  already_exported BOOL NOT NULL,
  PRIMARY KEY (id),
  foreign key (agreement_uuid) references agreements(uuid)
);

CREATE TABLE agreement_proposals (
  id BIGINT NOT NULL AUTO_INCREMENT,
  agreement_uuid VARCHAR(255) NOT NULL,
  already_generated CHAR(1) NOT NULL,
  proposal_file_path VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  foreign key (agreement_uuid) references agreements(uuid)
);