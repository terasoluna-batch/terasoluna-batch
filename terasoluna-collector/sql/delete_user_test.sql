--
-- Oracle database dump
--

-- Started on 2010-09-13 10:34:16

--SET client_encoding = 'UTF8';
--SET standard_conforming_strings = off;
--SET check_function_bodies = false;
--SET client_min_messages = warning;
--SET escape_string_warning = off;
--
--SET search_path = public, pg_catalog;

--
-- TOC entry 1758 (class 0 OID 16521)
-- Dependencies: 1489
-- Data for Name: job_control; Type: TABLE DATA; Schema: public; Owner: postgres
--

DELETE FROM user_test WHERE TO_NUMBER(USER_ID) > 2;
-- Completed on 2010-09-13 10:34:16

commit;
quit;
--
-- Oracle database dump complete
--

