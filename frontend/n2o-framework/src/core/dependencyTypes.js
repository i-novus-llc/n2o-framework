export const DEPENDENCY_TYPES = {
    visible: 'visible',
    enabled: 'enabled',
    fetch: 'fetch',
    RE_RENDER: 'reRender',
}

export const DEPENDENCY_ORDER = [
    DEPENDENCY_TYPES.fetch,
    DEPENDENCY_TYPES.visible,
    DEPENDENCY_TYPES.enabled,
]
