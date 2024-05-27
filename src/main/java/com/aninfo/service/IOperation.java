package com.aninfo.service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface of operation.
 */
public interface IOperation
{
	/**
	 * Execute the operation.
	 * 
	 * @param AtomicReference<Double> depositAmount		deposit amount passed by reference.
	 * 
	 * @throws Exception
	 * 
	 * @return void
	 */
	public void execute(AtomicReference<Double> sum) throws Exception;
}