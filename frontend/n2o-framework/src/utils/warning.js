const { N2O_WARNING_WATCHER = false } = window

export default (condition, message) => {
    if (!condition) {
        return
    }

    if (process.env.NODE_ENV !== 'production' || N2O_WARNING_WATCHER) {
        // eslint-disable-next-line no-console
        console.warn(message)
    }
}
