package org.crank.crud.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PersistedFile {

    @Column (length=200000, name="fileBytes")    
    protected byte[] bytes;
    @Column (name="fileName")    
    protected String name;
    @Column (name="fileContentType")    
    protected  String contentType;

    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes( byte[] bytes ) {
        this.bytes = bytes;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }
    public String getName() {
        return name;
    }
    public void setName( String name ) {
        this.name = name;
    }
}
