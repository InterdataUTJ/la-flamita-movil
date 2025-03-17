import { Tabs } from "expo-router";
import FontAwesome5 from '@expo/vector-icons/FontAwesome5';
import FontAwesome from '@expo/vector-icons/FontAwesome';
import colors from "@/config/colors";
import Navbar from "@/components/Navbar";

export default function RootLayout() {
  return (
    <>
      <Navbar />
      <Tabs screenOptions={{ tabBarActiveTintColor: colors.primary[600] }}>
        <Tabs.Screen 
          name="index" 
          options={{ 
            title: "Dispositivos", 
            headerShown: false,
            tabBarIcon: ({ color, size }) => <FontAwesome name="th-list" size={size} color={color} /> 
          }} 
        />
        <Tabs.Screen 
          name="crear" 
          options={{ 
            title: "Nuevo", 
            headerShown: false,
            tabBarIcon: ({ color, size }) => <FontAwesome name="plus" size={size} color={color} /> 
          }} 
        />
        
        <Tabs.Screen 
          name="perfil" 
          options={{ 
            title: "Perfil", 
            headerShown: false,
            tabBarIcon: ({ color, size }) => <FontAwesome5 name="user-alt" size={size} color={color} /> 
          }} 
        />
      </Tabs>
    </>
  );
}
