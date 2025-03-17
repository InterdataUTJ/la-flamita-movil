import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { DatosProps } from './types'


export const formatTimestamp = (timestamp?: string) => {
  if (!timestamp) return "";
  try {
    const date = new Date(timestamp);
    return date.toLocaleString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: true,
    });
  } catch {
    return "";
  }
};

export default function Datos(props: DatosProps) {
  return (
    <View style={styles.container}>
      <Text style={styles.nombre}>{props.nombre}</Text>
      <Text style={styles.valor}>{props.valor}</Text>
      <Text style={styles.timestamp}>{formatTimestamp(props.timestamp)}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'white',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 2,
    aspectRatio: 1,
    width: 220,
    padding: 3,
    borderRadius: 15,
    borderWidth: 1,
    borderColor: '#b8b8b8',
    elevation: 2,
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.05)', 
  },

  nombre: {
    fontWeight: 'bold',
    fontSize: 20,
  },

  valor: {
    fontWeight: 'bold',
    fontSize: 50,
    marginBottom: 10,
  },

  timestamp: {
    fontStyle: 'italic',
    fontSize: 17,
  },
})