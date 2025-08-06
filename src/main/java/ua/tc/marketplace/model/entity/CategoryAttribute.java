package ua.tc.marketplace.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a classification attribute for a category.
 *
 * <p>This class defines the structure of a classification attribute stored in the database. It
 * includes properties such as ID, name, and value type.
 */
//@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category_attributes")
@Entity
public class CategoryAttribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_id", nullable = false)
  private Attribute attribute;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
          name = "category_attribute_option",
          joinColumns = @JoinColumn(name = "category_attribute_id")
  )
  @Column(name = "value", nullable = false)
  private Set<String> values = new HashSet<>();
}
