import findIndex from 'lodash/findIndex'
import find from 'lodash/find'

class FiltersCache {
    private readonly cache: Array<{ pageId: string, query: object | null }>

    constructor() {
        this.cache = []
    }

    get getCache() {
        return this.cache.slice()
    }

    public getCacheByPageId(pageId: string) {
        return find(this.cache, c => c.pageId === pageId)
    }

    public addCache(pageId: string, query: object | null) {
        this.cache.push({ pageId, query })
    }

    public clearFromPage(pageId: string) {
        this.cache.splice(this.getCacheIndex(pageId) + 1)
    }

    public getCacheIndex(pageId: string): number {
        return findIndex(this.cache, c => c.pageId === pageId)
    }

    public setQueryTo(pageId: string, query: object) {
        this.cache[this.getCacheIndex(pageId)].query = { ...query }
    }

    public checkCacheExists(pageId: string): boolean {
        return !!find(this.cache, c => c.pageId === pageId)
    }
}

export default new FiltersCache()
