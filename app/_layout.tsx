import { Stack } from "expo-router";
import colors from "@/config/colors";

export default function RootLayout() {
  return (
    <Stack screenOptions={{
      statusBarStyle: "dark",
      statusBarBackgroundColor: colors.backgroundColor,
    }}>
      <Stack.Screen name="index" options={{ headerShown: false }} />
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
    </Stack>
  );
}
