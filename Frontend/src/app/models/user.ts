export interface User {
  id?: number;
  username: string;
  roles?: Role[];
}

export interface Role {
  id: number;
  name: string;
}
