import { safeParse } from "valibot";
import { clientEnvSchema, type ClientEnv } from "./schema";

const importMetaEnvParsed = safeParse(clientEnvSchema, import.meta.env);

if (!importMetaEnvParsed.success) {
  console.error(
    "Invalid client environment variables:\n",
    ...importMetaEnvParsed.issues
  );
  throw new Error("Invalid client environment variables");
}

export const clientEnv = importMetaEnvParsed.output as ClientEnv;
