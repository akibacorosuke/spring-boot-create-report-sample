package root.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.repository.ItemStatusId;

@Entity
@IdClass(ItemStatusId.class)
@Table(name = "item_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStatusEntity implements Serializable {

    private static final long serialVersionUID = 4613402454099661420L;

    @Id
    String itemCode;

    @Id
    String warehouseCode;

    @Column(name = "damage_a_invt_num")
    int damageTypeA;

    @Column(name = "damage_b_invt_num")
    int damageTypeB;

    int invtNum;

}
