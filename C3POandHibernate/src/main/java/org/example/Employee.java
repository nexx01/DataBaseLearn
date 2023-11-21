package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
   
    @Column(name = "name")
    private String name;
   
    @Column(name="city")
    private String city;
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
   
    public String getCity() {
        return city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
}