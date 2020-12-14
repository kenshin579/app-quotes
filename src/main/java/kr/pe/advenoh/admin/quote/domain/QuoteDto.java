package kr.pe.advenoh.admin.quote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.pe.advenoh.admin.quote.domain.Author;
import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.admin.quote.domain.enums.YN;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

public class QuoteDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class QuoteRequest {
        @NotNull
        private String quoteText;

        private String authorName;

        @NotNull
        private YN useYn;
        private List<String> tags; //todo : client에서 중복 값은 체크가 필요함

        @Builder
        public QuoteRequest(String quoteText, String authorName, YN useYn, List<String> tags) {
            this.quoteText = quoteText;
            this.authorName = authorName;
            this.useYn = useYn;
            this.tags = tags;
        }

        public Quote toEntity() {
            return Quote.builder()
                    .quoteText(this.quoteText)
                    .author(Author.builder().name(authorName).build())
                    .useYn(useYn)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class QuoteResponse {
        private Long quoteId;
        private String quoteText;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String authorName;
        private YN useYn;

        private List<String> tags;

        //todo : 좋아요의 수도 포함해야 하지 않을까?

        //todo : 이거 제거하는 방법을 찾아보기
        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        @Builder
        public QuoteResponse(Long quoteId, String quoteText, String authorName, YN useYn, List<String> tags) {
            this.quoteId = quoteId;
            this.quoteText = quoteText;
            this.authorName = authorName;
            this.useYn = useYn;
            this.tags = tags;
        }
    }
}
