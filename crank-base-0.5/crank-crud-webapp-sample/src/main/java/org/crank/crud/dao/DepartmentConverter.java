package org.crank.crud.dao;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.crank.crud.GenericDao;
import org.crank.crud.jsf.support.EntityConverter;
import org.crank.web.jsf.support.JSFSpringApplicationContextFinder;
import org.springframework.context.ApplicationContext;

public class DepartmentConverter extends EntityConverter {

    public DepartmentConverter () {
    }
    @SuppressWarnings("unchecked")
    void initDao () {
        ApplicationContext applicationContext = JSFSpringApplicationContextFinder.getApplicationContext();
        GenericDao dao = ((Map<String, GenericDao>) applicationContext.getBean( "repositories" )).get( "Department" );
        this.setDao( dao );        
    }

    @Override
    public Object getAsObject( FacesContext facesContext, UIComponent component, String value ) {
        System.out.println("AsObject VALUE " + value );
        initDao();
        Object object = super.getAsObject( facesContext, component, value );
        System.out.println("AsObject OBJECT " + object);
        return object;
    }

    @Override
    public String getAsString( FacesContext facesContext, UIComponent component, Object value ) {
        System.out.println("AsString VALUE " + value );
        initDao();
        String string = super.getAsString( facesContext, component, value );
        System.out.println("AsString OBJECT " + string);
        return string;

    }
}
