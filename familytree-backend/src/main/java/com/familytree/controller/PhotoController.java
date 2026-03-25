package com.familytree.controller;

import com.familytree.entity.Photo;
import com.familytree.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/album/{albumId}")
    public List<Photo> getByAlbum(@PathVariable Long albumId) {
        return photoService.getByAlbumId(albumId);
    }

    @PostMapping("/upload")
    public Photo upload(@RequestParam("file") MultipartFile file, @RequestParam Long albumId) throws Exception {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File uploadDir = new File(System.getProperty("user.dir") + "/uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File dest = new File(uploadDir.getAbsolutePath() + "/" + filename);
        file.transferTo(dest.getAbsoluteFile());

        Photo photo = new Photo();
        photo.setAlbumId(albumId);
        photo.setUrl("/uploads/" + filename);
        photo.setCreatedAt(LocalDateTime.now());
        return photoService.create(photo);
    }

    @PutMapping("/{id}")
    public Photo update(@PathVariable Long id, @RequestBody Photo photo, @RequestHeader("X-User-Id") Long userId) {
        return photoService.update(id, photo, userId);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        photoService.delete(id, userId);
        return Collections.singletonMap("message", "删除成功");
    }
}
