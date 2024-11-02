package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, message = "La suspensión delantera no puede tener más de 50 caracteres")
    @Column(name = "SuspensionDelantera", nullable = false, length = 50)
    private String suspensionDelantera;

    @NotBlank(message = "La suspensión trasera es obligatoria")
    @Size(max = 50, message = "La suspensión trasera no puede tener más de 50 caracteres")
    @Column(name = "SuspensionTrasera", nullable = false, length = 50)
    private String suspensionTrasera;
}
