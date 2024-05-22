import { VsError } from "solid-icons/vs";
import { ErrorBoundary, Suspense, type Component } from "solid-js";
import CurrentWeatherForm from "./CurrentWeatherForm";

const CurrentWeather: Component = () => {
  return (
    <div class="flex flex-col gap-2">
      <ErrorBoundary
        fallback={(error) => {
          return (
            <div class="alert alert-error shadow-lg w-max">
              <VsError />
              <span class="pl-2">
                Failed to load weather: {error?.message || "Unexpected error"}
              </span>
            </div>
          );
        }}
      >
        <Suspense
          fallback={<span class="loading loading-dots loading-lg"></span>}
        >
          <CurrentWeatherForm />
        </Suspense>
      </ErrorBoundary>
    </div>
  );
};

export default CurrentWeather;
