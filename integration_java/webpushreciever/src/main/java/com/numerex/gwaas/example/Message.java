package com.numerex.gwaas.example;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "message")
public class Message implements Serializable {

	private static final long serialVersionUID = 745025409062546967L;
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; 
	public static final String MSG_VERSION = "1";

	private String id;
	private String type;
	private Date timestamp;

	private HashMap<String, String> data;
	private HashMap<String, String> headers;

	public Message() {
		defaultDataSets();
		
		postCreation();
	}

	public Message(String type) {
		defaultDataSets();
		this.setType(type);
		
		postCreation();
	}

	public Message(String type, HashMap<String, String> headers, HashMap<String, String> data) {
		defaultDataSets();
		this.setType(type);
		if (headers != null)
			this.headers = headers;
		if (data != null)
			this.data = data;
		
		postCreation();
	}

	public Message(String id, String type, Date timestamp, HashMap<String, String> headers, HashMap<String, String> data) {
		defaultDataSets();
		this.id = id;
		this.setType(type);
		this.setTimestamp(timestamp);
		if (headers != null)
			this.headers = headers;
		if (data != null)
			this.data = data;
		
		postCreation();
	}

	public Message(Message m, boolean copyHeaders, boolean copyData) {
		defaultDataSets();
		this.id = m.getId();
		this.type = m.getType();
		this.setTimestamp(m.getTimestamp());

		if (copyHeaders)
			this.headers.putAll(m.getHeaders());
		if (copyData)
			this.data.putAll(m.getData());

		postCreation();
	}

	private void defaultDataSets() {
		this.id = UUID.randomUUID().toString();
		this.setTimestamp(new Date());
		this.type = null;
		this.data = new HashMap<String, String>();
		this.headers = new HashMap<String, String>();
	}
	
	private void postCreation() {
		this.headers.put("msg_version", MSG_VERSION);
	}
	
	
	
	/*
	 * Makes a date string formatted in the default way for messages
	 */
	public static String makeDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(Message.DATE_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(date);
	}
	
	/*
	 * Makes a date from a date string formatted in the default way for messages
	 */
	public static Date makeDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(Message.DATE_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new Error("couldn't parse date:" + date,e);
		}
	}
	
	
	
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\r\nMessage [\r\n	id=" + id + ", \r\n	type=" + type + ",\r\n	timestamp=" + makeDateString(timestamp) + ", \r\n	data=" + data + ",\r\n	headers=" + headers + "\r\n]";
	}

	public void setDataElement(String key, String value) {
		this.setElement(key, value, this.data);
	}

	public String getDataElement(String key) {
		return this.getElement(key, this.data);
	}

	public void setHeaderElement(String key, String value) {
		this.setElement(key, value, this.headers);
	}

	public String getHeaderElement(String key) {
		return this.getElement(key, this.headers);
	}

	private String getElement(String key, HashMap<String, String> collection) {
		if (collection.containsKey(key.toLowerCase()))
			return collection.get(key.toLowerCase());
		else
			return null;
	}

	private void setElement(String key, String value, HashMap<String, String> collection) {
		if (value != null) {
			collection.put(key.toLowerCase(), value.toString());
		} else {
			if (collection.containsKey(key.toLowerCase()))
				collection.remove(key.toLowerCase());
		}
	}

	public void setType(String type) {
		this.type = (type == null) ? null : type.toLowerCase();
	}

	public String getType() {
		return this.type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	public HashMap<String, String> getHeaders() {
		return this.headers;
	}

	public HashMap<String, String> getData() {
		return this.data;
	}
}