import React, { useState } from 'react';
import { Alert, ScrollView, StyleSheet, Text, View } from 'react-native';
import { Formik, FormikHelpers } from 'formik';
import * as Yup from 'yup';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import colors from '@/config/colors';
import TextInput from '@/components/TextInput';
import Button from '@/components/Button';
import useAuthContext from '@/hooks/AuthContext/hook';
import SelectInput from '@/components/Select';
import SensorService from '@/services/Sensores';

const CreateSchema = Yup.object().shape({
  tipo: Yup.string().trim().required("El nombre es obligatorio."),
  nombre: Yup.string().trim()
            .min(3, "El nombre debe de ser de minimo 3 caracteres.")
            .max(50, "El nombre debe de ser de maximo 50 caracteres.")
            .required("El nombre es obligatorio."),
});
  

interface IFormValues {
  nombre: string;
  tipo: "SENSOR" | "ACTUADOR" | "";
}

export default function Crear() {

  const auth = useAuthContext();
  const [loading, setLoading] = useState(false);

  const handleCreate = async (values: IFormValues, actions: FormikHelpers<IFormValues>) => {
    if (!auth.token || values.tipo === "") return;
    setLoading(true);

    try {
      await SensorService.crear(auth.token, { nombre: values.nombre, tipo: values.tipo });
      setLoading(false);
      actions.resetForm();
      Alert.alert("Dispositivo creado", "Dispositivo creado correctamente");
    } catch(e: Error | unknown) {
      
      if (e instanceof Error) Alert.alert("Ocurrio un error", e.message);
      else Alert.alert("Ocurrio un error", "Ah ocurrido un error al crear el dispositivo.");
      setLoading(false);
    
    }
  }

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Nuevo dispositivo</Text>
    
      <Formik
        initialValues={{ nombre: "", tipo: "SENSOR" }}
        onSubmit={handleCreate}
        validationSchema={CreateSchema}
      >
        {({ handleChange, handleBlur, handleSubmit, values, errors, touched }) => (
          <View style={styles.form}>
            <TextInput 
              label="Nombre"
              onChangeText={handleChange("nombre")}
              onBlur={handleBlur("nombre")}
              value={values.nombre}
              showErrors={Boolean(errors.nombre && touched.nombre && values.nombre !== "")}
              error={errors.nombre}
              required
            />

            <SelectInput
              label="Tipo"
              onChangeText={handleChange("tipo")}
              onBlur={handleBlur("tipo")}
              value={values.tipo}
              showErrors={Boolean(errors.tipo && touched.tipo && values.tipo !== "")}
              error={errors.tipo}
              required
            >
              <SelectInput.Option label="Sensor" value="SENSOR" />
              <SelectInput.Option label="Actuador" value="ACTUADOR" />
            </SelectInput>

            <Button 
              icon={<MaterialCommunityIcons name="pencil" size={15} color="white" />}
              onPress={() => {
                if (values.nombre === "" || values.tipo === "") alert("Por favor, rellene todos los campos.");
                handleSubmit();
              }}
              loading={loading}
            >
              Crear
            </Button>
          </View>
        )}
      </Formik>

    
    </ScrollView>
  )
}


const styles = StyleSheet.create({
  container: {
    marginHorizontal: 20,
    marginVertical: 10,
    backgroundColor: colors.backgroundColor,
  },

  title: {
    fontSize: 40,
    marginBottom: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },

  form: {
    display: "flex",
    gap: 15
  },
  
});