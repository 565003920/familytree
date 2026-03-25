package com.familytree.service;

import com.familytree.entity.Family;
import com.familytree.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;

    public Family create(Family family) {
        family.setMemberCount(0);
        family.setStatus(1);
        family.setCreatedAt(LocalDateTime.now());
        return familyRepository.save(family);
    }

    public List<Family> getByAdminId(Long adminId) {
        return familyRepository.findByAdminId(adminId);
    }

    public Family getById(Long id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
    }

    public Family update(Long id, Family data, Long userId) {
        Family family = getById(id);
        checkAdmin(family, userId);
        family.setName(data.getName());
        family.setSurname(data.getSurname());
        family.setOriginPlace(data.getOriginPlace());
        family.setDescription(data.getDescription());
        family.setUpdatedAt(LocalDateTime.now());
        return familyRepository.save(family);
    }

    public void delete(Long id, Long userId) {
        Family family = getById(id);
        checkAdmin(family, userId);
        family.setDeleted(1);
        familyRepository.save(family);
    }

    private void checkAdmin(Family family, Long userId) {
        if (!family.getAdminId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权限操作");
        }
    }
}
