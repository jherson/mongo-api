package com.rhcloud.mongo.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlIntegerTypeAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer value) throws Exception {
		return value.toString();
	}

	@Override
	public Integer unmarshal(String value) throws Exception {		
		if (value.startsWith("${env.")) {
        	value = System.getenv(value.replace("${env.", "").replace('}', ' ').trim());
		}
		return Integer.valueOf(value);
	}
}