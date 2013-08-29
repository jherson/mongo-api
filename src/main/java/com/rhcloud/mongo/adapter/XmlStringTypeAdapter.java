package com.rhcloud.mongo.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlStringTypeAdapter extends XmlAdapter<String,String> {

	@Override
	public String marshal(String value) throws Exception {
		return value;
	}

	@Override
	public String unmarshal(String value) throws Exception {
        if (value.startsWith("${env.")) {
        	value = System.getenv(value.replace("${env.", "").replace('}', ' ').trim());
        } 
        return value;
	}
}