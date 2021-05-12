export default (condition, message) => {
    if (condition && process.env.NODE_ENV !== 'production') {
        // eslint-disable-next-line no-console
        console.warn(message)
    }
}
