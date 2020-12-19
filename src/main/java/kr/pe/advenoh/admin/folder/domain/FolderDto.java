package kr.pe.advenoh.admin.folder.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class FolderDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class FolderResponse {
        private Long folderId;
        private String folderName;
        private Long numOfQuotes;

        @Builder
        public FolderResponse(Long folderId, String folderName, Long numOfQuotes) {
            this.folderId = folderId;
            this.folderName = folderName;
            this.numOfQuotes = numOfQuotes;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FolderStatsResponse {
        private Long totalNumOfQuotes;
        private Long totalNumOfLikes;

        @Builder
        public FolderStatsResponse(Long totalNumOfQuotes, Long totalNumOfLikes) {
            this.totalNumOfQuotes = totalNumOfQuotes;
            this.totalNumOfLikes = totalNumOfLikes;
        }
    }
}
