package org.javaguru.blacklist.core.validations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorCodeUtilTest {

    @Test
    public void shouldGetErrorDescription() throws Exception {
        ErrorCodeUtil errorCodeUtil = new ErrorCodeUtil();

        String description = errorCodeUtil.getErrorDescription("ERROR_CODE_1");

        assertEquals("Field personFirstName must not be empty!", description);
    }
}
