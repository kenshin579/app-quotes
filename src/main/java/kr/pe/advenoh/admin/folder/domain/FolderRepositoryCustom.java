package kr.pe.advenoh.admin.folder.domain;

import kr.pe.advenoh.admin.folder.domain.dto.FolderResponseDto;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderResponseDto> findAllByUsername(String username);
}
