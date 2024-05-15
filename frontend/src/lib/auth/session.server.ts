"use server";

import { action, cache, redirect } from "@solidjs/router";
import { useSession } from "vinxi/http";
import { serverEnv } from "../env/server/env";

async function dbGetUser(username: string) {
  //simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 1000));
  if (username === "admin@example.com") {
    return { id: 1, email: "admin@example.com", password: "password" };
  }
  throw new Error("User not found");
}

type UserSession = {
  userId?: number;
};

async function getSession() {
  return useSession({
    password: serverEnv.SESSION_SECRET,
  });
}

export const getUser = cache(async () => {
  try {
  } catch {}
}, "user");

export const login = action(async (username: string, password: string) => {
  console.log("login", username, password);
  // do validation
  try {
    const session = await getSession();
    const user = await dbGetUser(username);
    if (!user || password !== user.password) return new Error("Invalid login");
    await session.update((d: UserSession) => (d.userId = user.id));
  } catch (err) {
    console.error(err);
    return err as Error;
  }
  console.log("logged in");
  return redirect("/");
});

export const logout = action(async () => {
  const session = await getSession();
  await session.update((d: UserSession) => (d.userId = undefined));
  return redirect("/login");
});
