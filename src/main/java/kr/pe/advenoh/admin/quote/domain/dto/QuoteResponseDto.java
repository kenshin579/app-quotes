package kr.pe.advenoh.admin.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.pe.advenoh.admin.quote.domain.enums.YN;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuoteResponseDto {
    private Long quoteId;
    private String quoteText;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String authorName;
    private YN useYn;

    private List<String> tags;

    //todo : 좋아요의 수도 포함해야 하지 않을까?

    @Builder
    public QuoteResponseDto(Long quoteId, String quoteText, String authorName, YN useYn, List<String> tags) {
        this.quoteId = quoteId;
        this.quoteText = quoteText;
        this.authorName = authorName;
        this.useYn = useYn;
        this.tags = tags;
    }
}
