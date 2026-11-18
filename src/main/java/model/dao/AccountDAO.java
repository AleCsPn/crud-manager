package model.dao;

import model.Account;
import model.ModelException;

public interface AccountDAO {
	// Busca a conta pelo e-mail (usado no login). Devolve null se não existir.
	Account findByEmail(String email) throws ModelException;
}
