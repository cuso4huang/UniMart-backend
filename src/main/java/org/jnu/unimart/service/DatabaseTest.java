package org.jnu.unimart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void testConnection() {
		try {
			String sql = "SELECT 1"; // 执行简单的查询，验证数据库连接是否正常
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
			System.out.println("Database connection test result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connection failed!");
		}
	}
}
