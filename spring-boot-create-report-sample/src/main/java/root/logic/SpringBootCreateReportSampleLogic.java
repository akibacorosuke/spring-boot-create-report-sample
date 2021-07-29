package root.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanWriter;
import com.google.api.client.util.Objects;
import com.google.api.services.drive.Drive;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import root.config.Messages;
import root.constant.CreateReportSampleConstants;
import root.dto.ItemReportDto;
import root.entity.ItemInfoEntity;
import root.entity.ItemStatusEntity;
import root.exception.SpringBootCreateReportSampleException;
import root.helper.GoogleDriveApi;
import root.repository.ItemInfoRepository;
import root.repository.ItemStatusRepository;

@Slf4j
@Component
public class SpringBootCreateReportSampleLogic {

    @Autowired
    private Messages messages;

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @Autowired
    private ItemStatusRepository itemStatusRepository;

    @Autowired
    private GoogleDriveApi googleDriveApi;

    public void prepareDir() throws IOException {
        final File workDir = new File(CreateReportSampleConstants.WORK_PATH);
        FileUtils.forceMkdir(workDir);
        FileUtils.cleanDirectory(workDir);

        FileUtils.forceMkdir(new File(CreateReportSampleConstants.EXPORT_PATH));
    }

    public List<ItemReportDto> getItemReportDtos() throws SpringBootCreateReportSampleException {
        // get item info data
        final List<ItemInfoEntity> itemInfoEntities = itemInfoRepository.findByDeleteFlg("0");

        if (CollectionUtils.isEmpty(itemInfoEntities)) {
            throw new SpringBootCreateReportSampleException(
                    messages.format(CreateReportSampleConstants.MSG_ERROR_NO_ITEM_INFO));
        }

        log.info(messages.format(CreateReportSampleConstants.MSG_INFO_ITEM_INFO,
                String.valueOf(itemInfoEntities.size())));

        // get item status data of warehouse 1 and 2
        final List<ItemStatusEntity> itemStatusEntities = itemStatusRepository
                .findByWarehouseCodeIn(Arrays.asList("1", "2"));

        log.info(messages.format(CreateReportSampleConstants.MSG_INFO_ITEM_STATUS,
                String.valueOf(itemStatusEntities.size())));

        final List<ItemReportDto> itemReportDtos = new ArrayList<>();

        itemInfoEntities.forEach(entity -> {
            
            final ItemReportDto itemReportDto = new ItemReportDto();
            
            itemReportDto.setItemCode(entity.getItemCode());
            itemReportDto.setItemName(entity.getItemName());
            itemReportDto.setAuthor(entity.getAuthor());
            itemReportDto.setOriginalPrice(entity.getOriginalPrice());
            itemReportDto.setPriceWithTax(entity.getPriceWithTax());
            itemReportDto.setReleaseDate(entity.getReleaseDate());

            // get item status from warehouse 1
            final ItemStatusEntity itemStatusWare1Entity = itemStatusEntities.stream()
                    .filter(e -> Objects.equal(e.getWarehouseCode(), "1")
                            && Objects.equal(e.getItemCode(), entity.getItemCode()))
                    .findAny().orElse(new ItemStatusEntity());
            // get item status from warehouse 2
            final ItemStatusEntity itemStatusWare2Entity = itemStatusEntities.stream()
                    .filter(e -> Objects.equal(e.getWarehouseCode(), "2")
                            && Objects.equal(e.getItemCode(), entity.getItemCode()))
                    .findAny().orElse(new ItemStatusEntity());

            // inventory number
            itemReportDto.setAllInventoryNum(itemStatusWare1Entity.getInvtNum() + itemStatusWare2Entity.getInvtNum());
            itemReportDto.setInventoryNumWarehouse01(itemStatusWare1Entity.getInvtNum());
            itemReportDto.setInventoryNumWarehouse02(itemStatusWare2Entity.getInvtNum());
            // damage type A
            itemReportDto.setDamageTypeAAll(itemStatusWare1Entity.getDamageTypeA() + itemStatusWare2Entity.getDamageTypeA());
            itemReportDto.setDamageTypeA_Warehouse01(itemStatusWare1Entity.getDamageTypeA());
            itemReportDto.setDamageTypeA_Warehouse02(itemStatusWare2Entity.getDamageTypeA());
            // damage type B 
            itemReportDto.setDamageTypeBAll(itemStatusWare1Entity.getDamageTypeB() + itemStatusWare2Entity.getDamageTypeB());
            itemReportDto.setDamageTypeB_Warehouse01(itemStatusWare1Entity.getDamageTypeB());
            itemReportDto.setDamageTypeB_Warehouse02(itemStatusWare2Entity.getDamageTypeB());
            
            itemReportDtos.add(itemReportDto);
        });
        return itemReportDtos;
    }

    public void createFile(@NonNull final List<ItemReportDto> itemReportDtos) throws IOException {

        final String filePath = CreateReportSampleConstants.WORK_PATH + CreateReportSampleConstants.FILE_NAME;
        
        try (CsvAnnotationBeanWriter<ItemReportDto> csvWriter = new CsvAnnotationBeanWriter<>(ItemReportDto.class,
                Files.newBufferedWriter(
                        new File(filePath).toPath(),
                        StandardCharsets.UTF_8), CsvPreference.EXCEL_PREFERENCE)) {
            
            csvWriter.writeAll(itemReportDtos);
            csvWriter.flush();
        }
        FileUtils.copyFile(new File(filePath), new File(CreateReportSampleConstants.EXPORT_PATH + CreateReportSampleConstants.FILE_NAME));
    }

    public void uploadFile() throws IOException {
        final Drive service = googleDriveApi.connectDrive();
        
        googleDriveApi.upload(service, CreateReportSampleConstants.FILDER_ID,
                CreateReportSampleConstants.EXPORT_PATH, CreateReportSampleConstants.FILE_NAME, "*/*");
        
        log.info(messages.format(CreateReportSampleConstants.MSG_INFO_FILE_UPLOAD, CreateReportSampleConstants.FILE_NAME));
    }
}
