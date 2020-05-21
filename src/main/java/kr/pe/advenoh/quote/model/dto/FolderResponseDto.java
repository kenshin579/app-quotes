package kr.pe.advenoh.quote.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FolderResponseDto {
    private Long folderId;
    private String folderName;

    //todo : 폴더별로 몇개의 통계가 있는 지 넣기
    @Builder
    public FolderResponseDto(Long folderId, String folderName) {
        this.folderId = folderId;
        this.folderName = folderName;
    }
}