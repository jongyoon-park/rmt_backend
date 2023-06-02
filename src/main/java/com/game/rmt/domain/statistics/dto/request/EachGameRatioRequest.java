package com.game.rmt.domain.statistics.dto.request;

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
public class EachGameRatioRequest implements CreateRequest {
    private List<Long> platformIds;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void isValidParam() {
        if (isValidDate()) {
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
