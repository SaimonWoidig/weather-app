export class FetchError {
    message: string;
    status: number;
    cause?: Error;

    constructor(status: number, cause?: Error, message: string = "failed to fetch") {
        this.message = message;
        this.status = status;
        this.cause = cause;
    }
}

export async function fetchAs<T>(input: RequestInfo | URL, init?: RequestInit | undefined): Promise<T> {
    let response: Response;
    try {
        response = await fetch(input, init);
    } catch (e) {
        throw new FetchError(0, e as Error);
    }
    if (!response.ok) {
        throw new FetchError(response.status, undefined, await response.json());
    }
    return await response.json() as Promise<T>;
}