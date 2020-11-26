package kr.pe.advenoh.admin.folder.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FolderResponseDto {
    private Long folderId;
    private String folderName;
    private Long numOfQuotes;
}