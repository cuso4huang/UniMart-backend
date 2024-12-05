package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="tag_id")
    private int tagId;
    @Column(name = "tag_name")
    private String tagName;
}
