package kr.pe.advenoh.quote.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FolderStatsResponseDto {
    private Long totalNumOfQuotes;
    private Long totalNumOfLikes;

    @Builder
    public FolderStatsResponseDto(Long totalNumOfQuotes, Long totalNumOfLikes) {
        this.totalNumOfQuotes = totalNumOfQuotes;
        this.totalNumOfLikes = totalNumOfLikes;
    }
}