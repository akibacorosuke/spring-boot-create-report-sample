package root.dto;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@CsvBean(header = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemReportDto {
    @CsvColumn(number = 1, label = "item code")
    private String itemCode;

    @CsvColumn(number = 2, label = "original price")
    private int originalPrice;

    @CsvColumn(number = 3, label = "price with tax")
    private int priceWithTax;

    @CsvColumn(number = 4, label = "item name")
    private String itemName;

    @CsvColumn(number = 5, label = "author")
    private String author;

    @CsvColumn(number = 6, label = "release date")
    private String releaseDate;

    @CsvColumn(number = 7, label = "all inventory num")
    private int allInventoryNum;

    @CsvColumn(number = 8, label = "inventory number in warehouse01")
    private int inventoryNumWarehouse01;

    @CsvColumn(number = 9, label = "inventory number in warehouse02")
    private int inventoryNumWarehouse02;

    @CsvColumn(number = 10, label = "damage typeA all")
    private int damageTypeAAll;

    @CsvColumn(number = 11, label = "damage typeA number in warehouse01")
    private int damageTypeA_Warehouse01;

    @CsvColumn(number = 12, label = "damage typeA number in warehouse02")
    private int damageTypeA_Warehouse02;

    @CsvColumn(number = 13, label = "damage typeB all")
    private int damageTypeBAll;

    @CsvColumn(number = 14, label = "damage typeB number in warehouse01")
    private int damageTypeB_Warehouse01;

    @CsvColumn(number = 15, label = "damage typeB number in warehouse02")
    private int damageTypeB_Warehouse02;

}
