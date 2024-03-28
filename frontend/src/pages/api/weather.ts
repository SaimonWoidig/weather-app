import type {APIRoute} from "astro";
import {getCurrentWeather} from "@lib/services/weather-api/weatherApiService.ts";

const WeatherTypes = ["current", "forecast", "past"];

export const GET: APIRoute = async ({url}) => {
    const params = Object.fromEntries(url.searchParams);
    const latitude = params.latitude;
    if (latitude === undefined) return new Response(JSON.stringify({message: "missing latitude"}), {status: 400});

    const longitude = params.longitude;
    if (!longitude === undefined) return new Response(JSON.stringify({message: "missing longitude"}), {status: 400});

    const type = params.type;
    if (!type) return new Response(null, {status: 400});
    if (!WeatherTypes.includes(type)) {
        return new Response(JSON.stringify({message: "invalid type"}), {status: 400});
    }

    try {
        const data = await getCurrentWeather(Number(latitude), Number(longitude));
        return new Response(JSON.stringify(data));
    } catch (e) {
        console.error("got error from backend: ", e);
        return new Response(JSON.stringify({message: "internal server error"}), {status: 500});
    }
}