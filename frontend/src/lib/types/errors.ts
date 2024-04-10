export class FetchError extends Error {
    message: string;
    status: number;
    cause?: Error;

    constructor(status: number, cause?: Error, message: string = "failed to fetch") {
        super();
        this.message = message;
        this.status = status;
        this.cause = cause;
    }
}