package model.dao;

import model.Account;
import model.ModelException;

public interface AccountDAO {
	Account findByEmail(String email) throws ModelException;
}
