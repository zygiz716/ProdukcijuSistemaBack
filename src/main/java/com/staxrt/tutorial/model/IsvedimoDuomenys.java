package com.staxrt.tutorial.model;

import com.staxrt.tutorial.model.view.ProdukcijaView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IsvedimoDuomenys {
    String isvedimoInfo;
    List<Long> produkcijosIds;
}
