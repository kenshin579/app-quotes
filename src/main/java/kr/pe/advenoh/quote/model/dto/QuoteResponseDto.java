package kr.pe.advenoh.quote.model.dto;

import kr.pe.advenoh.quote.model.enums.YN;
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
    private String authorName;

    //    @JsonProperty("userInfo")
//    private UserDto user;
    private YN useYn;

    private List<String> tags;
    //todo : 좋아요의 수도 포함해야 하지 않을까?

    @Builder
    public QuoteResponseDto(Long quoteId, String quoteText, String authorName, YN useYn) {
        this.quoteId = quoteId;
        this.quoteText = quoteText;
        this.authorName = authorName;
        this.useYn = useYn;
    }
}
