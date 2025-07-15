import axios, { AxiosInstance } from 'axios';

const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000, // optional timeout for requests
});

/**
 * Fetches the hello message from the backend API.
 * @returns A promise that resolves to the response data.
 * @throws An error if the request fails.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const res = await apiClient.get<{ message: string }>('/hello');
    return res.data;
  } catch (error) {
    // Optionally you could handle or log the error here before throwing
    throw error;
  }
};