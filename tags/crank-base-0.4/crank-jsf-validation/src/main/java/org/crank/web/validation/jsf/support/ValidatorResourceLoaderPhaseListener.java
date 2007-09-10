package org.crank.web.validation.jsf.support;

import java.io.OutputStreamWriter;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import org.crank.annotations.design.Implements;
import org.crank.annotations.design.NeedsRefactoring;
import org.crank.annotations.design.OptionalInjection;
import org.crank.web.validation.ClientValidatorConstants;
import org.crank.web.validation.ValidationScriptReaper;

/**
 * Phaselistener to load resource from validation reaper.
 * 
 * @author Rick Hightower
 */
public class ValidatorResourceLoaderPhaseListener implements PhaseListener {

    /** Holds the validationScriptReaper which this class delegates to. */
    private ValidationScriptReaper validationScriptReaper = 
        new ValidationScriptReaper();

    /** Servers up the resource if the resource id matches. */
    @Implements(interfaceClass = PhaseListener.class)
    public void afterPhase(PhaseEvent event) {
        String rootId = event.getFacesContext().getViewRoot().getViewId();

        if (rootId.indexOf(ClientValidatorConstants.VALIDATOR_RESOURCE_VIEW_ID) != -1) {
            serveResource(event);
        }
    }

    /**
     * Serves up the resource.
     * 
     * @param event
     */
    @NeedsRefactoring("Should we cache this since it won't change?")
    private void serveResource(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();

        HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();

        try {
            response.setContentType("text/javascript");
            response.setStatus(200);
            OutputStreamWriter outputStreamWriter = 
                new OutputStreamWriter(response
                    .getOutputStream(), response.getCharacterEncoding());
            /* Delegate to the reaper. */
            validationScriptReaper.outputBaseValidationScritps(outputStreamWriter);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            facesContext.responseComplete(); // Stop the jsf lifecycle
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    /**
     * NO OP.
     */
    @Implements(interfaceClass = PhaseListener.class)
    public void beforePhase(PhaseEvent phaseEvent) {
        // Do nothing here
    }

    /** Only listent to the restore view phase. */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    /**
     * Allow another reaper to be injected. This is mostly for testing as we
     * don't envision a replacement reaper.
     * 
     * @param validationScriptReaper
     */
    @OptionalInjection
    public void setValidationScriptReaper(ValidationScriptReaper 
            validationScriptReaper) {
        this.validationScriptReaper = validationScriptReaper;
    }
}