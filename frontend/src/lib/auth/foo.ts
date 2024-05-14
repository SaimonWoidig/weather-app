"use server";
import { serverEnv } from "../env/server/env";

const foo = () => {
  console.log(serverEnv);
  console.log("\n");
};

export { foo };
