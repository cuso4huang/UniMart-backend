package org.jnu.unimart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void testConnection() {
		try {
			String sql = "SELECT 1";
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
			System.out.println("Database connection test result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
