package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Folder;

import java.util.List;

public interface FolderRepositoryCustom {
    List<Folder> findAllByUsername(String username);
}
