export interface SelectInputProps {
  label: string;
  required?: boolean;
  onChangeText?: (text: string) => void;
  onBlur?: (e: NativeSyntheticEvent<TargetedEvent>) => void;
  value?: string;
  showErrors?: boolean;
  error?: string;
  children?: React.ReactNode | React.ReactNode[];
}