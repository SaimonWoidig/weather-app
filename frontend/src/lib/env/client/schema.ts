import { object, optional, picklist, type Output } from "valibot";

export const clientEnvSchema = object({
  MODE: optional(
    picklist(["development", "production", "test"]),
    "development"
  ),
});

export type ClientEnv = Output<typeof clientEnvSchema>;
