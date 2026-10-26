package com.assetsystem.controller;

import com.assetsystem.config.AuthInterceptor;
import com.assetsystem.dto.SummaryResponse;
import com.assetsystem.service.SummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 首頁總資產看板 */
@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService service;

    public SummaryController(SummaryService service) {
        this.service = service;
    }

    @GetMapping
    public SummaryResponse get(@RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId) {
        return service.getSummary(memberId);
    }
}
