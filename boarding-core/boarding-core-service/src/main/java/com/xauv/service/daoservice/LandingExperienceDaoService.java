package com.xauv.service.daoservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xauv.exception.AESEncodeException;
import com.xauv.exception.CrawlerBehaviorException;
import com.xauv.format.InternalStandardMessageEnum;
import com.xauv.format.InternalStandardMessageFormat;
import com.xauv.mapper.LandingExperienceMapper;
import com.xauv.pojo.LandingExperience;
import com.xauv.pojo.vo.LandingExperienceShareVO;
import com.xauv.pojo.wx.WXLoginCallback;
import com.xauv.resultful.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LandingExperienceDaoService {

    private static final int DEFAULT_MAX_ROWS = 5;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LandingExperienceMapper landingExperienceMapper;
    @Autowired
    private UniversityDaoService universityDaoService;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, WXLoginCallback> redisTemplate;

    public PageResult<LandingExperienceShareVO> queryLandingExperienceByPageWithDirectError() {
        return new PageResult<>(0L,0, Collections.emptyList());
    }

    public PageResult<LandingExperienceShareVO> queryLandingExperienceByPage(
            int page, int rows, String loginSession) throws IOException, CrawlerBehaviorException, AESEncodeException {

        PageResult<LandingExperienceShareVO> result = null;

        //鉴定爬虫行为
       /* String decryptString = AESUtil.decryptEcbMode(loginSession);
        WXLoginCallback wxLoginCallback = objectMapper.readValue(decryptString, WXLoginCallback.class);
        //判断爬虫行为
        WXLoginCallback redisLoginCallback = redisTemplate.opsForValue().get(
                WXLoginCallback.ACCESS_FREQUENCY_PREFIX + wxLoginCallback.getOpenid());
        if(redisLoginCallback != null) {
            if(redisLoginCallback.getAccessFrequency() >= WXLoginCallback.MAX_ACCESS_FREQUENCY_LANDING_EXP
                    || redisLoginCallback.getAccFreqWithLandingExp() >= WXLoginCallback.MAX_ACCESS_FREQUENCY_LANDING_EXP) {
                throw new CrawlerBehaviorException();
            }
        } else {
            redisLoginCallback = wxLoginCallback;
        }*/
        //非爬虫行为
        if(rows > DEFAULT_MAX_ROWS) {
            rows = DEFAULT_MAX_ROWS;
        }
        if(page == 0) {
            page = 1;
        }
        PageHelper.startPage(page, rows);
        Example example = new Example(LandingExperience.class);
        List<LandingExperience> landingExperienceList = landingExperienceMapper.selectByExample(example);
        PageInfo<LandingExperience> landingExpsPageInfo = new PageInfo<>(landingExperienceList);

        if(landingExpsPageInfo.getTotal() <= 0) {
            result = new PageResult<>(0L,0, Collections.emptyList());
            return result;
        }
        int fid = 0;
        List<LandingExperienceShareVO> voList =
                Lists.newArrayListWithExpectedSize(DEFAULT_MAX_ROWS);

        for (LandingExperience exp : landingExperienceList) {
            voList.add(transferLandingExpToVO(exp, "x"+ fid++));
        }
        try {
            System.out.println(new ObjectMapper().writeValueAsString(voList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result = new PageResult<>(landingExpsPageInfo.getTotal(), landingExpsPageInfo.getPages(), voList);
        //saveQueryFrequencyActiveToRedis(redisLoginCallback, voList.size());
        return result;
    }

    //更新 redis
    @Async
    public void saveQueryFrequencyActiveToRedis(WXLoginCallback wxLoginCallback, Integer queryCount) {
        wxLoginCallback.setAccessFrequency(wxLoginCallback.getAccessFrequency() + queryCount);
        wxLoginCallback.setAccFreqWithLandingExp(wxLoginCallback.getAccFreqWithLandingExp() + queryCount);
        redisTemplate.opsForValue().set(
                WXLoginCallback.ACCESS_FREQUENCY_PREFIX
                        + wxLoginCallback.getOpenid(), wxLoginCallback, 7, TimeUnit.DAYS);
    }

    public InternalStandardMessageFormat saveLandingExperience(LandingExperienceShareVO landingExperienceShareVO) {
        InternalStandardMessageFormat format = null;

        if(!isLandingExpCandidate(landingExperienceShareVO)) {
            format = new InternalStandardMessageFormat(InternalStandardMessageEnum.INVALID_REQUEST_OBJECT);
            return format;
        }

        int insert = landingExperienceMapper.insert(transferLandingExpVOToNor(landingExperienceShareVO));
        if(insert < 1) {
            format = new
                    InternalStandardMessageFormat(InternalStandardMessageEnum.DAO_INSERT_RECORD_FAILED);
            return format;
        }
        return new InternalStandardMessageFormat(InternalStandardMessageEnum.DAP_INSERT_RECORD_SUCCESS);

        /*InternalStandardMessageFormat format = null;
        int insert = landingExperienceMapper.insert(transferLandingExpVOToNor(landingExperienceShareVO));
        if(insert < 1) {
            throw new RuntimeException("插入错误");
        }
        return insert;*/
    }

    private boolean isLandingExpCandidate(LandingExperienceShareVO landingExperienceShareVO) {
        if(StringUtils.isBlank(landingExperienceShareVO.getUniversityName())) {
            return false;
        }
        if(StringUtils.isBlank(landingExperienceShareVO.getExamDate())) {
            return false;
        }
        if(StringUtils.isBlank(landingExperienceShareVO.getInvolvedDirectionName())) {
            if(StringUtils.isBlank(landingExperienceShareVO.getMajorDirectionName())) {
                return false;
            }
        }
        if(StringUtils.isBlank(landingExperienceShareVO.getExamMark())) {
            return false;
        }
        return true;
    }

    private LandingExperienceShareVO transferLandingExpToVO(LandingExperience landingExp, String fid) {
        LandingExperienceShareVO vo = new LandingExperienceShareVO();
        vo.setFid(fid);
        vo.setUniversityName(landingExp.getUniversityName());
        vo.setBroadDirectionName(landingExp.getBroadDirectionName());
        vo.setInvolvedDirectionName(landingExp.getInvolvedDirectionName());
        vo.setMajorDirectionName(landingExp.getMajorDirectionName());
        vo.setExamDate(landingExp.getExamDate());
        vo.setExamMark(landingExp.getExamMark());
        vo.setExamRank(landingExp.getExamRank());
        vo.setReExamMark(landingExp.getReExamMark());
        vo.setReExamMarkLimit(landingExp.getReExamMarkLimit());
        vo.setContactInfo(landingExp.getContactInfo());
        vo.setMasterType(landingExp.getMasterType());
        vo.setExperience(landingExp.getExperience());
        vo.setBachelorUniversity(landingExp.getBachelorUniversity());
        return vo;
    }

    private LandingExperience transferLandingExpVOToNor(
            LandingExperienceShareVO vo) {
        LandingExperience landingExperience = new LandingExperience();
        String universityName = vo.getUniversityName();
        Long universityId = universityDaoService.getUniversityIdByName(universityName);
        String broadDirectionName = vo.getBroadDirectionName();
        String involvedDirectionName = vo.getInvolvedDirectionName();
        String majorDirectionName = vo.getMajorDirectionName();
        String examDate = vo.getExamDate();
        String examMark = vo.getExamMark();
        String examRank = vo.getExamRank();
        String reExamMark = vo.getReExamMark();
        String reExamMarkLimit = vo.getReExamMarkLimit();
        String contactInfo = vo.getContactInfo();
        int masterType = vo.getMasterType();
        String bachelorUniversity = vo.getBachelorUniversity();
        String experience = vo.getExperience();

        landingExperience.setUniversityId(universityId);
        landingExperience.setUniversityName(universityName);
        landingExperience.setBroadDirectionName(broadDirectionName);
        landingExperience.setInvolvedDirectionName(involvedDirectionName);
        landingExperience.setMajorDirectionName(majorDirectionName);
        landingExperience.setExamDate(examDate);
        landingExperience.setExamMark(examMark);
        landingExperience.setExamRank(examRank);
        landingExperience.setReExamMark(reExamMark);
        landingExperience.setReExamMarkLimit(reExamMarkLimit);
        landingExperience.setContactInfo(contactInfo);
        landingExperience.setMasterType(masterType);
        landingExperience.setExperience(experience);
        landingExperience.setBachelorUniversity(bachelorUniversity);
        landingExperience.setCreated(new Date());
        landingExperience.setUpdated(new Date());
        return landingExperience;
    }
}
