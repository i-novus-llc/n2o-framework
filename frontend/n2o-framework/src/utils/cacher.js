const cachingStore = () => {
    const cache = {}

    return {
        add(params, result) {
            cache[JSON.stringify(params)] = result
        },

        find(params) {
            const key = JSON.stringify(params)

            if (cache.hasOwnProperty(key)) {
                return cache[key]
            }

            return false
        },
    }
}

export default cachingStore()
