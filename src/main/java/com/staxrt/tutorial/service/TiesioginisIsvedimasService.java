package com.staxrt.tutorial.service;


import com.staxrt.tutorial.model.IsvedimoDuomenys;

import java.util.ArrayList;
import java.util.List;

public interface TiesioginisIsvedimasService {
    IsvedimoDuomenys forwardChainingOutput(String tikslas, List<String> faktai);
}
