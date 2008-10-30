package org.crank.message;

public class MessageManagerUtils {
	public static ThreadLocal<MessageManager>  messageManagerTL = new ThreadLocal<MessageManager>();
	
	public static MessageManager getCurrentInstance() {
		if(messageManagerTL.get()==null) {
			messageManagerTL.set(new NoOpMessageManager());
		}
		return messageManagerTL.get();
	}
	
	public static void setCurrentInstance(MessageManager messageManager){
		messageManagerTL.set(messageManager);
	}
}
