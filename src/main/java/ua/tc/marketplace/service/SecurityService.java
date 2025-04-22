package ua.tc.marketplace.service;

public interface SecurityService {
    boolean hasAnyRoleAndAdOwnership(Long adId);
    boolean hasAnyRoleAndOwnership(Long authorId);
    boolean hasAnyRoleAndOwnership(Long authorId, String... requiredRoles);
}
