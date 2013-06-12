package fr.kevinya.todolistapp.service;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpFactory {
	static public HttpRequestBase get(String type, String uri) {
		HttpRequestBase httpRequestBase = null;
		if (type.equals("post")) {
			httpRequestBase = new HttpPost(uri);
		} else if (type.equals("put")) {
			httpRequestBase = new HttpPut(uri);
		} else if (type.equals("delete")) {
			httpRequestBase = new HttpDelete(uri);
		} else {
			httpRequestBase = new HttpGet(uri);
		}
		httpRequestBase.setHeader("Content-Type", "application/json");
		return httpRequestBase;
	}
}
