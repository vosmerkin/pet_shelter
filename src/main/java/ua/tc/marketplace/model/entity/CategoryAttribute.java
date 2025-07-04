package ua.tc.marketplace.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tc.marketplace.model.enums.ValueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
@Table(name = "category_attributes")
@Entity
public class CategoryAttribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "category_id")
  private Category category;

  @JoinColumn(name = "attribute_id")
  private Attribute attribute;

  @OneToMany(mappedBy = "category_attributes", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CategoryAttributeValue> values;
}
