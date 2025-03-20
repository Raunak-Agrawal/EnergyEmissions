package com.effix.api.util;

import com.effix.api.service.CompanyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CompanyDataServiceFactory {

    @Autowired
    private List<CompanyDataService> companyDataServiceList;

    private static final Map<String, CompanyDataService> companyDataServiceListCache = new HashMap<>();


    @PostConstruct
    public void initCompanyDataServiceListCache(){
        for(CompanyDataService companyDataService: companyDataServiceList){
            companyDataServiceListCache.put(companyDataService.getCountry(), companyDataService);
        }
    }

    public CompanyDataService getCompanyDataService(String country) {
        return companyDataServiceListCache.get(country);
    }
}
