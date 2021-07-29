package root.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {
    
    SUCCESS(0), 
    FAILURE(1);

    private int value;
}
