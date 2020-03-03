package com.staxrt.tutorial.service;


import java.util.ArrayList;
import java.util.List;

public interface ForwardChainingService {
    String forwardChainingOutput(String goal, List<String> facts);
}
