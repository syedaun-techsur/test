import axios, { AxiosResponse } from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // Can be used to centralize base URL configuration
});

/**
 * Fetches greeting message from backend API.
 * @returns Promise resolving to the response data containing the message.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const res: AxiosResponse<{ message: string }> = await apiClient.get('/api/hello');
    return res.data;
  } catch (error) {
    // Customize error handling as needed (could log or rethrow custom error)
    throw new Error('Failed to fetch greeting from backend');
  }
};