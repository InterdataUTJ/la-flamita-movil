import { Image, StyleSheet, Text, View } from 'react-native'
import React from 'react'

export default function Navbar() {
  return (
    <View style={styles.container}>
      <Image
        source={require("@/assets/images/la_flamita.png")}
        style={styles.image}
      />
      <Text style={styles.title}>La flamita</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
    elevation: 5,
    boxShadow: '0px 0px 5px rgba(0,0,0,0.5)',
    borderBottomColor: '#e4e4e4',
    borderBottomWidth: 2,
    padding: 10,
    gap: 10,
  },

  image: {
    width: 40,
    height: 40,
  },

  title: {
    fontSize: 20,
    fontWeight: 'bold',
  }
})