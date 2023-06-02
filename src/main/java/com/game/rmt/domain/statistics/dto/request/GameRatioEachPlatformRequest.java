package com.game.rmt.domain.statistics.dto.request;

import com.game.rmt.domain.statistics.dto.RangeDate;
import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.game.rmt.global.errorhandler.exception.ErrorCode.BAD_REQUEST_GET_STATISTICS;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameRatioEachPlatformRequest implements CreateRequest {
    private List<Long> platformIds;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void isValidParam() {
        if (isValidPlatformIds() && isValidDate()) {
            return;
        }

        throw new BadRequestException(BAD_REQUEST_GET_STATISTICS);
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

    private boolean isValidPlatformIds() {
        return isNotNull(platformIds) && !platformIds.isEmpty();
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

    private boolean isValidRangeDate() {
        return isNotNullRangeDate() && isBeforeDate();
    }

    private boolean isOnlyStartDate() {
        return isNotNull(startDate) && !isNotNull(endDate);
    }

    private boolean isOnlyEndDate() {
        return !isNotNull(startDate) && isNotNull(endDate);
    }
}
