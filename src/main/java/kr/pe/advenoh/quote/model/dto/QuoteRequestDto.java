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
public class QuoteRequestDto {
    private String quoteText;
    private String authorName;
    private YN useYn;

    private Long folderId;
    private List<String> tags; //todo : client에서 중복 값은 체크가 필요함

    @Builder
    public QuoteRequestDto(String quoteText, String authorName, YN useYn, List<String> tags) {
        this.quoteText = quoteText;
        this.authorName = authorName;
        this.useYn = useYn;
        this.tags = tags;
    }
}