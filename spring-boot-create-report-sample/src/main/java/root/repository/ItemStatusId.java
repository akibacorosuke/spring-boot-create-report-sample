package root.repository;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ItemStatusId implements Serializable {
    private static final long serialVersionUID = -7644323166118394950L;

    String itemCode;

    String warehouseCode;

}
