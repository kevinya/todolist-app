package fr.kevinya.todolistapp.service;

import java.util.List;
import java.util.Observable;

import android.content.Context;
import fr.kevinya.todolistapp.dao.TaskDAO;
import fr.kevinya.todolistapp.datasource.TaskDataSource;
import fr.kevinya.todolistapp.entity.Task;

public class TaskServiceImpl extends Observable implements TaskServiceLocal {
	private static TaskServiceImpl instance;
	private TaskDAO taskDao;
	private TaskDataSource taskDataSource;
	
	public TaskServiceImpl() {
	}
	
	public TaskServiceImpl(Context context) {
		taskDao = new TaskDAO(context);
		taskDataSource = new TaskDataSource(this, context);
	}
	
	public static TaskServiceImpl getInstance(Context context) {
		if (instance == null) {
			instance = new TaskServiceImpl(context);
		}
		return instance;
	}
	
	public TaskDAO getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDAO taskDao) {
		this.taskDao = taskDao;
	}

	public TaskDataSource getTaskDataSource() {
		return taskDataSource;
	}

	public void setTaskDataSource(TaskDataSource taskDataSource) {
		this.taskDataSource = taskDataSource;
	}

	public Task create(Task task) {
		taskDao.open();
		Task newTask = taskDao.create(task);
		taskDao.close();
		triggerObservers();
		return newTask;
	}

	public Task update(Task task) {
		task.setVersion(task.getVersion() + 1);
		taskDao.open();
		Task newTask = taskDao.update(task);
		taskDao.close();
		triggerObservers();
		return newTask;
	}

	public Task delete(Task task) {
		task.setStatus(2);
		return update(task);
	}

	public List<Task> findAll() {
		taskDao.open();
		List<Task> tasks = taskDao.findAll();
		taskDao.close();
		return tasks;
	}

	public List<Task> findNotDeleted() {
		taskDao.open();
		List<Task> tasks = taskDao.findNotDeleted();
		taskDao.close();
		return tasks;
	}

	public Task findById(Integer id) {
		taskDao.open();
		Task task = taskDao.findById(id);
		taskDao.close();
		return task;
	}

	public List<Task> findNotInBackend() {
		taskDao.open();
		List<Task> tasks = taskDao.findNotInBackend();
		taskDao.close();
		return tasks;
	}

	public Task findByBackendId(Integer id) {
		taskDao.open();
		Task task = taskDao.findByBackendId(id);
		taskDao.close();
		return task;
	}
	
	public void triggerObservers() {
		setChanged();
		notifyObservers();
	}

}
