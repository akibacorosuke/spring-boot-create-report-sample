package root.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoEntity implements Serializable {

    private static final long serialVersionUID = -7070196764329221312L;

    @Id
    String itemCode;

    String itemName;

    String author;

    int originalPrice;

    int priceWithTax;

    String releaseDate;

    String deleteFlg;

}
