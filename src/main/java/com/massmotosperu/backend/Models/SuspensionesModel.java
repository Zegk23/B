package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_suspensiones")  
@NoArgsConstructor
@AllArgsConstructor
public class SuspensionesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDSuspension", updatable = false, nullable = false)
    private int idSuspension;

    @NotBlank(message = "La suspensión delantera es obligatoria")
    @Column(name = "SuspensionDelantera", nullable = false)
    private String suspensionDelantera;

    @NotBlank(message = "La suspensión trasera es obligatoria")
    @Column(name = "SuspensionTrasera", nullable = false)
    private String suspensionTrasera;
}
