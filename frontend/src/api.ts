import axios, { AxiosInstance } from 'axios';

const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
});

/**
 * Fetches the hello message from the backend API.
 * @returns A promise resolving to the data object containing the message.
 * @throws Error if the request fails.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const response = await apiClient.get('/hello');
    return response.data;
  } catch (error) {
    // Optional: Enhance error handling here as needed, e.g. logging
    throw new Error(
      error instanceof Error ? error.message : 'Failed to fetch hello message'
    );
  }
};