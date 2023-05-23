package com.game.rmt.domain.account.dto;

import com.game.rmt.global.parent.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountSearchFilter implements Filter {
    private final int DEFAULT_LIMIT = 20;
    private final int DEFAULT_PAGE = 0;

    private LocalDate searchStartDate;
    private LocalDate searchEndDate;
    private String searchProductName;
    private List<Long> searchGameIdList;
    private Integer searchMinPrice;
    private Integer searchMaxPrice;
    private List<Long> searchPlatformIdList;
    private Integer searchLimit;
    private Integer searchPage;

    public boolean isValidSearchDate() {
        return isNotNull(searchStartDate) || isNotNull(searchEndDate);
    }

    public boolean isValidSearchPrice() {
        return isNotNull(searchMinPrice) || isNotNull(searchMaxPrice);
    }

    public boolean isValidSearchPlatformIdList() {
        return isNotNull(searchPlatformIdList) || !searchPlatformIdList.isEmpty();
    }

    public boolean isValidProductName() {
        return isValidString(searchProductName);
    }

    public boolean isValidGameIdList() {
        return isNotNull(searchGameIdList) || !searchGameIdList.isEmpty();
    }

    public void validateLimit() {
        if (isValidLimit()) {
            return;
        }

        setDefaultLimit();
    }

    public void validatePage() {
        if (isValidPage()) {
            return;
        }

        setDefaultPage();
    }

    private boolean isValidPage() {
        return isNotNull(searchPage);
    }

    private void setDefaultPage() {
        this.searchPage = DEFAULT_PAGE;
    }

    private void setDefaultLimit() {
        this.searchLimit = DEFAULT_LIMIT;
    }

    private boolean isValidLimit() {
        return isNotNull(searchLimit);
    }
}
