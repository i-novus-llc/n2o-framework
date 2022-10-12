import findIndex from 'lodash/findIndex'
import find from 'lodash/find'

class FiltersCache {
    constructor() {
        this.cache = []
    }

    get getCache() {
        return this.cache.slice()
    }

    getCacheByPageId(pageId) {
        return find(this.cache, c => c.pageId === pageId)
    }

    addCache(pageId, query) {
        this.cache.push({ pageId, query })
    }

    clearFromPage(pageId) {
        this.cache.splice(this.getCacheIndex(pageId) + 1)
    }

    getCacheIndex(pageId) {
        return findIndex(this.cache, c => c.pageId === pageId)
    }

    setQueryTo(pageId, query) {
        this.cache[this.getCacheIndex(pageId)].query = { ...query }
    }

    checkCacheExists(pageId) {
        return !!find(this.cache, c => c.pageId === pageId)
    }
}

export default new FiltersCache()
