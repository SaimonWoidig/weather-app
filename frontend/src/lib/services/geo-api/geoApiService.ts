import {fetchAs} from "@lib/utils/FetchUtils.ts";
import {FetchError} from "@lib/types/errors.ts";

const apiUrl: string = import.meta.env.API_URL;

type GeoIPApiResponse = {
    latitude: number;
    longitude: number;
}

type Geo = {
    latitude: number;
    longitude: number;
    fallbackUsed: boolean;
}

export async function getGeoIP(ip: string): Promise<Geo> {
    const url: string = `${apiUrl}/geo/ip?ip=${ip}`;

    try {
        const response = await fetchAs<GeoIPApiResponse>(url)
        return {latitude: response.latitude, longitude: response.longitude, fallbackUsed: false};
    } catch (e: unknown) {
        if (e instanceof FetchError) {
            try {
                const fallback = await fetchAs<GeoIPApiResponse>(`${apiUrl}/geo/fallback`);
                return {latitude: fallback.latitude, longitude: fallback.longitude, fallbackUsed: true};
            } catch (e) {
                if (e instanceof FetchError) {
                    throw new Error("failed to fetch fallback Geo API", {cause: e});
                }
            }
        }
        throw new Error("failed to fetch Geo API", {cause: e});
    }
}