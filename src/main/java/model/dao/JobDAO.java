package model.dao;

import java.util.List;

import model.Job;
import model.ModelException;

public interface JobDAO {
	boolean save(Job job) throws ModelException;
	boolean update(Job job) throws ModelException;
	boolean delete(Job job) throws ModelException;
	List<Job> listAll() throws ModelException;
	Job findById(int id) throws ModelException;
	List<Job> listPage(int page, int pageSize) throws ModelException;
	int countAll() throws ModelException;
}
