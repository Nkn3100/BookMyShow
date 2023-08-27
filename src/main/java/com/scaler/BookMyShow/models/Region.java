package com.scaler.BookMyShow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity // in this case it will take class name as tableName, else we can do @Entity(name = "Region")
public class Region extends BaseModel{

    private String name;
    @OneToMany
    private List<Theatre> theatres;
}
