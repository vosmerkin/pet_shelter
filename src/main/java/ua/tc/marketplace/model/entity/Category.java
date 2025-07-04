package ua.tc.marketplace.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a categoryId.
 *
 * <p>This class defines the structure of a categoryId stored in the database. It includes properties
 * such as ID, name, and a list of classification attributes.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

//  @Builder.Default
//  @ManyToMany(fetch = FetchType.EAGER)
//  @JoinTable(
//      name = "category_attributes",
//      joinColumns = @JoinColumn(name = "category_id"),
//      inverseJoinColumns = @JoinColumn(name = "attribute_id"))
//  private List<Attribute> attributes=new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private List <CategoryAttribute> attributes;



}
