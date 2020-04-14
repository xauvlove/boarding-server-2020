package com.xauv.service.daoservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.xauv.exception.CrawlerBehaviorException;
import com.xauv.mapper.UniversityDirectionMapper;
import com.xauv.pojo.University;
import com.xauv.pojo.UniversityDirection;
import com.xauv.pojo.datastructure.DetailForRecruitment;
import com.xauv.pojo.datastructure.FirstSubjectDetail;
import com.xauv.pojo.datastructure.SecondSubjectDetail;
import com.xauv.pojo.datastructure.ThirdSubjectWithRecruitmentNum;
import com.xauv.pojo.vo.UniversityDirectionVO;
import com.xauv.pojo.vo.UniversityDirectionVOContent;
import com.xauv.pojo.wx.WXLoginCallback;
import com.xauv.utils.DESUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UniversityDirectionDaoService {

    private static final String basepath = "D:\\apps\\java-develop\\oneonone\\common\\subjectDetail\\fornum\\";

    @Autowired
    private UniversityDaoService universityDaoService;
    @Autowired
    private UniversityDirectionMapper universityDirectionMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, WXLoginCallback> redisTemplate;

    public List<UniversityDirectionVO> queryEmptyUniversity(){
        return Collections.emptyList();
    }

    /**
     * 根据条件查询
     * 这样查询可能查询量非常大！
     * @param
     * @return
     */
    public List<UniversityDirectionVO> getUniversityByConditions(
            UniversityDirection universityDirection, String loginSession)
            throws IOException, CrawlerBehaviorException, IllegalAccessException {

        if(StringUtils.isBlank(universityDirection.getUniversityName())) {
            throw new IllegalAccessException();
        }

        //鉴定爬虫行为
        String decryptString = DESUtil.getDecryptString(loginSession);
        WXLoginCallback wxLoginCallback = objectMapper.readValue(decryptString, WXLoginCallback.class);
        //判断爬虫行为
        WXLoginCallback redisLoginCallback = redisTemplate.opsForValue().get(
                WXLoginCallback.ACCESS_FREQUENCY_PREFIX + wxLoginCallback.getOpenid());
        if(redisLoginCallback != null) {
            if(redisLoginCallback.getAccessFrequency() >= WXLoginCallback.MAX_ACCESS_FREQUENCY_LANDING_EXP
                    || redisLoginCallback.getAccFreqWithUniversity() >= WXLoginCallback.MAX_ACCESS_FREQUENCY_UNIVERSITY) {
                throw new CrawlerBehaviorException();
            }
        } else {
            redisLoginCallback = wxLoginCallback;
        }

        if(StringUtils.isBlank(universityDirection.getBroadDirectionName())) {
            universityDirection.setBroadDirectionName(null);
        }
        int masterType = universityDirection.getMasterType() + 1;
        //全日制专硕 = 3  非全日制专硕 = 4
        if(masterType == 3) {
            universityDirection.setBroadDirectionName("zyxw");
        } else if(masterType == 4) {
            universityDirection.setBroadDirectionName("zyxw");
        }
        universityDirection.setMasterType(masterType);
        List<UniversityDirection> select = universityDirectionMapper.select(universityDirection);
        saveQueryFrequencyActiveToRedis(redisLoginCallback, select.size());
        return transferUniversityDirectionToVO(select);
    }

    //更新 redis
    @Async
    public void saveQueryFrequencyActiveToRedis(WXLoginCallback wxLoginCallback, Integer queryCount) {
        wxLoginCallback.setAccessFrequency(wxLoginCallback.getAccessFrequency() + queryCount);
        wxLoginCallback.setAccFreqWithUniversity(wxLoginCallback.getAccFreqWithUniversity() + queryCount);
        redisTemplate.opsForValue().set(
                WXLoginCallback.ACCESS_FREQUENCY_PREFIX
                        + wxLoginCallback.getOpenid(), wxLoginCallback, 7, TimeUnit.DAYS);
    }

    private List<UniversityDirectionVO> transferUniversityDirectionToVO(List<UniversityDirection> universityDirectionList) {
        if(universityDirectionList.size() <= 0) {
            return Collections.emptyList();
        }
        ArrayList<UniversityDirectionVO> universityDirectionVOList = Lists.newArrayListWithExpectedSize(universityDirectionList.size());
        for(int i=0;i<universityDirectionList.size();i++) {
            UniversityDirection ori = universityDirectionList.get(i);
            UniversityDirectionVO vo = new UniversityDirectionVO();
            UniversityDirectionVOContent voContent = new UniversityDirectionVOContent();
            vo.setUniversityDirectionVOContent(voContent);
            vo.setFid("x" + String.valueOf(i));
            vo.setOpen(false);
            /*List<String> masterTypeList = new ArrayList<>("全日制学术硕士", "非全日制学术硕士",
                    "全日制专业硕士", "");
            */
            voContent.setMasterType(ori.getMasterType());
            voContent.setObscureId(DESUtil.getEncryptString(String.valueOf(ori.getId())));
            voContent.setObscureUniversityId(DESUtil.getEncryptString(String.valueOf(ori.getUniversityId())));
            voContent.setUniversityName(ori.getUniversityName());
            voContent.setBroadDirectionName(ori.getBroadDirectionName());
            voContent.setBroadDirectionCode(ori.getBroadDirectionCode());
            voContent.setInvolvedDirectionName(ori.getInvolvedDirectionName());
            voContent.setInvolvedDirectionCode(ori.getInvolvedDirectionCode());
            voContent.setMajorDirectionName(ori.getMajorDirectionName());
            voContent.setMajorDirectionCode(ori.getMajorDirectionCode());
            voContent.setExamMode(ori.getExamMode());
            voContent.setInstituteName(ori.getInstituteName());
            voContent.setInstituteCode(ori.getInstituteCode());
            voContent.setSubjectName(ori.getSubjectName());
            voContent.setSubjectCode(ori.getSubjectCode());
            voContent.setDirection(ori.getDirectionName());
            voContent.setLearningMode(ori.getLearningMode());
            voContent.setRecruitmentCount(ori.getRecruitmentCount());
            voContent.setNewestPull(ori.getUpdated());

            universityDirectionVOList.add(vo);
        }
        return universityDirectionVOList;
    }

    /**
     *
     * @param universityDirection
     * @param page
     * @param rows
     * @return
     */
    public List<UniversityDirection> getUniversityByConditionsWithPage(
            UniversityDirection universityDirection, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(UniversityDirection.class);
        Example.Criteria criteria = example.createCriteria();

        return universityDirectionMapper.selectByExample(universityDirection);
    }

    public  List<UniversityDirection> getUniversityDirectionById(String name, int masterType, String majorDirectionName) {
        UniversityDirection universityDirection = new UniversityDirection();
        universityDirection.setUniversityName(name);
        universityDirection.setMasterType(masterType);
        universityDirection.setMajorDirectionName(majorDirectionName);
        return universityDirectionMapper.select(universityDirection);
    }

    @Transactional
    public void insertAllUniversityDirection(int index, int type) throws Exception{

        FileReader fr = null;

        if(type == 1) {
            fr = new FileReader(basepath + "subjectDetailRecruitment-xs"+index+".txt");
        } else if(type == 2) {
            fr = new FileReader(basepath + "subjectDetailRecruitment-zs"+index+".txt");
        } else {
            throw new Exception("传入参数错误");
        }

        BufferedReader br = new BufferedReader(fr);
        String line = "";
        List<UniversityDirection> universityDirectionList = new ArrayList<>();
        while(((line = br.readLine())!=null)) {
            if(StringUtils.isNotBlank(line)) {
                DetailForRecruitment detailForRecruitment = objectMapper.readValue(line, DetailForRecruitment.class);
                String universityName = detailForRecruitment.getSchoolName();
                String schoolId = detailForRecruitment.getSchoolId();

                //遍历一级学科
                List<FirstSubjectDetail> firstSubjectDetailList = detailForRecruitment.getFirstSubjectDetailList();
                for (FirstSubjectDetail firstSubjectDetail : firstSubjectDetailList) {
                    String broadDirectionName = firstSubjectDetail.getName();
                    String broadDirectionCode = firstSubjectDetail.getCode();
                    List<SecondSubjectDetail> secondSubjectDetailList = firstSubjectDetail.getSecondSubjectDetailList();
                    for (SecondSubjectDetail secondSubjectDetail : secondSubjectDetailList) {
                        String involvedDirectionName = secondSubjectDetail.getMc();
                        String involvedDirectionCode = secondSubjectDetail.getDm();
                        List<ThirdSubjectWithRecruitmentNum> thirdSubWithRecruNumsList = secondSubjectDetail.getThirdSubWithRecruNumsList();
                        for (ThirdSubjectWithRecruitmentNum thirdSubjectWithRecruitmentNum : thirdSubWithRecruNumsList) {
                            String examMode = thirdSubjectWithRecruitmentNum.getTestMode();
                            String learningMode = thirdSubjectWithRecruitmentNum.getLearningMode();
                            int masterType  = 0;
                            if(learningMode.equals("全日制") && type == 1) {
                                masterType = 1;
                            } else if(learningMode.equals("非全日制") && type == 1) {
                                masterType = 2;
                            } else if(learningMode.equals("全日制") && type == 2) {
                                masterType = 3;
                            } else if(learningMode.equals("非全日制") && type == 2) {
                                masterType = 4;
                            } else {
                                throw new Exception("学习模式解析错");
                            }
                            String numberStr = thirdSubjectWithRecruitmentNum.getNumber();
                            String recruitmentCount = thirdSubjectWithRecruitmentNum.getNumber();
                            String examRangeContent = thirdSubjectWithRecruitmentNum.getTestRangeSite();

                            String direction = thirdSubjectWithRecruitmentNum.getDirection();
                            int start = direction.indexOf("(");
                            int end = direction.indexOf(")");
                            String directionCode = direction.substring(start + 1, end);
                            String directionName = direction.substring(end + 1);
                            System.out.println("directionName--------------------------------" +directionName);

                            //处理研究所
                            String institute = thirdSubjectWithRecruitmentNum.getInstitute();
                            start = institute.indexOf("(");
                            end = institute.indexOf(")");
                           /* try {
                                institute.substring(start + 1, end);
                                institute.substring(end + 1, institute.length());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            String instituteCode = institute.substring(start + 1, end);
                            String instituteName = institute.substring(end + 1);
                            //处理 subject，分为代码和名称
                            String subject = thirdSubjectWithRecruitmentNum.getSubject();
                            start = subject.indexOf("(");
                            end = subject.indexOf(")");
                            String subjectCode = subject.substring(start + 1, end);
                            String subjectName = subject.substring(end + 1);

                            University university = universityDaoService.getUniversityByName(universityName);
                            Long universityId = university.getId();
                            if(!universityName.trim().equals(university.getName().trim())) {
                                System.err.println("发生高校名称不匹配 " + "数据库：" + university.getName() + " 现在：" + universityName);
                                throw new Exception("发生高校名称不匹配");
                            }

                            //设置一系列属性
                            UniversityDirection universityDirection = new UniversityDirection();
                            universityDirection.setUniversityId(universityId);
                            universityDirection.setUniversityName(universityName.trim());
                            universityDirection.setMasterType(masterType);
                            universityDirection.setBroadDirectionCode(broadDirectionCode);
                            universityDirection.setBroadDirectionName(broadDirectionName);
                            universityDirection.setInvolvedDirectionName(involvedDirectionName);
                            universityDirection.setInvolvedDirectionCode(involvedDirectionCode);
                            universityDirection.setMajorDirectionCode(subjectCode);
                            universityDirection.setMajorDirectionName(subjectName);
                            universityDirection.setExamMode(examMode);
                            universityDirection.setInstituteName(instituteName);
                            universityDirection.setInstituteCode(instituteCode);
                            universityDirection.setSubjectCode(subjectCode);
                            universityDirection.setSubjectName(subjectName);
                            universityDirection.setDirectionName(directionName);
                            universityDirection.setDirectionCode(directionCode);
                            universityDirection.setLearningMode(learningMode);
                            universityDirection.setRecruitmentCount(recruitmentCount);
                            universityDirection.setExamRangeContent(examRangeContent);
                            universityDirection.setCreated(new Date());
                            universityDirection.setUpdated(new Date());
                            universityDirectionList.add(universityDirection);
                            if(universityDirectionList.size() > 100) {
                                universityDirectionList.forEach(universityDirectionMapper::insert);
                                universityDirectionList.clear();
                            }
                        }
                    }
                }
            }
        }
    }
}




