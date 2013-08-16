package com.rhcloud.mongo.spi;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;

public class MongoDBServiceActivator implements ServiceActivator {
	
	private static final ServiceName SERVICE_NAME = ServiceName.of("Scanner", "ScannerService");

	@Override
	public void activate(ServiceActivatorContext context) throws ServiceRegistryException {
		context.getServiceTarget().addService(SERVICE_NAME, new ScannerService()).install();
	}
}