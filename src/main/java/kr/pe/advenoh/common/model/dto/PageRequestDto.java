package kr.pe.advenoh.common.model.dto;

import kr.pe.advenoh.common.constants.AppConstants;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public final class PageRequestDto {
    private int pageIndex = AppConstants.DEFAULT_PAGE_INDEX;
    private int pageSize = AppConstants.DEFAULT_PAGE_SIZE;
    private Sort.Direction direction = Sort.Direction.ASC;

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex <= 0 ? 1 : pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > AppConstants.MAX_PAGE_SIZE ? AppConstants.DEFAULT_PAGE_SIZE : pageSize;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize, direction, "createDt");
    }
}
