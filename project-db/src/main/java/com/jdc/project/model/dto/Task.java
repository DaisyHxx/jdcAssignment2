package com.jdc.project.model.dto;

import java.time.LocalDate;
import java.util.Arrays;

public class Task {

	private int id;
	private Phase phase;
	private String task;
	private LocalDate start;
	private int days;
	
	private int projectId;
	private String projectName;
	
	private int managerId;
	private String managerName;
	private String managerLogin;

	public enum Phase {
		Analysis, Design, Coding, Testing, Release;
				
		// find enum value
		public static Phase getEnumFromString(String value) {
			return Arrays.stream(values())
					.filter( ph -> ph.name().equalsIgnoreCase(value))
					.findAny()
					.orElse(null);
		}
	}

	public Task() {
		
	}
	public Task(String phase, String task, LocalDate start, int days, int projectId) {
		this.phase = Phase.getEnumFromString(phase);
		this.task = task;
		this.start = start;
		this.days = days;
		this.projectId = projectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerLogin() {
		return managerLogin;
	}

	public void setManagerLogin(String managerLogin) {
		this.managerLogin = managerLogin;
	}

}
