DROP TABLE user_test2;

CREATE TABLE user_test2
(
  user_id VARCHAR2(7) NOT NULL,
  user_family_name VARCHAR2(20),
  user_first_name VARCHAR2(20),
  user_age VARCHAR2(3),
  CONSTRAINT pk_user_test2 PRIMARY KEY (user_id)
);

quit
