// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.service;

import de.yaio.core.node.TaskNode;
import de.yaio.core.service.TaskNodeService;
import java.util.List;

privileged aspect TaskNodeService_Roo_Service {
    
    public abstract long TaskNodeService.countAllTaskNodes();    
    public abstract void TaskNodeService.deleteTaskNode(TaskNode taskNode);    
    public abstract TaskNode TaskNodeService.findTaskNode(Long id);    
    public abstract List<TaskNode> TaskNodeService.findAllTaskNodes();    
    public abstract List<TaskNode> TaskNodeService.findTaskNodeEntries(int firstResult, int maxResults);    
    public abstract void TaskNodeService.saveTaskNode(TaskNode taskNode);    
    public abstract TaskNode TaskNodeService.updateTaskNode(TaskNode taskNode);    
}
