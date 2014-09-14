// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.node.TaskNode;

privileged aspect TaskNodeServiceImpl_Roo_Service {
    
    declare @type: TaskNodeServiceImpl: @Service;
    
    declare @type: TaskNodeServiceImpl: @Transactional;
    
    public long TaskNodeServiceImpl.countAllTaskNodes() {
        return TaskNode.countTaskNodes();
    }
    
    public void TaskNodeServiceImpl.deleteTaskNode(TaskNode taskNode) {
        taskNode.remove();
    }
    
    public TaskNode TaskNodeServiceImpl.findTaskNode(String id) {
        return TaskNode.findTaskNode(id);
    }
    
    public List<TaskNode> TaskNodeServiceImpl.findAllTaskNodes() {
        return TaskNode.findAllTaskNodes();
    }
    
    public List<TaskNode> TaskNodeServiceImpl.findTaskNodeEntries(int firstResult, int maxResults) {
        return TaskNode.findTaskNodeEntries(firstResult, maxResults);
    }
    
    public void TaskNodeServiceImpl.saveTaskNode(TaskNode taskNode) {
        taskNode.persist();
    }
    
    public TaskNode TaskNodeServiceImpl.updateTaskNode(TaskNode taskNode) {
        return taskNode.merge();
    }
    
}
