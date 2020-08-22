package com.idutra.api.constants;

public interface MensagemConstant {
    /*Definições URI*/
    String URI_BASE = "/api/v1/";
    String URI_CHAR = "characters";
    String URI_CHAR_CREATE = "characters/create";
    String URI_CHAR_UPDATE = "characters/update";
    String URI_CHAR_REMOVE = "characters/delete/{uuid}";
    String URI_CHAR_GET = "characters/{uuid}";

    /*Doc Api*/
    String DOC_TAG_CHAR = "Personagem";

    /*Messages*/
    String MSG_REST_PERSONAGEM_EXCLUIDO_SUCESSO = "";//Todo: adicionar no messages.properties
}
