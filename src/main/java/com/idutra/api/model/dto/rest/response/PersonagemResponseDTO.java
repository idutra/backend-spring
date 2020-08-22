package com.idutra.api.model.dto.rest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

import static com.idutra.api.constants.LiteralConstants.PATTERN_OFFSET_DATE_TIME_SERIALIZER;

@Data
@Schema(type = "object", description = "Representação do response de personagem.")
public class PersonagemResponseDTO extends PersonagemDTO {
    @Schema(description = "Código identificador único do personagem criado", type = "string")
    private String uuid;
    @Schema(description = "Data/Hora da Criação do personagem em banco de dados", type = "date-time", pattern = PATTERN_OFFSET_DATE_TIME_SERIALIZER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_OFFSET_DATE_TIME_SERIALIZER)
    private OffsetDateTime dataHoraCriacao;
    @Schema(description = "Data/Hora da ultíma atualização das informações do personagem em banco de dados", type = "date-time", pattern = PATTERN_OFFSET_DATE_TIME_SERIALIZER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_OFFSET_DATE_TIME_SERIALIZER)
    private OffsetDateTime dataHoraUltAtualizacao;
}
