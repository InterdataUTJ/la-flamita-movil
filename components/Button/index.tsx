import { ActivityIndicator, StyleSheet, Text, TouchableOpacity } from 'react-native';
import React from 'react';
import colors from '@/config/colors';
import { ButtonProps } from './types';


export default function Button(props: ButtonProps) {
  return (
    <TouchableOpacity 
      disabled={props.disabled || props.loading}
      style={{ ...styles.button, ...((props.disabled || props.loading) ? styles.buttonDisabled : {}) }}
      onPress={props.onPress}
    >
      {props.loading ? <ActivityIndicator size="small" color="white" /> : props.icon}
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

  buttonDisabled: {
    backgroundColor: colors.primary[300],
  },

  label: {
    marginLeft: 7,
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  }
});