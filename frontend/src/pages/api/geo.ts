import type {APIRoute} from "astro";
import {getGeoIP} from "@lib/services/geo-api/geoApiService.ts";

export const GET: APIRoute = async ({clientAddress}) => {
    try {
        const data = await getGeoIP(clientAddress);
        return new Response(JSON.stringify(data));
    } catch (e) {
        console.error("got error from backend: ", e);
        return new Response(JSON.stringify({message: "internal server error"}), {status: 500});
    }
}