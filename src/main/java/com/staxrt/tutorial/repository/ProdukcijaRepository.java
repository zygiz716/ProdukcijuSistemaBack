package com.staxrt.tutorial.repository;

import com.staxrt.tutorial.model.Produkcija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdukcijaRepository extends JpaRepository<Produkcija, Long> {
}
