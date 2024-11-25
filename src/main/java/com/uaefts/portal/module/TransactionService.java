package com.uaefts.portal.module;

import java.io.File;

public interface TransactionService {

	void insertTransaction(String baseFileName, String status);

	void updateTransactionStatus(String baseFileName, String status);

	boolean persistFtrFile(File ftrFile);

	void sendFailureEmail(String baseFileName);
}
