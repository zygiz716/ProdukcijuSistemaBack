package com.staxrt.tutorial.service;

import com.staxrt.tutorial.model.IsvedimoDuomenys;

import java.io.IOException;
import java.util.List;

public interface AtbulinisIsvedimasService {
    IsvedimoDuomenys AtliktiAtbuliniIsvedima(String goal1, List<String> facts1);
}
