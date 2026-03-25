package com.familytree.controller;

import com.familytree.entity.Album;
import com.familytree.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/family/{familyId}")
    public List<Album> getByFamily(@PathVariable Long familyId) {
        return albumService.getByFamilyId(familyId);
    }

    @PostMapping
    public Album create(@RequestBody Album album) {
        return albumService.create(album);
    }

    @PutMapping("/{id}")
    public Album update(@PathVariable Long id, @RequestBody Album album, @RequestHeader("X-User-Id") Long userId) {
        return albumService.update(id, album, userId);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        albumService.delete(id, userId);
        return Collections.singletonMap("message", "删除成功");
    }
}
