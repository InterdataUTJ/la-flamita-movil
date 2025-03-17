import { ScrollView, StyleSheet, Text, View } from 'react-native'
import React, { useEffect, useState } from 'react'
import { useLocalSearchParams } from 'expo-router';
import { LineChart } from 'react-native-gifted-charts';
import { SensorResponse } from '@/services/Sensores/types';
import SensorService from '@/services/Sensores';
import useAuthContext from '@/hooks/AuthContext/hook';
import colors from '@/config/colors';
import Datos from '@/components/Datos';
import Grafica from '@/components/Datos/Grafica';

export default function Dispositivo() {
  const { id } = useLocalSearchParams();
  const auth = useAuthContext();
  const [sensor, setSensor] = useState({} as SensorResponse);

  const updateData = () => {
    if (!auth.token || !id) return;
    SensorService.mostrar(auth.token, id as string).then((sensor) =>
      setSensor(sensor)
    );
  }

  useEffect(() => {
    updateData();
    let interval = setInterval(updateData, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>{sensor.nombre}</Text>
      
      <View style={styles.inputContainer}>
        <Text style={styles.label}>ID</Text>
        <Text style={styles.input} selectable>{id}</Text>
      </View>
      
      <View style={styles.inputContainer}>
        <Text style={styles.label}>Nombre</Text>
        <Text style={styles.input} selectable>{sensor.nombre}</Text>
      </View>
      
      <View style={styles.inputContainer}>
        <Text style={styles.label}>Token</Text>
        <Text style={styles.input} selectable>{sensor.token}</Text>
      </View>


      <Text style={styles.subtitle}>Últimas capturas</Text>
      
      <View style={styles.dataContainer}>
        {!sensor.datos?.length ? <Text>No hay datos</Text> : null}

        {sensor.datos?.length && sensor.datos[sensor.datos?.length - 1]?.dato ? (
          Object.entries(sensor.datos[sensor.datos.length - 1].dato).map(([key, val]) => ( 
            <Datos 
              key={key}
              nombre={key}
              valor={val}
              timestamp={sensor.datos[sensor.datos.length - 1].timestamp}
            />
          ))
        ) : null}
      </View>



      <Text style={styles.subtitle}>Datos capturados</Text>

      <View style={styles.chartContainer}>
        {!sensor.datos?.length ? <Text style={{ textAlign: "center" }}>No hay datos</Text> : null}

        {sensor.datos && sensor.datos[0]?.dato && (
          Object.keys(sensor.datos[0].dato).map((key) => (
            <Grafica 
              key={key}
              nombre={key}
              datos={sensor.datos} 
            />
          ))
        )}

      </View>

    </ScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    marginHorizontal: 20,
    marginVertical: 10,
    backgroundColor: colors.backgroundColor,
  },

  title: {
    fontSize: 40,
    marginBottom: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },

  subtitle: {
    fontWeight: 'bold',
    fontSize: 30,
    marginTop: 20,
    marginBottom: 40,
    paddingBottom: 8,
    borderBottomWidth: 2,
    borderBottomColor: colors.quinary[700],
  },

  inputContainer: {
    marginBottom: 20,
  },

  label: {
    fontSize: 14,
    fontWeight: "bold",
    marginBottom: 5,
  },

  input: {
    display: "flex",
    padding: 10, 
    borderRadius: 8, 
    borderWidth: 1,
    borderColor: "#D1D5DB", 
    width: "100%",
    fontSize: 14,
    lineHeight: 20, 
    color: "#111827",
    backgroundColor: "white",
  },

  dataContainer: {
    flexGrow: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 20,
    flexWrap: 'wrap',
  },

  chartContainer: {
    flexGrow: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 20,
  }
})