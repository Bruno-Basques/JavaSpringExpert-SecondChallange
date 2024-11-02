package com.devsuperior.demo.controllers;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.services.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService service;
    
    @Autowired
    private ModelMapper modelMapper;
    
	@GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
		Page<Event> entityPage = service.findAllPaged(pageRequest);
		Page<EventDTO> dtoPage = entityPage.map(x -> modelMapper.map(x, EventDTO.class));
        return ResponseEntity.ok(dtoPage);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@PostMapping
	public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO dto) {
		Event entity = service.insert(modelMapper.map(dto, Event.class));
		dto = modelMapper.map(entity, EventDTO.class);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
