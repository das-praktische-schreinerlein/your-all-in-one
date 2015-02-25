// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import de.yaio.core.node.EventNode;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EventNode_Roo_Jpa_ActiveRecord {
    
    public static long EventNode.countEventNodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EventNode o", Long.class).getSingleResult();
    }
    
    public static List<EventNode> EventNode.findAllEventNodes() {
        return entityManager().createQuery("SELECT o FROM EventNode o", EventNode.class).getResultList();
    }
    
    public static EventNode EventNode.findEventNode(String sysUID) {
        if (sysUID == null || sysUID.length() == 0) return null;
        return entityManager().find(EventNode.class, sysUID);
    }
    
    public static List<EventNode> EventNode.findEventNodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EventNode o", EventNode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public EventNode EventNode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EventNode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
