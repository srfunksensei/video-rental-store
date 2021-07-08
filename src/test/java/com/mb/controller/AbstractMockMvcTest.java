package com.mb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractMockMvcTest {

    @Autowired
    protected ObjectMapper jsonMapper;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    protected <T> T parseResponse(final MvcResult result, final TypeReference<T> typeReference) throws IOException {
        return jsonMapper.readValue(result.getResponse().getContentAsByteArray(), typeReference);
    }
}
