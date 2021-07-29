package root.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import root.config.Messages;
import root.dto.ItemReportDto;
import root.exception.SpringBootCreateReportSampleException;
import root.logic.SpringBootCreateReportSampleLogic;
import root.type.CommandType;

@ExtendWith(SpringExtension.class)
public class SpringBootCreateReportSampleServiceTest {

    @Mock
    Messages messages;

    @Mock
    SpringBootCreateReportSampleLogic logic;

    @InjectMocks
    SpringBootCreateReportSampleService service;

    @Test
    void executeThenReturnSuccess() {
        final List<ItemReportDto> itemReportDtos = new ArrayList<>();
        try {
            Mockito.when(logic.getItemReportDtos()).thenReturn(itemReportDtos);
            Assertions.assertEquals(CommandType.SUCCESS, service.execute());
        } catch (final SpringBootCreateReportSampleException e) {
            Assertions.fail((Throwable) e);
        }
    }

    @Test
    void whenMethodInServiceIsCalledThenExceptionIsThrown() {
        try {
            Mockito.doThrow(IOException.class).when(logic).prepareDir();
            Assertions.assertEquals(CommandType.FAILURE, service.execute());
            
            Mockito.doThrow(IOException.class).when(logic).createFile(Mockito.anyList());
            Assertions.assertEquals(CommandType.FAILURE, service.execute());
            
            Mockito.doThrow(IOException.class).when(logic).uploadFile();
            Assertions.assertEquals(CommandType.FAILURE, service.execute());
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }
}
