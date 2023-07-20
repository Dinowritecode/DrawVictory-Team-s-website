import axios from "./request";
import {Response} from "./model";

export function getUserRole(): Promise<string> {
    return axios.get<Response<string>>('/api/user/role')
        .then(response => response.data.data || '')
        .catch(error => {
            console.error(error);
            return '';
        });
}

export function getUserPermissions(): Promise<string[]> {
    return axios.get<Response<string[]>>('/api/user/permissions')
        .then(response => response.data.data || [])
        .catch(error => {
            console.error(error);
            return [];
        });
}

export function getLocalToken(): string | null {
    return localStorage.getItem('token');
}

export function removeLocalToken(): void {
    localStorage.removeItem('token');
}