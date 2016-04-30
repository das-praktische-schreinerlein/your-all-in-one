---------------------------
--- add parent_hierarchy
---------------------------
alter table BASE_NODE ADD COLUMN CACHED_PARENT_HIERARCHY VARCHAR(2000) DEFAULT NULL;
