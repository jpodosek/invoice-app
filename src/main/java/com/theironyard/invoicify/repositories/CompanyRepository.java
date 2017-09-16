package com.theironyard.invoicify.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.Invoice;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	List<Invoice> findByInvoicesId(long clientId);	
}
