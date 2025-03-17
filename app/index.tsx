import { Redirect } from "expo-router";
import useAuthContext from "@/hooks/AuthContext/hook";
import { Image, Text, View } from "react-native";


export default function Index() {
  const auth = useAuthContext();

  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <Redirect href={auth.user ? "/(tabs)" : "/login"} />
      <Image 
        source={require("@/assets/images/la_flamita.png")}
        style={{ width: 200, height: 200 }}
      />
    </View>
  );
}

