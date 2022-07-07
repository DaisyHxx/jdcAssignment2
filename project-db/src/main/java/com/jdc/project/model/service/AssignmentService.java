package com.jdc.project.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Assignment;
import com.jdc.project.model.service.utils.AssignmentHelper;

@Service
public class AssignmentService {
	
	@Autowired
	private AssignmentHelper assignmentHelper;
	
	@Autowired
	@Qualifier("assignmentInsert")
	private SimpleJdbcInsert assignmentInsert;
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Value("${assignment.empty.memberid}")
	private String noMemberid;
	@Value("${assignment.empty.taskid}")
	private String noTaskid;
	@Value("${assignment.empty.type}")
	private String noType;
	
	private RowMapper<Assignment> rowMapper;
	public AssignmentService () {
		rowMapper = new BeanPropertyRowMapper<>(Assignment.class);
	}
	//create
	public int createAssignment(Assignment assignment) {
		var generatedId = 0;
		try {
			assignmentHelper.validate(assignment);
			generatedId = assignmentInsert.executeAndReturnKey(assignmentHelper.insertParams(assignment)).intValue();
		}catch(DataIntegrityViolationException e) {
			if ( assignment.getMemberId() == 0  ) {
				throw new ProjectDbException (noMemberid);
			}else {
				throw new ProjectDbException (noTaskid);
			}
		}catch(NullPointerException e) {
			throw new ProjectDbException(noType);
		}
		return generatedId;
	}

	
	// find by id
	public Assignment findById (int id) {
		var sql = """
				SELECT a.id, a.type, t.id as taskId, t.phase, t.name as task, t.start, t.days, m.name as member
				FROM assignment a
				INNER JOIN task t
				ON t.id = a.task_id
				INNER JOIN member m
				ON m.id = a.member_id
				WHERE a.id = :id
				""";
		return template.queryForObject(sql, Map.of("id",id), rowMapper);
	}
	
	// search
	public List<Assignment> search (int memberid, int taskid, String type){
		var sb = new StringBuffer("""
				SELECT a.member_id as memberId, a.task_id as taskId, a.type
				FROM assignment a
				INNER JOIN member m
				ON a.member_id = m.id
				INNER JOIN task t
				ON a.task_id = t.id
				WHERE 1=1
				""");
		var paramsMap = new HashMap<String,Object>();
		
		if (!String.valueOf(memberid).isEmpty() && memberid != 0) {
			sb.append(" AND a.member_id = :memberid");
			paramsMap.put("memberid",memberid);
		}
		
		if ( !String.valueOf(taskid).isEmpty() && taskid != 0) {
			sb.append(" AND a.task_id = :taskid");
			paramsMap.put("taskid", taskid);
		}
		
		if ( StringUtils.hasLength(type) ) {
			sb.append(" AND a.type = :type");
			paramsMap.put("type", type);
		}
		
		return template.query(sb.toString(), paramsMap, rowMapper);
	}
	
	
	// update : return no of rows affected
	public int update(int memberid, int taskid, String type, Map<String,Object> updateByFields ) {
		var sb = new StringBuffer("""
				UPDATE assignment a
				INNER JOIN member m
				ON a.member_id = m.id
				INNER JOIN task t
				ON a.task_id = t.id
				SET a.id = a.id
				""");
		
		var paramsMap = new HashMap<String,Object>();
		
		if (!String.valueOf(memberid).isEmpty() && memberid != 0) {
			sb.append(",a.member_id = :memberid");
			paramsMap.put("memberid",memberid);
		}
		
		if ( !String.valueOf(taskid).isEmpty() && taskid != 0) {
			sb.append(",a.task_id = :taskid");
			paramsMap.put("taskid", taskid);
		}
		
		if ( StringUtils.hasLength(type) ) {
			sb.append(",a.type = :type");
			paramsMap.put("type", type);
		}
		
		if (!updateByFields.isEmpty()) {
			sb.append(" WHERE 1=1");
			if ( Integer.parseInt(updateByFields.get("memberid").toString()) != 0 ) {
				sb.append(" AND a.member_id = :member_id");
				paramsMap.put("member_id", Integer.parseInt(updateByFields.get("memberid").toString()));
			}
			if ( Integer.parseInt(updateByFields.get("taskid").toString()) != 0 ) {
				sb.append(" AND a.task_id = :task_id");
				paramsMap.put("task_id", Integer.parseInt(updateByFields.get("taskid").toString()));
			}
		}
		
		return template.update(sb.toString(), paramsMap);
	}
	
	
	// delete
	public int deleteById (int id) {
		var sb = new StringBuffer("""
				DELETE FROM assignment a
				WHERE a.id = :id
				""");
		return template.update(sb.toString(), Map.of("id",id));
	}
}
