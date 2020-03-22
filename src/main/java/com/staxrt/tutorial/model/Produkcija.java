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
@Table(name = "produkcija")
public class Produkcija {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pavadinimas")
    private String pavadinimas;

    @Column(name = "ivestis", nullable = false)
    private String ivestis;

    @Column(name = "isvestis", nullable = false)
    private String isvestis;

    @ManyToMany(mappedBy = "produkcijos")
    private List<ProdukcijuGrandine> produkcijuGrandines;
}
