package com.assetsystem.service;

import com.assetsystem.dto.AssetItemRequest;
import com.assetsystem.entity.AssetCategory;
import com.assetsystem.entity.AssetItem;
import com.assetsystem.entity.Currency;
import com.assetsystem.exception.NotFoundException;
import com.assetsystem.repository.AssetItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AssetItemService {

    private final AssetItemRepository repository;
    private final ExchangeRateService exchangeRateService;

    public AssetItemService(AssetItemRepository repository, ExchangeRateService exchangeRateService) {
        this.repository = repository;
        this.exchangeRateService = exchangeRateService;
    }

    public List<AssetItem> findAll(Long memberId) {
        return repository.findByMemberIdOrderByIdAsc(memberId);
    }

    public List<AssetItem> findByCategory(Long memberId, AssetCategory category) {
        return repository.findByMemberIdAndCategoryOrderByIdAsc(memberId, category);
    }

    /**
     * 同類別同名稱已存在就更新金額，否則新增 (與原本 handleAsset 的行為相同)
     */
    @Transactional
    public AssetItem upsert(Long memberId, AssetItemRequest request) {
        String name = request.name().trim();
        AssetItem item = repository.findByMemberIdAndCategoryAndName(memberId, request.category(), name)
                .orElseGet(() -> {
                    AssetItem created = new AssetItem();
                    created.setMemberId(memberId);
                    created.setCategory(request.category());
                    created.setName(name);
                    created.setCurrency(guessCurrency(name));
                    return created;
                });
        item.setAmount(request.amount());
        if (request.currency() != null) {
            item.setCurrency(request.currency());
        }
        return repository.save(item);
    }

    @Transactional
    public AssetItem update(Long memberId, Long id, AssetItemRequest request) {
        AssetItem item = repository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("找不到資產項目 id=" + id));
        item.setCategory(request.category());
        item.setName(request.name().trim());
        item.setAmount(request.amount());
        if (request.currency() != null) {
            item.setCurrency(request.currency());
        }
        return repository.save(item);
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        AssetItem item = repository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("找不到資產項目 id=" + id));
        repository.delete(item);
    }

    /** 計算某類別的台幣小計 (美金乘匯率) */
    public BigDecimal totalTwd(Long memberId, AssetCategory category) {
        return findByCategory(memberId, category).stream()
                .map(item -> exchangeRateService.toTwd(item.getAmount(), item.getCurrency()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Currency guessCurrency(String name) {
        return name.contains("美金") || name.contains("美幣") ? Currency.USD : Currency.TWD;
    }
}
