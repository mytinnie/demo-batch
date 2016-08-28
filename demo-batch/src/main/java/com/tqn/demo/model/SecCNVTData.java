package com.tqn.demo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TEST_BATCH_SEC_CNVT")
@SequenceGenerator(name="SecCnvtSeq", sequenceName="SEC_CNVT_SEQUENCE", allocationSize = 1)
public class SecCNVTData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SecCnvtSeq")
	@Column(name="SEC_CNVT_SEQ_NUM")
	Long id;	
	@Column(name="TRANSACTIONDATETIME")
	private Date transactionDateTime;
	@Column(name="TRANSACTIONCODE")
	private String transactionCode;
	@Column(name="CUSTOMERPARTNUMBER")
	private String customerPartNumber;
	@Column(name="INTERNALPLANTCODE")
	private String internalPlantCode;
	@Column(name="ZEROQUANTITYINDICATOR")
	private String zeroQuantityIndicator;
	@Column(name="BASEUOM")
	private String baseUOM;
	
	public SecCNVTData() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getCustomerPartNumber() {
		return customerPartNumber;
	}

	public void setCustomerPartNumber(String customerPartNumber) {
		this.customerPartNumber = customerPartNumber;
	}

	public String getInternalPlantCode() {
		return internalPlantCode;
	}

	public void setInternalPlantCode(String internalPlantCode) {
		this.internalPlantCode = internalPlantCode;
	}

	public String getZeroQuantityIndicator() {
		return zeroQuantityIndicator;
	}

	public void setZeroQuantityIndicator(String zeroQuantityIndicator) {
		this.zeroQuantityIndicator = zeroQuantityIndicator;
	}

	public String getBaseUOM() {
		return baseUOM;
	}

	public void setBaseUOM(String baseUOM) {
		this.baseUOM = baseUOM;
	}
}
