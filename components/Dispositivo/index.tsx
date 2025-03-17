import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React from 'react';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import colors from '@/config/colors';
import { DispositivoProps } from './types';
import { useRouter } from 'expo-router';


export default function Dispositivo(props: DispositivoProps) {
  const router = useRouter();

  const handlePress = () => {
    const path = props.type === 'SENSOR' ? '/dispositivo/sensor' : '/dispositivo/actuador';
    router.push({ pathname: path, params: { id: props.id } });
  }

  return (
    <TouchableOpacity style={styles.container} onPress={handlePress}>
      <View style={styles.icon}>
        {
          props.type === 'SENSOR' 
          ? (<MaterialIcons name="sensors" size={24} color="white" />)
          : (<MaterialCommunityIcons name="led-variant-outline" size={24} color="white" />)
        }
      </View>
      <Text style={styles.name}>{props.name}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    padding: 20,
    borderRadius: 10,
    backgroundColor: colors.quinary[100],
    borderColor: colors.quinary[600],
    borderWidth: 1,
    elevation: 10,
    boxShadow: '4px 4px 10px rgba(0, 0, 0, 0.1)',
    flexDirection: 'row',
    alignItems: 'center',
  },

  icon: {
    padding: 10,
    borderRadius: 100,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 10,
    backgroundColor: colors.primary[600],
  },

  name: {
    fontSize: 20,
    fontWeight: 'bold',
  },
})