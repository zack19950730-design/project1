package com.assetsystem.repository;

import com.assetsystem.entity.AssetCategory;
import com.assetsystem.entity.AssetItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** 所有查詢都帶 memberId，確保只能存取自己的資產 */
public interface AssetItemRepository extends JpaRepository<AssetItem, Long> {

    List<AssetItem> findByMemberIdOrderByIdAsc(Long memberId);

    List<AssetItem> findByMemberIdAndCategoryOrderByIdAsc(Long memberId, AssetCategory category);

    Optional<AssetItem> findByMemberIdAndCategoryAndName(Long memberId, AssetCategory category, String name);

    Optional<AssetItem> findByIdAndMemberId(Long id, Long memberId);
}
