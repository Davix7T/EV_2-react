export async function http<T>(input: string, init?: RequestInit): Promise<T | null> {
  const baseUrl: string | undefined = (import.meta as any).env?.VITE_API_URL;
  const url: string = input.startsWith("http") ? input : `${baseUrl}${input}`;

  const token: string | null = localStorage.getItem("token");

  const headers: HeadersInit = {
    ...(init && init.headers ? (init.headers as HeadersInit) : {}),
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response: Response = await fetch(url, { ...init, headers });

  if (response.status === 401) {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/";
    return null;
  }

  if (response.status === 204) {
    return null;
  }

  const text: string = await response.text();
  try {
    const data: T = JSON.parse(text);
    if (!response.ok) {
      throw new Error((data as any)?.message || response.statusText);
    }
    return data;
  } catch (err) {
    if (!response.ok) {
      throw new Error(response.statusText || "Request failed");
    }
    return null;
  }
}

export default http;
