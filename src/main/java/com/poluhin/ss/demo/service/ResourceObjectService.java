package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.*;
import com.poluhin.ss.demo.domain.model.*;
import com.poluhin.ss.demo.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import static org.springframework.data.mapping.Alias.ofNullable;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;
    public Integer save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
                resourceObject.getId(), resourceObject.getValue(),
                resourceObject.getPath())).getId();

    }
    public ResourceObject get(int id) {
        return repository.findById(id)
                .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()))
                .orElse(null);
    }

}
