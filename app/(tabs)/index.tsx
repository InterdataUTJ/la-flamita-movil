import { ActivityIndicator, ScrollView, StyleSheet, Text, View } from 'react-native'
import React, { useEffect, useState } from 'react'
import colors from '@/config/colors'
import Dispositivo from '@/components/Dispositivo';
import { SensorResponse } from '@/services/Sensores/types';
import SensorService from '@/services/Sensores';
import useAuthContext from '@/hooks/AuthContext/hook';

export default function Home() {

  const auth = useAuthContext();
  const [dispositivos, setDispositivos] = useState([] as SensorResponse[]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!auth.token) return;
    SensorService.listar(auth.token).then((dispositivos) => {
      setDispositivos(dispositivos);
      setLoading(false);
    });
  });
  
  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Dispositivos</Text>

      <View style={styles.devices}>
        {loading && <ActivityIndicator size="large" color={colors.primary[600]} />}
        
        {dispositivos.map((dispositivo) => (
          <Dispositivo 
            key={dispositivo._id}
            id={dispositivo._id}  
            name={dispositivo.nombre} 
            type={dispositivo.tipo}
          />
        ))}
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

  devices: {
    gap: 20,
  }
});