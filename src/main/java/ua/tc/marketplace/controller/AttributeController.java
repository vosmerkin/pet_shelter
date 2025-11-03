package ua.tc.marketplace.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.tc.marketplace.model.dto.attribute.AttributeDto;
import ua.tc.marketplace.model.dto.attribute.AttributeRequest;
import ua.tc.marketplace.service.AttributeService;
import ua.tc.marketplace.util.openapi.AttributeOpenApi;

import static ua.tc.marketplace.config.ApiURLs.*;

/**
 * AttributeController handles HTTP requests related to CRUD operations for the Attribute entity. It
 * provides an API for retrieving, creating, updating, and deleting attributes.
 */
@RestController
@RequestMapping(ATTRIBUTE_BASE)
@RequiredArgsConstructor
@Slf4j
public class AttributeController implements AttributeOpenApi {

  private final AttributeService attributeService;

  @Override
  @GetMapping(ATTRIBUTE_GET_ALL)
  public ResponseEntity<List<AttributeDto>> getAllAttributes(Pageable pageable) {
    List<AttributeDto> attributes = attributeService.findAll(pageable);
    log.info("Attributes were retrieved successfully");
    return ResponseEntity.ok(attributes);
  }

  @Override
  @GetMapping(ATTRIBUTE_GET_BY_ID)
  public ResponseEntity<AttributeDto> getAttributeById(@PathVariable Long id) {
    AttributeDto attributeDto = attributeService.findById(id);
    log.info("Attribute with ID: {} was retrieved successfully", id);
    return ResponseEntity.ok(attributeDto);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(ATTRIBUTE_CREATE)
  public ResponseEntity<AttributeDto> createAttribute(
      @Valid @RequestBody AttributeRequest attributeDTO) {
    AttributeDto createdAttribute = attributeService.createAttribute(attributeDTO);
    log.info("Attribute was created successfully : {}", createdAttribute);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribute);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(ATTRIBUTE_UPDATE)
  public ResponseEntity<AttributeDto> updateAttribute(
      @PathVariable Long id, @RequestBody AttributeRequest attributeDto) {
    AttributeDto updatedAttribute = attributeService.update(id, attributeDto);
    log.info("Attribute with ID: {} was updated successfully: {}", id, updatedAttribute);
    return ResponseEntity.ok(updatedAttribute);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(ATTRIBUTE_DELETE)
  public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
    attributeService.deleteById(id);
    log.info("Attribute with ID: {} was deleted successfully", id);
    return ResponseEntity.noContent().build();
  }
}
