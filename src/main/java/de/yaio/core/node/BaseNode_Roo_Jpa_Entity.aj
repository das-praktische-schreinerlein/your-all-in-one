// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import de.yaio.core.node.BaseNode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect BaseNode_Roo_Jpa_Entity {
    
    declare @type: BaseNode: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long BaseNode.id;
    
    @Version
    @Column(name = "version")
    private Integer BaseNode.version;
    
    public Long BaseNode.getId() {
        return this.id;
    }
    
    public void BaseNode.setId(Long id) {
        this.id = id;
    }
    
    public Integer BaseNode.getVersion() {
        return this.version;
    }
    
    public void BaseNode.setVersion(Integer version) {
        this.version = version;
    }
    
}
