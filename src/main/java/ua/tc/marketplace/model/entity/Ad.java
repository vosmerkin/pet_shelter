package ua.tc.marketplace.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity class representing an advertisement.
 *
 * <p>This class defines the structure of an advertisement stored in the database. It includes
 * properties such as ID, author, title, description, price, photos, thumbnail, categoryId, creation
 * timestamp, and update timestamp.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad")
@Entity
public class Ad {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  private String title;

  private String description;

  private BigDecimal price;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "ad_id")
  private List<Photo> photos = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  private Photo thumbnail;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AdAttribute> adAttributes = new ArrayList<>();

  @CreationTimestamp private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;

  @Override
  public String toString() {
    String adAttributesString =
        adAttributes.stream()
            .map(
                adAttribute -> adAttribute.getAttribute().getName() + ": " + adAttribute.getValue())
            .collect(Collectors.joining(", "));

    return String.format(
        "Ad{id=%d, title='%s', description='%s', price=%s, category=%s, adAttributes=[%s], createdAt=%s, updatedAt=%s}",
        id,
        title,
        description,
        price,
        category != null ? category.getName() : "N/A",
        adAttributesString,
        createdAt,
        updatedAt);
  }
}
