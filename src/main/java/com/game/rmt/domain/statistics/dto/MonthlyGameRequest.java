package com.game.rmt.domain.statistics.dto;

import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.parent.CreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.game.rmt.global.errorhandler.exception.ErrorCode.BAD_REQUEST_GET_STATISTICS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyGameRequest implements CreateRequest {

    private Long gameId;
    private LocalDate startDate;
    private LocalDate endDate;

    /*
     * startDate와 endDate는 null이여도 상관 없음
     * startDate와 endDate 둘 다 null이면 현재일 기준 최근 1년의 데이터
     * startDate만 값이 있을 경우 startDate 기준으로 1년 이후의 데이터까지 통계
     * endDate만 값이 있을 경우 endDate 기준으로 1년 이전의 데이터까지 통계
     * */
    @Override
    public void isValidParam() {
        if (isNotNull(gameId) && isValidDate()) {
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