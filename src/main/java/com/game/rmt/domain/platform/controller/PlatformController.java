package com.game.rmt.domain.platform.controller;

import com.game.rmt.domain.platform.dto.response.PlatformListResponse;
import com.game.rmt.domain.platform.service.PlatformService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/platform")
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping("/list")
    public PlatformListResponse getPlatforms() {
        return new PlatformListResponse(platformService.getPlatforms());
    }
}
