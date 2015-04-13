package lv.welding.orders.utils;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Helper class to work with views inside flow
 */
public class Primefaces35PhaseListener implements PhaseListener {
	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent phaseEvent) {
        // NOP
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();

//        if (context.getAttributes().get(Constants.REQUEST_CONTEXT_ATTR) == null) {
//            context.getAttributes().put(Constants.REQUEST_CONTEXT_ATTR, new DefaultRequestContext());
//        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}