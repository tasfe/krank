package org.crank.core.spring.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.crank.crud.controller.CrudUtils;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;


/**
 * This class has the ability to obtain Spring-managed Converters,
 * Validators, and UIComponents in the case that they are not
 * configured in the JSF environment.
 * <p>
 * For all other behavior, this class delegates to the original
 * JSF Application implementation.
 * <p>
 * @author Rick Hightower
 */
@SuppressWarnings("deprecation")
public class SpringApplication extends Application {
    /**
     * Get logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Original application object (may be chained).
     */
    private Application originalApplication;

    /**
     * Creates a new SpringApplication object.
     */
    public SpringApplication() {
    }

    /**
     * Creates a new SpringApplication object.
     *
     * @param aOriginalApplication pass original (may be chained)
     */
    public SpringApplication(final Application aOriginalApplication) {
        this.originalApplication = aOriginalApplication;
    }

    /**
     * Delegate to original.
     *
     * @return ActionListner.
     */
    public ActionListener getActionListener() {
        return this.originalApplication.getActionListener();
    }

    /**
     * Delegate to original.
     *
     * @param listener Delegate to original.
     */
    public void setActionListener(final ActionListener listener) {
        this.originalApplication.setActionListener(listener);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public Locale getDefaultLocale() {
        return this.originalApplication.getDefaultLocale();
    }

    /**
     * Delegate to original.
     *
     * @param locale Delegate to original.
     */
    public void setDefaultLocale(final Locale locale) {
        this.originalApplication.setDefaultLocale(locale);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public String getDefaultRenderKitId() {
        return this.originalApplication.getDefaultRenderKitId();
    }

    /**
     * Delegate to original.
     *
     * @param renderKitId Delegate to original.
     */
    public void setDefaultRenderKitId(final String renderKitId) {
        this.originalApplication.setDefaultRenderKitId(renderKitId);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public String getMessageBundle() {
        return this.originalApplication.getMessageBundle();
    }

    /**
     * Delegate to original.
     *
     * @param bundle Delegate to original.
     */
    public void setMessageBundle(final String bundle) {
        this.originalApplication.setMessageBundle(bundle);
    }

    /**
     * Return our own navigation handler.
     *
     * @return special navigation handler.
     */
    public NavigationHandler getNavigationHandler() {
        return originalApplication.getNavigationHandler();
    }

    /**
     * Delegate to original.
     *
     * @param handler Delegate to original.
     */
    public void setNavigationHandler(final NavigationHandler handler) {
        this.originalApplication.setNavigationHandler(handler);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public PropertyResolver getPropertyResolver() {
        return this.originalApplication.getPropertyResolver();
    }

    /**
     * Delegate to original.
     *
     * @param resolver Delegate to original.
     */
    public void setPropertyResolver(final PropertyResolver resolver) {
        this.originalApplication.setPropertyResolver(resolver);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public VariableResolver getVariableResolver() {
        return this.originalApplication.getVariableResolver();
    }

    /**
     * Delegate to original.
     *
     * @param resolver Delegate to original.
     */
    public void setVariableResolver(final VariableResolver resolver) {
        this.originalApplication.setVariableResolver(resolver);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public ViewHandler getViewHandler() {
        return this.originalApplication.getViewHandler();
    }

    /**
     * Delegate to original.
     *
     * @param handler Delegate to original.
     */
    public void setViewHandler(final ViewHandler handler) {
        this.originalApplication.setViewHandler(handler);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    public StateManager getStateManager() {
        return this.originalApplication.getStateManager();
    }

    /**
     * Delegate to original.
     *
     * @param manager Delegate to original.
     */
    public void setStateManager(final StateManager manager) {
        this.originalApplication.setStateManager(manager);
    }

    /**
     * Delegate to original.
     *
     * @param componentType Delegate to original.
     * @param componentClass Delegate to original.
     */
    public void addComponent(final String componentType,
        final String componentClass) {
        this.originalApplication.addComponent(componentType, componentClass);
    }

    /**
     * Tries to create a UI component via the original Application and looks for
     * the UI component in Spring's root application context if it is not
     * obtainable through JSF's Application.
     * @param componentType the component type and Spring bean name for the UIComponent
     * @return the resulting UIComponent
     *         through either the JSF Application or Spring Application Context.
     */
    public UIComponent createComponent(final String componentType) {
        FacesException originalException = null;

        try {
            // Create component with original application
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to create component with type '" +
                    componentType + "' using original Application");
            }

            UIComponent originalComponent = this.originalApplication.createComponent(componentType);

            if (originalComponent != null) {
                return originalComponent;
            }

            originalException = new FacesException(
                    "Original Application returned a null component");
        } catch (FacesException e) {
            originalException = e;
        }

        // Get component from Spring root context
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to find component '" + componentType +
                "' in root WebApplicationContext");
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        WebApplicationContext wac = getWebApplicationContext(facesContext);

        if (wac.containsBean(componentType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Successfully found component '" + componentType +
                    "' in root WebApplicationContext");
            }

            return (UIComponent) wac.getBean(componentType);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Could not create component '" + componentType + "'");
        }

        throw new FacesException(
            "Could not create component using original Application.  " +
            "Also, could not find component in root WebApplicationContext",
            originalException);
    }

    /**
     * Tries to obtain a UI component though the given value binding.
     * If a non-null component is not obtained, then this method simply
     * delegates to {@link #createComponent(String)}, passing the componentType.
     * @param componentBinding the component value binding
     * @param context the FacesContext
     * @param componentType the component type
     * @return the resulting UIComponent
     *         through either the value binding, JSF Application or Spring Application Context.
     */
    public UIComponent createComponent(final ValueBinding componentBinding,
        final FacesContext context, final String componentType) {
        FacesException originalException = null;

        try {
            // Create component with value binding
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Attempting to create component with value binding");
            }

            Object value = componentBinding.getValue(context);

            if ((value != null) && value instanceof UIComponent) {
                return (UIComponent) value;
            }

            originalException = new FacesException(
                    "Original Application returned a null component");
        } catch (FacesException e) {
            originalException = e;
        }

        try {
            // Value binding did not return a UIComponent, so attempt creation by type
            return this.createComponent(componentType);
        } catch (FacesException e) {
            throw new FacesException(originalException.getMessage() +
                " Additionally, the component could not be created based on the component type",
                e);
        }
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    @SuppressWarnings("unchecked")
	public Iterator getComponentTypes() {
        return this.originalApplication.getComponentTypes();
    }

    /**
     * Delegate to original.
     *
     * @param converterId Delegate to original.
     * @param converterClass Delegate to original.
     */
    public void addConverter(final String converterId,
        final String converterClass) {
        this.originalApplication.addConverter(converterId, converterClass);
    }

    /**
     * Delegate to original.
     *
     * @param targetClass Delegate to original.
     * @param converterClass Delegate to original.
     */
    @SuppressWarnings("unchecked")
	public void addConverter(final Class targetClass,
        final String converterClass) {
        this.originalApplication.addConverter(targetClass, converterClass);
    }

    /**
     * Tries to create converter via the original Application and looks for
     * the converter in Spring's root application context if it is not
     * obtainable through JSF's Application.
     * @param converterId the converter ID and Spring bean name for the Converter
     * @return the resulting Converter
     */
    public Converter createConverter(final String converterId) {
        FacesException originalException = null;
        try {
            // Create converter with original application
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to create converter with id '" +
                    converterId + "' using original Application");
            }

            Converter originalConverter = this.originalApplication.createConverter(converterId);

            if (originalConverter != null) {
                return originalConverter;
            }

            originalException = new FacesException(
                    "Original Application returned a null Converter");
        } catch (FacesException e) {
            originalException = e;
        }

        // Get converter from Spring root context
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to find converter '" + converterId +
                "' in root WebApplicationContext");
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        WebApplicationContext wac = getWebApplicationContext(facesContext);

        if (wac.containsBean(converterId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Successfully found converter '" + converterId +
                    "' in root WebApplicationContext");
            }

            return (Converter) wac.getBean(converterId);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Could not create converter '" + converterId + "'");
        }

        throw new FacesException(
            "Could not create converter using original Application.  " +
            "Also, could not find converter in root WebApplicationContext",
            originalException);
    }

    /**
     * Tries to create converter via the original Application and looks for
     * the converter in Spring's root application context if it is not
     * obtainable through JSF's Application.
     * <p>
     * When looking for the converter in Spring, the bean name will be a
     * concatenation of the fully-qualified class name of the targetClass with
     * the suffix <code>-Converter</code>.  For example, if the target class is
     * <code>java.util.Locale</code>, then the corresponding converter would need
     * to have the bean name <code>java.util.Locale-Converter</code>.
     * @param targetClass the class on which the converter operates
     * @return the resulting Converter
     */
    @SuppressWarnings("unchecked")
	public Converter createConverter(final Class targetClass) {
        FacesException originalException = null;
        
        try {
            // Create converter with original application
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to create converter for class '" +
                    targetClass.getName() + "' using original Application");
            }

            Converter originalConverter = this.originalApplication.createConverter(targetClass);

            if (originalConverter != null) {
                return originalConverter;
            }

            originalException = new FacesException(
                    "Original Application returned a null Converter");
        } catch (FacesException e) {
            originalException = e;
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();

        WebApplicationContext wac = getWebApplicationContext(facesContext);

        if (wac.containsBean("converters")) {
            Map<String, Converter> converters = (Map<String, Converter>)wac.getBean("converters");
             
            Converter converter = converters.get(CrudUtils.getClassEntityName(targetClass)); 
            return converter;
        }
        throw originalException;
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    @SuppressWarnings("unchecked")
	public Iterator getConverterIds() {
        return this.originalApplication.getConverterIds();
    }

    @SuppressWarnings("unchecked")
	private List<Class> jsfConverterTypes;
    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    @SuppressWarnings("unchecked")
    public Iterator<Class> getConverterTypes() {
        if (jsfConverterTypes==null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                WebApplicationContext wac = getWebApplicationContext(facesContext);
                jsfConverterTypes = (List<Class>) wac.getBean("jsfConverterTypes");
                Iterator<Class> converterTypes = this.originalApplication.getConverterTypes();
                while ( converterTypes.hasNext() ) {
                    jsfConverterTypes.add( converterTypes.next() );
                }
                
        }
        return jsfConverterTypes.iterator();
    }

    /**
     * Delegate to original.
     *
     * @param ref Delegate to original.
     * @param params Delegate to original.
     *
     * @return Delegate to original.
     *
     */
    @SuppressWarnings("unchecked")
	public MethodBinding createMethodBinding(final String ref,
        final Class[] params) {
        return this.originalApplication.createMethodBinding(ref, params);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    @SuppressWarnings("unchecked")
	public Iterator getSupportedLocales() {
        return this.originalApplication.getSupportedLocales();
    }

    /**
     * Delegate to original.
     *
     * @param locales Delegate to original.
     */
    @SuppressWarnings("unchecked")
	public void setSupportedLocales(final Collection locales) {
        this.originalApplication.setSupportedLocales(locales);
    }

    /**
     * Delegate to original.
     *
     * @param validatorId Delegate to original.
     * @param validatorClass Delegate to original.
     */
    public void addValidator(final String validatorId,
        final String validatorClass) {
        this.originalApplication.addValidator(validatorId, validatorClass);
    }

    /**
     * Tries to create validator via the original Application and looks for
     * the validator in Spring's root application context if it is not
     * obtainable through JSF's Application.
     * @param validatorId the validator ID and Spring bean name for the Validator
     * @return the resulting Validator
     */
    public Validator createValidator(final String validatorId) {
        FacesException originalException = null;

        try {
            // Create validator with original application
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to create validator with id '" +
                    validatorId + "' using original Application");
            }

            Validator originalValidator = this.originalApplication.createValidator(validatorId);

            if (originalValidator != null) {
                return originalValidator;
            }

            originalException = new FacesException(
                    "Original Application returned a null Validator");
        } catch (FacesException e) {
            originalException = e;
        }

        // Get validator from Spring root context
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to find validator '" + validatorId +
                "' in root WebApplicationContext");
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        WebApplicationContext wac = getWebApplicationContext(facesContext);

        if (wac.containsBean(validatorId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Successfully found validator '" + validatorId +
                    "' in root WebApplicationContext");
            }

            return (Validator) wac.getBean(validatorId);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Could not create validator '" + validatorId + "'");
        }

        throw new FacesException(
            "Could not create validator using original Application.  " +
            "Also, could not find validator in root WebApplicationContext",
            originalException);
    }

    /**
     * Delegate to original.
     *
     * @return Delegate to original.
     */
    @SuppressWarnings("unchecked")
    public Iterator getValidatorIds() {
        return this.originalApplication.getValidatorIds();
    }

    /**
     * Delegate to original.
     *
     * @param ref Delegate to original.
     *
     * @return Delegate to original.
     *
     */
    public ValueBinding createValueBinding(final String ref) {
        return this.originalApplication.createValueBinding(ref);
    }

    /**
     * Retrieve the web application context to delegate bean name resolution to.
     * Default implementation delegates to FacesContextUtils.
     * @param facesContext the current JSF context
     * @return the Spring web application context
     * @see FacesContextUtils#getRequiredWebApplicationContext
     */
    protected WebApplicationContext getWebApplicationContext(
        final FacesContext facesContext) {
        return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
    }

    @Override
    public void addELContextListener( ELContextListener listener ) {
        this.originalApplication.addELContextListener( listener );
    }

    @Override
    public void addELResolver( ELResolver resolver ) {
        this.originalApplication.addELResolver( resolver );
    }

    @Override
    public UIComponent createComponent( ValueExpression componentExpression, FacesContext context, String componentType ) throws FacesException {
        return this.originalApplication.createComponent( componentExpression, context, componentType );
    }

    @SuppressWarnings("unchecked")
	@Override
    public Object evaluateExpressionGet( FacesContext context, String expression, Class expectedType ) throws ELException {
        return this.originalApplication.evaluateExpressionGet( context, expression, expectedType );
    }

    @Override
    public ELContextListener[] getELContextListeners() {
        return this.originalApplication.getELContextListeners();
    }

    @Override
    public ELResolver getELResolver() {
        return this.originalApplication.getELResolver();
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return this.originalApplication.getExpressionFactory();
    }

    @Override
    public ResourceBundle getResourceBundle( FacesContext ctx, String name ) {
        return this.originalApplication.getResourceBundle( ctx, name );
    }

    @Override
    public void removeELContextListener( ELContextListener listener ) {
        this.originalApplication.removeELContextListener( listener );
    }
}
