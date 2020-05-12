package com.rych.week3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Car extends RepresentationModel {
   private Long Id;
   private String make;
   private String model;
   private String colour;
}
