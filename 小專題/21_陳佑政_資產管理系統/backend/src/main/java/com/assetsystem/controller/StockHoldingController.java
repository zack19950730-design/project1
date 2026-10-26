package com.assetsystem.controller;

import com.assetsystem.config.AuthInterceptor;
import com.assetsystem.dto.StockHoldingRequest;
import com.assetsystem.dto.StockHoldingResponse;
import com.assetsystem.entity.Market;
import com.assetsystem.service.StockHoldingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 首頁投資看板的持股 */
@RestController
@RequestMapping("/api/holdings")
public class StockHoldingController {

    private final StockHoldingService service;

    public StockHoldingController(StockHoldingService service) {
        this.service = service;
    }

    /** GET /api/holdings?market=TW，附即時股價 */
    @GetMapping
    public List<StockHoldingResponse> list(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
                                           @RequestParam Market market) {
        return service.findByMarketWithPrice(memberId, market);
    }

    /** POST /api/holdings：同市場同代號則更新股數，否則新增 */
    @PostMapping
    public StockHoldingResponse upsert(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
                                       @Valid @RequestBody StockHoldingRequest request) {
        return service.upsert(memberId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId, @PathVariable Long id) {
        service.delete(memberId, id);
    }
}
