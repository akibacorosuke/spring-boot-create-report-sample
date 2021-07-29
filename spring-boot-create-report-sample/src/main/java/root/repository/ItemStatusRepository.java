package root.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entity.ItemStatusEntity;

public interface ItemStatusRepository extends JpaRepository<ItemStatusEntity, ItemStatusId> {
    
    List<ItemStatusEntity> findByWarehouseCodeIn(List<String> paramList);
    
}
