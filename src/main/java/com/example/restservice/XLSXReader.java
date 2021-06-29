package com.example.restservice;

import com.example.domain.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSXReader
{
    public List<Service> retrieveData()
    {
        List<Service> prodValues = new ArrayList<>();
        List<Service> stageValues = new ArrayList<>();

        try
        {
            File file = new File("D:\\Manohar&GayathriDocuments\\Manohar\\IUProjectDocuments\\URLsProductionCAS.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
//creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Iterator<Row> itr = wb.getSheetAt(0).iterator();    //iterating over excel file
            int i=1;
            int stageId = 1;
            int prodId = 1;
            while (itr.hasNext())
            {
                Service service = new Service();
                String protocol = "";
                boolean isProdRegistered = false;
                boolean isStageRegistered = false;
                boolean isCasRequested = false;
                String serviceUrl = "";
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex())
                    {
                        case 0:    //field that represents string cell type
                            String url = cell.getStringCellValue();
                            System.out.print(url + "\t\t\t");
                            if(url.endsWith("/")){
                                url = url.trim();
                                url = url.substring(0,url.lastIndexOf("/"));
                            }
                            if(url.endsWith("/*")){
                                url = url.substring(0,url.lastIndexOf("/*"));
                            }
                            url = url.replaceAll("[.]","\\\\.");
                            url = url.replaceAll("/","\\\\/");
                            if(url.startsWith("*\\.")){
                                url = url.replace("*.","([A-Za-z0-9]+?(.|-|_){1}|)*");
                            }
                            if(url.contains("*.") || url.contains("*.")){
                                url = url.replace("*.","([A-Za-z0-9_-]+)*");
                                url = url.replace("%.","([A-Za-z0-9_-]+)*");
                            }
                            serviceUrl = serviceUrl+url;
                            break;
                        case 1:    //field that represents number cell type
                            String protocolValue = cell.getStringCellValue();
                            if(protocolValue != null && "CAS".equalsIgnoreCase(protocolValue))
                                isCasRequested = true;
                            System.out.print(protocolValue + "\t\t\t");
                            break;
                        case 2:    //field that represents number cell type
                            String stageValue = cell.getStringCellValue();
                            if(stageValue != null && "Yes".equalsIgnoreCase(stageValue))
                                isStageRegistered = true;
                            System.out.print(stageValue + "\t\t\t");
                            break;
                        case 3:    //field that represents number cell type
                            String prodValue = cell.getStringCellValue();
                            if(prodValue != null && "Yes".equalsIgnoreCase(prodValue))
                                isProdRegistered = true;
                            System.out.print(isProdRegistered + "\t\t\t");
                            break;
                        default:
                    }
                }
                if(isCasRequested && isStageRegistered) {
                    service.setServiceId("^http(s)?://(www\\.|){1}"+serviceUrl+"\\/?.*");
                    service.setEnabled(isCasRequested);
                    service.setId(stageId);
                    service.setDescription("Primary IU Website");
                    service.setName("IU Website");
                    service.setEnabled(isStageRegistered);
                    stageValues.add(service);
                    stageId++;
                }
                if(isCasRequested && isProdRegistered) {
                    service.setServiceId("^http(s)?://(www\\.|){1}"+serviceUrl+"\\/?.*");
                    service.setEnabled(isCasRequested);
                    service.setId(prodId);
                    service.setDescription("Primary IU Website");
                    service.setName("IU Website");
                    service.setEnabled(isProdRegistered);
                    prodValues.add(service);
                    prodId++;
                }
                ObjectMapper writeValuesAsString = new ObjectMapper();
                String stageJsonValues = writeValuesAsString.writeValueAsString(stageValues);
                String prodJsonValues = writeValuesAsString.writeValueAsString(prodValues);
                FileWriter stageJsonFile = new FileWriter("D:\\Manohar&GayathriDocuments\\Manohar\\IUProjectDocuments\\stageJsonValues.json");
                FileWriter prodJsonFile = new FileWriter("D:\\Manohar&GayathriDocuments\\Manohar\\IUProjectDocuments\\prodJsonValues.json");
                stageJsonFile.write(stageJsonValues);
                prodJsonFile.write(prodJsonValues);
                stageJsonFile.close();
                prodJsonFile.close();
                System.out.println("");
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return stageValues;

    }
}  