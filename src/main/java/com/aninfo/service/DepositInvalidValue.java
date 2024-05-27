package com.aninfo.service;

import java.util.concurrent.atomic.AtomicReference;

import com.aninfo.exceptions.DepositNegativeSumException;

/**
 * Operation: Throw an exception.
 * 
 * @see IOperation
 */
public class DepositInvalidValue implements IOperation
{
	/**
	 * Execute the operation.
	 * 
	 * @param AtomicReference<Double> depositAmount		deposit amount passed by reference.
	 * 
	 * @throws DepositNegativeSumException
	 * 
	 * @return void
	 */
	public void execute(AtomicReference<Double> sum) throws DepositNegativeSumException
	{
		throw new DepositNegativeSumException("Cannot deposit negative sums");
	}

}