package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;

public class MongoDBServiceActivator implements ServiceActivator {
	
	private static final Logger log = Logger.getLogger(MongoDBServiceActivator.class.getName());
	private static final ServiceName SERVICE_NAME = ServiceName.of("scanner", "annotationScanner");

	@Override
	public void activate(ServiceActivatorContext context) throws ServiceRegistryException {
		context.getServiceTarget().addService(SERVICE_NAME, new ScannerService()).install();
	}
}