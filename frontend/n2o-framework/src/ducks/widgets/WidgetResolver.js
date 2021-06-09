import isNaN from 'lodash/isNaN'

class WidgetResolver {
    static get defaultState() {
        return ({
            isInit: false,
            isEnabled: true,
            isVisible: true,
            isLoading: false,
            isResolved: false,
            selectedId: null,
            isFilterVisible: true,
            isActive: false,
            type: null,
            dataProvider: {},
            sorting: {},
            filter: {
                key: null,
                type: null,
            },
            /* Query props */
            count: 0,
            /* System props */
            pageId: null,
            containerId: null,
            validation: {},
            error: null,
        })
    }

    /**
     * Получение id в формате number
     * @param {string | number} selectedId
     * @return {number}
     */
    static resolveSelectedId(selectedId) {
        if (selectedId !== '' && !isNaN(+selectedId)) {
            selectedId = +selectedId
        }

        return selectedId
    }
}

export default WidgetResolver
