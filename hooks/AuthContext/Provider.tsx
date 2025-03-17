import { useEffect, useState } from "react";
import PerfilService from "@/services/Perfil";

import { AuthContextProviderProps, AuthContextState } from "./types";
import storage from './utils/localStorage';
import AuthContext from "./index";

export default function AuthContextProvider({ children }: AuthContextProviderProps) {

  // Save the token and user in the local storage
  const [state, setState] = useState<AuthContextState>(storage.load() as AuthContextState);

  useEffect(() => {
    if (!state.token) return;
    getPerfil(state.token);
  }, [state.token]);

  const getPerfil = async (token: string) => {
    const user = await PerfilService.perfil(token);
    
    if (user?.avatar && process.env.EXPO_PUBLIC_API_URL) 
      user.avatar = user?.avatar.replace("http://localhost:8000", process.env.EXPO_PUBLIC_API_URL);

    setState(prev => storage.save({ ...prev, token, user }));
  }

  // Login function
  const handleLogin = async (correo: string, clave: string) => {
    const { token } = await PerfilService.login(correo, clave);
    await getPerfil(token);
  }


  // Logout function
  const handleLogout = async () => {
    if (!state.token) return;
    try {
      await PerfilService.logout(state.token);
      setState({} as AuthContextState);
      storage.remove();
    } catch(e: Error | unknown) { 
      console.error(e) 
    }
  }

  
  return (
    <AuthContext.Provider value={{
      token: state?.token,
      user: state?.user,
      login: handleLogin,
      logout: handleLogout,
    }}>
      {children}
    </AuthContext.Provider>
  );
}