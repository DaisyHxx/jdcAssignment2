package com.jdc.project.model.service.utils;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Project;
import com.jdc.project.model.dto.Task;
import com.jdc.project.model.dto.Task.Phase;

@Component
public class TaskHelper {

	@Value("${task.empty}")
	private String nullTask;
	@Value("${task.empty.phase}")
	private String noPhase;
	@Value("${task.empty.start}")
	private String noStartDate;
	@Value("${task.empty.projectid}")
	private String noProjectId;
	
	// validate Task
	public void validate(Task task) {
		if (task == null) {
			throw new ProjectDbException(nullTask);
		}
		
		if ( task.getPhase() == null ) {
			throw new ProjectDbException(noPhase);
		}
		
		if (!StringUtils.hasLength(String.valueOf(task.getStart()))) {
			throw new ProjectDbException(noStartDate);
		}
		
		if (String.valueOf(task.getProjectId()) == null) {
			throw new ProjectDbException(noProjectId);
		}
	}
	
	// map db column to task obj
	public Map<String, Object> insertParams(Task task) {
		// TODO
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("phase", task.getPhase());
		taskMap.put("name", task.getProjectName());
		taskMap.put("start", Date.valueOf(task.getStart()));
		taskMap.put("days", task.getDays());
		taskMap.put("project_id", task.getProjectId());
		
		return taskMap;
	}
}
