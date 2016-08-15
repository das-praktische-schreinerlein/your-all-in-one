---------------------------
--- fix import drop check-constraints: drop unnamed checks by rename old/add new/update new/drop old columns
---------------------------
ALTER TABLE BASE_NODE ALTER COLUMN IST_AUFWAND RENAME TO IST_AUFWAND_OLD;
ALTER TABLE BASE_NODE ALTER COLUMN IST_CHILDREN_SUM_AUFWAND RENAME TO IST_CHILDREN_SUM_AUFWAND_OLD;
ALTER TABLE BASE_NODE ALTER COLUMN IST_CHILDREN_SUM_STAND RENAME TO IST_CHILDREN_SUM_STAND_OLD;
ALTER TABLE BASE_NODE ALTER COLUMN IST_STAND RENAME TO IST_STAND_OLD;
ALTER TABLE BASE_NODE ALTER COLUMN PLAN_AUFWAND RENAME TO PLAN_AUFWAND_OLD;
ALTER TABLE BASE_NODE ALTER COLUMN PLAN_CHILDREN_SUM_AUFWAND RENAME TO PLAN_CHILDREN_SUM_AUFWAND_OLD;

alter table BASE_NODE add IST_AUFWAND DOUBLE;
alter table BASE_NODE add IST_CHILDREN_SUM_AUFWAND DOUBLE;
alter table BASE_NODE add IST_CHILDREN_SUM_STAND DOUBLE;
alter table BASE_NODE add IST_STAND DOUBLE;
alter table BASE_NODE add PLAN_AUFWAND DOUBLE;
alter table BASE_NODE add PLAN_CHILDREN_SUM_AUFWAND DOUBLE;

update BASE_NODE set IST_AUFWAND = IST_AUFWAND_OLD, IST_CHILDREN_SUM_AUFWAND = IST_CHILDREN_SUM_AUFWAND_OLD, IST_CHILDREN_SUM_STAND = IST_CHILDREN_SUM_STAND_OLD, IST_STAND = IST_STAND_OLD, PLAN_AUFWAND = PLAN_AUFWAND_OLD, PLAN_CHILDREN_SUM_AUFWAND = PLAN_CHILDREN_SUM_AUFWAND_OLD;

ALTER TABLE BASE_NODE drop IST_AUFWAND_OLD;
ALTER TABLE BASE_NODE drop IST_CHILDREN_SUM_AUFWAND_OLD;
ALTER TABLE BASE_NODE drop IST_CHILDREN_SUM_STAND_OLD;
ALTER TABLE BASE_NODE drop IST_STAND_OLD;
ALTER TABLE BASE_NODE drop PLAN_AUFWAND_OLD;
ALTER TABLE BASE_NODE drop PLAN_CHILDREN_SUM_AUFWAND_OLD;
