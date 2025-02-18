package ua.tc.marketplace.util.ad_filtering;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.tc.marketplace.model.entity.Ad;

import java.util.Map;

/**
 * FilterSpecificationFactory is a component responsible for generating JPA Specifications for
 * filtering ads.
 *
 * <p>This class uses a map of filter specifications and builds a combined JPA Specification based
 * on the provided filter criteria. It supports dynamic filtering by attribute or other predefined
 * criteria.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FilterSpecificationFactory {

    private final Map<String, FilterSpecification<Ad>> filters;

    public Specification<Ad> getSpecification(Map<String, String> filterCriteria) {
        Specification<Ad> specification = null;

        for (Map.Entry<String, String> entry : filterCriteria.entrySet()) {
            if (entry.getValue() == null) continue;

            String key = entry.getKey();
            if ("location".equals(key)) continue;

            Specification<Ad> newSpec = null;

            if ("isHot".equals(key)) {
                boolean isHotValue = Boolean.parseBoolean(entry.getValue());
                newSpec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isHot"), isHotValue);

            } else {
                FilterSpecification<Ad> filter = key.startsWith("attribute_") ? filters.get("attribute") : filters.get(key);
                if (filter != null) {
                    newSpec = filter.getSpecification(entry);
                }
            }

            if (newSpec != null) {
                specification = (specification == null) ? newSpec : specification.and(newSpec);
            }
        }

        return specification;
//    return filterCriteria.entrySet().stream()
//        .filter(entry -> entry.getValue() != null)
//        .filter(entry-> !entry.getKey().equals("location"))
//        .map(
//            entry -> {
//              String key = entry.getKey();
//              FilterSpecification<Ad> filter =
//                  key.startsWith("attribute_") ? filters.get("attribute") : filters.get(key);
//              return filter != null ? filter.getSpecification(entry) : null;
//            })
//        .filter(Objects::nonNull)
//        .reduce(Specification::and)
//        .orElse(null);
  }
}
