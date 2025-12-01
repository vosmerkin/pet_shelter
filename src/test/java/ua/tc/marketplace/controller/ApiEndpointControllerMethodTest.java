package ua.tc.marketplace.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.tc.marketplace.model.enums.ApiEndpoint;

class ApiEndpointControllerMethodTest {

    private static final String CONTROLLER_PACKAGE = "ua.tc.marketplace.controller";

    private static final List<Class<?>> CONTROLLER_CLASSES = findControllerClasses();

    @Test
    void allApiEndpointsMustHaveMatchingControllerMethodWithCorrectHttpMethod() throws Exception {
        // Step 1: Discover all controller methods with their paths and HTTP methods
        List<ControllerMethodInfo> controllerMethods = discoverControllerMethods();

        // Step 2: For each ApiEndpoint, find a matching controller method
        for (ApiEndpoint endpoint : ApiEndpoint.values()) {
            String expectedPath = normalizePath(endpoint.getPath());
            String expectedMethod = endpoint.getMethod().name();

            List<ControllerMethodInfo> matches = controllerMethods.stream()
                    .filter(cm -> cm.path.equals(expectedPath))
                    .filter(cm -> cm.methods.contains(expectedMethod))
                    .collect(Collectors.toList());

            assertThat(matches)
                    .withFailMessage(
                            "No matching controller method found for endpoint: %s %s (policy: %s). " +
                                    "Check if controller uses correct path and HTTP method.",
                            endpoint.getMethod(), endpoint.getPath(), endpoint.getPolicy())
                    .isNotEmpty();

            // Optional: Ensure only one match (avoid ambiguity)
            assertThat(matches)
                    .withFailMessage(
                            "Multiple controller methods match endpoint: %s %s. This may cause ambiguity.",
                            endpoint.getMethod(), endpoint.getPath())
                    .hasSize(1);
        }
    }

    private List<ControllerMethodInfo> discoverControllerMethods() {
        List<ControllerMethodInfo> methods = new ArrayList<>();

        for (Class<?> clazz : CONTROLLER_CLASSES) {
            RequestMapping typeMapping = clazz.getAnnotation(RequestMapping.class);
            String classPath = extractPath(typeMapping);

            for (Method method : clazz.getDeclaredMethods()) {
                RequestMapping methodMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (methodMapping == null) continue;

                String methodPath = extractPath(methodMapping);
                String fullPath = normalizePath(classPath + methodPath);
                Set<String> httpMethods = getHttpMethods(methodMapping);

                methods.add(new ControllerMethodInfo(fullPath, httpMethods));
            }
        }
        return methods;
    }

    private Set<String> getHttpMethods(RequestMapping mapping) {
        if (mapping.method().length == 0) {
            // Default to GET if no method specified (as Spring does)
            return Set.of(GET.name());
        }
        return Arrays.stream(mapping.method())
                .map(RequestMethod::name)
                .collect(Collectors.toSet());
    }

    private String extractPath(RequestMapping mapping) {
        if (mapping == null || mapping.value().length == 0) {
            return "";
        }
        // Use the first path if multiple are defined
        return mapping.value()[0];
    }

    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        // Ensure it starts with /
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        // Remove trailing slash (except for root)
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private static List<Class<?>> findControllerClasses() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        return scanner.findCandidateComponents(CONTROLLER_PACKAGE).stream()
                .map(beanDef -> {
                    try {
                        return Class.forName(beanDef.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Failed to load controller class: " + beanDef.getBeanClassName(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    private static class ControllerMethodInfo {
        private String path;
        private Set<String> methods;
    }
}