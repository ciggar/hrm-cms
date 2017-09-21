package org.deepsl.hrm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.deepsl.hrm.dao.DeptDao;
import org.deepsl.hrm.domain.Dept;
import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.DeptService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT)
@Service
public class DeptServiceImpl implements DeptService{
	
	@Autowired
	private DeptDao deptDao;
	
	
	//分页查询部门
	@Override
	public List<Dept> findDept(Dept dept, PageModel pageModel) {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("firstIndex", pageModel.getFirstLimitParam());
		hashMap.put("size", pageModel.getPageSize());
		hashMap.put("name", dept.getName());
		
		List<Dept> depts = deptDao.selectByPage(hashMap);
		return depts;
	}

	@Override
	public List<Dept> findAllDept() {
		// TODO Auto-generated method stub
		return deptDao.selectAllDept();
	}

	@Override
	public void removeDeptById(Integer id) {
		deptDao.deleteById(id);
		
	}

	@Override
	public void addDept(Dept dept) {
		deptDao.save(dept);
		
	}

	@Override
	public Dept findDeptById(Integer id) {
		
		return deptDao.selectById(id);
	}

	@Override
	public void modifyDept(Dept dept) {
		deptDao.update(dept);
		
	}
	
	//获取部门数量的方法
	@Override
	public int finDeptCount(Dept dept) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", dept.getName());
		return deptDao.count(hashMap);
	}
	
}
