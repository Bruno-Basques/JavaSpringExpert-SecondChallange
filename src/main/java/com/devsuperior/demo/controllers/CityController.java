package com.devsuperior.demo.controllers;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.services.CityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    @Autowired
    private CityService service;
    
    @Autowired
    private ModelMapper modelMapper;
    
	@GetMapping
    public ResponseEntity<List<CityDTO>> findAll() {
        List<City> entityList = service.findAll();
        List<CityDTO> dtoList = entityList.stream().map(x -> modelMapper.map(x, CityDTO.class)).toList();
        return ResponseEntity.ok(dtoList);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO dto) {
		City entity = service.insert(modelMapper.map(dto, City.class));
		dto = modelMapper.map(entity, CityDTO.class);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
