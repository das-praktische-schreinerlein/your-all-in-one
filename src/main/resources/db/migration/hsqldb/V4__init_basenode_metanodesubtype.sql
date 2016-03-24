---------------------------
--- update field BASE_NODE.META_NODE_SUB_TYPE with initial data
---------------------------

-------- tasks
---- departments
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.DEPARTMENT'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  EBENE in (0, 1)
;
--------- WIP
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.WIP'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  (NAME like '%urzfristige Projekte%' or NAME like '%urzfristige Aufgaben%' or NAME like '%urzfristige Pläne%' or
   NAME like '%aufende Projekte%' or NAME like '%aufende Aufgaben%' or NAME like '%aufende Pläne%')
;
--------- backlog
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.BACKLOG'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  (NAME like '%ittelfristige Projekte%' or NAME like '%ittelfristige Aufgaben%' or NAME like '%ittelfristige Pläne%' or
   NAME like '%langfristige Projekte%' or NAME like '%langfristige Aufgaben%' or NAME like '%langfristige Pläne%')
;
---- project
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.PROJECT'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  EBENE in (2,3,4) and
  STAT_WORKFLOW_COUNT > 1
;
---- subproject
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.SUBPROJECT'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  EBENE > 1 and
  STAT_WORKFLOW_COUNT > 1
;
---- tasks
update BASE_NODE set META_NODE_SUB_TYPE='TaskNodeMetaNodeSubType.TASK'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('TaskNode') and
  EBENE > 1
;

-------- events
----- timeframe
update BASE_NODE set META_NODE_SUB_TYPE='EventNodeMetaNodeSubType.TIMEFRAME'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('EventNode') and
  (NAME like '%Urlaub%')
;
----- deadline
update BASE_NODE set META_NODE_SUB_TYPE='EventNodeMetaNodeSubType.DEADLINE'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('EventNode') and
  (NAME like '%frist%' or NAME like '%Abgabetermin%')
;
---- events
update BASE_NODE set META_NODE_SUB_TYPE='EventNodeMetaNodeSubType.EVENT'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('EventNode')
;

-------- infos
----- infocontainer
update BASE_NODE set META_NODE_SUB_TYPE='InfoNodeMetaNodeSubType.CONTAINER'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('InfoNode') and
  STAT_CHILD_NODE_COUNT > 0
;
----- snapshot
update BASE_NODE set META_NODE_SUB_TYPE='InfoNodeMetaNodeSubType.SNAPSHOT'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('InfoNode') and
  (NAME like '%napshot%')
;
---- infos
update BASE_NODE set META_NODE_SUB_TYPE='InfoNodeMetaNodeSubType.INFONODE'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('InfoNode')
;

-------- resources
---- all
update BASE_NODE set META_NODE_SUB_TYPE='UrlResNodeMetaNodeSubType.RESOURCE'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('UrlResNode')
;

-------- Symlinks
---- all
update BASE_NODE set META_NODE_SUB_TYPE='SymLinkNodeMetaNodeSubType.SYMLINK'
where
  META_NODE_SUB_TYPE is null and
  DTYPE in ('SymLinkNode')
;
