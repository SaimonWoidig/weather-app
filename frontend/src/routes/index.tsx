import { Title } from "@solidjs/meta";
import WeatherCard from "src/components/WeatherCard";
import WeatherCode from "src/lib/weather/weather_code";

export default function Home() {
  return (
    <main>
      <Title>Home</Title>
      <h1 class="text-4xl font-bold p-2">Current weather</h1>
      <div class="flex items-center justify-center">
        <WeatherCard
          precipitation={15}
          temperature={20}
          weatherType={WeatherCode.DrizzleModerate}
        />
      </div>
    </main>
  );
}
