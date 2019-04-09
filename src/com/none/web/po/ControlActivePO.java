/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT China Mobile (SuZhou) Software Technology Co.,Ltd. 2019
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.none.web.po;

/**
 * 活动控制类
 * 
 * @author winter
 */
public class ControlActivePO {

	private String id;

	private String bu;

	private int peoNum;

	private String status;

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPeoNum() {
		return peoNum;
	}

	public void setPeoNum(int peoNum) {
		this.peoNum = peoNum;
	}

}
