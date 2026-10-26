package com.assetsystem.repository;

import com.assetsystem.entity.AssetCategory;
import com.assetsystem.entity.AssetItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetItemRepository extends JpaRepository<AssetItem, Long> {

    List<AssetItem> findByCategoryOrderByIdAsc(AssetCategory category);

    Optional<AssetItem> findByCategoryAndName(AssetCategory category, String name);
}
