package com.sigin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sigin.model.State;
import com.sigin.repository.StateRepository;

@Service
public class StateService {
	@Autowired
	private StateRepository repo;
	
	public List<State> getstate(){
		return repo.findAll();
	}


    public List<State> getStateByCountry(int cid) {
        return repo.findByCountry_Cid(cid);
    }
}
