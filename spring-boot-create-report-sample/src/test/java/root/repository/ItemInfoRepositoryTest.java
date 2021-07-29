package root.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import root.entity.ItemInfoEntity;
import root.entity.ItemStatusEntity;
import root.service.SpringBootCreateReportSampleService;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("root")
@PropertySource("application.properties")
public class ItemInfoRepositoryTest {

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @Autowired
    private ItemStatusRepository itemStatusRepository;

    @Autowired
    SpringBootCreateReportSampleService service;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DatabaseSetup({ "/dbFile/item_info.xml" })
    void testFindByDeleteFlg() {
        final List<ItemInfoEntity> expectedItemInfoEntities = Arrays.asList(
                new ItemInfoEntity("1234", "hoge1", "author1", 800, 850, "20210710", "0"),
                new ItemInfoEntity("1235", "hoge2", "author2", 1300, 1380, "20210711", "0"),
                new ItemInfoEntity("1236", "hoge3", null, 1400, 1490, "20210712", "0"));
        Assertions.assertEquals(expectedItemInfoEntities, itemInfoRepository.findByDeleteFlg("0"));
    }

    @Test
    @DatabaseSetup({ "/dbFile/item_status.xml" })
    void testFindByWarehouseCodeIn() {
        final List<ItemStatusEntity> expectedItemStatusEntity = Arrays.asList(
                // expect data of warehouse 1 and 2 
                new ItemStatusEntity("1234", "1", 0, 3, 10), 
                new ItemStatusEntity("1234", "2", 4, 5, 15),
                new ItemStatusEntity("1235", "1", 8, 9, 30), 
                new ItemStatusEntity("1236", "2", 10, 11, 40));
        Assertions.assertEquals(expectedItemStatusEntity,
                itemStatusRepository.findByWarehouseCodeIn(Arrays.asList("1", "2")));
    }
}
