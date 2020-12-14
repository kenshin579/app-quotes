package kr.pe.advenoh.admin.folder.domain;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderDto.FolderResponse> findAllByUsername(String username);
}
