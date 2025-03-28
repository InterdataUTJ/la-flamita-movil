import React, { useState } from 'react';
import { Alert, Image, ScrollView, StyleSheet, Text, View } from 'react-native';
import useAuthContext from '@/hooks/AuthContext/hook';
import Button from '@/components/Button';
import { router } from 'expo-router';
import FontAwesome5 from '@expo/vector-icons/FontAwesome5';
import colors from '@/config/colors';

export default function Home() {
  const auth = useAuthContext();
  const [loading, setLoading] = useState(false);

  const handleLogout = async () => {
    try {
      setLoading(true);

      await auth.logout();
      router.replace("/login");
    } catch(e: Error | any) {
      console.error(e);
      if (e instanceof Error) Alert.alert("Ocurrio un error", e.message);
      else Alert.alert("Ocurrio un error", "Ah ocurrido un error al crear el dispositivo.");
    }

    setLoading(false);
  }

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Mi perfil</Text>
      
      <View style={styles.infoContainer}>
        <Image
          source={{ uri: auth.user?.avatar }}
          style={styles.avatar}
        />
        <View>
          <Text style={styles.infoTitle}>{auth.user?.nombre} {auth.user?.apellido}</Text>
          <Text style={styles.infoSubitle}>{auth.user?.rol}</Text>
          <Text style={styles.infoSubitle}>{auth.user?.correo}</Text>
        </View>
      </View>

      <View style={styles.inputContainer}>
        <Text style={styles.label}>ID</Text>
        <Text style={styles.input}>{auth.user?._id}</Text>
      </View>

      <View style={styles.inputContainer}>
        <Text style={styles.label}>Nombre</Text>
        <Text style={styles.input}>{auth.user?.nombre}</Text>
      </View>

      <View style={styles.inputContainer}>
        <Text style={styles.label}>Apellido</Text>
        <Text style={styles.input}>{auth.user?.apellido}</Text>
      </View>

      <View style={styles.inputContainer}>
        <Text style={styles.label}>Correo</Text>
        <Text style={styles.input}>{auth.user?.correo}</Text>
      </View>

      <View style={styles.inputContainer}>
        <Text style={styles.label}>Rol</Text>
        <Text style={styles.input}>{auth.user?.rol}</Text>
      </View>

      <Button
        loading={loading}
        icon={<FontAwesome5 name="user-alt" size={15} color="white" />}
        onPress={handleLogout}
      >
        Cerrar sesión
      </Button>
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    marginHorizontal: 20,
    marginVertical: 10,
    backgroundColor: colors.backgroundColor,
  },

  infoContainer: {
    flexDirection: 'row',
    gap: 20,
    alignItems: 'center',
    marginBottom: 20,
    justifyContent: 'center',
    backgroundColor: colors.quinary[300],
    padding: 20,
    borderRadius: 10,
    elevation: 5,
    boxShadow: '0px 0px 5px rgba(0,0,0,0.5)',
  },

  infoTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 5,
    color: colors.quinary[900],
  },

  infoSubitle: {
    fontSize: 15,
    fontWeight: 'bold',
    marginBottom: 5,
  },

  avatar: {
    width: 80,
    height: 80,
    borderRadius: 50,
    objectFit: 'cover',
    backgroundColor: colors.quinary[500],
    borderColor: colors.quinary[500],
    borderWidth: 2,
    elevation: 5,
    boxShadow: '0px 0px 5px rgba(0,0,0,0.5)',
    
  },

  title: {
    fontSize: 40,
    marginBottom: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },

  inputContainer: {
    marginBottom: 20,
  },

  label: {
    fontSize: 14,
    fontWeight: "bold",
    marginBottom: 5,
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
})