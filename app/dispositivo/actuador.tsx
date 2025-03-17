import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { useLocalSearchParams } from 'expo-router';

export default function Dispositivo() {
  const { id } = useLocalSearchParams();

  return (
    <View>
      <Text>{id} 23</Text>
      <Text>Actuador</Text>
    </View>
  )
}

const styles = StyleSheet.create({})