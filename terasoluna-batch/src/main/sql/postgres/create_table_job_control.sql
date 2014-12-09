-- Table: job_control

-- DROP TABLE job_control;

CREATE TABLE job_control
(
  job_seq_id character varying(10) NOT NULL,
  job_app_cd character varying(10),
  job_arg_nm1 character varying(100),
  job_arg_nm2 character varying(100),
  job_arg_nm3 character varying(100),
  job_arg_nm4 character varying(100),
  job_arg_nm5 character varying(100),
  job_arg_nm6 character varying(100),
  job_arg_nm7 character varying(100),
  job_arg_nm8 character varying(100),
  job_arg_nm9 character varying(100),
  job_arg_nm10 character varying(100),
  job_arg_nm11 character varying(100),
  job_arg_nm12 character varying(100),
  job_arg_nm13 character varying(100),
  job_arg_nm14 character varying(100),
  job_arg_nm15 character varying(100),
  job_arg_nm16 character varying(100),
  job_arg_nm17 character varying(100),
  job_arg_nm18 character varying(100),
  job_arg_nm19 character varying(100),
  job_arg_nm20 character varying(100),
  blogic_app_status character varying(10),
  cur_app_status character varying(1),
  add_date_time timestamp,
  upd_date_time timestamp,
  CONSTRAINT pk_job_control PRIMARY KEY (job_seq_id)
);

