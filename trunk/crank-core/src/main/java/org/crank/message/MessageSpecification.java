package org.crank.message;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.core.Log;
import org.crank.core.ResourceBundleLocator;

/** Contains information about how to generate a message. 
 * This class knows how to create a message. 
 * It will look up the message in the resource bundle if it
 * starts with a "{". 
 * 
 * Future: It will look up the message in the 
 * EL context if it starts with a "#{"
 * */
public class MessageSpecification implements Serializable {
	private static final long serialVersionUID = 1L;

	Log log = Log.getLog(MessageSpecification.class);
    
	/** The detailMessage part of the message. */
    private String detailMessage="detailMessage";
    /** The summaryMessage part of the message. */
    private String summaryMessage="summaryMessage";
    /** Arguments that get passed to the detailMessage message. */
    private List <String> detailArgs;
    /** Arguments that get passed to the summaryMessage message. */
    private List <String> summaryArgs;
    
    /** The name of this message specification, used to look up information
     *  in the resource bundle if needed.
     */
    private String name;
    /** Used to create messages that inherit properties from their parents.
     * 
     */
    private String parent;
    
    /** Used to determine if an argument refers to an expression like a
     * JSF expression, a universal EL expression, or an OGNL expression.
     * This is configurable and could be changed if you are working
     * with another expression language parser.
     */
    private String expressionMarker = "#{";
    /**
     * Used to determine if an argument or message refers to an item that
     * should be looked up in the resourceBundle. 
     */
    private String i18nMarker = "{";
    /**
     * Used to find the resource bundle. This can vary based on the
     * web framework you are using, and wheter you want to use Spring
     * Message sources or not.
     */
    private ResourceBundleLocator  resourceBundleLocator;
    
    /** Who is this message about? For example for field validation
     * the subject is the name of the field. 
     */
    private String subject = "";

    private static final String SUMMARY_KEY = ".summary";
    private static final String DETAIL_KEY = ".detail";

    /** The init method tries to generate the message keys.
     * You should only call the init method if you don't inject
     * values into the detailMessage and summaryMessage.
     *
     */
    public void init() {
    	/* If the parent and name are equal to null, 
    	 * use the classname to load resources.
    	 * */
        if (name == null && parent == null) {
            this.setDetailMessage("{" + this.getClass().getName() + DETAIL_KEY + "}");
            this.setSummaryMessage("{" + this.getClass().getName() + SUMMARY_KEY+ "}");
        /* If the parent is null and the name is not,
         * use the name to load resources.
         */
        } else if (name != null && parent == null) {
            this.setDetailMessage("{" + "message." + getName() + DETAIL_KEY + "}");
            this.setSummaryMessage("{" + "message." + getName() + SUMMARY_KEY + "}");
        /* If the parent is present, initialize the message keys
         * with the parent name.
         */
        } else if (parent != null) {
            this.setDetailMessage("{" + "message." + parent + DETAIL_KEY + "}");
            this.setSummaryMessage("{" + "message." + parent + SUMMARY_KEY + "}");
        }
    }

    /** Create the summaryMessage message. */
    public String createSummaryMessage(Object... args){
    	if (MessageSpecificationGlobalConfig.isUseSummary()) {
    		return createMessage(summaryMessage, summaryArgs, args);
    	} else {
    		return "summary messages turned off";
    	}
    }

    /** Create the detailMessage message. */
    public String createDetailMessage(Object... args){
    	if (MessageSpecificationGlobalConfig.isUseDetail()) {
    		return createMessage(detailMessage, detailArgs, args);
    	} else {
    		return "detail messages turned off";
    	}
    }

    /** Creates a message. 
     *  @param argkeys arguments to lookup that were configured.
     *  @param args Arguments that were passed via the object that wants to
     *   generate the message  
     */
    public String createMessage(String key, List<String> argKeys, Object... args) {
    	/* Look up the message. */
    	String message = getMessage(key);
    	
    	/* Holds the actual arguments. */
    	Object[] actualArgs;
    	
    	/* If they passed arguments, 
    	 * then use this as the actual arguments. */
    	if (args.length > 0) {
    		actualArgs = args;
    	/* If they did not pass arguments, use the configured ones. */
    	} else if (argKeys!=null){
    		/* Convert the keys to values. */
        	actualArgs = keysToValues(argKeys);
    	} else {
    		actualArgs = new Object[]{};
    	}
		
    	return doCreateMessage(message, actualArgs);

    }
    
    /**
     * Actually creates the message.
     * @param message The message that was looked up.
     * @param actualArgs Arguments to the message.
     * @return
     */
    @SuppressWarnings("unchecked")
    private String doCreateMessage(String message, Object [] actualArgs) {
    	
    	List argumentList = new ArrayList(Arrays.asList(actualArgs));

    	/* If the subject is found add it as the first 
    	 * argument to the argument list. */
    	if (getSubject()!=null) {
    		argumentList.add(0, 
    				/* Look up the subject in the 
    				 * resource bundle, generate label if not found. */
    				MessageUtils.getLabel(getSubject(), 
    						resourceBundleLocator.getBundle()));
    	}
    	try {
    		/* Attempt to create the message. */
    		return MessageFormat.format(message, argumentList.toArray());
    	} catch (Exception ex) {
            
            log.handleExceptionError("We could not create the message", ex);
    		return ex.getMessage();
    	}
    }

    private String getMessage(String key) {
        return doGetMessage( key );
    }
    
    /** The doGetMessage does a bit of magic. If the message starts with {
     * than it assumes it is an i18N message and looks it up in the 
     * resource bundle. If it starts with #{ it assumes it is an expression
     * and uses OGNL, JSF EL, or the Universal EL to look up the expression
     * in the context. */
	private String doGetMessage(String key) {
		/* Find the resourceBundle. */
		ResourceBundle bundle = this.resourceBundleLocator.getBundle();
        
		
    	String message = null; //holds the message
    	
    	/* If the message starts with an i18nMarker look it up
    	 * in the resource bundle.
    	 */
    	if (key.startsWith(this.i18nMarker)) {
    		try {
    			key = key.substring(1, key.length()-1);
                message = lookupMessageInBundle(key, bundle, message);
    		} catch (MissingResourceException mre) {
    			message = key;
    		}
    	} 
    	/*
		 * If the message starts with the expression marker resolve it as an
		 * Expression (Universal, JSF, OGNL, etc.)
		 */
		else if (key.startsWith(this.expressionMarker)) {
			message = resolveExpression(key).toString();
		} else {
			/*
			 * If it does not start with those markers see if it has a ".". If
			 * it has a dot, try to look it up. If it is not found then just
			 * return the key as the message.
			 */
			if (key.contains(".")) {
				try {
					message = lookupMessageInBundle(key, bundle, message);
				} catch (MissingResourceException mre) {
					message = key;
				}
			} else {
				message = key;
			}
		}
		return message;
	}

	private String lookupMessageInBundle(String key, ResourceBundle bundle,
			String message) {
		if (getSubject()!=null) {
		    try {
		    	if (MessageSpecificationGlobalConfig.isUseSubject()) {
		    		message = bundle.getString(key + "." + getSubject());
		    	} else {
		    		message = bundle.getString(key);
		    	}
		    } catch (MissingResourceException mre) {
		        message = bundle.getString(key);                        
		    }
		} else {
			return bundle.getString(key);
		}
		return message;
	}

	/** This is a future method that we are going to implemnt
	 * so we can pass arguments that are really just expression.
	 * @param key
	 * @return
	 */
    private Object resolveExpression(String key) {
    	throw new UnsupportedOperationException("future resolve expressions from Universal EL or OGNL");
	}

    /** Convert the keys to values. */
	private Object [] keysToValues(List<String> argKeys) {
        List <String> values = new ArrayList<String>();
        for (String key : argKeys) {
            values.add(getMessage(key));
        }
        return values.toArray();
    }

	/** Holds the current subject. This allows this class to be stateless
	 * yet still allow us to change the subject on a per thread basis. */
	private ThreadLocal<String> subjectHolder = new ThreadLocal<String>();
	
	/** Allows client objects to set the subject for the current thread
	 * per instance of the MessageSpecification. */
    public void setCurrentSubject(String subject) {
		subjectHolder.set(subject);
	}

	/** Gets the current subject or the configured subject if the 
	 * current subject is not found. */
    public String getSubject() {
		if (subjectHolder.get()!=null) {
			return subjectHolder.get();
		}
		return subject;
	}

	
    protected String getDetailMessage() {
        return this.detailMessage;
    }

    @AllowsConfigurationInjection
    public void setDetailMessage(String detailKey) {
        this.detailMessage = detailKey;
    }

    protected String getSummaryMessage() {
        return this.summaryMessage;
    }

    @AllowsConfigurationInjection
    public void setSummaryMessage(String summaryKey) {
        this.summaryMessage = summaryKey;
    }

    protected List<String> getDetailArgs() {
        return this.detailArgs;
    }

    @AllowsConfigurationInjection
    public void setDetailArgs(List<String> argKeys) {
        this.detailArgs = argKeys;
    }

    protected List<String> getSummaryArgs() {
        return this.summaryArgs;
    }

    @AllowsConfigurationInjection
    public void setSummaryArgs(List<String> summaryArgKeys) {
        this.summaryArgs = summaryArgKeys;
    }

    @AllowsConfigurationInjection
    public void setName(String aName) {
        this.name = aName;
    }

    public String getName() {
        return this.name;
    }


    @AllowsConfigurationInjection
    public void setParent(String parent) {
        this.parent = parent;
    }


    @ExpectsInjection
	public void setResourceBundleLocator(ResourceBundleLocator resourceBundleLocator) {
		this.resourceBundleLocator = resourceBundleLocator;
	}

    @AllowsConfigurationInjection
	public void setSubject(String subject) {
		this.subject = subject;
	}
	

	protected ResourceBundleLocator getResourceBundleLocator() {
		return resourceBundleLocator;
	}
	
}
