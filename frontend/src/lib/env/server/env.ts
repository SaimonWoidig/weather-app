import { safeParse } from "valibot";
import { serverEnvSchema, type ServerEnv } from "./schema";

const processEnvParsed = safeParse(serverEnvSchema, process.env);

if (!processEnvParsed.success) {
  console.error(
    "Invalid server environment variables:\n",
    ...processEnvParsed.issues
  );
  throw new Error("Invalid server environment variables");
}

export const serverEnv = processEnvParsed.output as ServerEnv;
