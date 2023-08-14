package com.poluhin.ss.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import com.poluhin.ss.demo.domain.entity.ResourceObjectEntity;
import com.poluhin.ss.demo.domain.model.ResourceObject;
import com.poluhin.ss.demo.repository.ResourceObjectRepository;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceObjectServiceTest {
	@Mock
	private ResourceObjectRepository repository;
	@InjectMocks
	private ResourceObjectService service;
	private ResourceObject resourceObject_sample1;
	private ResourceObjectEntity resourceObjectEntity_sample1;

	@BeforeEach
	void init() {
		resourceObject_sample1 = new ResourceObject(1, "value", "path");

		resourceObjectEntity_sample1 = new ResourceObjectEntity(1, "value", "path");
	}

	@Test
	void save_Success() {
		when(repository.save(Mockito.any(ResourceObjectEntity.class))).thenAnswer(i -> i.getArguments()[0]);

		val result = service.save(resourceObject_sample1);

		assertEquals(result, resourceObjectEntity_sample1.getId());

	}

	@Test
	void get_Success() {
		when(repository.findById(1)).thenReturn(Optional.of(resourceObjectEntity_sample1));

		val result = service.get(1);

		assertEquals(result, resourceObject_sample1);
	}
}