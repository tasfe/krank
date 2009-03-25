package org.crank.web.validation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.annotations.design.OptionalInjection;
import org.crank.core.CrankConstants;
import org.crank.core.CrankContext;
import org.crank.core.Log;
import org.crank.core.ObjectRegistry;
import org.crank.validation.ValidatorMetaData;
import org.crank.validation.ValidatorMetaDataReader;
import org.crank.web.contribution.Contribution;
import org.crank.web.contribution.SimpleContributionSupport;

/**
 * This class collects JavaScript validation scripts based on meta-data it
 * retrieves about properties.
 * 
 * This class also aids in outputing a JavaScript validation library as a web
 * resource that the browser can cache.
 * 
 * Then it outputs calls to the actual JavaScript validaiton library based on
 * the meta-data of the JavaBean properties that correspond to the form fields.
 * 
 * We made the bulk of the validation script collection JSF agnostic so it would
 * be easy to use with other frameworks.
 * 
 * This is the backing class to some delivery mechanism.
 * 
 * For JSF the delivery mechanism is a JSF component and a PhaseListener.
 * 
 * For JSP/Spring MVC the delivery mechanism would be a custom tag.
 * 
 * @author Rick Hightower
 * 
 */
public class ValidationScriptReaper {

    private static Log log = Log.getLog(ValidationScriptReaper.class);
    /**
     * This class uses the objectRegistry to look up validation contributions.
     * The ObjectRegistry is an abstraction that knows how to talk to an IoC
     * container like Spring, HiveMind, Plexus, Pico, etc.
     */
    private ObjectRegistry objectRegistry;

    /**
     * This classes uses the ValidatorMetaDataReader to read validation
     * meta-data for properties. It uses this data to reap scripts.
     */
    private ValidatorMetaDataReader validatorMetaDataReader;

    /**
     * Holds a reference to the script contribution that is the main JavaScript
     * library.
     */
    private String baseValidationScriptContributionName = "jsValidationRules";

    /**
     * This was added to make the JSF style id names optional. By default we
     * assume JSF style id names. If we were to port this to work with another
     * framework, we would want to flip this bit when using it with said other
     * framework.
     */
    private boolean appendFormNameToProperty = true;

    /**
     * Should we prefix the property name to generate the HTML id? If so we need
     * to set this to something other than ":". ":" is JSF friendly.
     */
    private String propertyPrefix = ":";

    /**
     * Should we append a string to the JavaBean property name to generate the
     * HTML ID? If so we need to set this. It defaults to "", i.e., no postfix.
     */
    private String propertyPostfix = "";

    /**
     * This method outputs the base validation scripts. The base validation
     * scripts are typically the JavaScript validation libraries.
     * 
     * It is easy to write user defined library extentions and have them
     * included in the output.
     * 
     * The developer would just register extentions (e.g., date validation) to
     * the IoC container and this method will find the library extentions and
     * automatically output them.
     * 
     * JSF Design Note: This method gets called by the PhaseListener to deliver
     * up the JavaScript library. This class and this method is not tied to JSF.
     * 
     * @param writer
     * @throws IOException
     */
    public void outputBaseValidationScritps(Writer writer) throws IOException {
        /* Output the base library. */
        outputBaseContributionIfFound(writer);
        /* Output user defined contributions. */
        outputAllValidatorContributionFoundInRegistry(writer);
    }

    /**
     * Outputs the field validation, i.e., the JavaScript validation rules based
     * on form fields which are based on JavaBean properties.
     * 
     * @param writer
     * @param clazz
     * @param propertyNames
     * @param formName
     * @throws IOException
     */
    public void outputFieldValidation(Writer writer, Class<?> clazz, 
            String[] propertyNames, String formName) throws IOException {
        /* Get the meta data from the class and property names. */
        Map<String, List<ValidatorMetaData>> validatorMetaData = 
            collectMetaDataFromClass(clazz, propertyNames);

        /*
         * Grab the CSS styles for the rule message divs and output them.
         */
        String encodeValidationStyleClasses = convertRuleName(
                "encodeValidationStyleClasses");

        SimpleContributionSupport styleContribution = (SimpleContributionSupport) 
                getObjectRegistry().getObject(encodeValidationStyleClasses);
        styleContribution.addToWriter(writer);

        outputValidFormFunction(writer, propertyNames, formName, validatorMetaData);
        
        ouputFieldValidationSupportFunctions(writer, propertyNames, formName, validatorMetaData);
        
    }

    private void ouputFieldValidationSupportFunctions(Writer writer, String[] propertyNames, String formName, Map<String, List<ValidatorMetaData>> validatorMetaData) throws IOException {
        /*
         * Grab the encodeValidateFieldSupport template so we can output the
         * validation rule support functions. This was added to support AJAX.
         */
        ValidatorTemplateContribution encodeValidateFieldSupportTemplateContribution 
        = grabContribution("encodeValidateFieldSupport");

        String validators = collectAllValidatorContributionsFunctionSupportByPropertyNames(
                propertyNames, validatorMetaData, formName);
        Map<String, Object> templateArguments = new HashMap<String, Object>();
        templateArguments.put("validators", validators);
        templateArguments.put("form", formName);

        /*
         * Build the validator context for the validation function calls
         * template contribution and pass the context to the tempalte, then
         * invoke the template. The template will output the JavaScript calls.
         */
        ValidatorContext validatorContext = new ValidatorContext();
        validatorContext.setValidationRuleMetaData(templateArguments);
        
        validatorContext.setValidationRuleMetaData(templateArguments);
        encodeValidateFieldSupportTemplateContribution
                .placeValidatorContext(validatorContext);
        encodeValidateFieldSupportTemplateContribution.addToWriter(writer);
    }

    private void outputValidFormFunction(Writer writer, String[] propertyNames, String formName, Map<String, List<ValidatorMetaData>> validatorMetaData) throws IOException {
        /*
         * Grab the encodeValidateFormFunction template so we can output the
         * validation rule function calls that we need.
         */
        ValidatorTemplateContribution validationFunctionCallsTemplateContribution = 
            grabContribution("encodeValidateFormFunction");

        
        /*
         * Get the actual function calls and store them in a string that we can
         * pass to the ValidatorContext that we will pass to the validation
         * function calls template contribution.
         */
        String validators = collectAllValidatorContributionsFunctionCallsByPropertyNames(
                propertyNames, validatorMetaData, formName);
        Map<String, Object> templateArguments = new HashMap<String, Object>();
        templateArguments.put("validators", validators);
        templateArguments.put("form", formName);

        /*
         * Build the validator context for the validation function calls
         * template contribution and pass the context to the tempalte, then
         * invoke the template. The template will output the JavaScript calls.
         */
        ValidatorContext validatorContext = new ValidatorContext();
        validatorContext.setValidationRuleMetaData(templateArguments);
        
        /* Call the template. */
        validationFunctionCallsTemplateContribution
                .placeValidatorContext(validatorContext);
        validationFunctionCallsTemplateContribution.addToWriter(writer);
    }

    private ValidatorTemplateContribution grabContribution(String name) {
        String encodeValidateFormFunction = 
            convertRuleName(name);
        ValidatorTemplateContribution validationFunctionCallsTemplateContribution = 
            (ValidatorTemplateContribution) getObjectRegistry().getObject(encodeValidateFormFunction);
        return validationFunctionCallsTemplateContribution;
    }

    private String collectAllValidatorContributionsFunctionSupportByPropertyNames(String[] propertyNames, Map<String, List<ValidatorMetaData>> validatorMetaData, String formName) {
        /* Holds the function calls that we have collected thus far. */
        StringWriter swriter = new StringWriter(1000);

        /*
         * Iterate over the propertyNames, extracting hte property validation
         * meta-data, and then using that meta-data to look up the write
         * JavaScript function call template. Then using that template to
         * actually output the call to the JavaScript function to the browser.
         */
        writeValidatorTemplatesForProperties("Support", propertyNames, validatorMetaData, formName, swriter);
        return swriter.toString();
    }

    /**
     * This method collects meta-data from a class given a set of properties.
     * 
     * @param clazz
     * @param propertyNames
     * @return
     */
    private Map<String, List<ValidatorMetaData>> collectMetaDataFromClass(Class<?> clazz,
            String[] propertyNames) {
        /*
         * Holds the meta-data we collect in a map, where the key is
         * propertyName and the value is the validation meta-data.
         */
        Map<String, List<ValidatorMetaData>> validatorMetaData = 
            new HashMap<String, List<ValidatorMetaData>>();

        for (String propertyName : propertyNames) {

            List<ValidatorMetaData> propertyValidatorData = validatorMetaDataReader
                    .readMetaData(clazz, propertyName);
            validatorMetaData.put(propertyName, propertyValidatorData);

        }
        return validatorMetaData;
    }

    /**
     * This method collects the validation function calls based on validation
     * meta-data from the property name.
     * 
     * @param propertyNames
     * @param validationMetaData
     * @param form
     * @return
     * @throws IOException
     */
    private String collectAllValidatorContributionsFunctionCallsByPropertyNames(
            String[] propertyNames,
            Map<String, List<ValidatorMetaData>> validationMetaData, String form)
            throws IOException {

        assert form != null;
        assert propertyNames != null;
        assert validationMetaData != null;

        /* Holds the function calls that we have collected thus far. */
        StringWriter swriter = new StringWriter(1000);

        /*
         * Iterate over the propertyNames, extracting hte property validation
         * meta-data, and then using that meta-data to look up the write
         * JavaScript function call template. Then using that template to
         * actually output the call to the JavaScript function to the browser.
         */
        writeValidatorTemplatesForProperties("", propertyNames, validationMetaData, form, swriter);
        return swriter.toString();
    }

    private void writeValidatorTemplatesForProperties(String suffix, String[] propertyNames, Map<String, List<ValidatorMetaData>> validationMetaData, String form, StringWriter swriter) {
    	//System.out.println(Arrays.asList(propertyNames));
        for (String propertyName : propertyNames) {
            List<ValidatorMetaData> propertyValidationMetaData = 
                validationMetaData.get(propertyName);
            for (ValidatorMetaData validatorMetaData : propertyValidationMetaData) {
                
                String template = lookupValidatorAndEncodeIt(form, suffix, propertyName,
                        validatorMetaData);
                swriter.write(template);
            }
        }
    }

    /**
     * This method looks up the validator and encodes it to the browser. This
     * method builds the tempalte context passing arguments from the validator
     * meta-data.
     * 
     * @param form
     * @param fieldPropertyName
     * @param validatorMetaData
     * @return
     */
    private String lookupValidatorAndEncodeIt(String form, String suffix,
            String fieldPropertyName, ValidatorMetaData validatorMetaData) {
        /*
         * Get the rule name based on the validation rule stored in the
         * validator meta-data.
         */
        String contributionName = validatorMetaData.getName()+suffix;
        String ruleName = convertRuleName(contributionName);

        /*
         * Look up the contribution in the IoC container.
         */
        ValidatorTemplateContribution contribution = 
            (ValidatorTemplateContribution) getObjectRegistry()
                .getObjectReturnNullIfMissing(ruleName);
        
        if (contribution==null) {
            return "";
        }

        /* Generate the tempalteArguments */
        Map<String, Object> templateArguments = new HashMap<String, Object>();

        /*
         * Pass all the arguments from the validation meta-data to this
         * template. This is everything that was stored in the annotation or all
         * the name/value pairs stored in the properties file.
         */
        templateArguments.putAll(validatorMetaData.getProperties());

        /* Add the form name to the template context. */
        templateArguments.put("form", form);

        /* Add the field name to the tempalte context. */
        if (appendFormNameToProperty) {
            templateArguments.put("fieldId", form + propertyPrefix + 
                    fieldPropertyName + propertyPostfix);
        } else {
            templateArguments.put("fieldId", propertyPrefix 
                    + fieldPropertyName + propertyPostfix);
        }

        /*
         * Add the id of the HTML element that displays the error message.
         */
        templateArguments.put("divId", fieldPropertyName + "Error");

        /*
         * Holds the output.
         */
        StringWriter writer = new StringWriter(100);

        /* Create the validator context and pass it this map. */
        ValidatorContext validatorContext = new ValidatorContext();
        validatorContext.setValidationRuleMetaData(templateArguments);
        validatorContext.setFieldName(fieldPropertyName);

        contribution.placeValidatorContext(validatorContext);

        try {
            contribution.addToWriter(writer);
        } catch (IOException ioe) {
            
            log.handleExceptionError(
                    "There was an issue outputting the contribution ", ioe);
            return ioe.getMessage();
        }

        return writer.toString();
    }

    /** Allows us to set a namespace for our validation rules. */
    private String convertRuleName(String name) {
        name = CrankConstants.FRAMEWORK_PREFIX + CrankConstants.FRAMEWORK_DELIM
                + "client/validator" + CrankConstants.FRAMEWORK_DELIM + name;
        return name;
    }

    /**
     * This method allows developers to easily extend the validation library
     * that gets output just by registering ClientScriptValidatorContribution in
     * the IoC container. We look them all up and output them all as part of the
     * library.
     * 
     * @param writer
     * @throws IOException
     */
    private void outputAllValidatorContributionFoundInRegistry(Writer writer)
            throws IOException {
        Object[] objects = getObjectRegistry().getObjectsByType(
                ClientScriptValidatorContribution.class);
        for (Object object : objects) {

            ClientScriptValidatorContribution contribution = 
                (ClientScriptValidatorContribution) object;
            contribution.addToWriter(writer);
        }
    }

    /**
     * This method outputs the base contribution if found.
     * 
     * @param writer
     */
    private void outputBaseContributionIfFound(Writer writer) {
        try {
            outputContributionName(writer, baseValidationScriptContributionName);
        } catch (Exception ex) {
            
            // this just means that they don't have a base... you should
            // log this as info.
            log.handleExceptionInfo("Don't have a base", ex);
        }
    }

    /**
     * This method outputs a contribution by name.
     * 
     * @param writer
     * @param name
     * @throws IOException
     */
    private void outputContributionName(Writer writer, String name) 
                                       throws IOException {
        Contribution contribution = 
            (Contribution) getObjectRegistry().getObject(name);
        contribution.addToWriter(writer);
    }

    /**
     * This method looks up the objectRegistry if it was not injected. This
     * allows us to override the evil singleton if needed. The singleton, which
     * is the main interface to the IoC container, can be replaced by injecting
     * another ObjectRegistry.
     * 
     * This could for example allow us to configure the validation rules in Pico
     * while still using Spring IoC for everthing else.
     * 
     * @return
     */
    private ObjectRegistry getObjectRegistry() {
        if (objectRegistry == null) {
            objectRegistry = CrankContext.getObjectRegistry();
        }
        return objectRegistry;
    }

    @ExpectsInjection
    public void setValidatorMetaDataReader(
            ValidatorMetaDataReader validatorMetaDataReader) {
        this.validatorMetaDataReader = validatorMetaDataReader;
    }

    @AllowsConfigurationInjection
    public void setBaseValidationScriptContributionName(
            String baseValidationScriptContributionName) {
        this.baseValidationScriptContributionName = 
            baseValidationScriptContributionName;
    }

    @OptionalInjection
    public void setObjectRegistry(ObjectRegistry registry) {
        this.objectRegistry = registry;
    }

    public String getPropertyPostfix() {
        return propertyPostfix;
    }

    public void setPropertyPostfix(String propertyPostfix) {
        this.propertyPostfix = propertyPostfix;
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void setPropertyPrefix(String propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }

    public boolean isAppendFormNameToProperty() {
        return appendFormNameToProperty;
    }

    public void setAppendFormNameToProperty(boolean appendFormNameToProperty) {
        this.appendFormNameToProperty = appendFormNameToProperty;
    }

}
