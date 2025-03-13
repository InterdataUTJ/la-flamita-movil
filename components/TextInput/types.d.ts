export interface TextInputProps {
  label: string;
  required?: boolean;
  onChangeText?: (text: string) => void;
  onBlur?: (e: NativeSyntheticEvent<TextInputFocusEventData>) => void;
  value?: string;
  keyboardType?: KeyboardTypeOptions;
  secureTextEntry?: boolean;
  showErrors?: boolean;
  error?: string;
  enterKeyHint?: 'enter' | 'done' | 'next' | 'previous' | 'search' | 'send';
}