package com.jdc.project.test.utils;

import com.jdc.project.model.dto.Assignment;
import com.jdc.project.model.dto.Assignment.Type;
import com.jdc.project.model.dto.Task.Phase;

import static com.jdc.project.test.utils.CommonUtils.*;

public class AssignmentServiceTestUtils {

	public static int id(String csv) {
		var array = csv.split(",");
		return Integer.parseInt(array[0]);
	}
	
	public static Assignment dtoAssignment(String csv) {
		var array = csv.split(",");
		
		if (array.length == 0) {
			return null;
		}
		
		if (array.length == 5) {
			return new Assignment(
					integer(array[1]),
					integer(array[2]),
					array[3],
					array[4]
					);
		}
		
		if (array.length == 8) {
			var assignment = new Assignment();
			assignment.setId(integer(array[0]));
			assignment.setType( Type.valueOf(array[1]) );
			assignment.setTaskId(integer(array[2]));
			assignment.setPhase(Phase.valueOf(array[3]));
			assignment.setTask(array[4]);
			assignment.setStart(localDate(array[5]));
			assignment.setDays(integer(array[6]));
			assignment.setMember(array[7]);
			return assignment;
		}
		
		return new Assignment(
				integer(array[0]),
				integer(array[1]),
				array[2],
				array[3]
				);
	}
}
