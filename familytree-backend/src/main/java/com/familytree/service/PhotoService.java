package com.familytree.service;

import com.familytree.entity.Album;
import com.familytree.entity.Family;
import com.familytree.entity.Photo;
import com.familytree.repository.AlbumRepository;
import com.familytree.repository.FamilyRepository;
import com.familytree.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final FamilyRepository familyRepository;

    public Photo create(Photo photo) {
        photo.setCreatedAt(LocalDateTime.now());
        return photoRepository.save(photo);
    }

    public List<Photo> getByAlbumId(Long albumId) {
        return photoRepository.findByAlbumId(albumId);
    }

    public Photo getById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
    }

    public Photo update(Long id, Photo data, Long userId) {
        Photo photo = getById(id);
        checkAdmin(photo.getAlbumId(), userId);
        photo.setDescription(data.getDescription());
        return photoRepository.save(photo);
    }

    public void delete(Long id, Long userId) {
        Photo photo = getById(id);
        checkAdmin(photo.getAlbumId(), userId);
        photo.setDeleted(1);
        photoRepository.save(photo);
    }

    private void checkAdmin(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
        Family family = familyRepository.findById(album.getFamilyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
        if (!family.getAdminId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权限操作");
        }
    }
}
