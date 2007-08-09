package org.crank.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PersistedFile implements Serializable {
    
    @Column (length=100000)
    private byte [] bytes;
    private String mimeType;
    private String fileName;

    public PersistedFile( final byte[] bytes, final String mimeType,
            final String fileName ) {
        super();
        this.bytes = bytes;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    public PersistedFile() {
        
    }

    
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes( byte[] bytes ) {
        this.bytes = bytes;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType( String mimeType ) {
        this.mimeType = mimeType;
    }

}
