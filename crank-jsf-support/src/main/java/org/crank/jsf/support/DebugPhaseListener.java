package org.crank.jsf.support;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

public class DebugPhaseListener implements PhaseListener{
    
    protected PrintStream debug = System.out;
    
    public void afterPhase( PhaseEvent event ) {
        debug.printf( "------- AFTER %s --------- \n\n\n\n\n ", event.getPhaseId());
        if (event.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES) {
            debug.printf( "-------  Printing components --------- \n\n\n\n\n ");            
            printComps(FacesContext.getCurrentInstance().getViewRoot());
        }
        
    }

    public void beforePhase( PhaseEvent event ) {
        debug.printf( "\n\n\n\n\n-------BEFORE %s--------- ", event.getPhaseId());
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            printMap("ApplicationMap", FacesContext.getCurrentInstance().getExternalContext().getApplicationMap());
            printMap("SessionMap", FacesContext.getCurrentInstance().getExternalContext().getSessionMap());
            printMap("RequestMap", FacesContext.getCurrentInstance().getExternalContext().getRequestMap());
            printMap2("RequestParameterMap", FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap());
        }

    }


    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    } 
    
    private void printMap( String title, Map<String, Object> applicationMap ) {
        debug.printf( "-------  %s --------- \n\n\n\n\n ", title);
        Set<Entry<String, Object>> entries = applicationMap.entrySet();
        for (Map.Entry<String, Object> entry : entries){
            debug.printf( "%s=%s\n", entry.getKey(), entry.getValue().toString() );
        }

    }
    private void printMap2( String title, Map<String, String> map ) {
        debug.printf( "-------  %s --------- \n\n\n\n\n ", title);
        Set<Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries){
            debug.printf( "%s=%s\n", entry.getKey(), entry.getValue().toString() );
        }
    }

    private void printComps(UIComponent parent) {
        List<UIComponent> list = parent.getChildren();
        for (UIComponent comp : list) {
            if (comp instanceof UIInput) {
                UIInput inputComp = (UIInput)comp;
                try {
                    debug.printf("UIInput: id=%s submittedValue=%s value=%s \n", comp.getClientId(FacesContext.getCurrentInstance()),
                            inputComp.getSubmittedValue(), inputComp.getValue() ) ;
                }catch (Exception ex) {
                    debug.printf("UIInput: id=%s submittedValue=%s \n", comp.getClientId(FacesContext.getCurrentInstance()),
                            inputComp.getSubmittedValue()) ;
                }
            } else {
                printComps(comp);
            }
        }

        Map<String, UIComponent> componentMap = parent.getFacets();
        for (Map.Entry<String, UIComponent> entry : componentMap.entrySet()) {
            String key = entry.getKey();
            UIComponent comp = entry.getValue();
            if  (comp instanceof UIInput) {
                UIInput inputComp = (UIInput)comp;
                try {
                    debug.printf("UIInput: facetName=%s id=%s submittedValue=%s value=%s \n", key, comp.getClientId(FacesContext.getCurrentInstance()),
                            inputComp.getSubmittedValue(), inputComp.getValue() ) ;
                } catch (Exception ex) {
                    debug.printf("UIInput: facetName=%s id=%s submittedValue=%s \n", key, comp.getClientId(FacesContext.getCurrentInstance()),
                            inputComp.getSubmittedValue()) ;
                }
            } else {
                debug.printf("Prinintg Facet %s of %s\n", key, parent.getClientId(FacesContext.getCurrentInstance()));
                printComps(comp);
            }

        }
    }


}
