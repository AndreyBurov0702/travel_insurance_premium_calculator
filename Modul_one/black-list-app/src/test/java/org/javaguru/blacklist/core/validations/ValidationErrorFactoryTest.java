package org.javaguru.blacklist.core.validations;

import org.javaguru.blacklist.core.api.ValidationErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ValidationErrorFactoryTest {
    private ErrorCodeUtil errorCodeUtil;
    private ValidationErrorFactory validationErrorFactory;

    @BeforeEach
    public void setUp() {
        errorCodeUtil = mock(ErrorCodeUtil.class);
        validationErrorFactory = new ValidationErrorFactory(errorCodeUtil);
    }

    @Test
    public void testBuildError_ReturnsCorrectDTO() {
        String errorCode = "ERROR_CODE_1";
        String errorDescription = "Field personFirstName must not be empty!";
        when(errorCodeUtil.getErrorDescription(errorCode)).thenReturn(errorDescription);

        ValidationErrorDTO result = validationErrorFactory.buildError(errorCode);

        assertNotNull(result);
        assertEquals(errorCode, result.getErrorCode());
        assertEquals(errorDescription, result.getDescription());
        verify(errorCodeUtil, times(1)).getErrorDescription(errorCode);
    }

    @Test
    public void testBuildError_WithNullDescription() {
        String errorCode = "ERROR_CODE_2";
        when(errorCodeUtil.getErrorDescription(errorCode)).thenReturn(null);

        ValidationErrorDTO result = validationErrorFactory.buildError(errorCode);

        assertNotNull(result);
        assertEquals(errorCode, result.getErrorCode());
        assertNull(result.getDescription());
    }
}