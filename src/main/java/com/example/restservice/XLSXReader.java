package com.example.restservice;

import com.example.domain.Service;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSXReader
{
    public List<Service> retrieveData()
    {
        List<Service> values = new ArrayList<>();

        try
        {
            File file = new File("D:\\Manohar\\HTC Global\\IU\\URLsProductionCAS.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
//creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Iterator<Row> itr = wb.getSheetAt(0).iterator();    //iterating over excel file
            int i=0;
            while (itr.hasNext())
            {
                Service service = new Service();
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                while (cellIterator.hasNext())
                {
                    service.setId(i);
                    service.setDescription("Primary IU Website");
                    service.setName("IU Website");
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex())
                    {
                        case 0:    //field that represents string cell type
                            String serviceId = cell.getStringCellValue();
                            System.out.print(serviceId + "\t\t\t");
                            service.setServiceId("^http(s)?://(www\\.|){1}"+serviceId+"\\/?.*");
                            break;
                        case 1:    //field that represents number cell type
                            String enabled = cell.getStringCellValue();
                            boolean isEnabled = false;
                            if(enabled != null && "Yes".equalsIgnoreCase(enabled))
                                isEnabled = true;
                            System.out.print(enabled + "\t\t\t");
                            service.setEnabled(isEnabled);
                            break;
                        default:
                    }
                }
                System.out.println("");
                values.add(service);
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return values;

    }
}  