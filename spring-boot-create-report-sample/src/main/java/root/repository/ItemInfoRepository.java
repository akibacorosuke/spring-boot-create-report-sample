package root.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entity.ItemInfoEntity;

public interface ItemInfoRepository extends JpaRepository<ItemInfoEntity, String> {
    
    List<ItemInfoEntity> findByDeleteFlg(String paramString);
    
}
