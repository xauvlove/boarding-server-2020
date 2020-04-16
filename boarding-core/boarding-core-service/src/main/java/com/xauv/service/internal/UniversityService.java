package com.xauv.service.internal;

import com.xauv.mapper.UniversityDetailMapper;
import com.xauv.mapper.UniversityDirectionMapper;
import com.xauv.mapper.UniversityMapper;
import com.xauv.pojo.University;
import com.xauv.pojo.UniversityDetail;
import com.xauv.pojo.UniversityDirection;
import com.xauv.service.daoservice.UniversityDaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("internal-universityService")
public class UniversityService {

    private static final String basepath = "D:\\apps\\java-develop\\oneonone\\";

    @Autowired
    private UniversityDaoService universityDaoService;
    @Autowired
    private UniversityDetailMapper universityDetailMapper;

    @Autowired
    private UniversityDirectionMapper universityDirectionMapper;

    @Autowired
    private UniversityMapper universityMapper;


    /**
     * 1.如果是 985，必然是 211
     * 2.如果是 211，判断985是否有值，如果没值则设置为0，表示是211非985
     * 3.如果是 双一流大学 A，设置双一流大学 B 为 false
     * 4.如果是 双一流大学 B，设置双一流大学 A 为 false
     * 5.如果是 双一流学科，设置不属于为 false
     */
    @Transactional
    public void updateUniversityAttributes() {
        List<UniversityDetail> universityDetailList = universityDetailMapper.selectAll();
        for (UniversityDetail universityDetail : universityDetailList) {
            Boolean isNEF = universityDetail.getIsNEF();
            Boolean isTFF = universityDetail.getIsTFF();
            Boolean isClassicA = universityDetail.getIsClassicA();
            Boolean isClassicB = universityDetail.getIsClassicB();
            Boolean isClassicDisc = universityDetail.getIsClassicDisc();

            if(isNEF == null) {
                universityDetail.setIsNEF(false);
            }
            if(isTFF == null) {
                universityDetail.setIsTFF(false);
            }
            if(isClassicA == null) {
                universityDetail.setIsClassicA(false);
            }
            if(isClassicB == null) {
                universityDetail.setIsClassicB(false);
            }
            if(isClassicDisc == null) {
                universityDetail.setIsClassicDisc(false);
            }
            universityDetailMapper.updateByPrimaryKey(universityDetail);
        }
    }

    @Transactional
    public void updateUniversityNames() {
        long startId = 508747;
        long endId = 657317;

        for(;startId<=endId;startId++) {
            UniversityDirection universityDirection = universityDirectionMapper.selectByPrimaryKey(startId);
            Long universityId = universityDirection.getUniversityId();
            University university = universityMapper.selectByPrimaryKey(universityId);
            String universityName = university.getName();
            universityDirection.setUniversityName(universityName);
            universityDirectionMapper.updateByPrimaryKey(universityDirection);
        }
    }


    @Transactional
    public void fullWithUniversityAttributes() throws Exception {
        FileReader fileReader = new FileReader(basepath + "\\university-attributes\\一流学科大学.txt");
        BufferedReader br = new BufferedReader(fileReader);
        String line = "";
        while(StringUtils.isNotBlank(line = br.readLine())) {
            University university = new University();
            university.setName(line);
            university = universityMapper.select(university).get(0);
            Long universityId = university.getId();
            UniversityDetail universityDetail = new UniversityDetail();
            universityDetail.setUniversityId(universityId);
            List<UniversityDetail> universityDetailList = universityDetailMapper.select(universityDetail);
            if(universityDetailList.size() <= 0) {
                System.out.println(universityDetail);
            }
            universityDetail = universityDetailList.get(0);
            universityDetail.setIsClassicDisc(true);
            universityDetailMapper.updateByPrimaryKey(universityDetail);
        }
    }

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
