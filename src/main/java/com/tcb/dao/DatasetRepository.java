package com.tcb.dao;

import java.util.List;

import com.tcb.model.Dataset;


public interface DatasetRepository  {
//    List<Dataset> findDatasetByAuthor(String author);  
    List<Dataset> findDatasetByName(String name);  
    List<Dataset> findAll();
}
