import Http from "../base";
import { LoginResponse, PerfilResponse } from "./types";

export default class PerfilService {
  static login(correo: string, clave: string): Promise<LoginResponse> {
    return Http.post<LoginResponse>("/login", { correo, clave }) as Promise<LoginResponse>;
  }

  static logout(jwt: string) {
    return Http.post<undefined>("/logout", {}, { jwt });;
  }

  static perfil(jwt: string) {
    return Http.get<PerfilResponse>("/perfil", { jwt } );
  }
}