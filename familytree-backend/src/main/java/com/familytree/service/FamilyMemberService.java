package com.familytree.service;

import com.familytree.entity.Family;
import com.familytree.entity.FamilyMember;
import com.familytree.repository.FamilyMemberRepository;
import com.familytree.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyMemberService {
    private final FamilyMemberRepository memberRepository;
    private final FamilyRepository familyRepository;

    public FamilyMember create(FamilyMember member) {
        member.setCreatedAt(LocalDateTime.now());
        return memberRepository.save(member);
    }

    public List<FamilyMember> getByFamilyId(Long familyId) {
        return memberRepository.findByFamilyId(familyId);
    }

    public FamilyMember getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
    }

    public FamilyMember update(Long id, FamilyMember data, Long userId) {
        FamilyMember member = getById(id);
        checkAdmin(member.getFamilyId(), userId);
        member.setName(data.getName());
        member.setGender(data.getGender());
        member.setBirthDate(data.getBirthDate());
        member.setDeathDate(data.getDeathDate());
        member.setGeneration(data.getGeneration());
        member.setRanking(data.getRanking());
        member.setBio(data.getBio());
        member.setFatherId(data.getFatherId());
        member.setMotherId(data.getMotherId());
        member.setSpouseId(data.getSpouseId());
        return memberRepository.save(member);
    }

    public void delete(Long id, Long userId) {
        FamilyMember member = getById(id);
        checkAdmin(member.getFamilyId(), userId);
        member.setDeleted(1);
        memberRepository.save(member);
    }

    private void checkAdmin(Long familyId, Long userId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "资源不存在"));
        if (!family.getAdminId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权限操作");
        }
    }
}
