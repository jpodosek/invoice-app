package com.theironyard.invoicify.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.FlatFeeBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

public class FlatFeeBillingRecordControllerTests {

	private FlatFeeBillingRecordController controller;
	private BillingRecordRepository billingRecordRepo;
	private CompanyRepository companyRepo;
	private Authentication auth;
	private Company company;
	private User user;
	
	@Before
	public void setup() {
		auth = mock(Authentication.class);
		billingRecordRepo = mock(BillingRecordRepository.class);
		companyRepo = mock(CompanyRepository.class);
		controller = new FlatFeeBillingRecordController(billingRecordRepo, companyRepo );
		company = new Company();
		user = new User();
	}
	
	@Test
	public void test_create_creates_a_new_flat_fee_billing_record_and_saves_the_user() {
		//Arrange
		when(companyRepo.findOne(3L)).thenReturn(company);
		
		User user = (User) auth.getPrincipal();
		
		FlatFeeBillingRecord record = new FlatFeeBillingRecord(30, "some description", user, company);
		
		record.setCreatedBy(user);
		
		//Act
		ModelAndView actualMV = controller.create(record, 3l, auth);
		
		//Assert
		verify(billingRecordRepo).save(record);
		//verify(record.getClient()).isSameAs(client);
		
		assertThat(actualMV.getViewName()).isEqualTo("redirect:/billing-records");
		
		
	}
}
