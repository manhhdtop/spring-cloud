package info.manhhdtop.cloud.auth.processors;

import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.auth.repositories.PermissionRepository;
import info.manhhdtop.cloud.auth.repositories.RoleRepository;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.common.security.annotations.RequirePermission;
import info.manhhdtop.cloud.common.security.annotations.RequireRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Processor để scan các controller và tự động tạo role/permission từ annotations
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class RolePermissionProcessor implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext applicationContext;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private boolean processed = false;

    @Value("${app.admin.email:admin@cloud.or}")
    private String adminEmail;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Chỉ chạy một lần khi context được refresh
        if (processed) {
            return;
        }
        processed = true;
        
        log.info("Starting role and permission auto-creation from annotations...");
        
        Set<String> processedRoles = new HashSet<>();
        Set<String> processedPermissions = new HashSet<>();
        
        // Scan tất cả các bean có annotation @RestController
        applicationContext.getBeansWithAnnotation(RestController.class).values().forEach(controller -> {
            // Lấy class thực tế (không phải proxy)
            Class<?> controllerClass = AopUtils.getTargetClass(controller);
            String controllerModule = getControllerModule(controllerClass);
            
            // Xử lý annotation ở class level
            processClassLevelAnnotations(controllerClass, controllerModule, processedRoles, processedPermissions);
            
            // Xử lý annotation ở method level
            processMethodLevelAnnotations(controllerClass, controllerModule, processedRoles, processedPermissions);
        });
        
        log.info("Completed role and permission auto-creation. Processed {} roles and {} permissions", 
                processedRoles.size(), processedPermissions.size());
    }

    private void processClassLevelAnnotations(Class<?> controllerClass, String module, 
                                             Set<String> processedRoles, Set<String> processedPermissions) {
        RequireRole requireRole = AnnotationUtils.findAnnotation(controllerClass, RequireRole.class);
        if (requireRole != null) {
            String roleName = requireRole.value();
            String description = requireRole.description().isEmpty() 
                    ? "Auto-generated role for " + controllerClass.getSimpleName()
                    : requireRole.description();
            String roleModule = requireRole.module().isEmpty() ? module : requireRole.module();
            
            createRoleIfNotExists(roleName, description, roleModule, processedRoles);
        }

        RequirePermission requirePermission = AnnotationUtils.findAnnotation(controllerClass, RequirePermission.class);
        if (requirePermission != null) {
            String permissionName = requirePermission.value();
            String description = requirePermission.description().isEmpty()
                    ? "Auto-generated permission for " + controllerClass.getSimpleName()
                    : requirePermission.description();
            String permissionModule = requirePermission.module().isEmpty() ? module : requirePermission.module();
            
            createPermissionIfNotExists(permissionName, description, permissionModule, processedPermissions);
        }
    }

    private void processMethodLevelAnnotations(Class<?> controllerClass, String module,
                                              Set<String> processedRoles, Set<String> processedPermissions) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            RequireRole requireRole = AnnotationUtils.findAnnotation(method, RequireRole.class);
            if (requireRole != null) {
                String roleName = requireRole.value();
                String description = requireRole.description().isEmpty()
                        ? "Auto-generated role for " + controllerClass.getSimpleName() + "." + method.getName()
                        : requireRole.description();
                String roleModule = requireRole.module().isEmpty() ? module : requireRole.module();
                
                createRoleIfNotExists(roleName, description, roleModule, processedRoles);
            }

            RequirePermission requirePermission = AnnotationUtils.findAnnotation(method, RequirePermission.class);
            if (requirePermission != null) {
                String permissionName = requirePermission.value();
                String description = requirePermission.description().isEmpty()
                        ? "Auto-generated permission for " + controllerClass.getSimpleName() + "." + method.getName()
                        : requirePermission.description();
                String permissionModule = requirePermission.module().isEmpty() ? module : requirePermission.module();
                
                createPermissionIfNotExists(permissionName, description, permissionModule, processedPermissions);
            }
        }
    }

    private void createRoleIfNotExists(String roleName, String description, String module, Set<String> processedRoles) {
        if (processedRoles.contains(roleName)) {
            return; // Đã xử lý rồi
        }
        
        Role role;
        boolean isNewRole = false;
        
        if (roleRepository.findByName(roleName).isEmpty()) {
            role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            role = roleRepository.save(role);
            isNewRole = true;
            log.info("Auto-created role: {} (module: {})", roleName, module);
        } else {
            role = roleRepository.findByName(roleName).orElse(null);
            log.debug("Role already exists: {}", roleName);
        }
        
        // Nếu role là admin role và được tạo mới, tự động gán cho admin user
        if (isNewRole && role != null && isAdminRole(roleName)) {
            assignRoleToAdminUser(role);
        }
        
        processedRoles.add(roleName);
    }

    private boolean isAdminRole(String roleName) {
        return roleName != null && (roleName.contains("ADMIN") || roleName.equals("ROLE_ADMIN"));
    }

    private void assignRoleToAdminUser(Role role) {
        userRepository.findByEmail(adminEmail).ifPresent(adminUser -> {
            Set<Role> roles = adminUser.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
                adminUser.setRoles(roles);
            }
            if (!roles.contains(role)) {
                roles.add(role);
                userRepository.save(adminUser);
                log.info("Assigned role {} to admin user {}", role.getName(), adminEmail);
            }
        });
    }

    private void createPermissionIfNotExists(String permissionName, String description, String module, 
                                             Set<String> processedPermissions) {
        if (processedPermissions.contains(permissionName)) {
            return; // Đã xử lý rồi
        }
        
        if (permissionRepository.findByName(permissionName).isEmpty()) {
            Permission permission = new Permission();
            permission.setName(permissionName);
            permission.setDescription(description);
            permission.setModule(module);
            permissionRepository.save(permission);
            log.info("Auto-created permission: {} (module: {})", permissionName, module);
        } else {
            log.debug("Permission already exists: {}", permissionName);
        }
        processedPermissions.add(permissionName);
    }

    private String getControllerModule(Class<?> controllerClass) {
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            String path = requestMapping.value()[0];
            // Lấy module từ path, ví dụ: /api/v1/permissions -> permissions
            String[] parts = path.split("/");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            }
        }
        // Fallback: sử dụng tên class
        return controllerClass.getSimpleName().replace("Controller", "").toLowerCase();
    }
}

