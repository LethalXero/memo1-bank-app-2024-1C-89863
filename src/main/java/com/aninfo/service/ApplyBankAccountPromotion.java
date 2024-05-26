package com.aninfo.service;

import java.util.concurrent.atomic.AtomicReference;

import com.aninfo.exceptions.InvalidValueException;

/**
 * Operation: Apply bank account promotion.
 * 
 * @see IOperation
 */
public class ApplyBankAccountPromotion implements IOperation
{
	private Double promotionPercent;
	private Double promotionRewardLimit;

	/**
	 * Constructor.
	 * 
	 * @param promotionPercent		promotion percent value.
	 * @param promotionRewardLimit	promotion reward limit.
	 * 
	 * @throws InvalidValueException	if promotionRewardLimit is less than or equal to zero.
	 */
	public ApplyBankAccountPromotion(Double promotionPercent, Double promotionRewardLimit) throws InvalidValueException
	{
		if ( promotionPercent <= 0 )
			throw new InvalidValueException();

		this.promotionPercent     = promotionPercent;
		this.promotionRewardLimit = promotionRewardLimit;
	}

	/**
	 * Execute the operation.
	 * 
	 * @param AtomicReference<Double> depositAmount		deposit amount passed by reference.
	 * 
	 * @throws Exception
	 * 
	 * @return void
	 */
	public void execute(AtomicReference<Double> depositAmount) throws Exception
	{
		Double promotionReward = depositAmount.get() / this.promotionPercent;

		if (!( promotionReward <= this.promotionRewardLimit ))
		{
			promotionReward = this.promotionRewardLimit;
		}

		depositAmount.set(depositAmount.get() + promotionReward);
	}

}