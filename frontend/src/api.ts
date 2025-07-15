import axios from 'axios';

export const fetchHello = async () => {
  const res = await axios.get('http://localhost:8080/api/hello');
  return res.data;
}; 