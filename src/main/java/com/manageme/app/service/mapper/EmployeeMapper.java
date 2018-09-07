package com.manageme.app.service.mapper;

import com.manageme.app.domain.*;
import com.manageme.app.service.dto.EmployeeDTO;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, DepartmentMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(target = "name", expression = "java(fullName(employee))")
    @Mapping(target = "authorities", expression = "java(authoritiesToStringSet(employee.getUser()))")
    @Mapping(source = "user.login", target="login")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "assets", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "separationApplication", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
    
    default Set<String> authoritiesToStringSet(User user) {
    	if (user == null) {
    		return null;
    	}
    	Set<Authority> authorities = user.getAuthorities();
    	if (authorities == null) {
    		return null;
    	}
    	return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
    }
    
    default String fullName(Employee employee) {
    	if (employee == null) {
    		return null;
    	}
		
    	User user = employee.getUser();
    	if (user == null) {
    		return null;
    	}
    	
    	return user.getName();
    }
}
