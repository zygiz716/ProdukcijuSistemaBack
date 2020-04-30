package com.staxrt.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Gaminys {
    private String pavadinimas;
    private Long kaina;
    private List<Long> produkcijosIds;
}
