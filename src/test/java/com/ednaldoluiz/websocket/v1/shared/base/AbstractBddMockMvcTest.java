package com.ednaldoluiz.websocket.v1.shared.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.spring.CucumberContextConfiguration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@CucumberContextConfiguration
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public abstract class AbstractBddMockMvcTest {
    
    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper mapper = new ObjectMapper();
    
}