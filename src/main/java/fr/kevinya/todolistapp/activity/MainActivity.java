package fr.kevinya.todolistapp.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.kevinya.todolistapp.R;
import fr.kevinya.todolistapp.adapter.TaskAdapter;
import fr.kevinya.todolistapp.entity.Task;
import fr.kevinya.todolistapp.service.TaskServiceImpl;

public class MainActivity extends ListActivity implements Observer {
	List<Task> taskList;
	TaskAdapter taskAdapter;
	TaskServiceImpl taskService;
	
	Button button;
	EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		taskService = TaskServiceImpl.getInstance(this);
		taskService.addObserver(this);
		taskList = new ArrayList<Task>();
		taskAdapter = new TaskAdapter(this, taskList);
		setListAdapter(taskAdapter);

        getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Task task = (Task) taskAdapter.getItem(arg2);
				Integer status = (task.getStatus() == 0) ? 1 : 0;
				task.setStatus(status);
				taskService.update(task);
				taskAdapter.notifyDataSetChanged();
			}
		});
        
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Task task = (Task) taskAdapter.getItem(arg2);
				taskService.delete(task);
				return false;
			}
		});
        
		update(taskService, null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			taskService.getTaskDataSource().findAll();
			break;
		case R.id.menu_add_task:
			FragmentManager fragmentManager = getFragmentManager();
			AddTaskDialogFragment fragment = new AddTaskDialogFragment();
			fragment.show(fragmentManager, "fragment_add_task");
			break;
		}
		return true;
	}

	@Override
	public void update(Observable observable, Object data) {
		taskList = taskService.findNotDeleted();
		taskAdapter.setTaskList(taskList);
		taskAdapter.notifyDataSetChanged();
	}
}
