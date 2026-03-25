package com.familytree.repository;

import com.familytree.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByFamilyId(Long familyId);
}
