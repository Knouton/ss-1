package com.poluhin.ss.demo.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceObject {
    private int id;
    private String value;
    private String path;
}
