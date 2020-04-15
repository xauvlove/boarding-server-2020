package com.xauv.service.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xauv.pojo.datastructure.FirstSubjectDetail;
import com.xauv.pojo.datastructure.SecondSubjectDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("internal-directionService")
public class DirectionService {

    private final List<FirstSubjectDetail> firstSubjectDetailList = new ArrayList<>();

    @PostConstruct
    public void fullInDirection() throws IOException {

        InputStream stream = getClass().getClassLoader()
                .getResourceAsStream("private/data/subjectDetail.txt");

        /*FileReader fr = new FileReader(
                Thread.currentThread().getContextClassLoader().getResource("/").getPath()
                        + "/private/data/subjectDetail.txt");*/
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

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
        return firstSubjectDetailList.stream().map(FirstSubjectDetail::getName)
                .collect(Collectors.toList());
    }

    public List<String> getSecondDirectionNamesAndCodesByName(String name) {
        FirstSubjectDetail firstSubjectDetail= getFirstSubjectDetailByName(name);
        return firstSubjectDetail.getSecondSubjectDetailList().stream().map(
                SecondSubjectDetail::getMc).collect(Collectors.toList());
    }
}
