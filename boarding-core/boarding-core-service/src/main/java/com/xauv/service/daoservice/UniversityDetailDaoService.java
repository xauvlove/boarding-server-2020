package com.xauv.service.daoservice;

import com.xauv.mapper.UniversityDetailMapper;
import com.xauv.mapper.UniversityDirectionMapper;
import com.xauv.pojo.University;
import com.xauv.pojo.UniversityDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UniversityDetailDaoService {

    private static final String basepath = "D:\\apps\\java-develop\\oneonone\\";

    @Autowired
    private UniversityDirectionMapper universityDirectionMapper;
    @Autowired
    private UniversityDaoService universityDaoService;
    @Autowired
    private UniversityDetailMapper universityDetailMapper;

    /**
     * 插入所有的高校详细信息
     * @throws Exception
     */
    public void insertAllUniversityDetail() throws Exception {
        FileReader fr = new FileReader(basepath + "universities-ef.txt");
        BufferedReader brForEf = new BufferedReader(fr);
        String line = "";

        Map<String, University> allUniversityForMap = universityDaoService.getAllUniversityForMap();

        List<UniversityDetail> universityDetailList = new ArrayList<>();

        List<String> unknown = new ArrayList<>();

        while((line = brForEf.readLine()) != null) {
            if(StringUtils.isNotBlank(line)) {
                UniversityDetail universityDetail = new UniversityDetail();
                String[] split = line.split(" ");
                String name = split[0];
                String location = split[1];
                String belongsTo = split[2];
                String hasPostInstituteStr = split[3];
                String selfRegularMarkLineStr = split[4];
                String questionSite = split[5];
                String recruitmentSite = split[6];
                String nirvanaSite = split[7];

                University university = allUniversityForMap.get(name);

                if(university == null) {
                    unknown.add(name);
                    continue;
                }

                universityDetail.setUniversityId(university.getId());
                universityDetail.setHasPostInstitute("true".equals(hasPostInstituteStr));
                universityDetail.setSelfRegularMarkLine("true".endsWith(selfRegularMarkLineStr));
                universityDetail.setBelongsTo(belongsTo);
                universityDetail.setLocation(location);
                universityDetail.setQuestionSite(questionSite);
                universityDetail.setRecruitmentSite(recruitmentSite);
                universityDetail.setNirvanaSite(nirvanaSite);
                universityDetail.setCreated(new Date());
                universityDetail.setUpdated(new Date());
                universityDetail.setAlias(university.getName());
                universityDetailList.add(universityDetail);
            }
        }
        universityDetailList.forEach(universityDetailMapper::insert);
        System.out.println(unknown);
    }


}















