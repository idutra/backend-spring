package com.idutra.api.rest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idutra.api.component.MensagemComponente;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.AlterarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.CriarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.AtualizarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ConsultarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ListarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ResponseErroDTO;
import com.idutra.api.service.PersonagemService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.idutra.api.constants.MensagemConstant.DOC_TAG_CHAR;
import static com.idutra.api.constants.MensagemConstant.MSG_REST_PERSONAGEM_EXCLUIDO_SUCESSO;
import static com.idutra.api.constants.MensagemConstant.URI_BASE;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_CREATE;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_GET;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_REMOVE;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_UPDATE;
import static org.springframework.http.ResponseEntity.ok;

@Api(value = DOC_TAG_CHAR, tags = {DOC_TAG_CHAR})
@Validated
@RestController
@Log4j2
@RequestMapping(URI_BASE)
public class HarryPotterController extends AbstractController {
    private final PersonagemService service;
    private final MensagemComponente mensagemComponente;

    @Autowired
    public HarryPotterController(final PersonagemService service,
                                 final MensagemComponente mensagemComponente) {
        this.service = service;
        this.mensagemComponente = mensagemComponente;
    }

    @PostMapping(path = URI_CHAR_CREATE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Criar Personagem", description = "Criar um novo personagem na base interna",
            operationId = "incluirPersonagem", tags = {DOC_TAG_CHAR},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schema = @Schema(implementation = CriarPersonagemRequestDTO.class),
                    mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE
            ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CriarPersonagemResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<CriarPersonagemResponseDTO> incluirPersonagem(@Valid @NotNull @RequestBody CriarPersonagemRequestDTO personagemDTO) throws JsonProcessingException {
        CriarPersonagemResponseDTO responseDTO = this.service.salvarPersonagem(personagemDTO);
        return ok(responseDTO);
    }

    @PutMapping(path = URI_CHAR_UPDATE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Alterar Personagem", description = "Altera informações de um personagem já cadastrado na base interna",
            operationId = "alterarPersonagem", tags = {DOC_TAG_CHAR},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    schema = @Schema(implementation = AlterarPersonagemRequestDTO.class),
                    mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE
            ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AtualizarPersonagemResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<AtualizarPersonagemResponseDTO> alterarPersonagem(@Valid @NotNull @RequestBody AlterarPersonagemRequestDTO personagemDTO) {
        AtualizarPersonagemResponseDTO responseDTO = this.service.atualizarPersonagem(personagemDTO);
        return ok(responseDTO);
    }

    @DeleteMapping(path = URI_CHAR_REMOVE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remover Personagem", description = "Faz a exclusão de um registro de personagem na base interna",
            operationId = "removerPersonagem", tags = {DOC_TAG_CHAR},
            parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, schema = @Schema(type = "string"),
                            required = true, description = "Código id do personagem na base interna")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<String> removerPersonagem(@NotEmpty @PathVariable("id") String codigo) {
        this.service.removerPersonagem(codigo);
        return ok(this.mensagemComponente.get(MSG_REST_PERSONAGEM_EXCLUIDO_SUCESSO, codigo));
    }

    @GetMapping(path = URI_CHAR_GET)
    @Operation(summary = "Consultar Personagem", description = "Realiza a consulta de um personagem com base no código id",
            operationId = "consultarPersonagem", tags = {DOC_TAG_CHAR},
            parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, schema = @Schema(type = "string"),
                            required = true, description = "Código id do personagem")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PersonagemDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<PersonagemDTO> consultarPersonagem(@Valid @PathVariable("id") String codigo) {
        PersonagemDTO responseDTO = this.service.consultarPersonagem(codigo);
        return ok(responseDTO);
    }

    @GetMapping(path = URI_CHAR)
    @Operation(summary = "Listar Personagens", description = "Consulta uma lista de personagens de acordo com o filtro",
            operationId = "listarPersonagens", tags = {DOC_TAG_CHAR}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ConsultarPersonagemResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<ListarPersonagemResponseDTO> listarPersonagens(@RequestParam(required = false, value = "name") @Schema(description = "Nome do personagem") String name,
                                                                         @RequestParam(required = false, value = "role") @Schema(description = "Função do personagem") String role,
                                                                         @RequestParam(required = false, value = "school") @Schema(description = "Escola do personagem") String school,
                                                                         @RequestParam(required = false, value = "houseId") @Schema(description = "Código da casa do personagem") String houseId,
                                                                         @RequestParam(required = false, value = "patronus") @Schema(description = "Patronus do personagem") String patronus,
                                                                         @RequestParam(required = false, value = "id") @Schema(description = "Código identificador do personagem") String id) {
        ListarPersonagemResponseDTO responseDTO = this.service.listarPersonagens(name, role, school, houseId, patronus, id);
        return ok(responseDTO);
    }
}
