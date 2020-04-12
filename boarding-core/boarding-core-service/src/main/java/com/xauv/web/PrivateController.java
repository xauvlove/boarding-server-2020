package com.xauv.web;

import com.xauv.pojo.UniversityDirection;
import com.xauv.service.daoservice.UniversityDetailDaoService;
import com.xauv.service.daoservice.UniversityDirectionDaoService;
import com.xauv.service.daoservice.UniversityDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coreservice")
public class PrivateController {

    @Autowired
    private UniversityDaoService universityDaoService;

    @Autowired
    private UniversityDetailDaoService universityDetailDaoService;

    @Autowired
    private UniversityDirectionDaoService universityDirectionDaoService;

    @GetMapping("/fullwithuniversitydetail")
    public ResponseEntity<Void> insertAllUniversityDetail() {
        try {
            universityDetailDaoService.insertAllUniversityDetail();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/createUniversityDb")
    public ResponseEntity<Void> fullWithUniversityTable() {
        try {
            universityDaoService.fullWithUniversityTable();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getUniversityCount")
    public ResponseEntity<Integer> testAllUniversitiesWereInserted() {
        try {
            return ResponseEntity.ok(universityDaoService.getAllUniversitiesWereInserted());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/fullWithUniversityDirection")
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
    }

    @GetMapping("/university")
    public ResponseEntity<List<UniversityDirection>> getUniversityDirectionById(
            @RequestParam("name") String name, @RequestParam("masterType")Integer masterType,
            @RequestParam("majorDirectionName")String majorDirectionName) {
        return ResponseEntity.ok(universityDirectionDaoService.getUniversityDirectionById(name, masterType, majorDirectionName));
    }
}
