// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.yaio.core.node;

import de.yaio.core.datadomain.ResContentData.UploadWorkflowState;
import de.yaio.core.datadomain.ResIndexData.IndexWorkflowState;


privileged aspect UrlResNode_Roo_JavaBean {
    
    public String UrlResNode.getResLocRef() {
        return this.resLocRef;
    }
    
    public void UrlResNode.setResLocRef(String resLocRef) {
        this.resLocRef = resLocRef;
    }
    
    public String UrlResNode.getResLocName() {
        return this.resLocName;
    }
    
    public void UrlResNode.setResLocName(String resLocName) {
        this.resLocName = resLocName;
    }
    
    public String UrlResNode.getResLocTags() {
        return this.resLocTags;
    }
    
    public void UrlResNode.setResLocTags(String resLocTags) {
        this.resLocTags = resLocTags;
    }
    
    public String UrlResNode.getResContentMime() {
        return this.resContentMime;
    }

    public void UrlResNode.setResContentMime(String resContentMime) {
        this.resContentMime = resContentMime;
    }

    public Long UrlResNode.getResContentSize() {
        return this.resContentSize;
    }

    public void UrlResNode.setResContentSize(Long resContentSize) {
        this.resContentSize = resContentSize;
    }

    public String UrlResNode.getResContentDMSId() {
        return this.resContentDMSId;
    }

    public void UrlResNode.setResContentDMSId(String resContentDMSId) {
        this.resContentDMSId = resContentDMSId;
    }

    public String UrlResNode.getResContentDMSType() {
        return this.resContentDMSType;
    }

    public void UrlResNode.setResContentDMSType(String resContentDMSType) {
        this.resContentDMSType = resContentDMSType;
    }

    public UploadWorkflowState UrlResNode.getResContentDMSState() {
        return this.resContentDMSState;
    }

    public void UrlResNode.setResContentDMSState(UploadWorkflowState resContentDMSState) {
        this.resContentDMSState = resContentDMSState;
    }

    public String UrlResNode.getResIndexDMSId() {
        return this.resContentIndexDMSId;
    }

    public void UrlResNode.setResIndexDMSId(String resContentIndexDMSId) {
        this.resContentIndexDMSId = resContentIndexDMSId;
    }

    public String UrlResNode.getResIndexDMSType() {
        return this.resContentIndexDMSType;
    }

    public void UrlResNode.setResIndexDMSType(String resContentIndexDMSType) {
        this.resContentIndexDMSType = resContentIndexDMSType;
    }

    public IndexWorkflowState UrlResNode.getResIndexDMSState() {
        return this.resContentIndexDMSState;
    }

    public void UrlResNode.setResIndexDMSState(IndexWorkflowState resContentIndexDMSState) {
        this.resContentIndexDMSState = resContentIndexDMSState;
    }
}
