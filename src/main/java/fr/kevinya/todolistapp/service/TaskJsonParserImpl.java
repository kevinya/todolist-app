package fr.kevinya.todolistapp.service;

import org.json.JSONException;
import org.json.JSONObject;

import fr.kevinya.todolistapp.entity.Task;

public class TaskJsonParserImpl implements JsonParser {

	@Override
	public Task convert(String json) {
		Task task = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			task = new Task();
			task.setId(jsonObject.getInt("id"));
			task.setName(jsonObject.getString("name"));
			task.setStatus(jsonObject.getInt("status"));
			task.setVersion(jsonObject.getInt("version"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return task;
	}

	@Override
	public String convert(Object object) {
		Task task = (Task) object;
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: \"" + task.getId() + "\",");
		sb.append("name: \"" + task.getName().replaceAll("\"", "\\\"\"")
				+ "\",");
		sb.append("status: \"" + task.getStatus() + "\",");
		sb.append("version: \"" + task.getVersion() + "\"");
		sb.append("}");
		return sb.toString();
	}

}
