package com.ravindercodes.controller;

import com.ravindercodes.dto.request.TestRequest;
import com.ravindercodes.service.TestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

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
    public ResponseEntity<?> getAllRecord(@RequestParam(defaultValue = "0") final int page,
                                          @RequestParam(defaultValue = "10") final int size,
                                          @RequestParam(defaultValue = "id") final String sortBy,
                                          @RequestParam(defaultValue = "asc") final String sortDir) {
        return this.testService.getAllRecord(page, size, sortBy, sortDir);
    }

}
