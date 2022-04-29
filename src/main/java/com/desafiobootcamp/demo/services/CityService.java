package com.desafiobootcamp.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafiobootcamp.demo.dto.CityDTO;
import com.desafiobootcamp.demo.entities.City;
import com.desafiobootcamp.demo.repositories.CityRepository;
import com.desafiobootcamp.demo.services.exceptions.DatabaseException;

@Service
public class CityService {
	
	@Autowired
	private CityRepository repository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		var sort = Sort.by("name").ascending();
		List<City> cities = repository.findAll(sort);
		return cities.stream().map(city -> mapToDto(city)).collect(Collectors.toList());
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		try {
			City city = mapToEntity(dto);
			city = repository.save(city);
			return mapToDto(city);
		}
		catch(IllegalArgumentException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private City mapToEntity(CityDTO dto) {
		City city = new City();
		city.setName(dto.getName());
		return city;
	}

	private CityDTO mapToDto(City city) {
		CityDTO dto = new CityDTO();
		dto.setId(city.getId());
		dto.setName(city.getName());
		return dto;
	}
}
