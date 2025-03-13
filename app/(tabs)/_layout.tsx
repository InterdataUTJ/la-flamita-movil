import { Tabs } from "expo-router";
import Entypo from '@expo/vector-icons/Entypo';
import colors from "@/config/colors";

export default function RootLayout() {
  return (
    <Tabs screenOptions={{ tabBarActiveTintColor: colors.primary[600] }}>
      <Tabs.Screen name="index" options={{ 
        title: "Inicio", 
        headerShown: false,
        tabBarIcon: ({ color, size }) => <Entypo name="home" size={size} color={color} /> 
      }} 
      />
    </Tabs>
  );
}
