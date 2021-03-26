package pl.travel.travelapp.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.travel.travelapp.DTO.UserRegisterDTO;

import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    public static String asJsonString(final Object obj) {
        try {

            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnRegistrationStatusOk(){
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("faronnorbertkrk@gmail.com");
        userRegisterDTO.setFirstName("Norbert");
        userRegisterDTO.setLogin("norbert151748");
        userRegisterDTO.setSurName("Faron");
        userRegisterDTO.setPassword("N@jwalxcm123ka");
        userRegisterDTO.setNationality("Poland");
        userRegisterDTO.setBirthDay("03 Feb 2017");

        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .content(asJsonString(userRegisterDTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.ALL))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn();
            Assertions.assertEquals("a","a");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}