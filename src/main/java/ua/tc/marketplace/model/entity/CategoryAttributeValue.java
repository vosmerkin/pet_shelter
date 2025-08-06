package ua.tc.marketplace.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entity class representing a classification attribute for a category.
 *
 * <p>This class defines the structure of a classification attribute stored in the database. It
 * includes properties such as ID, name, and value type.
 */
//@Builder
@Data
@AllArgsConstructor
@Table(name = "category_attribute_value")
@Entity
public class CategoryAttributeValue {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;

  @EmbeddedId
  private CategoryAttributeId id;

//  @Column(name = "category_attribute_id")
//  private CategoryAttribute categoryAttribute;
  @NotEmpty
  private String value;
}
