import React, { useState } from "react";
import { Picker } from '@react-native-picker/picker';
import { StyleSheet, Text, View, NativeSyntheticEvent, TargetedEvent, Platform } from "react-native";
import colors from "@/config/colors";
import { SelectInputProps } from "./types";

export default function SelectInput(props: SelectInputProps) {

  const [focused, setFocused] = useState(false);

  const onBlur = (e: NativeSyntheticEvent<TargetedEvent>) => {
    setFocused(false);
    props.onBlur && props.onBlur(e);
  }

  return (
    <View>
      <Text style={styles.label}>
        {props.label}
        {props.required && <Text style={styles.required}>  *</Text>}
      </Text>
      <View style={{ ...styles.input, ...(focused ? styles.inputFocused : {}), ...(props.showErrors ? styles.inputError : {}) }}>
        <Picker
          onBlur={onBlur}
          style={styles.select}
          selectedValue={props.value}
          onValueChange={(itemValue, _) => props.onChangeText && props.onChangeText(itemValue)}
          onFocus={() => setFocused(true)}
        >
          {props.children}
        </Picker>
      </View>
      {props.showErrors && (
        <Text style={styles.error}>{props.error}</Text>
      )}
    </View>
  );
}

SelectInput.Option = Picker.Item;
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
    padding: 0,
    overflow: "hidden",
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
  },

  select: {
    padding: Platform.OS === "web" ? 10 : 0,
    backgroundColor: '#fff',
    borderWidth: 0,
  }
});
