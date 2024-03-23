import {fetchAs, FetchError} from "src/utils/FetchUtils";

const apiUrl: string = import.meta.env.API_URL;

interface GeoIPApiResponse {
    latitude: number;
    longitude: number;
}

export interface Geo {
    latitude: number;
    longitude: number;
    fallbackUsed: boolean;
}

export async function getGeoIP(ip: string): Promise<Geo> {
    const url: string = `${apiUrl}/geo/ip?ip=${ip}`;

    let fallbackUsed: boolean = false;

    const response = await fetchAs<GeoIPApiResponse>(url).catch(async (e: FetchError) => {
        console.log("failed to fetch for ip, trying fallback;", "cause was \n", e);

        try {
            const fallback = await fetchAs<GeoIPApiResponse>(`${apiUrl}/geo/fallback`);
            fallbackUsed = true;
            return fallback;
        } catch (e) {
            throw new Error("failed to fetch fallback API", {cause: e});
        }
    });

    if (!response) {
        throw new Error('Failed to fetch GeoIP data');
    }

    return {latitude: response.latitude, longitude: response.longitude, fallbackUsed: fallbackUsed};
}