package com.theironyard.invoicify.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.BillingRecord;
import com.theironyard.invoicify.models.Invoice;
import com.theironyard.invoicify.models.InvoiceLineItem;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
	
	private CompanyRepository companyRepo;
	private BillingRecordRepository	 billingRecordRepo;
	private InvoiceRepository invoiceRepository;
	
	public InvoiceController(CompanyRepository companyRepo, BillingRecordRepository	 billingRecordRepo, InvoiceRepository invoiceRepository) {
		this.companyRepo = companyRepo;
		this.billingRecordRepo = billingRecordRepo;
		this.invoiceRepository = invoiceRepository;
	}

	@GetMapping("")
	public ModelAndView list(Authentication auth) {
		User user = (User) auth.getPrincipal();
		ModelAndView mv = new ModelAndView("invoices/list");
		mv.addObject("user", user);
		mv.addObject("invoices", invoiceRepository.findAll() );
		return mv;
	}
	
	@GetMapping("new")
	public ModelAndView step1() {	
	ModelAndView mv = new ModelAndView("invoices/step-1");
	mv.addObject("companies", companyRepo.findAll());
	return mv;
	
	}
	
	@PostMapping("new")
	public ModelAndView step2(long clientId) {
	ModelAndView mv = new ModelAndView("invoices/step-2");
	mv.addObject("clientId", clientId);
	mv.addObject("records", billingRecordRepo.findByClientIdAndLineItemIdIsNull(clientId));
	return mv;
	}
	
	@PostMapping("create")
	public ModelAndView create(Invoice invoice, long clientId, long[] recordIds, Authentication auth) {
		ModelAndView mv = new ModelAndView();
		
		try {
				User creator = (User) auth.getPrincipal();
				long nowish = Calendar.getInstance().getTimeInMillis();
				Date now = new Date(nowish);
				List<BillingRecord> records = billingRecordRepo.findByIdIn(recordIds);
						
				List<InvoiceLineItem> items = new ArrayList<InvoiceLineItem>();
				for (BillingRecord record : records){ 
					InvoiceLineItem lineItem = new InvoiceLineItem();
					lineItem.setBillingRecord(record);
					lineItem.setCreatedBy(creator);
					lineItem.setCreatedOn(now);
					lineItem.setInvoice(invoice);
					items.add(lineItem);
					
				}
				
				invoice.setLineItems(items);
				invoice.setCreatedBy(creator);
				invoice.setCreatedOn(now);
				invoice.setCompany(companyRepo.findOne(clientId));
				invoiceRepository.save(invoice);
				mv.setViewName("redirect:/invoices");
		} 
		
		catch (InvalidDataAccessApiUsageException idaaue) {
			System.err.println(idaaue);
			mv.addObject("errorMessage", "Please select at least one billing record.");
			mv.addObject("clientId", clientId);
			mv.addObject("records", billingRecordRepo.findByClientIdAndLineItemIdIsNull(clientId));
			mv.setViewName("/invoices/step-2");
		}
		return mv;
	}	
}

















