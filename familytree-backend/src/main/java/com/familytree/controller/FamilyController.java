package com.familytree.controller;

import com.familytree.entity.Family;
import com.familytree.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyController {
    private final FamilyService familyService;

    @PostMapping
    public Family create(@RequestBody Family family) {
        return familyService.create(family);
    }

    @GetMapping
    public List<Family> getByAdmin(@RequestParam Long adminId) {
        return familyService.getByAdminId(adminId);
    }

    @GetMapping("/{id}")
    public Family getById(@PathVariable Long id) {
        return familyService.getById(id);
    }

    @PutMapping("/{id}")
    public Family update(@PathVariable Long id, @RequestBody Family family, @RequestHeader("X-User-Id") Long userId) {
        return familyService.update(id, family, userId);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        familyService.delete(id, userId);
        return Collections.singletonMap("message", "删除成功");
    }
}
