package com.staxrt.tutorial.repository;

import com.staxrt.tutorial.model.Produkcija;
import com.staxrt.tutorial.model.ProdukcijuGrandine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdukcijuGrandineRepository extends JpaRepository<ProdukcijuGrandine, Long> {
}
