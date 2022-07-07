package com.jdc.project.model.service;

import java.time.LocalDate;
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
import com.jdc.project.model.dto.Task;
import com.jdc.project.model.dto.Task.Phase;
import com.jdc.project.model.service.utils.TaskHelper;

@Service
public class TaskService {
	
	@Autowired
	@Qualifier("taskInsert")
	private SimpleJdbcInsert taskInsert;
	@Autowired
	private TaskHelper taskHelper;
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Value("${task.empty.start}")
	private String noStartDate;	
	@Value("${task.empty.phase}")
	private String noPhase;
	@Value("${task.empty.projectid}")
	private String noProjectId;
	
	
	private RowMapper<Task> rowMapper;
	public TaskService() {
		rowMapper = new BeanPropertyRowMapper<>(Task.class);
	}
	// insert
	public int createTask(Task task) {
		try {
			taskHelper.validate(task);
			var result = taskInsert.executeAndReturnKey(taskHelper.insertParams(task));
			return result.intValue();
		}catch(NullPointerException e) {
			throw new ProjectDbException (noStartDate);
		}catch(DataIntegrityViolationException e) {
			throw new ProjectDbException (noProjectId);
		}
	}
	
	// find by id
	public Task findById(int id) {
		var sql = """
				SELECT t.id, t.phase, t.name as task, t.start, t.days, t.project_id as projectId, p.name as projectName, m.id as managerId, m.name as managerName, m.login_id as managerLogin 
				FROM task t
				INNER JOIN project p
				ON t.project_id = p.id 
				INNER JOIN member m
				ON p.manager = m.id
				WHERE t.id = :id
				""";
		return template.queryForObject(
				sql, 
				Map.of("id",id),
				rowMapper);
	}
			
	// find
	public List<Task> search (String phase, String task, LocalDate start, int days, int project_id){
		var sb = new StringBuffer("""
				SELECT t.id, t.phase, t.name as task, t.start, t.days, t.project_id as projectId, p.name as projectName, m.id as managerId, m.name as managerName, m.login_id as managerLogin 
				FROM task t
				INNER JOIN project p
				ON t.project_id = p.id
				INNER JOIN
				(SELECT id,name,login_id FROM member) m
				ON p.manager = m.id
				WHERE 1=1 
				""");
		var params = new HashMap<String, Object>();
		
		if (phase != null) {
			sb.append(" AND t.phase = :phase");
			params.put("phase", phase );
		}
		
		if (StringUtils.hasLength(task)) {
			sb.append(" AND t.name = :name");
			params.put("name", task);
		}
		
		if (start != null) {
			sb.append("AND t.start = :start");
			params.put("start", start);
		}
		
		if ( !String.valueOf(days).isEmpty() && days != 0) {
			sb.append(" AND t.days = :days");
			params.put("days", days);
		}
		if ( !String.valueOf(project_id).isEmpty() && project_id != 0) {
			sb.append(" AND t.project_id = :project_id");
			params.put("project_id", project_id);
		}
		
		return template.query(sb.toString(), params, rowMapper);
	}
	
	
	// update
	public int updateTask(int id, Phase phase,String name, LocalDate start, int days ) {
		var sb = new StringBuffer("""
				UPDATE task t
				SET t.phase = :phase, t.name= :name, t.start = :start
				WHERE t.id = :id
				""");
		var params = new HashMap<String, Object>();
		params.put("phase", phase.name());
		params.put("name", name);
		params.put("start", start);
		params.put("days", days);
		params.put("id", id);
		
		return template.update(sb.toString(), params);
	}
	
	// delete
	public int deleteById (int id) {
		var sb = new StringBuffer("""
				DELETE FROM task t
				WHERE t.id = :id
				""");
		return template.update(sb.toString(), Map.of("id", id));

	}

}
