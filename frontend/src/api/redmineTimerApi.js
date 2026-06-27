import { api } from './http';

export async function validateRedmine(payload) {
  const { data } = await api.post('/auth/validate', payload);
  return data;
}

export async function listTasks() {
  const { data } = await api.get('/tasks');
  return data;
}

export async function startTimer(payload) {
  const { data } = await api.post('/timer/start', payload);
  return data;
}

export async function pauseTimer() {
  const { data } = await api.post('/timer/pause');
  return data;
}

export async function resumeTimer() {
  const { data } = await api.post('/timer/resume');
  return data;
}

export async function finishTimer(payload) {
  const { data } = await api.post('/timer/finish', payload);
  return data;
}

export async function listHistory() {
  const { data } = await api.get('/history');
  return data;
}
