"use server";
import { serverEnv } from "../env/server/env";
import type { ErrorResponse } from "../types";

type resetAPITokenResult = {
  token: string;
  error?: Error;
};

type ResetAPITokenResponse = {
  apiToken: string;
};

export async function resetAPIToken(
  userId: string,
  token: string
): Promise<resetAPITokenResult> {
  console.log("Resetting token for user", userId);
  try {
    const apiUrl = new URL(
      `/user/${userId}/settings/token`,
      serverEnv.BACKEND_API_URL
    );
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const data: ResetAPITokenResponse = await response.json();
      return { token: data.apiToken };
    }

    console.error("Failed to reset token for user ", userId, response);
    throw new Error("Failed to reset token");
  } catch (err) {
    return { token: "", error: err as Error };
  }
}

type ChangePasswordRequest = {
  newPassword: string;
};

export async function changePassword(
  userId: string,
  token: string,
  newPassword: string
): Promise<Error | undefined> {
  console.log("Changing password for user", userId);
  if (!newPassword) {
    return new Error("Password is required");
  }
  if (newPassword.length < 8) {
    return new Error("Password must be at least 8 characters");
  }
  try {
    const apiUrl = new URL(
      `/user/${userId}/settings/password`,
      serverEnv.BACKEND_API_URL
    );

    const requestData: ChangePasswordRequest = { newPassword };
    const response = await fetch(apiUrl, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(requestData),
    });
    if (response.ok) {
      return;
    }

    console.error("Failed to change password for user ", userId, response);
    throw new Error("Failed to change password");
  } catch (err) {
    return err as Error;
  }
  return;
}
