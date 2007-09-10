package com.arcmind.jpa.course;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven
public class SimpleMessageBean implements MessageListener {

	@Resource
	private MessageDrivenContext mdc;

	public void onMessage(Message message) {
		TextMessage textMessage = null;

		try {
			textMessage = (TextMessage) message;
			String someText = textMessage.getText();
			doSomethingWithMessage(someText);
		} catch (JMSException e) {
			e.printStackTrace();
			mdc.setRollbackOnly();
		} catch (Throwable th) {
			th.printStackTrace();
		}

	}

	private void doSomethingWithMessage(String messageText) {
		System.out.println(messageText);
	}

}
