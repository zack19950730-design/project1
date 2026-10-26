package com.assetsystem.controller;

import com.assetsystem.dto.AssetItemRequest;
import com.assetsystem.entity.AssetCategory;
import com.assetsystem.entity.AssetItem;
import com.assetsystem.service.AssetItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 流動資金 / 固定資產 / 負債 */
@RestController
@RequestMapping("/api/assets")
public class AssetItemController {

    private final AssetItemService service;

    public AssetItemController(AssetItemService service) {
        this.service = service;
    }

    /** GET /api/assets?category=CASH，不帶 category 回傳全部 */
    @GetMapping
    public List<AssetItem> list(@RequestParam(required = false) AssetCategory category) {
        return category == null ? service.findAll() : service.findByCategory(category);
    }

    /** POST /api/assets：同類別同名稱則更新金額，否則新增 */
    @PostMapping
    public AssetItem upsert(@Valid @RequestBody AssetItemRequest request) {
        return service.upsert(request);
    }

    @PutMapping("/{id}")
    public AssetItem update(@PathVariable Long id, @Valid @RequestBody AssetItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
