package com.tcb.service;

import java.util.List;

import com.tcb.model.Dataset;

public interface DatasetService {
	public List<Dataset> findAll();
	public List<Dataset> findDatasetByName(String name);
//	public User findByLogin(String login);
//	
//	public void addUser(String login, String pwd, String role, String email);
	
}
