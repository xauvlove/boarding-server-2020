package com.xauv.web;

import com.xauv.pojo.BottomTip;
import com.xauv.pojo.UniversityDirection;
import com.xauv.service.daoservice.UniversityDirectionDaoService;
import com.xauv.service.daoservice.UniversityDaoService;
import com.xauv.service.internal.BottomTipService;
import com.xauv.service.internal.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/coreservice")
public class PrivateController {

    @Autowired
    private UniversityDaoService universityDaoService;

    @Resource(name = "internal-universityService")
    private UniversityService universityService;

    @Autowired
    private UniversityDirectionDaoService universityDirectionDaoService;

    @Autowired
    private BottomTipService bottomTipService;


    @PostMapping("/tip/bottom")
    public ResponseEntity<Void> saveBottomTips(List<BottomTip> bottomTipList) {
        bottomTipService.saveBottomTips(bottomTipList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updateUniversityAttrs")
    public ResponseEntity<Void> updateUniversityAttributes() {
        universityService.updateUniversityAttributes();
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/updatenames")
    public ResponseEntity<Void> updateUniversityNames() {
        universityService.updateUniversityNames();
        return ResponseEntity.ok().build();
    }*/

    /*@GetMapping("/fullwithuniversityattribute")
    public ResponseEntity<Void> fullWithUniversityAttributes() throws Exception {
        universityService.fullWithUniversityAttributes();
        return ResponseEntity.ok().build();
    }*/

    /*@GetMapping("/fullwithuniversitydetail")
    public ResponseEntity<Void> insertAllUniversityDetail() {
        try {
            universityService.insertAllUniversityDetail();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

   /* @GetMapping("/createUniversityDb")
    public ResponseEntity<Void> fullWithUniversityTable() {
        try {
            universityDaoService.fullWithUniversityTable();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

    /*@GetMapping("/getUniversityCount")
    public ResponseEntity<Integer> testAllUniversitiesWereInserted() {
        try {
            return ResponseEntity.ok(universityDaoService.getAllUniversitiesWereInserted());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

    /**
     * 填充 bd_university_direction 表
     * 核心方法
     * 使用6个线程
     * @return
     */
    /*@GetMapping("/fullWithUniversityDirection")
    public ResponseEntity<Void> insertAllUniversityDirection() {
        try {
            for (int i = 0; i < 6; i++) {
                universityDirectionDaoService.insertAllUniversityDirection(i, 1);
            }
            for (int i = 0; i < 6; i++) {
                universityDirectionDaoService.insertAllUniversityDirection(i, 2);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/

    /*@GetMapping("/university")
    public ResponseEntity<List<UniversityDirection>> getUniversityDirectionById(
            @RequestParam("name") String name, @RequestParam("masterType")Integer masterType,
            @RequestParam("majorDirectionName")String majorDirectionName) {
        return ResponseEntity.ok(universityDirectionDaoService.getUniversityDirectionById(name, masterType, majorDirectionName));
    }*/
}
