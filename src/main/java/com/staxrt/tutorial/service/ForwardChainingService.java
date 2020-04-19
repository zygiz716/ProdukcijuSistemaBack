package com.staxrt.tutorial.service;


import com.staxrt.tutorial.model.IsvedimoDuomenys;

import java.util.ArrayList;
import java.util.List;

public interface ForwardChainingService {
    IsvedimoDuomenys forwardChainingOutput(String goal, List<String> facts);
}
