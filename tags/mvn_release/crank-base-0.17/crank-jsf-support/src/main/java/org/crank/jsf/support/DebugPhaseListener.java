package org.crank.jsf.support;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class DebugPhaseListener implements PhaseListener{
    
    protected PrintStream debug = System.out;
    
    public void afterPhase( PhaseEvent event ) {
        debug.printf( "------- AFTER %s --------- \n\n\n\n\n ", event.getPhaseId());
        
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

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    } 
    

}
