package com.jdc.project.model.service.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Assignment;
import com.jdc.project.model.dto.Task;

@Component
public class AssignmentHelper {
	
	@Value("${assignment.empty}")
	private String nullAssignment;
	@Value("${assignment.empty.memberid}")
	private String noMemberid;
	@Value("${assignment.empty.taskid}")
	private String noTaskid;
	@Value("${assignment.empty.type}")
	private String noType;

	public void validate(Assignment assignment) {
		if (assignment == null) {
			throw new ProjectDbException(nullAssignment);
		}
		
		if ( !StringUtils.hasLength(String.valueOf(assignment.getMemberId())) ) {
			throw new ProjectDbException(noMemberid);
		}
		
		if (!StringUtils.hasLength(String.valueOf(assignment.getTaskId()))) {
			throw new ProjectDbException(noTaskid);
		}
		
		if ( !StringUtils.hasLength(assignment.getType().name())) {
			throw new ProjectDbException(noType);
		}
	}
	
	public Map<String,Object> insertParams(Assignment assignment){
		Map<String,Object> assignmentMap = new HashMap<>();
		assignmentMap.put("member_id", assignment.getMemberId());
		assignmentMap.put("task_id", assignment.getTaskId());
		assignmentMap.put("type", assignment.getType().name());
		assignmentMap.put("remark", assignment.getRemark());
		return assignmentMap;
	}
}
