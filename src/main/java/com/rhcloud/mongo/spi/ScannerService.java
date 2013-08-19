package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

public class ScannerService implements Service<AnnotationScanner> {
	
	private Logger log = Logger.getLogger(ScannerService.class.getName());
	private AnnotationScanner annotationScanner;
	
	public ScannerService(AnnotationScanner annotationScanner) {
		this.annotationScanner = annotationScanner;
	}

	@Override
	public AnnotationScanner getValue() throws IllegalStateException, IllegalArgumentException {
		return annotationScanner;
	}

	@Override
	public void start(StartContext context) throws StartException {
		log.info("start");
		annotationScanner.startScan();
	}

	@Override
	public void stop(StopContext context) {
		log.info("stop");
	}
}