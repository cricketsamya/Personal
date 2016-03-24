CREATE TABLE patient( id INTEGER IDENTITY, first_name VARCHAR(64) NOT NULL, last_name VARCHAR(64) NOT NULL );
CREATE TABLE zone( id INTEGER IDENTITY, name VARCHAR(64));
CREATE TABLE patientzone( id INTEGER IDENTITY, patient_id INTEGER, zone_id INTEGER,zone_type VARCHAR(1),FOREIGN KEY (patient_id) REFERENCES patient(id),FOREIGN KEY (zone_id) REFERENCES zone(id));
CREATE TABLE patienthabitationhistory( id INTEGER IDENTITY, patient_id INTEGER, zone_id INTEGER,lastTS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,iscurrentstate INTEGER, FOREIGN KEY (patient_id) REFERENCES patient(id),FOREIGN KEY (zone_id) REFERENCES zone(id));

INSERT INTO patient (first_name, last_name) VALUES ('Sameer','Kulkarni');
INSERT INTO patient (first_name, last_name) VALUES ('John','Cena');
INSERT INTO patient (first_name, last_name) VALUES ('Eric','Bishop');
INSERT INTO patient (first_name, last_name) VALUES ('King','Ace');
INSERT INTO patient (first_name, last_name) VALUES ('Brother','Big');

INSERT INTO zone (name) VALUES ('Ward I');
INSERT INTO zone (name) VALUES ('Ward II');
INSERT INTO zone (name) VALUES ('Balcony');
INSERT INTO zone (name) VALUES ('Parking');
INSERT INTO zone (name) VALUES ('Toilet');
INSERT INTO zone (name) VALUES ('Basement');
INSERT INTO zone (name) VALUES ('OT');

INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,2,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,4,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,6,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (1,4,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (1,6,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (2,4,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (3,2,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (3,4,'R');
INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (4,2,'R');

INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (0,1,'A');
INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (1,1,'A');
INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (2,0,'A');
INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (3,0,'A');
INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (4,1,'A');

INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (0,1,1);
INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (1,1,1);
INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (2,0,1);
INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (3,0,1);
INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (4,1,1);


UPDATE patienthabitationhistory SET iscurrentstate = 0 WHERE iscurrentstate = 1 AND patient_id=0;
INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (0,3,1);