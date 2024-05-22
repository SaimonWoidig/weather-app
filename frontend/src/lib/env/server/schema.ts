import {
  minLength,
  object,
  optional,
  picklist,
  string,
  url,
  type Output,
} from "valibot";

export const serverEnvSchema = object({
  NODE_ENV: optional(
    picklist(["development", "production", "test"]),
    "development"
  ),
  BACKEND_API_URL: string([minLength(1), url()]),
});

export type ServerEnv = Output<typeof serverEnvSchema>;
