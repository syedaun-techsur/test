import axios, { AxiosInstance } from 'axios';

interface HelloResponse {
  message: string;
}

const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000, // set timeout to avoid hanging requests
});

export const fetchHello = async (): Promise<HelloResponse> => {
  try {
    const res = await apiClient.get<HelloResponse>('/hello');
    return res.data;
  } catch (error) {
    // Consider adding logging here if needed
    throw new Error('Failed to fetch hello message from backend');
  }
};