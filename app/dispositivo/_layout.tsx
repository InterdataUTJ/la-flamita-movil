import { Stack } from 'expo-router'
import React from 'react'

export default function DispositivoLayout() {
  return (
    <Stack>
      <Stack.Screen
        name="actuador"
        options={{
          title: 'Actuador',
          presentation: 'modal',
        }}
      />
      <Stack.Screen
        name="sensor"
        options={{
          title: 'Sensor',
          presentation: 'modal',
        }}
      />
    </Stack>
  )
}