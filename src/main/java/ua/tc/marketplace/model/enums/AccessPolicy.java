package ua.tc.marketplace.model.enums;

import lombok.Getter;

import java.util.Set;

import static ua.tc.marketplace.model.enums.UserRole.*;

@Getter
public enum AccessPolicy {
    PUBLIC(false, Set.of(), false),
    AUTHENTICATED(true, Set.of(USER, SHELTER, VOLUNTEER), false),
    AUTHENTICATED_AND_OWNER(true, Set.of(USER, SHELTER, VOLUNTEER), true),

    ADMIN_OR_AUTHENTICATED_AND_OWNER(true, Set.of(USER, SHELTER, VOLUNTEER,ADMIN), true) {
        @Override
        public String getPreAuthorizeExpression(String resourceIdParamName) {
            // Custom SpEL: admin bypasses ownership check
            return String.format(
                    "(hasAuthority('ADMIN')) or " +
                            "(hasAuthority('USER') and @securityService.isOwnerOfResource(%s))",
                    resourceIdParamName
            );
        }},
    ADMIN_ONLY(true, Set.of(ADMIN), false);



    private final boolean requiresAuthentication;
    private final Set<UserRole> requiredAuthorities;
    private final boolean requiresOwnership;

    AccessPolicy(boolean requiresAuthentication, Set<UserRole> requiredAuthorities, boolean requiresOwnership) {
        this.requiresAuthentication = requiresAuthentication;
        this.requiredAuthorities = Set.copyOf(requiredAuthorities);
        this.requiresOwnership = requiresOwnership;
    }

    public String getPreAuthorizeExpression(String resourceIdParamName) {
        if (!requiresAuthentication) {
            return "permitAll"; // not used in @PreAuthorize, but for consistency
        }
        StringBuilder expr = new StringBuilder();

        // Role check
        if (!requiredAuthorities.isEmpty()) {
            String roles = requiredAuthorities.stream()
                    .map(role -> "hasAuthority('" + role + "')")
                    .reduce((a, b) -> a + " and " + b)
                    .orElse("");
            expr.append("(").append(roles).append(")");
        }

        // Ownership check (only if user has required role and ownership is needed)
        if (requiresOwnership) {
            if (expr.length() > 0) {
                expr.append(" and ");
            }
            expr.append("@securityService.isOwnerOfResource(").append(resourceIdParamName).append(")");
        }

        return expr.length() > 0 ? expr.toString() : "authenticated";
    }

}
