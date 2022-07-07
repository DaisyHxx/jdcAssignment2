package com.jdc.project.model.service.utils;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Project;

@Component
public class ProjectHelper {
	
	@Value("${project.empty}")
	private String nullProject;
	@Value("${project.empty.name}")
	private String noName;
	@Value("${project.empty.manager}")
	private String noManager;
	@Value("${project.empty.start}")
	private String noStartDate;	


	//validate for noname, nnomanger, nodate
	public void validate(Project dto) {
		if (dto == null) {
			throw new ProjectDbException(nullProject);
		}
		if (!StringUtils.hasLength(dto.getName())) {
			throw new ProjectDbException(noName);
		}
		
		if (!StringUtils.hasLength( String.valueOf(dto.getManagerId()) )) {
			throw new ProjectDbException(noManager);
		}
		
		if (!StringUtils.hasLength(String.valueOf(dto.getStartDate()))) {
			throw new ProjectDbException(noStartDate);
		}
	}

	// map db column to Project obj
	public Map<String, Object> insertParams(Project dto) {
		// TODO
		Map<String, Object> projectMap = new HashMap<>();
		projectMap.put("name", dto.getName());
		projectMap.put("description", dto.getDescription());
		projectMap.put("manager", dto.getManagerId());
		projectMap.put("start", Date.valueOf(dto.getStartDate()));
		projectMap.put("months", dto.getMonths());
		
		return projectMap;
	}
}
