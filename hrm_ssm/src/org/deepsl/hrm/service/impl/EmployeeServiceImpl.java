package org.deepsl.hrm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.deepsl.hrm.dao.EmployeeDao;
import org.deepsl.hrm.domain.Employee;
import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.EmployeeService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT)
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeDao EmployeeDao;
	
	
	//分页查询部门
	@Override
	public List<Employee> findEmployee(Employee Employee, PageModel pageModel) {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("firstIndex", pageModel.getFirstLimitParam());
		hashMap.put("size", pageModel.getPageSize());
		
		hashMap.put("employee", Employee);
		if (Employee.getDept()!=null) {
			hashMap.put("dept_id", Employee.getDept().getId());
		}
		if (Employee.getJob()!=null) {
			hashMap.put("job_id", Employee.getJob().getId());
		}
		
		List<Employee> Employees = EmployeeDao.selectByPage(hashMap);
		return Employees;
	}


	@Override
	public void removeEmployeeById(Integer id) {
		EmployeeDao.deleteById(id);
		
	}

	@Override
	public void addEmployee(Employee Employee) {
		EmployeeDao.save(Employee);
		
	}

	@Override
	public Employee findEmployeeById(Integer id) {
		
		return EmployeeDao.selectById(id);
	}

	@Override
	public void modifyEmployee(Employee Employee) {
		EmployeeDao.update(Employee);
		
	}
	
	//获取部门数量的方法
	@Override
	public int findEmployeeCount(Employee Employee) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		
		hashMap.put("employee", Employee);
		if (Employee.getDept()!=null) {
			hashMap.put("dept_id", Employee.getDept().getId());
		}
		if (Employee.getJob()!=null) {
			hashMap.put("job_id", Employee.getJob().getId());
		}
		
		
		
		return EmployeeDao.count(hashMap);
	}

	
	
}
