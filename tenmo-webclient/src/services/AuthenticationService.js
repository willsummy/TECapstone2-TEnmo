import axios from 'axios';

const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {
    login(credentials) {
      return http.post('/login', credentials);
    }
}
