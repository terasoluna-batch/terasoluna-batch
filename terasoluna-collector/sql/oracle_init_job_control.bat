rem	使用するDBユーザ、パスワード、接続文字列を変更する場合は、
rem	下記記述書式を参考にしてください。
rem	【記述書式】
rem	sqlplus <使用するDBユーザ>/<パスワード>@<ネットサービス名> @terasoluna_functionsample_thin.sql

sqlplus functest/functest@10.68.255.230:1521/TERADB @insert_job_control.sql

pause