package org.javaguru.blacklist.rest;

import org.javaguru.blacklist.rest.common.JsonFileReader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BlackListedPersonCheckControllerTestCase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
     private JsonFileReader jsonFileReader;

    private static final String BASE_URL = "/blacklist/person/check/";


    protected void executeAndCompare(String testCaseFolderName) throws Exception {
        executeAndCompare(
                "rest/" + testCaseFolderName + "/request.json",
                "rest/" + testCaseFolderName + "/response.json",
                false
        );
    }
    protected void executeAndCompare(String testCaseFolderName,
                                     boolean ignoreUUIDValue) throws Exception {
        executeAndCompare(
                "rest/" + testCaseFolderName + "/request.json",
                "rest/" + testCaseFolderName + "/response.json",
                ignoreUUIDValue
        );
    }
    protected void executeAndCompare(String jsonRequestFilePath,
                                     String jsonResponseFilePath,
                                     boolean ignoreUUIDValue) throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile(jsonRequestFilePath);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();

        String jsonResponse = jsonFileReader.readJsonFromFile(jsonResponseFilePath);

        if (ignoreUUIDValue) {
            assertJson(responseBodyContent)
                    .where()
                    .keysInAnyOrder()
                    .arrayInAnyOrder()
                    .isEqualTo(jsonResponse);
        } else {
            assertJson(responseBodyContent)
                    .where()
                    .keysInAnyOrder()
                    .arrayInAnyOrder()
                    .isEqualTo(jsonResponse);
        }
    }
}
