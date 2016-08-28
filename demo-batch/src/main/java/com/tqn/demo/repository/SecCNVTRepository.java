package com.tqn.demo.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tqn.demo.model.SecCNVTData;

@Repository
public interface SecCNVTRepository extends CrudRepository<SecCNVTData, Long> {
	@Query("select s from SecCNVTData s where s.customerPartNumber = ?")
	Iterable<SecCNVTData> findByCustomerPartNumber(String customerPartNumber);
}
