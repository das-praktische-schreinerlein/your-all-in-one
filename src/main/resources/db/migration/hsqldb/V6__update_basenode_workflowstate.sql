---------------------------
--- update field WORKFLOW_STATE with new workflow-codes
---------------------------
update BASE_NODE set WORKFLOW_STATE=10 where WORKFLOW_STATE = 0;
update BASE_NODE set WORKFLOW_STATE=20 where WORKFLOW_STATE = 1;
update BASE_NODE set WORKFLOW_STATE=30 where WORKFLOW_STATE = 2;
update BASE_NODE set WORKFLOW_STATE=40 where WORKFLOW_STATE = 3;
update BASE_NODE set WORKFLOW_STATE=50 where WORKFLOW_STATE = 4;
update BASE_NODE set WORKFLOW_STATE=60 where WORKFLOW_STATE = 5;
update BASE_NODE set WORKFLOW_STATE=70 where WORKFLOW_STATE = 6;
update BASE_NODE set WORKFLOW_STATE=80 where WORKFLOW_STATE = 7;
update BASE_NODE set WORKFLOW_STATE=90 where WORKFLOW_STATE = 8;
