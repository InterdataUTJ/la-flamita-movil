import React, { useState } from "react";
import { StyleSheet, Text, TextInput as RNTextInput, View, NativeSyntheticEvent, TextInputFocusEventData } from "react-native";
import colors from "@/config/colors";
import { TextInputProps } from "./types";

export default function TextInput(props: TextInputProps) {

  const [focused, setFocused] = useState(false);

  const onBlur = (e: NativeSyntheticEvent<TextInputFocusEventData>) => {
    setFocused(false);
    props.onBlur && props.onBlur(e);
  }

  return (
    <View>
      <Text style={styles.label}>
        {props.label}
        {props.required && <Text style={styles.required}>  *</Text>}
      </Text>
      <RNTextInput
        style={{ ...styles.input , ...(focused ? styles.inputFocused : {}), ...(props.error ? styles.inputError : {}) }}
        placeholder={props.label}
        onChangeText={props.onChangeText}
        onBlur={onBlur}
        value={props.value}
        keyboardType={props.keyboardType}
        secureTextEntry={props.secureTextEntry}
        enterKeyHint={props.enterKeyHint || "done"}
        onFocus={() => setFocused(true)}
      />
      {props.showErrors && (
        <Text style={styles.error}>{props.error}</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  label: {
    fontSize: 14,
    fontWeight: "bold",
    marginBottom: 5,
  },

  required: {
    color: colors.red
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

  inputFocused: {
    borderColor: colors.primary[600],
    borderWidth: 2,
    outline: "none",
  },

  inputError: {
    borderColor: colors.red,
    borderWidth: 2,
  },

  error: {
    color: colors.red,
    fontSize: 12,
    marginTop: 5,
    fontWeight: "bold",
  }
});
