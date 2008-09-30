package org.crank.crud.controller.support.tomahawk;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FileUploadHandler;
import org.crank.crud.model.PersistedFile;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class TomahawkFileUploadHandler implements FileUploadHandler {

    private String markerString = "uploadFile_";
    private final String dot = "."; 
    private String  bytesProperty ="bytes";
    private String nameProperty = "name";
    private String contentTypeProperty = "contentType";
    private Class<?> fileClass = PersistedFile.class;

	@SuppressWarnings("unchecked")
	public void upload( CrudOperations crudOperations ) {
          BeanWrapper wrapper = new BeanWrapperImpl(crudOperations.getEntity());
          Map<String, Object> dynamicProperties = crudOperations.getDynamicProperties();
          
          for(Map.Entry<String, Object> entry: dynamicProperties.entrySet()) {
               if (entry.getKey().startsWith( markerString )) {
                   extractFileInfo( wrapper, entry.getKey().substring( markerString.length() ), (UploadedFile) entry.getValue() );
               }
          }
    }

    private void extractFileInfo( BeanWrapper wrapper, String propertyName, UploadedFile uploadFile ) {
           if (uploadFile == null) {
               return;
           }
           Object propertyValue = initFilePropertyIfNeeded( wrapper, propertyName );
           if (propertyValue instanceof PersistedFile) {
               extractInfoNormal( propertyName, uploadFile, (PersistedFile) propertyValue );
           }  else {
               extactInfoDynamic( wrapper, propertyName, uploadFile );
           }
    }

    private void extactInfoDynamic( BeanWrapper wrapper, String propertyName, UploadedFile uploadFile ) {
        try {
               wrapper.setPropertyValue( propertyName + dot + bytesProperty , uploadFile.getBytes() );
           } catch (IOException e) {
               throw new RuntimeException("Unable to set bytes for property " + propertyName, e);
           }
           wrapper.setPropertyValue( propertyName + dot + nameProperty, new File(uploadFile.getName()).getName());
           wrapper.setPropertyValue( propertyName + dot + contentTypeProperty, uploadFile.getContentType());
    }

    private void extractInfoNormal( String propertyName, UploadedFile uploadFile, PersistedFile file  ) {
           try {
               file.setBytes( uploadFile.getBytes() );
           } catch (IOException e) {
               throw new RuntimeException("Unable to set bytes for property " + propertyName, e);
           }
           file.setContentType( uploadFile.getContentType() );
           file.setName( new File(uploadFile.getName()).getName() );
    }

    private Object initFilePropertyIfNeeded( BeanWrapper wrapper, String propertyName ) {
        Object propertyValue = wrapper.getPropertyValue( propertyName );
           if (propertyValue == null) {
               try {
                   propertyValue = fileClass.newInstance();
                   wrapper.setPropertyValue( propertyName, propertyValue );
                } catch (Exception ex) {
                    throw new RuntimeException("Can't create " + propertyName + " of type " + fileClass, ex);
                }
           }
        return propertyValue;
    }
    
    public void setMarkerString( String markerString ) {
        this.markerString = markerString;
    }

    public void setBytesProperty( String bytesProperty ) {
        this.bytesProperty = bytesProperty;
    }

    public void setContentTypeProperty( String contentTypeProperty ) {
        this.contentTypeProperty = contentTypeProperty;
    }

    public void setNameProperty( String nameProperty ) {
        this.nameProperty = nameProperty;
    }

    public void setFileClass( Class<?> fileClass ) {
        this.fileClass = fileClass;
    }

}
