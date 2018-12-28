class RequestError extends Error {
  constructor(message, status, body = null) {
    super(message);
    this.name = 'RequestError';
    this.message = message;
    this.status = status;
    this.body = body;
    if (Error.captureStackTrace) {
      Error.captureStackTrace(this, this.constructor);
    } else {
      this.stack = new Error(message).stack;
    }
  }
}

export default RequestError;
