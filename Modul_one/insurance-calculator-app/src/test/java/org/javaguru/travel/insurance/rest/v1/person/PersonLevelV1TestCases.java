package org.javaguru.travel.insurance.rest.v1.person;

import org.javaguru.travel.insurance.rest.v1.TravelCalculatePremiumControllerV1TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersonLevelV1TestCases extends TravelCalculatePremiumControllerV1TestCase {

    private static final String TEST_FILE_BASE_FOLDER = "person";

    @Test
    @DisplayName("ERROR_CODE_7 personFirstName is NULL")
    public void check_ERROR_CODE_7_NULL() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_7_personFirstName_is_null");
    }

    @Test
    @DisplayName("ERROR_CODE_7 personFirstName is empty")
    public void check_ERROR_CODE_7_EMPTY() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_7_personFirstName_is_empty");
    }

    @Test
    @DisplayName("ERROR_CODE_8 personLastName is NULL")
    public void check_ERROR_CODE_8_NULL() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_8_personLastName_is_null");
    }

    @Test
    @DisplayName("ERROR_CODE_8 personLastName is empty")
    public void check_ERROR_CODE_8_EMPTY() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_8_personLastName_is_empty");
    }

    @Test
    @DisplayName("ERROR_CODE_11 personBirthDate is NULL")
    public void check_ERROR_CODE_11() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_11_personBirthDate_is_null");
    }

    @Test
    @DisplayName("ERROR_CODE_12 personBirthDate in the future")
    public void check_ERROR_CODE_12() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_12_personBirthDate_in_the_future");
    }

    @Test
    @DisplayName("ERROR_CODE_16 personCode is NULL, must not be empty")
    public void check_ERROR_CODE_16_NULL() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_16_personCode_is_null");
    }

    @Test
    @DisplayName("ERROR_CODE_16 personCode is empty, must not be empty")
    public void check_ERROR_CODE_16_EMPTY() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_16_personCode_is_empty");
    }

    @Test
    @DisplayName("ERROR_CODE_21 personCode invalid format")
    public void check_ERROR_CODE_21_InvalidFormat() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_21_personCode_invalid_format");
    }

    @Test
    @DisplayName("ERROR_CODE_22 personFirstName invalid format")
    public void check_ERROR_CODE_22_InvalidFormat() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_22_personFirstName_invalid_format");
    }

    @Test
    @DisplayName("ERROR_CODE_23 personLastName invalid format")
    public void check_ERROR_CODE_23_InvalidFormat() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_23_personLastName_invalid_format");
    }

    @Test
    @DisplayName("ERROR_CODE_24 personFirstName is too long")
    public void check_ERROR_CODE_24_personFirstName_is_too_long() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_24_personFirstName_is_too_long");
    }

    @Test
    @DisplayName("ERROR_CODE_24 personLastName is too long")
    public void check_ERROR_CODE_24_personLastName_is_too_long() throws Exception {
        executeAndCompare(TEST_FILE_BASE_FOLDER + "/ERROR_CODE_24_personLastName_is_too_long");
    }

}
