import axios, { AxiosResponse } from 'axios';

interface HelloResponse {
  message: string;
}

/**
 * Fetches a greeting message from the backend API.
 * @returns A promise resolving to the greeting message.
 */
export const fetchHello = async (): Promise<HelloResponse> => {
  try {
    const { data }: AxiosResponse<HelloResponse> = await axios.get('http://localhost:8080/api/hello');
    return data;
  } catch (error) {
    // Wrap and rethrow the error to provide context
    throw new Error(`Failed to fetch hello message: ${error instanceof Error ? error.message : String(error)}`);
  }
};