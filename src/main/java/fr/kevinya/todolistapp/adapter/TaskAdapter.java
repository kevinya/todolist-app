package fr.kevinya.todolistapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import fr.kevinya.todolistapp.R;
import fr.kevinya.todolistapp.entity.Task;

public class TaskAdapter extends BaseAdapter {
	private Context context;
	private List<Task> taskList;
	
	public TaskAdapter() {
	}
	
	public TaskAdapter(Context context, List<Task> taskList) {
		this.context = context;
		this.taskList = taskList;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	@Override
	public int getCount() {
		return taskList.size();
	}

	@Override
	public Object getItem(int position) {
		return taskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return taskList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View returnedView = convertView;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.task, null);
		}
		CheckBox checkbox = (CheckBox) returnedView.findViewById(R.id.checkBox1);
		TextView textview = (TextView) returnedView.findViewById(R.id.textView1);
		

		Task task = (Task) getItem(position);
		if (task.getStatus() == 1) {	
			checkbox.setChecked(true);
		}
		textview.setText(task.getName());
		
		return returnedView;
	}

}
