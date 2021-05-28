/**
 * Двухключевой кеш для хранения, оборачивающих в SecurityCheck, ХОК'ов
 * необходим в FactoryProvider, чтобы не создавать замыкания на каждый ререндер родительского компонента,
 * которые приводят к тому что оборачиваемый компонент из-за этого каждый раз убирался из DOM
 * и вставлялся обратно уже с новым замыканием на тот же конфиг
 */
export class ComponentCache {
    /**
     * @type {WeakMap<function, WeakMap<object, function>>}
     */
    cache = new WeakMap()

    /**
     * @param {function} component
     * @param {object} config
     * @param {function} wrapper
     */
    set(component, config, wrapper) {
        if (!this.cache.has(component)) {
            this.cache.set(component, new WeakMap())
        }
        this.cache.get(component).set(config, wrapper)
    }

    /**
     * @param {function} component
     * @param {object} config
     * @return {function|void}
     */
    get(component, config) {
        const componentCache = this.cache.get(component)

        if (!componentCache) {
            return
        }

        // eslint-disable-next-line consistent-return
        return componentCache.get(config)
    }

    /**
     * @param {function} component
     * @param {object} config
     * @return {boolean}
     */
    has(component, config) {
        return this.cache.has(component) && this.cache.get(component).has(config)
    }
}
