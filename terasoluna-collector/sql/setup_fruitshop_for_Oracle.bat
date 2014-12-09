rem	使用するDBユーザ、パスワード、接続文字列を変更する場合は、
rem	下記記述書式を参考にしてください。
rem	【記述書式】
rem	sqlplus <使用するDBユーザ>/<パスワード>@<ネットサービス名> @terasoluna_functionsample_rich.sql

sqlplus functest/functest@10.68.255.230:1521/TERADB @create_table_job_control.sql
sqlplus functest/functest@10.68.255.230:1521/TERADB @create_table_user_test.sql
sqlplus functest/functest@10.68.255.230:1521/TERADB @create_table_user_test2.sql
sqlplus functest/functest@10.68.255.230:1521/TERADB @terasoluna_fruitshop.sql

call oracle_init_job_control.bat
call oracle_init_user_test.bat
call oracle_init_user_test2.bat

pause