package kr.pe.advenoh.quote.model.dto;

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