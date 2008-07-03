package org.crank.web.jsf.support;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class DebugPhaseListener implements PhaseListener {

	public void afterPhase(PhaseEvent phaseEvent) {
		System.out.println("After Phase " + phaseEvent.getPhaseId());
		
		if (phaseEvent.getPhaseId() == PhaseId.RENDER_RESPONSE) {
			System.out.println("-------------------------------");
			System.out.println("\n");
		} else {
			System.out.println("");
		}
	}

	public void beforePhase(PhaseEvent phaseEvent) {
		System.out.println("Before Phase " + phaseEvent.getPhaseId());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
