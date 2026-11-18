package model.dao;

import java.util.List;

import model.Candidate;
import model.ModelException;

public interface CandidateDAO {
	boolean save(Candidate candidate) throws ModelException;
	boolean update(Candidate candidate) throws ModelException;
	boolean delete(Candidate candidate) throws ModelException;
	List<Candidate> listAll() throws ModelException;
	Candidate findById(int id) throws ModelException;
	List<Candidate> listPage(int page, int pageSize) throws ModelException;
	int countAll() throws ModelException;
}
