package com.jdc.project.test.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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

import com.jdc.project.model.ProjectDbException;
import com.jdc.project.model.dto.Task;
import com.jdc.project.model.dto.Task.Phase;
import com.jdc.project.model.service.TaskService;
import com.jdc.project.test.utils.CommonUtils;

import static com.jdc.project.test.utils.TaskServiceTestUtils.*;


@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(locations = "classpath:application.xml")
@Sql("classpath:/tasks.sql")
public class TaskServiceTest {

	@Autowired
	private TaskService service;
	
	@Value("${task.empty}")
	private String nullTask;
	@Value("${task.empty.phase}")
	private String noPhase;
	@Value("${task.empty.start}")
	private String noStartDate;
	@Value("${task.empty.projectid}")
	private String noProjectId;
	
	//@Disabled
	@Order(1)
	@ParameterizedTest
	@ValueSource (strings = {
			"9,Analysis,Book Store,20220510,30,1",
			"9,Design,Book Store,20220510,25,1",
			"9,Coding,Order Me,20220510,30,5"
	})
	void should_create_task(String csv) {
		var expectedId = id(csv);
		var task = dto(csv);
		
		var id = service.createTask(task);
		
		assertEquals(expectedId, id);
	}
	
	//@Disabled
	@Order(2)
	@ParameterizedTest
	@ValueSource(strings= {
			",,,,"
	})
	void should_not_create_empty_Task(String csv) {
		assertNull(dto(csv));
		var exception = assertThrows(ProjectDbException.class,
				() -> service.createTask(dto(csv))
				);
		assertEquals(nullTask,exception.getMessage());
	}
	
	//@Disabled
	@Order(3)
	@ParameterizedTest
	@ValueSource(strings = {
			",Book Store,20220510,30,1",
			",Book Store,20220510,30,1",
			",Order Me,20220510,30,5",
	})
	void should_not_create_no_Phase(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createTask(dto(csv)));
		assertEquals(noPhase, exception.getMessage());
	}
	
	
	//@Disabled
	@Order(4)
	@ParameterizedTest
	@ValueSource(strings = {
			"Analysis,Book Store,,30,1",
			"Design,Book Store,,30,1",
			"Coding,Order Me,,30,5",
	})
	void should_not_create_no_StartDate(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createTask(dto(csv)));
		assertEquals(noStartDate, exception.getMessage());
	}
	
	//@Disabled
	@Order(5)
	@ParameterizedTest
	@ValueSource(strings = {
			"9,Analysis,Book Store,20220510,30,,2,Aung Aung,aungaung",
			"9,Design,Book Store,20220510,30,,2,Aung Aung,aungaung",
			"9,Coding,Order Me,20220510,30,,2,Aung Aung,aungaung",
	})
	void should_not_create_no_ProjectId(String csv) {
		var exception = assertThrows(ProjectDbException.class, () -> service.createTask(dto(csv)));
		assertEquals(noProjectId, exception.getMessage());
	}
	
	//@Disabled
	@Order(6)
	@ParameterizedTest
	@CsvSource({
			"Analysis,,,,,2",
			"Analysis,The Movies,,,,1",
			",,2022-05-10,,6,3"
	})
	void should_search_correctly(
			String phase,
			String task,
			LocalDate start,
			String days,
			String project_id,
			int size
			) {
		
		var result = service.search(phase, task, start, CommonUtils.integer(days), CommonUtils.integer(project_id));
		
		assertNotNull(result);
		assertEquals(size, result.size());		
	}
	
	//@Disabled
	@Order(7)
	@ParameterizedTest
	@ValueSource (strings = {
			"6,Analysis,The Movies,20220510,30,6,3,Aung Naing,aungnaing",
			"2,Design,Project DB,20220401,60,2,3,Aung Naing,aungnaing",
			"4,Testing,Project DB,20220401,70,2,3,Aung Naing,aungnaing",
	})
	void should_find_with_id (String csv) {
		var id = id(csv);
		var dto = dto(csv);
		
		var result = service.findById(id);
		
		assertNotNull(result);
		
		assertEquals(dto.getId(), result.getId());
		assertEquals(dto.getPhase(), result.getPhase());
		assertEquals(dto.getTask(), result.getTask());
		assertEquals(dto.getStart(), result.getStart());
		assertEquals(dto.getDays(), result.getDays());
		assertEquals(dto.getProjectId(), result.getProjectId());
		assertEquals(dto.getManagerId(), result.getManagerId());
		assertEquals(dto.getManagerName(), result.getManagerName());
		assertEquals(dto.getManagerLogin(), result.getManagerLogin());
	}
	
	
	// test update with test id 
	//@Disabled
	@Order(8)
	@ParameterizedTest
	@CsvSource({
		"6,Analysis,Analysis of The Movies,2022-05-10,30,1",
		"4,Testing,Project DB,2022-04-01,70,1"
	})
	void should_updated (int id, String phase, String task, LocalDate start, int days, int expected) {
		var result = service.updateTask(id, Phase.valueOf(phase), task, start, days);
		assertEquals(expected, result);
	}
	
	@Order(9)
	@ParameterizedTest
	@CsvSource({
		"1,1",
		"4,1",
		"5,1"
	})
	void should_deleted (int id, int expected) {
		var result = service.deleteById(id);
		assertEquals(expected, result);
	}
}
