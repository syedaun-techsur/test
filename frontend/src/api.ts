import axios, { AxiosInstance } from 'axios';

const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
});

export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const response = await apiClient.get('/hello');
    return response.data;
  } catch (error) {
    // Optionally, add better error handling/logging here
    throw error;
  }
};