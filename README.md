# demo-back

#### 介绍
- framework：后端框架
- 技术栈： Spring Boot 2.1.0 、 Spring Boot Jpa、 JWT、Spring Security、Redis、mybatis-plus、myexcel、mysql、jdk8

#### 系统功能
- 用户管理：提供用户的相关配置，新增用户后，默认密码为：Pass_123
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 字典管理：可维护常用一些固定的数据，如：状态，性别等
- 操作日志：记录用户操作的日志
- 异常日志：记录异常日志，方便开发人员定位错误
- 系统缓存： 查看操作redis中缓存信息
- 在线用户：查看在线用户信息
