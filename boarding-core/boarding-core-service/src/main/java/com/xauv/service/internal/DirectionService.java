package com.xauv.service.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.xauv.pojo.datastructure.FirstSubjectDetail;
import com.xauv.pojo.datastructure.SecondSubjectDetail;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectionService {

    private final List<FirstSubjectDetail> firstSubjectDetailList = new ArrayList<>();

    @PostConstruct
    public void fullInDirection() throws IOException {
        FileReader fr = new FileReader(DirectionService.class.getResource(
                "/private/data").getPath()+"/subjectDetail.txt");
        BufferedReader br = new BufferedReader(fr);

        ObjectMapper objectMapper = new ObjectMapper();

        String line = "";
        while(StringUtils.isNotBlank(line = br.readLine())) {
            FirstSubjectDetail firstSubjectDetail = objectMapper.readValue(line, FirstSubjectDetail.class);
            if(firstSubjectDetail.getName().equals("zyxw")) {
                firstSubjectDetail.setName("专业学位");
            }
            firstSubjectDetailList.add(firstSubjectDetail);
            System.out.println();
        }
    }

    public FirstSubjectDetail getFirstSubjectDetailByName(String name) {
        for (FirstSubjectDetail firstSubjectDetail : firstSubjectDetailList) {
            if(name.equals(firstSubjectDetail.getName())) {
                return firstSubjectDetail;
            }
        }
        return null;
    }

    public List<String> getAllFirstDirectionNames() {
        return firstSubjectDetailList.stream().map(FirstSubjectDetail::getName).collect(Collectors.toList());
    }

    public List<String> getSecondDirectionNamesAndCodesByName(String name) {
        FirstSubjectDetail firstSubjectDetail= getFirstSubjectDetailByName(name);
        /*List<SecondSubjectDetail> secondSubjectDetailList = firstSubjectDetail.getSecondSubjectDetailList();
        List<Pair<String, String>> secondSubjectDetailNameAndCodeList = Lists.newArrayListWithExpectedSize(secondSubjectDetailList.size());
        for (SecondSubjectDetail secondSubjectDetail : secondSubjectDetailList) {
            Pair<String, String> pair = new Pair<>(secondSubjectDetail.getMc(), secondSubjectDetail.getDm());
            secondSubjectDetailNameAndCodeList.add(pair);
        }
        return secondSubjectDetailNameAndCodeList;
        */
        return firstSubjectDetail.getSecondSubjectDetailList().stream().map(SecondSubjectDetail::getMc).collect(Collectors.toList());
    }
}
