/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.jsf.util;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author Gleywson
 */
public class JsfExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public JsfExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable exception = context.getException();

            boolean handled = false;

            try {
                if (exception instanceof ViewExpiredException) {
                    handled = true;
                    JsfUtil.redirect("/dashboard.xhtml");
                } else {
                    handled = true;
                    JsfUtil.redirect("/error.xhtml");
                }
            } finally {
                if (handled) {
                    events.remove();
                }
            }
        }

        getWrapped().handle();
    }
    
}
