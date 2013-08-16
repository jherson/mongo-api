package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

import com.rhcloud.mongo.AnnotationScanner;

public class ScannerService implements Service<AnnotationScanner> {
	
	private Logger log = Logger.getAnonymousLogger(ScannerService.class.getName());
	
	@Override
    public AnnotationScanner getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }

    @Override
    public void start(StartContext context) throws StartException {
        log.info("start");
    }

    @Override
    public void stop(StopContext context) {
        log.info("stop");
    }
}