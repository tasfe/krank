package org.crank.metadata;

import static org.crank.metadata.ErrorHandlerType.DISPLAY_MESSAGE;
import static org.crank.metadata.Severity.ERROR;

import java.io.Serializable;

/** POJO that holds information on how we would like to handle an error. */
public class ErrorHandlerData implements Serializable {
	/** The exception that we are hanlding. */
	private Class<?> exceptionClass = Exception.class;
	/**	The message we are going to send the end user */
	private String messageDetail = "Problem"; 
	/** The message we are going to send the end user. */
	private String messageSummary = "Problem"; 
	private String id = "";
	/**	Handle all unhandled exceptions. */
	private boolean defaultHandler = false; 
	private boolean useMessageBundleForMessage =  false; //
	private boolean useMessageBundleForArgs =  false; //
	private boolean useExceptionForDetail =  true; //
	private String messageDetailKey =  ""; 
	private String[] messageDetailArgs =  {""};
	private String[] messageDetailArgKeys =  {""};
	private String messageSummaryKey =  "";
	private String[] messageSummaryArgs =  {""};
	private String[] messageSummaryArgKeys =  {""};
	/** How should we handle this error message. */
	private ErrorHandlerType type =  DISPLAY_MESSAGE; //
	private Severity severity = ERROR;
	private String outcome = "success";
	
	public boolean isDefaultHandler() {
		return defaultHandler;
	}
	public void setDefaultHandler(boolean defaultHandler) {
		this.defaultHandler = defaultHandler;
	}
	public Class<?> getExceptionClass() {
		return exceptionClass;
	}
	public void setExceptionClass(Class<?> exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}
	public String[] getMessageDetailArgKeys() {
		return messageDetailArgKeys;
	}
	public void setMessageDetailArgKeys(String[] messageDetailArgKeys) {
		this.messageDetailArgKeys = messageDetailArgKeys;
	}
	public String[] getMessageDetailArgs() {
		return messageDetailArgs;
	}
	public void setMessageDetailArgs(String[] messageDetailArgs) {
		this.messageDetailArgs = messageDetailArgs;
	}
	public String getMessageDetailKey() {
		return messageDetailKey;
	}
	public void setMessageDetailKey(String messageDetailKey) {
		this.messageDetailKey = messageDetailKey;
	}
	public String getMessageSummary() {
		return messageSummary;
	}
	public void setMessageSummary(String messageSummary) {
		this.messageSummary = messageSummary;
	}
	public String[] getMessageSummaryArgKeys() {
		return messageSummaryArgKeys;
	}
	public void setMessageSummaryArgKeys(String[] messageSummaryArgKeys) {
		this.messageSummaryArgKeys = messageSummaryArgKeys;
	}
	public String[] getMessageSummaryArgs() {
		return messageSummaryArgs;
	}
	public void setMessageSummaryArgs(String[] messageSummaryArgs) {
		this.messageSummaryArgs = messageSummaryArgs;
	}
	public String getMessageSummaryKey() {
		return messageSummaryKey;
	}
	public void setMessageSummaryKey(String messageSummaryKey) {
		this.messageSummaryKey = messageSummaryKey;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public ErrorHandlerType getType() {
		return type;
	}
	public void setType(ErrorHandlerType type) {
		this.type = type;
	}
	public boolean isUseExceptionForDetail() {
		return useExceptionForDetail;
	}
	public void setUseExceptionForDetail(boolean useExceptionForDetail) {
		this.useExceptionForDetail = useExceptionForDetail;
	}
	public boolean isUseMessageBundleForArgs() {
		return useMessageBundleForArgs;
	}
	public void setUseMessageBundleForArgs(boolean useMessageBundleForArgs) {
		this.useMessageBundleForArgs = useMessageBundleForArgs;
	}
	public boolean isUseMessageBundleForMessage() {
		return useMessageBundleForMessage;
	}
	public void setUseMessageBundleForMessage(boolean useMessageBundleForMessage) {
		this.useMessageBundleForMessage = useMessageBundleForMessage;
	}	
	

}
