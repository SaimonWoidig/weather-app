/// <reference types="vitest" />
import {getViteConfig} from 'astro/config';

export default getViteConfig({
    test: {
        name: "weather-app-frontend",
        watch: false,
        coverage: {
            provider: "v8",
            reporter: [
                "text",
                "cobertura",
                "html",
            ],
            include: [
                "src/**"
            ],
        }
    }
});