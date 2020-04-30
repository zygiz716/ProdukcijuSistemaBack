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

    @Column(name="ivestys")
    @ElementCollection(targetClass=String.class)
    private List<String> ivestys;

    @Column(name = "isvestis")
    private String isvestis;

    @Column(name = "kaina")
    private Long kaina;

    @Transient
    private boolean flag1 = false;

    @Transient
    private boolean flag2 = false;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "produkcija_grandine",
            joinColumns = @JoinColumn(name = "produkcijos_id"),
            inverseJoinColumns = @JoinColumn(name = "grandines_id"))
    private List<ProdukcijuGrandine> produkcijuGrandines;

    public String printIvestys(){
        String text = "";
        if(ivestys.size()>0) {
            for (int i = 0; i < ivestys.size() - 1; i++) {
                text += ivestys.get(i) + ",";
            }
            text += ivestys.get(ivestys.size() - 1);
        }
        return text;
    }
}
