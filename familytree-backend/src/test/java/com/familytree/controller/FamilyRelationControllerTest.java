package com.familytree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.familytree.entity.FamilyRelation;
import com.familytree.repository.FamilyRelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FamilyRelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FamilyRelationRepository relationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        relationRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void createRelation_returnsCreatedRelation() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("familyId", 10);
        request.put("memberId", 1);
        request.put("relatedMemberId", 2);
        request.put("relationType", "father");

        mockMvc.perform(post("/api/relations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.familyId").value(10))
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.relationType").value("father"));
    }

    @Test
    @WithMockUser
    void getRelationsByMember_returnsRelationList() throws Exception {
        FamilyRelation relation = new FamilyRelation();
        relation.setFamilyId(10L);
        relation.setMemberId(1L);
        relation.setRelatedMemberId(2L);
        relation.setRelationType("father");
        relationRepository.save(relation);

        mockMvc.perform(get("/api/relations/member/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value(1));
    }

    @Test
    @WithMockUser
    void deleteRelation_returnsOk() throws Exception {
        FamilyRelation relation = new FamilyRelation();
        relation.setFamilyId(10L);
        relation.setMemberId(1L);
        relation.setRelatedMemberId(2L);
        relation.setRelationType("spouse");
        FamilyRelation saved = relationRepository.save(relation);

        mockMvc.perform(delete("/api/relations/" + saved.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getTreeData_returnsTreeDataForFamily() throws Exception {
        FamilyRelation relation = new FamilyRelation();
        relation.setFamilyId(10L);
        relation.setMemberId(1L);
        relation.setRelatedMemberId(2L);
        relation.setRelationType("child");
        relationRepository.save(relation);

        mockMvc.perform(get("/api/families/10/tree-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relations").isArray());
    }
}
