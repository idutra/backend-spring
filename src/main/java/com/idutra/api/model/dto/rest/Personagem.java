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

import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_DH_CREATION_NOT_NULL;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_DH_ULT_ATU_NOT_NULL;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_NAME_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_PATRONUS_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ROLE_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_SCHOOL_NOT_EMPTY;

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
    @NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY)
    private String id;
    @NotEmpty(message = MSG_PERSONAGEM_NAME_NOT_EMPTY)
    private String name;
    @NotEmpty(message = MSG_PERSONAGEM_ROLE_NOT_EMPTY)
    private String role;
    @NotEmpty(message = MSG_PERSONAGEM_SCHOOL_NOT_EMPTY)
    private String school;
    @NotEmpty(message = MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY)
    private String houseId;
    @NotEmpty(message = MSG_PERSONAGEM_PATRONUS_NOT_EMPTY)
    private String patronus;
    @NotNull(message = MSG_PERSONAGEM_DH_CREATION_NOT_NULL)
    private OffsetDateTime dataHoraCriacao;
    @NotNull(message = MSG_PERSONAGEM_DH_ULT_ATU_NOT_NULL)
    private OffsetDateTime dataHoraUltAtualizacao;

    @PrePersist
    public void prePersist() {
        if (Strings.isEmpty(this.id)) {
            this.id = UUID.randomUUID().toString();
        }
        this.dataHoraCriacao = OffsetDateTime.now();
        this.dataHoraUltAtualizacao = OffsetDateTime.now();
    }
}
