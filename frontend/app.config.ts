import { defineConfig } from "@solidjs/start/config";

export default defineConfig({
  server: {},
  vite: {
    build: {
      target: "esnext",
    },
    resolve: {
      alias: {
        src: "/src",
      },
    },
  },
});
