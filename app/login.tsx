import { ScrollView, Text, View, Image, StyleSheet, Alert } from "react-native";
import FontAwesome5 from '@expo/vector-icons/FontAwesome5';
import { Formik } from "formik";
import * as Yup from "yup";
import TextInput from "@/components/TextInput";
import Button from "@/components/Button";
import colors from "@/config/colors";
import { useRouter } from "expo-router";
import useAuthContext from "@/hooks/AuthContext/hook";
import { useState } from "react";

const LoginSchema = Yup.object().shape({
  correo: Yup.string().email("El correo debe de ser valido.").required("El correo es obligatorio."),
  clave: Yup.string().min(8, "La contraseña debe de ser minimo de 8 caracteres.").required("La contraseña es obligatoria."),
});
  

export default function Login() {
  const router = useRouter();
  const auth = useAuthContext();
  const [loading, setLoading] = useState(false);

  const handleLogin = ({ correo, clave }: { correo: string, clave: string }) => {
    setLoading(true);
    auth.login(correo, clave)
      .then(() => {
        setLoading(false);
        router.push("/(tabs)");
      })
      .catch((e) => {
        if (e instanceof Error) Alert.alert("Ocurrio un error", e.message);
        else Alert.alert("Ocurrio un error", "Ah ocurrido un error al crear el dispositivo.");
        setLoading(false);
      });
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Image
        source={require("@/assets/images/la_flamita.png")}
        style={styles.image}
      />
      <Text style={styles.title}>Iniciar sesión</Text>
      
      <Formik
        initialValues={{ correo: "", clave: "" }}
        onSubmit={handleLogin}
        validationSchema={LoginSchema}
      >
        {({ handleChange, handleBlur, handleSubmit, values, errors, touched }) => (
          <View style={styles.form}>
            <TextInput 
              label="Correo"
              onChangeText={handleChange("correo")}
              onBlur={handleBlur("correo")}
              value={values.correo}
              keyboardType="email-address"
              showErrors={Boolean(errors.correo && touched.correo)}
              error={errors.correo}
              required
            />

            <TextInput 
              label="Contraseña"
              onChangeText={handleChange("clave")}
              onBlur={handleBlur("clave")}
              value={values.clave}
              showErrors={Boolean(errors.clave && touched.clave)}
              error={errors.clave}
              secureTextEntry
            />

            <Button 
              loading={loading}
              icon={<FontAwesome5 name="user-alt" size={15} color="white" />}
              onPress={() => handleSubmit()}
            >
              Iniciar sesión
            </Button>
          </View>
        )}
      </Formik>
      
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    width: "100%",
    padding: 20,
    gap: 20,
    backgroundColor: colors.backgroundColor,
  },

  image: {
    width: 200,
    height: 200,
    marginHorizontal: "auto",
  },

  title: {
    fontSize: 40,
    fontWeight: "bold",
    textAlign: "center",
  },

  form: {
    display: "flex",
    gap: 15
  },
});
