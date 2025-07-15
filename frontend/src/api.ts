import axios, { AxiosError } from 'axios';

export const fetchHello = async (): Promise<any> => {
  try {
    const response = await axios.get('http://localhost:8080/api/hello');
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      // Handle Axios-specific errors
      console.error('Axios error:', error.message);
      // Optionally, rethrow or handle the error as needed
    } else {
      // Handle non-Axios errors
      console.error('Unexpected error:', error);
    }
    throw error; // Rethrow the error after logging
  }
};