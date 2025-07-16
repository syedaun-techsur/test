import axios, { AxiosResponse } from 'axios';

const BASE_URL = 'http://localhost:8080/api';

/**
 * Fetches a hello message from the backend API.
 *
 * @returns A Promise resolving to an object containing a message string.
 * @throws An error if the request fails.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const res: AxiosResponse<{ message: string }> = await axios.get(`${BASE_URL}/hello`);
    return res.data;
  } catch (error) {
    // Optional: Could customize or log the error here before rethrowing
    throw error;
  }
};