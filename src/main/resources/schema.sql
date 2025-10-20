CREATE TABLE USERS (
                       USER_ID NUMBER(10) PRIMARY KEY,
                       USERNAME VARCHAR(50) NOT NULL,
                       EMAIL VARCHAR(100),
                       CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE FRND_REQUEST (
                              REQUEST_ID VARCHAR(36) PRIMARY KEY,
                              TARGET_USER_ID NUMBER(10) NOT NULL,
                              REQUEST_USER_ID NUMBER(10) NOT NULL,
                              PROC_YN CHAR(1) DEFAULT 'N',
                              REQUESTED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              RESPONDED_AT TIMESTAMP
);

CREATE TABLE FRND_LST_MST (
                              FROM_USER_ID NUMBER(10) NOT NULL,
                              TO_USER_ID NUMBER(10) NOT NULL,
                              APPROVEDAT DATE NOT NULL,
                              PRIMARY KEY (FROM_USER_ID, TO_USER_ID)
);