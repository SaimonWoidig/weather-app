import { Title } from "@solidjs/meta";
import { Navigate } from "@solidjs/router";
import { useAuth } from "src/components/AuthProvider";
import WeatherCard from "src/components/WeatherCard";
import WeatherCode from "src/lib/weather/weather_code";

export default function UserArea() {
  const auth = useAuth();

  if (!auth.isLoggedInFn()) {
    return <Navigate href="/login" />;
  }

  return (
    <main>
      <Title>User Area</Title>
      <h1 class="text-4xl font-bold p-2">Your personal forecast</h1>
      <div>
        <WeatherCard
          precipitation={0}
          temperature={11}
          weatherType={WeatherCode.Fog}
        />
      </div>
    </main>
  );
}
