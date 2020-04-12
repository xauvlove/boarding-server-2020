package com.xauv.web;

import com.xauv.format.InternalStandardMessageFormat;
import com.xauv.pojo.vo.LandingExperienceShareVO;
import com.xauv.resultful.PageResult;
import com.xauv.service.daoservice.LandingExperienceDaoService;
import com.xauv.service.dispatcherservice.LandingExperienceDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/share")
public class LandingExperienceController {

    @Autowired
    private LandingExperienceDaoService landingExperienceDaoService;

    @Autowired
    private LandingExperienceDispatcherService landingExperienceDispatcherService;

    @PostMapping("/landing/experience")
    public ResponseEntity<Void> saveLandingExperience(
            @RequestBody LandingExperienceShareVO landingExperienceShareVO) {
        //InternalStandardMessageFormat format = landingExperienceDispatcherService.doDispatcher(landingExperienceShareVO);
        InternalStandardMessageFormat format =
                landingExperienceDaoService.saveLandingExperience(landingExperienceShareVO);
        if(format.getCode() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/landing/experience")
    public ResponseEntity<PageResult<LandingExperienceShareVO>> getLandingExperiencesByPage(
            @RequestParam("page") Integer page, @RequestParam("rows") Integer rows, @RequestParam("loginSession") String loginSession) {
        try {
            return ResponseEntity.ok(landingExperienceDaoService.queryLandingExperienceByPage(page,rows, loginSession));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(landingExperienceDaoService.queryLandingExperienceByPageWithDirectError());
        }
    }
}
