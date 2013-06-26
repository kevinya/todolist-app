package fr.kevinya.todolistapp.service;

import java.util.List;

import fr.kevinya.todolistapp.entity.Task;

public interface TaskServiceLocal extends TaskService {
	public List<Task> findNotInBackend();
	public Task findByBackendId(Integer id);
	
}
