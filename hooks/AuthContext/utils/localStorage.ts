import { Platform } from 'react-native';
import * as SecureStore from 'expo-secure-store';
import { AuthContextState } from '../types';
const LOCAL_STORAGE_KEY = 'la-flamita-admin_perfil_data';

function load() {
  if (Platform.OS === 'web') {
    const storageString = localStorage.getItem(LOCAL_STORAGE_KEY);
    return storageString ? JSON.parse(storageString) : {};
  }

  const storageString = SecureStore.getItem(LOCAL_STORAGE_KEY);
  return storageString ? JSON.parse(storageString) : {};
}

function save(data: AuthContextState) {
  if (Platform.OS === 'web') {
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(data));
  } else {
    SecureStore.setItem(LOCAL_STORAGE_KEY, JSON.stringify(data));
  }
  
  return data;
}

function remove() {
  if (Platform.OS === 'web') {
    localStorage.removeItem(LOCAL_STORAGE_KEY);
    return;
  }

  SecureStore.deleteItemAsync(LOCAL_STORAGE_KEY);
}

export default { load, save, remove };