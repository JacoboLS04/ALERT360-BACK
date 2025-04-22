package co.edu.uniquindio.alert360_BACK.security.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data //Create the Setter, Getters,To String, hashcode
@NoArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String name;
    private String description;

    public Category(String name, String description){
        this.name = name;
        this.description = description;
    }

}
