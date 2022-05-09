# 初识smart-doc
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
v2022-05-09 17:35:53|2022-05-09 17:35:53|auto|@amamiyaren|Created by smart-doc



## 用户接口
### 根据用户id查询用户信息
**URL:** http://localhost:8080/javaweb/user/getUserById

**Type:** GET

**Author:** ybw

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 根据用户id查询用户信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|用户id|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8080/javaweb/user/getUserById?id=125
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|响应码|-
message|string|响应消息|-
data|object|响应数据|-
└─id|int32|主键自增id|-
└─userName|string|用户名|-
└─userTel|int32|手机号|-
└─userPassword|string|密码|-
└─userEmail|string|注册邮箱|-
└─userStuNo|string|注册学号|-
└─userAvatar|string|用户头像|-
└─userPermission|string|用户权限|-
└─userSalt|string|密码盐|-

**Response-example:**
```
{
  "code": 614,
  "message": "success",
  "data": {
    "id": 990,
    "userName": "ashley.romaguera",
    "userTel": 930,
    "userPassword": "hh30pk",
    "userEmail": "grace.hyatt@yahoo.com",
    "userStuNo": "l9xpv9",
    "userAvatar": "pa1086",
    "userPermission": "edxszs",
    "userSalt": "yv8n0z"
  }
}
```

### 注册用户
**URL:** http://localhost:8080/javaweb/user/register

**Type:** POST

**Author:** ybw

**Content-Type:** application/json; charset=utf-8

**Description:** 注册用户

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
userName|string|用户名|false|-
userPassword|string|密码|false|-
userTel|int32|用户手机号|false|-

**Request-example:**
```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8080/javaweb/user/register --data '{
  "userName": "ashley.romaguera",
  "userPassword": "oc0y4i",
  "userTel": 348
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|响应码|-
message|string|响应消息|-
data|object|响应数据|-

**Response-example:**
```
{
  "code": 924,
  "message": "success",
  "data": "fng9ge"
}
```


