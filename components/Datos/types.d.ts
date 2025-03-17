import { SensorDato } from "@/services/Sensores/types";

export interface DatosProps {
  nombre: string;
  valor: string;
  timestamp: string;
}

export interface GraficaProps {
  nombre: string;
  datos?: SensorDato[];
}