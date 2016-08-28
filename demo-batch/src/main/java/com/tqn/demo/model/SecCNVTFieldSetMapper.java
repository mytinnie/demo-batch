package com.tqn.demo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.tqn.demo.model.SecCNVTData;

public class SecCNVTFieldSetMapper<T> implements FieldSetMapper<T> {

	public T mapFieldSet(FieldSet fs) throws BindException {
		if (fs == null) {			
			return null;
		}
		SecCNVTData data = new SecCNVTData();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			data.setTransactionDateTime(dateFormat.parse(fs.readString("transactionDate")+fs.readString("transactionTime")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.setTransactionCode(fs.readString("transactionCode"));
		data.setCustomerPartNumber(fs.readString("customerPartNumber"));
		data.setInternalPlantCode(fs.readString("internalPlantCode"));
		data.setZeroQuantityIndicator(fs.readString("zeroQuantityIndicator"));
		data.setBaseUOM(fs.readString("baseUOM"));
		return (T) data;
	}

}
