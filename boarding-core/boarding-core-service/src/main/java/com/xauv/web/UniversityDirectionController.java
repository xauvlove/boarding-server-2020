package com.xauv.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.xauv.pojo.UniversityDirection;
import com.xauv.pojo.vo.UniversityDirectionVO;
import com.xauv.service.daoservice.UniversityDirectionDaoService;
import com.xauv.utils.ConditionCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/university/direction")
public class UniversityDirectionController {

    @Autowired
    private UniversityDirectionDaoService universityDirectionDaoService;
    @Autowired
    private ConditionCheckUtil conditionCheckUtil;

    @GetMapping("/list")
    public ResponseEntity<List<UniversityDirectionVO>> getUniversityVoByConditions(
             UniversityDirection universityDirection) throws IllegalAccessException, JsonProcessingException {
        if(conditionCheckUtil.checkRequestParam(universityDirection, 3).getCode() < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(universityDirectionDaoService.getUniversityByConditions(universityDirection));
    }
}
