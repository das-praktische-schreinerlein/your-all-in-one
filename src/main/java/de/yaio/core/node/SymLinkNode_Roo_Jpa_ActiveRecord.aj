// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

privileged aspect SymLinkNode_Roo_Jpa_ActiveRecord {
    
    public static long SymLinkNode.countSymLinkNodes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SymLinkNode o", Long.class).getSingleResult();
    }
    
    public static List<SymLinkNode> SymLinkNode.findAllSymLinkNodes() {
        return entityManager().createQuery("SELECT o FROM SymLinkNode o", SymLinkNode.class).getResultList();
    }
    
    public static SymLinkNode SymLinkNode.findSymLinkNode(String sysUID) {
        if (sysUID == null || sysUID.length() == 0) return null;
        return entityManager().find(SymLinkNode.class, sysUID);
    }
    
    public static List<SymLinkNode> SymLinkNode.findSymLinkNodeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SymLinkNode o", SymLinkNode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public SymLinkNode SymLinkNode.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SymLinkNode merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
