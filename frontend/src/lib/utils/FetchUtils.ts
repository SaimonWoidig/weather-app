import {FetchError} from "@lib/types/errors.ts";

export async function fetchAs<T>(input: RequestInfo | URL, init?: RequestInit | undefined): Promise<T> {
    let response: Response;
    try {
        response = await fetch(input, init);
    } catch (e) {
        throw new FetchError(0, e as Error);
    }
    if (!response.ok) {
        let resData;
        try {
            resData = await response.json();
        } catch (e) {
            resData = "failed to parse error response as JSON";
        }
        throw new FetchError(response.status, undefined, resData as string);
    }
    try {
        return await response.json() as Promise<T>;
    } catch (e) {
        throw new FetchError(response.status, e as Error, "request succeeded, but failed to parse response as JSON");
    }
}