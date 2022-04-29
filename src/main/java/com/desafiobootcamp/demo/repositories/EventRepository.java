package com.desafiobootcamp.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafiobootcamp.demo.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
