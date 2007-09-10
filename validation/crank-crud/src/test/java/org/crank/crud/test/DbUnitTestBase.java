package org.crank.crud.test;

import java.io.InputStream;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.testng.annotations.BeforeClass;

/**
*
*  @version $Revision:$
*  @author Rick Hightower
*/
public abstract class DbUnitTestBase extends SpringTestNGBase {

    protected EntityManagerFactory entityManagerFactory;
    private IDatabaseTester databaseTester;
    protected final Logger log = Logger.getLogger( this.getClass() );

    private IDataSet loadDataSet( IDatabaseConnection connection ) throws Exception {
        // load the data set
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream stream = cl.getResource( getDataSetXml() ).openStream();

        IDataSet data;

        if (getUseFlatXmlDataSet()) {
            data = new FlatXmlDataSet( stream );
        } else {
            data = new XmlDataSet( stream );
        }

        // order the data set
        DatabaseSequenceFilter filter = new DatabaseSequenceFilter( connection, data.getTableNames() );
        return new FilteredDataSet( filter, data );
    }

    @BeforeClass
    protected void initPersistenceStuff() throws Exception {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            Session hibernateSession = (Session) em.getDelegate();
            Connection conn = hibernateSession.connection();
            IDatabaseConnection dbunitConn = new DatabaseConnection( conn );
            IDataSet dataSet = loadDataSet( dbunitConn );
            databaseTester = new DefaultDatabaseTester( dbunitConn );
            databaseTester.setDataSet( dataSet );
            databaseTester.setSetUpOperation( DatabaseOperation.CLEAN_INSERT );
            databaseTester.onSetup();
        } catch (Exception ex) {
            log.debug( "Exception in initializing database", ex );
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory( EntityManagerFactory vmcBizEntityManagerFactory ) {
        this.entityManagerFactory = vmcBizEntityManagerFactory;
    }

    protected boolean getUseFlatXmlDataSet() {
        return true;
    }

    public abstract String getDataSetXml();

}
