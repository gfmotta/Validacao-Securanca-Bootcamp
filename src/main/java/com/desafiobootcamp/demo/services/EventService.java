package com.desafiobootcamp.demo.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafiobootcamp.demo.dto.EventDTO;
import com.desafiobootcamp.demo.entities.Event;
import com.desafiobootcamp.demo.repositories.CityRepository;
import com.desafiobootcamp.demo.repositories.EventRepository;
import com.desafiobootcamp.demo.services.exceptions.DatabaseException;
import com.desafiobootcamp.demo.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired 
	private CityRepository cityRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Transactional(readOnly = true)
	public Page<EventDTO> PagedFindAll(Pageable pageable) {
		Page<Event> page = eventRepository.findAll(pageable);
		return page.map(event -> mapper.map(event, EventDTO.class));
	}
	
	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Optional<Event> optionalEvent = eventRepository.findById(id);
		Event event = optionalEvent.orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
		return mapper.map(event, EventDTO.class);
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		try {
			Event entity = mapper.map(dto, Event.class);
			entity.setCity(cityRepository.getOne(dto.getCityId()));
			entity = eventRepository.save(entity);
			return mapper.map(entity, EventDTO.class);
		}
		catch(IllegalArgumentException e) {
			throw new DatabaseException("Por favor, informe os dados novamente");
		}
	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = eventRepository.getOne(id);
			mapper.map(dto, entity);
			entity.setCity(cityRepository.getOne(dto.getCityId()));
			entity = eventRepository.save(entity);
			return mapper.map(entity, EventDTO.class);
		}
		catch(RuntimeException e) {
			throw new ResourceNotFoundException("Evento não encontrado");
		}
	}
	
	@Transactional
	public void delete(Long id) {
		try {
			eventRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Evento não encontrado. Por favor informe o evento novamente");
		}
	}
}
