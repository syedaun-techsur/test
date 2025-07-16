import axios from 'axios';

/**
 * Fetches greeting message from backend API.
 * @returns {Promise<{ message: string }>} The response data containing the greeting message.
 * @throws Will throw an error if the request fails.
 */
export const fetchHello = async (): Promise<{ message: string }> => {
  try {
    const { data } = await axios.get<{ message: string }>('/api/hello');
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      // Provide more detailed error if response exists
      throw new Error(
        error.response?.data?.message || `Failed to fetch hello message: ${error.message}`
      );
    } else {
      // Non-Axios error fallback
      throw new Error('An unexpected error occurred while fetching hello message.');
    }
  }
};