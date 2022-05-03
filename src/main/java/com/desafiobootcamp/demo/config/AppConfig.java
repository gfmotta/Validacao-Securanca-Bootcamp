package com.desafiobootcamp.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.desafiobootcamp.demo.dto.CityDTO;
import com.desafiobootcamp.demo.dto.EventDTO;
import com.desafiobootcamp.demo.entities.City;
import com.desafiobootcamp.demo.entities.Event;

@Configuration
public class AppConfig {

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Bean
	public ModelMapper mapper() {
		var modelMapper = new ModelMapper();
		
		modelMapper.createTypeMap(CityDTO.class, City.class).addMappings(mapper -> mapper.skip(City::setId));
		modelMapper.createTypeMap(EventDTO.class, Event.class).addMappings(mapper -> {
				mapper.skip(Event::setId);
				mapper.skip((dest, v) -> dest.getCity().setId((Long)v));
			});
		
		return modelMapper;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtSecret);
		return tokenConverter;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
