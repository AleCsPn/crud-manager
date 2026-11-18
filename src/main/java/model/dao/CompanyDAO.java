package model.dao;

import java.util.List;

import model.ModelException;
import model.Company;

public interface CompanyDAO {
	boolean save(Company company) throws ModelException;
	boolean update(Company company) throws ModelException;
	boolean delete(Company company) throws ModelException;
	List<Company> listAll() throws ModelException;
	Company findById(int id) throws ModelException;
	// Paginação: devolve apenas os registros da página pedida (page começa em 1)
	List<Company> listPage(int page, int pageSize) throws ModelException;
	// Total de registros (necessário para calcular quantas páginas existem)
	int countAll() throws ModelException;
}
