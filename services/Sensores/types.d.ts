export interface SensorDato {
  _id: string;
  dato: { [key: string]: string[] } | string;
  timestamp: string;
}

export interface SensorResponse {
  _id: string;
  estado: boolean;
  datos: SensorDato[];
  nombre: string;
  tipo: "SENSOR" | "ACTUADOR";
  token: string;
}

export interface SensorRequest {
  nombre?: string;
  tipo?: "SENSOR" | "ACTUADOR";
}