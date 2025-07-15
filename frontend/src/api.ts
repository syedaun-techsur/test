import axios, { AxiosResponse } from 'axios';

export const fetchHello = async (): Promise<any> => {
  try {
    const response: AxiosResponse = await axios.get('http://localhost:8080/api/hello');
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch hello message: ${error instanceof Error ? error.message : String(error)}`);
  }
}