import { ScrollView, StyleSheet, Text, View } from 'react-native'
import React, { useEffect, useState } from 'react'
import { useLocalSearchParams } from 'expo-router';
import Fontisto from '@expo/vector-icons/Fontisto';
import { SensorResponse } from '@/services/Sensores/types';
import SensorService from '@/services/Sensores';
import useAuthContext from '@/hooks/AuthContext/hook';
import colors from '@/config/colors';
import Button from '@/components/Button';
import { formatTimestamp } from '@/components/Datos';

export default function Dispositivo() {
  const { id } = useLocalSearchParams();
    const auth = useAuthContext();
    const [sensor, setSensor] = useState({} as SensorResponse);
    const [loading, setLoading] = useState(false);
  
    const updateData = () => {
      if (!auth.token || !id) return;
      SensorService.mostrar(auth.token, id as string).then((sensor) =>
        setSensor(sensor)
      );
    }

    const handleToggleState = async () => {
      if (!auth.token || !id) return;
      setLoading(true);
      const dato = sensor.datos[0]?.dato === "1" ? "0" : "1";
      
      try {
        await SensorService.enviar(sensor.token, id as string, dato);
        await SensorService.mostrar(auth.token, id as string).then((sensor) =>
          setSensor(sensor)
        );
  
        setLoading(false);
      } catch (error) {
        console.error(error);
        setLoading(false);
      }
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
  
  
        <Text style={styles.subtitle}>Estado actual</Text>
        <View style={styles.dataContainer}>
          <Text style={styles.dataTitle}>{(sensor.datos?.[0].dato as string === "1") ? "Encendido" : "Apagado"}</Text>
          <Text style={styles.dataTimestamp}>{formatTimestamp(sensor.datos?.[0].timestamp)}</Text>
          <Button 
            icon={<Fontisto name="fire" size={18} color="white" />}
            onPress={handleToggleState}
            loading={loading}
          >
            {(sensor.datos?.[0].dato as string === "1") ? "Apagar" : "Encender"}
          </Button>
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
    marginVertical: 20,
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
    gap: 15,
    backgroundColor: "white",
    padding: 20,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#D1D5DB",
    elevation: 2,
    boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.05)",
  },

  dataTitle: {
    fontSize: 20,
    fontWeight: "bold",
    textAlign: "center",
  },

  dataTimestamp: {
    fontSize: 18,
    textAlign: "center",
  }

})