package kr.pe.advenoh.admin.folder.domain;

import kr.pe.advenoh.admin.folder.domain.dto.FolderDto;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderDto.FolderResponse> findAllByUsername(String username);
}
