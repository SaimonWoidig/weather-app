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
    console.log(apiUrl);
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

export async function changePassword(
  token: string,
  newPassword: string
): Promise<Error | undefined> {
  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 1000));

  // TODO: connect to API

  return;
}
