package com.idutra.api.constants;

public interface MensagemConstant {
    /*Definições URI*/
    String URI_BASE = "/api/v1/";
    String URI_CHAR = "characters";
    String URI_CHAR_CREATE = "characters/create";
    String URI_CHAR_UPDATE = "characters/update";
    String URI_CHAR_REMOVE = "characters/delete/{id}";
    String URI_CHAR_GET = "characters/{id}";

    /*Doc Api*/
    String DOC_TAG_CHAR = "Personagem";

    /*Messages*/
    String MSG_SQL_CONSTRAINT_EXCEPTION = "sqlexception.constraint.error.message";
    String MSG_REST_PERSONAGEM_EXCLUIDO_SUCESSO = "entity.personagem.remove.successfull.message";
    String MSG_INT_POTTER_API_ERROR = "integracao.potterapi.error.message";
    String MSG_INT_POTTER_API_CHAR_NOT_FOUND = "integracao.potterapi.char.notfound.message";
    String MSG_INT_POTTER_API_CHAR_HOUSE_INVALID = "integracao.potterapi.charhouse.invalid.message";
    String MSG_INT_POTTER_API_HOUSE_INVALID = "integracao.potterapi.house.invalid.message";


    String MSG_PERSONAGEM_NOT_FOUND = "entity.personagem.notfound.message";
    String MSG_LISTA_PERSONAGEM_EMPTY = "entity.lista.personagem.empty.message";
    String MSG_UPDATE_PERSONAGEM_NAME_ERROR = "entity.update.personagem.name.error.message";
    String MSG_UPDATE_PERSONAGEM_HOUSE_ERROR = "entity.update.personagem.house.error.message";

    String MSG_RESPONSE_NOT_NULL = "object.response.notnull.message";
    String MSG_REQUEST_NOT_NULL = "object.request.notnull.message";
    String MSG_PERSONAGEM_NOT_NULL = "object.personagem.notnull.message";

    String MSG_PROPRIEDADE_DESCONHECIDA = "message.property_unrecognized";
    String MSG_REQUEST_BODY_MISSING = "requestBody.invalid.message";
    String MSG_REQUEST_PARAM_MISSING = "requestParam.missing.message";
    String MISSING_HEADER_MESSAGE = "exception.missing.header.message";
    String MSG_PERSONAGEM_DUPLICADO = "entity.personagem.duplicado.message";

    /*Atributos*/
    String MSG_PERSONAGEM_ID_NOT_EMPTY = "atributo.id.notempty.message";
    String MSG_PERSONAGEM_NAME_NOT_EMPTY = "atributo.name.notempty.message";
    String MSG_PERSONAGEM_ROLE_NOT_EMPTY = "atributo.role.notempty.message";
    String MSG_PERSONAGEM_SCHOOL_NOT_EMPTY = "atributo.school.notempty.message";
    String MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY = "atributo.house.notempty.message";
    String MSG_PERSONAGEM_PATRONUS_NOT_EMPTY = "atributo.patronus.notempty.message";
    String MSG_PERSONAGEM_DH_CREATION_NOT_NULL = "atributo.datahoracriacao.notnull.message";
    String MSG_PERSONAGEM_DH_ULT_ATU_NOT_NULL = "atributo.datahoraultatualizacao.notnull.message";
}
