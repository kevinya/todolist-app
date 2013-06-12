package fr.kevinya.todolistapp.entity;

public class Task {
	private Integer id;
	private String name;
	private Integer status;
	private Integer version;
	
	public Task() {	
	}
	
	public Task(Integer id, String name, Integer status, Integer version) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
}
