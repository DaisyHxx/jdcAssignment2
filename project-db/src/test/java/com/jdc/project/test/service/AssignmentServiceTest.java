package com.jdc.project.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.StringUtils;

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.service.AssignmentService;
import com.jdc.project.test.utils.CommonUtils;

import static com.jdc.project.test.utils.AssignmentServiceTestUtils.*;
import static com.jdc.project.test.utils.TaskServiceTestUtils.dto;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(locations = "classpath:application.xml")
@Sql("classpath:/assignments.sql")
public class AssignmentServiceTest {

	@Autowired
	private AssignmentService service;
	
	@Value("${assignment.empty}")
	private String nullAssignment;
	@Value("${assignment.empty.memberid}")
	private String noMemberid;
	@Value("${assignment.empty.taskid}")
	private String noTaskid;
	@Value("${assignment.empty.type}")
	private String noType;
	
	//@Disabled
	@Order(1)
	@ParameterizedTest
	@ValueSource( strings = {
			"7,2,1,Review,Checked!!",
			"7,1,3,Review,Coding need to be better!!"
	})
	void should_create_assignment(String csv) {
		var expectedId = id(csv);
		var dto = dtoAssignment(csv);
		var generatedId = service.createAssignment(dto);
		
		assertEquals(expectedId,generatedId);
	}
	
	//@Disabled
	@Order(2)
	@ParameterizedTest
	@ValueSource(strings= {
			",,,,"
	})
	void should_not_create_empty_Assignment(String csv) {
		assertNull(dtoAssignment(csv));
		var exception = assertThrows(ProjectDbException.class,
				() -> service.createAssignment(dtoAssignment(csv))
				);
		assertEquals(nullAssignment,exception.getMessage());
	}
	
	//@Disabled
	@Order(3)
	@ParameterizedTest
	@ValueSource(strings = {
			",1,Response,Great"
	})
	void should_not_create_no_memberid(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createAssignment(dtoAssignment(csv)));
		assertEquals(noMemberid, exception.getMessage());
	}
	
	//@Disabled
	@Order(4)
	@ParameterizedTest
	@ValueSource(strings = {
			"1,,Review,Great"
	})
	void should_not_create_no_taskid(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createAssignment(dtoAssignment(csv)));
		assertEquals(noTaskid, exception.getMessage());
	}
	
	//@Disabled
	@Order(5)
	@ParameterizedTest
	@ValueSource(strings = {
			"1,4,,Great"
	})
	void should_not_create_no_type(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createAssignment(dtoAssignment(csv)));
		assertEquals(noType, exception.getMessage());
	}
	
	//@Disabled
	@Order(6)
	@ParameterizedTest
	@ValueSource(strings = {
			"3,Review,2,Design,Project DB,20220401,60,Aung Naing",
			"2,Response,1,Analysis,Project DB,20220401,35,Aung Naing"
	})
	void should_found_by_id(String csv) {
		var id = id (csv);
		var dto = dtoAssignment(csv);
		
		var result = service.findById(id);
		
		assertNotNull(result);
		
		assertEquals(dto.getId(), result.getId());
		assertEquals(dto.getType(), result.getType());
		assertEquals(dto.getTaskId(), result.getTaskId());
		assertEquals(dto.getPhase(), result.getPhase());
		assertEquals(dto.getTask(), result.getTask());
		assertEquals(dto.getStart(), result.getStart());
		assertEquals(dto.getDays(), result.getDays());
		assertEquals(dto.getMember(),result.getMember());
	}
	
	//@Disabled
	@Order(7)
	@ParameterizedTest
	@CsvSource({
			"3,2,Review,1",
			",,Review,4",
			",,Response,2"
	})
	void should_search_assignment (String memberid, String taskid, String type, int expectedSize) {
		var result = service.search(
				CommonUtils.integer(memberid), 
				CommonUtils.integer(taskid), 
				type);
		
		assertNotNull (result);
		assertEquals(expectedSize, result.size());
	}
	
	//@Disabled
	@Order(8)
	@ParameterizedTest
	@CsvSource({
		",,Response,3,2,2",
		"2,,Tested,3,6,1",
		"1,5,Response,3,7,1"
	})
	void should_updated(String memberid, String taskid, String type, String updateByField1, String updateByField2, int expectedSize) {
		var map = new HashMap<String,Object>();
		if (StringUtils.hasLength(updateByField1)) {
			map.put("memberid",CommonUtils.integer(updateByField1));			
		}
		if (StringUtils.hasLength(updateByField2)) {
			map.put("taskid", CommonUtils.integer(updateByField2));
		}
		
		var result = service.update(
				CommonUtils.integer(memberid), 
				CommonUtils.integer(taskid), 
				type,
				map);
		
		assertNotNull(result);
		assertEquals(expectedSize, result);
	}
	
	@Order(9)
	@ParameterizedTest
	@CsvSource({
		"1,1",
		"4,1"
	})
	void should_deleted (int id, int expected) {
		var result = service.deleteById(id);
		assertEquals(expected, result);
	}
}
