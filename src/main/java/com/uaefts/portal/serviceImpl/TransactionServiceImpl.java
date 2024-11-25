package com.uaefts.portal.serviceImpl;

import java.io.File;

import org.springframework.stereotype.Service;

import com.uaefts.portal.module.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public void insertTransaction(String baseFileName, String status) {
        // Logic to insert transaction into DB
    }

    @Override
    public void updateTransactionStatus(String baseFileName, String status) {
        // Logic to update transaction status in DB
    }

    @Override
    public boolean persistFtrFile(File ftrFile) {
        // Logic to call stored procedure and persist file
        return true;
    }

    @Override
    public void sendFailureEmail(String baseFileName) {
        // Logic to send email on failure
    }

	
}
