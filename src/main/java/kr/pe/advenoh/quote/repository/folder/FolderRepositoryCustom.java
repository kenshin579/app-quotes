package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderResponseDto> findAllByUsername(String username);
}
