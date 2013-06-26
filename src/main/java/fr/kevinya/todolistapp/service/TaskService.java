package fr.kevinya.todolistapp.service;

import java.util.List;

import fr.kevinya.todolistapp.entity.Task;

public interface TaskService {
	public Task create(Task task);
	public Task update(Task task);
	public Task delete(Task task);
	public List<Task> findAll();
	public List<Task> findNotDeleted();
	public Task findById(Integer id);
}
