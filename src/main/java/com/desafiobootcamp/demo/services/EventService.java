package com.desafiobootcamp.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafiobootcamp.demo.dto.EventDTO;
import com.desafiobootcamp.demo.entities.City;
import com.desafiobootcamp.demo.entities.Event;
import com.desafiobootcamp.demo.repositories.EventRepository;
import com.desafiobootcamp.demo.services.exceptions.DatabaseException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Transactional(readOnly = true)
	public Page<EventDTO> PagedFindAll(Pageable pageable) {
		Page<Event> page = repository.findAll(pageable);
		return page.map(event -> mapToDto(event));
	}
	
	@Transactional
	public EventDTO insert(EventDTO event) {
		try {
			Event entity = mapToEntity(event);
			entity = repository.save(entity);
			return mapToDto(entity);
		}
		catch(IllegalArgumentException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private EventDTO mapToDto(Event entity) {
		EventDTO dto = new EventDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDate(entity.getDate());
		dto.setUrl(entity.getUrl());
		dto.setCityId(entity.getCity().getId());
		return dto;
	}
	
	private Event mapToEntity(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
		return entity;
	}
}
