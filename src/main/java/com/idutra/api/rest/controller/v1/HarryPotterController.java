package com.idutra.api.rest.controller.v1;

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

import static org.springframework.http.ResponseEntity.ok;

@Api(value = "", tags = {""})
@Validated
@RestController
@Log4j2
@RequestMapping("/api/v1/")
public class HarryPotterController extends AbstractController {
    private PersonagemService service;

    @Autowired
    public HarryPotterController(final PersonagemService service) {
        this.service = service;
    }

    @PostMapping(path = "characters/create", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "", description = "",
            operationId = "incluirPersonagem", tags = {""},
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
    public ResponseEntity<CriarPersonagemResponseDTO> incluirPersonagem(@Valid @NotNull @RequestBody CriarPersonagemRequestDTO personagemDTO) {
        CriarPersonagemResponseDTO responseDTO = this.service.salvarPersonagem(personagemDTO);
        return ok(responseDTO);
    }

    @PutMapping(path = "characters/update", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "", description = "",
            operationId = "alterarPersonagem", tags = {""},
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

    @DeleteMapping(path = "characters/delete/{uuid}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "", description = "",
            operationId = "removerPersonagem", tags = {""},
            parameters = {
                    @Parameter(name = "uuid", in = ParameterIn.PATH, schema = @Schema(type = "string"),
                            required = true, description = "Código uuid do personagem na base interna")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
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
    public ResponseEntity<String> removerPersonagem(@NotEmpty @PathVariable("uuid") String codigoUuid) {
        String resposta = this.service.removerPersonagem(codigoUuid);
        return ok(resposta);
    }

    @GetMapping(path = "characters/{uuid}")
    @Operation(summary = "", description = "",
            operationId = "consultarPersonagem", tags = {""},
            parameters = {
                    @Parameter(name = "uuid", in = ParameterIn.PATH, schema = @Schema(type = "string"),
                            required = true, description = "Código uuid do personagem")
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
    public ResponseEntity<PersonagemDTO> consultarPersonagem(@Valid @PathVariable("uuid") String codigo) {
        PersonagemDTO responseDTO = this.service.consultarPersonagem(codigo);
        return ok(responseDTO);
    }

    @GetMapping(path = "characters")
    @Operation(summary = "", description = "",
            operationId = "listarPersonagens", tags = {""}
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
    public ResponseEntity<ListarPersonagemResponseDTO> listarPersonagens(@RequestParam(required = false, value = "name") String name,
                                                                         @RequestParam(required = false, value = "role") String role,
                                                                         @RequestParam(required = false, value = "school") String school,
                                                                         @RequestParam(required = false, value = "houseId") String houseId,
                                                                         @RequestParam(required = false, value = "patronus") String patronus,
                                                                         @RequestParam(required = false, value = "uuid") String uuid) {
        ListarPersonagemResponseDTO responseDTO = this.service.listarPersonagens(name, role, school, houseId, patronus, uuid);
        return ok(responseDTO);
    }
}
