package com.company.helpers;

import com.company.CatInfo;
import com.company.Requests;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatHelpers {
    public static List<CatInfo> searchCatsByDescription(List<CatInfo> cats, String regex) {
        List<CatInfo> catList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        for(CatInfo cat: cats) {
            Matcher matcher = pattern.matcher(cat.getDescription());
            if(matcher.find() && cat.getImage() != null) catList.add(cat);
        }
        return catList;
    }

    public static void printRequestHeader(Map<String, List<String>> header) {
        for (Map.Entry<String, List<String>> entries : header.entrySet()) {
            String values = "";
            for (String value : entries.getValue()) {
                values += value + ",";
            }
            System.out.println("Response " + entries.getKey() + " - " +  values );
        }
    }

    public static void printCatsInfo(List<CatInfo> catInfoList) {
        for(CatInfo cat: catInfoList) {
            System.out.println(cat);
        }
    }

    public static List<CatInfo> getCatsAsList(Requests requests) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(requests.getCats(), new TypeReference<List<CatInfo>>(){});
    }
}
