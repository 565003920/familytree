# 族谱网站MVP完善设计文档

## 1. 概述

**目标**：完善familytree项目的缺失功能
**方案**：渐进式实现（方案A）
**预计工期**：3-4天

## 2. 需要补充的功能

### 2.1 家族关系管理
- 创建独立的family_relations表
- 实现关系CRUD API
- 支持多种关系类型（父子、配偶、兄弟等）

### 2.2 Pinia状态管理
- 用户认证状态（token、用户信息）
- 当前家族信息（选中家族、成员缓存）
- 全局UI状态（加载、错误提示、主题）

### 2.3 测试模块
- Service层单元测试
- Controller层集成测试

## 3. 技术设计

### 3.1 数据库设计

#### family_relations表结构
```sql
CREATE TABLE family_relations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_id BIGINT NOT NULL COMMENT '家族ID',
  member_id BIGINT NOT NULL COMMENT '成员ID',
  related_member_id BIGINT NOT NULL COMMENT '关联成员ID',
  relation_type VARCHAR(20) NOT NULL COMMENT '关系类型：father/mother/1./child/sibling',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_member (member_id),
  INDEX idx_related (related_member_id),
  INDEX idx_family (family_id),
  UNIQUE KEY uk_relation (member_id, related_member_id, relation_type)
) COMMENT='家族关系表';
```

### 3.2 后端设计

#### 实体类
- FamilyRelation.java

#### Repository层
- FamilyRelationRepository.java

#### Service层
- FamilyRelationService.java
  - createRelation()
  - getRelationsByMember()
  - deleteRelation()
  - getTreeData() - 构建树形结构数据

#### Controller层
- FamilyRelationController.java
  - POST /api/relations
  - GET /api/relations/member/{memberId}
  - DELETE /api/relations/{id}
  - GET /api/families/{familyId}/tree-data

### 3.3 前端设计

#### Pinia Store结构

**stores/auth.js** - 用户认证
```javascript
{
  state: { token, user, isAuthenticated },
  actions: { login(), logout(), refreshToken() }
}
```

**stores/family.js** - 家族信息
```javascript
{
  state: { currentFamily, members, relations },
  actions: { setCurrentFamily(), loadMembers(), loadRelations() }
}
```

**stores/ui.js** - 全局UI
```javascript
{
  state: { loading, error, theme },
  actions: { showLoading(), hideLoading(), setError(), clearError() }
}
```

#### API封装
- api/relations.js - 关系管理API调用
- api/interceptors.js - 请求拦截器（自动添加token）

### 3.4 测试设计

#### Service层单元测试
- FamilyRelationServiceTest.java
- 使用Mockito mock Repository层
- 测试业务逻辑正确性

#### Controller层集成测试
- FamilyRelationControllerTest.java
- 使用MockMvc测试HTTP请求
- 使用H2内存数据库
- 测试完整请求响应流程

## 4. 实施计划

### 阶段1：家族关系管理（第1天）

**后端任务**：
1. 更新init.sql，添加family_relations表
2. 创建FamilyRelation实体类
3. 创建FamilyRelationRepository
4. 实现FamilyRelationService业务逻辑
5. 创建FamilyRelationController API端点

**前端任务**：
1. 创建api/relations.js
2. 更新FamilyTree.vue，调用新的tree-data API

**验收标准**：
- 可以创建、查询、删除关系
- 家族树页面能正确展示关系数据

### 阶段2：Pinia状态管理（第2天）

**任务**：
1. 创建stores/auth.js
2. 创建stores/family.js
3. 创建stores/ui.js
4. 创建api/interceptors.js
5. 更新main.js注册Pinia
6. 重构Login.vue使用auth store
7. 重构FamilyList.vue使用family store
8. 添加全局loading和error提示

**验收标准**：
- 登录后token自动保存和使用
- 页面切换时状态正确保持
- 加载和错误提示正常显示

### 阶段3：测试模块（第3-4天）

**任务**：
1. 添加测试依赖到pom.xml
2. 创建Service层单元测试
3. 创建Controller层集成测试
4. 配置H2测试数据库
5. 运行测试并修复问题

**验收标准**：
- 所有测试通过
- 测试覆盖核心业务逻辑
- 测试可重复运行

## 5. 技术依赖

### 后端新增依赖
```xml
<!-- 测试依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### 前端已有依赖
- pinia: ^2.1.0 ✓
- axios: ^1.6.0 ✓
- d3: ^7.9.0 ✓

## 6. 风险与注意事项

### 技术风险
1. **关系表设计**：需要防止循环引用和重复关系
2. **状态同步**：Pinia状态需要与后端数据保持一致
3. **测试环境**：H2数据库与MySQL可能有语法差异

### 缓解措施
1. 在数据库层添加唯一约束防止重复
2. 实现状态刷新机制
3. 使用H2的MySQL兼容模式

## 7. 验收清单

- [ ] family_relations表创建成功
- [ ] 关系CRUD API正常工作
- [ ] 家族树页面展示关系数据
- [ ] Pinia stores创建并正常工作
- [ ] 登录状态持久化
- [ ] Service层测试通过
- [ ] Controller层测试通过
- [ ] 所有测试可重复运行
