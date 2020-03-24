package com.staxrt.tutorial.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "produkcijuGrandine")
public class ProdukcijuGrandine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pavadinimas")
    private String pavadinimas;

    @ManyToMany
    @JoinTable(
            name = "produkcija_grandine",
            joinColumns = @JoinColumn(name = "grandines_id"),
            inverseJoinColumns = @JoinColumn(name = "produkcijos_id"))
    private List<Produkcija> produkcijos;
}
