package fr.polytech.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "payment", schema = "public")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
