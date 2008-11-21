package org.crank.crud.controller.support.tomahawk;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudOutcome;
import org.crank.crud.model.PersistedFile;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class TomahawkFileUploadHandlerTest {

    @SuppressWarnings("unchecked")
	private CrudControllerBase crudControllerBase;
    private TomahawkFileUploadHandler fileUploadHandler;
   
    @BeforeMethod
    protected void setUp() throws Exception {
        
        crudControllerBase = new CrudControllerBaseMock();         
        fileUploadHandler = new TomahawkFileUploadHandler();
        crudControllerBase.getDynamicProperties().clear();
        
    }

    @SuppressWarnings("unchecked")
    @Test()
    public void testUpload() {
        crudControllerBase.getDynamicProperties().put( "uploadFile_file",  new UpFile());

        fileUploadHandler.upload( crudControllerBase );

        PersistedFile file = ((Entity)crudControllerBase.getEntity()).getFile();
        assertNotNull(file);
        assertEquals( file.getName(), "bar.txt" );
        assertEquals( file.getContentType(), "funsun" );
        assertEquals( file.getBytes()[0], 0);
    }
    
    @SuppressWarnings("unchecked")
    @Test()
    public void testUpload1() {
        fileUploadHandler.setFileClass( FileObject.class );
        fileUploadHandler.setContentTypeProperty( "mimeType" );
        fileUploadHandler.setNameProperty( "fileName" );
        fileUploadHandler.setBytesProperty( "someBytes" );
        crudControllerBase.getDynamicProperties().put( "uploadFile_fileObject",  new UpFile());

        fileUploadHandler.upload( crudControllerBase );
        
        FileObject file = ((Entity)crudControllerBase.getEntity()).getFileObject();
        assertNotNull(file);
        assertEquals( file.getFileName(), "bar.txt" );
        assertEquals( file.getMimeType(), "funsun" );
        assertEquals( file.getSomeBytes()[0], 0);
    }

    public static class Entity implements Serializable{
        PersistedFile file;
        FileObject fileObject;

        public FileObject getFileObject() {
            return fileObject;
        }

        public void setFileObject( FileObject fileObject ) {
            this.fileObject = fileObject;
        }

        public PersistedFile getFile() {
            return file;
        }

        public void setFile( PersistedFile file ) {
            this.file = file;
        }
        
    }
    
    
    
    class UpFile implements UploadedFile{

        public byte[] getBytes() throws IOException {
            return new byte[] {0,1,2,3,4,5};
        }

        public String getContentType() {
            return "funsun";
        }

        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        public String getName() {
            return "//foo//bar.txt";
        }

        public long getSize() {
            return 0;
        }
        
    }

    @SuppressWarnings("unchecked")
    class CrudControllerBaseMock extends CrudControllerBase{
        
        CrudControllerBaseMock () {
            this.entity = new Entity();
        }

        @Override
        protected CrudOutcome doCreate() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected CrudOutcome doUpdate() {
            // TODO Auto-generated method stub
            return null;
        }

        public CrudOutcome cancel() {
            // TODO Auto-generated method stub
            return null;
        }

        public CrudOutcome delete() {
            // TODO Auto-generated method stub
            return null;
        }

        public CrudOutcome deleteSelected() {
            // TODO Auto-generated method stub
            return null;
        }

        public CrudOutcome loadCreate() {
            // TODO Auto-generated method stub
            return null;
        }

        public CrudOutcome read() {
            // TODO Auto-generated method stub
            return null;
        }

		@Override
		protected CrudOutcome doCancel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected CrudOutcome doDelete() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected CrudOutcome doRead() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected CrudOutcome doLoadCreate() {
			// TODO Auto-generated method stub
			return null;
		}

		public List getSelectedEntities() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected CrudOutcome doLoadListing() {
			// TODO Auto-generated method stub
			return null;
		}
    } 
    
}
