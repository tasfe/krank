package org.crank.web.validation.spring.support;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crank.annotations.design.NeedsRefactoring;
import org.crank.annotations.design.OptionalInjection;
import org.crank.web.validation.ClientValidatorConstants;
import org.crank.web.validation.ValidationScriptReaper;

/**
 * HttpServlet to load resource from validation reaper.
 * 
 * @author Rick Hightower
 */
@SuppressWarnings("serial")
public class ValidatorResourceLoaderServlet extends HttpServlet  {

    /** Holds the validationScriptReaper which this class delegates to. */
    private ValidationScriptReaper validationScriptReaper = 
        new ValidationScriptReaper();

    /**
     * Serves up the resource.
     * 
     * @param event
     */
    @NeedsRefactoring("Should we cache this since it won't change?")
    private void serveResource(final HttpServletResponse response) {

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
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
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

	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		
        if (request.getRequestURI().indexOf(ClientValidatorConstants.VALIDATOR_RESOURCE_VIEW_ID) != -1) {
            serveResource(response);
        }
	}
}