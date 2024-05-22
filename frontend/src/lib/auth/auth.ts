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

export type RegisterResponse = {
  email: string;
  userId: string;
  jwtToken: string;
};

type RegisterRequest = {
  email: string;
  password: string;
};

export async function register(email: string, password: string) {
  console.log("registering user", email);
  if (!email) {
    throw new Error("Email is required");
  }
  if (!password) {
    throw new Error("Password is required");
  }
  if (password.length < 8) {
    throw new Error("Password must be at least 8 characters");
  }

  const apiUrl = new URL("/auth/register", serverEnv.BACKEND_API_URL);
  const requestData: RegisterRequest = { email, password };

  try {
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestData),
    });
    if (response.ok) {
      const registerResponse: RegisterResponse = await response.json();
      console.log("Registered user with email", registerResponse.email);
      return registerResponse;
    }
    if (response.status === 409) {
      const errorResponse: ErrorResponse = await response.json();
      console.log(errorResponse);
      throw new Error(errorResponse.message);
    }
    console.error("Unexpected failure", response);
    throw new Error("Failed to register");
  } catch (err) {
    console.warn("Register failed for user", email, err);
    if (err instanceof Error) {
      throw err;
    }
    console.error("Unexpected failure", err);
    throw new Error("Failed to register");
  }
}
