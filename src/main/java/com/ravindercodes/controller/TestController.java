package com.ravindercodes.controller;

import com.ravindercodes.dto.request.TestRequest;
import com.ravindercodes.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody final TestRequest testRequest) {
        return this.testService.save(testRequest);
    }

    @GetMapping("/get-all-record")
    public ResponseEntity<?> getAllRecord() {
        return this.testService.getAllRecord();
    }

}
