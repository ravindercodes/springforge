package com.ravindercodes.service.impl;

import com.ravindercodes.constant.MessagesConstants;
import com.ravindercodes.dto.request.TestRequest;
import com.ravindercodes.dto.response.SuccessResponse;
import com.ravindercodes.dto.response.TestResponse;
import com.ravindercodes.entity.TestEntity;
import com.ravindercodes.repository.TestRepository;
import com.ravindercodes.service.TestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestImpl implements TestService {

    @Autowired
    private final TestRepository testRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public TestImpl(TestRepository testRepository, ModelMapper modelMapper) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> save(TestRequest testRequest) {
        TestEntity testEntity = modelMapper.map(testRequest, TestEntity.class);
        TestEntity savedRecord = this.testRepository.save(testEntity);
        TestResponse response = modelMapper.map(savedRecord, TestResponse.class);
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.RECORD_SAVED, response));
    }

    @Override
    public ResponseEntity<?> getAllRecord() {
        List<TestEntity> testList = this.testRepository.findAll();
        return ResponseEntity.ok(SuccessResponse.success(MessagesConstants.RECORD_SAVED, testList));
    }
}
