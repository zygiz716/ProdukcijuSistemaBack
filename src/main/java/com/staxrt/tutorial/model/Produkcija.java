package com.staxrt.tutorial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "produkcija")
public class Produkcija {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pavadinimas")
    private String pavadinimas;

    @Column(name = "ivestis")
    private String ivestis;

    @Column(name = "isvestis")
    private String isvestis;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "produkcija_grandine",
            joinColumns = @JoinColumn(name = "produkcijos_id"),
            inverseJoinColumns = @JoinColumn(name = "grandines_id"))
    private List<ProdukcijuGrandine> produkcijuGrandines;
}
