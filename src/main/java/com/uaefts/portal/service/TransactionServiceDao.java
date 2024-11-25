package com.uaefts.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uaefts.portal.repository.TransactionRepository;

@Service
public class TransactionServiceDao {

	@Autowired
	private TransactionRepository transactionRepository;

	public String updateTransaction(Long id,String refNo) {
		try {
			transactionRepository.updateEntityNameById(id, refNo);
			return "success";
		}
		catch(Exception e) {
			return "error-"+e.getMessage();
		}
		
	}

}
