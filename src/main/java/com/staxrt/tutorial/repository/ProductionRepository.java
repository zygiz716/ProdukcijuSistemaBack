package com.staxrt.tutorial.repository;

import com.staxrt.tutorial.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {}
