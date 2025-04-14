class RequestError extends Error {
    constructor(message, status, headers, body = null, json = null) {
        super(message)
        this.name = 'RequestError'
        this.message = message
        this.status = status
        this.headers = headers
        this.body = body
        this.json = json
        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, this.constructor)
        } else {
            this.stack = new Error(message).stack
        }
    }

    getMeta() {
        return this.json?.meta
    }
}

export default RequestError
