/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT China Mobile (SuZhou) Software Technology Co.,Ltd. 2019
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.none.web.po;

public class LotteryPO {

	private String staffId;

	private Float lotteryNum;

	private String activeId;

	public String getActiveId() {
		return activeId;
	}

	public void setActiveId(String activeId) {
		this.activeId = activeId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public Float getLotteryNum() {
		return lotteryNum;
	}

	public void setLotteryNum(Float lotteryNum) {
		this.lotteryNum = lotteryNum;
	}

	@Override
	public String toString() {
		return "[staffId=" + staffId + ", lotteryNum=" + lotteryNum + ", activeId=" + activeId + "]";
	}

}
