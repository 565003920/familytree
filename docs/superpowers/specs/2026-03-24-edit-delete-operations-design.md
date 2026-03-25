# 家族树系统 — 编辑与删除操作设计文档

**日期：** 2026-03-24
**状态：** 已批准

## 概述

为家族（Family）、成员（FamilyMember）、相册（Album）、图片（Photo）四种核心实体补充编辑（Update）和删除（Delete）操作。当前系统仅实现了创建（Create）和读取（Read）功能。

## 设计决策

| 决策项 | 选择 | 理由 |
|--------|------|------|
| 删除策略 | 二次确认弹窗 + 软删除 | 数据安全，可恢复 |
| 级联删除 | 不级联，仅删当前实体 | 简化实现，避免误删子数据 |
| 权限控制 | 仅家族管理员（adminId）可操作 | 前后端均校验 |
| 编辑范围 | 全字段可编辑 | 满足完整编辑需求 |
| UI 形式 | 内联弹窗（el-dialog） | 与现有创建操作风格一致 |
| `deleted` 与 `status` 关系 | `deleted` 为独立的软删除标志，`status` 保留原含义不变 | `status` 表示业务状态，`deleted` 表示逻辑删除，两者职责不同 |

## 后端 API 设计

### 数据库变更

对四张表各新增 `deleted` 字段：

```sql
ALTER TABLE families       ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE family_members ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE albums         ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE photos         ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
```

`deleted` 字段与 Family 已有的 `status` 字段无关：`status` 表示业务状态（如启用/禁用），`deleted` 仅用于软删除标记。软删除时仅设 `deleted=1`，不修改 `status`。

### 新增 API 端点

| 实体 | 编辑 | 删除 |
|------|------|------|
| Family | `PUT /api/families/{id}` | `DELETE /api/families/{id}` |
| FamilyMember | `PUT /api/members/{id}` | `DELETE /api/members/{id}` |
| Album | `PUT /api/albums/{id}` | `DELETE /api/albums/{id}` |
| Photo | `PUT /api/photos/{id}` | `DELETE /api/photos/{id}` |

### PUT 请求/响应格式

所有 PUT 端点采用**全量替换**模式（非 PATCH），请求体为完整 JSON 对象，响应体返回更新后的完整实体。

**PUT /api/families/{id}**
- 请求体：`{ "name": "string", "surname": "string", "originPlace": "string", "description": "string" }`
- 响应体：更新后的 Family 完整 JSON，HTTP 200

**PUT /api/members/{id}**
- 请求体：`{ "name": "string", "gender": "string", "birthDate": "string", "deathDate": "string", "generation": int, "ranking": int, "bio": "string", "fatherId": long|null, "motherId": long|null, "spouseId": long|null }`
- 响应体：更新后的 FamilyMember 完整 JSON，HTTP 200

**PUT /api/albums/{id}**
- 请求体：`{ "name": "string", "description": "string" }`
- 响应体：更新后的 Album 完整 JSON，HTTP 200

**PUT /api/photos/{id}**
- 请求体：`{ "description": "string" }`
- 响应体：更新后的 Photo 完整 JSON，HTTP 200

### DELETE 响应格式

所有 DELETE 端点执行软删除后返回 HTTP 200，响应体：`{ "message": "删除成功" }`。

### 错误响应

| 场景 | HTTP 状态码 | 响应体示例 |
|------|------------|-----------|
| 实体不存在 | 404 Not Found | `{ "message": "资源不存在" }` |
| 实体已被软删除 | 404 Not Found | `{ "message": "资源不存在" }` |
| 无权限（非管理员） | 403 Forbidden | `{ "message": "无权限操作" }` |
| 请求参数校验失败 | 400 Bad Request | `{ "message": "参数错误: name不能为空" }` |

### 权限校验

每个 PUT/DELETE 端点在 Service 层校验：当前登录用户（从 JWT 解析）的 userId 必须等于该实体所属 Family 的 `adminId`，否则返回 `403 Forbidden`。

**权限查找路径：**
- **Family**：直接查 `family.adminId`
- **FamilyMember**：通过 `member.familyId` 查到 Family，再查 `family.adminId`
- **Album**：通过 `album.familyId` 查到 Family，再查 `family.adminId`
- **Photo**：通过 `photo.albumId` 查到 Album，再通过 `album.familyId` 查到 Family，再查 `family.adminId`

### 软删除实现

- 所有删除操作执行 `UPDATE xxx SET deleted=1 WHERE id=?`
- 使用 JPA `@Where(clause = "deleted = 0")` 注解在 Entity 类级别全局过滤软删除记录，无需逐个修改查询方法签名
- 这样 `findById`、`findAll`、以及所有自定义查询方法均自动过滤已删除记录
- 如现有 JPA 版本不支持 `@Where`，则退回逐个修改查询方法签名的方式：
  - `List<Family> findByAdminIdAndDeleted(Long adminId, int deleted)`
  - `List<FamilyMember> findByFamilyIdAndDeleted(Long familyId, int deleted)`
  - `List<Album> findByFamilyIdAndDeleted(Long familyId, int deleted)`
  - `List<Photo> findByAlbumIdAndDeleted(Long albumId, int deleted)`
  - 同时 `findById` 调用后需在 Service 层检查 `deleted == 0`，若已删除则视为不存在返回 404

### 编辑操作可修改字段

| 实体 | 可编辑字段 |
|------|-----------|
| Family | name, surname, originPlace, description |
| FamilyMember | name, gender, birthDate, deathDate, generation, ranking, bio, fatherId, motherId, spouseId |
| Album | name, description |
| Photo | description |

## 前端 UI 设计

### API 服务层扩展

在 `src/api/family.js` 中新增：
- `familyApi.update(id, data)` — `PUT /api/families/{id}`
- `familyApi.delete(id)` — `DELETE /api/families/{id}`
- `memberApi.update(id, data)` — `PUT /api/members/{id}`
- `memberApi.delete(id)` — `DELETE /api/members/{id}`

在 `src/api/album.js` 中新增（photo 相关方法与 album 放在同一文件中，与现有 `photoApi.upload` 和 `photoApi.getList` 保持一致）：
- `albumApi.update(id, data)` — `PUT /api/albums/{id}`
- `albumApi.delete(id)` — `DELETE /api/albums/{id}`
- `photoApi.update(id, data)` — `PUT /api/photos/{id}`
- `photoApi.delete(id)` — `DELETE /api/photos/{id}`

### Pinia Store 层

本次不新增 Store 操作。原因：现有项目中创建操作也未经过 Store，而是由 Vue 组件直接调用 API 服务方法。编辑/删除操作遵循相同模式，保持一致性。

### 页面改动

#### FamilyList.vue（家族列表）
- 每个家族卡片新增「编辑」「删除」按钮，仅当 `currentUser.id === family.adminId` 时显示
- 编辑：复用已有 `el-dialog` 模式，预填充当前家族数据（name、surname、originPlace、description）
- 删除：`ElMessageBox.confirm()` 二次确认，确认后调用软删除接口，从列表移除

#### FamilyDetail.vue（成员管理）
- 每个成员条目新增「编辑」「删除」按钮
- 编辑：新增编辑 dialog，两列表单布局，预填充所有字段
- 删除：确认后软删除，刷新成员列表

#### AlbumList.vue（相册列表）
- 每个相册卡片新增「编辑」「删除」按钮
- 编辑：复用已有 dialog 模式，预填充 name、description
- 删除：确认后软删除

#### AlbumDetail.vue（图片列表）
- 每张图片新增「编辑描述」「删除」按钮（悬浮显示）
- 编辑：小型 dialog，仅编辑 description 字段
- 删除：确认后软删除

### 错误处理

前端统一在 axios 响应拦截器或调用处处理后端错误：
- 403：`ElMessage.error('无权限操作')`
- 404：`ElMessage.error('资源不存在')`
- 400：`ElMessage.error(response.data.message)`
- 其他：`ElMessage.error('操作失败，请重试')`

### 二次确认弹窗统一文案

```
确认删除？
此操作将删除「{实体名称}」，数据将被标记为已删除。
[取消]  [确认删除]
```

## 受影响文件清单

### 后端（Java）

| 层级 | 文件 | 改动 |
|------|------|------|
| Entity | Family.java, FamilyMember.java, Album.java, Photo.java | 新增 `deleted` 字段，添加 `@Where` 注解 |
| Repository | FamilyRepository.java, FamilyMemberRepository.java, AlbumRepository.java, PhotoRepository.java | 如不用 `@Where` 则更新查询方法签名 |
| Service | FamilyService.java, FamilyMemberService.java, AlbumService.java, PhotoService.java | 新增 update/delete 方法 + 权限校验（含实体归属家族查找链路） |
| Controller | FamilyController.java, FamilyMemberController.java, AlbumController.java, PhotoController.java | 新增 PUT/DELETE 端点 |

### 前端（Vue）

| 文件 | 改动 |
|------|------|
| src/api/family.js | 新增 familyApi.update/delete, memberApi.update/delete |
| src/api/album.js | 新增 albumApi.update/delete, photoApi.update/delete |
| src/views/FamilyList.vue | 新增编辑/删除按钮及 dialog |
| src/views/FamilyDetail.vue | 新增成员编辑/删除按钮及 dialog |
| src/views/AlbumList.vue | 新增相册编辑/删除按钮及 dialog |
| src/views/AlbumDetail.vue | 新增图片编辑/删除按钮及悬浮操作 |

## 不在本次范围内

- 已软删除数据的管理后台/恢复功能
- 孤儿数据的自动清理
- 图片文件本身的物理删除（仅标记数据库记录）
