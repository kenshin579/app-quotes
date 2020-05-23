package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderResponseDto> findAllByUsername(String username);
}
