import {GlobalError, globalErrorEvent} from "../../Pages/ErrorModal/GlobalModalError";
import {PagesEnum} from "../../Pages/PagesEnum";

export default async function checkForAuthError(e: any): Promise<any> {
    let status = e?.response?.status;
    if (status === 401) {
        showNotAuthorizedMessage();
    }
    return e;
}

export function showNotAuthorizedMessage() {
    globalErrorEvent(new GlobalError("You are not authorized", PagesEnum.LOGIN, true))
}