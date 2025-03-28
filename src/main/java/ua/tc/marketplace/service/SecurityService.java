package ua.tc.marketplace.service;

public interface SecurityService {
    boolean hasAnyRoleAndOwnership(Long authorId);
    boolean hasAnyRoleAndOwnership(Long authorId, String... requiredRoles);
}
