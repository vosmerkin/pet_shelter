package ua.tc.marketplace.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ua.tc.marketplace.model.AttributeValueKey;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.AdAttribute;
import ua.tc.marketplace.model.entity.Attribute;
import ua.tc.marketplace.repository.AdCustomRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class AdRepositoryImpl implements AdCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<AttributeValueKey, Long> countAdsGroupedByAttribute(Specification<Ad> specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Ad> root = query.from(Ad.class);

        Join<Ad, AdAttribute> adAttributesJoin = root.join("adAttributes");
        Join<AdAttribute, Attribute> attributeJoin = adAttributesJoin.join("attribute");

        Predicate predicate = (specification != null)
                ? specification.toPredicate(root, query, builder)
                : builder.conjunction();
        query.where(predicate);

        query.multiselect(
                attributeJoin.get("name").alias("attributeName"),
                adAttributesJoin.get("value").alias("value"),
                builder.count(root).alias("adCount")
        ).groupBy(attributeJoin.get("name"), adAttributesJoin.get("value"));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> new AttributeValueKey(
                                tuple.get("attributeName", String.class),
                                tuple.get("value", String.class)
                        ),
                        tuple -> tuple.get("adCount", Long.class)
                ));
    }
}
