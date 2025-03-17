import { Stack } from "expo-router";
import AuthContextProvider from "@/hooks/AuthContext/Provider";

export default function RootLayout() {
  return (
    <AuthContextProvider>
      <Stack screenOptions={{
        statusBarStyle: "dark",
        statusBarBackgroundColor: "white",
      }}>
        <Stack.Screen name="index" options={{ headerShown: false }} />
        <Stack.Screen name="login" options={{ headerShown: false }} />
        <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
        <Stack.Screen name="dispositivo" options={{ headerShown: false, presentation: "modal" }} />
      </Stack>
    </AuthContextProvider>
  );
}
