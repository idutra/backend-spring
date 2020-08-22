package com.idutra.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "proprietario")
@Table(name = "personagem", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "personagem_nome_uk")
})
@Entity
public class Personagem {

    @Id
    @NotNull
    private String uuid;
    @NotEmpty
    private String name;
    @NotEmpty
    private String role;
    @NotEmpty
    private String school;
    @NotEmpty
    private String houseId;
    @NotEmpty
    private String patronus;
    @NotNull
    private OffsetDateTime dataHoraCriacao;
    @NotNull
    private OffsetDateTime dataHoraUltAtualizacao;

    public Personagem(String name, String role, String school, String houseId, String patronus, String uuid) {
        this.name = name;
        this.role = role;
        this.school = school;
        this.houseId = houseId;
        this.patronus = patronus;
        this.uuid = uuid;
    }

    @PrePersist
    public void prePersist() {
        if (Strings.isEmpty(this.uuid)) {
            this.uuid = UUID.randomUUID().toString();
        }
        this.dataHoraCriacao = OffsetDateTime.now();
        this.dataHoraUltAtualizacao = OffsetDateTime.now();
    }

    public Personagem(String name,
                      String role,
                      String school,
                      String houseId,
                      String patronus) {
        this.name = name;
        this.role = role;
        this.school = school;
        this.houseId = houseId;
        this.patronus = patronus;
    }
}
