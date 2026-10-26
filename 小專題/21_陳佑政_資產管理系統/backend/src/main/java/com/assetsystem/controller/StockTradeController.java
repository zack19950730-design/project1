package com.assetsystem.controller;

import com.assetsystem.config.AuthInterceptor;
import com.assetsystem.dto.StockTradeRequest;
import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockTrade;
import com.assetsystem.service.StockTradeService;
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

/** 股票交易紀錄 */
@RestController
@RequestMapping("/api/trades")
public class StockTradeController {

    private final StockTradeService service;

    public StockTradeController(StockTradeService service) {
        this.service = service;
    }

    /** GET /api/trades?market=TW */
    @GetMapping
    public List<StockTrade> list(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
                                 @RequestParam Market market) {
        return service.findByMarket(memberId, market);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockTrade create(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
                             @Valid @RequestBody StockTradeRequest request) {
        return service.create(memberId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId, @PathVariable Long id) {
        service.delete(memberId, id);
    }
}
