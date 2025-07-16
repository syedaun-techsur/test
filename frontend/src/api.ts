import axios, { AxiosInstance } from 'axios';

const BASE_URL = 'http://localhost:8080/api';

const apiClient: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000, // added timeout for better UX and error handling
});

/**
 * Fetches a greeting message from the backend API.
 * @returns A Promise resolving to the data object containing the message.
 * @throws Will throw an error if the HTTP request fails.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const response = await apiClient.get('/hello');
    return response.data;
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      // Optionally, you can enhance error handling here
      throw new Error(error.response?.data?.message || error.message);
    }
    throw new Error('An unexpected error occurred');
  }
};