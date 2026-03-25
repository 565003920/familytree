package com.familytree.service;

import com.familytree.entity.Album;
import com.familytree.entity.Family;
import com.familytree.repository.AlbumRepository;
import com.familytree.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final FamilyRepository familyRepository;

    public Album create(Album album) {
        album.setCreatedAt(LocalDateTime.now());
        return albumRepository.save(album);
    }

    public List<Album> getByFamilyId(Long familyId) {
        return albumRepository.findByFamilyId(familyId);
    }

    public Album getById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
    }

    public Album update(Long id, Album data, Long userId) {
        Album album = getById(id);
        checkAdmin(album.getFamilyId(), userId);
        album.setName(data.getName());
        album.setDescription(data.getDescription());
        return albumRepository.save(album);
    }

    public void delete(Long id, Long userId) {
        Album album = getById(id);
        checkAdmin(album.getFamilyId(), userId);
        album.setDeleted(1);
        albumRepository.save(album);
    }

    private void checkAdmin(Long familyId, Long userId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
        if (!family.getAdminId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权限操作");
        }
    }
}
