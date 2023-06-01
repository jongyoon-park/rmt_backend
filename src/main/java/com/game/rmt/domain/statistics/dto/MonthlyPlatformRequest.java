package com.game.rmt.domain.statistics.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.game.rmt.global.errorhandler.exception.ErrorCode.BAD_REQUEST_GET_STATISTICS;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyPlatformRequest implements CreateRequest {

    private Long platformId;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void isValidParam() {
        if (isNotNull(platformId) && isValidDate()) {
            return;
        }

        throw new BadRequestException(BAD_REQUEST_GET_STATISTICS);
    }

    public boolean isValidRangeDate() {
        return isNotNullRangeDate() && isBeforeDate();
    }

    public boolean isOnlyStartDate() {
        return isNotNull(startDate) && !isNotNull(endDate);
    }

    public boolean isOnlyEndDate() {
        return !isNotNull(startDate) && isNotNull(endDate);
    }

    public RangeDate getRangeDateCondition() {
        if (isValidRangeDate()) {
            return RangeDate.RANGE_DATE;
        }

        if (isOnlyStartDate()) {
            return RangeDate.ONLY_START_DATE;
        }

        if (isOnlyEndDate()) {
            return RangeDate.ONLY_END_DATE;
        }

        return RangeDate.NONE;
    }

    private boolean isValidDate() {
        return !isNotNullRangeDate() || isBeforeDate();
    }

    private boolean isNotNullRangeDate() {
        return isNotNull(startDate) && isNotNull(endDate);
    }

    private boolean isBeforeDate() {
        return startDate.isBefore(endDate);
    }
}
