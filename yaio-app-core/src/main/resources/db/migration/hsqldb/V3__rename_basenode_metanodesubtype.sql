---------------------------
--- rename field BASE_NODE.META_NODE_SUB_TYPE and set SYS_CUR_CHECKSUM to null to reinit it on next run
---------------------------
alter table BASE_NODE ALTER COLUMN META_NODE_SUB_TYPE_TAGS RENAME TO META_NODE_SUB_TYPE;
update BASE_NODE set SYS_CUR_CHECKSUM = null;
