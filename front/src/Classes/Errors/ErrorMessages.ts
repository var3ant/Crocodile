export enum Errors {
    ALREADY_EXIST_MESSAGE,
    BAD_AUTHENTICATOR_MESSAGE,
    ILLEGAL_NAME_MESSAGE,
    ILLEGAL_ARGUMENT_EXCEPTION,
    INTERNAL_SERVER_ERROR_MESSAGE,
    INVALID_USER_AUTH_DATA_MESSAGE,
    ROOM_NOT_FOUND_MESSAGE,
    USER_NOT_FOUND_MESSAGE,
    USER_NOT_IN_ROOM_MESSAGE,
    WRONG_GAME_ROLE_EXCEPTION,
    ILLEGAL_USER_MESSAGE,
    BAD_PASSWORD_MESSAGE

}

export function errorToString(enumValue: Errors): string {
    return Errors[enumValue]
}