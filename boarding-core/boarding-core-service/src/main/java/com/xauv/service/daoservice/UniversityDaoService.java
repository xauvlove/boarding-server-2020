package com.xauv.service.daoservice;

import com.google.common.collect.Maps;
import com.xauv.mapper.UniversityMapper;
import com.xauv.pojo.University;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UniversityDaoService {

    private static final String BASE_PATH = "D:\\apps\\java-develop\\oneonone\\";

    @Autowired
    private UniversityMapper universityMapper;

    @Transactional
    public void insertAllUniversityAlias() {

    }

    public Long getUniversityIdByName(String name) {
        University university = new University();
        university.setName(name);
        List<University> select = universityMapper.select(university);
        if(select.size() <= 0) {
            return -1L;
        }
        return select.get(0).getId();
    }

    public Map<String, University> getAllUniversityForMap() {
        return universityMapper.selectAll().stream().collect(Collectors.toMap(University::getName, Function.identity(), (k1, k2)->k1));
    }

    @Transactional
    public void fullWithUniversityTable() throws Exception {
        combineFile();
        FileReader frForNS = new FileReader(BASE_PATH + "university_name_schoolId.txt");
        BufferedReader brForNS = new BufferedReader(frForNS);

        FileReader frForUN = new FileReader(BASE_PATH + "common\\uniqueids\\alluniversityUniqueIdAndName.txt");
        BufferedReader brForUN = new BufferedReader(frForUN);

        String line = "";

        // map 存放 <name, University>
        Map<String, Integer> map = Maps.newHashMap();

        while((line = brForUN.readLine())!=null) {
            if(StringUtils.isNotBlank(line)) {
                String[] split = line.split(" ");
                String uidStr = split[0];
                String name = split[1];
                int uid = Integer.parseInt(uidStr);
                map.put(name, uid);
            }
        }

        List<University> universityList = new ArrayList<>();
        try{
            while(StringUtils.isNotBlank((line = brForNS.readLine()))) {
                String[] split = line.split(" ");
                String name = split[0];
                String schoolId = split[1];
                if(schoolId == null) {
                    System.err.println("严重错误：" + schoolId);
                }
                University university = new University();

                university.setName(name);
                university.setCreated(new Date());
                university.setUpdated(new Date());
                university.setSchoolId(schoolId);
                //检查是否有唯一id 代码
                Integer integer = map.get(name);
                if(integer != null) {
                    university.setUniqueId(integer);
                }
                universityList.add(university);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        universityList.forEach(
                universityMapper::insert
        );
    }

    public int getAllUniversitiesWereInserted() {
        University university = new University();
        //university.setSchoolId(123);
        List<University> universityList = universityMapper.selectByExample(university);
        return universityList.size();
    }

    private void combineFile() throws Exception {
        String basepath = "D:\\apps\\java-develop\\oneonone\\common\\uniqueids\\";
        FileWriter fw = new FileWriter(basepath + "alluniversityUniqueIdAndName.txt", true);
        for (int i = 1; i <= 6; i++) {
            FileReader fr = new FileReader(basepath + "universityUniqueIdAndName" + i + ".txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine())!=null) {
                if(StringUtils.isNotBlank(line)) {
                    fw.write(line + "\n");
                }
            }
        }
        fw.flush();
        fw.close();
    }

    public University getUniversityByName(String name) {
        University university = new University();
        university.setName(name);
        return universityMapper.select(university).get(0);
    }

}
