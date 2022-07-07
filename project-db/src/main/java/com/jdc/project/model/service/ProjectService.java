package com.jdc.project.model.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Project;
import com.jdc.project.model.dto.ProjectVO;
import com.jdc.project.model.service.utils.ProjectHelper;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectHelper projectHelper;
	@Autowired
	private SimpleJdbcInsert projectInsert;
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Value("${project.empty.manager}")
	private String noManager;
	@Value("${project.empty.start}")
	private String noStartDate;
	
	private static RowMapper<Project> rowMapper= new BeanPropertyRowMapper(Project.class);
	
	
			
	// Map for query para with rs
	private MapSqlParameterSource map = new MapSqlParameterSource();
	
	public int create(Project project) {
		try {
			projectHelper.validate(project);			
			return projectInsert.executeAndReturnKey(projectHelper.insertParams(project)).intValue();
		}catch (DataIntegrityViolationException e) {
			throw new ProjectDbException(noManager);
		}catch (NullPointerException e) {
			throw new ProjectDbException(noStartDate);
		}
	}

	public Project findById(int id) {
		var query = "select p.id as id, p.name, p.description, p.manager as managerId, m.name as managerName, p.start as startDate, p.months as months"
				+ "  from project p "
				+ "INNER JOIN member m ON p.manager = m.id "
				+ "AND p.id = :id";
		return template.queryForObject(
				query, 
				Map.of("id", id), 
				rowMapper
				);
	}

	public List<Project> search(String project, String manager, LocalDate dateFrom, LocalDate dateTo) {		
		// concatenate query with one of those fields using StringBuilder
		var sb = new StringBuilder("""
					SELECT p.id, p.name, p.description, p.manager as managerId, p.start as startDate, p.months, m.name as managerName, m.login_id as managerLogin 
					FROM project p
					INNER JOIN member m 
					ON m.id = p.manager
						""") ;
		
		// check dateFrom is null
			if (dateFrom == null) {
				dateFrom = new Date(0).toLocalDate();			
			}
						
		// check dateTo is null
			if (dateTo == null) {
				dateTo = new Date(0).toLocalDate();
			}
					
			
		// search project with project's name
		if (StringUtils.hasLength(project)) {
			sb.append(" AND p.name LIKE :project").toString();
			map.addValue("project", project.toLowerCase().concat("%"));				
		}
		
		// search project with manager
		if(StringUtils.hasLength(manager)) {
			sb.append(" AND m.name LIKE :manager").toString();
			map.addValue("manager", manager.concat("%"));			
		}
					
		// search project with datefrom
		if (!dateFrom.equals(null) && !dateFrom.equals(new Date(0).toLocalDate())) {
			sb.append(" AND p.start < :dateFrom").toString();
			map.addValue("dateFrom", dateFrom);
		}
			
		// search project with dateTo
		if (!dateTo.equals(null) && !dateTo.equals(new Date(0).toLocalDate())){
			sb.append(" AND p.months > PERIOD_DIFF (DATE_FORMAT (:dateTo, \"%Y%m\"), DATE_FORMAT ( p.start, \"%Y%m\"))").toString();
			map.addValue("dateTo", dateTo);
		}
				
		return template.query(sb.toString(), map, rowMapper );
	}

	
	public int update(int id, String name, String description, LocalDate startDate, int month) {
		var sb = new StringBuilder ("""
				UPDATE project p
				SET p.id = :id, p.name = :name, p.description = :description, p.start = :start, p.months = :month
				WHERE 1=1 
				AND p.id = :id
				""");
		map.addValue("id", id);
		map.addValue("name", name);
		map.addValue("description", description);
		map.addValue("start", startDate);
		map.addValue("month", month);
		
		return template.update(sb.toString(), map);
	}

	public int deleteById(int id) {
		var sb = new StringBuilder("""
				DELETE from project p 
				WHERE p.id = :id
				""");
		map.addValue("id", id);
		return template.update(sb.toString(), map);
	}

}
