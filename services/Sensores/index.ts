import Http from "../base";
import { SensorResponse, SensorRequest } from "./types";

export default class SensorService {
  static listar(jwt: string): Promise<SensorResponse[]> {
    return Http.get<SensorResponse[]>("/sensor/listar", { jwt }) as Promise<SensorResponse[]>;
  }

  static crear(jwt: string, sensor: SensorRequest) {
    return Http.post<undefined>("/sensor/crear", sensor, { jwt, asForm: true });
  }

  static editar(jwt: string, id: string, sensor: SensorRequest) {
    return Http.put<undefined>(`/sensor/editar/${id}`, sensor, { jwt, asForm: true });
  }

  static mostrar(jwt: string, id: string): Promise<SensorResponse> {
    return Http.get<SensorResponse>(`/sensor/mostrar/${id}`, { jwt }) as Promise<SensorResponse>;
  }

  static enviar(jwt: string, id: string, dato: string): Promise<undefined> {
    return Http.post<undefined>(`/sensor/enviar/${id}`, { dato }, { jwt });
  }

  static eliminar(jwt: string, id: string) {
    return Http.delete<undefined>(`/sensor/eliminar/${id}`, { jwt });
  }
}