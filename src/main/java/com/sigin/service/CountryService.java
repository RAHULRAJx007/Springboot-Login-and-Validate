package com.sigin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigin.model.Country;
import com.sigin.repository.CountryRepository;

@Service
public class CountryService {
	@Autowired 
	private CountryRepository repo;
	
	public List<Country> countrylist(){
		return repo.findAll();
	}
	public List<Country> findAll() {
		return repo.findAll();
	}
	
}
