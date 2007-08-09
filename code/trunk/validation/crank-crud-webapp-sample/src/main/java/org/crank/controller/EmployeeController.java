package org.crank.controller;

import java.io.IOException;
import java.io.Serializable;

import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.CrudOutcome;
import org.crank.crud.controller.CrudState;
import org.crank.crud.model.Employee;
import org.crank.crud.model.PersistedFile;
import org.apache.myfaces.custom.fileupload.UploadedFile;


public class EmployeeController implements CrudOperations {
    
    private CrudController controller;
    private UploadedFile uploadFile;

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile( UploadedFile uploadFile ) {
        this.uploadFile = uploadFile;
    }

    public CrudController getController() {
        return controller;
    }

    public void setController( CrudController controller ) {
        this.controller = controller;
    }

    public CrudOutcome cancel() {
        return controller.cancel();
    }

    public CrudOutcome create() {
        initFile();
        return controller.create();
    }
    

    private void initFile() {
        Employee employee = (Employee) this.getEntity();
        if (employee.getFile()==null) {
            employee.setFile( new PersistedFile() );
        }
        try {
            employee.getFile().setBytes( uploadFile.getBytes() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        employee.getFile().setFileName( uploadFile.getName() );
        employee.getFile().setMimeType( uploadFile.getContentType() );
    }

    public CrudOutcome delete() {
        return controller.delete();
    }

    public Serializable getEntity() {
        return controller.getEntity();
    }

    public CrudOutcome loadCreate() {
        return controller.loadCreate();
    }

    public CrudOutcome read() {
        return controller.read();
    }

    public CrudOutcome update() {
        initFile();
        return controller.update();
    }

    public Class getEntityClass() {
        return controller.getEntityClass();
    }

    public String getName() {
        return controller.getName();
    }

    public CrudState getState() {
        return controller.getState();
    }
    
}
