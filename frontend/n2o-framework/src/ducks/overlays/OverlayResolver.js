class OverlayResolver {
    /**
     * Стейт модалки по умолчанию
     * @return {{showPrompt: boolean, mode: string, visible: boolean, name: null, props: {}}}
     */
    static get defaultState() {
        return ({
            visible: false,
            name: null,
            showPrompt: false,
            mode: 'modal',
            props: {},
        })
    }

    /**
     * Получить индекс оверлея из массива
     * @param {any[]} state
     * @param {string} name
     * @return {number}
     */
    static findIndexByNameInArray(state, name) {
        return state.findIndex(
            overlay => overlay.name === name,
        )
    }
}

export default OverlayResolver
