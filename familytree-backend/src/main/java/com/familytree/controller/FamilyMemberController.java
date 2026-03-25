package com.familytree.controller;

import com.familytree.entity.FamilyMember;
import com.familytree.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class FamilyMemberController {
    private final FamilyMemberService memberService;

    @PostMapping
    public FamilyMember create(@RequestBody FamilyMember member) {
        return memberService.create(member);
    }

    @GetMapping
    public List<FamilyMember> getByFamily(@RequestParam Long familyId) {
        return memberService.getByFamilyId(familyId);
    }

    @GetMapping("/{id}")
    public FamilyMember getById(@PathVariable Long id) {
        return memberService.getById(id);
    }

    @PutMapping("/{id}")
    public FamilyMember update(@PathVariable Long id, @RequestBody FamilyMember member, @RequestHeader("X-User-Id") Long userId) {
        return memberService.update(id, member, userId);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        memberService.delete(id, userId);
        return Collections.singletonMap("message", "删除成功");
    }
}
