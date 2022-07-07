package com.jdc.project.test.utils;

import static com.jdc.project.test.utils.CommonUtils.integer;
import static com.jdc.project.test.utils.CommonUtils.localDate;

import com.jdc.project.model.dto.Task;
import com.jdc.project.model.dto.Task.Phase;

public class TaskServiceTestUtils {

	public static int id(String csv) {
		var array = csv.split(",");
		return Integer.parseInt(array[0]);
	}
	
public static Task dto(String csv) {
		
		var array = csv.split(",");
		
		if (array.length == 0) {
			return null;
		}
		
		if(array.length == 9) {
			var dto = new Task();
			dto.setId(integer(array[0]));
			dto.setPhase(Phase.valueOf(array[1]));
			dto.setTask(array[2]);
			dto.setStart(localDate(array[3]));
			dto.setDays(integer(array[4]));
			dto.setProjectId(integer(array[5]));			
			dto.setManagerId(integer(array[6]));
			dto.setManagerName(array[7]);
			dto.setManagerLogin(array[8]);		
			return dto;
		}
		
		if(array.length == 6) {
			return new Task(
					array[1], 
					array[2],  
					localDate(array[3]), 
					integer(array[4]),
					integer(array[5]));	
		}
		
		return new Task(
				array[0], 
				array[1],  
				localDate(array[2]), 
				integer(array[3]),
				integer(array[4]));
	}
}
