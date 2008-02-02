package org.crank.jsf.support;

import javax.el.ELContext;
import javax.el.MethodInfo;
import javax.faces.component.ActionSource2;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class DebugActionListener implements ActionListener {
	private ActionListener nextActionListener;
	
	public DebugActionListener (ActionListener nextActionListener) {
		this.nextActionListener = nextActionListener;
	}
	public void processAction(ActionEvent event)
			throws AbortProcessingException {
		ActionSource2 actionSource = (ActionSource2)event.getComponent();
		ELContext context = FacesContext.getCurrentInstance().getELContext();
		MethodInfo methodInfo = actionSource.getActionExpression().getMethodInfo(context);
		System.out.println("1 DebugActionListener::" + methodInfo.getName());
		nextActionListener.processAction(event);
		System.out.println("2 DebugActionListener::" + methodInfo.getName());
	}

}
