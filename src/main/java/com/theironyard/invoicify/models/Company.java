package com.theironyard.invoicify.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(length=255, nullable=false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="company")
	private List<Invoice> invoices;
	
	private int invoiceCount;
	
	public Company() {}
	
	public Company(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public int getInvoiceCount() {
		setInvoiceCount();
		return invoiceCount;
	}

	public void setInvoiceCount() {
		this.invoiceCount = invoices.size();
	}
	
}
