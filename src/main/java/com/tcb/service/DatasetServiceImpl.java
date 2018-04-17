package com.tcb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcb.dao.DatasetRepository;
import com.tcb.model.Dataset;

@Service
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private DatasetRepository dataDao;

	@Override
	public List<Dataset> findAll() {
	return dataDao.findAll();
	}

	@Override
	public List<Dataset> findDatasetByName(String name) {
		// TODO Auto-generated method stub
		return dataDao.findDatasetByName(name);
	}
	
	
	
//	@Override
//	public void addUser(String login, String pwd, String role, String email) {
//		String pwde = bCryptPasswordEncoder.encode(pwd);
//		User tmpUser = new User();
//		tmpUser.setPassword(pwde);
//		tmpUser.setLogin(login);
//		tmpUser.setEnabled(1);
//		tmpUser.setRole(role);
//		tmpUser.setEmail(email);
//		userDao.save(tmpUser);
//	}
//	
//	@Override
//	public User findByLogin(String login) {
//		return userDao.findByLogin(login);
//	}
	
	
}
