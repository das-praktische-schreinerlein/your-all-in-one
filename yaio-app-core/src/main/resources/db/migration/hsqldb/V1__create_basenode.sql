---------------------------
--- create table BASE_NODE
---------------------------
CREATE MEMORY TABLE IF NOT EXISTS PUBLIC.BASE_NODE(
  DTYPE VARCHAR(31) NOT NULL,
  SYSUID VARCHAR(80) NOT NULL PRIMARY KEY,
  CLASS_NAME VARCHAR(255),
  DOC_LAYOUT_ADD_STYLE_CLASS VARCHAR(255),
  DOC_LAYOUT_FLG_CLOSE_DIV VARCHAR(5),
  DOC_LAYOUT_SHORT_NAME VARCHAR(80),
  DOC_LAYOUT_TAG_COMMAND VARCHAR(5),
  EBENE INTEGER,
  IMPORT_TMP_ID BIGINT,
  IST_AUFWAND DOUBLE,
  IST_CHILDREN_SUM_AUFWAND DOUBLE,
  IST_CHILDREN_SUM_ENDE TIMESTAMP,
  IST_CHILDREN_SUM_STAND DOUBLE,
  IST_CHILDREN_SUM_START TIMESTAMP,
  IST_ENDE TIMESTAMP,
  IST_STAND DOUBLE,
  IST_START TIMESTAMP,
  IST_TASK VARCHAR(30),
  META_NODE_NUMMER VARCHAR(10),
  META_NODE_PRAEFIX VARCHAR(10),
  META_NODE_TYPE_TAGS VARCHAR(255),
  META_NODE_SUB_TYPE_TAGS VARCHAR(255),
  NAME VARCHAR(2000) NOT NULL,
  NODE_DESC VARCHAR(64000),
  PLAN_AUFWAND DOUBLE,
  PLAN_CALC_CHECK_SUM VARCHAR(50),
  PLAN_CALC_ENDE TIMESTAMP,
  PLAN_CALC_START TIMESTAMP,
  PLAN_CHILDREN_SUM_AUFWAND DOUBLE,
  PLAN_CHILDREN_SUM_ENDE TIMESTAMP,
  PLAN_CHILDREN_SUM_START TIMESTAMP,
  PLAN_DURATION INTEGER,
  PLAN_DURATION_MEASURE VARCHAR(255),
  PLAN_ENDE TIMESTAMP,
  PLAN_PREDECESSOR_DEPENDENCIE_TYPE VARCHAR(255),
  PLAN_PREDECESSOR_SHIFT INTEGER,
  PLAN_PREDECESSOR_SHIFT_MEASURE VARCHAR(255),
  PLAN_PREDECESSOR_TYPE VARCHAR(255),
  PLAN_START TIMESTAMP,
  PLAN_TASK VARCHAR(30),
  SORT_POS INTEGER,
  SRC_NAME VARCHAR(64000),
  STAT_CHILD_NODE_COUNT INTEGER,
  STAT_INFO_COUNT INTEGER,
  STAT_URL_RES_COUNT INTEGER,
  STAT_WORKFLOW_COUNT INTEGER,
  STAT_WORKFLOW_TODO_COUNT INTEGER,
  STATE VARCHAR(255),
  SYS_CHANGE_COUNT INTEGER,
  SYS_CHANGE_DATE TIMESTAMP,
  SYS_CREATE_DATE TIMESTAMP,
  SYS_CUR_CHECKSUM VARCHAR(50),
  TYPE VARCHAR(255),
  VERSION INTEGER,
  WORKFLOW_STATE INTEGER,
  SYM_LINK_NAME VARCHAR(255),
  SYM_LINK_REF VARCHAR(800),
  SYM_LINK_TAGS VARCHAR(255),
  RES_CONTENTDMSID VARCHAR(255),
  RES_CONTENTDMSSTATE INTEGER,
  RES_CONTENTDMSTYPE VARCHAR(20),
  RES_CONTENT_MIME VARCHAR(60),
  RES_CONTENT_SIZE BIGINT,
  RES_INDEXDMSID VARCHAR(255),
  RES_INDEXDMSSTATE INTEGER,
  RES_INDEXDMSTYPE VARCHAR(20),
  RES_LOC_NAME VARCHAR(255),
  RES_LOC_REF VARCHAR(2000),
  RES_LOC_TAGS VARCHAR(255),
  PARENT_NODE VARCHAR(80),
  PLAN_PREDECESSOR VARCHAR(80),
  CHECK((PUBLIC.BASE_NODE.EBENE>=0) AND (PUBLIC.BASE_NODE.EBENE<=50)),
  CHECK(PUBLIC.BASE_NODE.IST_AUFWAND>=0),
  CHECK(PUBLIC.BASE_NODE.IST_CHILDREN_SUM_AUFWAND>=0),
  CHECK((PUBLIC.BASE_NODE.IST_CHILDREN_SUM_STAND<=100) AND (PUBLIC.BASE_NODE.IST_CHILDREN_SUM_STAND>=0)),
  CHECK((PUBLIC.BASE_NODE.IST_STAND<=100) AND (PUBLIC.BASE_NODE.IST_STAND>=0)),
  CHECK(PUBLIC.BASE_NODE.PLAN_AUFWAND>=0),
  CHECK(PUBLIC.BASE_NODE.PLAN_CHILDREN_SUM_AUFWAND>=0),
  CHECK(PUBLIC.BASE_NODE.PLAN_DURATION>=0),
  CHECK(PUBLIC.BASE_NODE.SORT_POS>=0),
  CHECK(PUBLIC.BASE_NODE.STAT_CHILD_NODE_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.STAT_INFO_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.STAT_URL_RES_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.STAT_WORKFLOW_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.STAT_WORKFLOW_TODO_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.SYS_CHANGE_COUNT>=0),
  CHECK(PUBLIC.BASE_NODE.RES_CONTENT_SIZE>=0),
  CONSTRAINT FK_JYL61EY5OPRUI5UCIRG56R5N FOREIGN KEY(PARENT_NODE) REFERENCES PUBLIC.BASE_NODE(SYSUID),
  CONSTRAINT FK_DWE6I2RVCRBEP652K2KNWLSK1 FOREIGN KEY(PLAN_PREDECESSOR) REFERENCES PUBLIC.BASE_NODE(SYSUID)
);
