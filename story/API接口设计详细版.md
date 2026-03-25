# 族谱网站 - 完整API接口设计

## 基础信息

**Base URL**: `https://api.familytree.com/v1`

**通用响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1234567890
}
```

**错误码**:
- 200: 成功
- 400: 请求参数错误
- 401: 未授权
- 403: 无权限
- 404: 资源不存在
- 500: 服务器错误

---

## 一、用户认证模块

### 1.1 用户注册
```
POST /auth/register
```

**请求参数**:
```json
{
  "username": "zhangsan",
  "phone": "13800138000",
  "password": "password123",
  "code": "123456"
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "username": "zhangsan",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```


### 1.2 用户登录
```
POST /auth/login
```

**请求参数**:
```json
{
  "phone": "13800138000",
  "password": "password123"
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "username": "zhangsan",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here"
  }
}
```

### 1.3 刷新Token
```
POST /auth/refresh-token
```

**请求参数**:
```json
{
  "refreshToken": "refresh_token_here"
}
```

### 1.4 用户登出
```
POST /auth/logout
```

**请求头**:
```
Authorization: Bearer {token}
```


## 二、家族管理模块

### 2.1 创建家族
```
POST /families
```

**请求参数**:
```json
{
  "name": "张氏家族",
  "surname": "张",
  "originPlace": "山东济南",
  "description": "家族简介"
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "familyId": 1,
    "name": "张氏家族",
    "adminId": 1
  }
}
```

### 2.2 获取家族列表
```
GET /families?page=1&size=10
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "name": "张氏家族",
        "memberCount": 100,
        "createdAt": "2026-01-01"
      }
    ]
  }
}
```


### 2.3 获取家族详情
```
GET /families/:id
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "张氏家族",
    "surname": "张",
    "originPlace": "山东济南",
    "memberCount": 100,
    "generationCount": 5
  }
}
```

### 2.4 更新家族信息
```
PUT /families/:id
```

**请求参数**:
```json
{
  "name": "张氏家族",
  "description": "更新后的简介"
}
```

### 2.5 删除家族
```
DELETE /families/:id
```


## 三、家族成员模块

### 3.1 添加成员
```
POST /families/:familyId/members
```

**请求参数**:
```json
{
  "name": "张三",
  "gender": "male",
  "birthDate": "1980-01-01",
  "generation": "第三代",
  "ranking": 1
}
```

### 3.2 获取成员列表
```
GET /families/:familyId/members?page=1&size=20
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "name": "张三",
        "gender": "male",
        "birthDate": "1980-01-01"
      }
    ]
  }
}
```


### 3.3 获取成员详情
```
GET /families/:familyId/members/:memberId
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "张三",
    "gender": "male",
    "birthDate": "1980-01-01",
    "education": "本科",
    "occupation": "工程师"
  }
}
```

### 3.4 更新成员信息
```
PUT /families/:familyId/members/:memberId
```

### 3.5 删除成员
```
DELETE /families/:familyId/members/:memberId
```

### 3.6 搜索成员
```
GET /families/:familyId/members/search?keyword=张三
```


## 四、家族树模块

### 4.1 获取家族树
```
GET /families/:familyId/tree?depth=5
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "nodes": [
      {
        "id": 1,
        "name": "张三",
        "generation": 1
      }
    ],
    "edges": [
      {
        "from": 1,
        "to": 2,
        "type": "parent-child"
      }
    ]
  }
}
```

### 4.2 添加关系
```
POST /families/:familyId/relations
```

**请求参数**:
```json
{
  "memberId": 1,
  "relatedMemberId": 2,
  "relationType": "parent"
}
```


### 4.3 删除关系
```
DELETE /families/:familyId/relations/:relationId
```

### 4.4 导出家族树
```
GET /families/:familyId/tree/export?format=pdf
```

## 五、相册模块

### 5.1 创建相册
```
POST /albums
```

**请求参数**:
```json
{
  "familyId": 1,
  "name": "家庭相册",
  "visibility": "family"
}
```

### 5.2 获取相册列表
```
GET /albums?familyId=1&page=1&size=10
```


### 5.3 上传照片
```
POST /albums/:albumId/photos
```

**请求参数** (multipart/form-data):
```
file: 图片文件
title: 照片标题
description: 照片描述
takenAt: 拍摄时间
```

### 5.4 获取照片列表
```
GET /albums/:albumId/photos?page=1&size=20
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "title": "全家福",
        "thumbnailUrl": "https://cdn.example.com/thumb.jpg"
      }
    ]
  }
}
```


### 5.5 删除照片
```
DELETE /photos/:photoId
```

### 5.6 照片修复
```
POST /photos/:photoId/restore
```

### 5.7 照片上色
```
POST /photos/:photoId/colorize
```

## 六、活动模块

### 6.1 创建活动
```
POST /activities
```

**请求参数**:
```json
{
  "familyId": 1,
  "title": "春节聚会",
  "activityType": "gathering",
  "startTime": "2026-02-01 10:00:00",
  "location": "北京市朝阳区"
}
```


### 6.2 获取活动列表
```
GET /activities?familyId=1&page=1&size=10
```

### 6.3 活动报名
```
POST /activities/:activityId/participants
```

**请求参数**:
```json
{
  "companions": 2,
  "note": "带家属参加"
}
```

### 6.4 取消报名
```
DELETE /activities/:activityId/participants/:userId
```

## 七、公告模块

### 7.1 发布公告
```
POST /announcements
```

**请求参数**:
```json
{
  "familyId": 1,
  "title": "重要通知",
  "content": "公告内容",
  "isPinned": false
}
```


### 7.2 获取公告列表
```
GET /announcements?familyId=1&page=1&size=10
```

## 八、通知模块

### 8.1 获取通知列表
```
GET /notifications?page=1&size=20
```

### 8.2 标记已读
```
PUT /notifications/:id/read
```

## 九、评论模块

### 9.1 添加评论
```
POST /comments
```

**请求参数**:
```json
{
  "targetType": "announcement",
  "targetId": 1,
  "content": "评论内容"
}
```


### 9.2 获取评论列表
```
GET /comments?targetType=announcement&targetId=1
```

## 十、议题模块

### 10.1 创建议题
```
POST /discussions
```

**请求参数**:
```json
{
  "familyId": 1,
  "title": "议题标题",
  "content": "议题内容",
  "discussionType": "vote"
}
```

### 10.2 投票
```
POST /discussions/:id/votes
```

**请求参数**:
```json
{
  "voteOption": "同意"
}
```


## 十一、订阅模块

### 11.1 获取套餐列表
```
GET /subscription-plans
```

### 11.2 创建订单
```
POST /payment-orders
```

**请求参数**:
```json
{
  "planId": 1,
  "paymentMethod": "alipay"
}
```

### 11.3 查询订单状态
```
GET /payment-orders/:orderNo
```

## 十二、统计模块

### 12.1 家族统计
```
GET /families/:familyId/statistics
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "memberCount": 100,
    "maleCount": 55,
    "femaleCount": 45,
    "avgAge": 45
  }
}
```


### 12.2 地域分布
```
GET /families/:familyId/statistics/location
```

---

**文档结束**

