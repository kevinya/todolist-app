package fr.kevinya.todolistapp.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.kevinya.todolistapp.entity.Task;
import fr.kevinya.todolistapp.helper.SQLHelper;

public class TaskDAO {
	private SQLiteDatabase database;
	private SQLHelper dbHelper;
	private String[] allColumns = {
			SQLHelper.TABLE_TASKS_COLUMN_ID,
			SQLHelper.TABLE_TASKS_COLUMN_BACKEND_ID,
			SQLHelper.TABLE_TASKS_COLUMN_NAME,
			SQLHelper.TABLE_TASKS_COLUMN_STATUS,
			SQLHelper.TABLE_TASKS_COLUMN_VERSION
	};
	
	public TaskDAO(Context context) {
		dbHelper = new SQLHelper(context);
	}
	
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Task create(Task task) {
		ContentValues values = new ContentValues();
		values.put(SQLHelper.TABLE_TASKS_COLUMN_BACKEND_ID, task.getBackendId());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_NAME, task.getName());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_STATUS, task.getStatus());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_VERSION, task.getVersion());
	    long insertId = database.insert(SQLHelper.TABLE_TASKS, null, values);
	    
		return findById(Integer.getInteger(String.valueOf(insertId)));
	}
	
	public Task update(Task task) {
		ContentValues values = new ContentValues();
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_BACKEND_ID, task.getBackendId());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_NAME, task.getName());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_STATUS, task.getStatus());
	    values.put(SQLHelper.TABLE_TASKS_COLUMN_VERSION, task.getVersion());
		database.update(SQLHelper.TABLE_TASKS, values, SQLHelper.TABLE_TASKS_COLUMN_ID + " = " + task.getId(), null);
		
		return findById(task.getId());
	}
	
	public void delete(Task task) {
		database.delete(SQLHelper.TABLE_TASKS, SQLHelper.TABLE_TASKS_COLUMN_ID + " = " + task.getId(), null);
	}

	public Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getInt(0));
		task.setBackendId(cursor.getInt(1));
		task.setName(cursor.getString(2));
		task.setStatus(cursor.getInt(3));
		task.setVersion(cursor.getInt(4));
		return task;
	}

	public Task findById(Integer id) {
	    Cursor cursor = database.query(SQLHelper.TABLE_TASKS,
	            allColumns, SQLHelper.TABLE_TASKS_COLUMN_ID + " = " + id, null,
	            null, null, null);
	    Task task = null;
	    if (cursor.moveToFirst()) {
	    	task = cursorToTask(cursor);
	    }
	    cursor.close();
	    
	    return task;
	}

	public Task findByBackendId(Integer id) {
	    Cursor cursor = database.query(SQLHelper.TABLE_TASKS,
	            allColumns, SQLHelper.TABLE_TASKS_COLUMN_BACKEND_ID + " = " + id, null,
	            null, null, null);
	    Task task = null;
	    if (cursor.moveToFirst()) {
	    	task = cursorToTask(cursor);
	    }
	    cursor.close();
	    
	    return task;
	}
	
	public List<Task> findAll() {
		List<Task> tasks = new ArrayList<Task>();

	    Cursor cursor = database.query(SQLHelper.TABLE_TASKS,
	            allColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	tasks.add(cursorToTask(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return tasks;
	}
	
	public List<Task> findNotDeleted() {
		List<Task> tasks = new ArrayList<Task>();

	    Cursor cursor = database.query(SQLHelper.TABLE_TASKS,
	            allColumns, SQLHelper.TABLE_TASKS_COLUMN_STATUS + " < " + 2, null,
	            null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	tasks.add(cursorToTask(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return tasks;
	}
	
	public List<Task> findNotInBackend() {
		List<Task> tasks = new ArrayList<Task>();

	    Cursor cursor = database.query(SQLHelper.TABLE_TASKS,
	            allColumns, SQLHelper.TABLE_TASKS_COLUMN_BACKEND_ID + " IS NULL", null,
	            null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	tasks.add(cursorToTask(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    return tasks;
	}
	
}
