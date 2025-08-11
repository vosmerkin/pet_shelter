package ua.tc.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tc.marketplace.model.entity.Category;
import ua.tc.marketplace.model.entity.CategoryAttribute;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing {@link CategoryAttribute} entities.
 *
 * <p>This interface extends {@link JpaRepository} and provides basic CRUD operations
 * for {@link CategoryAttribute} entities, including methods for saving, finding, updating, and deleting
 * category attributes. The repository uses JPA to interact with the database and manage {@link CategoryAttribute}
 * objects.</p>
 *
 * <p>By extending {@link JpaRepository}, this interface inherits a set of pre-defined methods
 * for querying and persisting {@link CategoryAttribute} entities, and it can be further customized
 * with additional query methods if needed.</p>
 */
@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Long> {
    Optional<CategoryAttribute> findByCategory_IdAndAttribute_Id(Long categoryId, Long attributeId);
    Optional<Set<CategoryAttribute>> findByCategory_Id(Long categoryId);
}
