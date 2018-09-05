package com.manageme.app.service;

import com.manageme.app.domain.Employee;
import com.manageme.app.repository.EmployeeRepository;
import com.manageme.app.repository.SeparationApplicationRepository;
import com.manageme.app.security.AuthoritiesConstants;
import com.manageme.app.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Save a employee.
     *
     * @param employee the entity to save
     * @return the persisted entity
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);        return employeeRepository.save(employee);
    }

    /**
     * Get all the employees.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.debug("Request to get all Employees");
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.HUMAN_RESOURCES)){
            return employeeRepository.findAll();
        } else {
            if (SecurityUtils.getCurrentUserLogin().isPresent()) {
                return employeeRepository.findAllByUserLogin(SecurityUtils.getCurrentUserLogin().get());
            }
        }
        return Collections.emptyList();
    }


    /**
     * Get one employee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
