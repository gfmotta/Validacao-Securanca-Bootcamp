package com.desafiobootcamp.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafiobootcamp.demo.dto.CityDTO;
import com.desafiobootcamp.demo.entities.City;
import com.desafiobootcamp.demo.repositories.CityRepository;
import com.desafiobootcamp.demo.services.exceptions.DatabaseException;
import com.desafiobootcamp.demo.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {
	
	@Autowired
	private CityRepository repository;
	
	@Autowired
	private ModelMapper mapper;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		var sort = Sort.by("name").ascending();
		List<City> cities = repository.findAll(sort);
		return cities.stream().map(city -> mapper.map(city, CityDTO.class)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		Optional<City> optionalCity = repository.findById(id);
		City city = optionalCity.orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));
		return mapper.map(city, CityDTO.class);
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		try {
			City city = mapper.map(dto, City.class);
			city = repository.save(city);
			return mapper.map(city, CityDTO.class);
		}
		catch(IllegalArgumentException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = repository.getOne(id);
			mapper.map(dto, entity);
			entity = repository.save(entity);
			return mapper.map(entity, CityDTO.class);
		}
		catch(RuntimeException e) {
			throw new ResourceNotFoundException("Cidade não encontrada");
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Cidade não encontrada. Por favor informe a cidade novamente");
		}
	}
}
