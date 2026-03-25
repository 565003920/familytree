# 族谱网站MVP完善实施计划

## 计划概述

**基于规范**：familytree-completion.md
**实施方式**：渐进式，分3个阶段
**预计工期**：3-4天

## 阶段1：家族关系管理（1天）

### 步骤1.1：更新数据库schema
**文件**：familytree-backend/src/main/resources/init.sql
**操作**：在文件末尾添加family_relations表定义

### 步骤1.2：创建FamilyRelation实体
**文件**：familytree-backend/src/main/java/com/familytree/entity/FamilyRelation.java
**内容**：
- 字段：id, familyId, memberId, relatedMemberId, relationType, createdAt
- 使用@Entity和Lombok注解

### 步骤1.3：创建Repository接口
**文件**：familytree-backend/src/main/java/com/familytree/repository/FamilyRelationRepository.java
**方法**：
- findByMemberId(Long memberId)
- findByFamilyId(Long familyId)

### 步骤1.4：创建Service层
**文件**：familytree-backend/src/main/java/com/familytree/service/FamilyRelationService.java
**方法**：
- createRelation()
- getRelationsByMember()
- deleteRelation()
- getTreeData() - 构建树形数据结构

### 步骤1.5：创建Controller层
**文件**：familytree-backend/src/main/java/com/familytree/controller/FamilyRelationController.java
**端点**：
- POST /api/relations - 创建关系
- GET /api/relations/member/{memberId} - 查询成员关系
- DELETE /api/relations/{id} - 删除关系
- GET /api/families/{familyId}/tree-data - 获取树形数据

### 步骤1.6：创建前端API封装
**文件**：familytree-frontend/src/api/relations.js
**方法**：createRelation, getRelations, deleteRelation, getTreeData

### 步骤1.7：更新家族树页面
**文件**：familytree-frontend/src/views/FamilyTree.vue
**操作**：调用getTreeData API，使用D3.js渲染

**验收**：可以创建、查询、删除关系，家族树正确展示

## 阶段2：Pinia状态管理（1天）

### 步骤2.1：创建auth store
**文件**：familytree-frontend/src/stores/auth.js
**状态**：token, user, isAuthenticated
**操作**：login, logout, refreshToken

### 步骤2.2：创建family store
**文件**：familytree-frontend/src/stores/family.js
**状态**：currentFamily, members, relations
**操作**：setCurrentFamily, loadMembers, loadRelations

### 步骤2.3：创建ui store
**文件**：familytree-frontend/src/stores/ui.js
**状态**：loading, error, theme
**操作**：showLoading, hideLoading, setError, clearError

### 步骤2.4：创建axios拦截器
**文件**：familytree-frontend/src/api/interceptors.js
**功能**：请求自动添加token，响应错误统一处理

### 步骤2.5：注册Pinia
**文件**：familytree-frontend/src/main.js
**操作**：导入createPinia并注册

### 步骤2.6：重构Login.vue
**操作**：使用auth store替代本地状态

### 步骤2.7：重构FamilyList.vue
**操作**：使用family store管理家族数据

**验收**：登录状态持久化，页面切换状态保持，loading/error提示正常

## 阶段3：测试模块（1-2天）

### 步骤3.1：添加测试依赖
**文件**：familytree-backend/pom.xml
**依赖**：spring-boot-starter-test, h2database

### 步骤3.2：配置测试环境
**文件**：familytree-backend/src/test/resources/application-test.yml
**配置**：H2数据库连接，MySQL兼容模式

### 步骤3.3：创建Service单元测试
**文件**：familytree-backend/src/test/java/com/familytree/service/FamilyRelationServiceTest.java
**测试**：createRelation, getRelationsByMember, deleteRelation
**工具**：Mockito mock Repository

### 步骤3.4：创建Controller集成测试
**文件**：familytree-backend/src/test/java/com/familytree/controller/FamilyRelationControllerTest.java
**测试**：POST/GET/DELETE端点
**工具**：MockMvc, H2数据库

### 步骤3.5：运行测试并修复
**命令**：mvn test
**操作**：修复失败的测试用例

**验收**：所有测试通过，可重复运行

## 关键文件清单

**后端新增**：
- entity/FamilyRelation.java
- repository/FamilyRelationRepository.java
- service/FamilyRelationService.java
- controller/FamilyRelationController.java
- test/service/FamilyRelationServiceTest.java
- test/controller/FamilyRelationControllerTest.java

**前端新增**：
- stores/auth.js
- stores/family.js
- stores/ui.js
- api/relations.js
- api/interceptors.js

**修改文件**：
- init.sql
- pom.xml
- main.js
- Login.vue
- FamilyList.vue
- FamilyTree.vue
