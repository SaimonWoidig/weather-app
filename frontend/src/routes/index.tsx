import { Title } from "@solidjs/meta";
import CurrentWeather from "src/components/CurrentWeather";

export default function Home() {
  return (
    <main>
      <Title>Home</Title>
      <h1 class="text-4xl font-bold p-2">Current weather</h1>
      <CurrentWeather />
    </main>
  );
}
