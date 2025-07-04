package ua.tc.marketplace.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ua.tc.marketplace.model.enums.ValueType;

/**
 * Entity class representing a classification attribute for a category.
 *
 * <p>This class defines the structure of a classification attribute stored in the database. It
 * includes properties such as ID, name, and value type.
 */
//@Builder
//@Data
//@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_attribute_values")
@Entity
public class CategoryAttributeValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category_attribute_id")
  private CategoryAttribute categoryAttribute;

  private String value;
}
