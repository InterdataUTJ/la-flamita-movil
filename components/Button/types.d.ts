import { GestureResponderEvent } from "react-native"

export interface ButtonProps {
  children?: React.ReactNode;
  onPress?: (e: GestureResponderEvent) => void;
  icon?: React.ReactNode;
  disabled?: boolean;
  loading?: boolean;
}