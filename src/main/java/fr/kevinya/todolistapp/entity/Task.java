package fr.kevinya.todolistapp.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	private Integer id;
	private Integer backendId;
	private String name;
	private Integer status;
	private Integer version;
	
	public Task() {	
	}
	
	public Task(Integer id, Integer backendId, String name, Integer status, Integer version) {
		this.id = id;
		this.backendId = backendId;
		this.name = name;
		this.status = status;
		this.version = version;
	}

	public Task(JSONObject json) {
		try {
			this.backendId = json.getInt("id");
			this.name = json.getString("name");
			this.status = json.getInt("status");
			this.version = json.getInt("version");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBackendId() {
		return backendId;
	}

	public void setBackendId(Integer backendId) {
		this.backendId = backendId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", getBackendId());
			json.put("name", getName());
			json.put("status", getStatus());
			json.put("version", getVersion());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String toString() {
		return toJSON().toString();
	}
}
