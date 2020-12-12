package kr.pe.advenoh.admin.quote.domain.dto;

import kr.pe.advenoh.admin.quote.domain.enums.YN;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor //todo : @NoArgsConstructor(access = AccessLevel.PROTECTED) 변경하고 builder로 교체하기
@ToString
public class QuoteRequestDto {
    @NotNull
    private String quoteText;

    private String authorName;

    @NotNull
    private YN useYn;
    private Long folderId;
    private List<String> tags; //todo : client에서 중복 값은 체크가 필요함

    @Builder
    public QuoteRequestDto(String quoteText, String authorName, YN useYn, Long folderId, List<String> tags) {
        this.quoteText = quoteText;
        this.authorName = authorName;
        this.useYn = useYn;
        this.folderId = folderId;
        this.tags = tags;
    }
}