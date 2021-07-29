package root.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.io.Resources;

import root.config.Messages;
import root.constant.CreateReportSampleConstants;
import root.dto.ItemReportDto;
import root.entity.ItemInfoEntity;
import root.entity.ItemStatusEntity;
import root.exception.SpringBootCreateReportSampleException;
import root.helper.GoogleDriveApi;
import root.repository.ItemInfoRepository;
import root.repository.ItemStatusRepository;

@ExtendWith(SpringExtension.class)
public class SpringBootCreateReportSampleLogicTest {
    @Mock
    private Messages messages;

    @Mock
    private ItemInfoRepository itemInfoRepository;

    @Mock
    private ItemStatusRepository itemStatusRepository;

    @Mock
    private GoogleDriveApi googleDriveApi;

    @InjectMocks
    private SpringBootCreateReportSampleLogic logic;

    static final File workDir = new File(CreateReportSampleConstants.WORK_PATH);
    static final File exportDir = new File(CreateReportSampleConstants.EXPORT_PATH);
    static final File backupDir = new File(System.getProperty("java.io.tmpdir") + "/testBackup/");

    @BeforeAll
    static void beforeAllTest() {
        try {
            FileUtils.deleteDirectory(backupDir);
            // move export dir to avoid deleting existing files
            FileUtils.moveDirectory(exportDir, backupDir);
            FileUtils.forceMkdir(workDir);
            FileUtils.forceMkdir(exportDir);
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @BeforeEach
    void beforeEachTest() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void afterAllTests() {
        try {
            FileUtils.deleteDirectory(exportDir);
            FileUtils.moveDirectory(backupDir, exportDir);
            FileUtils.deleteDirectory(backupDir);
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void whenNoWorkDirThenCreate() {
        try {
            FileUtils.deleteDirectory(workDir);
            FileUtils.deleteDirectory(exportDir);
            logic.prepareDir();
            Assertions.assertTrue(workDir.isDirectory());
            Assertions.assertTrue(exportDir.isDirectory());
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void whenFileInWorkDirThenCleanTheDir() {
        try {
            FileUtils.forceMkdir(workDir);
            new File(CreateReportSampleConstants.WORK_PATH + CreateReportSampleConstants.FILE_NAME).createNewFile();
            logic.prepareDir();
            Assertions.assertTrue(workDir.list().length == 0);
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void whenFileInWorkDirIsOpenThenThrowIOException() {
        try {
            FileUtils.deleteDirectory(workDir);
            workDir.createNewFile();
            Assertions.assertThrows(IOException.class, () -> logic.prepareDir());
            FileUtils.delete(workDir);
            FileUtils.forceMkdir(workDir);
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testGetItemReportDtosMethodWorksSuccess() {
        final List<ItemInfoEntity> ItemInfoEntities = Arrays.asList(
                        new ItemInfoEntity("1220", "item name4", "author10", 900, 950, "20210301", "0"),
                        new ItemInfoEntity("1230", "item name5", "author0", 1000, 1030, "20210405", "0"),
                        new ItemInfoEntity("1234", "item name1", "author1", 1100, 1180, "20210510", "0"),
                        new ItemInfoEntity("1235", "item name2", "author2", 1200, 1290, "20210611", "0"),
                        new ItemInfoEntity("1236", "item name3", "author3", 1300, 1395, "20210712", "0"));
        
        Mockito.when(itemInfoRepository.findByDeleteFlg("0")).thenReturn(ItemInfoEntities);
        final List<ItemStatusEntity> ItemStatusEntities = new ArrayList<>(
                Arrays.asList( 
                        // only exist in warehouse 1
                        new ItemStatusEntity("1220", "1", 1, 2, 3),
                        // only exist in warehouse 2
                        new ItemStatusEntity("1230", "2", 4, 5, 6),
                        // same data in both 1 and 2 warehouse to check the inventory calculation
                        new ItemStatusEntity("1234", "1", 3, 4, 10), 
                        new ItemStatusEntity("1234", "2", 2, 5, 11),
                        new ItemStatusEntity("1235", "1", 0, 5, 18), 
                        new ItemStatusEntity("1235", "2", 4, 1, 26),
                        // no data of the same item code in item info
                        new ItemStatusEntity("1237", "2", 2, 5, 11)));
        
        Mockito.when(itemStatusRepository.findByWarehouseCodeIn(Arrays.asList("1", "2"))).thenReturn(ItemStatusEntities);
        
        final List<ItemReportDto> expectedItemReportDtos = Arrays.asList(
                new ItemReportDto("1220", 900, 950, "item name4", "author10", "20210301", 3, 3, 0, 1, 1, 0, 2, 2, 0),
                new ItemReportDto("1230", 1000, 1030, "item name5", "author0", "20210405", 6, 0, 6, 4, 0, 4, 5, 0, 5),
                new ItemReportDto("1234", 1100, 1180, "item name1", "author1", "20210510", 21, 10, 11, 5, 3, 2, 9, 4, 5),
                new ItemReportDto("1235", 1200, 1290, "item name2", "author2", "20210611", 44, 18, 26, 4, 0, 4, 6, 5, 1),
                new ItemReportDto("1236", 1300, 1395, "item name3", "author3", "20210712", 0, 0, 0, 0, 0, 0, 0, 0, 0));
        try {
            final List<ItemReportDto> actualItemReportDtos = logic.getItemReportDtos();
            
            Assertions.assertEquals(expectedItemReportDtos, actualItemReportDtos);
        } catch (final SpringBootCreateReportSampleException e) {
            Assertions.fail((Throwable) e);
        }
    }

    @Test
    void whenNoItemInfoThenThrowException() {
        final String expectedErrorMsg = "root.exception.SpringBootCreateReportSampleException";
        Mockito.when(itemInfoRepository.findByDeleteFlg("0")).thenReturn(null);
        try {
            Mockito.when(logic.getItemReportDtos()).thenThrow(SpringBootCreateReportSampleException.class);
        } catch (final SpringBootCreateReportSampleException e) {
            Assertions.assertEquals(expectedErrorMsg, e.getClass().getName());
        }
    }

    @Test
    void testCreateFileWithDataSuccess() {
        try {
            final List<ItemReportDto> expectedItemReportDtos = Arrays.asList(
                    new ItemReportDto("1234", 1100, 1180, "item name1", "author1", "20210510", 21, 10, 11, 5, 3, 2, 9, 4, 5),
                    new ItemReportDto("1235", 1200, 1290, "item name2", "author2", "20210611", 44, 18, 26, 4, 0, 4, 6, 5, 1),
                    new ItemReportDto("1236", 1300, 1395, "item name3", "author3", "20210712", 6, 0, 6, 4, 0, 4, 5, 0, 5),
                    new ItemReportDto("1230", 1000, 1030, "item name4", "author4", "20210805", 0, 0, 0, 0, 0, 0, 0, 0, 0));
            
            logic.createFile(expectedItemReportDtos);
            
            final File exceptedFile = new File(Resources.getResource("inventory_info_sample.csv").getPath());
            final File workDirFile = new File(CreateReportSampleConstants.WORK_PATH + CreateReportSampleConstants.FILE_NAME);
            final File exportDirFile = new File(CreateReportSampleConstants.EXPORT_PATH + CreateReportSampleConstants.FILE_NAME);
            // file name check
            Assertions.assertEquals(CreateReportSampleConstants.FILE_NAME, workDirFile.getName());
            Assertions.assertEquals(CreateReportSampleConstants.FILE_NAME, exportDirFile.getName());
            // file content check
            Assertions.assertTrue(FileUtils.contentEquals(exceptedFile, workDirFile));
            Assertions.assertTrue(FileUtils.contentEquals(exceptedFile, exportDirFile));
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testCreateFileNoDataThenThrowException() {
        final String expectedErrorMsg = "java.lang.NullPointerException";
        try {
            logic.createFile(null);
        } catch (final Exception e) {
            Assertions.assertEquals(expectedErrorMsg, e.getClass().getName());
        }
    }

    @Test
    void testUpload() {
        try {
            logic.uploadFile();
        } catch (final IOException e) {
            Assertions.fail(e);
        }
    }

}
