package com.ravindercodes.service;

import com.ravindercodes.dto.request.TestRequest;
import org.springframework.http.ResponseEntity;

public interface TestService {

    public ResponseEntity<?> save(TestRequest testRequest);

    public ResponseEntity<?> getAllRecord();

}
