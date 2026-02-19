export const normalizeToArray = (key?: string | string[] | null): string[] => {
    if (key == null) { return [] }

    return Array.isArray(key) ? key : [key]
}

export const getNewActiveKeys = (activeKeys: string[], key: string, accordion?: boolean): string[] => {
    if (accordion) {
        return activeKeys.includes(key) ? [] : [key]
    }
    if (activeKeys.includes(key)) {
        return activeKeys.filter(k => k !== key)
    }

    return [...activeKeys, key]
}
