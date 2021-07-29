package root.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import root.config.Messages;
import root.constant.CreateReportSampleConstants;

@ExtendWith(SpringExtension.class)
public class MessagesTest {

    @InjectMocks
    private Messages messages;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMessage() {
        final String expected = "Report creation sample batch starts";
        
        Mockito.when(messageSource.getMessage(ArgumentMatchers.eq(CreateReportSampleConstants.MSG_INFO_EXEC_START),
                ArgumentMatchers.eq(new Object[] {}), ArgumentMatchers.any())).thenReturn(expected);
        final String actual = messages.format(CreateReportSampleConstants.MSG_INFO_EXEC_START);
        Assertions.assertEquals(expected, actual);
    }
}
