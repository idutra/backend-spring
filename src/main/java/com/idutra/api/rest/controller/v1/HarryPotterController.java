package com.idutra.api.rest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idutra.api.component.MensagemComponente;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.AlterarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.CriarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.ListarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.AtualizarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ConsultarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.DeleteResponseDTO;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;

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
    @Operation(summary = "Criar Personagem", description = "Criar um novo personagem na base interna" +
            "\nPara criação de um novo persão são condideradas as seguintes regras:" +
            "\n - Não é permitido inserir um personagem que esteja presente na api potterapi" +
            "\n - Não é permitido inserir um personagem que esteja sendo vinculado a uma casa diferente da casa que está vinculado no potterapi" +
            "\n - Não é permitido inserir um personagem vinculando a uma casa que não exista no potterapi.",
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
    @Operation(summary = "Alterar Personagem", description = "Altera informações de um personagem já cadastrado na base interna" +
            "\n - Para alterar um personagem será necessário informar o `id` do personagem." +
            "\n - É permitido apenas a alteração das informações: `role`,`school` e `patronus`, caso sejam informados outros dados a serem alterados, será retornado exceção",
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

    @DeleteMapping(path = {URI_CHAR_REMOVE}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remover Personagem", description = "Remove um personagem da base interna.",
            operationId = "removerPersonagem", tags = DOC_TAG_CHAR,
            parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, schema = @Schema(type = "string"),
                            required = true, description = "Código identificador de personagem")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DeleteResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Acesso proibido ao usuário", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseErroDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseErroDTO.class))})
    })
    public ResponseEntity<DeleteResponseDTO> removerPersonagem(@NotEmpty @PathVariable("id") String codigo) {
        DeleteResponseDTO responseDTO = this.service.removerPersonagem(codigo);
        responseDTO.setMensagem(this.mensagemComponente.get(MSG_REST_PERSONAGEM_EXCLUIDO_SUCESSO, codigo));
        return ok(responseDTO);
    }

    @GetMapping(path = URI_CHAR_GET)
    @Operation(summary = "Consultar Personagem", description = "Realiza a consulta de um personagem com base no código `id`" +
            "\n - Serão retornadas informações de personagem que existem apenas na base interna.",
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
    public ResponseEntity<PersonagemDTO> consultarPersonagem(@Valid @PathVariable("id") String codigo) throws InterruptedException {
        log.info(" >>> Iniciando consulta");
        PersonagemDTO responseDTO = this.service.consultarPersonagem(codigo);
        log.info(">>> Finalizando cache");
        return ok(responseDTO);
    }

    @GetMapping(path = URI_CHAR)
    @Operation(summary = "Listar Personagens", description = "Responsável por listar personagens cadastrados na base interna." +
            "\n - Caso não seja informado nenhum parâmetro no filtro, serão retornados todos os personagens cadastrados na base interna" +
            "\n - Caso seja informado um ou mais parâmetros no filtro, serão retornados apenas os personagens que possuirem as informações do filtro.",
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
    public ResponseEntity<ListarPersonagemResponseDTO> listarPersonagens(@BeanParam ListarPersonagemRequestDTO personagemFiltroDTO) {
        ListarPersonagemResponseDTO responseDTO = this.service.listarPersonagens(personagemFiltroDTO);
        return ok(responseDTO);
    }
}
