---------------------------
--- extend column-size
---------------------------
alter table BASE_NODE ALTER COLUMN SRC_NAME VARCHAR(64000000);
alter table BASE_NODE ALTER COLUMN NODE_DESC VARCHAR(64000000);
