type resetAPITokenResult = {
  token: string;
  error?: Error;
};

export async function resetAPIToken(
  token: string
): Promise<resetAPITokenResult> {
  "use server";

  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 1000));

  // TODO: connect to API

  return { token: "some token" };
}

export async function changePassword(
  token: string,
  newPassword: string
): Promise<Error | undefined> {
  "use server";

  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 1000));

  // TODO: connect to API

  return;
}
