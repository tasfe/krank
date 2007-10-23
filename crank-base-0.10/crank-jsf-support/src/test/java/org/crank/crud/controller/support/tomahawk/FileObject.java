package org.crank.crud.controller.support.tomahawk;

public class FileObject {
    private String fileName;
    private String mimeType;
    private byte[] someBytes;
    public FileObject () {
        
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
    public byte[] getSomeBytes() {
        return someBytes;
    }
    public void setSomeBytes( byte[] someBytes ) {
        this.someBytes = someBytes;
    }

}
