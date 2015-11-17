DROP TABLE IF EXISTS user_test;

CREATE TABLE user_test
(
  user_id INT NOT NULL,
  user_family_name VARCHAR2(20),
  user_first_name VARCHAR2(20),
  user_age VARCHAR2(3),
  CONSTRAINT pk_user_test PRIMARY KEY (user_id)
);

