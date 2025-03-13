import React from 'react'
import colors from '@/config/colors';
import { StyleSheet, Text, TouchableOpacity } from 'react-native'

import { ButtonProps } from './types'

export default function Button(props: ButtonProps) {
  return (
    <TouchableOpacity 
      style={styles.button}
      onPress={props.onPress}
    >
      {props.icon}
      <Text style={styles.label}>{props.children}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  button: {
    display: "flex",
    flexDirection: "row",
    borderRadius: 8, 
    paddingHorizontal: 20,
    paddingVertical: 10,
    textAlign: "center",
    justifyContent: "center",
    alignItems: "center",
    gap: 2,
    backgroundColor: colors.primary[600],
  },

  label: {
    marginLeft: 7,
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  }
});