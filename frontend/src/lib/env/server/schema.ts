import {
  minLength,
  object,
  optional,
  picklist,
  string,
  type Output,
} from "valibot";

export const serverEnvSchema = object({
  NODE_ENV: optional(
    picklist(["development", "production", "test"]),
    "development"
  ),
  SESSION_SECRET: string([minLength(32)]),
});

export type ServerEnv = Output<typeof serverEnvSchema>;
