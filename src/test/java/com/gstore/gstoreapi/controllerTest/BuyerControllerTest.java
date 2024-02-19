package com.gstore.gstoreapi.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gstore.gstoreapi.converters.impl.BuyerConverter;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.services.BuyerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BuyerControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void testSaveUser() throws Exception {
        BuyerDTO buyerDTO = BuyerDTO.builder()
                .name("George")
                .age(33)
                .email("test@test.com")
                .country("Romania")
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/buyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeObjectAsJson(buyerDTO)))
                .andExpect(status().is(201));

        mvc.perform(MockMvcRequestBuilders.get("/buyers/1"))
                .andExpect(status().is(302))
                .andExpect(result -> assertEquals(buildUserDTOAsMap(buyerDTO),
                        readJsonAsObject(result.getResponse().getContentAsString())));
    }

    @Test
    void testDeleteUser() {
        BuyerDTO buyerDTO = BuyerDTO.builder()
                .name("George")
                .age(33)
                .email("test@test.com")
                .country("Romania")
                .build();
//
//        mvc.perform(MockMvcRequestBuilders.post("/buyers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(writeObjectAsJson(buyerDTO)))
//                .andExpect(status().is(201));
//
//
//        mvc.perform(MockMvcRequestBuilders.delete("/buyers/1"))
//                .andExpect(status().is());

    }

    @Test
    void testSaveUser_andWrongPayload() throws Exception {
        BuyerDTO buyerDTO = BuyerDTO.builder()
                .name("John")
                .age(18)
                .email("john@doe.com")
                .country("Romania3433")
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/buyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeObjectAsJson(buyerDTO)))
                .andExpect(status().is(400));
    }

    private String writeObjectAsJson(Object obj) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponsePayload readJsonAsObject(String json) {
        ObjectReader or = new ObjectMapper().reader();
        try {
            return or.readValue(json, ResponsePayload.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponsePayload buildUserDTOAsMap(BuyerDTO buyerDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper
                .convertValue(buyerDTO, new TypeReference<>() {});

        return ResponsePayload.builder()
                .response(map)
                .statusCode(HttpStatus.FOUND.name())
                .build();
    }
}
