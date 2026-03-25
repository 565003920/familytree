package com.familytree.service;

import com.familytree.entity.FamilyRelation;
import com.familytree.repository.FamilyRelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FamilyRelationServiceTest {

    @Mock
    private FamilyRelationRepository relationRepository;

    @InjectMocks
    private FamilyRelationService relationService;

    private FamilyRelation sampleRelation;

    @BeforeEach
    void setUp() {
        sampleRelation = new FamilyRelation();
        sampleRelation.setId(1L);
        sampleRelation.setFamilyId(10L);
        sampleRelation.setMemberId(1L);
        sampleRelation.setRelatedMemberId(2L);
        sampleRelation.setRelationType("father");
        sampleRelation.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createRelation_savesAndReturnsRelation() {
        when(relationRepository.save(any(FamilyRelation.class))).thenReturn(sampleRelation);

        FamilyRelation result = relationService.createRelation(10L, 1L, 2L, "father");

        assertNotNull(result);
        assertEquals(10L, result.getFamilyId());
        assertEquals(1L, result.getMemberId());
        assertEquals(2L, result.getRelatedMemberId());
        assertEquals("father", result.getRelationType());
        verify(relationRepository, times(1)).save(any(FamilyRelation.class));
    }

    @Test
    void getRelationsByMember_returnsRelationList() {
        when(relationRepository.findByMemberId(1L)).thenReturn(Arrays.asList(sampleRelation));

        List<FamilyRelation> result = relationService.getRelationsByMember(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMemberId());
        verify(relationRepository, times(1)).findByMemberId(1L);
    }

    @Test
    void deleteRelation_callsRepositoryDeleteById() {
        doNothing().when(relationRepository).deleteById(1L);

        relationService.deleteRelation(1L);

        verify(relationRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTreeData_returnsMapWithRelations() {
        when(relationRepository.findByFamilyId(10L)).thenReturn(Arrays.asList(sampleRelation));

        Map<String, Object> result = relationService.getTreeData(10L);

        assertNotNull(result);
        assertTrue(result.containsKey("relations"));
        List<?> relations = (List<?>) result.get("relations");
        assertEquals(1, relations.size());
    }
}
