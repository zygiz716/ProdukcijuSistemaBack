package com.staxrt.tutorial.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "production")
public class Production {

    public Production(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "consistent", nullable = false)
    private String consistent;

    @Column(name = "antecedent", nullable = false)
    private String antecedent;

    @Column(name="antecedents")
    @ElementCollection(targetClass=String.class)
    private List<String> antecedents;

    @Transient
    private boolean flag1 = false;

    @Transient
    private boolean flag2 = false;

    Production(String consistent, String antecedent){
        this.consistent = consistent;
        this.antecedent = antecedent;
        flag1 = false;
        flag2 = false;
    }

    public String printAntecendents(){
        String text = "";
        if(antecedents.size()>0) {
            for (int i = 0; i < antecedents.size() - 1; i++) {
                text += antecedents.get(i) + ",";
            }
            text += antecedents.get(antecedents.size() - 1);
        }
        return text;
    }

    public String getConsistent(){
        return consistent;
    }

    public String getAntecedent(){
        return antecedent;
    }

    public List<String> getAntecedents(){
        return antecedents;
    }

    public void setFlag1(boolean flag1){
        this.flag1 = flag1;
    }

    public boolean getFlag1(){
        return flag1;
    }

    public void setFlag2(boolean flag2){
        this.flag2 = flag2;
    }

    public boolean getFlag2(){
        return flag2;
    }
}
