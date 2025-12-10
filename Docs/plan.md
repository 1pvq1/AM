 **数字助理应用集成**的后续步骤和计划

---

# 📂 数字助理应用集成规划

## 已完成
- 在 `AndroidManifest.xml` 中声明了 `ACTION_ASSIST` 的 Intent Filter，使系统识别该应用可以处理数字助理请求。

---

## 接下来步骤

### 1. **角色授权**
- 使用 `RoleManager` 请求用户授予本应用 **数字助理角色** (`ROLE_ASSISTANT`)。
- 在应用启动或设置页面提供入口，调用：
  ```kotlin
  val roleManager = getSystemService(RoleManager::class.java)
  if (roleManager.isRoleAvailable(RoleManager.ROLE_ASSISTANT)) {
      if (!roleManager.isRoleHeld(RoleManager.ROLE_ASSISTANT)) {
          val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_ASSISTANT)
          startActivityForResult(intent, REQUEST_CODE_ROLE_ASSISTANT)
      }
  }
  ```

### 2. **处理 Assist 请求**
- 在 `AssistantActivity` 中接收并处理 `ACTION_ASSIST` Intent。
- 启动应用的主界面或 Copilot UI，展示交互入口。

### 3. **用户设置说明**
- 在 README 或应用内提示用户：  
  - 打开 **系统设置 → 应用 → 默认应用 → 数字助理应用**  
  - 手动选择本应用为默认数字助理。

### 4. **交互体验优化**
- 在 `AssistantActivity` 中：
  - 提供语音输入或快捷操作入口。
  - 支持快速返回搜索/问答界面。
  - 保持轻量启动，避免卡顿。

### 5. **兼容性与测试**
- 在 Android 10+ 使用 `RoleManager`。  
- 在 Android 9 及以下，依赖 Intent Filter，用户需手动选择。  
- 测试不同设备和系统版本的行为。

---

# 📌 TODO 清单

- [x] 在 `AndroidManifest.xml` 中声明 `ACTION_ASSIST` Intent Filter  
- [ ] 在应用中实现 `RoleManager` 请求 `ROLE_ASSISTANT`  
- [ ] 在 `AssistantActivity` 中处理 `ACTION_ASSIST` Intent，启动应用逻辑  
- [ ] 在 README 中添加用户操作说明（如何在系统设置里选择默认数字助理）  
- [ ] 优化启动体验（语音入口、快速响应）  
- [ ] 测试 Android 10+ 与 Android 9- 的兼容性  
- [ ] 增加错误处理（用户拒绝授权、权限不足时的提示）

---

 **数字助理应用集成路线图**。  
