package com.ravindercodes.controller;

import com.ravindercodes.dto.request.TestRequest;
import com.ravindercodes.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@Valid @RequestBody final TestRequest testRequest) {
        return this.testService.save(testRequest);
    }

    @GetMapping("/get-all-record")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllRecord() {
        return this.testService.getAllRecord();
    }

}
