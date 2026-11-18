package model.dao;

import model.Account;
import model.ModelException;

public class MySQLAccountDAO implements AccountDAO {

	@Override
	public Account findByEmail(String email) throws ModelException {
		DBHandler db = new DBHandler();

		// PreparedStatement: o e-mail digitado vai como parâmetro (?) e nunca 
		// é concatenado na SQL, o que impede SQL Injection no formulário de login
		String sql = "SELECT id, name, email, password_hash FROM accounts WHERE email = ?";

		db.prepareStatement(sql);
		db.setString(1, email);
		db.executeQuery();

		Account account = null;
		if (db.next()) {
			account = new Account(db.getInt("id"));
			account.setName(db.getString("name"));
			account.setEmail(db.getString("email"));
			account.setPasswordHash(db.getString("password_hash"));
		}
		db.close();

		return account;
	}
}
