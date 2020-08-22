package com.idutra.api.model.dto.rest.response;

import com.idutra.api.model.dto.rest.PersonagemDTO;
import lombok.Data;

import java.util.List;

@Data
public class ConsultarPersonagemResponseDTO {
    List<PersonagemDTO> personagens;
}
