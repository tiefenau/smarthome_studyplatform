use pfiva_db;

create table user_tbl
(
	user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    device_id VARCHAR(50) NOT NULL,
    PRIMARY KEY(user_id)
);

create table query_tbl
(
	query_id INT NOT NULL AUTO_INCREMENT,
    user_query VARCHAR(500) NOT NULL,
    hotword VARCHAR(50),
    query_timestamp DATETIME,
    file_location VARCHAR(300),
    user_id INT NOT NULL,
    PRIMARY KEY(query_id),
    FOREIGN KEY(user_id) REFERENCES user_tbl(user_id)
);

create table intent_tbl
(
	intent_id INT NOT NULL AUTO_INCREMENT,
    intent_name VARCHAR(100) NOT NULL,
    query_id INT NOT NULL,
    PRIMARY KEY(intent_id),
    FOREIGN KEY(query_id) REFERENCES query_tbl(query_id) ON DELETE CASCADE
);

create table slots_tbl
(
	slot_id INT NOT NULL AUTO_INCREMENT,
    slot_name VARCHAR(100) NOT NULL,
    slot_value VARCHAR(100) NOT NULL,
    intent_id INT NOT NULL,
    PRIMARY KEY(slot_id),
    FOREIGN KEY(intent_id) REFERENCES intent_tbl(intent_id) ON DELETE CASCADE 
);

create table clients_tbl
(
	client_id INT NOT NULL AUTO_INCREMENT,
    client_name VARCHAR(100) NOT NULL,
    client_token VARCHAR(500) NOT NULL,
    token_timestamp DATETIME,
    PRIMARY KEY(client_id)
);

create table feedback_tbl
(
	feedback_id INT NOT NULL AUTO_INCREMENT,
    feedback_query VARCHAR(500) NOT NULL,
    user_response VARCHAR(100),
    feedback_timestamp DATETIME,
    query_id INT NOT NULL,
    PRIMARY KEY(feedback_id),
    FOREIGN KEY(query_id) REFERENCES query_tbl(query_id) ON DELETE CASCADE
);

create table topic_tbl
(
	topic_id INT NOT NULL AUTO_INCREMENT,
    topic_name VARCHAR(100) NOT NULL,
    creation_date DATETIME,
    modification_date DATETIME,
    PRIMARY KEY(topic_id)
);

create table configuration_tbl
(
	config_id INT NOT NULL AUTO_INCREMENT,
    config_key VARCHAR(100) NOT NULL, 
    config_value VARCHAR(200) NOT NULL,
    PRIMARY KEY(config_id)
);

create table messages_tbl
(
	message_id INT NOT NULL AUTO_INCREMENT,
    message_text VARCHAR(500) NOT NULL,
    status VARCHAR(20),
    delivery_date DATETIME,
    topic_id INT NOT NULL,
    PRIMARY KEY(message_id),
    FOREIGN KEY(topic_id) REFERENCES topic_tbl(topic_id) ON DELETE CASCADE
);

create table message_users_tbl
(
	message_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(message_id) REFERENCES messages_tbl(message_id)  ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user_tbl(user_id)
    
);

create table message_response_tbl
(
	response_id INT NOT NULL AUTO_INCREMENT,
    value VARCHAR(200) NOT NULL,
    message_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(response_id),
    FOREIGN KEY(message_id) REFERENCES messages_tbl(message_id)  ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user_tbl(user_id)
);

create table survey_tbl
(
	survey_id INT NOT NULL AUTO_INCREMENT,
    survey_name VARCHAR(100) NOT NULL,
    status VARCHAR(20),
    delivery_date DATETIME,
    topic_id INT NOT NULL,
    PRIMARY KEY(survey_id),
    FOREIGN KEY(topic_id) REFERENCES topic_tbl(topic_id)  ON DELETE CASCADE
);

create table questions_tbl
(
	question_id INT NOT NULL AUTO_INCREMENT,
    question VARCHAR(500) NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    survey_id INT NOT NULL,
    PRIMARY KEY(question_id),
    FOREIGN KEY(survey_id) REFERENCES survey_tbl(survey_id)  ON DELETE CASCADE
);

create table options_tbl
(
	option_id INT NOT NULL AUTO_INCREMENT,
    value VARCHAR(100) NOT NULL,
    question_id INT NOT NULL,
    PRIMARY KEY(option_id),
    FOREIGN KEY(question_id) REFERENCES questions_tbl(question_id)  ON DELETE CASCADE
);

create table survey_users_tbl
(
	survey_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(survey_id) REFERENCES survey_tbl(survey_id)  ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user_tbl(user_id)
);

create table survey_response_tbl
(
	response_id INT NOT NULL AUTO_INCREMENT,
    value VARCHAR(200) NOT NULL,
    question_id INT NOT NULL,
    survey_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(response_id),
    FOREIGN KEY(question_id) REFERENCES questions_tbl(question_id) ON DELETE CASCADE,
    FOREIGN KEY(survey_id) REFERENCES survey_tbl(survey_id),
    FOREIGN KEY(user_id) REFERENCES user_tbl(user_id)
);