// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

privileged aspect BaseNode_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager BaseNode.entityManager;
    
    public static final EntityManager BaseNode.entityManager() {
        EntityManager em = new BaseNode().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long BaseNode.countBaseNodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BaseNode o", Long.class).getSingleResult();
    }
    
    public static List<BaseNode> BaseNode.findAllBaseNodes() {
        return entityManager().createQuery("SELECT o FROM BaseNode o", BaseNode.class).getResultList();
    }
    
    public static BaseNode BaseNode.findBaseNode(String sysUID) {
        if (sysUID == null || sysUID.length() == 0) return null;
        return entityManager().find(BaseNode.class, sysUID);
    }
    
    public static List<BaseNode> BaseNode.findBaseNodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BaseNode o", BaseNode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void BaseNode.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void BaseNode.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BaseNode attached = BaseNode.findBaseNode(this.sysUID);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void BaseNode.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void BaseNode.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public BaseNode BaseNode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BaseNode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
