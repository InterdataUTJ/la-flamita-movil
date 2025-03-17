import { ScrollView, StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { LineChart, lineDataItem } from 'react-native-gifted-charts'
import { GraficaProps } from './types'
import { SensorDato } from '@/services/Sensores/types'

export default function Grafica(props: GraficaProps) {

  if (!props.datos) return null;

  const unidad = (typeof props.datos[0].dato !== "string") ? props.datos[0].dato[props.nombre][1] : "";
  const data = props.datos.map((dato: SensorDato) => {
    if (typeof dato.dato === 'string') return dato.dato;
    return {
      value: parseFloat(dato.dato[props.nombre][0]),
      dataPointText: `${dato.dato[props.nombre][0]}${dato.dato[props.nombre][1]}`,
    }
  }) as lineDataItem[];

  return (
    <ScrollView horizontal style={styles.container} contentContainerStyle={styles.contentContainer}>
      <Text style={styles.title}>{props.nombre} ({unidad})</Text>
      <LineChart
        dataPointsColor1='rgb(54, 162, 235)'
        startOpacity1={0.5}
        endOpacity={0.01}
        color='rgb(54, 162, 235)'
        startFillColor='rgb(54, 162, 235)'
        endFillColor='rgb(54, 162, 235)'
        textShiftY={-10}
        textShiftX={-10}
        textFontSize={10}
        textColor='rgb(16, 58, 87)'
        yAxisLabelSuffix={unidad}
        data={data}
        curved
        adjustToWidth
        areaChart
      />
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    borderRadius: 10,
    elevation: 5,
    boxShadow: '0 0 5px rgba(0, 0, 0, 0.1)',
    borderColor: 'rgba(0, 0, 0, 0.1)',
    borderWidth: 2,
    backgroundColor: 'white',
  },

  contentContainer: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },

  title: {
    fontSize: 20,
    marginBottom: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  }
})