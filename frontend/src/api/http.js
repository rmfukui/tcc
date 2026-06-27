import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  timeout: 15000,
});

export function getErrorMessage(error) {
  return error?.response?.data?.message || 'Nao foi possivel completar a operacao.';
}
