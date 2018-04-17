package com.tcb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcb.dao.DatasetRepository;
import com.tcb.dao.ElasticSearchDatasetsRepository;
import com.tcb.model.Dataset;
import com.tcb.service.DatasetService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DatasetsController {
	

	 
	//call service rather than repo 
	@Autowired
	private DatasetService dataService;
	
	@RequestMapping("/ds")
	public List<Dataset> home() {
		
		return dataService.findAll();
	}
	@RequestMapping(value="/find", method = RequestMethod.GET )
	@ResponseBody public List<Dataset> findByDataset(@RequestParam(value="name", required=false)String name ){
		return dataService.findDatasetByName(name);
	}
}
