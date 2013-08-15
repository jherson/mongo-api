package com.rhcloud.mongo.spi;

import java.util.ServiceLoader;

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceRegistryException;

public class MongoDBServiceActivatorImpl implements MongoDBServiceActivator {
	
	public MongoDBServiceActivatorImpl() {
		System.out.println("Starting activator");
	}
	
	@Override
	public void startScan() {
		System.out.println("Starting scan");
	}

}