package org.crank.web.validation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crank.core.CrankConstants;
import org.crank.core.CrankContext;
import org.crank.core.Log;
import org.crank.core.ObjectRegistry;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;

/**
 * AjaxValidationServlet allows Ajax calls to our validation 
 * framework.
 * It looks up validation rules and executes them. 
 * The client passes information
 * about the value, type and validator rule name, then this servlet
 * executes the rule and returns an output string signifying the success
 * or failure of the validation.
 * <br /> <br />
 * <b>Input</b> <br />
 * This servlet expects three request parameters:
 *  <ul>
 *  <li><code>validator</code> -- Name of the validator rule we want to execute. </li>
 *  <li><code>value</code> -- The actual value from the server. </li>
 *  <li><code>type</code> -- Type is optional if blank we assume java.lang.String.
 *  If set, we attempt a conversion.</li>
 *  </ul>
 * 
 * This Servlet uses the ObjectRegistry to look up the validation rule.
 * It also uses the ObjectRegistry to convert the string to a <code>type</code>.
 * <br />
 * The default ObjectRegistry uses Spring's conversion support to convert from a 
 * string to another type.
 * <br /><br />
 * <b>Design Note:</b> <br /> 
 * We decided our needs are basic enough to do the Ajax support with a Servlet.
 * <br /><br />
 * <b>Ouput</b> <br />
 * This Servlet returns the string "valid" as output if the validation was valid.
 * It returns "NOT VALID" if the validation was not valid.
 * @author Rick Hightower
 *
 */
public class AjaxValidationServlet extends HttpServlet {
    private static Log log = Log.getLog(AjaxValidationServlet.class);

    private static ObjectRegistry objectRegistry;


    /**
     * Handle an AJAX request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            /* Holds the name of the validator rule. */
            String validatorName = request.getParameter("validator");

            /* Holds the value of the field that the client wants validated. */
            String value = request.getParameter("value");

            /* Holds the type of the field. */
            String type = request.getParameter("type");

            /* Find the validation rule and excute it with the information
             * we took out of the request parameters. 
             */
            findAndExecuteValidationRule(response.getWriter(), validatorName, value, type);
        } catch (Exception ex) {
            log.handleExceptionError("Trying to lookup valiator", ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Find the validation rule and excutes it.
     * @param writer
     * @param validatorName
     * @param value
     * @param type
     * @throws Exception
     */
    private void findAndExecuteValidationRule(PrintWriter writer, String validatorName,
            String value, String type) throws Exception {
        /* Look up the validator in the object registry. */
        FieldValidator validator = lookupValidatorInRegistry(validatorName);
        log.info("Found validator " + validator);

        /* Convert the object if the type is specified. */
        Object oValue = null;
        if (type == null || "".equals(type.trim())) {
            oValue = value;
        } else {
            oValue = convertIfNeeded(value, type);
        }

        /* Actually validated the field. */
        ValidatorMessage message = (ValidatorMessage) validator.validate(oValue, "none");
        if (!message.hasError()) {
            log.info("VALID");
            writer.print("valid");
        } else {
            log.info("NOT VALID");
            writer.print("NOT VALID");
        }
    }

    /**
     * Converts the value if the type is set.
     * @param value
     * @param type
     * @return
     */
    private Object convertIfNeeded(Object value, String type) {
        Class <?> clazz = null;
        try {
            Class.forName(type);
        } catch (Exception ex) {
            log.handleExceptionError("Type sent was not found:" + type, ex);
        }
        return objectRegistry.convertObject(value, clazz);
    }

    /**
     * This method looks up the validator in the registry.
     * 
     * 
     * @param validator
     *            The name of the validator that we are looking up.
     * 
     * @return
     */
    private FieldValidator lookupValidatorInRegistry(String validator) {
        ObjectRegistry applicationContext = CrankContext.getObjectRegistry();

        FieldValidator fvalidator = (FieldValidator) applicationContext.getObject(
                CrankConstants.FRAMEWORK_PREFIX + CrankConstants.FRAMEWORK_DELIM
                        + "validator" + CrankConstants.FRAMEWORK_DELIM + validator,
                FieldValidator.class);
        return fvalidator;
    }

    @Override
    public void init() throws ServletException {
        super.init();

        objectRegistry = CrankContext.getObjectRegistry();
    }

    public AjaxValidationServlet() {
    }
    
}
