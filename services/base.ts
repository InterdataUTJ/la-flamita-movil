
export interface ErrorResponse {
  name?: string;
  message?: string;
}

interface HttpOptions {
  jwt?: string;
  asForm?: boolean;
}

function ApiServiceError({ name, message }: ErrorResponse = {}): Error {
  return new class extends Error {
    name = name || "ApiServiceError";
    message = message || "Error al realizar la petición al servidor.";
  }
}

export default class Http {

  public static baseUrl: string = `${process.env.EXPO_PUBLIC_API_URL}/api`;

  static async get<T>(url: string, options: HttpOptions = {}): Promise<T | undefined> {
    return this.noBodyRequest<T>("get", url, options);
  }

  static async delete<T>(url: string, options: HttpOptions = {}): Promise<T | undefined> {
    return this.noBodyRequest<T>("delete", url, options);
  }

  static async post<T>(url: string, body: object, options: HttpOptions = {}): Promise<T | undefined> {
    return this.bodyRequest<T>("post", url, body, options);
  }

  static async put<T>(url: string, body: object, options: HttpOptions = {}): Promise<T | undefined> {
    return this.bodyRequest<T>("put", url, body, options);
  }


  private static async noBodyRequest<T>(method: string, url: string, options: HttpOptions = {}): Promise<T | undefined> {
    return new Promise<T | undefined>((resolve, reject) => {
      const headers = new Headers();
      if (options.jwt) headers.append("Authorization", `Bearer ${options.jwt}`);

      // Send request
      fetch(`${this.baseUrl}${url}`, { method, headers })
        .then(res => {
          if (!res.ok) return res.json().then((data) => reject(ApiServiceError(data)));
          if (res.headers.get("Content-Type")?.includes("application/json")) return res.json().then(resolve);
          resolve(undefined);

        })
        .catch(() => reject(ApiServiceError()));
    });
  }

  private static async bodyRequest<T>(method: string, url: string, body: object, options: HttpOptions = {}): Promise<T | undefined> {
    return new Promise<T | undefined>((resolve, reject) => {
      const headers = new Headers();
      const formBody = new FormData();
      if (options.jwt) headers.append("Authorization", `Bearer ${options.jwt}`);
      if (!options.asForm) headers.append("Content-Type", "application/json");
      else Object.entries(body).forEach(([key, value]) => formBody.append(key, value));

      // Send request
      fetch(`${this.baseUrl}${url}`, { 
        method,  
        headers,
        body: options.asForm ? formBody : JSON.stringify(body),

      }).then(res => {
        if (!res.ok) return res.json().then((data) => reject(ApiServiceError(data)));
        if (res.headers.get("Content-Type")?.includes("application/json")) return res.json().then(resolve);
        resolve(undefined);

      }).catch(() => reject(ApiServiceError()));
    });
  }
  
}