// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import de.yaio.core.node.InfoNode;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect InfoNode_Roo_Jpa_ActiveRecord {
    
    public static long InfoNode.countInfoNodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InfoNode o", Long.class).getSingleResult();
    }
    
    public static List<InfoNode> InfoNode.findAllInfoNodes() {
        return entityManager().createQuery("SELECT o FROM InfoNode o", InfoNode.class).getResultList();
    }
    
    public static InfoNode InfoNode.findInfoNode(String sysUID) {
        if (sysUID == null || sysUID.length() == 0) return null;
        return entityManager().find(InfoNode.class, sysUID);
    }
    
    public static List<InfoNode> InfoNode.findInfoNodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InfoNode o", InfoNode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public InfoNode InfoNode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        InfoNode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
