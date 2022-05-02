package com.company;

import com.company.helpers.CatHelpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Requests requests = new Requests();
        Map<String, List<String>> header;
        String option;
        List<CatInfo> catList;

        System.out.println("Enter option:");
        System.out.println("login - for cookies authentication");
        System.out.println("get cats - to obtain a list of cats");
        System.out.println("head - to send a head request");
        System.out.println("options - to send an options request");
        System.out.println("vote - to place a like vote for specific cats");
        System.out.println("exit - to exit application");

        do {
            System.out.print(">");
             option = scanner.nextLine();
            switch(option) {
                case "login":
                    System.out.println(new Authenticate().getCookies());
                    break;
                case "get cats":
                    catList = CatHelpers.getCatsAsList(requests);
                    CatHelpers.printCatsInfo(catList);
                    break;
                case "head":
                    header = requests.headRequest();
                    CatHelpers.printRequestHeader(header);
                    break;
                case "options":
                    header = requests.optionsRequest();
                    CatHelpers.printRequestHeader(header);
                    break;
                case "vote":
                    catList = CatHelpers.getCatsAsList(requests);
                    List<CatInfo> catInfoList = CatHelpers.searchCatsByDescription(catList, "cats\\s");
                    requests.voteSpecificCats(catInfoList);
                    System.out.println("Cats that corespond to inserted regex:");
                    CatHelpers.printCatsInfo(catInfoList);
                    break;
                default:
                    if(!option.equals("exit")) System.out.println("No such option. Try again");
            }
        }while(!option.equals("exit"));
    }

}
