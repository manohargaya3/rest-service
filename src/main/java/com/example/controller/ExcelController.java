package com.example.controller;

import com.example.domain.Service;
import com.example.restservice.XLSXReader;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController implements ErrorController{

    private final static String PATH = "/error";

    @GetMapping(value = "/values", produces = "application/json")
    @ResponseBody
    public List<Service> getService() {
        List<com.example.domain.Service> list = new ArrayList<>();
        XLSXReader xlsxReader = new XLSXReader();

        return xlsxReader.retrieveData();
    }

    @GetMapping(value = "/", produces = "application/json")
    @ResponseBody
    public String getSampleAPI() {
        return "hello world";
    }

    @Override
    @RequestMapping(PATH)
    @ResponseBody
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "No Mapping Found";
    }
}