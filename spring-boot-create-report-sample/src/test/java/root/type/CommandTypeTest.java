package root.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CommandTypeTest {
    
    @Test
    void success() {
        Assertions.assertEquals(0, CommandType.SUCCESS.getValue());
        Assertions.assertEquals(1, CommandType.FAILURE.getValue());
    }
}
