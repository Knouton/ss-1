package com.poluhin.ss.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "SECURITY_TEST", name = "RECOURCE_OBJECTS")
public class ResourceObjectEntity {

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "recource_value")
    private String value;
    @Column(name = "PATH")
    private String path;
}
