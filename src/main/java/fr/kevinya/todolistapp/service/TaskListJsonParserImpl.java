package fr.kevinya.todolistapp.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.kevinya.todolistapp.entity.Task;

public class TaskListJsonParserImpl implements JsonParser {

	@Override
	public List<Task> convert(String json) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Task task = new Task();
				task.setId(jsonObject.getInt("id"));
				task.setName(jsonObject.getString("name"));
				task.setStatus(jsonObject.getInt("status"));
				task.setVersion(jsonObject.getInt("version"));
				taskList.add(i, task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return taskList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convert(Object object) {
		List<Task> taskList = (List<Task>) object;
		TaskJsonParserImpl taskJSonParser = new TaskJsonParserImpl();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Task task : taskList) {
			sb.append(taskJSonParser.convert(task));
		}
		sb.append("]");
		return sb.toString();
	}

}
