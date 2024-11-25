package com.uaefts.portal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int updateEntityNameById(Long recId,String refNo) {
		String sql="update testflow set name=? where id=?";
		return jdbcTemplate.update(sql,refNo,recId);
	}

}
