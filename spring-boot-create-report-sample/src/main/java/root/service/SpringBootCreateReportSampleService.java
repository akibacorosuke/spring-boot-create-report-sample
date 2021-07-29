package root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import root.config.Messages;
import root.constant.CreateReportSampleConstants;
import root.logic.SpringBootCreateReportSampleLogic;
import root.type.CommandType;

@Slf4j
@Service
public class SpringBootCreateReportSampleService {

    @Autowired
    private Messages messages;

    @Autowired
    SpringBootCreateReportSampleLogic springBootSampleFormatLogic;

    public CommandType execute() {
        try {
            log.info(messages.format(CreateReportSampleConstants.MSG_INFO_EXEC_START));

            //create and clean dir
            springBootSampleFormatLogic.prepareDir();

            //create inventory report 
            springBootSampleFormatLogic.createFile(springBootSampleFormatLogic.getItemReportDtos());

            //upload report to cloud
            springBootSampleFormatLogic.uploadFile();

        } catch (final Throwable e) {
            log.error(messages.format(CreateReportSampleConstants.MSG_ERROR_EXEC_END), e.getMessage());
            return CommandType.FAILURE;
        }
        log.info(messages.format(CreateReportSampleConstants.MSG_INFO_EXEC_END));
        return CommandType.SUCCESS;
    }
}
