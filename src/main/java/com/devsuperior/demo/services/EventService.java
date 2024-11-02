package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

	@Autowired
    private EventRepository repository;
	
	@Transactional(readOnly = true)
	public Page<Event> findAllPaged(Pageable pageable) {
		Page<Event> page = repository.findAll(pageable);
		return page;
	}
	
	@Transactional
	public Event insert(Event entity) {
		entity = repository.save(entity);
		return entity;
	}
	
	@Transactional
	public Event update(Long id, Event event) {
		try {
			Event newlyUpdatedProduct = repository.getReferenceById(id);
			copySourceEventToDestinationEvent(event, newlyUpdatedProduct);
	        newlyUpdatedProduct = repository.save(newlyUpdatedProduct);
	        return newlyUpdatedProduct;
	    }
	    catch (EntityNotFoundException e) {
	    	throw new ResourceNotFoundException("Recurso não encontrado");
	    }
	}
	 
	private void copySourceEventToDestinationEvent(Event srcProduct, Event destProduct) {
		destProduct.setName(srcProduct.getName());
		destProduct.setDate(srcProduct.getDate());
	    destProduct.setUrl(srcProduct.getUrl());
	    destProduct.setCity(srcProduct.getCity());	     
	}
}
