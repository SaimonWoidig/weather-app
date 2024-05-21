"use server";

import { serverEnv } from "../env/server/env";
import type { ErrorResponse } from "../types";

export type LoginResponse = {
  email: string;
  userId: string;
  jwtToken: string;
};

type LoginRequest = {
  email: string;
  password: string;
};

export async function login(email: string, password: string) {
  console.log("logging in user", email);
  if (!email) {
    throw new Error("Email is required");
  }
  if (!password) {
    throw new Error("Password is required");
  }

  const apiUrl = new URL("/auth/login", serverEnv.BACKEND_API_URL);
  const requestData: LoginRequest = { email, password };

  try {
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestData),
    });
    if (response.ok) {
      const loginResponse: LoginResponse = await response.json();
      return loginResponse;
    }
    if (response.status === 401) {
      const errorResponse: ErrorResponse = await response.json();
      console.log(errorResponse);
      throw new Error(errorResponse.message);
    }
    console.error("Unexpected failure", response);
    throw new Error("Failed to login");
  } catch (err) {
    console.warn("Login failed for user", email, err);
    if (err instanceof Error) {
      throw err;
    }
    console.error("Unexpected failure", err);
    throw new Error("Failed to login");
  }
}
