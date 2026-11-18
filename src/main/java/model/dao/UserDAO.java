package model.dao;

import java.util.List;

import model.ModelException;
import model.User;

public interface UserDAO {
	boolean save(User user) throws ModelException ;
	boolean update(User user) throws ModelException;
	boolean delete(User user) throws ModelException;
	List<User> listAll() throws ModelException;
	User findById(int id) throws ModelException;
	// Paginação: devolve apenas os registros da página pedida (page começa em 1)
	List<User> listPage(int page, int pageSize) throws ModelException;
	// Total de registros (necessário para calcular quantas páginas existem)
	int countAll() throws ModelException;
}
