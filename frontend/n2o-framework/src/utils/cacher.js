const cachingStore = () => {
    const cache = {}

    return {
        add(params, result) {
            cache[JSON.stringify(params)] = result
        },

        find(params) {
            const key = JSON.stringify(params)

            // eslint-disable-next-line no-prototype-builtins
            if (cache.hasOwnProperty(key)) {
                return cache[key]
            }

            return false
        },
    }
}

export default cachingStore()
