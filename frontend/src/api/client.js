import axios from "axios";
import { API_BASE_URL } from "../constants/config";

export const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    "Content-Type": "application/json"
  }
});
