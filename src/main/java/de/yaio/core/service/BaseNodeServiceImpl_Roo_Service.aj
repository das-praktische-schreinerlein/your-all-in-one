// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.node.BaseNode;

privileged aspect BaseNodeServiceImpl_Roo_Service {
    
    declare @type: BaseNodeServiceImpl: @Service;
    
    declare @type: BaseNodeServiceImpl: @Transactional;
    
    public long BaseNodeServiceImpl.countAllBaseNodes() {
        return BaseNode.countBaseNodes();
    }
    
    public void BaseNodeServiceImpl.deleteBaseNode(BaseNode baseNode) {
        baseNode.remove();
    }
    
    public BaseNode BaseNodeServiceImpl.findBaseNode(String id) {
        return BaseNode.findBaseNode(id);
    }
    
    public List<BaseNode> BaseNodeServiceImpl.findAllBaseNodes() {
        return BaseNode.findAllBaseNodes();
    }
    
    public List<BaseNode> BaseNodeServiceImpl.findBaseNodeEntries(int firstResult, int maxResults) {
        return BaseNode.findBaseNodeEntries(firstResult, maxResults);
    }
    
    public void BaseNodeServiceImpl.saveBaseNode(BaseNode baseNode) {
        baseNode.persist();
    }
    
    public BaseNode BaseNodeServiceImpl.updateBaseNode(BaseNode baseNode) {
        return baseNode.merge();
    }
    
}
